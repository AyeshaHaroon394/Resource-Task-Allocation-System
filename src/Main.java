import java.util.List;
public class Main {

    public static void main(String[] args) {
        // Assuming files are named "students.txt" and "tasks.txt"
        String studentFileName = "students.txt";
        String taskFileName = "task.txt";

        // Read student and task data from files
        List<Student> students = FileReading.readStudents(studentFileName);
        List<Task> tasks = FileReading.readTasks(taskFileName);

        // Print out read data
        System.out.println("Students Data:");
        for (Student student : students) {
            student.printResources();
        }

        System.out.println("\nTasks Data:");
        for (Task task : tasks) {
            task.printTasks();
        }

        // Apply matching strategies
        MatchingStrategy exactMatchStrategy = new ExactMatch();
        MatchingStrategy skillOnlyMatchStrategy = new SkillOnlyMatch();

        System.out.println("\nExact Match Results:");
        for (Task task : tasks) {
            System.out.println("For task: " + task.getTaskName());
            for (Student student : students) {
                if (exactMatchStrategy.matches(student, task)) {
                    System.out.println(student.getName() + " matches this task.");
                }
            }
        }

        System.out.println("\nSkill Only Match Results:");
        for (Task task : tasks) {
            System.out.println("For task: " + task.getTaskName());
            for (Student student : students) {
                if (skillOnlyMatchStrategy.matches(student, task)) {
                    System.out.println(student.getName() + " matches this task.");
                }
            }
        }
    }
}
