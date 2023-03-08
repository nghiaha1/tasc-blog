package com.tasc.blogging.controller.admin;

import com.tasc.blogging.aop.ApplicationException;
import com.tasc.blogging.controller.BaseController;
import com.tasc.blogging.model.response.BaseResponse;
import com.tasc.blogging.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/blog")
public class AdminBlogController extends BaseController {
    @Autowired
    private BlogService blogService;

    @PutMapping("/update/status/{id}")
    public ResponseEntity<BaseResponse> updateStatus(@PathVariable Long id) throws ApplicationException {
        return createdResponse(blogService.updateStatus(id));
    }
}
