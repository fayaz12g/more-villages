package one.fayaz.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityDataRegistry;
import net.minecraft.core.Holder;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.resources.Identifier;
import one.fayaz.MoreVillages;
import one.fayaz.entity.IronGolemVariant;

public class ModEntityDataSerializers {

    public static final EntityDataSerializer<Holder<IronGolemVariant>> IRON_GOLEM_VARIANT =
            EntityDataSerializer.forValueType(IronGolemVariant.STREAM_CODEC);

    public static void initialize() {
        // Use Fabric's registry instead of vanilla's to prevent desynchronization
        // First parameter is the identifier, second is the serializer
        FabricEntityDataRegistry.register(
                Identifier.fromNamespaceAndPath(MoreVillages.MOD_ID, "iron_golem_variant"),
                IRON_GOLEM_VARIANT
        );
    }
}