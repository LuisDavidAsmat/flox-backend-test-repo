package com.nocountry.floxbackend.enums;

public enum Permission
{
    // Project Permissions
    CREATE_PROJECT,
    EDIT_PROJECT,
    DELETE_PROJECT,
    VIEW_PROJECT,

    // Task Permissions
    CREATE_TASK,
    EDIT_TASK,
    DELETE_TASK,
    ASSIGN_TASKS,
    UPDATE_OWN_TASKS,

    // User Management
    MANAGE_USERS,

    // Reporting
    GENERATE_REPORTS,
    VIEW_REPORTS,

    // Communication
    COMMENT_ON_TASKS
}
