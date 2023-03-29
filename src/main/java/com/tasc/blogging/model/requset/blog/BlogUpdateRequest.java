package com.tasc.blogging.model.requset.blog;

import com.tasc.blogging.entity.enums.BlogStatus;
import lombok.Data;

import java.util.List;

@Data
public class BlogUpdateRequest {
    private Long id;
    private String title;
    private String content;
    private List<String> urls;
    private List<Long> categories;
    private BlogStatus status;
}
