package com.nocountry.floxbackend.entities;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public interface ProjectProjection
{
    Long getId();
    String getName();
    String getDescription();
    LocalDateTime getStartDate();
    LocalDateTime getEndDate();
    Double getBudget();
    Double getCompletionPercentage();
    String getCreatorName();
    Set<String> getTaskTitles();
    Set<String> getMemberUsernames();
}
