package me.phoenix.netherwartmacro.utils;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

public class KeyBinds {

    public static KeyBinding[] keyBindings;

    public static void init() {
        keyBindings = new KeyBinding[3];

        keyBindings[0] = new KeyBinding("Starts the Netherwart Macro", Keyboard.KEY_F8, "Macro");
        keyBindings[1] = new KeyBinding("Stops the Netherwart Macro", Keyboard.KEY_F9, "Macro");
        keyBindings[2] = new KeyBinding("Pauses/Unpauses the Netherwart Macro", Keyboard.KEY_F7, "Macro");

        for (int i = 0; i < keyBindings.length; ++i)
        {
            ClientRegistry.registerKeyBinding(keyBindings[i]);
        }

    }

}
