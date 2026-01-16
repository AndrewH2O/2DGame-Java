import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable{
	// screen settings
	final int originalTileSize = 16;

	int FPS = 60;

	// old games had small screens e.g. 320 by 224 so we scale
	final int scale = 3; // scaling factor larger screens

	final int tileSize = originalTileSize * scale; // 48 * 48
	final int maxScreenCol = 16;
	final int maxScreenRow = 12;
	final int screenWidth = tileSize * maxScreenCol; // 768
	final int screenHeight = tileSize * maxScreenRow; // 576

	KeyHandler keyHandler = new KeyHandler();
	// players default position
	int playerX = 100;
	int playerY = 100;
	int playerSpeed = 4;

	//Time
	Thread gameThread;

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

		while(gameThread != null) {
			currentTime = System.nanoTime();
			delta += (currentTime - lastTime) / drawInterval; // how much time has passed in frame seconds?
			lastTime = currentTime;

			if(delta >= 1) {
				update();
				repaint();
				delta--;
			}
		}
	}

	public void update() {
		if(keyHandler.upPressed) {
			playerY -= playerSpeed;
		}
		else if(keyHandler.downPressed) {
			playerY += playerSpeed;
		}
		else if(keyHandler.leftPressed) {
			playerX -= playerSpeed;
		}
		else if(keyHandler.rightPressed) {
			playerX += playerSpeed;
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;// gives us better geometry
		g2.setColor(Color.WHITE);
		System.out.println("Player "+playerX+","+playerY);
		g2.fillRect(playerX, playerY, tileSize, tileSize);
		g2.dispose();
	}
}
