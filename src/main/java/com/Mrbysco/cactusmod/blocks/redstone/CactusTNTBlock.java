package com.mrbysco.cactusmod.blocks.redstone;

import com.mrbysco.cactusmod.entities.AbstractSpikeEntity;
import com.mrbysco.cactusmod.entities.CactusTNTEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.List;

public class CactusTNTBlock extends TntBlock {
	protected static final VoxelShape COLLISION_SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 15.0D, 15.0D);
	protected static final VoxelShape OUTLINE_SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D);

	public CactusTNTBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}

	public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return COLLISION_SHAPE;
	}

	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return OUTLINE_SHAPE;
	}

	@Override
	public void onCaughtFire(BlockState state, Level world, BlockPos pos, @Nullable Direction face, @Nullable LivingEntity igniter) {
		CactusTNTBlock.explode(world, pos, igniter);
	}

	@Override
	public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entityIn) {
		entityIn.hurt(DamageSource.CACTUS, 1.0F);
		super.entityInside(state, worldIn, pos, entityIn);
	}

	public static void explode(Level world, BlockPos worldIn) {
		explode(world, worldIn, (LivingEntity) null);
	}

	public static void explode(Level worldIn, BlockPos pos, @Nullable LivingEntity entityIn) {
		if (!worldIn.isClientSide) {
			CactusTNTEntity cactusTNTEntity = new CactusTNTEntity(worldIn, (double) pos.getX() + 0.5D, (double) pos.getY(), (double) pos.getZ() + 0.5D, entityIn);
			worldIn.addFreshEntity(cactusTNTEntity);
			worldIn.playSound((Player) null, cactusTNTEntity.getX(), cactusTNTEntity.getY(), cactusTNTEntity.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
		}
	}

	@Override
	public void onProjectileHit(Level level, BlockState state, BlockHitResult hitResult, Projectile projectile) {
		if (projectile instanceof AbstractSpikeEntity) {
			level.destroyBlock(hitResult.getBlockPos(), false, null);
			explode(level, hitResult.getBlockPos());
		}
		super.onProjectileHit(level, state, hitResult, projectile);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable BlockGetter worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
		tooltip.add(new TranslatableComponent("cactus.tnt.info").withStyle(ChatFormatting.GREEN));
	}
}
