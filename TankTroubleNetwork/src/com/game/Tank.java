package com.game;

import java.util.BitSet;
import java.util.Random;

public class Tank {
	public int x, y;
	public double angle = 0;
	private static double speed = 1.4, rotSpeed = 0.06;
	public int color;
	private byte score;
	public int bulletCount;
	public TankSprite sprite;
	public int ip, index;
	private Map map;
	private byte KeySet;
	public boolean alive;

	public Tank(TankSprite sprite, int index, byte KeySet) {

		this.sprite = sprite;
		this.index = index;
		this.KeySet = KeySet;
		score = 0;
	}

	public void update() {
		move();
		shoot();
	}

	private boolean prevSpace = false;

	public void spawn() {
		alive = true;
		this.map = Host.getMap();
		Random rand = new Random();
		boolean check = false;
		while (!check) {
			x = rand.nextInt(map.width * map.scale - 64);
			y = rand.nextInt(map.height * map.scale - 64);
			angle = (double) (rand.nextInt((int) (2 * Math.PI * 100))) / 100;
			pixels1 = sprite.rotate(angle);
			if (!collisionDetect(x, y)) {
				sprite.pixels = pixels1;
				cos = Math.cos(angle) * speed;
				sin = Math.sin(angle) * speed;
				check = true;
			}
			xa = x;
			ya = y;
		}
		check = false;
		bulletCount = 0;
	}

	private void shoot() {
		if (Start.key.getKey(KeySet, 's')) {
			if (!prevSpace) {
				prevSpace = true;
				if (bulletCount < 6) {
					bulletCount++;
					Host.addBullet((int) ((x + 32) + 43 * Math.cos(angle)), (int) ((y + 32) + 43* Math.sin(angle)),
							angle, index);
				}
			}
		} else
			prevSpace = false;
	}

	private double xa, ya, cos = 1, sin = 10;
	private int[][] pixels1;

	private void move() {
		if (Start.key.getKey(KeySet, 'u')) {
			if (!collisionDetect((int) (xa + cos), (int) (ya + sin),-15066598)) {
				xa += cos;
				ya += sin;
				this.x = (int) xa;
				this.y = (int) ya;
				// System.out.println(angle +" X: " + this.x + " Y: " + this.y);
			}
		}

		if (Start.key.getKey(KeySet, 'd')) {
			if (!collisionDetect((int) (xa - cos), (int) (ya - sin),-15066598)) {
				xa -= cos;
				ya -= sin;
				this.x = (int) xa;
				this.y = (int) ya;
				// System.out.println(angle +" X: " + this.x + " Y: " + this.y);
			}
		}

		if (Start.key.getKey(KeySet, 'r')) { // rotate clockwise
			pixels1 = sprite.rotate(angle + rotSpeed);
			if (!collisionDetect(x, y)) {
				angle = angle > 2 * Math.PI ? angle + rotSpeed - 2 * Math.PI : angle + rotSpeed;
				sprite.pixels = pixels1;
				cos = Math.cos(angle) * speed;
				sin = Math.sin(angle) * speed;
			}
		}

		if (Start.key.getKey(KeySet, 'l')) { // rotate clockwise
			pixels1 = sprite.rotate(angle - rotSpeed);
			if (!collisionDetect(x, y)) {
				angle = angle > 0 ? angle - rotSpeed + 2 * Math.PI : angle - rotSpeed;
				sprite.pixels = pixels1;
				cos = Math.cos(angle) * speed;
				sin = Math.sin(angle) * speed;
			}
		}

		/*
		 * if (Start.key.left)// rotate antiClockwise if (!collisionDetect(x -
		 * speed, y)) { x -= speed; }
		 */
	}

	private boolean collisionDetect(int x, int y) {

		for (int xx = 0; xx < 64; xx++) {
			for (int yy = 0; yy < 64; yy++) {
				if (pixels1[xx][yy] != -1) {
					if (map.pixels.get(((map.width * ((yy + y) / map.scale)) + (xx + x) / map.scale))) {
						// System.out.println(angle +" X: " + xx + " Y: " + yy);
						return true;
					}
				}
			}
		}
		return false;
	}
	
	private boolean collisionDetect(int x, int y, int colour) {

		for (int xx = 0; xx < 64; xx++) {
			for (int yy = 0; yy < 64; yy++) {
				if (pixels1[xx][yy] == colour) {
					if (map.pixels.get(((map.width * ((yy + y) / map.scale)) + (xx + x) / map.scale))) {
						// System.out.println(angle +" X: " + xx + " Y: " + yy);
						return true;
					}
				}
			}
		}
		return false;
	}

	public void destroy() {
		alive = false;
		x = -100;
		y = -100;
		// Host.destroyTank(index);
	}

	public void roundWon() {
		score++;
	}

	public int getScore() {
		return (int) score;
	}

	public void bulletDestroyed() {
		bulletCount--;
	}
}
