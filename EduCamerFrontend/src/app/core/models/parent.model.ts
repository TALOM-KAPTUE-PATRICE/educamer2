export interface ChildActivitySummary {
  activeCoursesCount: number;
  recentSubmissionsCount: number;
  totalAssignmentsCount: number;
}

export interface LinkedStudent {
  id: number;
  name: string;
  avatarUrl: string | null;
  activitySummary: ChildActivitySummary;
}
