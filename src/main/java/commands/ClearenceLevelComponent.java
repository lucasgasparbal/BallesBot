package commands;


import net.dv8tion.jda.api.entities.Member;

public interface ClearenceLevelComponent {

    boolean isAdminOnly();
    String clearenceLevelForInstructions();

    boolean userAuthorChecksClearenceLevel(Member user);


}
