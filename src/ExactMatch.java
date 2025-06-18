import java.util.List;

class ExactMatch implements MatchingStrategy {
    @Override
    public boolean matches(Student student, Task task) {
        List<Skill> studentSkills = student.getSkills();
        List<Skill> taskRequirements = task.getRequirements();

        if (studentSkills.size() < taskRequirements.size()) {
            return false;
        }

        for (Skill reqSkill : taskRequirements) {
            boolean flag = false;
            for (Skill studentSkill : studentSkills) {
                if (studentSkill.getLanguage().equals(reqSkill.getLanguage()) && studentSkill.getLevel() >= reqSkill.getLevel()) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                return false;
            }
        }

        return true;
    }
}
