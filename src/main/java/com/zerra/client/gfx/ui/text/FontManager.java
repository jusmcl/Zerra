package com.zerra.client.gfx.ui.text;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.zerra.client.ZerraClient;
import com.zerra.client.gfx.renderer.GuiRenderer;
import com.zerra.client.gfx.shader.TextShader;
import com.zerra.client.util.Loader;
import com.zerra.client.util.ResourceLocation;

/**
 * <em><b>Copyright (c) 2019 The Zerra Team.</b></em> <br>
 * </br>
 * Used in managing fonts.
 *
 * @author AndrewAlfazy
 */
public class FontManager
{
	public static final ResourceLocation DEFAULT_FONT_LOCATION = new ResourceLocation("textures/gui/fonts/default.fnt");

	private TextShader shader;

	private final Map<ResourceLocation, Font> fonts;

	public FontManager()
	{
		this.fonts = new HashMap<ResourceLocation, Font>();
		this.shader = new TextShader();
		this.shader.start();
		this.shader.loadProjectionMatrix(GuiRenderer.DEFAULT_MATRIX);
		this.shader.stop();
		this.loadFont(DEFAULT_FONT_LOCATION);
	}

	public boolean loadFont(ResourceLocation locationFont)
	{
		if (!fonts.containsKey(locationFont))
		{
			try (BufferedReader bis = new BufferedReader(new InputStreamReader(locationFont.getInputStream())))
			{
				Font font = null;

				ResourceLocation locationTexture = null;
				String name = null;
				float size = 0;
				float lineHeight = 0;
				float base = 0;
				float textureWidth = 0;
				float textureHeight = 0;

				String line, parts[];
				while ((line = bis.readLine()) != null)
				{
					parts = line.replace("  ", " ").replace("  ", " ").replace("  ", " ").split(" ");
					switch (parts[0])
					{
					case "info":
						name = parts[1].replace("face=", "").replace("\"", "");
						size = Float.parseFloat(parts[2].replace("size=", ""));
						break;
					case "common":
						lineHeight = Float.parseFloat(parts[1].replace("lineHeight=", ""));
						base = Float.parseFloat(parts[2].replace("base=", ""));
						textureWidth = Float.parseFloat(parts[3].replace("scaleW=", ""));
						textureHeight = Float.parseFloat(parts[4].replace("scaleH=", ""));
						System.err.println(base);
						break;
					case "page":
						locationTexture = new ResourceLocation(locationFont.getLocation().replace(new File(locationFont.getPath()).getName(), parts[2].replace("file=", "").replace("\"", "")));
						break;
					case "chars":
						font = new Font(Integer.parseInt(parts[1].replace("count=", "")), locationTexture, name, size, lineHeight, base);
						break;
					case "char":
						font.chars.put((char) Float.parseFloat(parts[1].replace("id=", "")), new Char(Float.parseFloat(parts[2].replace("x=", "")), Float.parseFloat(parts[3].replace("y=", "")), Float.parseFloat(parts[4].replace("width=", "")), Float.parseFloat(parts[5].replace("height=", "")), Float.parseFloat(parts[6].replace("xoffset=", "")), Float.parseFloat(parts[7].replace("yoffset=", "")), Float.parseFloat(parts[8].replace("xadvance=", "")), textureWidth, textureHeight));
						break;
					}
				}
				fonts.put(locationFont, font);
				return true;
			}
			catch (IOException e)
			{
				ZerraClient.logger().error("Can't load the file " + locationFont.getPath() + " Will use fallback (default) font insted");
				ZerraClient.logger().catching(e);
			}
		}
		return false;
	}

	public Font getFont(ResourceLocation locationFont)
	{
		return fonts.containsKey(locationFont) ? fonts.get(locationFont) : loadFont(locationFont) ? fonts.get(locationFont) : fonts.get(DEFAULT_FONT_LOCATION);
	}

	public TextMesh generateTextMesh(String text, float size, ResourceLocation locationFont, boolean dynamicText)
	{
		TextMesh mesh = new TextMesh(Loader.generateVertexArray(), Loader.generateVBO(), Loader.generateVBO());
		GL30.glBindVertexArray(mesh.vao);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, mesh.vboVertices);
		GL20.glVertexAttribPointer(0, 2, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, mesh.vboVertices);
		GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 0, 0);
		GL30.glBindVertexArray(0);
		updateTextMesh(mesh, text, size, locationFont, dynamicText);
		return mesh;
	}

	public void updateTextMesh(TextMesh mesh, String text, float size, ResourceLocation locationFont, boolean dynamicText)
	{
		Font font = fonts.get(locationFont);
		float scaller = size / font.size;
		float maxX = 0;
		float maxY = 0;

		{// Don't remove this extra brackets will be used later -AndrewAlfazy.
			float x = 0;
			float y = 0;
			char[] chars = text.toCharArray();
			float[] vertices = new float[chars.length * 6 * 2];
			float[] texcoords = new float[chars.length * 6 * 2];
			mesh.verticesCount += chars.length * 6;
			for (int i = 0; i < chars.length; i++)
			{
				vertices[i * 12 + 0] = x + font.chars.get(chars[i]).xoffset * scaller;
				vertices[i * 12 + 1] = y + font.chars.get(chars[i]).yoffset * scaller;

				texcoords[i * 12 + 0] = font.chars.get(chars[i]).minTexcoordX;
				texcoords[i * 12 + 1] = font.chars.get(chars[i]).minTexcoordY;

				vertices[i * 12 + 2] = x + font.chars.get(chars[i]).xoffset * scaller;
				vertices[i * 12 + 3] = y + font.chars.get(chars[i]).height * scaller + font.chars.get(chars[i]).yoffset * scaller;

				texcoords[i * 12 + 2] = font.chars.get(chars[i]).minTexcoordX;
				texcoords[i * 12 + 3] = font.chars.get(chars[i]).maxTexcoordY;

				vertices[i * 12 + 4] = x + font.chars.get(chars[i]).width * scaller + font.chars.get(chars[i]).xoffset * scaller;
				vertices[i * 12 + 5] = y + font.chars.get(chars[i]).yoffset * scaller;

				texcoords[i * 12 + 4] = font.chars.get(chars[i]).maxTexcoordX;
				texcoords[i * 12 + 5] = font.chars.get(chars[i]).minTexcoordY;

				vertices[i * 12 + 6] = x + font.chars.get(chars[i]).xoffset * scaller;
				vertices[i * 12 + 7] = y + font.chars.get(chars[i]).height * scaller + font.chars.get(chars[i]).yoffset * scaller;

				texcoords[i * 12 + 6] = font.chars.get(chars[i]).minTexcoordX;
				texcoords[i * 12 + 7] = font.chars.get(chars[i]).maxTexcoordY;

				vertices[i * 12 + 8] = x + font.chars.get(chars[i]).width * scaller + font.chars.get(chars[i]).xoffset * scaller;
				vertices[i * 12 + 9] = y + font.chars.get(chars[i]).height * scaller + font.chars.get(chars[i]).yoffset * scaller;

				texcoords[i * 12 + 8] = font.chars.get(chars[i]).maxTexcoordX;
				texcoords[i * 12 + 9] = font.chars.get(chars[i]).maxTexcoordY;

				vertices[i * 12 + 10] = x + font.chars.get(chars[i]).width * scaller + font.chars.get(chars[i]).xoffset * scaller;
				vertices[i * 12 + 11] = y + font.chars.get(chars[i]).yoffset * scaller;

				texcoords[i * 12 + 10] = font.chars.get(chars[i]).maxTexcoordX;
				texcoords[i * 12 + 11] = font.chars.get(chars[i]).minTexcoordY;

				x += font.chars.get(chars[i]).xadvance;
			}

			maxX = x > maxX ? x : maxX;
			maxY = y > maxY ? y : maxY;

			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, mesh.vboVertices);
			GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices, dynamicText ? GL15.GL_DYNAMIC_DRAW : GL15.GL_STATIC_DRAW);
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, mesh.vboTexcoords);
			GL15.glBufferData(GL15.GL_ARRAY_BUFFER, texcoords, dynamicText ? GL15.GL_DYNAMIC_DRAW : GL15.GL_STATIC_DRAW);
		} // Don't remove this extra brackets will be used later -AndrewAlfazy.

		mesh.width = maxX;
		mesh.height = maxY;

		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}

	public void render(TextMesh mesh)
	{
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		shader.start();
		GL30.glBindVertexArray(mesh.vao);
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL30.glDrawArrays(GL11.GL_TRIANGLES, 0, mesh.verticesCount);
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);
		shader.stop();
		GL11.glDisable(GL11.GL_BLEND);
	}

}
