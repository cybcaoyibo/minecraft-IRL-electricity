package cyb.electricity;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.client.ForgeHooksClient;

public class common_ce
{
	static void draw_meter(int x, int y, double scale, double z, FontRenderer fr, String type, double min, double max, double cur, String minstr, String midstr, String maxstr)
	{
		Tessellator tes = Tessellator.instance;
		GL11.glColor4f(1, 1, 1, 1);
		ForgeHooksClient.bindTexture(cyb2.img, 0);
		double a_beg = Math.PI / 6.;
		double a_end = 5 * Math.PI / 6.;
		tes.startDrawingQuads();
		for(int i = 0; i < 24; i++)
		{
			double a_now = a_beg + (a_end - a_beg) / 24. * i;
			double a_next = a_beg + (a_end - a_beg) / 24. * (i + 1);
			double x1 = -Math.cos(a_now) * scale + x;
			double y1 = -Math.sin(a_now) * scale + y;
			double x2 = -Math.cos(a_now) * scale / 2. + x;
			double y2 = -Math.sin(a_now) * scale / 2. + y;
			double x3 = -Math.cos(a_next) * scale / 2. + x;
			double y3 = -Math.sin(a_next) * scale / 2. + y;
			double x4 = -Math.cos(a_next) * scale + x;
			double y4 = -Math.sin(a_next) * scale + y;
			tes.addVertexWithUV(x1, y1, z, 16. / 256., 0);
			tes.addVertexWithUV(x2, y2, z, 32. / 256., 0);
			tes.addVertexWithUV(x3, y3, z, 32. / 256., 1. / 256.);
			tes.addVertexWithUV(x4, y4, z, 16. / 256., 1. / 256.);
		}
		tes.draw();
		double tx = x - fr.getStringWidth(type) / 2.;
		double ty = y - scale / 6.;
		fr.drawString(type, (int)tx, (int)ty, 0xFF1111);
		tx = x - Math.cos(a_beg) * scale - fr.getStringWidth(minstr) / 2. + scale / 3.;
		ty = y - Math.sin(a_beg) * scale;
		fr.drawString(minstr, (int)tx, (int)ty, 0x111111);
		tx = x - Math.cos(a_end) * scale - fr.getStringWidth(maxstr) / 2. - scale / 3.;
		ty = y - Math.sin(a_end) * scale;
		fr.drawString(maxstr, (int)tx, (int)ty, 0x111111);
		tx = x - fr.getStringWidth(midstr) / 2.;
		ty = y - scale + scale / 4.;
		fr.drawString(midstr, (int)tx, (int)ty, 0x111111);
		GL11.glColor4f(0, 0, 0, 1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		for(int i = 0; i <= 24; i++)
		{
			tes.startDrawing(GL11.GL_LINES);
			double a_now = a_beg + (a_end - a_beg) / 24. * i;
			double x1 = -Math.cos(a_now) * scale + x;
			double y1 = -Math.sin(a_now) * scale + y;
			double sp = 6. / 7.;
			if(i == 0 || i == 12 || i == 24)
			{
				sp = 3. / 4.;
				GL11.glLineWidth(2);
			}
			else
				GL11.glLineWidth(1);
			double x2 = -Math.cos(a_now) * scale * sp + x;
			double y2 = -Math.sin(a_now) * scale * sp + y;
			tes.addVertex(x1, y1, z);
			tes.addVertex(x2, y2, z);
			tes.draw();
		}
		GL11.glLineWidth(3);
		double ang = (cur - min) / (max - min) * (a_end - a_beg) + a_beg;
		if(ang < a_beg - Math.PI / 18.)
			ang = a_beg - Math.PI / 18.;
		else if(ang > a_end + Math.PI / 18.)
			ang = a_end + Math.PI / 18.;
		GL11.glColor4f(.8f, 0, 0, 1);
		tes.startDrawing(GL11.GL_LINES);
		tes.addVertex(x - Math.cos(ang) * scale * 1.1, y - Math.sin(ang) * scale * 1.1, z);
		tes.addVertex(x - Math.cos(ang) * scale * 0.3, y - Math.sin(ang) * scale * 0.3, z);
		tes.draw();
		GL11.glLineWidth(1);
	}
}
