package one.fayaz.mixin.client;

import net.minecraft.client.renderer.entity.IronGolemRenderer;
import net.minecraft.client.renderer.entity.state.IronGolemRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.animal.golem.IronGolem;
import one.fayaz.render.IronGolemRenderStateAccessor;
import one.fayaz.entity.IronGolemVariantAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(IronGolemRenderer.class)
public class IronGolemRendererMixin {

    // 1. Copy data from Entity to State
    @Inject(method = "extractRenderState", at = @At("TAIL"))
    private void extractVariantState(IronGolem entity, IronGolemRenderState state, float partialTick, CallbackInfo ci) {
        // Get variant from the entity (Server/World side data)
        var entityAccessor = (IronGolemVariantAccessor) entity;
        var variant = entityAccessor.moreVillages$getVariant();

        // Save it into the render state (Client/Render side data)
        ((IronGolemRenderStateAccessor) state).moreVillages$setVariant(variant);
    }

    // 2. Use data from State to choose texture
    @Inject(method = "getTextureLocation", at = @At("HEAD"), cancellable = true)
    private void getVariantTexture(IronGolemRenderState state, CallbackInfoReturnable<Identifier> cir) {
        // Read variant from the render state
        var stateAccessor = (IronGolemRenderStateAccessor) state;
        var variant = stateAccessor.moreVillages$getVariant();

        if (variant != null && variant.isBound()) {
            Identifier assetId = variant.value().modelAndTexture().asset().id();
            // Convert "minecraft:entity/iron_golem/cold" -> "minecraft:textures/entity/iron_golem/cold.png"
            Identifier fullTexturePath = assetId.withPath(path -> "textures/" + path + ".png");
            cir.setReturnValue(fullTexturePath);
        }
    }
}