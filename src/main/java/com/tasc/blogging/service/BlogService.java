package com.tasc.blogging.service;

import com.tasc.blogging.aop.ApplicationException;
import com.tasc.blogging.entity.blog.Blog;
import com.tasc.blogging.entity.blog.Comment;
import com.tasc.blogging.entity.blog.Thumbnail;
import com.tasc.blogging.entity.blog.Category;
import com.tasc.blogging.entity.enums.BlogStatus;
import com.tasc.blogging.entity.user.User;
import com.tasc.blogging.model.requset.blog.BlogCreateRequest;
import com.tasc.blogging.model.requset.blog.BlogUpdateRequest;
import com.tasc.blogging.model.response.BasePagingData;
import com.tasc.blogging.model.response.BaseResponse;
import com.tasc.blogging.model.response.blog.BlogDTO;
import com.tasc.blogging.model.response.blog.CategoryDTO;
import com.tasc.blogging.repository.BlogRepository;
import com.tasc.blogging.repository.CategoryRepository;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.tasc.blogging.entity.enums.ERROR;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
public class BlogService {
    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private ThumbnailService thumbnailService;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CommentService commentService;
    @Autowired
    private ModelMapper modelMapper;

    public BlogDTO convertToDTO(Blog blog) {
        return modelMapper.map(blog, BlogDTO.class);
    }

    public List<BlogDTO> convertToDTOList(List<Blog> blogs) {
        return blogs.stream().map(blog -> {
                    BlogDTO blogDto = modelMapper.map(blog, BlogDTO.class);
                    List<CategoryDTO> categoryDtoList = blog.getCategories().stream()
                            .map(category -> modelMapper.map(category, CategoryDTO.class))
                            .collect(Collectors.toList());
                    blogDto.setCategories(categoryDtoList);
                    return blogDto;
                })
                .collect(Collectors.toList());
    }

    @CachePut(value = "blog", key = "#result.data.id")
    public BaseResponse<BlogDTO> createBlog(BlogCreateRequest request, String token) throws ApplicationException {
        log.info("1 - Create blog request: {}", request);
        validateCreateBlogRequest(request);

        log.info("2 - Get user from token");
        User createdBy = commentService.getUserFromAccessToken(token);

        log.info("3 - Find thumbnail by ids: {}", request.getUrls());
        List<Thumbnail> thumbnails = new ArrayList<>();

        for (String url : request.getUrls()) {
            Thumbnail blogThumbnail = Thumbnail.builder()
                    .url(url)
                    .build();
            thumbnails.add(blogThumbnail);
            thumbnailService.createThumbnail(url);
        }

        log.info("4 - Save all thumbnails");


        List<Category> categories = new ArrayList<>();

        for (Long categoryId : request.getCategories()) {
            Category category = categoryRepository.findById(categoryId).orElse(null);
            if (category == null)
                throw new ApplicationException(ERROR.CATEGORY_NOT_FOUND);
            categories.add(category);
        }

        log.info("5 - Create new blog");
        Blog blog = Blog.builder()
                .title(request.getTitle())
                .content(request.getDescription())
                .thumbnails(thumbnails)
                .categories(categories)
                .createdBy(createdBy)
                .status(BlogStatus.PENDING)
                .build();
        blogRepository.save(blog);
        log.info("6 - Create new blog success {}", blog);
        BlogDTO blogDTO = convertToDTO(blog);

        return new BaseResponse<>("Create Blog Success", blogDTO);
    }

//    @Cacheable(value = "blogs", key = "#page + '-' + #size")
    public BaseResponse<BasePagingData<List<BlogDTO>>> findAll(int page, int size) throws ApplicationException {
        log.info("1 - Find all blogs with page {} and size {}", page, size);
        PageRequest pageRequest = PageRequest.of(page, size);

        Page<Blog> pageResult = blogRepository.findAll(pageRequest);

        List<Blog> content = pageResult.getContent();

        List<BlogDTO> blogDTOs = convertToDTOList(content);

        int totalPages = pageResult.getTotalPages();
        int totalItems = (int) pageResult.getTotalElements();
        int currentPage = pageResult.getNumber() + 1;

        return new BaseResponse<>("Find All Success", new BasePagingData<>(currentPage, size, totalPages, totalItems, blogDTOs));
    }

    public BaseResponse<BlogDTO> findById(Long id) throws ApplicationException {
        log.info("1 - Find blog by id: {}", id);
        Optional<Blog> optionalBlog = blogRepository.findById(id);
        if (optionalBlog.isEmpty()) {
            throw new ApplicationException(ERROR.BLOG_NOT_FOUND);
        }
        Blog blog = optionalBlog.get();
        BlogDTO blogDTO = convertToDTO(blog);
        return new BaseResponse<>("Find Blog Success", blogDTO);
    }

    @CachePut(value = "blogs", key = "#id")
    public BaseResponse<BlogDTO> updateStatus(Long id) throws ApplicationException {
        Optional<Blog> optionalBlog = blogRepository.findById(id);
        if (optionalBlog.isEmpty()) {
            throw new ApplicationException(ERROR.BLOG_NOT_FOUND);
        }

        Blog blog = optionalBlog.get();

        if (blog.getStatus() == BlogStatus.PENDING) {
            blog.setStatus(BlogStatus.APPROVED);
        } else {
            blog.setStatus(BlogStatus.PENDING);
        }

        BlogDTO blogDTO = convertToDTO(blog);
        return new BaseResponse<>("Change Blog Status Success", blogDTO);
    }

    @CachePut(value = "blogs", key = "#request.id")
    public BaseResponse<BlogDTO> updateBlog(BlogUpdateRequest request, String token) throws ApplicationException {
        log.info("1 - validate update request: {}", request);
        validateUpdateBlogRequest(request);

        log.info("2 - Find blog by id: {}", request.getId());
        Optional<Blog> optionalBlog = blogRepository.findById(request.getId());

        if (optionalBlog.isEmpty()) {
            log.error("Blog not found");
            throw new ApplicationException(ERROR.BLOG_NOT_FOUND);
        }
        Blog blog = optionalBlog.get();

        User user = commentService.getUserFromAccessToken(token);

        if (!blog.getCreatedBy().getId().equals(user.getId())) {
            log.error("User not authorized");
            throw new ApplicationException(ERROR.USER_NOT_AUTHORIZED);
        }

        List<Category> categories = new ArrayList<>();

        for (Long categoryId : request.getCategories()) {
            Category category = categoryRepository.findById(categoryId).orElse(null);
            if (category == null)
                throw new ApplicationException(ERROR.CATEGORY_NOT_FOUND);
            categories.add(category);
        }

        List<Thumbnail> thumbnails = new ArrayList<>();

        for (String url : request.getUrls()) {
            Thumbnail blogThumbnail = Thumbnail.builder()
                    .url(url)
                    .build();
            thumbnails.add(blogThumbnail);
        }

        log.info("3 - Update blog");
        blog.setTitle(request.getTitle());
        blog.setContent(request.getContent());
        blog.setCategories(categories);
        blog.setThumbnails(thumbnails);
        blog.setStatus(BlogStatus.PENDING);

        log.info("4 - Save blog");
        blogRepository.save(blog);

        BlogDTO blogDTO = convertToDTO(blog);

        return new BaseResponse<>("Update Blog Success", blogDTO);
    }

    public BaseResponse<BlogDTO> likeBlog(Long blogId, String token) throws ApplicationException {
        String isLike = "Like";

        Optional<Blog> optionalBlog = blogRepository.findById(blogId);
        if (optionalBlog.isEmpty()) {
            throw new ApplicationException(ERROR.BLOG_NOT_FOUND);
        }

        Blog blog = optionalBlog.get();

        User user = commentService.getUserFromAccessToken(token);

        if (blog.getLikedUserList().contains(user)) {
            blog.getLikedUserList().remove(user);
            isLike = "Unlike";
        } else {
            blog.getLikedUserList().add(user);
        }

        blogRepository.save(blog);
        return new BaseResponse<>(isLike + " Blog Success", convertToDTO(blog));
    }

    @CacheEvict(value = "blogs", allEntries = true)
    public BaseResponse<String> deleteBlog(Long blogId, String token) throws ApplicationException {
        log.info("1 - Find blog by id: {}", blogId);
        Optional<Blog> optionalBlog = blogRepository.findById(blogId);

        if (optionalBlog.isEmpty()) {
            log.error("Blog not found");
            throw new ApplicationException(ERROR.BLOG_NOT_FOUND);
        }

        Blog blog = optionalBlog.get();

        log.info("2 - Check user is owner of blog");
        User user = commentService.getUserFromAccessToken(token);

        if (!blog.getCreatedBy().getId().equals(user.getId())) {
            log.error("User not authorized");
            throw new ApplicationException(ERROR.USER_NOT_AUTHORIZED);
        }

        for (Comment comment : blog.getComments()) {
            commentService.deleteComment(comment.getId(), token);
        }

        for (Thumbnail thumbnail : blog.getThumbnails()) {
            thumbnailService.deleteThumbnail(thumbnail.getId());
        }

        blogRepository.delete(blog);
        return new BaseResponse<>("Delete Blog Success");
    }

    private void validateCreateBlogRequest(BlogCreateRequest request) throws ApplicationException {
        log.info("1.1 - Validate create blog request title: {}", request.getTitle());
        if (StringUtils.isBlank(request.getTitle())) {
            log.error("Blog title is empty");
            throw new ApplicationException(ERROR.BLOG_TITLE_IS_EMPTY);
        }

        log.info("1.2 - Validate create blog request description: {}", request.getDescription());
        if (StringUtils.isBlank(request.getDescription())) {
            log.error("Blog description is empty");
            throw new ApplicationException(ERROR.BLOG_DESCRIPTION_IS_EMPTY);
        }

        log.info("1.3 - Validate create blog request categories: {}", request.getCategories());
        if (request.getCategories() == null || request.getCategories().isEmpty()) {
            log.error("Blog categories is empty");
            throw new ApplicationException(ERROR.BLOG_CATEGORIES_IS_EMPTY);
        }

        log.info("1.4 - Validate create blog request thumbnails: {}", request.getUrls());
        if (request.getUrls() == null || request.getUrls().isEmpty()) {
            log.error("Blog thumbnails is empty");
            throw new ApplicationException(ERROR.BLOG_THUMBNAILS_IS_EMPTY);
        }
    }

    private void validateUpdateBlogRequest(BlogUpdateRequest request) throws ApplicationException {
        log.info("1.1 - Validate update blog request");
        if (request == null) {
            log.error("Blog request is null");
            throw new ApplicationException(ERROR.BLOG_REQUEST_IS_NULL);
        }

        log.info("1.2 - Validate update blog request id: {}", request.getId());
        if (request.getId() == null) {
            log.error("Blog id is null");
            throw new ApplicationException(ERROR.BLOG_ID_IS_NULL);
        }

        log.info("1.3 - Validate update blog request title: {}", request.getTitle());
        if (StringUtils.isBlank(request.getTitle())) {
            log.error("Blog title is empty");
            throw new ApplicationException(ERROR.BLOG_TITLE_IS_EMPTY);
        }

        log.info("1.4 - Validate update blog request content: {}", request.getContent());
        if (StringUtils.isBlank(request.getContent())) {
            log.error("Blog description is empty");
            throw new ApplicationException(ERROR.BLOG_DESCRIPTION_IS_EMPTY);
        }

        log.info("1.5 - Validate update blog request categories: {}", request.getCategories());
        if (request.getCategories() == null || request.getCategories().isEmpty()) {
            log.error("Blog categories is empty");
            throw new ApplicationException(ERROR.BLOG_CATEGORIES_IS_EMPTY);
        }

        log.info("1.6 - Validate update blog request thumbnails: {}", request.getUrls());
        if (request.getUrls() == null || request.getUrls().isEmpty()) {
            log.error("Blog thumbnails is empty");
            throw new ApplicationException(ERROR.BLOG_THUMBNAILS_IS_EMPTY);
        }
    }
}
