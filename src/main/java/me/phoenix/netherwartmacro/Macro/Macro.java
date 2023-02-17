package me.phoenix.netherwartmacro.Macro;

import me.phoenix.netherwartmacro.utils.*;

import me.phoenix.netherwartmacro.Main;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import static me.phoenix.netherwartmacro.utils.Utils.playerOnIsland;

public class Macro {

    //KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindRight.getKeyCode(), false);

    public static boolean macroEnabled = false;
    public static String farmingDirection;
    public static String goingForwardOrBackward; //all even layers = forward all odd layers backward
    public static boolean finishedChangingKeys = false;
    public static boolean moving = false;
    public static boolean checkedY = false;
    public static boolean paused = false;
    public static int yLevel = 0;
    public static boolean onlyRunOnce = false;
    public static boolean onlyRunOnce2 = false;
    public static boolean pausedVariableButItsMorePaused = false;
    public static boolean changedLayer = false;
    public static boolean fixingRot = false;
    int x1;
    int z1;

    public static void changeKeys() {
        if (Utils.isStandingStill() && macroEnabled && farmingDirection.equalsIgnoreCase("right") && !finishedChangingKeys) {
            moving = false;
            Utils.sendMessage("Changing direction Left");
            KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindRight.getKeyCode(), false);
            KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindLeft.getKeyCode(), true);
            moving = true;
            farmingDirection = "left";
            finishedChangingKeys = true;
            if (changedLayer) {
                changedLayer = false;
            }
        }
        if (Utils.isStandingStill() && macroEnabled && farmingDirection.equalsIgnoreCase("left") && !finishedChangingKeys) {
            moving = false;
            Utils.sendMessage("Changing direction Right");
            KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindLeft.getKeyCode(), false);
            KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindRight.getKeyCode(), true);
            moving = true;
            farmingDirection = "right";
            finishedChangingKeys = true;
            if (changedLayer) {
                changedLayer = false;
            }
        }
    }

    public static void fixRotation() {
        if (!fixingRot) {
            fixingRot = true;
            if (Utils.getDirectionFacingofPlayer().equalsIgnoreCase("north")) {
                RotUtil.look(new RotUtil.Rotation(5.0F,180.0F));
            }else if (Utils.getDirectionFacingofPlayer().equalsIgnoreCase("south")) {
                RotUtil.look(new RotUtil.Rotation(5.0F,0.0F));
            }else if (Utils.getDirectionFacingofPlayer().equalsIgnoreCase("east")) {
                RotUtil.look(new RotUtil.Rotation(5.0F,-90.0F));
            }else if (Utils.getDirectionFacingofPlayer().equalsIgnoreCase("west")) {
                RotUtil.look(new RotUtil.Rotation(5.0F,90.0F));
            }
        }
    }

    public static void startMacro() {
        new Thread(() -> {
            try {
                macroEnabled = true;
                pausedVariableButItsMorePaused = false;
                if (!paused) {
                    Utils.sendMessage("Macro started!");
                    farmingDirection = "right";
                    goingForwardOrBackward = "forward";
                }
                fixRotation();
                Thread.sleep(500);
                if (farmingDirection.equalsIgnoreCase("right") && goingForwardOrBackward.equalsIgnoreCase("forward")) {
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), true);
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindRight.getKeyCode(), true);
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindAttack.getKeyCode(), true);
                } else if (farmingDirection.equalsIgnoreCase("left") && goingForwardOrBackward.equalsIgnoreCase("forward")) {
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), true);
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindLeft.getKeyCode(), true);
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindAttack.getKeyCode(), true);
                } else if (farmingDirection.equalsIgnoreCase("right") && goingForwardOrBackward.equalsIgnoreCase("backward")) {
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindBack.getKeyCode(), true);
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindRight.getKeyCode(), true);
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindAttack.getKeyCode(), true);
                } else if (farmingDirection.equalsIgnoreCase("left") && goingForwardOrBackward.equalsIgnoreCase("backward")) {
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindBack.getKeyCode(), true);
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindLeft.getKeyCode(), true);
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindAttack.getKeyCode(), true);
                }
                if (!moving) {
                    moving = Utils.isStandingStill();
                }
                while (macroEnabled) {
                    while (moving) {
                        if (!checkedY && playerOnIsland()) {
                            Thread.sleep(1000);
                            checkedY = true;
                            Utils.sendMessage("Checked Y");
                            yLevel = Main.mc.thePlayer.getPosition().getY();
                        }
                        Utils.sendMessage("Moving!");
                        if (!macroEnabled) {
                            break;
                        }
                        Thread.sleep(250);
                        finishedChangingKeys = false;
                        changeKeys();
                        Thread.sleep(500);
                        if (Main.mc.thePlayer.getPosition().getY() != yLevel) {
                            changedLayer = true;
                            resetPlayer();
                            Utils.sendMessage("Changed Layer");
                            Utils.sendMessage("Resetting Player");
                            checkedY = false;
                            KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), false);
                            KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindBack.getKeyCode(), true);
                            if (goingForwardOrBackward.equalsIgnoreCase("forward")) {
                                goingForwardOrBackward = "backward";
                            } else {
                                goingForwardOrBackward = "forward";
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void stopMacro() {
        Utils.sendMessage("Macro stopped!");
        macroEnabled = false;
        moving = false;
        checkedY = false;
        paused = false;
        fixingRot = false;
        KeyBinding.unPressAllKeys();
    }

    public static void pauseMacro() {
        if (!pausedVariableButItsMorePaused) {
            Utils.sendMessage("Macro paused!");
            macroEnabled = false;
            moving = false;
            paused = true;
            pausedVariableButItsMorePaused = true;
            KeyBinding.unPressAllKeys();
        } else {
            Utils.sendMessage("Macro unpaused!");
            pausedVariableButItsMorePaused = false;
            startMacro();
        }
    }

    public static void resetPlayer() {
        new Thread(() -> {
            try {
                pauseMacro();
                Thread.sleep(500);
                if (Utils.standingOnPad()) {
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindRight.getKeyCode(), true);
                    Thread.sleep(500);
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindRight.getKeyCode(), false);
                }
                Main.mc.thePlayer.sendChatMessage("/sethome");
                Thread.sleep(500);
                Main.mc.thePlayer.sendChatMessage("/hub");
                Thread.sleep(5000);
                Main.mc.thePlayer.sendChatMessage("/is");
                Thread.sleep(5000);
                pauseMacro();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    @SubscribeEvent
    public void onStartKeypress(InputEvent.KeyInputEvent event) {
        if (KeyBinds.keyBindings[0].isPressed()) {
            startMacro();
        } else if (KeyBinds.keyBindings[1].isPressed()) {
            stopMacro();
        } else if (KeyBinds.keyBindings[2].isPressed()) {
            pauseMacro();
        }
    }

    public static boolean inGui = false;

    @SubscribeEvent
    public void guiIsOpen(TickEvent.PlayerTickEvent event) {
        if (Main.mc.thePlayer != null && Minecraft.getMinecraft().currentScreen != null && macroEnabled) {
            pauseMacro();
            inGui = true;
        } else if (Main.mc.thePlayer != null && inGui && paused && Minecraft.getMinecraft().currentScreen == null) {
            pauseMacro();
            inGui = false;
        }
    }

    @SubscribeEvent
    public void notOnIsland(TickEvent.PlayerTickEvent event) {
        new Thread(() -> {
            try {
                if (macroEnabled && !onlyRunOnce) {
                    if (!playerOnIsland() && !onlyRunOnce) {
                        onlyRunOnce = true;
                        pauseMacro();
                        Thread.sleep(500);
                        Main.mc.thePlayer.sendChatMessage("/play sb");
                        Thread.sleep(5000);
                        Main.mc.thePlayer.sendChatMessage("/is");
                        Thread.sleep(5000);
                        pauseMacro();
                        Thread.sleep(500);
                        onlyRunOnce = false;
                    }
                }
            } catch (Exception e) {
            }
        }).start();
    }

    @SubscribeEvent
    public void stuckInBlock(TickEvent.PlayerTickEvent event) {
        new Thread(() -> {
            try {
                if (macroEnabled && !onlyRunOnce2) {
                    onlyRunOnce2 = true;
                    x1 = Main.mc.thePlayer.getPosition().getX();
                    z1 = Main.mc.thePlayer.getPosition().getZ();
                    Thread.sleep(4000);
                    if (Main.mc.thePlayer.getPosition().getX() == x1 && Main.mc.thePlayer.getPosition().getZ() == z1) {
                        pauseMacro();
                        Thread.sleep(750);
                        if (goingForwardOrBackward.equalsIgnoreCase("forward") && farmingDirection.equalsIgnoreCase("right")) {
                            KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), false);
                            KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindRight.getKeyCode(), false);
                            KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindBack.getKeyCode(), true);
                            KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindLeft.getKeyCode(), true);
                            Thread.sleep(500);
                            KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindBack.getKeyCode(), false);
                            KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindLeft.getKeyCode(), false);
                        }else if (goingForwardOrBackward.equalsIgnoreCase("forward") && farmingDirection.equalsIgnoreCase("left") ) {
                            KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), false);
                            KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindLeft.getKeyCode(), false);
                            KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindBack.getKeyCode(), true);
                            KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindRight.getKeyCode(), true);
                            Thread.sleep(500);
                            KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindBack.getKeyCode(), false);
                            KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindRight.getKeyCode(), false);
                        } else if (goingForwardOrBackward.equalsIgnoreCase("backward") && farmingDirection.equalsIgnoreCase("right") ) {
                            KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindBack.getKeyCode(), false);
                            KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindRight.getKeyCode(), false);
                            KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), true);
                            KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindLeft.getKeyCode(), true);
                            Thread.sleep(500);
                            KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), false);
                            KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindLeft.getKeyCode(), false);
                        } else if (goingForwardOrBackward.equalsIgnoreCase("backward") && farmingDirection.equalsIgnoreCase("left") ) {
                            KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindBack.getKeyCode(), false);
                            KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindLeft.getKeyCode(), false);
                            KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), true);
                            KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindRight.getKeyCode(), true);
                            Thread.sleep(500);
                            KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), false);
                            KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindRight.getKeyCode(), false);
                        }
                        Thread.sleep(750);
                        pauseMacro();
                    }
                    onlyRunOnce2 = false;
                }
            } catch (Exception e) {
            }
        }).start();
    }

}