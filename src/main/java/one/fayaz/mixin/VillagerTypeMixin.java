package one.fayaz.mixin;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.npc.villager.VillagerType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import one.fayaz.MoreVillages;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(VillagerType.class)
public class VillagerTypeMixin {

	@Shadow
	@Final
	public static Map<ResourceKey<Biome>, ResourceKey<VillagerType>> BY_BIOME;

	@Inject(method = "<clinit>", at = @At("TAIL"))
	private static void addCustomVillagerTypes(CallbackInfo ci) {
		// Create resource keys for your custom villager types
		ResourceKey<VillagerType> badlandsType = ResourceKey.create(
				BuiltInRegistries.VILLAGER_TYPE.key(),
				Identifier.fromNamespaceAndPath(MoreVillages.MOD_ID, "badlands")
		);

		ResourceKey<VillagerType> cherryType = ResourceKey.create(
				BuiltInRegistries.VILLAGER_TYPE.key(),
				Identifier.fromNamespaceAndPath(MoreVillages.MOD_ID, "cherry")
		);

		// Override badlands biomes to use your custom badlands villager
		BY_BIOME.put(Biomes.BADLANDS, badlandsType);
		BY_BIOME.put(Biomes.ERODED_BADLANDS, badlandsType);
		BY_BIOME.put(Biomes.WOODED_BADLANDS, badlandsType);

		// Map cherry grove to your custom cherry villager
		BY_BIOME.put(Biomes.CHERRY_GROVE, cherryType);
	}
}