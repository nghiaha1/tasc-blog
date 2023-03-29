package com.tasc.blogging.entity.user;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.tasc.blogging.entity.blog.Blog;
import com.tasc.blogging.entity.base.BaseEntity;
import com.tasc.blogging.entity.blog.Comment;
import com.tasc.blogging.entity.enums.BaseStatus;
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
@Table(name = "user")
@JsonPropertyOrder({"id", "username", "email", "firstName", "lastName", "bio", "image", "role"})
public class User extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    private String firstName;

    private String lastName;

    @Lob
    private String bio;

    @Lob
    private String image;

    @ManyToOne
    @JsonBackReference
    private Role role;

    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<Blog> blogs;

    @ManyToMany(mappedBy = "likedUserList", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Blog> likedBlogList;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<Comment> comments;

    private String verificationCode;

    @Enumerated(EnumType.STRING)
    private BaseStatus status;
}
