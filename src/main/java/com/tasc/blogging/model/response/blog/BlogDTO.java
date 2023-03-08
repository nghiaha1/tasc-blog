package com.tasc.blogging.model.response.blog;

import com.tasc.blogging.entity.enums.BlogStatus;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.List;

@Data
public class BlogDTO {
    private Long id;
    private String title;
    private String content;
    private List<CategoryDTO> categories;
    private String createdAt;
    private String updatedAt;
    @Enumerated(EnumType.STRING)
    private BlogStatus status;
}
