package com.Mrbysco.CactusMod.blocks;

import com.Mrbysco.CactusMod.CactusMod;
import com.Mrbysco.CactusMod.Reference;

import net.minecraft.block.BlockCake;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CactusCakeBlock extends BlockCake{
	public CactusCakeBlock(String registryName) {		
		setHardness(0.5F);
		setSoundType(SoundType.CLOTH);
		
		this.setUnlocalizedName(Reference.PREFIX + registryName.replaceAll("_", ""));
		this.setRegistryName(registryName);
	}
	
	@Override
	public Material getMaterial(IBlockState state) {
		return Material.CAKE;
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
		ItemStack itemstack = playerIn.getHeldItem(hand);
		
        if (!worldIn.isRemote)
            return this.eatCake(worldIn, pos, state, playerIn);
        else
            return this.eatCake(worldIn, pos, state, playerIn) || itemstack.isEmpty();
    }
	
	private boolean eatCake(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player)
    {
        if (!player.canEat(false))
        {
            return false;
        }
        else
        {
            player.addStat(StatList.CAKE_SLICES_EATEN);
            player.attackEntityFrom(DamageSource.CACTUS, 0.5F);
            player.getFoodStats().addStats(2, 0.1F);
            int i = ((Integer)state.getValue(BITES)).intValue();

            if (i < 6)
            {
                worldIn.setBlockState(pos, state.withProperty(BITES, Integer.valueOf(i + 1)), 3);
            }
            else
            {
                worldIn.setBlockToAir(pos);
            }

            return true;
        }
    }
	
	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		super.onEntityCollidedWithBlock(worldIn, pos, state, entityIn);
        entityIn.attackEntityFrom(DamageSource.CACTUS, 1.0F);
	}
}
