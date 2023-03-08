package com.tasc.blogging.entity.user;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.tasc.blogging.entity.blog.Blog;
import com.tasc.blogging.entity.base.BaseEntity;
import com.tasc.blogging.entity.enums.BaseStatus;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "user")
@JsonPropertyOrder({"id", "username", "email", "firstName", "lastName", "bio", "image", "role"})
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    @Lob
    private String bio;
    @Lob
    private String image;
    @ManyToOne
    @JsonManagedReference
    private Role role;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<Blog> blogs;
    @Enumerated(EnumType.STRING)
    private BaseStatus status;
    private String verificationCode;
}
