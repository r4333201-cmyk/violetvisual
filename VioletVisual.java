package ru.violet.visual;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VioletVisual implements ClientModInitializer {
	public static final String ID = "violetvisual";
	public static final Logger LOG = LoggerFactory.getLogger(ID);

	private static KeyBinding menuKey, zoomKey;
	private static boolean zooming = false, fbApplied = false;
	private static int savedFov = 70;

	@Override
	public void onInitializeClient() {
		Cfg.load();

		KeyBinding.Category cat = KeyBinding.Category.create(Identifier.of(ID, "main"));
		menuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.violetvisual.menu", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_SHIFT, cat));
		zoomKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.violetvisual.zoom", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_C, cat));

		HudElementRegistry.attachElementAfter(VanillaHudElements.MISC_OVERLAYS, Identifier.of(ID, "hud"), Hud::render);

		ClientTickEvents.END_CLIENT_TICK.register(this::tick);
	}

	private void tick(MinecraftClient mc) {
		while (menuKey.wasPressed()) mc.setScreen(new VisualScreen());
		if (mc.player == null || mc.options == null) return;

		// Зум (удержание клавиши): временно меняем FOV
		boolean down = zoomKey.isPressed() && mc.currentScreen == null;
		if (down && !zooming) {
			savedFov = mc.options.getFov().getValue();
			mc.options.getFov().setValue(Cfg.zoomFov);
			zooming = true;
		} else if (!down && zooming) {
			mc.options.getFov().setValue(savedFov);
			zooming = false;
		}

		// Фулбрайт: клиентский эффект ночного зрения (без иконки и частиц)
		if (Cfg.fullbright) {
			if (!mc.player.hasStatusEffect(StatusEffects.NIGHT_VISION)) {
				mc.player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 32767, 0, false, false, false));
			}
			fbApplied = true;
		} else if (fbApplied) {
			mc.player.removeStatusEffect(StatusEffects.NIGHT_VISION);
			fbApplied = false;
		}
	}
}
