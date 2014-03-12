package cyb.electricity;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.StringTranslate;
import net.minecraftforge.client.ForgeHooksClient;

public class gui_ce extends GuiContainer
{
	public tile_ce tile;
	public gui_ce(InventoryPlayer player, tile_ce te)
	{
		super(new container_ce(player, te));
		tile = te;
	}
	
	@Override
    public void drawGuiContainerForegroundLayer(int param1, int param2)
	{
	}

	@Override
	public void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
	{
		ForgeHooksClient.bindTexture("/gui/furnace.png", 0);
    	GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    	int x = (width - xSize) / 2;
    	int y = (height - ySize) / 2;
    	this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    	GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Tessellator tes = Tessellator.instance;
        tes.startDrawingQuads();
        tes.addVertexWithUV(x + 6, y + 76, zLevel, 6. / 256., 7. / 256.);
        tes.addVertexWithUV(x + 170, y + 76, zLevel, 7. / 256., 7. / 256.);
        tes.addVertexWithUV(x + 170, y + 6, zLevel, 7. / 256., 6. / 256.);
        tes.addVertexWithUV(x + 6, y + 6, zLevel, 6. / 256., 6. / 256.);
        tes.draw();
        if(tile.get_meta() == 1)
        {
        	this.fontRenderer.drawString(StringTranslate.getInstance().translateKey(iblk_ce.nm + "._1.name"), x + 6, y + 6, 0);
        	this.fontRenderer.drawString(" - CYB", x + 6 + this.fontRenderer.getStringWidth(StringTranslate.getInstance().translateKey(iblk_ce.nm + "._1.name")), y + 6, 0x555555);
        	GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        	ForgeHooksClient.bindTexture("/gui/furnace.png", 0);
        	this.drawTexturedModalRect(x + 13, y + 52, 55, 16, 18, 18);
        	this.drawTexturedModalRect(x + 15, y + 34, 57, 37, 13, 13);
        	if(tile.remain_burn_time > 0)
        	{
	        	int time_for_render = (int)(tile.remain_burn_time * 14 / tile.max_burn_time);
	        	this.drawTexturedModalRect(x + 15, y + 34 + 14 - time_for_render, 176, 14 - time_for_render, 14, time_for_render);
        	}
        	ForgeHooksClient.bindTexture(cyb2.img, 0);
        	this.drawTexturedModalRect(x + 50, y + 27, 0, 16, 18, 36);
        	if(tile.power > 0)
        	{
	        	int power_for_render = (int)(tile.power * 34. / tile.max_power);
	        	this.drawTexturedModalRect(x + 51, y + 28 + 34 - power_for_render, 18, 16 + 34 - power_for_render, 16, power_for_render);
        	}
        	this.fontRenderer.drawString("1000.0J", x + 50, y + 64, 0);
        	this.fontRenderer.drawString(Math.round(tile.power * 10.) / 10. + "J", x + 50, y + 28 - 10, 0);
        	common_ce.draw_meter(x + 121, y + 38, 30., zLevel, fontRenderer, "V", 0, 12, tile.voltage_for_render, "0", "6", "12");
        	common_ce.draw_meter(x + 121, y + 76, 30., zLevel, fontRenderer, "A", 0, 1, tile.generator_current, "0", "0.5", "1");
        }
        else if(tile.get_meta() == 3 || tile.get_meta() == 4)
        {
        	int meta = tile.get_meta();
        	this.fontRenderer.drawString(StringTranslate.getInstance().translateKey(iblk_ce.nm + "._" + meta + ".name"), x + 6, y + 6, 0);
        	this.fontRenderer.drawString(" - CYB", x + 6 + this.fontRenderer.getStringWidth(StringTranslate.getInstance().translateKey(iblk_ce.nm + "._" + meta + ".name")), y + 6, 0x555555);	
        	common_ce.draw_meter(x + xSize / 2, y + 70, 50., zLevel, fontRenderer, meta == 3 ? "A" : "V", 0, meta == 3 ? 1 : 12, tile.real_value, "0", meta == 3 ? "0.5" : "6", meta == 3 ? "1" : "12");
        	String str = Math.round(tile.real_value * 100.) / 100. + (meta == 3 ? "A" : "V");
        	this.fontRenderer.drawString(str, x + xSize / 2 - fontRenderer.getStringWidth(str) / 2, y + 70, 0);
        }
	}
}
