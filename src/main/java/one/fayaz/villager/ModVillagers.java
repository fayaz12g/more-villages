package one.fayaz.villager;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.npc.villager.VillagerType;
import one.fayaz.MoreVillages;


public class ModVillagers {

    public static final VillagerType BADLANDS = register("badlands");
    public static final VillagerType CHERRY = register("cherry");


    private static VillagerType register(String name) {
        Identifier id = Identifier.fromNamespaceAndPath(MoreVillages.MOD_ID, name);
        ResourceKey<VillagerType> key = ResourceKey.create(BuiltInRegistries.VILLAGER_TYPE.key(), id);
        return Registry.register(BuiltInRegistries.VILLAGER_TYPE, key, new VillagerType());
    }

    public static void initialize() {
        MoreVillages.LOGGER.info("Registering villager types for " + MoreVillages.MOD_ID);
    }

}