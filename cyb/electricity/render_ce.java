package cyb.electricity;

import java.util.logging.Level;

import javax.swing.text.html.parser.Entity;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cyb.c.common_c;
import cyb.c.common_c.color;

public class render_ce implements ISimpleBlockRenderingHandler
{
	public int rid;
	
	public void ren_inv_wire(Block block, int meta, RenderBlocks ren)
	{
		Tessellator tes = Tessellator.instance;
        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        tes.startDrawingQuads();
        tes.setNormal(0.0F, -1.0F, 0.0F);
        ren.renderBottomFace(block, 0.0D, 0.0D, 0.0D, 50);
        tes.draw();
        tes.startDrawingQuads();
        tes.setNormal(0.0F, 1.0F, 0.0F);
        ren.renderTopFace(block, 0.0D, 0.0D, 0.0D, 50);
        tes.draw();
        tes.startDrawingQuads();
        tes.setNormal(0.0F, 0.0F, -1.0F);
        ren.renderEastFace(block, 0.0D, 0.0D, 0.0D, 50);
        tes.draw();
        tes.startDrawingQuads();
        tes.setNormal(0.0F, 0.0F, 1.0F);
        ren.renderWestFace(block, 0.0D, 0.0D, 0.0D, 50);
        tes.draw();
        tes.startDrawingQuads();
        tes.setNormal(-1.0F, 0.0F, 0.0F);
        ren.renderNorthFace(block, 0.0D, 0.0D, 0.0D, 53);
        tes.draw();
        tes.startDrawingQuads();
        tes.setNormal(1.0F, 0.0F, 0.0F);
        ren.renderSouthFace(block, 0.0D, 0.0D, 0.0D, 53);
        tes.draw();
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	}
	
	public void ren_inv_std(Block block, int meta, RenderBlocks ren)
	{
		Tessellator tes = Tessellator.instance;
        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        tes.startDrawingQuads();
        tes.setNormal(0.0F, -1.0F, 0.0F);
        ren.renderBottomFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(0, meta));
        tes.draw();
        tes.startDrawingQuads();
        tes.setNormal(0.0F, 1.0F, 0.0F);
        ren.renderTopFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(1, meta));
        tes.draw();
        tes.startDrawingQuads();
        tes.setNormal(0.0F, 0.0F, -1.0F);
        ren.renderEastFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(2, meta));
        tes.draw();
        tes.startDrawingQuads();
        tes.setNormal(0.0F, 0.0F, 1.0F);
        ren.renderWestFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(3, meta));
        tes.draw();
        tes.startDrawingQuads();
        tes.setNormal(-1.0F, 0.0F, 0.0F);
        ren.renderNorthFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(4, meta));
        tes.draw();
        tes.startDrawingQuads();
        tes.setNormal(1.0F, 0.0F, 0.0F);
        ren.renderSouthFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(5, meta));
        tes.draw();
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	}
	
	public void ren_inv_que(Block block, int meta, RenderBlocks ren)
	{
		Tessellator tes = Tessellator.instance;
        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        tes.startDrawingQuads();
        tes.setNormal(0.0F, -1.0F, 0.0F);
        ren.renderBottomFace(block, 0.0D, 0.0D, 0.0D, 0);
        tes.draw();
        tes.startDrawingQuads();
        tes.setNormal(0.0F, 1.0F, 0.0F);
        ren.renderTopFace(block, 0.0D, 0.0D, 0.0D, 0);
        tes.draw();
        tes.startDrawingQuads();
        tes.setNormal(0.0F, 0.0F, -1.0F);
        ren.renderEastFace(block, 0.0D, 0.0D, 0.0D, 0);
        tes.draw();
        tes.startDrawingQuads();
        tes.setNormal(0.0F, 0.0F, 1.0F);
        ren.renderWestFace(block, 0.0D, 0.0D, 0.0D, 0);
        tes.draw();
        tes.startDrawingQuads();
        tes.setNormal(-1.0F, 0.0F, 0.0F);
        ren.renderNorthFace(block, 0.0D, 0.0D, 0.0D, 0);
        tes.draw();
        tes.startDrawingQuads();
        tes.setNormal(1.0F, 0.0F, 0.0F);
        ren.renderSouthFace(block, 0.0D, 0.0D, 0.0D, 0);
        tes.draw();
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	}
	
	@Override
	public void renderInventoryBlock(Block block, int meta, int rid,
			RenderBlocks ren)
	{
		if(meta == 2)
		{
			ren.setRenderBounds(0, 6. / 16., 6. / 16., 1, 10. / 16., 10. / 16.);
			ren_inv_wire(block, meta, ren);
		}
		else if(meta == 5)
		{
			ren.setRenderBoundsFromBlock(block);
			ren_inv_que(block, meta, ren);
		}
		else
		{
			ren.setRenderBoundsFromBlock(block);
			ren_inv_std(block, meta, ren);
		}
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z,
			Block block, int mid, RenderBlocks ren)
	{
		if(!(world.getBlockTileEntity(x, y, z) instanceof tile_ce))
			return false;
		tile_ce te = (tile_ce)world.getBlockTileEntity(x, y, z);
		int meta = te.get_meta();
		if(meta == 2)
		{
			ren.setRenderBounds(6. / 16., 6. / 16., 6. / 16., 10. / 16., 10. / 16., 10. / 16.);
			ren.renderStandardBlock(block, x, y, z);
			if(te.wire_XN)
			{
				ren.setRenderBounds(0, 6. / 16., 6. / 16., 6. / 16., 10. / 16., 10. / 16.);
				ren.renderStandardBlock(block, x, y, z);
			}
			if(te.wire_XP)
			{
				ren.setRenderBounds(10. / 16., 6. / 16., 6. / 16., 1., 10. / 16., 10. / 16.);
				ren.renderStandardBlock(block, x, y, z);
			}
			if(te.wire_YN)
			{
				ren.setRenderBounds(6. / 16., 0., 6. / 16., 10. / 16., 6. / 16., 10. / 16.);
				ren.renderStandardBlock(block, x, y, z);
			}
			if(te.wire_YP)
			{
				ren.setRenderBounds(6. / 16., 10. / 16., 6. / 16., 10. / 16., 1., 10. / 16.);
				ren.renderStandardBlock(block, x, y, z);
			}
			if(te.wire_ZN)
			{
				ren.setRenderBounds(6. / 16., 6. / 16., 0., 10. / 16., 10. / 16., 6. / 16.);
				ren.renderStandardBlock(block, x, y, z);
			}
			if(te.wire_ZP)
			{
				ren.setRenderBounds(6. / 16., 6. / 16., 10. / 16., 10. / 16., 10. / 16., 1.);
				ren.renderStandardBlock(block, x, y, z);
			}
			return true;
		}
		else if(meta == 5)
		{
			if(!res.isvalid(te.rid))
				return ren.renderStandardBlock(block, x, y, z);
			int tex = res.texind(te.rid);
			common_c.color coe = new common_c.color(0xffffff);
			if(EntityRenderer.anaglyphEnable)
				coe.anaglyph();
			if(Minecraft.isAmbientOcclusionEnabled())
			{
				ren.enableAO = true;
				common_c.prepare_ao(ren, world, block, x, y, z);
			}
			else
				ren.enableAO = false;
			int bC = block.getMixedBrightnessForBlock(world, x, y, z);
			Tessellator tes = Tessellator.instance;
			if(te.facing == 1)
			{
				if(ren.enableAO)
					common_c.aoYP(ren, world, block, x, y, z, bC, coe);
				else
				{
					tes.setBrightness(bC);
					tes.setColorOpaque_F(coe.r, coe.g, coe.b);
				}
				ren.setRenderBounds(1. / 16., 5. / 16., 5. / 16., 15. / 16., 11. / 16., 11. / 16.);
				ren.renderTopFace(block, x, y, z, tex);
				ren.setRenderBounds(0., 5. / 16., 5. / 16., 1. / 16., 11. / 16., 11. / 16.);
				ren.renderTopFace(block, x, y, z, 65);
				ren.setRenderBounds(15. / 16., 5. / 16., 5. / 16., 1., 11. / 16., 11. / 16.);
				ren.renderTopFace(block, x, y, z, 65);
				if(ren.enableAO)
					common_c.aoYN(ren, world, block, x, y, z, bC, coe);
				else
					tes.setColorOpaque_F(coe.r * .5f, coe.g * .5f, coe.b * .5f);
				ren.setRenderBounds(1. / 16., 5. / 16., 5. / 16., 15. / 16., 11. / 16., 11. / 16.);
				ren.renderBottomFace(block, x, y, z, tex);
				ren.setRenderBounds(0., 5. / 16., 5. / 16., 1. / 16., 11. / 16., 11. / 16.);
				ren.renderBottomFace(block, x, y, z, 65);
				ren.setRenderBounds(15. / 16., 5. / 16., 5. / 16., 1., 11. / 16., 11. / 16.);
				ren.renderBottomFace(block, x, y, z, 65);
				if(ren.enableAO)
					common_c.aoZP(ren, world, block, x, y, z, bC, coe);
				else
				{
					tes.setBrightness(bC);
					tes.setColorOpaque_F(coe.r * .8f, coe.g * .8f, coe.b * .8f);
				}
				ren.setRenderBounds(1. / 16., 5. / 16., 5. / 16., 15. / 16., 11. / 16., 11. / 16.);
				ren.renderWestFace(block, x, y, z, tex);
				ren.setRenderBounds(0., 5. / 16., 5. / 16., 1. / 16., 11. / 16., 11. / 16.);
				ren.renderWestFace(block, x, y, z, 65);
				ren.setRenderBounds(15. / 16., 5. / 16., 5. / 16., 1., 11. / 16., 11. / 16.);
				ren.renderWestFace(block, x, y, z, 65);
				if(ren.enableAO)
					common_c.aoZN(ren, world, block, x, y, z, bC, coe);
				else
					tes.setColorOpaque_F(coe.r * .8f, coe.g * .8f, coe.b * .8f);
				ren.uvRotateEast = 3;
				ren.setRenderBounds(1. / 16., 5. / 16., 5. / 16., 15. / 16., 11. / 16., 11. / 16.);
				ren.renderEastFace(block, x, y, z, tex);
				ren.setRenderBounds(0., 5. / 16., 5. / 16., 1. / 16., 11. / 16., 11. / 16.);
				ren.renderEastFace(block, x, y, z, 65);
				ren.setRenderBounds(15. / 16., 5. / 16., 5. / 16., 1., 11. / 16., 11. / 16.);
				ren.renderEastFace(block, x, y, z, 65);
				ren.uvRotateEast = 0;
				if(ren.enableAO)
					common_c.aoXP(ren, world, block, x, y, z, bC, coe);
				else
				{
					tes.setBrightness(bC);
					tes.setColorOpaque_F(coe.r * .6f, coe.g * .6f, coe.b * .6f);
				}
				ren.setRenderBounds(0., 5. / 16., 5. / 16., 1. / 16., 11. / 16., 11. / 16.);
				ren.renderSouthFace(block, x, y, z, 1);
				ren.setRenderBounds(15. / 16., 5. / 16., 5. / 16., 1., 11. / 16., 11. / 16.);
				ren.renderSouthFace(block, x, y, z, 1);
				if(ren.enableAO)
					common_c.aoXN(ren, world, block, x, y, z, bC, coe);
				else
					tes.setColorOpaque_F(coe.r * .6f, coe.g * .6f, coe.b * .6f);
				ren.setRenderBounds(0., 5. / 16., 5. / 16., 1. / 16., 11. / 16., 11. / 16.);
				ren.renderNorthFace(block, x, y, z, 1);
				ren.setRenderBounds(15. / 16., 5. / 16., 5. / 16., 1., 11. / 16., 11. / 16.);
				ren.renderNorthFace(block, x, y, z, 1);
			}
			else if(te.facing == 2)
			{
				if(ren.enableAO)
					common_c.aoXP(ren, world, block, x, y, z, bC, coe);
				else
				{
					tes.setBrightness(bC);
					tes.setColorOpaque_F(coe.r * .6f, coe.g * .6f, coe.b * .6f);
				}
				ren.setRenderBounds(5. / 16., 1. / 16., 5. / 16., 11. / 16., 15. / 16., 11. / 16.);
				ren.uvRotateSouth = 2;
				ren.renderSouthFace(block, x, y, z, tex);
				ren.setRenderBounds(5. / 16., 0., 5. / 16., 11. / 16., 1. / 16., 11. / 16.);
				ren.renderSouthFace(block, x, y, z, 65);
				ren.setRenderBounds(5. / 16., 15. / 16., 5. / 16., 11. / 16., 1., 11. / 16.);
				ren.renderSouthFace(block, x, y, z, 65);
				ren.uvRotateSouth = 0;
				if(ren.enableAO)
					common_c.aoXN(ren, world, block, x, y, z, bC, coe);
				else
					tes.setColorOpaque_F(coe.r * .6f, coe.g * .6f, coe.b * .6f);
				ren.setRenderBounds(5. / 16., 1. / 16., 5. / 16., 11. / 16., 15. / 16., 11. / 16.);
				ren.uvRotateNorth = 1;
				ren.renderNorthFace(block, x, y, z, tex);
				ren.setRenderBounds(5. / 16., 0., 5. / 16., 11. / 16., 1. / 16., 11. / 16.);
				ren.renderNorthFace(block, x, y, z, 65);
				ren.setRenderBounds(5. / 16., 15. / 16., 5. / 16., 11. / 16., 1., 11. / 16.);
				ren.renderNorthFace(block, x, y, z, 65);
				ren.uvRotateNorth = 0;
				if(ren.enableAO)
					common_c.aoZP(ren, world, block, x, y, z, bC, coe);
				else
				{
					tes.setBrightness(bC);
					tes.setColorOpaque_F(coe.r * .8f, coe.g * .8f, coe.b * .8f);
				}
				ren.uvRotateWest = 1;
				ren.setRenderBounds(5. / 16., 1. / 16., 5. / 16., 11. / 16., 15. / 16., 11. / 16.);
				ren.renderWestFace(block, x, y, z, tex);
				ren.setRenderBounds(5. / 16., 0., 5. / 16., 11. / 16., 1. / 16., 11. / 16.);
				ren.renderWestFace(block, x, y, z, 65);
				ren.setRenderBounds(5. / 16., 15. / 16., 5. / 16., 11. / 16., 1., 11. / 16.);
				ren.renderWestFace(block, x, y, z, 65);
				ren.uvRotateWest = 0;
				if(ren.enableAO)
					common_c.aoZN(ren, world, block, x, y, z, bC, coe);
				else
					tes.setColorOpaque_F(coe.r * .8f, coe.g * .8f, coe.b * .8f);
				ren.uvRotateEast = 2;
				ren.setRenderBounds(5. / 16., 1. / 16., 5. / 16., 11. / 16., 15. / 16., 11. / 16.);
				ren.renderEastFace(block, x, y, z, tex);
				ren.setRenderBounds(5. / 16., 0., 5. / 16., 11. / 16., 1. / 16., 11. / 16.);
				ren.renderEastFace(block, x, y, z, 65);
				ren.setRenderBounds(5. / 16., 15. / 16., 5. / 16., 11. / 16., 1., 11. / 16.);
				ren.renderEastFace(block, x, y, z, 65);
				ren.uvRotateEast = 0;
				if(ren.enableAO)
					common_c.aoYP(ren, world, block, x, y, z, bC, coe);
				else
				{
					tes.setBrightness(bC);
					tes.setColorOpaque_F(coe.r, coe.g, coe.b);
				}
				ren.setRenderBounds(5. / 16., 0., 5. / 16., 11. / 16., 1. / 16., 11. / 16.);
				ren.renderTopFace(block, x, y, z, 1);
				ren.setRenderBounds(5. / 16., 15. / 16., 5. / 16., 11. / 16., 1., 11. / 16.);
				ren.renderTopFace(block, x, y, z, 1);
				if(ren.enableAO)
					common_c.aoYN(ren, world, block, x, y, z, bC, coe);
				else
					tes.setColorOpaque_F(coe.r * .5f, coe.g * .5f, coe.b * .5f);
				ren.setRenderBounds(5. / 16., 0., 5. / 16., 11. / 16., 1. / 16., 11. / 16.);
				ren.renderBottomFace(block, x, y, z, 1);
				ren.setRenderBounds(5. / 16., 15. / 16., 5. / 16., 11. / 16., 1., 11. / 16.);
				ren.renderBottomFace(block, x, y, z, 1);
			}
			else if(te.facing == 3)
			{
				if(ren.enableAO)
					common_c.aoYP(ren, world, block, x, y, z, bC, coe);
				else
				{
					tes.setBrightness(bC);
					tes.setColorOpaque_F(coe.r, coe.g, coe.b);
				}
				ren.uvRotateTop = 1;
				ren.setRenderBounds(5. / 16., 5. / 16., 1. / 16., 11. / 16., 11. / 16., 15. / 16.);
				ren.renderTopFace(block, x, y, z, tex);
				ren.setRenderBounds(5. / 16., 5. / 16., 0., 11. / 16., 11. / 16., 1. / 16.);
				ren.renderTopFace(block, x, y, z, 65);
				ren.setRenderBounds(5. / 16., 5. / 16., 15. / 16., 11. / 16., 11. / 16., 1.);
				ren.renderTopFace(block, x, y, z, 65);
				ren.uvRotateTop = 0;
				if(ren.enableAO)
					common_c.aoYN(ren, world, block, x, y, z, bC, coe);
				else
					tes.setColorOpaque_F(coe.r * .5f, coe.g * .5f, coe.b * .5f);
				ren.uvRotateBottom = 2;
				ren.setRenderBounds(5. / 16., 5. / 16., 1. / 16., 11. / 16., 11. / 16., 15. / 16.);
				ren.renderBottomFace(block, x, y, z, tex);
				ren.setRenderBounds(5. / 16., 5. / 16., 0., 11. / 16., 11. / 16., 1. / 16.);
				ren.renderBottomFace(block, x, y, z, 65);
				ren.setRenderBounds(5. / 16., 5. / 16., 15. / 16., 11. / 16., 11. / 16., 1.);
				ren.renderBottomFace(block, x, y, z, 65);
				ren.uvRotateBottom = 0;
				if(ren.enableAO)
					common_c.aoXP(ren, world, block, x, y, z, bC, coe);
				else
				{
					tes.setBrightness(bC);
					tes.setColorOpaque_F(coe.r * .6f, coe.g * .6f, coe.b * .6f);
				}
				ren.uvRotateSouth = 3;
				ren.setRenderBounds(5. / 16., 5. / 16., 1. / 16., 11. / 16., 11. / 16., 15. / 16.);
				ren.renderSouthFace(block, x, y, z, tex);
				ren.setRenderBounds(5. / 16., 5. / 16., 0., 11. / 16., 11. / 16., 1. / 16.);
				ren.renderSouthFace(block, x, y, z, 65);
				ren.setRenderBounds(5. / 16., 5. / 16., 15. / 16., 11. / 16., 11. / 16., 1.);
				ren.renderSouthFace(block, x, y, z, 65);
				ren.uvRotateSouth = 0;
				if(ren.enableAO)
					common_c.aoXN(ren, world, block, x, y, z, bC, coe);
				else
					tes.setColorOpaque_F(coe.r * .6f, coe.g * .6f, coe.b * .6f);
				ren.uvRotateEast = 3;
				ren.setRenderBounds(5. / 16., 5. / 16., 1. / 16., 11. / 16., 11. / 16., 15. / 16.);
				ren.renderNorthFace(block, x, y, z, tex);
				ren.setRenderBounds(5. / 16., 5. / 16., 0., 11. / 16., 11. / 16., 1. / 16.);
				ren.renderNorthFace(block, x, y, z, 65);
				ren.setRenderBounds(5. / 16., 5. / 16., 15. / 16., 11. / 16., 11. / 16., 1.);
				ren.renderNorthFace(block, x, y, z, 65);
				ren.uvRotateEast = 0;
				if(ren.enableAO)
					common_c.aoZP(ren, world, block, x, y, z, bC, coe);
				else
				{
					tes.setBrightness(bC);
					tes.setColorOpaque_F(coe.r * .8f, coe.g * .8f, coe.b * .8f);
				}
				ren.setRenderBounds(5. / 16., 5. / 16., 0., 11. / 16., 11. / 16., 1. / 16.);
				ren.renderWestFace(block, x, y, z, 1);
				ren.setRenderBounds(5. / 16., 5. / 16., 15. / 16., 11. / 16., 11. / 16., 1.);
				ren.renderWestFace(block, x, y, z, 1);
				if(ren.enableAO)
					common_c.aoZN(ren, world, block, x, y, z, bC, coe);
				else
					tes.setColorOpaque_F(coe.r * .8f, coe.g * .8f, coe.b * .8f);
				ren.setRenderBounds(5. / 16., 5. / 16., 0., 11. / 16., 11. / 16., 1. / 16.);
				ren.renderEastFace(block, x, y, z, 1);
				ren.setRenderBounds(5. / 16., 5. / 16., 15. / 16., 11. / 16., 11. / 16., 1.);
				ren.renderEastFace(block, x, y, z, 1);
			}
			else
				return ren.renderStandardBlock(block, x, y, z);
			return true;
		}
		else
			return ren.renderStandardBlock(block, x, y, z);
	}

	@Override
	public boolean shouldRender3DInInventory()
	{
		return true;
	}

	@Override
	public int getRenderId()
	{
		return rid;
	}

}
