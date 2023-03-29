package com.tasc.blogging.service;

import com.tasc.blogging.aop.ApplicationException;
import com.tasc.blogging.entity.blog.Blog;
import com.tasc.blogging.entity.blog.Comment;
import com.tasc.blogging.entity.enums.CommentStatus;
import com.tasc.blogging.entity.enums.ERROR;
import com.tasc.blogging.entity.user.User;
import com.tasc.blogging.model.requset.blog.CommentCreateRequest;
import com.tasc.blogging.model.requset.blog.CommentUpdateRequest;
import com.tasc.blogging.model.response.BaseResponse;
import com.tasc.blogging.repository.BlogRepository;
import com.tasc.blogging.repository.CommentRepository;
import com.tasc.blogging.repository.UserRepository;
import com.tasc.blogging.util.JWTUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Log4j2
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private UserRepository userRepository;

//    @Value("${JWT_SECRET_KEY}")
//    private String secret;

    public BaseResponse<Comment> createComment(CommentCreateRequest request, String token) throws ApplicationException {
        log.info("1 - Create comment request: {}", request);
        validateCreateCommentRequest(request);

        Optional<Blog> blogOptional = blogRepository.findById(request.getBlogId());

        log.info("2 - Check blog exist");
        if (blogOptional.isEmpty()) {
            log.error("Blog not found");
            throw new ApplicationException("Blog not found");
        }

        Blog blog = blogOptional.get();

        User user = getUserFromAccessToken(token);

        Comment comment = Comment.builder()
                .content(request.getContent())
                .blog(blog)
                .user(user)
                .status(CommentStatus.ACTIVE)
                .build();

        commentRepository.save(comment);
        return new BaseResponse<>("Create comment success", comment);
    }

    public BaseResponse<Comment> updateComment(CommentUpdateRequest request, String token) throws ApplicationException {
        log.info("1 - Validate comment request: {}", request);
        validateUpdateCommentRequest(request);

        Optional<Comment> commentOptional = commentRepository.findById(request.getId());

        log.info("2 - Check comment exist");
        if (commentOptional.isEmpty()) {
            log.error("Comment not found");
            throw new ApplicationException(ERROR.COMMENT_NOT_FOUND);
        }

        Comment comment = commentOptional.get();

        Optional<Blog> blogOptional = blogRepository.findById(request.getBlogId());

        log.info("3 - Check blog exist");
        if (blogOptional.isEmpty()) {
            log.error("Blog not found");
            throw new ApplicationException("Blog not found");
        }

        Blog blog = blogOptional.get();

        log.info("4 - Get user from access token");
        User user = getUserFromAccessToken(token);

        log.info("5 - Check comment exist in blog");
        for (Comment c : blog.getComments()) {
            if (c.getUser() == user) {
                c.setContent(request.getContent());
                commentRepository.save(c);
                break;
            }
        }

        commentRepository.save(comment);
        return new BaseResponse<>("Update comment success", comment);
    }

    public BaseResponse<String> deleteComment(Long id, String token) throws ApplicationException {
        String isSuccess = "Failed";

        if (id == null) {
            log.error("Comment id is null");
            throw new ApplicationException(ERROR.COMMENT_NOT_FOUND);
        }

        Optional<Comment> optionalComment = commentRepository.findById(id);

        if (optionalComment.isEmpty()) {
            log.error("Comment not found");
            throw new ApplicationException(ERROR.COMMENT_NOT_FOUND);
        }

        Comment comment = optionalComment.get();

        User user = getUserFromAccessToken(token);

        if (comment.getUser() != user) {
            log.error("User not found");
            throw new ApplicationException(ERROR.USER_NOT_FOUND);
        } else {
            commentRepository.delete(comment);
            isSuccess = "Success";
        }

        return new BaseResponse<>("Delete comment " + isSuccess, isSuccess);
    }

    public User getUserFromAccessToken(String accessToken) throws ApplicationException {
        log.info("4.1 - Decode access token: {}", accessToken);
        Jws<Claims> claims = JWTUtil.parseToken(accessToken);

        if (claims == null) {
            log.error("Access token is invalid");
            throw new ApplicationException(ERROR.INVALID_ACCESS_TOKEN);
        }

        log.info("4.2 - Get user email from access token");
        String email = claims.getBody().getSubject();

        log.info("4.3 - Get user from email: {}", email);
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            log.error("User not found");
            throw new ApplicationException(ERROR.USER_NOT_FOUND);
        }

        User user = userOptional.get();

        return user;
    }

    private void validateCreateCommentRequest(CommentCreateRequest request) throws ApplicationException {
        if (request == null) {
            log.error("Request is null");
            throw new ApplicationException(ERROR.INVALID_COMMENT_REQUEST);
        }

        log.info("1.1 - Validate request blog id: {}", request.getBlogId());
        if (request.getBlogId() == null) {
            log.error("Blog id is null");
            throw new ApplicationException(ERROR.COMMENT_BLOG_NOT_FOUND);
        }

        log.info("1.2 - Validate request content: {}", request.getContent());
        if (StringUtils.isBlank(request.getContent())) {
            log.error("Content is null");
            throw new ApplicationException(ERROR.COMMENT_CONTENT_IS_EMPTY);
        }
    }

    private void validateUpdateCommentRequest(CommentUpdateRequest request) throws ApplicationException {
        if (request == null) {
            log.error("Request is null");
            throw new ApplicationException(ERROR.INVALID_COMMENT_REQUEST);
        }

        log.info("1.1 - Validate request comment id: {}", request.getId());
        if (request.getId() == null) {
            log.error("Comment id is null");
            throw new ApplicationException(ERROR.COMMENT_NOT_FOUND);
        }

        log.info("1.2 - Validate request blog id: {}", request.getBlogId());
        if (request.getBlogId() == null) {
            log.error("Comment blog id is null");
            throw new ApplicationException(ERROR.COMMENT_BLOG_NOT_FOUND);
        }

        log.info("1.3 - Validate request content: {}", request.getContent());
        if (StringUtils.isBlank(request.getContent())) {
            log.error("Comment content is null");
            throw new ApplicationException(ERROR.COMMENT_CONTENT_IS_EMPTY);
        }
    }
}
