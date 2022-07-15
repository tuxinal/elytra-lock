package xyz.tuxinal.elytralock;

import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.systems.RenderSystem;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Items;

public class ElytraLock implements ClientModInitializer {
    protected static KeyMapping lockKey;
    public static boolean locked = false;
    public static ConfigHolder<ModConfig> configHolder;

    @Override
    public void onInitializeClient() {
        // ========== Init ==========
        final Minecraft client = Minecraft.getInstance();
        AutoConfig.register(ModConfig.class, Toml4jConfigSerializer::new);
        configHolder = AutoConfig.getConfigHolder(ModConfig.class);
        lockKey = KeyBindingHelper
                .registerKeyBinding(new KeyMapping("elytralock.lockkey", GLFW.GLFW_KEY_G, "key.categories.misc"));

        // ========== Events ==========
        ClientTickEvents.END_CLIENT_TICK.register(_client -> {
            while (lockKey.consumeClick()) {
                locked = !locked;
                client.player.displayClientMessage(Component.translatable("elytralock.locktoggled_" + locked), true);
            }
        });
        HudRenderCallback.EVENT.register(((matrixStack, tickDelta) -> {
            var config = getConfig();
            if (!(config.enabled && config.showIndicator) || client.options.hideGui) {
                return;
            }
            if (client.player.getItemBySlot(EquipmentSlot.CHEST).getItem() != Items.ELYTRA) {
                return;
            }
            if ((!locked && config.whenToShow == WhenToShow.Locked)
                    || (locked && config.whenToShow == WhenToShow.Unlocked)) {
                return;
            }
            ResourceLocation texture = new ResourceLocation("elytra-lock",
                    locked ? "textures/gui/locked.png" : "textures/gui/unlocked.png");
            // `>> 1` = division by 2 but more efficient (and slightly inaccurate, though we
            // don't need that much precision)
            var x = (client.getWindow().getGuiScaledWidth() >> 1) - 4;
            var y = client.getWindow().getGuiScaledHeight() - 44;
            RenderSystem.setShaderTexture(0, texture);
            GuiComponent.blit(matrixStack, x, y, 0, 0, 8, 8, 8, 8);
        }));
    }

    public static ModConfig getConfig() {
        return configHolder.getConfig();
    }
}
