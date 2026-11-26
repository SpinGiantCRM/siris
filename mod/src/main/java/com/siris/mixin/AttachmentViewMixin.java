package com.siris.mixin;

import com.mojang.renderpearl.backend.vulkan.VulkanDevice;
import com.mojang.renderpearl.backend.vulkan.VulkanGpuTextureView;
import com.siris.AttachmentView;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = VulkanGpuTextureView.class, remap = false)
abstract class AttachmentViewMixin implements AttachmentView {
    @Shadow @Final private VulkanDevice device;
    @Unique private VulkanGpuTextureView siris$attachment;

    @Override
    public long siris$attachmentHandle() {
        var view = (VulkanGpuTextureView) (Object) this;
        if (view.mipLevels() == 1) {
            return view.vkImageView();
        }
        if (siris$attachment == null) {
            siris$attachment = (VulkanGpuTextureView) device.createTextureView(view.texture(), view.baseMipLevel(), 1);
        }
        return siris$attachment.vkImageView();
    }

    @Inject(method = "close", at = @At("HEAD"))
    private void siris$releaseAttachment(CallbackInfo ci) {
        if (siris$attachment != null) {
            siris$attachment.close();
            siris$attachment = null;
        }
    }
}
