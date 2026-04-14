package com.cursee.monolib.platform.services;

import com.cursee.monolib.core.util.TriFunction;
import java.util.function.BiFunction;
import java.util.function.Function;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
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

@Deprecated
public interface IRegisterHelper {

  <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(BiFunction<BlockPos, BlockState, T> blockEntityConstructor, Block... validBlocks);

  <BE extends BlockEntity, T extends BlockEntityType<BE>, R extends BlockEntityRenderer<BE>> void registerBlockEntityRenderer(T blockEntityType,
      Function<BlockEntityRendererProvider.Context, R> blockEntityRendererConstructor);

  <T extends Entity> EntityType<T> createEntityType(String key, BiFunction<EntityType<T>, Level, T> entityConstructor, MobCategory mobCategory);

  <E extends Entity, T extends EntityType<E>, R extends EntityRenderer<E>> void registerEntityRenderer(T entityType, Function<EntityRendererProvider.Context, R> entityRendererConstructor);

  <T extends AbstractContainerMenu> MenuType<T> createMenuType(BiFunction<Integer, Inventory, T> menuConstructor, FeatureFlagSet flagSet);

  <M extends AbstractContainerMenu, S extends AbstractContainerScreen<M>> void registerScreen(MenuType<M> menuType, TriFunction<M, Inventory, Component, S> screenConstructor);
}
