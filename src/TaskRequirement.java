public class TaskRequirement {
    private int taskId;
    private int skillId;

    // Getters and setters
    public int getTaskId() { return taskId; }
    public int getSkillId() { return skillId; }
    public void setTaskId(int taskId) { this.taskId = taskId; }
    public void setSkillId(int skillId) { this.skillId = skillId; }

    // Constructor
    public TaskRequirement(int taskId, int skillId) {
        this.taskId = taskId;
        this.skillId = skillId;
    }
}
