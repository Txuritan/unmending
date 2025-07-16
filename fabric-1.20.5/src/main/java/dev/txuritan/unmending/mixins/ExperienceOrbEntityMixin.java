package dev.txuritan.unmending.mixins;

import dev.txuritan.unmending.Unmending;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(ExperienceOrbEntity.class)
public class ExperienceOrbEntityMixin {
    @Inject(method = "repairPlayerGears", at = @At("HEAD"), cancellable = true)
    private void disableRepairPlayerGears(PlayerEntity player, int amount, CallbackInfoReturnable<Integer> info) {
        final Map.Entry<EquipmentSlot, ItemStack> entry = EnchantmentHelper.chooseEquipmentWith(Enchantments.MENDING, player, ItemStack::isDamaged);
        if (entry == null) {
            return;
        }

        final ItemStack itemStack = entry.getValue();

        final ItemEnchantmentsComponent enchantments = EnchantmentHelper.getEnchantments(itemStack);
        if (!Unmending.hasMending(enchantments)) {
            return;
        }

        info.setReturnValue(amount);
        info.cancel();
    }
}
