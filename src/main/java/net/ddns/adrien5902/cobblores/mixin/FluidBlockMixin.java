package net.ddns.adrien5902.cobblores.mixin;

import net.ddns.adrien5902.cobblores.Cobblores;
import net.minecraft.block.Block;
import net.minecraft.block.FluidBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FluidBlock.class)
public class FluidBlockMixin {

    @Redirect(method = "receiveNeighborFluids", at = @At(value = "FIELD", target = "Lnet/minecraft/block/Blocks;COBBLESTONE:Lnet/minecraft/block/Block;"))
    private Block cobblores$replaceCobblestoneWithRandomBlock() {
        return Cobblores.getRandomBlock();
    }
}
