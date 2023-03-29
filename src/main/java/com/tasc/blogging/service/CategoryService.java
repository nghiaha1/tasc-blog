package com.tasc.blogging.service;

import com.tasc.blogging.aop.ApplicationException;
import com.tasc.blogging.entity.blog.Category;
import com.tasc.blogging.model.requset.blog.CategoryCreateRequest;
import com.tasc.blogging.model.response.BasePagingData;
import com.tasc.blogging.model.response.BaseResponse;
import com.tasc.blogging.model.response.blog.CategoryDTO;
import com.tasc.blogging.repository.CategoryRepository;
import com.tasc.blogging.util.Constant;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
public class CategoryService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CategoryRepository categoryRepository;

    public CategoryDTO convertToDTO(Category category) {
        return modelMapper.map(category, CategoryDTO.class);
    }

    public List<CategoryDTO> convertToDTOList(List<Category> categories) {
        return categories.stream().map(category -> {
                    CategoryDTO categoryDto = modelMapper.map(category, CategoryDTO.class);
                    return categoryDto;
                })
                .collect(Collectors.toList());
    }

    public BaseResponse<CategoryDTO> findById(Long id) throws ApplicationException {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isEmpty())
            throw new ApplicationException(ERROR.CATEGORY_NOT_FOUND);

        Category category = optionalCategory.get();

        CategoryDTO categoryDTO = convertToDTO(category);

        return new BaseResponse<>("Find By Id Success", categoryDTO);
    }

    public BaseResponse<BasePagingData<List<Category>>> findAll(int page, int size) throws ApplicationException {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Category> pageResult = categoryRepository.findAll(pageRequest);

        List<Category> content = pageResult.getContent();

        int totalPages = pageResult.getTotalPages();
        int totalItems = (int) pageResult.getTotalElements();
        int currentPage = pageResult.getNumber() + 1;

        return new BaseResponse<>("Find All Success", new BasePagingData<>(currentPage, size, totalPages, totalItems, content));
    }

    public BaseResponse createCategory(CategoryCreateRequest request) throws ApplicationException {
        log.info("1 - Create category request: {}", request);
        validateCreateCategoryRequest(request);

        if (!request.checkIsRoot()) {
            Optional<Category> checkParentCateOpt = categoryRepository.findById(request.getParent());

            if (checkParentCateOpt.isEmpty()) {
                log.info("Parent category not found");
                throw new ApplicationException(ERROR.CATEGORY_PARENT_NOT_FOUND);
            }

            Category parentCategory = checkParentCateOpt.get();

            Category category = Category.builder()
                    .title(request.getTitle())
                    .description(request.getDescription())
                    .isRoot(request.checkIsRoot() ? Constant.ONOFF.ON : Constant.ONOFF.OFF)
                    .parent(parentCategory)
                    .children(new ArrayList<>())
                    .build();

            categoryRepository.save(category);

            return new BaseResponse("Create Category Success", category);

        } else {
            Category category = Category.builder()
                    .title(request.getTitle())
                    .description(request.getDescription())
                    .isRoot(request.checkIsRoot() ? Constant.ONOFF.ON : Constant.ONOFF.OFF)
                    .parent(null)
                    .children(new ArrayList<>())
                    .build();

            categoryRepository.save(category);

            return new BaseResponse("Create Category Success", category);
        }
    }

    public BaseResponse deleteCategory(Long id) throws ApplicationException {
        Optional<Category> optionalCategory = categoryRepository.findById(id);

        if (optionalCategory.isEmpty()) {
            throw new ApplicationException(ERROR.CATEGORY_NOT_FOUND);
        }

        Category category = optionalCategory.get();

        categoryRepository.delete(category);
        return new BaseResponse("Delete Category Success");
    }

    private void validateCreateCategoryRequest(CategoryCreateRequest request) throws ApplicationException {
        if (StringUtils.isBlank(request.getTitle())) {
            throw new ApplicationException(ERROR.CATEGORY_TITLE_IS_EMPTY);
        }

        if (StringUtils.isBlank(request.getDescription())) {
            throw new ApplicationException(ERROR.CATEGORY_DESCRIPTION_IS_EMPTY);
        }

        if (request.checkIsRoot() && request.getParent() != null) {
            log.info("level is invalid");
            throw new ApplicationException(ERROR.CATEGORY_LEVEL_IS_INVALID);
        }

        if (!request.checkIsRoot() && request.getParent() == null) {
            log.info("parent is blank");
            throw new ApplicationException(ERROR.CATEGORY_PARENT_IS_BLANK);
        }
    }
}
