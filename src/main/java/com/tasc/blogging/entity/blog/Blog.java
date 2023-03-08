package com.tasc.blogging.entity.blog;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.tasc.blogging.entity.base.BaseEntity;
import com.tasc.blogging.entity.enums.BaseStatus;
import com.tasc.blogging.entity.enums.BlogStatus;
import com.tasc.blogging.entity.user.User;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "blog")
@JsonPropertyOrder({"id", "title", "description", "user"})
public class Blog extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Lob
    private String content;
    @ManyToMany
    @JoinTable(name = "blog_thumbnail",
            joinColumns = @JoinColumn(name = "blog_id"),
            inverseJoinColumns = @JoinColumn(name = "thumbnail_id"))
    @JsonBackReference
    private List<Thumbnail> thumbnails;
    @ManyToMany
    @JoinTable(name = "blog_category",
            joinColumns = @JoinColumn(name = "blog_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    @JsonManagedReference
    private List<Category> categories;
    @ManyToOne
    @JoinTable(name = "user_blog",
            joinColumns = @JoinColumn(name = "blog_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    @JsonManagedReference
    private User user;
    @Enumerated(EnumType.STRING)
    private BlogStatus status;
}
