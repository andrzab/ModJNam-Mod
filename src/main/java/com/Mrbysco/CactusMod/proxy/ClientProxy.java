package com.Mrbysco.CactusMod.proxy;

import com.Mrbysco.CactusMod.entities.EntityCactusGolem;
import com.Mrbysco.CactusMod.render.RenderCactusGolem;

import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy{

	@Override
	public void preInit()
	{
		RegisterEntityRenders();
	}
	
	@Override
	public void init()
	{
		
	}
	
	public static void RegisterEntityRenders()
	{
		RenderingRegistry.registerEntityRenderingHandler(EntityCactusGolem.class, RenderCactusGolem.FACTORY);
	}
}
