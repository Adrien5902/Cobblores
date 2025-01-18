package net.ddns.adrien5902.cobblores.client;

import java.util.Collections;
import java.util.List;

import com.google.common.collect.ImmutableList;

import net.ddns.adrien5902.cobblores.RandomTable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class BlocksConfig extends ElementListWidget<BlocksConfig.Entry> {
    protected ConfigScreen parent;
    public RandomTable table;
    public static final int ITEM_HEIGHT = 18;

    public BlocksConfig(ConfigScreen parent, RandomTable table) {
        super(MinecraftClient.getInstance(), parent.width, parent.height, 20, parent.height - 32, ITEM_HEIGHT + 2);
        this.table = table;
        this.update();
    }

    public void update() {
        this.clearEntries();
        for (var item : table.blocks.entrySet()) {
            this.addEntry(new BlockEntry(item.getKey(), item.getValue(), this));
        }
    }

    @Override
    public int getRowWidth() {
        return 256;
    }

    public static abstract class Entry extends ElementListWidget.Entry<BlocksConfig.Entry> {
        public final BlocksConfig parent;
        protected MinecraftClient client;

        public Entry(BlocksConfig parent) {
            this.parent = parent;
            this.client = MinecraftClient.getInstance();
        }
    }

    public static class BlockEntry extends Entry {
        protected Block block;
        protected int weight;
        public final TextFieldWidget blockNameWidget;
        public final TextFieldWidget weightWidget;
        public final ButtonWidget deleteButtonWidget;

        public BlockEntry(Block block, int weight, BlocksConfig parent) {
            super(parent);

            this.block = block;
            this.weight = weight;

            this.deleteButtonWidget = ButtonWidget.builder(Text.literal("X"), button -> {
                System.out.println("test");
                this.parent.table.blocks.remove(this.block);
                this.parent.update();
            }).dimensions(0, 0, ITEM_HEIGHT + 2, ITEM_HEIGHT + 2).build();

            this.blockNameWidget = new TextFieldWidget(this.client.textRenderer, 0, 0, 164, ITEM_HEIGHT,
                    Text.literal(Registries.BLOCK.getId(this.block).toString()));
            this.weightWidget = new TextFieldWidget(this.client.textRenderer, 0, 0, 42, ITEM_HEIGHT,
                    Text.literal(String.valueOf(weight)));

            this.update();

            this.blockNameWidget.setChangedListener(value -> {
                this.parent.table.blocks.remove(this.block);
                try {
                    Identifier id = Identifier.tryParse(value);
                    Block gottenBlock = Registries.BLOCK.get(id);

                    if (gottenBlock == null || gottenBlock instanceof AirBlock) {
                        throw new Exception();
                    }

                    this.blockNameWidget.setEditableColor(14737632);
                    this.block = gottenBlock;
                    this.updateParent();
                } catch (Exception e) {
                    // Delete previous block
                    this.block = null;
                    this.blockNameWidget.setEditableColor(16711680);
                }
            });

            this.weightWidget.setChangedListener(value -> {
                try {
                    int intWeight = Integer.parseInt(value);
                    this.weightWidget.setEditableColor(14737632);
                    this.weight = intWeight;
                } catch (Exception e) {
                    this.weight = 0;
                    this.weightWidget.setEditableColor(16711680);
                }
                this.updateParent();
            });
        }

        void update() {
            this.blockNameWidget.setText(Registries.BLOCK.getId(this.block).toString());
            this.weightWidget.setText(String.valueOf(this.weight));
        }

        void updateParent() {
            this.parent.table.blocks.put(this.block, this.weight);
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX,
                int mouseY, boolean hovered, float tickDelta) {

            context.drawBorder(x, y - 1, ITEM_HEIGHT + 2, ITEM_HEIGHT + 2, -6250336);
            if (block != null || block instanceof AirBlock) {
                context.drawItem(block.asItem().getDefaultStack(), x + 2, y + 1);
            } else {
                context.drawText(client.textRenderer, Text.literal("?"), x + 2 + 4, y + 2 + 4, 16711680, false);
            }

            this.blockNameWidget.setX(x + 20);
            this.blockNameWidget.setY(y);
            this.blockNameWidget.render(context, mouseX, mouseY, tickDelta);

            this.weightWidget.setX(x + 164 + 20);
            this.weightWidget.setY(y);
            this.weightWidget.render(context, mouseX, mouseY, tickDelta);

            this.deleteButtonWidget.setX(x + 164 + 44 + 20);
            this.deleteButtonWidget.setY(y - 1);
            this.deleteButtonWidget.render(context, mouseX, mouseY, tickDelta);
        }

        @Override
        public List<? extends Element> children() {
            return ImmutableList.of(this.blockNameWidget, this.weightWidget, this.deleteButtonWidget);
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return ImmutableList.of(this.blockNameWidget, this.weightWidget, this.deleteButtonWidget);
        }
    }

    private static class ErrorEntry extends Entry {
        Exception e;

        public ErrorEntry(BlocksConfig parent, Exception e) {
            super(parent);
            this.e = e;
        }

        @Override
        public List<? extends Element> children() {
            return ImmutableList.of();
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX,
                int mouseY, boolean hovered, float tickDelta) {
            context.drawText(client.textRenderer, Text.literal(e.getMessage()), x, y, 16711680, true);
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return Collections.emptyList();
        }
    }

    public void addError(Exception e) {
        this.addEntry(new ErrorEntry(this, e));
        e.getMessage();
    }
}