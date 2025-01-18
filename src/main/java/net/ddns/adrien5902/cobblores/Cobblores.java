package net.ddns.adrien5902.cobblores;

import net.ddns.adrien5902.cobblores.ConfigManager.Config;
import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Block;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

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

        ServerPlayNetworking.registerGlobalReceiver(new Identifier(Cobblores.MOD_NAMESPACE, "request_config"),
                (server, player, handler, buf, responseSender) -> {
                    if (player.hasPermissionLevel(4)) {
                        PacketByteBuf responseBuf = PacketByteBufs.create();
                        responseBuf.writeString(config.toJson());
                        responseSender.sendPacket(new Identifier(Cobblores.MOD_NAMESPACE, "send_config"), responseBuf);
                    }
                });

        ServerPlayNetworking.registerGlobalReceiver(new Identifier(Cobblores.MOD_NAMESPACE, "set_config"),
                (server, player, handler, buf, responseSender) -> {
                    if (player.hasPermissionLevel(4)) {
                        setConfig(ConfigManager.Config.fromJson(buf.readString()));
                    }
                });
    }

    public static void reload() {
        ConfigManager.Config newConfig = ConfigManager.readConfig();
        setConfig(newConfig);
    }

    public static void setConfig(ConfigManager.Config newConfig) {
        config = newConfig;
        table = RandomTable.readFromConfig(config);
        ConfigManager.writeConfig(config);
    }

    public static ConfigManager.Config getConfig() {
        return config;
    }

    public static RandomTable getBlockTable() {
        return table;
    }

    public static Block getRandomBlock() {
        return table.getRandomBlock();
    }
}
