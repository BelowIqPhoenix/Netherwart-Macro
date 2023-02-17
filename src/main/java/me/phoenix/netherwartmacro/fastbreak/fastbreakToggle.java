package me.phoenix.netherwartmacro.fastbreak;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import org.jetbrains.annotations.NotNull;

public class fastbreakToggle implements ICommand {

    @Override
    public String getCommandName() {
        return "fastbreak";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/" + getCommandName();
    }

    @Override
    public List<String> getCommandAliases() {
        return new ArrayList<>(Arrays.asList(new String[] { "fastbreaktoggle" }));
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 0)
            if (Config.fastbreak) {
                Config.fastbreak = false;
                (Minecraft.getMinecraft()).thePlayer.addChatMessage((IChatComponent)new ChatComponentText(EnumChatFormatting.BLUE + "Yung Scripts >> Fast break disabled."));
            } else {
                Config.fastbreak = true;
                (Minecraft.getMinecraft()).thePlayer.addChatMessage((IChatComponent)new ChatComponentText(EnumChatFormatting.BLUE + "Yung Scripts >> Fast break enabled."));
            }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return new ArrayList<>();
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }

    @Override
    public int compareTo(@NotNull ICommand o) {
        return 0;
    }

}
