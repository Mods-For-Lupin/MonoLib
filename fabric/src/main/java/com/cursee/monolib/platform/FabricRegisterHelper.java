package com.cursee.monolib.platform;

import com.cursee.monolib.core.util.TriFunction;
import com.cursee.monolib.platform.services.IRegisterHelper;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.BiFunction;
import java.util.function.Function;

public class FabricRegisterHelper implements IRegisterHelper {

    @Override
    public <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(BiFunction<BlockPos, BlockState, T> blockEntityConstructor, Block... validBlocks) {
        return FabricBlockEntityTypeBuilder.create(blockEntityConstructor::apply, validBlocks).build();
    }

    @Override
    public <BE extends BlockEntity, T extends BlockEntityType<BE>, R extends BlockEntityRenderer<BE>> void registerBlockEntityRenderer(T blockEntityType, Function<BlockEntityRendererProvider.Context, R> blockEntityRendererConstructor) {
        BlockEntityRenderers.register(blockEntityType, blockEntityRendererConstructor::apply);
    }

    @Override
    public <T extends Entity> EntityType<T> createEntityType(String key, BiFunction<EntityType<T>, Level, T> entityConstructor, MobCategory mobCategory) {
        return EntityType.Builder.of(entityConstructor::apply, mobCategory).build(key);
    }

    @Override
    public <E extends Entity, T extends EntityType<E>, R extends EntityRenderer<E>> void registerEntityRenderer(T entityType, Function<EntityRendererProvider.Context, R> entityRendererConstructor) {
        EntityRendererRegistry.register(entityType, entityRendererConstructor::apply);
    }

    @Override
    public <T extends AbstractContainerMenu> MenuType<T> createMenuType(BiFunction<Integer, Inventory, T> menuConstructor, FeatureFlagSet flagSet) {
        return new MenuType<>(menuConstructor::apply, flagSet);
    }

    @Override
    public <M extends AbstractContainerMenu, S extends AbstractContainerScreen<M>> void registerScreen(MenuType<M> menuType, TriFunction<M, Inventory, Component, S> screenConstructor) {
        MenuScreens.register(menuType, screenConstructor::apply);
    }
}
