package main;

import entity.Player;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable{
	// screen settings
	final int originalTileSize = 16;

	int FPS = 60;

	// old games had small screens e.g. 320 by 224 so we scale
	final int scale = 3; // scaling factor larger screens

	public  int tileSize = originalTileSize * scale; // 48 * 48
	final int maxScreenCol = 16;
	final int maxScreenRow = 12;
	final int screenWidth = tileSize * maxScreenCol; // 768
	final int screenHeight = tileSize * maxScreenRow; // 576

	KeyHandler keyHandler = new KeyHandler();


	//Time
	Thread gameThread;
	Player player = new Player(this, keyHandler);

	public GamePanel() {
		setPreferredSize(new Dimension(screenWidth, screenHeight));
		setBackground(Color.BLACK);
		setDoubleBuffered(true);
		this.addKeyListener(keyHandler);
		this.setFocusable(true);
	}

	public void startGameThread() {
		gameThread = new Thread(this);
		gameThread.start(); // calls run method
	}

	// ************* sleep method game loop *****************************************************
//	@Override
//	public void run() {
//		/*
//		 * start -----> update/repaint----> sleep ---> repeat
//		 * start ------------------------------------> repeat 0.016666 seconds
//		 * */
//		double drawInterval = 1_000_000_000 / FPS; // nanoseconds unit/FPS = 0.016666 seconds
//		double nextDrawTime = System.nanoTime() + drawInterval;
//
//		while (gameThread != null) {
//			// update
//			update();
//			// draw screen
//			repaint(); // calls
//
//			try {
//				double remainingTime = nextDrawTime - System.nanoTime();
//				remainingTime /= 1_000_000; //convert to milliseconds
//				if(remainingTime < 0){ // did we use all time painting?
//					remainingTime = 0;
//				}
//				Thread.sleep((long) remainingTime);
//
//				nextDrawTime += drawInterval;
//
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
//	}

	// ************* delta or accumulator game loop *********************************************
	@Override
	public void run() {
		double drawInterval = 1_000_000_000 / FPS; // nanoseconds unit/FPS = 0.016666 seconds
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;

		// display FPS on sceen
		long timer = 0;
		int drawCount = 0;

		while(gameThread != null) {
			currentTime = System.nanoTime();
			delta += (currentTime - lastTime) / drawInterval; // how much time has passed in frame seconds?
			timer += currentTime - lastTime;
			lastTime = currentTime;

			if(delta >= 1) {
				update();
				repaint();
				delta--;
				drawCount++;
			}

			if(timer >= 1_000_000_000) {
				System.out.println("FPS: " + drawCount );
				drawCount = 0;
				timer = 0;
			}
		}
	}

	public void update() {
		player.update();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;// gives us better geometry

		player.draw(g2);

		g2.dispose();
	}
}
