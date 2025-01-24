package net.akif.minecraftmod;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class InventorySorter {

    public static void sortInventory(Inventory inventory) {
        // Speichert alle Items ins Array. (Außer hotbar)
        List<ItemStack> allItems = new ArrayList<>();

        // Nimmt er alles mit
        for (int i = 9; i < inventory.getContainerSize(); i++) {
            ItemStack itemStack = inventory.getItem(i);
            if (!itemStack.isEmpty()) {
                allItems.add(itemStack.copy());
            }
        }

        // sortiert die Items nach Alphabet
        allItems.sort((stack1, stack2) -> stack1.getItem().getDescription().getString().compareTo(stack2.getItem().getDescription().getString()));

        // stacked die gleiche items
        List<ItemStack> stackedItems = new ArrayList<>();
        for (ItemStack itemStack : allItems) {
            boolean stacked = false;

            // versucht die items zu stacken die mehr als 1 hatten
            if (itemStack.getMaxStackSize() > 1) {
                for (ItemStack existingStack : stackedItems) {
                    if (ItemStack.isSameItem(itemStack, existingStack) && existingStack.getCount() < 64) {
                        int space = 64 - existingStack.getCount();
                        int toAdd = Math.min(itemStack.getCount(), space);
                        existingStack.grow(toAdd);  // addiert zu dem existierten stack zum adden
                        itemStack.shrink(toAdd);    // subtrahiert von dem original stack (bevor sortierte)
                        stacked = true;
                        if (itemStack.isEmpty()) break;
                    }
                }
            }

            // Wenn Item nicht stackbar ist wie z.B. Armor, Sword, ... macht er ein neue stack
            if (!stacked && !itemStack.isEmpty()) {
                itemStack.setCount(Math.min(itemStack.getCount(), 64));  // Cap at 64 per stack
                stackedItems.add(itemStack.copy());
            }
        }

        // Löscht alle items von dem inventar bevor sie es sortiert wieder added.
        for (int i = 9; i < inventory.getContainerSize(); i++) { // Alle slots außer hotbar (erste 9)
            inventory.setItem(i, ItemStack.EMPTY);
        }

        // Plaziert die sortierten items zurück in das inventar
        int index = 9;  // plaziert erst nach dem ersten 9 slots (hotbar)
        for (ItemStack itemStack : stackedItems) {
            if (index < 36) {  // plaziert items nur in den inventar
                if (inventory.getItem(index).isEmpty()) {
                    inventory.setItem(index++, itemStack);  // Plaziert die sortiere Items in den nächsten verfügbaren slot
                } else {
                    // überspringt die gefüllte slots und plaziert in den nächsten (++)
                    index++;
                }
            }
        }
    }
}