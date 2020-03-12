package commands.admin;

import commands.ClearenceLevelComponent;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;

public class AdminLevelComponent implements ClearenceLevelComponent {

    @Override
    public boolean isAdminOnly() {
        return true;
    }

    @Override
    public String clearenceLevelForInstructions() {
        return "**REQUIERE ROL ADMIN**\n -----\n";
    }

    @Override
    public boolean userAuthorChecksClearenceLevel(Member user) {
        return user.hasPermission(Permission.MANAGE_SERVER);
    }

}
