package net.akif.minecraftmod.item;

import net.akif.minecraftmod.MinecraftMod;
import net.akif.minecraftmod.item.custom.ChiselItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, MinecraftMod.MOD_ID);


    public static final RegistryObject<Item> ALEXANDRITE = ITEMS.register("alexandrite",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RAW_ALEXANDRITE = ITEMS.register("raw_alexandrite",
            () -> new Item(new Item.Properties()));


    public static final RegistryObject<Item> BRUSH = ITEMS.register("brush",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BRUSHX = ITEMS.register("brushx",
            () -> new Item(new Item.Properties()));



    public static final RegistryObject<Item> CHISEL = ITEMS.register("chisel",
            () -> new ChiselItem(new Item.Properties().durability(32)));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
