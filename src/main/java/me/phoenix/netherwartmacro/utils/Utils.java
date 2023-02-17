package me.phoenix.netherwartmacro.utils;

import me.phoenix.netherwartmacro.Main;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.lang3.Validate;

import static me.phoenix.netherwartmacro.Macro.Macro.macroEnabled;
import static net.minecraft.init.Items.nether_wart;
import static net.minecraft.init.Items.potato;

public class Utils {

    public static final char COLOUR_CHAR = '\u00A7';

    private boolean alwaysFastBreaking = true;

    public static String translateAlternativeColourCode(char alternateColourCode, String string) {
        Validate.notNull(string, "Cannot translate null text");

        char[] b = string.toCharArray();
        for (int i = 0; i < b.length - 1; i++) {
            if (b[i] == alternateColourCode && "0123456789AaBbCcDdEeFfKkLlMmNnOoRrXx".indexOf(b[i + 1]) > -1) {
                b[i] = Utils.COLOUR_CHAR;
                b[i + 1] = Character.toLowerCase(b[i + 1]);

            }
        }
        return new String(b);
    }

    public static String chat(String string) {
        return Utils.translateAlternativeColourCode('&', string);
    }

    public static void sendMessage(String message) {
        /*
        Utils.sendMessage("...")
         */
        Main.mc.thePlayer.addChatMessage(new ChatComponentText(chat("&8[&6*&8] &9" + message)));
    }

    public static class URotation {
        public float pitch;
        public float yaw;

        public URotation(float pitch, float yaw) {
            this.pitch = pitch;
            this.yaw = yaw;
        }
    }

    public static void look(URotation rotation) {
        Main.mc.thePlayer.rotationPitch = rotation.pitch;
        Main.mc.thePlayer.rotationYaw = rotation.yaw;
    }

    public static boolean isStandingStill() {
        /*
        True = standingStill
        False = Moving
         */
        return (Main.mc.thePlayer.motionX == 0.0D && Main.mc.thePlayer.motionZ == 0.0D);
    }

    public static String getDirectionFacingofPlayer() {

        /*
        North = 1
        East = 3
        South = 4
        West = 1
         */

        int direction = MathHelper.floor_double((double) ((Main.mc.thePlayer.rotationYaw * 4F) / 360F) + 0.5D) & 3;
        if (direction == 1) {
            return "west";
        } else if (direction == 2) {
            return "north";
        } else if (direction == 3) {
            return "east";
        } else {
            return "south";
        }
    }

    public static boolean inSkyblock;
    public static Locations location = Locations.NULL;

    public static boolean isInSkyblock() {
        ScoreObjective scoreboardObj = Main.mc.theWorld.getScoreboard().getObjectiveInDisplaySlot(1);
        if (scoreboardObj != null) {
            if (ScoreboardUtils.cleanSB(scoreboardObj.getDisplayName()).startsWith("SKYBLOCK")) {
                inSkyblock = true;
                return true;
            }
            inSkyblock = false;
            Main.location = Locations.NULL;
            return false;
        }
        return false;
    }

    public static boolean standingOnPad() {
        BlockPos pos = new BlockPos((Minecraft.getMinecraft()).thePlayer.posX, (Minecraft.getMinecraft()).thePlayer.posY - 0.8D, (Minecraft.getMinecraft()).thePlayer.posZ);
        return (Main.mc.theWorld.getBlockState(pos).getBlock() == Blocks.end_portal_frame);
    }

    public static void getLocation() {
        if (inSkyblock) {
            for (String s : ScoreboardUtils.getSidebarLines()) {
                if (s.startsWith(" §7⏣")) {
                    String sCleaned = ScoreboardUtils.cleanSB(s);
                    switch (sCleaned.substring(sCleaned.indexOf("") + 2)) {
                        case "Void Sepulture":
                            Main.location = Locations.END;
                            break;
                        case "The Catacombs":
                            Main.location = Locations.DUNGEON;
                            break;
                        case "Your Island":
                            Main.location = Locations.PRIVATEISLAND;
                            break;
                        case "Dark Thicket":
                            Main.location = Locations.PARK;
                            break;
                        case "Mithril Deposits":
                        case "Jungle":
                        case "Goblin Holdout":
                        case "Precursor Remnants":
                            Main.location = Locations.CHOLLOWS;
                            break;
                        case "Divan's Gateway":
                            Main.location = Locations.DWARVENMINES;
                            break;
                    }
                    return;
                }
            }
        }
    }

    public static boolean playerOnIsland() {
        if(ScoreboardUtils.scoreboardContains(TextUtils.stripColor("Your Island"))) {
            return true;
        } else {
            return false;
        }
    }

}
