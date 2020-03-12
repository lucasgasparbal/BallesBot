package commands.util;

import commands.Command;

import java.util.Comparator;

public class CommandComparator implements Comparator<Command> {
    @Override
    public int compare(Command commandOne, Command commandTwo) {
        return commandOne.compareToCommand(commandTwo);
    }
}
