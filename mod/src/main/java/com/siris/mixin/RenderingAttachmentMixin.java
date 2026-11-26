package com.siris.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.renderpearl.backend.vulkan.VulkanCommandEncoder;
import com.mojang.renderpearl.backend.vulkan.VulkanGpuTextureView;
import com.siris.AttachmentView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = VulkanCommandEncoder.class, remap = false)
abstract class RenderingAttachmentMixin {
    // The same texture keeps its full view when shaders sample the mip chain.
    // WrapOperation, not Redirect: Lunar brings its own mixins into VulkanCommandEncoder.
    @WrapOperation(method = "createRenderPass", at = @At(value = "INVOKE",
        target = "Lcom/mojang/renderpearl/backend/vulkan/VulkanGpuTextureView;vkImageView()J"), require = 2)
    private long siris$renderingView(VulkanGpuTextureView view, Operation<Long> original) {
        return view.mipLevels() == 1 ? original.call(view) : ((AttachmentView) view).siris$attachmentHandle();
    }
}
