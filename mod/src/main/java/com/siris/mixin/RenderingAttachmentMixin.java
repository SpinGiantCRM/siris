package com.siris.mixin;

import com.mojang.renderpearl.backend.vulkan.VulkanCommandEncoder;
import com.mojang.renderpearl.backend.vulkan.VulkanGpuTextureView;
import com.siris.AttachmentView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = VulkanCommandEncoder.class, remap = false)
abstract class RenderingAttachmentMixin {
    // The same texture keeps its full view when shaders sample the mip chain.
    @Redirect(method = "createRenderPass", at = @At(value = "INVOKE",
        target = "Lcom/mojang/renderpearl/backend/vulkan/VulkanGpuTextureView;vkImageView()J"), require = 2)
    private long siris$renderingView(VulkanGpuTextureView view) {
        return ((AttachmentView) view).siris$attachmentHandle();
    }
}
