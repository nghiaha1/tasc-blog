package com.tasc.blogging.entity.blog;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.tasc.blogging.entity.base.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "thumbnail")
public class Thumbnail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private String url;
    @ManyToMany(mappedBy = "thumbnails")
    @JsonBackReference
    private List<Blog> blogs;

}
