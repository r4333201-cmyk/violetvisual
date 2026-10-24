package ru.violet.visual;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Files;
import java.nio.file.Path;

public class Cfg {
	public static final String[] TOTEMS = {"original", "violet", "crimson", "ice", "gold", "emerald", "void"};
	public static final String[] TOTEM_NAMES = {"Оригинал", "Фиолетовый", "Алый", "Ледяной", "Золотой", "Изумрудный", "Бездна"};
	public static final int[] ZOOM_FOVS = {30, 40, 50};

	public static int totem = 1;
	public static float stretch = 1.0f;
	public static boolean hud = true;
	public static boolean fpsBoost = false;
	public static boolean fullbright = false;
	public static int zoomFov = 30;

	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static Path file() { return FabricLoader.getInstance().getConfigDir().resolve("violetvisual.json"); }

	private static class Data {
		int totem = 1; float stretch = 1.0f; boolean hud = true; boolean fpsBoost = false; boolean fullbright = false; int zoomFov = 30;
	}

	public static void load() {
		try {
			if (!Files.exists(file())) return;
			Data d = GSON.fromJson(Files.readString(file()), Data.class);
			if (d == null) return;
			totem = Math.max(0, Math.min(TOTEMS.length - 1, d.totem));
			stretch = Math.max(0.5f, Math.min(2.0f, d.stretch));
			hud = d.hud; fpsBoost = d.fpsBoost; fullbright = d.fullbright;
			zoomFov = Math.max(30, Math.min(110, d.zoomFov));
		} catch (Exception e) {
			VioletVisual.LOG.warn("Не удалось прочитать конфиг", e);
		}
	}

	public static void save() {
		try {
			Data d = new Data();
			d.totem = totem; d.stretch = stretch; d.hud = hud; d.fpsBoost = fpsBoost; d.fullbright = fullbright; d.zoomFov = zoomFov;
			Files.writeString(file(), GSON.toJson(d));
		} catch (Exception e) {
			VioletVisual.LOG.warn("Не удалось сохранить конфиг", e);
		}
	}
}
