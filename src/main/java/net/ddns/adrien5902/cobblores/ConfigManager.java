package net.ddns.adrien5902.cobblores;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registries;

public class ConfigManager {
    private static Gson gson = new Gson();

    public static Path getPath() {
        return FabricLoader.getInstance().getConfigDir().resolve(Cobblores.MOD_NAMESPACE + ".json");
    }

    public static Config readConfig() {
        try {
            String json = Files.readString(getPath());
            return Config.fromJson(json);
        } catch (IOException e) {
            return Config.DEFAULT;
        }
    }

    public static void writeConfig(Config config) {
        try {
            Files.writeString(getPath(), config.toJson());
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public static class Config {
        public static Config DEFAULT = new Config();
        public Map<String, Integer> blocks;

        Config() {
            blocks = Map.of(Registries.BLOCK.getId(Blocks.COBBLESTONE).toString(), 1);
        }

        public static ConfigManager.Config fromJson(String json) {
            return gson.fromJson(json, ConfigManager.Config.class);
        }

        public String toJson() {
            return gson.toJson(this);
        }

        public void setBlocksTable(RandomTable blocksTable) {
            HashMap<String, Integer> hashMap = new HashMap<>();
            for (var entry : blocksTable.blocks.entrySet()) {
                hashMap.put(Registries.BLOCK.getId(entry.getKey()).toString(), entry.getValue());
            }

            blocks = hashMap;
        }
    }
}