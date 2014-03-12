package cyb.c;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

//通用函数库（渲染相关） by CYB

public class common_c
{
	public static class color
	{
		public float r = 0, g = 0, b = 0;
		public color(){}
		public color(int ival)
		{
			r = (float)(ival >> 16 & 255) / 255.0F;
			g = (float)(ival >> 8 & 255) / 255.0F;
			b = (float)(ival & 255) / 255.0F;
		}
		public void anaglyph()
		{
            float new_r = (r * 30.0F + g * 59.0F + b * 11.0F) / 100.0F;
            float new_g = (r * 30.0F + g * 70.0F) / 100.0F;
            float new_b = (r * 30.0F + b * 70.0F) / 100.0F;
            r = new_r;
            g = new_g;
            b = new_b;
		}
	}
	
	public static void prepare_ao(RenderBlocks renderer, IBlockAccess world, Block block, int x, int y, int z)
	{
		renderer.lightValueOwn = block.getAmbientOcclusionLightValue(world, x, y, z);
		renderer.aoLightValueXNeg = block.getAmbientOcclusionLightValue(world, x - 1, y, z);
		renderer.aoLightValueXPos = block.getAmbientOcclusionLightValue(world, x + 1, y, z);
		renderer.aoLightValueYNeg = block.getAmbientOcclusionLightValue(world, x, y - 1, z);
		renderer.aoLightValueYPos = block.getAmbientOcclusionLightValue(world, x, y + 1, z);
		renderer.aoLightValueZNeg = block.getAmbientOcclusionLightValue(world, x, y, z - 1);
		renderer.aoLightValueZPos = block.getAmbientOcclusionLightValue(world, x, y, z + 1);
		renderer.aoGrassXYZPPC = Block.canBlockGrass[world.getBlockId(x + 1, y + 1, z)];
		renderer.aoGrassXYZPNC = Block.canBlockGrass[world.getBlockId(x + 1, y - 1, z)];
		renderer.aoGrassXYZNPC = Block.canBlockGrass[world.getBlockId(x - 1, y + 1, z)];
		renderer.aoGrassXYZNNC = Block.canBlockGrass[world.getBlockId(x - 1, y - 1, z)];
		renderer.aoGrassXYZCPN = Block.canBlockGrass[world.getBlockId(x, y + 1, z - 1)];
		renderer.aoGrassXYZCNP = Block.canBlockGrass[world.getBlockId(x, y - 1, z + 1)];
		renderer.aoGrassXYZCPP = Block.canBlockGrass[world.getBlockId(x, y + 1, z + 1)];
		renderer.aoGrassXYZCNN = Block.canBlockGrass[world.getBlockId(x, y - 1, z - 1)];
		renderer.aoGrassXYZPCN = Block.canBlockGrass[world.getBlockId(x + 1, y, z - 1)];
		renderer.aoGrassXYZNCP = Block.canBlockGrass[world.getBlockId(x - 1, y, z + 1)];
		renderer.aoGrassXYZNCN = Block.canBlockGrass[world.getBlockId(x - 1, y, z - 1)];
		renderer.aoGrassXYZPCP = Block.canBlockGrass[world.getBlockId(x + 1, y, z + 1)];
	}
	
	public static void aoXN(RenderBlocks renderer, IBlockAccess world, Block block, int x, int y, int z, int bXN, color coe)
	{
		float v1, v2, v3, v4;
		if (renderer.aoType > 0)
        {
            renderer.aoLightValueScratchXYNN = block.getAmbientOcclusionLightValue(world, x, y - 1, z);
            renderer.aoLightValueScratchXZNN = block.getAmbientOcclusionLightValue(world, x, y, z - 1);
            renderer.aoLightValueScratchXZNP = block.getAmbientOcclusionLightValue(world, x, y, z + 1);
            renderer.aoLightValueScratchXYNP = block.getAmbientOcclusionLightValue(world, x, y + 1, z);
            renderer.aoBrightnessXYNN = block.getMixedBrightnessForBlock(world, x, y - 1, z);
            renderer.aoBrightnessXZNN = block.getMixedBrightnessForBlock(world, x, y, z - 1);
            renderer.aoBrightnessXZNP = block.getMixedBrightnessForBlock(world, x, y, z + 1);
            renderer.aoBrightnessXYNP = block.getMixedBrightnessForBlock(world, x, y + 1, z);

            if (!renderer.aoGrassXYZNCN && !renderer.aoGrassXYZNNC)
            {
                renderer.aoLightValueScratchXYZNNN = renderer.aoLightValueScratchXZNN;
                renderer.aoBrightnessXYZNNN = renderer.aoBrightnessXZNN;
            }
            else
            {
                renderer.aoLightValueScratchXYZNNN = block.getAmbientOcclusionLightValue(world, x, y - 1, z - 1);
                renderer.aoBrightnessXYZNNN = block.getMixedBrightnessForBlock(world, x, y - 1, z - 1);
            }

            if (!renderer.aoGrassXYZNCP && !renderer.aoGrassXYZNNC)
            {
                renderer.aoLightValueScratchXYZNNP = renderer.aoLightValueScratchXZNP;
                renderer.aoBrightnessXYZNNP = renderer.aoBrightnessXZNP;
            }
            else
            {
                renderer.aoLightValueScratchXYZNNP = block.getAmbientOcclusionLightValue(world, x, y - 1, z + 1);
                renderer.aoBrightnessXYZNNP = block.getMixedBrightnessForBlock(world, x, y - 1, z + 1);
            }

            if (!renderer.aoGrassXYZNCN && !renderer.aoGrassXYZNPC)
            {
                renderer.aoLightValueScratchXYZNPN = renderer.aoLightValueScratchXZNN;
                renderer.aoBrightnessXYZNPN = renderer.aoBrightnessXZNN;
            }
            else
            {
                renderer.aoLightValueScratchXYZNPN = block.getAmbientOcclusionLightValue(world, x, y + 1, z - 1);
                renderer.aoBrightnessXYZNPN = block.getMixedBrightnessForBlock(world, x, y + 1, z - 1);
            }

            if (!renderer.aoGrassXYZNCP && !renderer.aoGrassXYZNPC)
            {
                renderer.aoLightValueScratchXYZNPP = renderer.aoLightValueScratchXZNP;
                renderer.aoBrightnessXYZNPP = renderer.aoBrightnessXZNP;
            }
            else
            {
                renderer.aoLightValueScratchXYZNPP = block.getAmbientOcclusionLightValue(world, x, y + 1, z + 1);
                renderer.aoBrightnessXYZNPP = block.getMixedBrightnessForBlock(world, x, y + 1, z + 1);
            }

            v4 = (renderer.aoLightValueScratchXYNN + renderer.aoLightValueScratchXYZNNP + renderer.aoLightValueXNeg + renderer.aoLightValueScratchXZNP) / 4.0F;
            v1 = (renderer.aoLightValueXNeg + renderer.aoLightValueScratchXZNP + renderer.aoLightValueScratchXYNP + renderer.aoLightValueScratchXYZNPP) / 4.0F;
            v2 = (renderer.aoLightValueScratchXZNN + renderer.aoLightValueXNeg + renderer.aoLightValueScratchXYZNPN + renderer.aoLightValueScratchXYNP) / 4.0F;
            v3 = (renderer.aoLightValueScratchXYZNNN + renderer.aoLightValueScratchXYNN + renderer.aoLightValueScratchXZNN + renderer.aoLightValueXNeg) / 4.0F;
            renderer.brightnessTopRight = renderer.getAoBrightness(renderer.aoBrightnessXYNN, renderer.aoBrightnessXYZNNP, renderer.aoBrightnessXZNP, bXN);
            renderer.brightnessTopLeft = renderer.getAoBrightness(renderer.aoBrightnessXZNP, renderer.aoBrightnessXYNP, renderer.aoBrightnessXYZNPP, bXN);
            renderer.brightnessBottomLeft = renderer.getAoBrightness(renderer.aoBrightnessXZNN, renderer.aoBrightnessXYZNPN, renderer.aoBrightnessXYNP, bXN);
            renderer.brightnessBottomRight = renderer.getAoBrightness(renderer.aoBrightnessXYZNNN, renderer.aoBrightnessXYNN, renderer.aoBrightnessXZNN, bXN);
        }
        else
        {
            v1 = v2 = v3 = v4 = renderer.aoLightValueXNeg;
            renderer.brightnessTopLeft = renderer.brightnessBottomLeft = renderer.brightnessBottomRight = renderer.brightnessTopRight = bXN;
        }

        renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = coe.r * 0.6F;
        renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = coe.g * 0.6F;
        renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = coe.b * 0.6F;
        renderer.colorRedTopLeft *= v1;
        renderer.colorGreenTopLeft *= v1;
        renderer.colorBlueTopLeft *= v1;
        renderer.colorRedBottomLeft *= v2;
        renderer.colorGreenBottomLeft *= v2;
        renderer.colorBlueBottomLeft *= v2;
        renderer.colorRedBottomRight *= v3;
        renderer.colorGreenBottomRight *= v3;
        renderer.colorBlueBottomRight *= v3;
        renderer.colorRedTopRight *= v4;
        renderer.colorGreenTopRight *= v4;
        renderer.colorBlueTopRight *= v4;
	}
	
	public static void aoXP(RenderBlocks renderer, IBlockAccess world, Block block, int x, int y, int z, int bXP, color coe)
	{
		float v1, v2, v3, v4;
		if (renderer.aoType > 0)
        {
            renderer.aoLightValueScratchXYPN = block.getAmbientOcclusionLightValue(world, x, y - 1, z);
            renderer.aoLightValueScratchXZPN = block.getAmbientOcclusionLightValue(world, x, y, z - 1);
            renderer.aoLightValueScratchXZPP = block.getAmbientOcclusionLightValue(world, x, y, z + 1);
            renderer.aoLightValueScratchXYPP = block.getAmbientOcclusionLightValue(world, x, y + 1, z);
            renderer.aoBrightnessXYPN = block.getMixedBrightnessForBlock(world, x, y - 1, z);
            renderer.aoBrightnessXZPN = block.getMixedBrightnessForBlock(world, x, y, z - 1);
            renderer.aoBrightnessXZPP = block.getMixedBrightnessForBlock(world, x, y, z + 1);
            renderer.aoBrightnessXYPP = block.getMixedBrightnessForBlock(world, x, y + 1, z);

            if (!renderer.aoGrassXYZPNC && !renderer.aoGrassXYZPCN)
            {
                renderer.aoLightValueScratchXYZPNN = renderer.aoLightValueScratchXZPN;
                renderer.aoBrightnessXYZPNN = renderer.aoBrightnessXZPN;
            }
            else
            {
                renderer.aoLightValueScratchXYZPNN = block.getAmbientOcclusionLightValue(world, x, y - 1, z - 1);
                renderer.aoBrightnessXYZPNN = block.getMixedBrightnessForBlock(world, x, y - 1, z - 1);
            }

            if (!renderer.aoGrassXYZPNC && !renderer.aoGrassXYZPCP)
            {
                renderer.aoLightValueScratchXYZPNP = renderer.aoLightValueScratchXZPP;
                renderer.aoBrightnessXYZPNP = renderer.aoBrightnessXZPP;
            }
            else
            {
                renderer.aoLightValueScratchXYZPNP = block.getAmbientOcclusionLightValue(world, x, y - 1, z + 1);
                renderer.aoBrightnessXYZPNP = block.getMixedBrightnessForBlock(world, x, y - 1, z + 1);
            }

            if (!renderer.aoGrassXYZPPC && !renderer.aoGrassXYZPCN)
            {
                renderer.aoLightValueScratchXYZPPN = renderer.aoLightValueScratchXZPN;
                renderer.aoBrightnessXYZPPN = renderer.aoBrightnessXZPN;
            }
            else
            {
                renderer.aoLightValueScratchXYZPPN = block.getAmbientOcclusionLightValue(world, x, y + 1, z - 1);
                renderer.aoBrightnessXYZPPN = block.getMixedBrightnessForBlock(world, x, y + 1, z - 1);
            }

            if (!renderer.aoGrassXYZPPC && !renderer.aoGrassXYZPCP)
            {
                renderer.aoLightValueScratchXYZPPP = renderer.aoLightValueScratchXZPP;
                renderer.aoBrightnessXYZPPP = renderer.aoBrightnessXZPP;
            }
            else
            {
                renderer.aoLightValueScratchXYZPPP = block.getAmbientOcclusionLightValue(world, x, y + 1, z + 1);
                renderer.aoBrightnessXYZPPP = block.getMixedBrightnessForBlock(world, x, y + 1, z + 1);
            }

            v1 = (renderer.aoLightValueScratchXYPN + renderer.aoLightValueScratchXYZPNP + renderer.aoLightValueXPos + renderer.aoLightValueScratchXZPP) / 4.0F;
            v4 = (renderer.aoLightValueXPos + renderer.aoLightValueScratchXZPP + renderer.aoLightValueScratchXYPP + renderer.aoLightValueScratchXYZPPP) / 4.0F;
            v3 = (renderer.aoLightValueScratchXZPN + renderer.aoLightValueXPos + renderer.aoLightValueScratchXYZPPN + renderer.aoLightValueScratchXYPP) / 4.0F;
            v2 = (renderer.aoLightValueScratchXYZPNN + renderer.aoLightValueScratchXYPN + renderer.aoLightValueScratchXZPN + renderer.aoLightValueXPos) / 4.0F;
            renderer.brightnessTopLeft = renderer.getAoBrightness(renderer.aoBrightnessXYPN, renderer.aoBrightnessXYZPNP, renderer.aoBrightnessXZPP, bXP);
            renderer.brightnessTopRight = renderer.getAoBrightness(renderer.aoBrightnessXZPP, renderer.aoBrightnessXYPP, renderer.aoBrightnessXYZPPP, bXP);
            renderer.brightnessBottomRight = renderer.getAoBrightness(renderer.aoBrightnessXZPN, renderer.aoBrightnessXYZPPN, renderer.aoBrightnessXYPP, bXP);
            renderer.brightnessBottomLeft = renderer.getAoBrightness(renderer.aoBrightnessXYZPNN, renderer.aoBrightnessXYPN, renderer.aoBrightnessXZPN, bXP);
        }
        else
        {
            v1 = v2 = v3 = v4 = renderer.aoLightValueXPos;
            renderer.brightnessTopLeft = renderer.brightnessBottomLeft = renderer.brightnessBottomRight = renderer.brightnessTopRight = bXP;
        }

        renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = coe.r * 0.6F;
        renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = coe.g * 0.6F;
        renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = coe.b * 0.6F;
        renderer.colorRedTopLeft *= v1;
        renderer.colorGreenTopLeft *= v1;
        renderer.colorBlueTopLeft *= v1;
        renderer.colorRedBottomLeft *= v2;
        renderer.colorGreenBottomLeft *= v2;
        renderer.colorBlueBottomLeft *= v2;
        renderer.colorRedBottomRight *= v3;
        renderer.colorGreenBottomRight *= v3;
        renderer.colorBlueBottomRight *= v3;
        renderer.colorRedTopRight *= v4;
        renderer.colorGreenTopRight *= v4;
        renderer.colorBlueTopRight *= v4;
	}
	
	public static void aoZP(RenderBlocks renderer, IBlockAccess world, Block block, int x, int y, int z, int bZP, color coe)
	{
		float v1, v2, v3, v4;
		if(renderer.aoType > 0)
        {
            renderer.aoLightValueScratchXZNP = block.getAmbientOcclusionLightValue(world, x - 1, y, z);
            renderer.aoLightValueScratchXZPP = block.getAmbientOcclusionLightValue(world, x + 1, y, z);
            renderer.aoLightValueScratchYZNP = block.getAmbientOcclusionLightValue(world, x, y - 1, z);
            renderer.aoLightValueScratchYZPP = block.getAmbientOcclusionLightValue(world, x, y + 1, z);
            renderer.aoBrightnessXZNP = block.getMixedBrightnessForBlock(world, x - 1, y, z);
            renderer.aoBrightnessXZPP = block.getMixedBrightnessForBlock(world, x + 1, y, z);
            renderer.aoBrightnessYZNP = block.getMixedBrightnessForBlock(world, x, y - 1, z);
            renderer.aoBrightnessYZPP = block.getMixedBrightnessForBlock(world, x, y + 1, z);

            if (!renderer.aoGrassXYZNCP && !renderer.aoGrassXYZCNP)
            {
                renderer.aoLightValueScratchXYZNNP = renderer.aoLightValueScratchXZNP;
                renderer.aoBrightnessXYZNNP = renderer.aoBrightnessXZNP;
            }
            else
            {
                renderer.aoLightValueScratchXYZNNP = block.getAmbientOcclusionLightValue(world, x - 1, y - 1, z);
                renderer.aoBrightnessXYZNNP = block.getMixedBrightnessForBlock(world, x - 1, y - 1, z);
            }

            if (!renderer.aoGrassXYZNCP && !renderer.aoGrassXYZCPP)
            {
                renderer.aoLightValueScratchXYZNPP = renderer.aoLightValueScratchXZNP;
                renderer.aoBrightnessXYZNPP = renderer.aoBrightnessXZNP;
            }
            else
            {
                renderer.aoLightValueScratchXYZNPP = block.getAmbientOcclusionLightValue(world, x - 1, y + 1, z);
                renderer.aoBrightnessXYZNPP = block.getMixedBrightnessForBlock(world, x - 1, y + 1, z);
            }

            if (!renderer.aoGrassXYZPCP && !renderer.aoGrassXYZCNP)
            {
                renderer.aoLightValueScratchXYZPNP = renderer.aoLightValueScratchXZPP;
                renderer.aoBrightnessXYZPNP = renderer.aoBrightnessXZPP;
            }
            else
            {
                renderer.aoLightValueScratchXYZPNP = block.getAmbientOcclusionLightValue(world, x + 1, y - 1, z);
                renderer.aoBrightnessXYZPNP = block.getMixedBrightnessForBlock(world, x + 1, y - 1, z);
            }

            if (!renderer.aoGrassXYZPCP && !renderer.aoGrassXYZCPP)
            {
                renderer.aoLightValueScratchXYZPPP = renderer.aoLightValueScratchXZPP;
                renderer.aoBrightnessXYZPPP = renderer.aoBrightnessXZPP;
            }
            else
            {
                renderer.aoLightValueScratchXYZPPP = block.getAmbientOcclusionLightValue(world, x + 1, y + 1, z);
                renderer.aoBrightnessXYZPPP = block.getMixedBrightnessForBlock(world, x + 1, y + 1, z);
            }

            v1 = (renderer.aoLightValueScratchXZNP + renderer.aoLightValueScratchXYZNPP + renderer.aoLightValueZPos + renderer.aoLightValueScratchYZPP) / 4.0F;
            v4 = (renderer.aoLightValueZPos + renderer.aoLightValueScratchYZPP + renderer.aoLightValueScratchXZPP + renderer.aoLightValueScratchXYZPPP) / 4.0F;
            v3 = (renderer.aoLightValueScratchYZNP + renderer.aoLightValueZPos + renderer.aoLightValueScratchXYZPNP + renderer.aoLightValueScratchXZPP) / 4.0F;
            v2 = (renderer.aoLightValueScratchXYZNNP + renderer.aoLightValueScratchXZNP + renderer.aoLightValueScratchYZNP + renderer.aoLightValueZPos) / 4.0F;
            renderer.brightnessTopLeft = renderer.getAoBrightness(renderer.aoBrightnessXZNP, renderer.aoBrightnessXYZNPP, renderer.aoBrightnessYZPP, bZP);
            renderer.brightnessTopRight = renderer.getAoBrightness(renderer.aoBrightnessYZPP, renderer.aoBrightnessXZPP, renderer.aoBrightnessXYZPPP, bZP);
            renderer.brightnessBottomRight = renderer.getAoBrightness(renderer.aoBrightnessYZNP, renderer.aoBrightnessXYZPNP, renderer.aoBrightnessXZPP, bZP);
            renderer.brightnessBottomLeft = renderer.getAoBrightness(renderer.aoBrightnessXYZNNP, renderer.aoBrightnessXZNP, renderer.aoBrightnessYZNP, bZP);
        }
        else
        {
            v1 = v2 = v3 = v4 = renderer.aoLightValueZPos;
            renderer.brightnessTopLeft = renderer.brightnessBottomLeft = renderer.brightnessBottomRight = renderer.brightnessTopRight = bZP;
        }

        renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = coe.r * 0.8F;
        renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = coe.g * 0.8F;
        renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = coe.b * 0.8F;
        renderer.colorRedTopLeft *= v1;
        renderer.colorGreenTopLeft *= v1;
        renderer.colorBlueTopLeft *= v1;
        renderer.colorRedBottomLeft *= v2;
        renderer.colorGreenBottomLeft *= v2;
        renderer.colorBlueBottomLeft *= v2;
        renderer.colorRedBottomRight *= v3;
        renderer.colorGreenBottomRight *= v3;
        renderer.colorBlueBottomRight *= v3;
        renderer.colorRedTopRight *= v4;
        renderer.colorGreenTopRight *= v4;
        renderer.colorBlueTopRight *= v4;
	}
	
	public static void aoZN(RenderBlocks renderer, IBlockAccess world, Block block, int x, int y, int z, int bZN, color coe)
	{
		float v1, v2, v3, v4;
		if(renderer.aoType > 0)
		{
	        renderer.aoLightValueScratchXZNN = block.getAmbientOcclusionLightValue(world, x - 1, y, z);
	        renderer.aoLightValueScratchYZNN = block.getAmbientOcclusionLightValue(world, x, y - 1, z);
	        renderer.aoLightValueScratchYZPN = block.getAmbientOcclusionLightValue(world, x, y + 1, z);
	        renderer.aoLightValueScratchXZPN = block.getAmbientOcclusionLightValue(world, x + 1, y, z);
	        renderer.aoBrightnessXZNN = block.getMixedBrightnessForBlock(world, x - 1, y, z);
	        renderer.aoBrightnessYZNN = block.getMixedBrightnessForBlock(world, x, y - 1, z);
	        renderer.aoBrightnessYZPN = block.getMixedBrightnessForBlock(world, x, y + 1, z);
	        renderer.aoBrightnessXZPN = block.getMixedBrightnessForBlock(world, x + 1, y, z);
	
	        if (!renderer.aoGrassXYZNCN && !renderer.aoGrassXYZCNN)
	        {
	            renderer.aoLightValueScratchXYZNNN = renderer.aoLightValueScratchXZNN;
	            renderer.aoBrightnessXYZNNN = renderer.aoBrightnessXZNN;
	        }
	        else
	        {
	            renderer.aoLightValueScratchXYZNNN = block.getAmbientOcclusionLightValue(world, x - 1, y - 1, z);
	            renderer.aoBrightnessXYZNNN = block.getMixedBrightnessForBlock(world, x - 1, y - 1, z);
	        }
	
	        if (!renderer.aoGrassXYZNCN && !renderer.aoGrassXYZCPN)
	        {
	            renderer.aoLightValueScratchXYZNPN = renderer.aoLightValueScratchXZNN;
	            renderer.aoBrightnessXYZNPN = renderer.aoBrightnessXZNN;
	        }
	        else
	        {
	            renderer.aoLightValueScratchXYZNPN = block.getAmbientOcclusionLightValue(world, x - 1, y + 1, z);
	            renderer.aoBrightnessXYZNPN = block.getMixedBrightnessForBlock(world, x - 1, y + 1, z);
	        }
	
	        if (!renderer.aoGrassXYZPCN && !renderer.aoGrassXYZCNN)
	        {
	            renderer.aoLightValueScratchXYZPNN = renderer.aoLightValueScratchXZPN;
	            renderer.aoBrightnessXYZPNN = renderer.aoBrightnessXZPN;
	        }
	        else
	        {
	            renderer.aoLightValueScratchXYZPNN = block.getAmbientOcclusionLightValue(world, x + 1, y - 1, z);
	            renderer.aoBrightnessXYZPNN = block.getMixedBrightnessForBlock(world, x + 1, y - 1, z);
	        }
	
	        if (!renderer.aoGrassXYZPCN && !renderer.aoGrassXYZCPN)
	        {
	            renderer.aoLightValueScratchXYZPPN = renderer.aoLightValueScratchXZPN;
	            renderer.aoBrightnessXYZPPN = renderer.aoBrightnessXZPN;
	        }
	        else
	        {
	            renderer.aoLightValueScratchXYZPPN = block.getAmbientOcclusionLightValue(world, x + 1, y + 1, z);
	            renderer.aoBrightnessXYZPPN = block.getMixedBrightnessForBlock(world, x + 1, y + 1, z);
	        }
	
	        v1 = (renderer.aoLightValueScratchXZNN + renderer.aoLightValueScratchXYZNPN + renderer.aoLightValueZNeg + renderer.aoLightValueScratchYZPN) / 4.0F;
	        v2 = (renderer.aoLightValueZNeg + renderer.aoLightValueScratchYZPN + renderer.aoLightValueScratchXZPN + renderer.aoLightValueScratchXYZPPN) / 4.0F;
	        v3 = (renderer.aoLightValueScratchYZNN + renderer.aoLightValueZNeg + renderer.aoLightValueScratchXYZPNN + renderer.aoLightValueScratchXZPN) / 4.0F;
	        v4 = (renderer.aoLightValueScratchXYZNNN + renderer.aoLightValueScratchXZNN + renderer.aoLightValueScratchYZNN + renderer.aoLightValueZNeg) / 4.0F;
	        renderer.brightnessTopLeft = renderer.getAoBrightness(renderer.aoBrightnessXZNN, renderer.aoBrightnessXYZNPN, renderer.aoBrightnessYZPN, bZN);
	        renderer.brightnessBottomLeft = renderer.getAoBrightness(renderer.aoBrightnessYZPN, renderer.aoBrightnessXZPN, renderer.aoBrightnessXYZPPN, bZN);
	        renderer.brightnessBottomRight = renderer.getAoBrightness(renderer.aoBrightnessYZNN, renderer.aoBrightnessXYZPNN, renderer.aoBrightnessXZPN, bZN);
	        renderer.brightnessTopRight = renderer.getAoBrightness(renderer.aoBrightnessXYZNNN, renderer.aoBrightnessXZNN, renderer.aoBrightnessYZNN, bZN);
	    }
	    else
	    {
	        v4 = renderer.aoLightValueZNeg;
	        v3 = renderer.aoLightValueZNeg;
	        v2 = renderer.aoLightValueZNeg;
	        v1 = renderer.aoLightValueZNeg;
	        renderer.brightnessTopLeft = renderer.brightnessBottomLeft = renderer.brightnessBottomRight = renderer.brightnessTopRight = bZN;
	    }
	
	    renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = coe.r * 0.8F;
	    renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = coe.g * 0.8F;
	    renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = coe.b * 0.8F;
	    renderer.colorRedTopLeft *= v1;
	    renderer.colorGreenTopLeft *= v1;
	    renderer.colorBlueTopLeft *= v1;
	    renderer.colorRedBottomLeft *= v2;
	    renderer.colorGreenBottomLeft *= v2;
	    renderer.colorBlueBottomLeft *= v2;
	    renderer.colorRedBottomRight *= v3;
	    renderer.colorGreenBottomRight *= v3;
	    renderer.colorBlueBottomRight *= v3;
	    renderer.colorRedTopRight *= v4;
	    renderer.colorGreenTopRight *= v4;
	    renderer.colorBlueTopRight *= v4;
	}
	
	public static void aoYP(RenderBlocks renderer, IBlockAccess world, Block block, int x, int y, int z, int bYP, color coe)
	{
		float v1, v2, v3, v4;
		if(renderer.aoType > 0)
		{
            renderer.aoBrightnessXYNP = block.getMixedBrightnessForBlock(world, x - 1, y, z);
            renderer.aoBrightnessXYPP = block.getMixedBrightnessForBlock(world, x + 1, y, z);
            renderer.aoBrightnessYZPN = block.getMixedBrightnessForBlock(world, x, y, z - 1);
            renderer.aoBrightnessYZPP = block.getMixedBrightnessForBlock(world, x, y, z + 1);
            renderer.aoLightValueScratchXYNP = block.getAmbientOcclusionLightValue(world, x - 1, y, z);
            renderer.aoLightValueScratchXYPP = block.getAmbientOcclusionLightValue(world, x + 1, y, z);
            renderer.aoLightValueScratchYZPN = block.getAmbientOcclusionLightValue(world, x, y, z - 1);
            renderer.aoLightValueScratchYZPP = block.getAmbientOcclusionLightValue(world, x, y, z + 1);

            if (!renderer.aoGrassXYZCPN && !renderer.aoGrassXYZNPC)
            {
                renderer.aoLightValueScratchXYZNPN = renderer.aoLightValueScratchXYNP;
                renderer.aoBrightnessXYZNPN = renderer.aoBrightnessXYNP;
            }
            else
            {
                renderer.aoLightValueScratchXYZNPN = block.getAmbientOcclusionLightValue(world, x - 1, y, z - 1);
                renderer.aoBrightnessXYZNPN = block.getMixedBrightnessForBlock(world, x - 1, y, z - 1);
            }

            if (!renderer.aoGrassXYZCPN && !renderer.aoGrassXYZPPC)
            {
                renderer.aoLightValueScratchXYZPPN = renderer.aoLightValueScratchXYPP;
                renderer.aoBrightnessXYZPPN = renderer.aoBrightnessXYPP;
            }
            else
            {
                renderer.aoLightValueScratchXYZPPN = block.getAmbientOcclusionLightValue(world, x + 1, y, z - 1);
                renderer.aoBrightnessXYZPPN = block.getMixedBrightnessForBlock(world, x + 1, y, z - 1);
            }

            if (!renderer.aoGrassXYZCPP && !renderer.aoGrassXYZNPC)
            {
                renderer.aoLightValueScratchXYZNPP = renderer.aoLightValueScratchXYNP;
                renderer.aoBrightnessXYZNPP = renderer.aoBrightnessXYNP;
            }
            else
            {
                renderer.aoLightValueScratchXYZNPP = block.getAmbientOcclusionLightValue(world, x - 1, y, z + 1);
                renderer.aoBrightnessXYZNPP = block.getMixedBrightnessForBlock(world, x - 1, y, z + 1);
            }

            if (!renderer.aoGrassXYZCPP && !renderer.aoGrassXYZPPC)
            {
                renderer.aoLightValueScratchXYZPPP = renderer.aoLightValueScratchXYPP;
                renderer.aoBrightnessXYZPPP = renderer.aoBrightnessXYPP;
            }
            else
            {
                renderer.aoLightValueScratchXYZPPP = block.getAmbientOcclusionLightValue(world, x + 1, y, z + 1);
                renderer.aoBrightnessXYZPPP = block.getMixedBrightnessForBlock(world, x + 1, y, z + 1);
            }

            v4 = (renderer.aoLightValueScratchXYZNPP + renderer.aoLightValueScratchXYNP + renderer.aoLightValueScratchYZPP + renderer.aoLightValueYPos) / 4.0F;
            v1 = (renderer.aoLightValueScratchYZPP + renderer.aoLightValueYPos + renderer.aoLightValueScratchXYZPPP + renderer.aoLightValueScratchXYPP) / 4.0F;
            v2 = (renderer.aoLightValueYPos + renderer.aoLightValueScratchYZPN + renderer.aoLightValueScratchXYPP + renderer.aoLightValueScratchXYZPPN) / 4.0F;
            v3 = (renderer.aoLightValueScratchXYNP + renderer.aoLightValueScratchXYZNPN + renderer.aoLightValueYPos + renderer.aoLightValueScratchYZPN) / 4.0F;
            renderer.brightnessTopRight = renderer.getAoBrightness(renderer.aoBrightnessXYZNPP, renderer.aoBrightnessXYNP, renderer.aoBrightnessYZPP, bYP);
            renderer.brightnessTopLeft = renderer.getAoBrightness(renderer.aoBrightnessYZPP, renderer.aoBrightnessXYZPPP, renderer.aoBrightnessXYPP, bYP);
            renderer.brightnessBottomLeft = renderer.getAoBrightness(renderer.aoBrightnessYZPN, renderer.aoBrightnessXYPP, renderer.aoBrightnessXYZPPN, bYP);
            renderer.brightnessBottomRight = renderer.getAoBrightness(renderer.aoBrightnessXYNP, renderer.aoBrightnessXYZNPN, renderer.aoBrightnessYZPN, bYP);
        }
        else
        {
            v1 = v2 = v3 = v4 = renderer.aoLightValueYPos;
            renderer.brightnessTopLeft = renderer.brightnessBottomLeft = renderer.brightnessBottomRight = renderer.brightnessTopRight = bYP;
        }

        renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = coe.r;
        renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = coe.g;
        renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = coe.b;
        renderer.colorRedTopLeft *= v1;
        renderer.colorGreenTopLeft *= v1;
        renderer.colorBlueTopLeft *= v1;
        renderer.colorRedBottomLeft *= v2;
        renderer.colorGreenBottomLeft *= v2;
        renderer.colorBlueBottomLeft *= v2;
        renderer.colorRedBottomRight *= v3;
        renderer.colorGreenBottomRight *= v3;
        renderer.colorBlueBottomRight *= v3;
        renderer.colorRedTopRight *= v4;
        renderer.colorGreenTopRight *= v4;
        renderer.colorBlueTopRight *= v4;
	}
	
	public static void aoYN(RenderBlocks renderer, IBlockAccess world, Block block, int x, int y, int z, int bYN, color coe)
	{
		float v1, v2, v3, v4;
		if(renderer.aoType > 0)
		{
	        renderer.aoBrightnessXYNN = block.getMixedBrightnessForBlock(world, x - 1, y, z);
	        renderer.aoBrightnessYZNN = block.getMixedBrightnessForBlock(world, x, y, z - 1);
	        renderer.aoBrightnessYZNP = block.getMixedBrightnessForBlock(world, x, y, z + 1);
	        renderer.aoBrightnessXYPN = block.getMixedBrightnessForBlock(world, x + 1, y, z);
	        renderer.aoLightValueScratchXYNN = block.getAmbientOcclusionLightValue(world, x - 1, y, z);
	        renderer.aoLightValueScratchYZNN = block.getAmbientOcclusionLightValue(world, x, y, z - 1);
	        renderer.aoLightValueScratchYZNP = block.getAmbientOcclusionLightValue(world, x, y, z + 1);
	        renderer.aoLightValueScratchXYPN = block.getAmbientOcclusionLightValue(world, x + 1, y, z);
	        
	        if (!renderer.aoGrassXYZCNN && !renderer.aoGrassXYZNNC)
	        {
	        	renderer.aoLightValueScratchXYZNNN = renderer.aoLightValueScratchXYNN;
	            renderer.aoBrightnessXYZNNN = renderer.aoBrightnessXYNN;
	        }
	        else
	        {
	        	renderer.aoLightValueScratchXYZNNN = block.getAmbientOcclusionLightValue(world, x - 1, y, z - 1);
	        	renderer.aoBrightnessXYZNNN = block.getMixedBrightnessForBlock(world, x - 1, y, z - 1);
	        }
	
	        if (!renderer.aoGrassXYZCNP && !renderer.aoGrassXYZNNC)
	        {
	            renderer.aoLightValueScratchXYZNNP = renderer.aoLightValueScratchXYNN;
	            renderer.aoBrightnessXYZNNP = renderer.aoBrightnessXYNN;
	        }
	        else
	        {
	            renderer.aoLightValueScratchXYZNNP = block.getAmbientOcclusionLightValue(world, x - 1, y, z + 1);
	            renderer.aoBrightnessXYZNNP = block.getMixedBrightnessForBlock(world, x - 1, y, z + 1);
	        }
	
	        if (!renderer.aoGrassXYZCNN && !renderer.aoGrassXYZPNC)
	        {
	            renderer.aoLightValueScratchXYZPNN = renderer.aoLightValueScratchXYPN;
	            renderer.aoBrightnessXYZPNN = renderer.aoBrightnessXYPN;
	        }
	        else
	        {
	            renderer.aoLightValueScratchXYZPNN = block.getAmbientOcclusionLightValue(world, x + 1, y, z - 1);
	            renderer.aoBrightnessXYZPNN = block.getMixedBrightnessForBlock(world, x + 1, y, z - 1);
	        }
	
	        if (!renderer.aoGrassXYZCNP && !renderer.aoGrassXYZPNC)
	        {
	            renderer.aoLightValueScratchXYZPNP = renderer.aoLightValueScratchXYPN;
	            renderer.aoBrightnessXYZPNP = renderer.aoBrightnessXYPN;
	        }
	        else
	        {
	            renderer.aoLightValueScratchXYZPNP = block.getAmbientOcclusionLightValue(world, x + 1, y, z + 1);
	            renderer.aoBrightnessXYZPNP = block.getMixedBrightnessForBlock(world, x + 1, y, z + 1);
	        }
            v1 = (renderer.aoLightValueScratchXYZNNP + renderer.aoLightValueScratchXYNN + renderer.aoLightValueScratchYZNP + renderer.aoLightValueYNeg) / 4.0F;
            v2 = (renderer.aoLightValueScratchYZNP + renderer.aoLightValueYNeg + renderer.aoLightValueScratchXYZPNP + renderer.aoLightValueScratchXYPN) / 4.0F;
            v3 = (renderer.aoLightValueYNeg + renderer.aoLightValueScratchYZNN + renderer.aoLightValueScratchXYPN + renderer.aoLightValueScratchXYZPNN) / 4.0F;
            v4 = (renderer.aoLightValueScratchXYNN + renderer.aoLightValueScratchXYZNNN + renderer.aoLightValueYNeg + renderer.aoLightValueScratchYZNN) / 4.0F;
            
            renderer.brightnessTopLeft = renderer.getAoBrightness(renderer.aoBrightnessXYZNNP, renderer.aoBrightnessXYNN, renderer.aoBrightnessYZNP, bYN);
            renderer.brightnessTopRight = renderer.getAoBrightness(renderer.aoBrightnessYZNP, renderer.aoBrightnessXYZPNP, renderer.aoBrightnessXYPN, bYN);
            renderer.brightnessBottomRight = renderer.getAoBrightness(renderer.aoBrightnessYZNN, renderer.aoBrightnessXYPN, renderer.aoBrightnessXYZPNN, bYN);
            renderer.brightnessBottomLeft = renderer.getAoBrightness(renderer.aoBrightnessXYNN, renderer.aoBrightnessXYZNNN, renderer.aoBrightnessYZNN, bYN);
		}
		else
		{
            v1 = v2 = v3 = v4 = renderer.aoLightValueYNeg;
            renderer.brightnessTopLeft = renderer.brightnessBottomLeft = renderer.brightnessBottomRight = renderer.brightnessTopRight = renderer.aoBrightnessXYNN;
		}

        renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = coe.r * 0.5F;
        renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = coe.g * 0.5F;
        renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = coe.b * 0.5F;
        renderer.colorRedTopLeft *= v1;
        renderer.colorGreenTopLeft *= v1;
        renderer.colorBlueTopLeft *= v1;
        renderer.colorRedBottomLeft *= v2;
        renderer.colorGreenBottomLeft *= v2;
        renderer.colorBlueBottomLeft *= v2;
        renderer.colorRedBottomRight *= v3;
        renderer.colorGreenBottomRight *= v3;
        renderer.colorBlueBottomRight *= v3;
        renderer.colorRedTopRight *= v4;
        renderer.colorGreenTopRight *= v4;
        renderer.colorBlueTopRight *= v4;
	}
}
