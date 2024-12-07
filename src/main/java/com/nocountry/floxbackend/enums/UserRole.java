package com.nocountry.floxbackend.enums;

import lombok.Getter;

import java.util.List;

@Getter
public enum UserRole
{
    ROLE_ADMIN(
            "Administrator",
            List.of(
                    Permission.CREATE_PROJECT,
                    Permission.EDIT_PROJECT,
                    Permission.DELETE_PROJECT,
                    Permission.MANAGE_USERS,
                    Permission.GENERATE_REPORTS
            )
    ),
    ROLE_USER( "User",
            List.of(
                    Permission.VIEW_PROJECT,
                    Permission.UPDATE_OWN_TASKS,
                    Permission.COMMENT_ON_TASKS
            )
    ),
    ROLE_PROJECT_MANAGER(
            "Project Manager",
            List.of(
                    Permission.CREATE_PROJECT,
                    Permission.EDIT_PROJECT,
                    Permission.ASSIGN_TASKS,
                    Permission.VIEW_REPORTS
            )
    ),
    ROLE_TEAM_MEMBER(
            "Team Member",
            List.of(
                    Permission.VIEW_PROJECT,
                    Permission.UPDATE_OWN_TASKS,
                    Permission.COMMENT_ON_TASKS
            )
    ),
    ROLE_GUEST(
            "Guest",
            List.of(
                    Permission.VIEW_PROJECT
            )
    );

    private final String displayName;
    private final List<Permission> permissions;

    UserRole(String displayName, List<Permission> permissions) {
        this.displayName = displayName;
        this.permissions = permissions;
    }

    public boolean hasPermissions(Permission permission)
    {
        return permissions.contains(permission);
    }
}

