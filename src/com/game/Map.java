package com.game;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.BitSet;

import javax.imageio.ImageIO;

public class Map {
	private String path;
	public int width, height, scale;

	public BitSet pixels; // true = black, white = false

	public Map(String Path, int scale) {
		path = Path;
		this.scale = scale;
		try {
			URL a = Map.class.getResource(Path);
			System.out.println(a);
			BufferedImage image = ImageIO.read(a);
			
			width = image.getWidth();
			height = image.getHeight();
			
			int[] pixelsRGB = new int[width * height];
			
			image.getRGB(0, 0, width, height, pixelsRGB, 0, width);
			pixels = new BitSet(width*height);
			for(int i =0; i< width*height; i++){
				pixels.set(i,pixelsRGB[i] == -11776948);
			}	
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
