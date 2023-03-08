package com.tasc.blogging.model.response.blog;

import com.tasc.blogging.entity.enums.BaseStatus;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
public class CategoryDTO {
    private Long id;
    private String title;
    private String description;
    private String createdAt;
    private String updatedAt;
    @Enumerated(EnumType.STRING)
    private BaseStatus status;
}
