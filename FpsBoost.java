package ru.violet.visual;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.GraphicsMode;
import net.minecraft.client.option.ParticlesMode;

/** Включает набор настроек для максимального FPS и умеет вернуть всё как было. */
public class FpsBoost {
	private static boolean saved = false;
	private static int view, blend, maxFps;
	private static double entityScale;
	private static boolean vsync, shadows;
	private static ParticlesMode particles;
	private static GraphicsMode graphics;

	public static void set(boolean on) {
		MinecraftClient mc = MinecraftClient.getInstance();
		GameOptions o = mc.options;
		if (o == null) return;
		if (on) {
			if (!saved) {
				view = o.getViewDistance().getValue();
				blend = o.getBiomeBlendRadius().getValue();
				maxFps = o.getMaxFps().getValue();
				entityScale = o.getEntityDistanceScaling().getValue();
				vsync = o.getEnableVsync().getValue();
				shadows = o.getEntityShadows().getValue();
				particles = o.getParticles().getValue();
				graphics = o.getGraphicsMode().getValue();
				saved = true;
			}
			o.getViewDistance().setValue(Math.min(view, 8));
			o.getBiomeBlendRadius().setValue(0);
			o.getMaxFps().setValue(260);
			o.getEntityDistanceScaling().setValue(0.5);
			o.getEnableVsync().setValue(false);
			o.getEntityShadows().setValue(false);
			o.getParticles().setValue(ParticlesMode.MINIMAL);
			o.getGraphicsMode().setValue(GraphicsMode.FAST);
		} else if (saved) {
			o.getViewDistance().setValue(view);
			o.getBiomeBlendRadius().setValue(blend);
			o.getMaxFps().setValue(maxFps);
			o.getEntityDistanceScaling().setValue(entityScale);
			o.getEnableVsync().setValue(vsync);
			o.getEntityShadows().setValue(shadows);
			o.getParticles().setValue(particles);
			o.getGraphicsMode().setValue(graphics);
			saved = false;
		}
		o.write();
	}
}
