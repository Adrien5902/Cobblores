package net.ddns.adrien5902.cobblores.client;

import net.ddns.adrien5902.cobblores.Cobblores;
import net.ddns.adrien5902.cobblores.ConfigManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.Identifier;

public class CobbloresClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(
                new Identifier(Cobblores.MOD_NAMESPACE, "send_config"),
                (client, handler, buf, responseSender) -> {
                    String configData = buf.readString();
                    client.execute(() -> {
                        System.out.println("aaaa");
                        Screen screen = MinecraftClient.getInstance().currentScreen;
                        if (screen instanceof ConfigScreen) {
                            ConfigManager.Config config = ConfigManager.Config.fromJson(configData);
                            ConfigScreen configScreen = (ConfigScreen) screen;
                            if (configData != null) {
                                configScreen.update(config);
                            } else {
                                configScreen.blocksConfig
                                        .addError(new Exception("Can't config cobblores on this server"));
                            }
                        }
                    });
                });
    }
}
