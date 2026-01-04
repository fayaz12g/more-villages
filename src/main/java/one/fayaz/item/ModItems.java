package one.fayaz.item;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import one.fayaz.MoreVillages;
import one.fayaz.sound.ModSounds;

public class ModItems {

    // Declare items
    public static Item MUSIC_DISC_ZEN;

    // Helper method that handles ResourceKey creation and registration
    private static <T extends Item> T registerItem(String name, T item) {
        Identifier id = Identifier.fromNamespaceAndPath(MoreVillages.MOD_ID, name);
        ResourceKey<Item> key = ResourceKey.create(BuiltInRegistries.ITEM.key(), id);
        return Registry.register(BuiltInRegistries.ITEM, key, item);
    }

    // Initialize all items - called AFTER blocks are registered
    public static void initialize() {
        MoreVillages.LOGGER.info("Registering items for " + MoreVillages.MOD_ID);

        // ========== ZEN MUSIC DISC ==========
        Identifier musicDiscZenId = Identifier.fromNamespaceAndPath(
                MoreVillages.MOD_ID,
                "music_disc_zen"
        );
        ResourceKey<Item> musicDiscZenKey = ResourceKey.create(
                BuiltInRegistries.ITEM.key(),
                musicDiscZenId
        );

        Item.Properties musicDiscZenProperties = new Item.Properties()
                .jukeboxPlayable(ModSounds.ZEN_JUKEBOX_SONG)  // Link to the jukebox song
                .stacksTo(1)  // Music discs don't stack
                .rarity(net.minecraft.world.item.Rarity.RARE);  // Optional: make it rare quality

        musicDiscZenProperties.setId(musicDiscZenKey);

        MUSIC_DISC_ZEN = Registry.register(
                BuiltInRegistries.ITEM,
                musicDiscZenKey,
                new Item(musicDiscZenProperties.jukeboxPlayable(ModSounds.ZEN_JUKEBOX_SONG))
        );

        MoreVillages.LOGGER.info("Successfully registered music_disc_zen item");
    }
}