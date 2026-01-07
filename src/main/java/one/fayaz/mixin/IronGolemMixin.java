package one.fayaz.mixin;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry; // Import this
import net.minecraft.core.RegistryAccess; // Import this
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.golem.IronGolem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import one.fayaz.entity.ModEntityDataSerializers;
import one.fayaz.entity.IronGolemVariant;
import one.fayaz.entity.IronGolemVariantAccessor;
import one.fayaz.entity.IronGolemVariants;
import one.fayaz.ModRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.world.entity.variant.VariantUtils;

@Mixin(IronGolem.class)
public abstract class IronGolemMixin implements IronGolemVariantAccessor {

    @Unique
    private static final EntityDataAccessor<Holder<IronGolemVariant>> DATA_VARIANT_ID =
            SynchedEntityData.defineId(IronGolem.class, ModEntityDataSerializers.IRON_GOLEM_VARIANT);

    @Unique
    private Holder<IronGolemVariant> moreVillages$cachedVariant;

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void defineVariantData(SynchedEntityData.Builder entityData, CallbackInfo ci) {
        IronGolem self = (IronGolem) (Object) this;

        // FIX: Retrieve the registry and the default variant immediately.
        // We cannot use 'moreVillages$cachedVariant' here because it is currently null.
        RegistryAccess registryAccess = self.registryAccess();
        Registry<IronGolemVariant> registry = registryAccess.lookupOrThrow(ModRegistries.IRON_GOLEM_VARIANT);

        // Get the default "Temperate" variant to use as the initial value
        Holder<IronGolemVariant> defaultVariant = registry.getOrThrow(IronGolemVariants.PLAINS);

        // Initialize our cache now so it's ready
        this.moreVillages$cachedVariant = defaultVariant;

        // Define the data parameter with the NON-NULL default value
        entityData.define(DATA_VARIANT_ID, defaultVariant);
    }

    @Inject(method = "<init>(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/Level;)V", at = @At("RETURN"))
    private void initVariant(EntityType<? extends IronGolem> entityType, Level level, CallbackInfo ci) {
        // You can keep this or remove it.
        // Since we set the default in defineSynchedData now, this is technically redundant
        // unless you want to do specific logic here.
        // However, keeping it doesn't hurt as long as you handle exceptions.

        IronGolem self = (IronGolem) (Object) this;
        if (!level.isClientSide()) {
            // Logic to ensure variant is set if needed
        }
    }

    // ... (rest of the file: saveVariantData, readVariantData, overrides) ...
    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void saveVariantData(ValueOutput output, CallbackInfo ci) {
        Holder<IronGolemVariant> variant = moreVillages$getVariant();
        if (variant != null) {
            VariantUtils.writeVariant(output, variant);
        }
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void readVariantData(ValueInput input, CallbackInfo ci) {
        VariantUtils.readVariant(input, ModRegistries.IRON_GOLEM_VARIANT)
                .ifPresent(variant -> {
                    moreVillages$cachedVariant = variant;
                    this.moreVillages$setVariant(variant);
                });
    }

    @Unique
    @Override
    public void moreVillages$setVariant(Holder<IronGolemVariant> variant) {
        IronGolem self = (IronGolem) (Object) this;
        moreVillages$cachedVariant = variant;
        self.getEntityData().set(DATA_VARIANT_ID, variant);
    }

    @Unique
    @Override
    public Holder<IronGolemVariant> moreVillages$getVariant() {
        IronGolem self = (IronGolem) (Object) this;
        Holder<IronGolemVariant> variant = self.getEntityData().get(DATA_VARIANT_ID);
        // Fallback to cache if data is somehow null, though defineSynchedData fix makes this less likely
        if (variant == null) {
            variant = moreVillages$cachedVariant;
        }
        return variant;
    }
}