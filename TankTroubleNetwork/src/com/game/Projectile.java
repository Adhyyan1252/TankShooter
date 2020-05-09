package com.game;

public class Projectile {
	public int x, y;
	public boolean real;
	public double angle; // radians
	private long originTime;
	private static double speed = 2.8;
	private static Map map;
	public static Sprite sprite = new Sprite("res/Bullet.png");
	public int index;

	public Projectile(int x, int y, double angle, int index) {
		this.x = x;
		this.y = y;
		xa = x;
		ya = y;
		this.angle = angle;
		originTime = System.currentTimeMillis();
		this.map = Screen.map;
		cos = Math.cos(angle) * speed;
		sin = Math.sin(angle) * speed;
		this.index = index;
		real = true;
	}

	public void update() {
		if (System.currentTimeMillis() - originTime > 18000)
			destroy();
		move();
		collisionTank();
	}

	private void destroy() {
		real = false;
		Host.destroyBullet(index);
	}

	private double xa, ya;
	private double cos, sin;

	private void move() {
		collisionWall(x, (int) (ya + sin), (int) (xa + cos));
		xa += cos;
		ya += sin;
		x = (int) (xa);
		y = (int) (ya);
	}

	private void collisionTank() {
		for (int i = 0; i < Host.tanks.length; i++) {
			if (Host.tanks[i] == null)
				continue;
			if (!Host.tanks[i].alive)
				continue;
			Tank tank = Host.tanks[i];
			if (x + 6 > tank.x && x + 6 < tank.x + 64 && y + 6 > tank.y && y + 6 < tank.y + 64) {
				if (tank.sprite.pixels[6 + x - tank.x][6 + y - tank.y] != -1) {
					// HIT!!!
					Host.tanks[i].destroy();
					destroy();
				}
			}
		}
	}

	private void collisionWall(int x, int y) {
		if (map.pixels.get((x + 6) / map.scale + (y / map.scale) * map.width)
				|| map.pixels.get((x + 6) / map.scale + (y + 12 / map.scale) * map.width)) {
			angle = (Math.PI * 2) - angle;
			cos = Math.cos(angle) * speed;
			sin = Math.cos(angle) * speed;
		} else if (map.pixels.get((x) / map.scale + ((y + 6) / map.scale) * map.width)
				|| map.pixels.get((x + 12) / map.scale + (y + 6 / map.scale) * map.width)) {
			angle = Math.PI - angle;
			cos = Math.cos(angle) * speed;
			sin = Math.cos(angle) * speed;
		}
	}

	private void collisionWall(int ox, int y, int nx) {
		// check collision after incrementing y
		for (int xx = 0; xx < sprite.width; xx++) {
			for (int yy = 0; yy < sprite.height; yy++) {
				if (sprite.pixels[xx][yy] != -1 ) {
					if (map.pixels.get(((map.width * ((yy + y) / map.scale)) + (xx + ox) / map.scale))) {
						// System.out.println(angle +" X: " + xx + " Y: " + yy);
						angle = (Math.PI * 2) - angle;
						cos = Math.cos(angle) * speed;
						sin = Math.sin(angle) * speed;
						return;
					}
				}
			}
		}
		for (int xx = 0; xx < sprite.width; xx++) {
			for (int yy = 0; yy < sprite.height; yy++) {
				if (sprite.pixels[xx][yy] != -1) {
					if (map.pixels.get(((map.width * ((yy + y) / map.scale)) + (xx + nx) / map.scale))) {
						// System.out.println(angle +" X: " + xx + " Y: " + yy);
						angle = (Math.PI) - angle;
						cos = Math.cos(angle) * speed;
						sin = Math.sin(angle) * speed;
						return;
					}
				}
			}
		}
	}
}
