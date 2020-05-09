package com.game;

public class TankSprite {
	public int[][] oPixels;
	public int[][] pixels;
	public int width, height;
	private final int colorChangeTank = -3403503, colorChangeTurret = -6413311, colorChangeBlack = -14803426;

	public TankSprite(Sprite sheet, int colorTank, int colorTurret) {
		this.width = sheet.width;
		this.height = sheet.height;
		oPixels = new int[sheet.width][sheet.height];
		for (int x = 0; x < sheet.width; x++) {
			for (int y = 0; y < sheet.height; y++) {
				oPixels[x][y] = sheet.pixels[x][y];
				if (oPixels[x][y] == colorChangeTank)
					oPixels[x][y] = colorTank;
				else if (oPixels[x][y] == colorChangeTurret)
					oPixels[x][y] = colorTurret;
			}
		}
		pixels = oPixels;
	}

	public int[][] rotate(double angle) {
		int[][] pixels = this.oPixels;
		int[][] result = new int[width][height];

		double nx_x = rotX(-angle, 1.0, 0.0);
		double nx_y = rotY(-angle, 1.0, 0.0);
		double ny_x = rotX(-angle, 0.0, 1.0);
		double ny_y = rotY(-angle, 0.0, 1.0);

		double x0 = rotX(-angle, -width / 2.0, -height / 2.0) + width / 2.0;
		double y0 = rotY(-angle, -width / 2.0, -height / 2.0) + height / 2.0;

		for (int y = 0; y < height; y++) {
			double x1 = x0;
			double y1 = y0;
			for (int x = 0; x < width; x++) {
				int xx = (int) x1;
				int yy = (int) y1;
				int col = 0;
				if (xx < 0 || xx >= height || yy < 0 || yy >= height)
					col = -1;
				else
					col = pixels[xx][yy];
				result[x][y] = col;
				x1 += nx_x;
				y1 += nx_y;
			}
			x0 += ny_x;
			y0 += ny_y;
		}

		return result;
	}

	private double rotX(double angle, double x, double y) {
		double cos = Math.cos(angle - Math.PI / 2);
		double sin = Math.sin(angle - Math.PI / 2);
		return x * cos + y * -sin;
	}

	private double rotY(double angle, double x, double y) {
		double cos = Math.cos(angle - Math.PI / 2);
		double sin = Math.sin(angle - Math.PI / 2);
		return x * sin + y * cos;

	}
}
