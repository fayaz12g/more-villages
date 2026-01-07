package one.fayaz;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import one.fayaz.block.ModBlocks;
import one.fayaz.entity.IronGolemSpawnHandler;
import one.fayaz.entity.IronGolemVariant;
import one.fayaz.entity.ModEntityDataSerializers;
import one.fayaz.item.ModItems;
import one.fayaz.sound.ModSounds;
import one.fayaz.villager.ModVillagers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MoreVillages implements ModInitializer {
	public static final String MOD_ID = "more-villages";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Loading MoreVillages...!");

		ModEntityDataSerializers.initialize();

		DynamicRegistries.registerSynced(
				ModRegistries.IRON_GOLEM_VARIANT,
				IronGolemVariant.DIRECT_CODEC,
				IronGolemVariant.NETWORK_CODEC // Use the network codec to save bandwidth
		);

		// Register spawn handler for setting variants
		IronGolemSpawnHandler.register();

		ModBlocks.initialize();
		ModItems.initialize();
		ModSounds.initialize();
		ModVillagers.initialize();
	}
}