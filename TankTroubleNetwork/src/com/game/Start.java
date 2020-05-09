package com.game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

@SuppressWarnings("unused")
public class Start extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;
	public static Keyboard key;
	int width = 768;
	int height = 577;
	int scale = 1;
	int fps = 60;
	public int updates = 0, frames = 0;
	private Host host;
	private JFrame frame;
	private Thread thread;
	
	private static Sprite sheet;
	private static TankSprite sprite;
	private static Tank tank;
	
	private static boolean running = false;

	private BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

	public Start() {
		Dimension size = new Dimension(width * scale, height * scale);
		setPreferredSize(size);
		Screen.set(width, height);
		frame = new JFrame();
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = 0;
		}
		key = new Keyboard();
		addKeyListener(key);
		host = new Host(2);
		sheet = new Sprite("res/Tank2.png");
		sprite = new TankSprite(sheet, 0x0C0000, 0xCFFAF);
		host.addTank(sprite,(byte) 1);
		host.addTank(new TankSprite(sheet,0xFF00FF,0xFF20FF), (byte) 2);
		
		frame.setResizable(false);
		frame.setTitle("Tank Trouble | ups:0   fps:0");
		frame.add(this);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		start();
	}

	public synchronized void start() {
		running = true;
		thread = new Thread(this, "Thread");
		thread.start();
	}

	public synchronized void stop(int a) {
		System.out.println("Stop");
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		long initTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		long finalTime;
		double delta = 0;
		final double ns = 1000000000.0 / fps;
		while (running) {
			finalTime = System.nanoTime();
			delta += (finalTime - initTime) / ns;
			initTime = finalTime;
			while(delta >= 1) {
				update();
				updates++;
				delta--;
			}
			render();
			frames++;
			if (System.currentTimeMillis() - timer >= 1000) {
				timer += 1000;
				frame.setTitle("Tank Trouble | ups: " + updates + "   fps: " + frames);
				updates = 0;
				frames = 0;
			}
		}
	}

	public void update() {
		key.update();
		host.update();
		
	}

	public void render() {
		BufferStrategy bs = getBufferStrategy();
		while (bs == null) {
			//System.out.println("Creating");
			createBufferStrategy(3);
			bs = getBufferStrategy();
		}
		
		Screen.render();
		host.render();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				pixels[x + y * width] = Screen.pixels[x][y];
			}
		}
		

		Graphics g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		bs.show();
		g.dispose();

	}
	
	public static void main(String[] args) {
		Start s = new Start();
	}
}
