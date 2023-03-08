package com.tasc.blogging.entity.user;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.tasc.blogging.entity.base.BaseEntity;
import com.tasc.blogging.entity.enums.BaseStatus;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "role")
@JsonPropertyOrder({"id", "name", "user"})
public class Role extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @OneToMany(mappedBy = "role")
    @JsonBackReference
    private List<User> user;
    @Enumerated(EnumType.STRING)
    private BaseStatus status;
}
