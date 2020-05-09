package com.game;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Sprite {
	private String path;
	public int width, height;

	public int[][] pixels;

	public Sprite(String Path) {
		path = Path;
		try {
			BufferedImage image = ImageIO.read(Map.class.getResource(path));
			width = image.getWidth();
			height = image.getHeight();
			int[] pixelsRGB = new int[width * height];
			pixels = new int[width][height];
			image.getRGB(0, 0, width, height, pixelsRGB, 0, width);
		
			for(int x =0; x<width; x++){
				for(int y =0; y<height;y++){
					pixels[x][y] = pixelsRGB[y*width + x];
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
}

