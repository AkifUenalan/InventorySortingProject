package net.akif.minecraftmod;


import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;


@Mod(MinecraftMod.MOD_ID)
public class MinecraftMod {

    public static final String MOD_ID = "invsortingmod";

    public MinecraftMod() {
        MinecraftForge.EVENT_BUS.register(this);

    }
}



