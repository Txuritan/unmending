package dev.txuritan.unmending.mixins;

import net.minecraft.component.ComponentType;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.function.Predicate;

@Mixin(EnchantmentHelper.class)
public abstract class EnchantmentHelperMixin {

	@Inject(method = "chooseEquipmentWith", cancellable = true, at = @At("HEAD"))
	private static void chooseEquipmentWith(
			ComponentType<?> componentType,
			LivingEntity entity,
			Predicate<ItemStack> stackPredicate,
			CallbackInfoReturnable<Optional<EnchantmentEffectContext>> cir
	) {
		if (componentType == EnchantmentEffectComponentTypes.REPAIR_WITH_XP) {
			cir.setReturnValue(Optional.empty());
		}
	}

}
