package dev.txuritan.unmending.mixins;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ItemMixin {

	@Inject(method = "canRepair", at = @At("HEAD"), cancellable = true)
	private void canRepair(ItemStack stack, ItemStack ingredient, CallbackInfoReturnable<Boolean> cir) {
		if ((stack.getItem() == Items.TRIDENT) && (ingredient.getItem() == Items.PRISMARINE_CRYSTALS)) {
			cir.setReturnValue(true);
		}
	}
}
