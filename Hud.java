package ru.violet.visual;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.ArrayList;
import java.util.List;

public class Hud {
	public static final int BG = 0xB0140A24;
	public static final int ACCENT = 0xFFB06BFF;
	public static final int TEXT = 0xFFEBDDFF;

	public static void render(DrawContext ctx, RenderTickCounter tick) {
		MinecraftClient mc = MinecraftClient.getInstance();
		if (!Cfg.hud || mc.player == null || mc.options.hudHidden) return;
		TextRenderer tr = mc.textRenderer;

		List<String> lines = new ArrayList<>();
		lines.add("Violet Visual");
		lines.add("FPS: " + mc.getCurrentFps());
		PlayerListEntry e = mc.getNetworkHandler() == null ? null : mc.getNetworkHandler().getPlayerListEntry(mc.player.getUuid());
		lines.add("Пинг: " + (e == null ? "-" : e.getLatency() + " мс"));
		lines.add(String.format("XYZ: %.0f %.0f %.0f", mc.player.getX(), mc.player.getY(), mc.player.getZ()));
		lines.add("Курс: " + mc.player.getHorizontalFacing().asString());
		lines.add("Тотемов: " + countTotems(mc.player.getInventory()));

		int w = 0;
		for (String s : lines) w = Math.max(w, tr.getWidth(s));
		int x = 6, y = 6, h = lines.size() * 11 + 6;
		ctx.fill(x, y, x + w + 12, y + h, BG);
		ctx.fill(x, y, x + 2, y + h, ACCENT);
		int ty = y + 4;
		for (int i = 0; i < lines.size(); i++) {
			ctx.drawText(tr, lines.get(i), x + 7, ty, i == 0 ? ACCENT : TEXT, true);
			ty += 11;
		}
	}

	private static int countTotems(PlayerInventory inv) {
		int n = 0;
		for (int i = 0; i < inv.size(); i++) {
			ItemStack s = inv.getStack(i);
			if (s.isOf(Items.TOTEM_OF_UNDYING)) n += s.getCount();
		}
		return n;
	}
}
