package com.mrbysco.cactusmod.blocks;

import com.mrbysco.cactusmod.blocks.decorative.BlockRotatable;
import com.mrbysco.cactusmod.entities.CactusGolem;
import com.mrbysco.cactusmod.entities.CactusSnowGolemEntity;
import com.mrbysco.cactusmod.init.CactusRegistry;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import net.minecraft.world.level.block.state.predicate.BlockMaterialPredicate;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class CarvedCactusBlock extends BlockRotatable {
    protected static final VoxelShape COLLISION_SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 15.0D, 15.0D);
    protected static final VoxelShape OUTLINE_SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D);

    @Nullable
    private static BlockPattern cactusGolemPattern;
    @Nullable
    private static BlockPattern cactusSnowmanPattern;

    private static final java.util.function.Predicate<BlockState> IS_CARVED_CACTUS = (state) -> state != null && (state.getBlock() == CactusRegistry.CARVED_CACTUS.get() || state.getBlock() == CactusRegistry.JACKO_CACTUS.get());

    
	public CarvedCactusBlock(BlockBehaviour.Properties builder) {
		super(builder);
	}

    @Override
    public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entityIn) {
        entityIn.hurt(DamageSource.CACTUS, 1.0F);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return COLLISION_SHAPE;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return OUTLINE_SHAPE;
    }

    @Override
    public void onPlace(BlockState state, Level worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!oldState.is(state.getBlock())) {
            this.trySpawnGolem(worldIn, pos);
        }
    }

    private void trySpawnGolem(Level world, BlockPos pos) {
        BlockPattern.BlockPatternMatch blockpattern$patternhelper = this.getCactusSnowmanPattern().find(world, pos);
        if (blockpattern$patternhelper != null) {
            for(int i = 0; i < this.getCactusSnowmanPattern().getHeight(); ++i) {
                BlockInWorld cachedblockinfo = blockpattern$patternhelper.getBlock(0, i, 0);
                world.setBlock(cachedblockinfo.getPos(), Blocks.AIR.defaultBlockState(), 2);
                world.levelEvent(2001, cachedblockinfo.getPos(), Block.getId(cachedblockinfo.getState()));
            }

            CactusSnowGolemEntity cactusSnowman = null;//EntityType.SNOW_GOLEM.create(world);
            BlockPos blockpos1 = blockpattern$patternhelper.getBlock(0, 2, 0).getPos();
            cactusSnowman.moveTo((double)blockpos1.getX() + 0.5D, (double)blockpos1.getY() + 0.05D, (double)blockpos1.getZ() + 0.5D, 0.0F, 0.0F);
            world.addFreshEntity(cactusSnowman);

            for(ServerPlayer serverplayerentity : world.getEntitiesOfClass(ServerPlayer.class, cactusSnowman.getBoundingBox().inflate(5.0D))) {
                CriteriaTriggers.SUMMONED_ENTITY.trigger(serverplayerentity, cactusSnowman);
            }

            for(int l = 0; l < this.getCactusSnowmanPattern().getHeight(); ++l) {
                BlockInWorld cachedblockinfo3 = blockpattern$patternhelper.getBlock(0, l, 0);
                world.blockUpdated(cachedblockinfo3.getPos(), Blocks.AIR);
            }
        } else {
            blockpattern$patternhelper = this.getCactusGolemPattern().find(world, pos);
            if (blockpattern$patternhelper != null) {
                for(int j = 0; j < this.getCactusGolemPattern().getWidth(); ++j) {
                    for(int k = 0; k < this.getCactusGolemPattern().getHeight(); ++k) {
                        BlockInWorld cachedblockinfo2 = blockpattern$patternhelper.getBlock(j, k, 0);
                        world.setBlock(cachedblockinfo2.getPos(), Blocks.AIR.defaultBlockState(), 2);
                        world.levelEvent(2001, cachedblockinfo2.getPos(), Block.getId(cachedblockinfo2.getState()));
                    }
                }

                BlockPos blockpos = blockpattern$patternhelper.getBlock(1, 2, 0).getPos();
                CactusGolem cactusGolem = (CactusGolem)EntityType.IRON_GOLEM.create(world);
                cactusGolem.setPlayerCreated(true);
                cactusGolem.moveTo((double)blockpos.getX() + 0.5D, (double)blockpos.getY() + 0.05D, (double)blockpos.getZ() + 0.5D, 0.0F, 0.0F);
                world.addFreshEntity(cactusGolem);

                for(ServerPlayer serverplayerentity1 : world.getEntitiesOfClass(ServerPlayer.class, cactusGolem.getBoundingBox().inflate(5.0D))) {
                    CriteriaTriggers.SUMMONED_ENTITY.trigger(serverplayerentity1, cactusGolem);
                }

                for(int i1 = 0; i1 < this.getCactusGolemPattern().getWidth(); ++i1) {
                    for(int j1 = 0; j1 < this.getCactusGolemPattern().getHeight(); ++j1) {
                        BlockInWorld cachedblockinfo1 = blockpattern$patternhelper.getBlock(i1, j1, 0);
                        world.blockUpdated(cachedblockinfo1.getPos(), Blocks.AIR);
                    }
                }
            }
        }
    }
    
    protected BlockPattern getCactusSnowmanPattern() {
        if (cactusSnowmanPattern == null) {
        	cactusSnowmanPattern = BlockPatternBuilder.start().aisle("^", "#", "#").where('^', BlockInWorld.hasState(IS_CARVED_CACTUS)).where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.CACTUS))).build();
        }

        return cactusSnowmanPattern;
    }
    
    protected BlockPattern getCactusGolemPattern() {
        if (cactusGolemPattern == null) {
    		cactusGolemPattern = BlockPatternBuilder.start().aisle("~^~", "###", "~#~").where('^', BlockInWorld.hasState(IS_CARVED_CACTUS)).where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(CactusRegistry.PRICKLY_IRON.get()))).where('~', BlockInWorld.hasState(BlockMaterialPredicate.forMaterial(Material.AIR))).build();
        }

        return cactusGolemPattern;
    }
}