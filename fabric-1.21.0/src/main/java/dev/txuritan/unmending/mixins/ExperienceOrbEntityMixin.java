package dev.txuritan.unmending.mixins;

import dev.txuritan.unmending.Unmending;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(ExperienceOrbEntity.class)
public class ExperienceOrbEntityMixin {
    @Inject(method = "repairPlayerGears", at = @At("HEAD"), cancellable = true)
    private void disableRepairPlayerGears(ServerPlayerEntity player, int amount, CallbackInfoReturnable<Integer> info) {
        final Optional<EnchantmentEffectContext> optional = EnchantmentHelper.chooseEquipmentWith(EnchantmentEffectComponentTypes.REPAIR_WITH_XP, player, ItemStack::isDamaged);
        if (optional.isEmpty()) {
            return;
        }

        final ItemStack itemStack = optional.get().stack();

        final ItemEnchantmentsComponent enchantments = EnchantmentHelper.getEnchantments(itemStack);
        if (!Unmending.hasMending(enchantments)) {
            return;
        }

        info.setReturnValue(amount);
        info.cancel();
    }
}
