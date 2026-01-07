package one.fayaz.entity;

import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.animal.TemperatureVariants;
import net.minecraft.world.entity.variant.BiomeCheck;
import net.minecraft.world.entity.variant.ModelAndTexture;
import net.minecraft.world.entity.variant.SpawnPrioritySelectors;
import net.minecraft.world.level.biome.Biome;
import one.fayaz.ModRegistries;

public class IronGolemVariants {
    public static final ResourceKey<IronGolemVariant> TEMPERATE;
    public static final ResourceKey<IronGolemVariant> WARM;
    public static final ResourceKey<IronGolemVariant> COLD;
    public static final ResourceKey<IronGolemVariant> DEFAULT;

    private static ResourceKey<IronGolemVariant> createKey(final Identifier id) {
        return ResourceKey.create(ModRegistries.IRON_GOLEM_VARIANT, id);
    }

    public static void bootstrap(final BootstrapContext<IronGolemVariant> context) {
        register(context, TEMPERATE, IronGolemVariant.ModelType.NORMAL, "temperate_iron_golem",
                SpawnPrioritySelectors.fallback(0));
        register(context, WARM, IronGolemVariant.ModelType.WARM, "warm_iron_golem",
                BiomeTags.SPAWNS_WARM_VARIANT_FARM_ANIMALS);
        register(context, COLD, IronGolemVariant.ModelType.COLD, "cold_iron_golem",
                BiomeTags.SPAWNS_COLD_VARIANT_FARM_ANIMALS);
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

    static {
        TEMPERATE = createKey(TemperatureVariants.TEMPERATE);
        WARM = createKey(TemperatureVariants.WARM);
        COLD = createKey(TemperatureVariants.COLD);
        DEFAULT = TEMPERATE;
    }
}