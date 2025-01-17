package com.mrbysco.cactusmod.entities;

import com.mrbysco.cactusmod.init.CactusRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.common.Tags.Blocks;

import java.util.Random;

public class CactusPigEntity extends Pig implements ICactusMob {

	public CactusPigEntity(EntityType<? extends Pig> entityType, Level worldIn) {
		super(entityType, worldIn);
	}

	public CactusPigEntity getBreedOffspring(ServerLevel level, AgeableMob ageableMob) {
		return CactusRegistry.CACTUS_PIG.get().create(level);
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0D).add(Attributes.MOVEMENT_SPEED, 0.25D);
	}

	public static boolean canAnimalSpawn(EntityType<? extends Animal> animal, LevelAccessor worldIn, MobSpawnType reason, BlockPos pos, Random random) {
		return worldIn.getBlockState(pos.below()).is(Blocks.SAND) && worldIn.getRawBrightness(pos, 0) > 8;
	}
}
