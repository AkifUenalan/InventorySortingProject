package net.akif.minecraftmod;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class InventorySorter {

    public static void sortInventory(Inventory inventory, boolean byQuantity) {
        // Alle items außer hotbar, armorslots, offhand
        List<ItemStack> allItems = new ArrayList<>();


        for (int i = 9; i < 36; i++) {
            ItemStack itemStack = inventory.getItem(i);
            if (!itemStack.isEmpty()) {
                allItems.add(itemStack.copy());
            }
        }

        // sortiert nach alphabet oder quantity mit boolean
        if (!byQuantity) {
            allItems.sort((stack1, stack2) ->
                    stack1.getItem().getDescription().getString()
                            .compareTo(stack2.getItem().getDescription().getString()));
        } else {
            allItems.sort((stack1, stack2) ->
                    Integer.compare(stack2.getCount(), stack1.getCount()));
        }

        // Stacked gleiche items
        List<ItemStack> stackedItems = new ArrayList<>();
        for (ItemStack itemStack : allItems) {
            boolean stacked = false;

            // versucht items zu stacken die schon mehr als 1 waren
            if (itemStack.getMaxStackSize() > 1) {
                for (ItemStack existingStack : stackedItems) {
                    if (ItemStack.isSameItem(itemStack, existingStack) && existingStack.getCount() < 64) {
                        int space = 64 - existingStack.getCount();
                        int toAdd = Math.min(itemStack.getCount(), space);
                        existingStack.grow(toAdd);  // zu dem existierenden stack adden
                        itemStack.shrink(toAdd);    // von dem originalen stack löschen
                        stacked = true;
                        if (itemStack.isEmpty()) break;
                    }
                }
            }

            // Wenn Item nicht stacked werden kann wie z.B Swords
            if (!stacked && !itemStack.isEmpty()) {
                itemStack.setCount(Math.min(itemStack.getCount(), 64));  // 64 Stack limit
                stackedItems.add(itemStack.copy());
            }
        }

        // Löscht alle slots
        for (int i = 9; i < 36; i++) {
            inventory.setItem(i, ItemStack.EMPTY);
        }

        // plaziert sortierte items in den inventar / nach dem 9 ten slot
        int index = 9;
        for (ItemStack itemStack : stackedItems) {
            if (index < 36) {
                if (inventory.getItem(index).isEmpty()) {
                    inventory.setItem(index++, itemStack);
                } else {
                    index++;
                }
            }
        }
    }
}