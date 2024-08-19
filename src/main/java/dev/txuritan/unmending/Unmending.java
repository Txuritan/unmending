package dev.txuritan.unmending;

import dev.txuritan.unmending.api.events.AnvilUpdateEvent;
import net.fabricmc.api.ModInitializer;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.ActionResult;

public class Unmending implements ModInitializer {
    @Override
    public void onInitialize() {
        AnvilUpdateEvent.EVENT.register(this::handleAnvilUpdateEvent);
    }

    private ActionResult handleAnvilUpdateEvent(AnvilUpdateEvent event) {
        ItemStack left = event.getLeft();
        ItemStack right = event.getRight();
        ItemStack out = event.getOutput();

        if (out.isEmpty() && (left.isEmpty() || right.isEmpty())) {
            return ActionResult.PASS;
        }

        boolean isMended = false;

        ItemEnchantmentsComponent leftEnchantments = getEnchantments(left);
        ItemEnchantmentsComponent rightEnchantments = getEnchantments(right);

        if (hasMending(leftEnchantments) || hasMending(rightEnchantments)) {
            if (left.getItem() == right.getItem()) {
                isMended = true;
            }

            if (right.getItem() == Items.ENCHANTED_BOOK) {
                isMended = true;
            }
        }

        if (isMended) {
            if (out.isEmpty()) {
                out = left.copy();
            }

            out.set(DataComponentTypes.ENCHANTMENTS, ItemEnchantmentsComponent.DEFAULT);
            addEnchantments(leftEnchantments, out);
            addEnchantments(rightEnchantments, out);

            out.set(DataComponentTypes.REPAIR_COST, 0);

            event.setOutput(out);
            if (event.getCost() == 0) {
                event.setCost(1);
            }

            return ActionResult.CONSUME;
        }

        return ActionResult.PASS;
    }

    private static boolean hasMending(ItemEnchantmentsComponent enchantments) {
        for (RegistryEntry<Enchantment> enchantment : enchantments.getEnchantments()) {
            if (enchantment.value()
                    .effects()
                    .contains(EnchantmentEffectComponentTypes.REPAIR_WITH_XP)) {
                return true;
            }
        }
        return false;
    }

    private static void addEnchantments(ItemEnchantmentsComponent enchantments, ItemStack destination) {
        for (RegistryEntry<Enchantment> enchantment : enchantments.getEnchantments()) {
            //Skip adding mending
            if (enchantment.matchesKey(Enchantments.MENDING)) {
                continue;
            }

            int level = enchantments.getLevel(enchantment);

            destination.addEnchantment(enchantment, level);
        }
    }

    private static ItemEnchantmentsComponent getEnchantments(ItemStack item) {
        if (item.getItem() == Items.ENCHANTED_BOOK) {
            return item.getOrDefault(DataComponentTypes.STORED_ENCHANTMENTS, ItemEnchantmentsComponent.DEFAULT);
        }
        return item.getOrDefault(DataComponentTypes.ENCHANTMENTS, ItemEnchantmentsComponent.DEFAULT);
    }
}
