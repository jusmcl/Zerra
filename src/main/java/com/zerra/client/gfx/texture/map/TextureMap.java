package com.zerra.client.gfx.texture.map;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import org.joml.Vector2i;
import org.lwjgl.opengl.GL11;

import com.zerra.client.Zerra;
import com.zerra.client.gfx.texture.ITexture;
import com.zerra.client.gfx.texture.TextureManager;
import com.zerra.client.util.LoadingUtils;
import com.zerra.client.util.ResourceLocation;

public class TextureMap implements ITexture {

	private ResourceLocation location;
	private int textureId;
	private int width;
	private int height;
	private List<ResourceLocation> spriteLocations;

	public TextureMap(ResourceLocation location, TextureManager textureManager) {
		this.location = location;
		this.textureId = GL11.glGenTextures();
		this.width = 128;
		this.height = 128;
		this.spriteLocations = new ArrayList<ResourceLocation>();
		textureManager.loadTexture(location, this);
	}

	public void dispose() {
		this.spriteLocations.clear();
	}

	public void stitch() {
		int startingSize = 128;
		Map<Vector2i, List<BufferedImage>> imageBatch = new HashMap<Vector2i, List<BufferedImage>>();
		Map<ResourceLocation, BufferedImage> foundImages = new HashMap<ResourceLocation, BufferedImage>();
		this.addImage(LoadingUtils.createMissingImage(16, 16), imageBatch);

		long startTime = System.currentTimeMillis();
		Zerra.logger().info("Stitching " + this.spriteLocations.size() + " into atlas \'" + this.location.toString() + "\'");
		for (ResourceLocation location : this.spriteLocations) {
			try {
				BufferedImage image = ImageIO.read(location.getInputStream());
				this.addImage(image, imageBatch);
				foundImages.put(location, image);
				if (image.getWidth() > startingSize)
					startingSize = image.getWidth();
				if (image.getHeight() > startingSize)
					startingSize = image.getHeight();
			} catch (Exception e) {
				Zerra.logger().warn("Could not find atlas sprite at \'" + location + "\'. Skipping!");
			}
		}

		List<Vector2i> orderedSizes = imageBatch.keySet().stream().sorted((o1, o2) -> {
			return o2.x * o2.y - o1.x * o1.y;
		}).collect(Collectors.toList());

		int startingExponent = (int) Math.ceil(Math.log(startingSize) / Math.log(2));
		int currentWidth = (int) Math.pow(2, startingExponent + 1);
		int currentHeight = (int) Math.pow(2, startingExponent + 1);
		int xStart = 0;
		int yStart = 0;
		int currentY = 0;
		int xPointer = 0;
		int yPointer = 0;
		Vector2i previousSize = new Vector2i();
		BufferedImage atlas = new BufferedImage(currentWidth, currentHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = atlas.createGraphics();

		for (Vector2i size : orderedSizes) {
			List<BufferedImage> images = imageBatch.get(size);
			if (images != null) {
				for (BufferedImage image : images) {
					if (xPointer + image.getWidth() > currentWidth) {
						yPointer = yStart;
						xPointer = xStart;
						currentY = yStart;
						yStart = 0;
					} else {
						if (yStart == 0 || image.getHeight() < yStart) {
							yStart = currentY + image.getHeight();
						}
						if (previousSize.y > image.getHeight()) {
							xStart = xPointer;
						}
					}
					if (yPointer + image.getHeight() > currentHeight) {
						xStart = (int) Math.pow(2, startingExponent + 1);
						xPointer = xStart;
						yPointer = 0;
						yStart = 0;
						currentY = 0;
						startingExponent++;
						currentWidth = (int) Math.pow(2, startingExponent + 1);
						currentHeight = (int) Math.pow(2, startingExponent + 1);
						BufferedImage newImage = new BufferedImage(currentWidth, currentHeight, BufferedImage.TYPE_INT_ARGB);
						for (int y = 0; y < atlas.getHeight(); y++) {
							for (int x = 0; x < atlas.getWidth(); x++) {
								newImage.setRGB(x, y, atlas.getRGB(x, y));
							}
						}
						atlas = newImage;
						g.dispose();
						g = atlas.createGraphics();
					}
					g.drawImage(image, xPointer, yPointer, null);
					xPointer += image.getWidth();
					previousSize.x = image.getWidth();
					previousSize.y = image.getHeight();
				}
			}
		}
		g.dispose();
		
		Zerra.logger().info("Created " + atlas.getWidth() + "x" + atlas.getHeight() + " atlas in " + (System.currentTimeMillis() - startTime) / 1000.0 + " seconds");
		try {
			File folder = new File("debug/atlas");
			if (!folder.exists()) {
				folder.mkdirs();
			}
			File file = new File(folder, this.location.getDomain() + "-" + this.location.getLocation() + ".png");
			ImageIO.write(atlas, "PNG", new FileOutputStream(file));
		} catch (Exception e) {
			e.printStackTrace();
		}

		Zerra.getInstance().stop();
	}

	private void addImage(BufferedImage image, Map<Vector2i, List<BufferedImage>> imageBatch) {
		Vector2i size = new Vector2i(image.getWidth(), image.getHeight());
		List<BufferedImage> list = imageBatch.get(size);
		if (list == null) {
			list = new ArrayList<BufferedImage>();
			imageBatch.put(size, list);
		}
		list.add(image);
	}

	public void register(ResourceLocation location) {
		this.spriteLocations.add(location);
	}

	public ResourceLocation getLocation() {
		return location;
	}

	@Override
	public int getId() {
		return textureId;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}
}