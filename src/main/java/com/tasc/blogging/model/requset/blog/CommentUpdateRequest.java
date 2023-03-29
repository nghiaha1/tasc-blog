package com.tasc.blogging.model.requset.blog;

import lombok.Data;

@Data
public class CommentUpdateRequest {
    private Long id;
    private Long blogId;
    private String content;
}
