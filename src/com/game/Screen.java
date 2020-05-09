package com.game;

public class Screen {
	public static int width, height;
	public static Map map;
	
	public static int[][] pixels;

	public static void set(int width1, int height1) {
		width = width1;
		height = height1;
		pixels = new int[width][height];
		map = new Map("res/Map3.png", 8);
		//map = new Map("res/Bullet.png", 8);
		clear(0xFFFFFF);
	}

	public static void render() {
		clear(0xFFFFFF);
		
	}

	public static void renderMap(Map map) {

		for (int y = 0; y < Screen.height && y < map.height * map.scale; y++) {
			for (int x = 0; x < Screen.width && x < map.width * map.scale; x++) {
				pixels[x][y] = map.pixels.get((map.width * (y / map.scale) + x / map.scale)) ? 0x4C4C4C : 0xE6E6E6;
			}
		}
	}

	public static void renderTank(Tank tank) {
		TankSprite sprite = tank.sprite;
		//System.out.println("XX: " + tank.x + " YY: " + tank.y);
		for (int y = 0,yy =tank.y; y < sprite.height; y++, yy++) {
			for (int x = 0,xx=tank.x; x < sprite.width; x++, xx++) {
				if (yy < 0 || yy >= Screen.height || xx < 0 || xx >= Screen.width || sprite.pixels[x][y] == -1)
					continue;
				Screen.pixels[xx][yy] = sprite.pixels[x][y];
			}
		}
	}
	
	public static void renderBullet(Projectile bullet){
		Sprite sprite = bullet.sprite;
		//System.out.println("XX: " + tank.x + " YY: " + tank.y);
		for (int y = 0,yy =bullet.y; y < sprite.height; y++, yy++) {
			for (int x = 0,xx=bullet.x; x < sprite.width; x++, xx++) {
				if (yy < 0 || yy >= Screen.height || xx < 0 || xx >= Screen.width || sprite.pixels[x][y] == -1)
					continue;
				Screen.pixels[xx][yy] = sprite.pixels[x][y];
			}
		}
	}

	private static void clear(int colour) {
		for (int y = 0; y < Screen.height; y++) {
			for (int x = 0; x < Screen.width; x++) {
				pixels[x][y] = colour;
			}
		}
	}

}
