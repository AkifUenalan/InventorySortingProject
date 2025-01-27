package net.akif.minecraftmod;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "invsortingmod", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class InventorySortGui {

    private static final int BUTTON_WIDTH = 20;
    private static final int BUTTON_HEIGHT = 18;

    // Button textures
    private static final ResourceLocation BUTTON_UNFOCUSED =
            ResourceLocation.fromNamespaceAndPath("minecraftmod", "textures/gui/button_unfocused.png");
    private static final ResourceLocation BUTTON_FOCUSED =
            ResourceLocation.fromNamespaceAndPath("minecraftmod", "textures/gui/button_focused.png");

    private static final ResourceLocation BOOK_UNFOCUSED =
            ResourceLocation.fromNamespaceAndPath("minecraftmod", "textures/gui/book_unfocused.png");
    private static final ResourceLocation BOOK_FOCUSED =
            ResourceLocation.fromNamespaceAndPath("minecraftmod", "textures/gui/book_focused.png");

    @SubscribeEvent
    public static void onGuiOpen(ScreenEvent.Init.Post event) {
        if (event.getScreen() instanceof InventoryScreen inventoryScreen) {
            int recipeBookX = ((AbstractContainerScreen<?>) inventoryScreen).getGuiLeft() + 106;
            int y = ((AbstractContainerScreen<?>) inventoryScreen).getGuiTop() + 61;
            int x = recipeBookX + 24;

            // erste button mit dem regulären texturen
            CustomSortButton sortButtonAlphabet = new CustomSortButton(
                    x, y, BUTTON_WIDTH, BUTTON_HEIGHT,
                    Component.literal(""),
                    button -> {
                        Minecraft minecraft = Minecraft.getInstance();
                        if (minecraft.getSingleplayerServer() != null) {
                            ServerPlayer serverPlayer = minecraft.getSingleplayerServer().getPlayerList().getPlayerByName("Dev");

                            assert serverPlayer != null;
                            InventorySorter.sortInventory(serverPlayer.getInventory(), false);
                            serverPlayer.sendSystemMessage(Component.literal("Your inventory is now sorted alphabetically."));
                            System.out.println("Inventory sorted for player: " + serverPlayer.getName().getString());
                            serverPlayer.inventoryMenu.broadcastChanges();
                        }
                    },
                    BUTTON_UNFOCUSED,  // Unfocused texture
                    BUTTON_FOCUSED     // Focused texture
            );
            event.addListener(sortButtonAlphabet);
        }
    }

    @SubscribeEvent
    public static void onGuiOpen2(ScreenEvent.Init.Post event) {
        if (event.getScreen() instanceof InventoryScreen inventoryScreen) {
            int recipeBookX = ((AbstractContainerScreen<?>) inventoryScreen).getGuiLeft() + 127;
            int y = ((AbstractContainerScreen<?>) inventoryScreen).getGuiTop() + 61;
            int x = recipeBookX + 24;

            //zweite button mit dem book texturen
            CustomSortButton sortButtonQuantity = new CustomSortButton(
                    x, y, BUTTON_WIDTH, BUTTON_HEIGHT,
                    Component.literal(""),
                    button -> {
                        Minecraft minecraft = Minecraft.getInstance();
                        if (minecraft.getSingleplayerServer() != null) {
                            ServerPlayer serverPlayer = minecraft.getSingleplayerServer().getPlayerList().getPlayerByName("Dev");
                            assert serverPlayer != null;

                            InventorySorter.sortInventory(serverPlayer.getInventory(), true);

                            serverPlayer.sendSystemMessage(Component.literal("Your inventory is now sorted by quantity."));
                            System.out.println("Inventory sorted for player: " + serverPlayer.getName().getString());
                            serverPlayer.inventoryMenu.broadcastChanges();
                        }
                    },
                    BOOK_UNFOCUSED,   // Unfocused texture
                    BOOK_FOCUSED      // Focused texture
            );
            event.addListener(sortButtonQuantity);
        }
    }

    // button stuff classe die den Originalen Button klasse extendiert
    private static class CustomSortButton extends Button {
        private final ResourceLocation unfocusedTexture;
        private final ResourceLocation focusedTexture;

        public CustomSortButton(int x, int y, int width, int height, Component message, OnPress onPress,
                                ResourceLocation unfocusedTexture, ResourceLocation focusedTexture) {
            super(x, y, width, height, message, onPress, DEFAULT_NARRATION);
            this.unfocusedTexture = unfocusedTexture;
            this.focusedTexture = focusedTexture;
        }

        @Override
        public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            ResourceLocation buttonTexture = isHovered() ? focusedTexture : unfocusedTexture;

            RenderSystem.setShaderTexture(0, buttonTexture);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

            guiGraphics.blit(buttonTexture, this.getX(), this.getY(), 0, 0,
                    BUTTON_WIDTH, BUTTON_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT);

            RenderSystem.disableBlend();
        }
    }
}