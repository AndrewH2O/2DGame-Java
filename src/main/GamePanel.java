package main;

import Tile.TileManager;
import entity.Player;
import object.SuperObject;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {
	// screen settings
	final int originalTileSize = 16;

	Font arial_40;
	int FPS = 60;

	// old games had small screens e.g. 320 by 224 so we scale
	final int scale = 3; // scaling factor larger screens

	// SCREEN SETTINGS
	public int tileSize = originalTileSize * scale; // 48 * 48
	public final int maxScreenCol = 16;
	public final int maxScreenRow = 12;
	public final int screenWidth = tileSize * maxScreenCol; // 768
	public final int screenHeight = tileSize * maxScreenRow; // 576

	// WORLD SETTINGS
	public final int maxWorldCol = 50;
	public final int maxWorldRow = 50;

	// SYSTEM SETTINGS
	KeyHandler keyHandler = new KeyHandler(this);
	TileManager tileManager = new TileManager(this);
	Sound music = new Sound();
	Sound soundEffect = new Sound();

	//collision detection
	public CollisionChecker collisionChecker = new CollisionChecker(this);
	public AssetSetter assetSetter = new AssetSetter(this);
	public UI ui = new UI(this);

	//Time
	Thread gameThread;

	// ENTITY AND OBJECTS
	public Player player = new Player(this, keyHandler);

	public SuperObject[] obj = new SuperObject[10];// is this an object store. Display up to 10 objects in the game

	// GAME STATE
	public int gameState;
	public final int playState = 1;
	public final int pauseState = 2;


	public GamePanel() {
		setPreferredSize(new Dimension(screenWidth, screenHeight));
		setBackground(Color.BLACK);
		setDoubleBuffered(true);
		this.addKeyListener(keyHandler);
		this.setFocusable(true);
		arial_40 = new Font("Arial", Font.PLAIN, 40);
	}

	public void startGameThread() {
		gameThread = new Thread(this);
		gameThread.start(); // calls run method
	}

	public void setupGame() {
		assetSetter.setObject();
		playMusic(0);
		gameState = playState;
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

		while (gameThread != null) {
			currentTime = System.nanoTime();
			delta += (currentTime - lastTime) / drawInterval; // how much time has passed in frame seconds?
			timer += currentTime - lastTime;
			lastTime = currentTime;

			if (delta >= 1) {
				update();
				repaint();
				delta--;
				drawCount++;
			}

			if (timer >= 1_000_000_000) {
				System.out.println("FPS: " + drawCount);
				drawCount = 0;
				timer = 0;
			}
		}
	}

	public void update() {
		if(gameState == playState) {
			player.update();
		}
		if(gameState == pauseState) {
			// nothing
		}

	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;// gives us better geometry

		long drawStart = System.nanoTime();
		//DEBUG - how long is stuff taking?
		if (keyHandler.checkDrawTime) {
			drawStart = System.nanoTime();
		}


		tileManager.draw(g2);

		for (int i = 0; i < obj.length; i++) {
			if (obj[i] != null) obj[i].draw(g2, this);
		}

		player.draw(g2);

		// DEBUG
		if(keyHandler.checkDrawTime) {
			long drawEnd = System.nanoTime();
			long passedTime = drawEnd - drawStart;
			g2.setFont(arial_40);
			g2.setColor(Color.WHITE);
			g2.drawString("Draw time: " + passedTime + "ns", 10, 400);
			System.out.println("Draw time: " + passedTime + "ns");
		}

		ui.draw(g2);
		g2.dispose();
	}

	public void playMusic(int i) {
		music.setFile(i);
		music.play();
		music.loop();
	}

	public void stopMusic() {
		music.stop();
	}

	public void playSoundEffect(int i) {
		soundEffect.setFile(i);
		soundEffect.play();
	}
}
