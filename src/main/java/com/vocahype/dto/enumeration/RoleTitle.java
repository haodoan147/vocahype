package com.vocahype.dto.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RoleTitle {
    USER(1, "user"),
    CONTRIBUTOR(2, "contributor"),
    ADMIN(3, "admin");

    private final long id;
    private final String title;
}
