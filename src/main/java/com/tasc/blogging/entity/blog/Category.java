package com.tasc.blogging.entity.blog;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.tasc.blogging.entity.base.BaseEntity;
import com.tasc.blogging.entity.enums.BaseStatus;
import com.tasc.blogging.util.Constant;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "category")
@JsonPropertyOrder({"id", "title", "description", "blogs", "parent", "children", "isRoot"})
public class Category extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    @ManyToMany(mappedBy = "categories", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonBackReference
    private List<Blog> blogs;
    @ManyToOne
    @JoinTable(name = "category_parent",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "parent_id"))
    @JsonBackReference
    private Category parent;
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private List<Category> children;
    private Integer isRoot;

    public boolean checkIsRoot() {
        return isRoot != null && isRoot == Constant.ONOFF.ON;
    }

    @Enumerated(EnumType.STRING)
    private BaseStatus status;
}
