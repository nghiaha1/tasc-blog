package com.tasc.blogging.controller.user;

import com.tasc.blogging.aop.ApplicationException;
import com.tasc.blogging.controller.BaseController;
import com.tasc.blogging.model.requset.blog.BlogCreateRequest;
import com.tasc.blogging.model.requset.blog.BlogUpdateRequest;
import com.tasc.blogging.model.response.BaseResponse;
import com.tasc.blogging.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.cache.annotation.Cacheable;


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
    public ResponseEntity<BaseResponse> createBlog(@RequestBody BlogCreateRequest request,
                                                   @RequestHeader(name = "Authorization") String token) throws ApplicationException {
        String tokenValue = token.split(" ")[1];
        return createdResponse(blogService.createBlog(request, tokenValue));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> findById(@PathVariable Long id) throws ApplicationException {
        return createdResponse(blogService.findById(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<BaseResponse> updateBlog(@RequestBody BlogUpdateRequest request,
                                                   @RequestHeader(name = "Authorization") String token) throws ApplicationException {
        String tokenValue = token.split(" ")[1];
        return createdResponse(blogService.updateBlog(request, tokenValue));
    }

    @PutMapping("/like/{blogId}")
    public ResponseEntity<BaseResponse> likeBlog(@PathVariable Long blogId,
                                                 @RequestHeader(name = "Authorization") String token) throws ApplicationException {
        String tokenValue = token.split(" ")[1];
        return createdResponse(blogService.likeBlog(blogId, tokenValue));
    }

    @DeleteMapping("/delete/{blogId}")
    public ResponseEntity<BaseResponse> deleteBlog(@PathVariable Long blogId,
                                                   @RequestHeader(name = "Authorization") String token) throws ApplicationException {
        String tokenValue = token.split(" ")[1];
        return createdResponse(blogService.deleteBlog(blogId, tokenValue));
    }
}
