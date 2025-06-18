import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class FileReading {
    public static List<Student> readStudents(String fileName) {
        List<Student> students = new ArrayList<>();

        try (BufferedReader read = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = read.readLine()) != null) {
                String[] st = line.split("\\|");
                String name = st[0].trim();
                String[] skillsArray = st[1].trim().split(", ");

                List<Skill> skills = new ArrayList<>();

                for (String skillStr : skillsArray) {
                    String[] skillParts = skillStr.split(":");
                    if (skillParts.length == 2) {
                        String language = skillParts[0].trim();
                        int experience = Integer.parseInt(skillParts[1].trim());
                        skills.add(new Skill(language, experience));
                    }
                }

                Student student = new Student(name, skills);
                students.add(student);
            }
        } catch (IOException e) {
            System.out.println("Error in file");
            e.printStackTrace();
        }

        return students;
    }

    public static List<Task> readTasks(String fileName) {
        List<Task> tasks = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] taskInfo = line.split("\\|");
                String taskName = taskInfo[0].trim();
                String[] reqSkills = taskInfo[1].trim().split(", ");
                List<Skill> requirements = new ArrayList<>();

                for (String reqSkill : reqSkills) {
                    String[] skillParts = reqSkill.split(":");
                    if (skillParts.length == 2) {
                        String language = skillParts[0].trim();
                        char level = skillParts[1].charAt(0);
                        requirements.add(new Skill(language, level));
                    }
                }

                Task task = new Task(taskName, requirements);
                tasks.add(task);
            }
        } catch (IOException e) {
            System.out.println("Error in file");
            e.printStackTrace();
        }

        return tasks;
    }
}