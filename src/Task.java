import java.util.List;
class Task {
    private String taskName;
    private List<Skill> requirements;
    private int id;

    public Task(String taskName, List<Skill> requirements) {
        this.taskName = taskName;
        this.requirements = requirements;
    }

    public Task(int i, String n)
    {
        id=i;
        taskName=n;
    }

    public void printTasks() {
        System.out.println("Task Name: " + this.taskName);
        for (Skill reqSkill : requirements) {
            System.out.println("Required Skill: " + reqSkill.getLanguage());
            System.out.println("Required Level: " + reqSkill.getLevel());
        }
        System.out.println("----------------------------------------");
    }

    public List<Skill> getRequirements() {
        return requirements;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setRequirements(List<Skill> requirements) {
        this.requirements = requirements;
    }
}

