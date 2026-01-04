package one.fayaz.block;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import one.fayaz.MoreVillages;

public class ModBlocks {

    // Declare blocks
    public static Block EXAMPLE_BLOCK;

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
        // Create the ResourceKey first
        Identifier exampleBlockId = Identifier.fromNamespaceAndPath(
                MoreVillages.MOD_ID,
                "example_block"
        );
        ResourceKey<Block> exampleBlockKey = ResourceKey.create(
                BuiltInRegistries.BLOCK.key(),
                exampleBlockId
        );

        // Create properties and set the ID
        BlockBehaviour.Properties properties = BlockBehaviour.Properties.ofFullCopy(Blocks.DIRT);

        // Set the resource key on the properties before creating the block
        // This is required in MC 1.21+
        properties.setId(exampleBlockKey);

        // Now create the block with the ID already set
        EXAMPLE_BLOCK = Registry.register(
                BuiltInRegistries.BLOCK,
                exampleBlockKey,
                new ExampleBlock(properties)
        );

        MoreVillages.LOGGER.info("Successfully registered example_block block");
    }
}