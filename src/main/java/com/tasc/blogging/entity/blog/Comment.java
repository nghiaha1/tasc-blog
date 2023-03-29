package com.tasc.blogging.entity.blog;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.tasc.blogging.entity.base.BaseEntity;
import com.tasc.blogging.entity.enums.CommentStatus;
import com.tasc.blogging.entity.user.User;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "comment")
@JsonPropertyOrder({"id", "content", "user", "blog", "status"})
public class Comment extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Lob
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(name = "comment_user",
            joinColumns = @JoinColumn(name = "comment_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    @JsonBackReference
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(name = "comment_blog",
            joinColumns = @JoinColumn(name = "comment_id"),
            inverseJoinColumns = @JoinColumn(name = "blog_id"))
    @JsonBackReference
    private Blog blog;

    @Enumerated(EnumType.STRING)
    private CommentStatus status;

}
