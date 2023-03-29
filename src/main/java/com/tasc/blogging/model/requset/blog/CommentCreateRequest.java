package com.tasc.blogging.model.requset.blog;

import lombok.Data;

@Data
public class CommentCreateRequest {
    private Long blogId;
    private String content;
}
