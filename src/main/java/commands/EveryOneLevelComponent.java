package commands;


import net.dv8tion.jda.api.entities.Member;

public class EveryOneLevelComponent implements ClearenceLevelComponent {
    @Override
    public boolean isAdminOnly() {
        return true;
    }

    @Override
    public String clearenceLevelForInstructions() {
        return "";
    }

    @Override
    public boolean userAuthorChecksClearenceLevel(Member user) {
        return true;
    }
}
