package main;

import object.OBJ_Key;

import java.awt.*;
import java.awt.image.BufferedImage;

public class UI {
	GamePanel gp;
	Font arial_40, arial_80_bold;
	BufferedImage keyImage;
	public boolean messageOn = false;
	public String message = "";
	int messageCounter = 0;
	public boolean gameOver = false;

	public UI(GamePanel gp) {
		this.gp = gp;
		arial_40 = new Font("Arial", Font.PLAIN, 40);
		arial_80_bold = new Font("Arial", Font.BOLD, 80);
		OBJ_Key key = new OBJ_Key();
		keyImage = key.image;
	}

	public void showMessage(String msg) {
		message = msg;
		messageOn = true;
	}

	public void draw(Graphics2D g2) {
		if(gameOver) {
			g2.setFont(arial_40);
			g2.setColor(Color.WHITE);

			// display centre of screen
			String text;
			int textLength;
			int x, y;

			text = "You found the treasure!";
			textLength = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
			x = gp.screenWidth/2 - textLength/2;
			y = gp.screenHeight/2 - (gp.tileSize * 3); // raise text a bit
			g2.drawString(text, x, y);

			g2.setFont(arial_80_bold);
			g2.setColor(Color.YELLOW);
			text = "Congratulations";
			textLength = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
			x = gp.screenWidth/2 - textLength/2;
			y = gp.screenHeight/2 + (gp.tileSize * 2);
			g2.drawString(text, x, y);

			gp.gameThread = null;

		} else {
			// don't instatiate every time in game loop
			//g2.setFont(new Font("Arial", Font.PLAIN, 40));
			g2.setFont(arial_40);
			g2.setColor(Color.WHITE);
			// y not top, its base line i.e. bottom line
			g2.drawImage(keyImage, gp.tileSize/2, gp.tileSize/2, gp.tileSize, gp.tileSize, null);

			g2.drawString("x "+gp.player.hasKey,74,65);

			if(messageOn){
				g2.setFont(g2.getFont().deriveFont(30F)); // if we already set font we can change its size
				g2.drawString(message, gp.tileSize/2, gp.tileSize * 5);

				// to disappear message after a while
				messageCounter++;

				if(messageCounter > 100){
					messageCounter = 0;
					messageOn = false;
				}
			}
		}



	}
}
