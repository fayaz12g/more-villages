package one.fayaz.block;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import one.fayaz.MoreVillages;

import java.util.function.Function;

import static net.minecraft.world.level.block.Blocks.PEARLESCENT_FROGLIGHT;

public class ModBlocks {

    // Declare blocks
    public static Block SAKURA_FROGLIGHT;

    // Helper method that handles ResourceKey creation and registration
    private static <T extends Block> T registerBlock(String name, T block) {
        Identifier id = Identifier.fromNamespaceAndPath(MoreVillages.MOD_ID, name);
        ResourceKey<Block> key = ResourceKey.create(BuiltInRegistries.BLOCK.key(), id);
        return Registry.register(BuiltInRegistries.BLOCK, key, block);
    }

    // Initialize all blocks
    public static void initialize() {
        MoreVillages.LOGGER.info("Registering blocks for " + MoreVillages.MOD_ID);
        registerBlocks();
    }

    public static void registerBlocks() {
//        // Create the ResourceKey first
        Identifier sakuraFroglightId = Identifier.fromNamespaceAndPath(
                MoreVillages.MOD_ID,
                "sakura_froglight"
        );
        ResourceKey<Block> sakuraFroglightKey = ResourceKey.create(
                BuiltInRegistries.BLOCK.key(),
                sakuraFroglightId
        );

        // Create properties and set the ID
        BlockBehaviour.Properties properties = BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_PINK).strength(0.3F).lightLevel((statex) -> 15).sound(SoundType.FROGLIGHT);

        // Set the resource key on the properties before creating the block
        // This is required in MC 1.21+
        properties.setId(sakuraFroglightKey);

        // Now create the block with the ID already set
        SAKURA_FROGLIGHT = Registry.register(
                BuiltInRegistries.BLOCK,
                sakuraFroglightKey,
                new RotatedPillarBlock(properties)
        );

        MoreVillages.LOGGER.info("Successfully registered sakura_froglight block");
    }

}