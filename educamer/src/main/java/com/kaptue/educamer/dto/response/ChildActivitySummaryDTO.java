package com.kaptue.educamer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class ChildActivitySummaryDTO {
    private long activeCoursesCount;
    private long recentSubmissionsCount;
    private long totalAssignmentsCount;
}