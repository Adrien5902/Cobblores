package net.ddns.adrien5902.cobblores;

import net.ddns.adrien5902.cobblores.ConfigManager.Config;
import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Block;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

public class Cobblores implements ModInitializer {

    public static final String MOD_NAMESPACE = "cobblores";

    private static Config config = null;
    private static RandomTable table = null;

    @Override
    public void onInitialize() {
        ServerLifecycleEvents.START_DATA_PACK_RELOAD.register((server, serverResourceManager) -> {
            reload();
        });
        ServerLifecycleEvents.SERVER_STARTED.register((server) -> {
            reload();
        });
    }

    public static void reload() {
        config = ConfigManager.readConfig();
        table = RandomTable.readFromConfig(config);
        ConfigManager.writeConfig(config);
    }

    public static Block getRandomBlock() {
        return table.getRandomBlock();
    }
}
