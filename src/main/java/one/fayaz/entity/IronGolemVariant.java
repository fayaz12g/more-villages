package one.fayaz.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.variant.ModelAndTexture;
import net.minecraft.world.entity.variant.PriorityProvider;
import net.minecraft.world.entity.variant.SpawnCondition;
import net.minecraft.world.entity.variant.SpawnContext;
import net.minecraft.world.entity.variant.SpawnPrioritySelectors;
import one.fayaz.ModRegistries;

public record IronGolemVariant(
        ModelAndTexture<ModelType> modelAndTexture,
        SpawnPrioritySelectors spawnConditions
) implements PriorityProvider<SpawnContext, SpawnCondition> {

    public static final Codec<IronGolemVariant> DIRECT_CODEC = RecordCodecBuilder.create((i) ->
            i.group(
                    ModelAndTexture.codec(IronGolemVariant.ModelType.CODEC, IronGolemVariant.ModelType.NORMAL)
                            .forGetter(IronGolemVariant::modelAndTexture),
                    SpawnPrioritySelectors.CODEC.fieldOf("spawn_conditions")
                            .forGetter(IronGolemVariant::spawnConditions)
            ).apply(i, IronGolemVariant::new)
    );

    public static final Codec<IronGolemVariant> NETWORK_CODEC = RecordCodecBuilder.create((i) ->
            i.group(
                    ModelAndTexture.codec(IronGolemVariant.ModelType.CODEC, IronGolemVariant.ModelType.NORMAL)
                            .forGetter(IronGolemVariant::modelAndTexture)
            ).apply(i, IronGolemVariant::new)
    );

    public static final Codec<Holder<IronGolemVariant>> CODEC;
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<IronGolemVariant>> STREAM_CODEC;

    private IronGolemVariant(final ModelAndTexture<ModelType> assetInfo) {
        this(assetInfo, SpawnPrioritySelectors.EMPTY);
    }

    public List<PriorityProvider.Selector<SpawnContext, SpawnCondition>> selectors() {
        return this.spawnConditions.selectors();
    }

    static {
        CODEC = RegistryFixedCodec.create(ModRegistries.IRON_GOLEM_VARIANT);
        STREAM_CODEC = ByteBufCodecs.holderRegistry(ModRegistries.IRON_GOLEM_VARIANT);
    }

    public enum ModelType implements StringRepresentable {
        NORMAL("normal"),
        COLD("cold"),
        WARM("warm");

        public static final Codec<ModelType> CODEC = StringRepresentable.fromEnum(ModelType::values);
        private final String name;

        private ModelType(final String name) {
            this.name = name;
        }

        public String getSerializedName() {
            return this.name;
        }
    }
}