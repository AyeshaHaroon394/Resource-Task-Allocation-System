public class TaskWithSkills {
    private int taskId;
    private String taskName;
    private String skills;
    private String levels;

    public TaskWithSkills(int taskId, String taskName, String skills, String levels) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.skills = skills;
        this.levels = levels;
    }

    // Getters and possibly setters

    public int getTaskId() { return taskId; }
    public String getTaskName() { return taskName; }
    public String getSkills() { return skills; }
    public String getLevels() { return levels; }
}
