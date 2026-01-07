package one.fayaz.entity;

import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.animal.TemperatureVariants;
import net.minecraft.world.entity.npc.villager.VillagerType;
import net.minecraft.world.entity.variant.BiomeCheck;
import net.minecraft.world.entity.variant.ModelAndTexture;
import net.minecraft.world.entity.variant.SpawnPrioritySelectors;
import net.minecraft.world.level.biome.Biome;
import one.fayaz.ModRegistries;
import one.fayaz.ModTags;

public class IronGolemVariants {

    public static final ResourceKey<IronGolemVariant> DESERT = createKey("desert");
    public static final ResourceKey<IronGolemVariant> JUNGLE = createKey("jungle");
    public static final ResourceKey<IronGolemVariant> PLAINS = createKey("plains");
    public static final ResourceKey<IronGolemVariant> SAVANNA = createKey("savanna");
    public static final ResourceKey<IronGolemVariant> SNOW = createKey("snow");
    public static final ResourceKey<IronGolemVariant> SWAMP = createKey("swamp");
    public static final ResourceKey<IronGolemVariant> TAIGA = createKey("taiga");
    public static final ResourceKey<IronGolemVariant> CHERRY = createKey("cherry");
    public static final ResourceKey<IronGolemVariant> BADLANDS = createKey("badlands");
    public static final ResourceKey<IronGolemVariant> BEACH = createKey("beach");

    private static ResourceKey<IronGolemVariant> createKey(final String name) {
        return ResourceKey.create(ModRegistries.IRON_GOLEM_VARIANT, Identifier.withDefaultNamespace(name));
    }

    private static ResourceKey<IronGolemVariant> createKey(final Identifier id) {
        return ResourceKey.create(ModRegistries.IRON_GOLEM_VARIANT, id);
    }

    public static void bootstrap(final BootstrapContext<IronGolemVariant> context) {

        register(context, DESERT, IronGolemVariant.ModelType.DESERT, "desert",
                BiomeTags.HAS_VILLAGE_DESERT);

        register(context, PLAINS, IronGolemVariant.ModelType.PLAINS, "plains",
                SpawnPrioritySelectors.fallback(0));

        register(context, SAVANNA, IronGolemVariant.ModelType.SAVANNA, "savanna",
                BiomeTags.HAS_VILLAGE_SAVANNA);

        register(context, SNOW, IronGolemVariant.ModelType.SNOW, "snow",
                BiomeTags.HAS_VILLAGE_SNOWY);

        register(context, TAIGA, IronGolemVariant.ModelType.TAIGA, "taiga",
                BiomeTags.HAS_VILLAGE_TAIGA);

        register(context, CHERRY, IronGolemVariant.ModelType.CHERRY, "cherry",
                ModTags.HAS_VILLAGE_CHERRY);

        register(context, BADLANDS, IronGolemVariant.ModelType.BADLANDS, "badlands",
                ModTags.HAS_VILLAGE_BADLANDS);

        register(context, JUNGLE, IronGolemVariant.ModelType.JUNGLE, "jungle",
                ModTags.HAS_VILLAGE_JUNGLE);

        register(context, BEACH, IronGolemVariant.ModelType.BEACH, "beach",
                ModTags.HAS_VILLAGE_BEACH);

        register(context, SWAMP, IronGolemVariant.ModelType.SWAMP, "swamp",
                ModTags.HAS_VILLAGE_SWAMP);
    }

    private static void register(
            final BootstrapContext<IronGolemVariant> context,
            final ResourceKey<IronGolemVariant> name,
            final IronGolemVariant.ModelType modelType,
            final String textureName,
            final TagKey<Biome> spawnBiome
    ) {
        HolderSet<Biome> biomes = context.lookup(Registries.BIOME).getOrThrow(spawnBiome);
        register(context, name, modelType, textureName,
                SpawnPrioritySelectors.single(new BiomeCheck(biomes), 1));
    }

    private static void register(
            final BootstrapContext<IronGolemVariant> context,
            final ResourceKey<IronGolemVariant> name,
            final IronGolemVariant.ModelType modelType,
            final String textureName,
            final SpawnPrioritySelectors selectors
    ) {
        Identifier textureId = Identifier.withDefaultNamespace("entity/iron_golem/" + textureName);
        context.register(name, new IronGolemVariant(new ModelAndTexture<>(modelType, textureId), selectors));
    }

}