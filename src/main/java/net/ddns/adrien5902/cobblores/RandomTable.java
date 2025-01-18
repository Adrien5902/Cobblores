package net.ddns.adrien5902.cobblores;

import java.util.HashMap;

import net.ddns.adrien5902.cobblores.ConfigManager.Config;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;

public class RandomTable {
    public HashMap<Block, Integer> blocks;

    public static RandomTable readFromConfig(Config config) {

        RandomTable table = new RandomTable();
        table.blocks = new HashMap<Block, Integer>();

        for (var entry : config.blocks.entrySet()) {
            table.blocks.put(Registries.BLOCK.get(Identifier.tryParse(entry.getKey())), entry.getValue());
        }

        return table;
    }

    public int getTotalWeight() {
        int totalWeight = 0;
        for (Integer value : blocks.values()) {
            totalWeight += value;
        }
        return totalWeight;
    }

    public Block getRandomBlock() {
        int totalWeight = getTotalWeight();
        int currentWeight = Random.create().nextBetween(1, totalWeight);

        for (var entry : blocks.entrySet()) {
            Block block = entry.getKey();
            Integer weight = entry.getValue();

            if (currentWeight <= weight) {
                return block;
            } else {
                currentWeight -= weight;
            }
        }

        return Blocks.COBBLESTONE;
    }
}
