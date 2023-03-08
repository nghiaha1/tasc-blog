package com.tasc.blogging.controller.user;

import com.tasc.blogging.aop.ApplicationException;
import com.tasc.blogging.controller.BaseController;
import com.tasc.blogging.model.requset.blog.CCreateRequest;
import com.tasc.blogging.model.response.BaseResponse;
import com.tasc.blogging.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController extends BaseController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/all")
    public ResponseEntity<BaseResponse> findAll(@RequestParam(defaultValue = "1") int page,
                                                @RequestParam(defaultValue = "10") int size) throws ApplicationException {
        if (page <= 1) page = 1;
        if (size <= 0) size = 10;
        return createdResponse(categoryService.findAll(page - 1, size));
    }

    @PostMapping("/create")
    public ResponseEntity<BaseResponse> createCategory(@RequestBody CCreateRequest request) throws ApplicationException {
        return createdResponse(categoryService.createCategory(request));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<BaseResponse> deleteCategory(@PathVariable Long id) throws ApplicationException {
        return createdResponse(categoryService.deleteCategory(id));
    }
}
