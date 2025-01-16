package net.ddns.adrien5902.cobblores;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registries;

public class ConfigManager {
    public static File getFile() {
        return FabricLoader.getInstance().getConfigDir().resolve(Cobblores.MOD_NAMESPACE + ".json").toFile();
    }

    public static Config readConfig() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(getFile(), Config.class);
        } catch (IOException e) {
            return Config.DEFAULT;
        }
    }

    public static void writeConfig(Config config) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(getFile(), config);
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
    }
}