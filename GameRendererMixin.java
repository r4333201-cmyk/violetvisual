package ru.violet.visual.mixin;

import net.minecraft.client.render.GameRenderer;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.violet.visual.Cfg;
import ru.violet.visual.VioletVisual;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

	/** Подмена модели тотема в анимации «пропа». */
	@ModifyVariable(method = "showFloatingItem", at = @At("HEAD"), argsOnly = true)
	private ItemStack violetvisual$skin(ItemStack stack) {
		if (Cfg.totem <= 0 || !stack.isOf(Items.TOTEM_OF_UNDYING)) return stack;
		ItemStack copy = stack.copy();
		copy.set(DataComponentTypes.ITEM_MODEL, Identifier.of(VioletVisual.ID, "totem_" + Cfg.TOTEMS[Cfg.totem]));
		return copy;
	}

	/** Растяг: растягиваем проекцию мира по горизонтали. */
	@Inject(method = "getBasicProjectionMatrix", at = @At("RETURN"))
	private void violetvisual$stretch(CallbackInfoReturnable<Matrix4f> cir) {
		float s = Cfg.stretch;
		if (s != 1.0f && cir.getReturnValue() != null) cir.getReturnValue().scaleLocal(s, 1.0f, 1.0f);
	}
}
