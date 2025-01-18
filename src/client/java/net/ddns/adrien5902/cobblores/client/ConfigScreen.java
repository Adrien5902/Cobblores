package net.ddns.adrien5902.cobblores.client;

import java.util.HashMap;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;

import net.ddns.adrien5902.cobblores.Cobblores;
import net.ddns.adrien5902.cobblores.ConfigManager;
import net.ddns.adrien5902.cobblores.RandomTable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ConfigScreen extends Screen implements ConfigScreenFactory<ConfigScreen> {
    private Screen parent;
    public BlocksConfig blocksConfig;
    public ConfigManager.Config config;

    @Environment(EnvType.CLIENT)
    public ConfigScreen() {
        super(Text.literal("Cobblores config"));
    }

    @Environment(EnvType.CLIENT)
    @Override
    protected void init() {
        HashMap<Block, Integer> table = new HashMap<>();
        this.blocksConfig = new BlocksConfig(this, new RandomTable(table));
        this.addSelectableChild(this.blocksConfig);

        this.addDrawableChild(
                ButtonWidget.builder(Text.literal("+ Add block"), button -> {
                    this.blocksConfig.table.blocks.put(Blocks.AIR, 1);
                    this.blocksConfig.update();
                })
                        .dimensions(this.width / 2 - 155, this.height - 29, 150, 20)
                        .build());

        this.addDrawableChild(
                ButtonWidget.builder(ScreenTexts.DONE, button -> onDone())
                        .dimensions(this.width / 2 - 155 + 160, this.height - 29, 150, 20)
                        .build());

        try {
            this.retrieveConfig();
        } catch (Exception e) {
            this.blocksConfig.addError(e);
        }
    }

    @Environment(EnvType.CLIENT)
    public void update(ConfigManager.Config config) {
        this.config = config;
        RandomTable blocksTable = RandomTable.readFromConfig(this.config);
        this.blocksConfig.table = blocksTable;
        this.blocksConfig.update();
    }

    public void retrieveConfig() throws Exception {
        if (client.getCurrentServerEntry() == null) {
            ConfigManager.Config config = Cobblores.getConfig();

            if (config == null) {
                throw new Exception("Please log into a world to start editing cobblores config");
            }

            this.update(config);
        } else {
            // TODO: pinging icon, see MultiplayerServerListWidget
            PacketByteBuf buf = PacketByteBufs.create();
            ClientPlayNetworking.send(new Identifier(Cobblores.MOD_NAMESPACE,
                    "request_config"), buf);
        }
    }

    public void saveTable() {
        if (config != null) {
            config.setBlocksTable(blocksConfig.table);
            if (client.getCurrentServerEntry() == null) {
                Cobblores.setConfig(config);
            } else {
                PacketByteBuf buf = PacketByteBufs.create();
                buf.writeString(config.toJson());
                ClientPlayNetworking.send(new Identifier(Cobblores.MOD_NAMESPACE,
                        "set_config"), buf);
            }
        }
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        this.blocksConfig.render(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 8, 16777215);
    }

    @Environment(EnvType.CLIENT)
    void onDone() {
        this.saveTable();
        this.client.setScreen(this.parent);
    }

    @Environment(EnvType.CLIENT)
    public ConfigScreen create(Screen parent) {
        this.parent = parent;
        return this;
    }
}