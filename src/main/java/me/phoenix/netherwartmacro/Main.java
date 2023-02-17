package me.phoenix.netherwartmacro;

import me.phoenix.netherwartmacro.Macro.Macro;
import me.phoenix.netherwartmacro.commands.ConfigGuiCommand;
import me.phoenix.netherwartmacro.fastbreak.Config;
import me.phoenix.netherwartmacro.fastbreak.fastbreak;
import me.phoenix.netherwartmacro.fastbreak.fastbreakToggle;
import me.phoenix.netherwartmacro.utils.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.io.File;


@Mod(name = "NetherWart Macro", modid = "NetherWart", version = "1.0", acceptedMinecraftVersions = "[1.8.9]", clientSideOnly = true)
public class Main {

    public static GuiScreen displayScreen;

    public static final Minecraft mc = Minecraft.getMinecraft();
    public static Config config = new Config();

    public static File modDir = new File(new File(mc.mcDataDir, "config"), "netherwartmacro");

    public static Locations location = Locations.NULL;


    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        if(!modDir.exists())
            modDir.mkdir();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        config.initialize();

        KeyBinds.init();
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new Macro());
        MinecraftForge.EVENT_BUS.register(new ScoreboardUtils());
        MinecraftForge.EVENT_BUS.register(new Utils());
        MinecraftForge.EVENT_BUS.register(new fastbreak());

        ClientCommandHandler.instance.registerCommand(new ConfigGuiCommand());
        ClientCommandHandler.instance.registerCommand(new fastbreakToggle());
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {

    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if(event.phase != TickEvent.Phase.START) return;

        if(displayScreen != null) {
            mc.displayGuiScreen(displayScreen);
            displayScreen = null;
        }
    }



}
