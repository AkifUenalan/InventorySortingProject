package net.akif.minecraftmod;

import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(MinecraftMod.MOD_ID)
public class MinecraftMod {

    public static final String MOD_ID = "invsortingmod";
    public static final Logger LOGGER = LogUtils.getLogger();

    public MinecraftMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        // Register the commonSetup method for modloading
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
    }
}



