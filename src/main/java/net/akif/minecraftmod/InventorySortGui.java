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

    // Die Locations für den png dateien.

    private static final ResourceLocation BUTTON_UNFOCUSED =
            ResourceLocation.fromNamespaceAndPath("minecraftmod", "textures/gui/button_unfocused.png");

    private static final ResourceLocation BUTTON_FOCUSED =
            ResourceLocation.fromNamespaceAndPath("minecraftmod", "textures/gui/button_focused.png");

    // Wenn inventar geöffnet wird ---- Bei verwendung von SubscribeEvent müssen wir nicht in das main mod class dieses mod registrieren oder aufrufen.
    @SubscribeEvent
    public static void onGuiOpen(ScreenEvent.Init.Post event) {
        if (event.getScreen() instanceof InventoryScreen inventoryScreen) {
            int recipeBookX = ((AbstractContainerScreen<?>) inventoryScreen).getGuiLeft() + 104;
            int y = ((AbstractContainerScreen<?>) inventoryScreen).getGuiTop() + 61;
            int x = recipeBookX + 24;

            CustomSortButton sortButton = new CustomSortButton(
                    x, y, BUTTON_WIDTH, BUTTON_HEIGHT,
                    Component.literal(""),
                    button -> {

                        Minecraft minecraft = Minecraft.getInstance();
                        if (minecraft.getSingleplayerServer() != null) {
                            ServerPlayer serverPlayer = minecraft.getSingleplayerServer().getPlayerList().getPlayerByName("Dev");

                            // Sortier das inventar
                            assert serverPlayer != null;
                            InventorySorter.sortInventory(serverPlayer.getInventory());

                            // Bescheid geben das die inventar sortiert wurde. ( In Game )
                            serverPlayer.sendSystemMessage(Component.literal("Your inventory has been sorted!"));

                            // LOGGER in Run bzw Bescheid geber
                            System.out.println("Inventory sorted for player: " + serverPlayer.getName().getString());

                            // Alle Inventar Änderungen speichern
                            serverPlayer.inventoryMenu.broadcastChanges();
                        }
                    }
            );
            event.addListener(sortButton);
        }
    }

    // Erstellung von CustomWidget (Button)
    private static class CustomSortButton extends Button {
        public CustomSortButton(int x, int y, int width, int height, Component message, OnPress onPress) {
            super(x, y, width, height, message, onPress, DEFAULT_NARRATION);
        }

        @Override
        public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            // Welche png soll verwendet werden wenn isHovered() ?
            ResourceLocation buttonTexture;
            if (isHovered()) buttonTexture = BUTTON_FOCUSED;
            else buttonTexture = BUTTON_UNFOCUSED;

            // Default Textures
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