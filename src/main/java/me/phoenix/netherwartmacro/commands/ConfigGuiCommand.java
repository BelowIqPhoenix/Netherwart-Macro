package me.phoenix.netherwartmacro.commands;

import me.phoenix.netherwartmacro.Main;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

public class ConfigGuiCommand extends CommandBase {


    @Override
    public String getCommandName() {
        return "macro";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return null;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        Main.displayScreen = Main.config.gui();
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
}
