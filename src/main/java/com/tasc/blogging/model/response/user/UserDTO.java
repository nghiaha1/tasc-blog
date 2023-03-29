package com.tasc.blogging.model.response.user;

import com.tasc.blogging.entity.enums.BaseStatus;
import com.tasc.blogging.entity.user.Role;
import lombok.Data;

@Data
public class UserDTO {
    private String email;
    private String firstName;
    private String lastName;
    private Role role;
    private BaseStatus status;
}
