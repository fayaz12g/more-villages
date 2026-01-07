package one.fayaz.entity;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.golem.IronGolem;
import net.minecraft.world.entity.variant.SpawnContext;
import net.minecraft.world.entity.variant.VariantUtils;
import one.fayaz.MoreVillages;
import one.fayaz.entity.IronGolemVariant;
import one.fayaz.entity.IronGolemVariantAccessor;
import one.fayaz.entity.IronGolemVariants;
import one.fayaz.ModRegistries;

/**
 * Handles setting variants for Iron Golems when they spawn.
 * Uses Fabric's entity events to set variants at spawn time.
 */
public class IronGolemSpawnHandler {

    public static void register() {
        // Register event that fires when entities are loaded into the world
        ServerEntityEvents.ENTITY_LOAD.register(IronGolemSpawnHandler::onEntityLoad);
    }

    private static void onEntityLoad(Entity entity, ServerLevel level) {
        // Only process Iron Golems
        if (!(entity instanceof IronGolem golem)) {
            return;
        }

        // Cast to accessor to access variant methods
        if (!(entity instanceof IronGolemVariantAccessor accessor)) {
            return;
        }

        // Skip player-created golems - they keep default variant
        if (golem.isPlayerCreated()) {
            return;
        }

        try {
            // Get current variant
            Holder<IronGolemVariant> currentVariant = accessor.moreVillages$getVariant();

            // Check if this is still the default variant from initialization
            // We want to set a biome-specific variant for new spawns
            if (currentVariant != null && currentVariant.is(IronGolemVariants.PLAINS)) {
                // This might be a new spawn with default variant
                // Try to select a better variant based on location
                var selectedVariant = VariantUtils.selectVariantToSpawn(
                        SpawnContext.create(level, golem.blockPosition()),
                        ModRegistries.IRON_GOLEM_VARIANT
                );

                if (selectedVariant.isPresent() && !selectedVariant.get().is(IronGolemVariants.PLAINS)) {
                    // Found a non-default variant for this biome, use it
                    accessor.moreVillages$setVariant(selectedVariant.get());
                    MoreVillages.LOGGER.debug("Set Iron Golem variant to {} at {}",
                            selectedVariant.get().key(), golem.blockPosition());
                }
            }
        } catch (Exception e) {
            MoreVillages.LOGGER.error("Error setting Iron Golem variant", e);
        }
    }
}