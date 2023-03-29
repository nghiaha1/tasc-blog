package com.tasc.blogging.model.requset.blog;

import lombok.Data;

import java.util.List;

@Data
public class BlogCreateRequest {
    private String title;
    private String description;
    private List<Long> categories;
    private List<String> urls;
}
