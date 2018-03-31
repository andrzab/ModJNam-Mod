package com.Mrbysco.CactusMod.items.tools;

import java.util.Set;

import com.Mrbysco.CactusMod.Reference;
import com.Mrbysco.CactusMod.init.CactusItems;
import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemCactusAxe extends ItemTool{
    private static final Set<Block> EFFECTIVE_ON = Sets.newHashSet(Blocks.PLANKS, Blocks.BOOKSHELF, Blocks.LOG, Blocks.LOG2, Blocks.CHEST, Blocks.PUMPKIN, Blocks.LIT_PUMPKIN, Blocks.MELON_BLOCK, Blocks.LADDER, Blocks.WOODEN_BUTTON, Blocks.WOODEN_PRESSURE_PLATE);
	private final ToolMaterial material;
    
	public ItemCactusAxe(String registryName) {
		super(CactusItems.cactusMaterial, EFFECTIVE_ON);
		
		this.material = CactusItems.cactusMaterial;
        this.maxStackSize = 1;
        this.attackDamage = 7.0F;
        this.attackSpeed = -3.2F;
        
		this.setUnlocalizedName(Reference.PREFIX + registryName.replaceAll("_", ""));
		this.setRegistryName(registryName);
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos,
			EntityLivingBase entityLiving) {
		entityLiving.attackEntityFrom(DamageSource.CACTUS, 0.5F);
		return super.onBlockDestroyed(stack, worldIn, state, pos, entityLiving);
	}
	
	public float getDestroySpeed(ItemStack stack, IBlockState state)
    {
        Material material = state.getMaterial();
        return material != Material.WOOD && material != Material.PLANTS && material != Material.VINE ? super.getDestroySpeed(stack, state) : this.efficiency;
    }
}
