package one.fayaz.villager;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.npc.villager.VillagerType;
import one.fayaz.MoreVillages;


public class ModVillagerType {

    public static final SoundEvent CHERRY_VILLAGER = registerVillagerType("cherry_villager");
    public static final ResourceKey<VillagerType> CHERRY_KEY =
            ResourceKey.create(BuiltInRegistries.VILLAGER_TYPE.key(), Identifier.fromNamespaceAndPath(MoreVillages.MOD_ID, "music_disc_zen"));


    private static SoundEvent registerVillagerType(String name) {
        Identifier id = Identifier.fromNamespaceAndPath(MoreVillages.MOD_ID, name);
        ResourceKey<SoundEvent> key = ResourceKey.create(BuiltInRegistries.SOUND_EVENT.key(), id);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, key, SoundEvent.createVariableRangeEvent(id));
    }

    public static void registerSounds() {
        MoreVillages.LOGGER.info("Registering Mod Villager Type for " + MoreVillages.MOD_ID);
    }
}