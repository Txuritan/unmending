package dev.txuritan.unmending;

import dev.txuritan.unmending.api.events.AnvilUpdateEvent;

import net.fabricmc.api.ModInitializer;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.ActionResult;

import org.jetbrains.annotations.NotNull;

public class Unmending implements ModInitializer {
    @Override
    public void onInitialize() {
        AnvilUpdateEvent.EVENT.register(event -> {
            final ItemStack left = event.getLeft();
            final ItemStack right = event.getRight();
            ItemStack out = event.getOutput();

            if (out.isEmpty() && (left.isEmpty() || right.isEmpty())) {
                return ActionResult.PASS;
            }

            boolean isMended = false;

            final ItemEnchantmentsComponent enchantmentsLeft = EnchantmentHelper.getEnchantments(left);
            final ItemEnchantmentsComponent enchantmentsRight = EnchantmentHelper.getEnchantments(right);

            if (hasMending(enchantmentsLeft) || hasMending(enchantmentsRight)) {
                if (left.getItem() == right.getItem()) {
                    isMended = true;
                }

                if (right.getItem() == Items.ENCHANTED_BOOK) {
                    isMended = true;
                }
            }

            if (!isMended) {
                return ActionResult.PASS;
            }

            if (out.isEmpty()) {
                out = left.copy();
            }

            out.set(DataComponentTypes.ENCHANTMENTS, ItemEnchantmentsComponent.DEFAULT);

            addEnchantments(enchantmentsLeft, out);
            addEnchantments(enchantmentsRight, out);

            out.set(DataComponentTypes.REPAIR_COST, 0);
            if (out.isDamageable()) {
                out.setDamage(0);
            }

            event.setOutput(out);
            if (event.getCost() == 0) {
                event.setCost(1);
            }

            return ActionResult.CONSUME;

        });
    }

    private static boolean hasMending(final @NotNull ItemEnchantmentsComponent enchantments) {
        for (final RegistryEntry<Enchantment> enchantment : enchantments.getEnchantments()) {
            if (enchantment.matchesKey(Enchantments.MENDING)) {
                return true;
            }
        }

        return false;
    }

    private static void addEnchantments(final @NotNull ItemEnchantmentsComponent enchantments, final @NotNull ItemStack itemStack) {
        for (final RegistryEntry<Enchantment> enchantment : enchantments.getEnchantments()) {
            if (enchantment.matchesKey(Enchantments.MENDING)) {
                continue;
            }

            itemStack.addEnchantment(enchantment, enchantments.getLevel(enchantment));
        }
    }
}