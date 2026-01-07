package one.fayaz;

import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import one.fayaz.entity.IronGolemVariant;

public class ModRegistries {

    public static final ResourceKey<Registry<IronGolemVariant>> IRON_GOLEM_VARIANT =
            ResourceKey.createRegistryKey(Identifier.withDefaultNamespace("iron_golem_variant"));
}