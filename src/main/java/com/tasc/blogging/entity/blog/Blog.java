package com.tasc.blogging.entity.blog;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.tasc.blogging.entity.base.BaseEntity;
import com.tasc.blogging.entity.enums.BlogStatus;
import com.tasc.blogging.entity.user.User;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "blog")
@JsonPropertyOrder({"id", "title", "description", "user"})
public class Blog extends BaseEntity implements Serializable {
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
    @JsonBackReference
    private List<Category> categories;

    @ManyToOne
    @JoinTable(name = "user_blog",
            joinColumns = @JoinColumn(name = "blog_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    @JsonBackReference
    private User createdBy;

    @OneToMany(mappedBy = "blog", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @ManyToMany
    @JoinTable(name = "blog_like",
            joinColumns = @JoinColumn(name = "blog_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    @JsonBackReference
    private List<User> likedUserList;

    @Enumerated(EnumType.STRING)
    private BlogStatus status;
}
