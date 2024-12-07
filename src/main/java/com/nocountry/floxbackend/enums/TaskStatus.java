package com.nocountry.floxbackend.enums;

import lombok.Getter;

@Getter
public enum TaskStatus
{
    BACKLOG(
            "Backlog",
            "Tasks not yet started"
    ),
    TODO(
            "To Do",
            "Tasks ready to be worked on"
    ),
    IN_PROGRESS(
            "In Progress",
            "Tasks currently being worked on"
    ),
    UNDER_REVIEW(
            "Under Review",
            "Tasks awaiting review or validation"
    ),
    BLOCKED(
            "Blocked",
            "Tasks impedied by external factors"
    ),
    DONE(
            "Completed",
            "Tasks successfully finished"
    ),
    CANCELLED(
            "Cancelled",
            "Tasks that will not be completed"
    );

    private final String displayName;
    private final String description;

    TaskStatus(String displayName, String description)
    {
        this.displayName = displayName;
        this.description = description;
    }

    public boolean isActive ()
    {
        return this == TODO || this == IN_PROGRESS || this == UNDER_REVIEW || this == BLOCKED;
    }
}
