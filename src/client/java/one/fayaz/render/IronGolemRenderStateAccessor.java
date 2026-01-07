package one.fayaz.render;

import net.minecraft.core.Holder;
import one.fayaz.entity.IronGolemVariant;

public interface IronGolemRenderStateAccessor {
    Holder<IronGolemVariant> moreVillages$getVariant();
    void moreVillages$setVariant(Holder<IronGolemVariant> variant);
}