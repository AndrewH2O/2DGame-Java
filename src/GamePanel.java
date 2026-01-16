import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable{
	// screen settings
	final int originalTileSize = 16;

	// old games had small screens e.g. 320 by 224 so we scale
	final int scale = 3; // scaling factor larger screens

	final int tileSize = originalTileSize * scale; // 48 * 48
	final int maxScreenCol = 16;
	final int maxScreenRow = 12;
	final int screenWidth = tileSize * maxScreenCol; // 768
	final int screenHeight = tileSize * maxScreenRow; // 576

	//Time
	Thread gameThread;

	public GamePanel() {
		setPreferredSize(new Dimension(screenWidth, screenHeight));
		setBackground(Color.BLACK);

		setDoubleBuffered(true);
	}

	public void startGameThread() {
		gameThread = new Thread(this);
		gameThread.start(); // calls run method
	}

	int i =0;
	@Override
	public void run() {
		while (gameThread != null) {
			System.out.println("Game loop " + i++);
		}
	}
}
