package one.fayaz.sound;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.JukeboxSong;
import one.fayaz.MoreVillages;


public class ModSounds {

    public static final SoundEvent MUSIC_DISC_ZEN = registerSoundEvent("music_disc.zen");

    // ResourceKey for the JukeboxSong (defined in JSON, not code)
    public static final ResourceKey<JukeboxSong> ZEN_JUKEBOX_SONG =
            ResourceKey.create(Registries.JUKEBOX_SONG, Identifier.fromNamespaceAndPath(MoreVillages.MOD_ID, "zen"));


    private static SoundEvent registerSoundEvent(String name) {
        Identifier id = Identifier.fromNamespaceAndPath(MoreVillages.MOD_ID, name);
        ResourceKey<SoundEvent> key = ResourceKey.create(BuiltInRegistries.SOUND_EVENT.key(), id);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, key, SoundEvent.createVariableRangeEvent(id));
    }

    public static void initialize() {
        MoreVillages.LOGGER.info("Registering sounds for " + MoreVillages.MOD_ID);
    }
}