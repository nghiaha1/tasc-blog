package com.tasc.blogging.controller.user;

import com.tasc.blogging.aop.ApplicationException;
import com.tasc.blogging.controller.BaseController;
import com.tasc.blogging.model.requset.blog.CommentCreateRequest;
import com.tasc.blogging.model.requset.blog.CommentUpdateRequest;
import com.tasc.blogging.model.response.BaseResponse;
import com.tasc.blogging.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/comment")
public class UserCommentController extends BaseController {
    @Autowired
    private CommentService commentService;

    @PostMapping("/create")
    public ResponseEntity<BaseResponse> createComment(@RequestBody CommentCreateRequest request,
                                                      @RequestHeader(name = "Authorization") String token) throws ApplicationException {
        String tokenValue = token.split(" ")[1];
        return createdResponse(commentService.createComment(request, tokenValue));
    }

    @PutMapping("/update")
    public ResponseEntity<BaseResponse> updateComment(@RequestBody CommentUpdateRequest request,
                                                      @RequestHeader(name = "Authorization") String token) throws ApplicationException {
        String tokenValue = token.split(" ")[1];
        return createdResponse(commentService.updateComment(request, tokenValue));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<BaseResponse> deleteComment(@PathVariable Long id,
                                                      @RequestHeader(name = "Authorization") String token) throws ApplicationException {
        String tokenValue = token.split(" ")[1];
        return createdResponse(commentService.deleteComment(id, tokenValue));
    }
}
