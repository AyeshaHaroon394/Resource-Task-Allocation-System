import java.util.List;

class SkillOnlyMatch implements MatchingStrategy {
    @Override
    public boolean matches(Student student, Task task) {
        List<Skill> studentSkills = student.getSkills();
        List<Skill> taskRequirements = task.getRequirements();

        for (Skill reqSkill : taskRequirements) {
            boolean flag = false;
            for (Skill studentSkill : studentSkills) {
                if (studentSkill.getLanguage().equals(reqSkill.getLanguage())) {
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
