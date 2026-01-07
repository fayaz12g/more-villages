package one.fayaz.mixin.client;

import net.minecraft.client.renderer.entity.state.IronGolemRenderState;
import net.minecraft.core.Holder;
import one.fayaz.render.IronGolemRenderStateAccessor;
import one.fayaz.entity.IronGolemVariant;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(IronGolemRenderState.class)
public class IronGolemRenderStateMixin implements IronGolemRenderStateAccessor {

	@Unique
	private Holder<IronGolemVariant> moreVillages$variant;

	@Override
	public Holder<IronGolemVariant> moreVillages$getVariant() {
		return moreVillages$variant;
	}

	@Override
	public void moreVillages$setVariant(Holder<IronGolemVariant> variant) {
		this.moreVillages$variant = variant;
	}
}