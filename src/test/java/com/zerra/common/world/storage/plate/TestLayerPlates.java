package com.zerra.common.world.storage.plate;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import org.joml.Vector2i;
import org.joml.Vector3i;

import com.zerra.common.world.tile.Tile;

public class TestLayerPlates {

	public static void main(String[] args) {
		try {
			test();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings({ "resource", "serial" })
	public static void test() throws IOException {

		LayerPlate layerPlates = new LayerPlate();
		Scanner scanner = new Scanner(System.in);
		System.out.println("please specify the x coordinate of the plate");
		int x = scanner.nextInt();
		System.out.println("please specify the y coordinate of the plate");
		int y = scanner.nextInt();
		System.out.println("please specify the z coordinate of the plate");
		int z = scanner.nextInt();

		Vector3i vector3i = new Vector3i(x, y, z);
		Plate plate = layerPlates.getPlate(vector3i);

		BufferedImage image = makeIMG(plate);
		File file;
		file = new File("./world.png");
		file.createNewFile();
		ImageIO.write(image, "png", file);
		JFrame frame = new JFrame() {
			@Override
			public void paint(Graphics g) {
				g.drawImage(image, 0, 0, (Image img, int infoflags, int x, int y, int width, int height) -> infoflags != ImageObserver.ERROR);
			}
		};
		frame.setVisible(true);
	}

	private static BufferedImage makeIMG(Plate plate) {
		BufferedImage bufferedImage = new BufferedImage(Plate.SIZE, Plate.SIZE, TYPE_INT_RGB);
		for (int x = 0; x < Plate.SIZE; x++) {
			for (int z = 0; z < Plate.SIZE; z++) {
				Tile tile = plate.getTileAt(new Vector2i(x, z));
				bufferedImage.setRGB(x, z, tile.getColor());
			}
		}
		return bufferedImage;
	}

}
