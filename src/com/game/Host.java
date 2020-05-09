package com.game;

import java.util.Random;;

public class Host {
	private int players;
	public static Tank[] tanks;
	private static Projectile[][] bullets;
	private static Map map;

	public Host(int players) {
		this.players = players;
		tanks = new Tank[players];
		set();
	}

	private void set() {
		Random rand = new Random();
		int r = 4;
		System.out.println("Map: " + r);
		bullets = new Projectile[players][6];
		map = new Map("res/Map" + r + ".png", 5);
		for (int i = 0; i < tanks.length; i++) {
			if (tanks[i] == null)
				continue;
			tanks[i].spawn();
		}

	}

	public static Map getMap() {
		return map;
	}

	public void update() {
		for (int i = 0; i < tanks.length; i++) {
			if (tanks[i] == null)
				continue;
			if (!tanks[i].alive)
				continue;
			tanks[i].update();
		}
		for (int i = 0; i < bullets.length; i++) {
			for (int j = 0; j < 6; j++) {
				if (bullets[i][j] == null)
					continue;
				bullets[i][j].update();
			}
		}
		int c = 0;
		for (int i = 0; i < tanks.length; i++) {
			if (tanks[i] == null)
				continue;
			if (!tanks[i].alive)
				continue;
			c++;
		}
		if (!end && c <= 1) {
			endTime = System.currentTimeMillis();
			end = true;
		} else if (System.currentTimeMillis() - endTime > 3000 && c <= 1) {
			end = false;
			roundEnd(c);
		}
	}

	private boolean end = false;
	private long endTime;

	private void roundEnd(int c) {
		if (c == 1) {
			for (int i = 0; i < tanks.length; i++) {
				if (tanks[i] == null)
					continue;
				if (!tanks[i].alive)
					continue;

				tanks[i].roundWon();
				tanks[i].destroy();
				break;
			}
		}
		set();
		System.out.println("Round Ended");
	}

	public void render() {
		Screen.renderMap(map);
		for (int i = 0; i < tanks.length; i++) {
			if (tanks[i] == null)
				continue;
			if (!tanks[i].alive)
				continue;
			Screen.renderTank(tanks[i]);
		}
		for (int i = 0; i < bullets.length; i++) {
			for (int k = 0; k < 6; k++) {
				if (bullets[i][k] == null)
					continue;
				Screen.renderBullet(bullets[i][k]);
			}
		}
	}

	public void addTank(TankSprite sprite, byte KeySet) {
		int i = 0;
		while (i < tanks.length) {
			if (tanks[i] == null)
				break;
			i++;
		}
		tanks[i] = new Tank(sprite, i, KeySet);
		tanks[i].spawn();
		players++;
	}

	public static void addBullet(int x, int y, double angle, int index) {
		int i = 0;
		while (i < 6) {
			if (bullets[index][i] == null)
				break;
			i++;
		}
		bullets[index][i] = new Projectile(x, y, angle, index);
	}

	public static void destroyBullet(int index) {
		for (int j = 0; j < 6; j++) {
			if (bullets[index][j] != null) {
				if (!bullets[index][j].real) {
					bullets[index][j] = null;
					if (tanks[index] != null)
						tanks[index].bulletDestroyed();
				}
			}
		}
	}

	public static void destroyTank(int index) {
		tanks[index] = null;
	}
}
