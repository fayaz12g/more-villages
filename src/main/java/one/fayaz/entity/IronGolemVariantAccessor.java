package one.fayaz.entity;

import net.minecraft.core.Holder;

/**
 * Accessor interface for Iron Golem variants.
 * Cast IronGolem instances to this interface to access variant methods.
 */
public interface IronGolemVariantAccessor {

    /**
     * Gets the current variant of this Iron Golem.
     * @return The holder for the golem's variant
     */
    Holder<IronGolemVariant> moreVillages$getVariant();

    /**
     * Sets the variant of this Iron Golem.
     * @param variant The holder for the new variant
     */
    void moreVillages$setVariant(Holder<IronGolemVariant> variant);
}