package one.fayaz.villager;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.entity.npc.villager.VillagerType;
import one.fayaz.MoreVillages;


public class ModVillagerProfession {

//    public static final SoundEvent CHERRY_VILLAGER = registerVillagerType("cherry_villager");
    public static final ResourceKey<VillagerProfession> CHERRY_KEY =
            ResourceKey.create(BuiltInRegistries.VILLAGER_PROFESSION.key(), Identifier.fromNamespaceAndPath(MoreVillages.MOD_ID, "music_disc_zen"));


//    private static VillagerProfession registerVillagerType(String name) {
//        Identifier id = Identifier.fromNamespaceAndPath(MoreVillages.MOD_ID, name);
//        ResourceKey<VillagerProfession> key = ResourceKey.create(BuiltInRegistries.VILLAGER_PROFESSION.key(), id);
//        return Registry.register(BuiltInRegistries.VILLAGER_PROFESSION, key, VillagerProfession.NITWIT);
//    }

    public static void registerSounds() {
        MoreVillages.LOGGER.info("Registering Mod Villager Profession for " + MoreVillages.MOD_ID);
    }
}