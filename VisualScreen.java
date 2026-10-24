package ru.violet.visual;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;

public class VisualScreen extends Screen {
	private static final int W = 220, ROW = 24;
	private int px, py, ph;

	public VisualScreen() { super(Text.literal("Violet Visual")); }

	@Override
	protected void init() {
		int rows = 8;
		ph = 40 + rows * ROW + 12;
		px = (width - W) / 2 - 10;
		py = (height - ph) / 2;
		int x = px + 20, w = W - 20, y = py + 34;

		addBtn(x, y, w, "Тотем: " + Cfg.TOTEM_NAMES[Cfg.totem], b -> {
			Cfg.totem = (Cfg.totem + 1) % Cfg.TOTEMS.length; Cfg.save(); clearAndInit();
		});
		y += ROW;
		addBtn(x, y, w, "Показать анимацию тотема", b -> {
			if (client != null) {
				client.setScreen(null);
				client.gameRenderer.showFloatingItem(new ItemStack(Items.TOTEM_OF_UNDYING));
			}
		});
		y += ROW;
		addDrawableChild(ButtonWidget.builder(Text.literal("-"), b -> { step(-0.1f); }).dimensions(x, y, 24, 20).build());
		addBtn(x + 26, y, w - 52, String.format("Растяг: %.1fx", Cfg.stretch), b -> {
			Cfg.stretch = 1.0f; Cfg.save(); clearAndInit();
		});
		addDrawableChild(ButtonWidget.builder(Text.literal("+"), b -> { step(0.1f); }).dimensions(x + w - 24, y, 24, 20).build());
		y += ROW;
		addBtn(x, y, w, "FPS-буст: " + onOff(Cfg.fpsBoost), b -> {
			Cfg.fpsBoost = !Cfg.fpsBoost; FpsBoost.set(Cfg.fpsBoost); Cfg.save(); clearAndInit();
		});
		y += ROW;
		addBtn(x, y, w, "Фулбрайт: " + onOff(Cfg.fullbright), b -> {
			Cfg.fullbright = !Cfg.fullbright; Cfg.save(); clearAndInit();
		});
		y += ROW;
		addBtn(x, y, w, "HUD: " + onOff(Cfg.hud), b -> {
			Cfg.hud = !Cfg.hud; Cfg.save(); clearAndInit();
		});
		y += ROW;
		addBtn(x, y, w, "Зум (удерживай C), FOV: " + Cfg.zoomFov, b -> {
			int i = 0;
			for (int k = 0; k < Cfg.ZOOM_FOVS.length; k++) if (Cfg.ZOOM_FOVS[k] == Cfg.zoomFov) i = k;
			Cfg.zoomFov = Cfg.ZOOM_FOVS[(i + 1) % Cfg.ZOOM_FOVS.length]; Cfg.save(); clearAndInit();
		});
		y += ROW;
		addBtn(x, y, w, "Закрыть", b -> close());
	}

	private void step(float d) {
		Cfg.stretch = Math.round(Math.max(0.5f, Math.min(2.0f, Cfg.stretch + d)) * 10f) / 10f;
		Cfg.save(); clearAndInit();
	}

	private void addBtn(int x, int y, int w, String label, ButtonWidget.PressAction a) {
		addDrawableChild(ButtonWidget.builder(Text.literal(label), a).dimensions(x, y, w, 20).build());
	}

	private static String onOff(boolean v) { return v ? "ВКЛ" : "ВЫКЛ"; }

	@Override
	public void renderBackground(DrawContext ctx, int mouseX, int mouseY, float delta) {
		super.renderBackground(ctx, mouseX, mouseY, delta);
		ctx.fill(px, py, px + W + 20, py + ph, 0xF0120820);
		ctx.fill(px, py, px + W + 20, py + 2, Hud.ACCENT);
		ctx.fill(px, py + 26, px + W + 20, py + 27, 0xFF3A1B66);
		ctx.drawText(textRenderer, "Violet Visual", px + 20, py + 10, Hud.ACCENT, true);
	}

	@Override
	public boolean shouldPause() { return false; }
}
