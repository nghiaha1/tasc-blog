package com.tasc.blogging.controller.user;

import com.tasc.blogging.aop.ApplicationException;
import com.tasc.blogging.controller.BaseController;
import com.tasc.blogging.model.requset.blog.BCreateRequest;
import com.tasc.blogging.model.requset.blog.BUpdateRequest;
import com.tasc.blogging.model.response.BaseResponse;
import com.tasc.blogging.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/blog")
public class UserBlogController extends BaseController {
    @Autowired
    private BlogService blogService;

    @GetMapping("/all")
    public ResponseEntity<BaseResponse> findAll(@RequestParam(defaultValue = "1") int page,
                                                @RequestParam(defaultValue = "10") int size) {
        if (page <= 1) page = 1;
        if (size <= 0) size = 10;

        return createdResponse(blogService.findAll(page - 1, size));
    }

    @PostMapping("/create")
    public ResponseEntity<BaseResponse> createBlog(@RequestBody BCreateRequest request) throws ApplicationException {
        return createdResponse(blogService.createBlog(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> findById(@PathVariable Long id) throws ApplicationException {
        return createdResponse(blogService.findById(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<BaseResponse> updateBlog(@PathVariable Long id, @RequestBody BUpdateRequest request) throws ApplicationException {
        return createdResponse(blogService.updateBlog(id, request));
    }
}
