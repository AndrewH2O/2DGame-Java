package entity;

import main.GamePanel;
import main.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity {
	GamePanel gp;
	KeyHandler keyHandler;

	// where player drawn on screen - at the centre - doesn't change
	public final int screenX;
	public final int screenY;

	public Player(GamePanel gamePanel, KeyHandler keyHandler) {
		this.gp = gamePanel;
		this.keyHandler = keyHandler;

		screenX = gp.screenWidth / 2 - gp.tileSize / 2;
		screenY = gp.screenHeight / 2 - gp.tileSize / 2;

		// for collision detection
		solidArea = new Rectangle(8, 16, 32, 32-2); // full player width height is 48

		setDefaults();
		getPlayerImage();
	}

	public void setDefaults() {
		worldX = gp.tileSize * 23; // player position on world map (world01.txt) but not on the screen.
		worldY = gp.tileSize * 21;
		speed = 4;
		direction = "down";
	}

	public void update() {
		if(keyHandler.downPressed || keyHandler.upPressed || keyHandler.leftPressed || keyHandler.rightPressed) {
			// add above if so that character animation is only when up down left or right is pressed
			if(keyHandler.upPressed) {
				direction = "up";
			}
			else if(keyHandler.downPressed) {
				direction = "down";
			}
			else if(keyHandler.leftPressed) {
				direction = "left";
			}
			else if(keyHandler.rightPressed) {
				direction = "right";
			}

			// check for tile collision
			collisionOn = false;
			gp.collisionChecker.checkTile(this);
			// if no collision, then move
			if(!collisionOn) {
				switch (direction) {
					case "up":
						worldY -= speed;
						break;
					case "down":
						worldY += speed;
						break;
					case "left":
						worldX -= speed;
						break;
					case "right":
						worldX += speed;
						break;
				}
			}

			// update animation as this runs 60 times per second
			// every 10 count the spriteNum is changed between 1 and 2
			spriteCounter++;
			if(spriteCounter >= 12) {
				if (spriteNum == 1) spriteNum = 2;
				else if (spriteNum == 2) spriteNum = 1;
				spriteCounter = 0;
			}
		}

	}

	public void draw(Graphics2D g) {
		//g.setColor(Color.WHITE);
		//g.fillRect(x, y, gamePanel.tileSize, gamePanel.tileSize);
		BufferedImage image = null;
		switch (direction) {
			case "up":
				if(spriteNum == 1)image = up1;
				if(spriteNum == 2)image = up2;
				break;
			case "down":
				if(spriteNum == 1)image = down1;
				if(spriteNum == 2)image = down2;
				break;
			case "left":
				if(spriteNum == 1)image = left1;
				if(spriteNum == 2)image = left2;
				break;
			case "right":
				if(spriteNum == 1)image = right1;
				if(spriteNum == 2)image = right2;
				break;
		}
		g.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
	}

	public void getPlayerImage() {
		try {
			up1 = ImageIO.read(getClass().getResourceAsStream("/player/boy_up_1.png"));
			up2 = ImageIO.read(getClass().getResourceAsStream("/player/boy_up_2.png"));
			down1 = ImageIO.read(getClass().getResourceAsStream("/player/boy_down_1.png"));
			down2 = ImageIO.read(getClass().getResourceAsStream("/player/boy_down_2.png"));
			left1 = ImageIO.read(getClass().getResourceAsStream("/player/boy_left_1.png"));
			left2 = ImageIO.read(getClass().getResourceAsStream("/player/boy_left_2.png"));
			right1 = ImageIO.read(getClass().getResourceAsStream("/player/boy_right_1.png"));
			right2 = ImageIO.read(getClass().getResourceAsStream("/player/boy_right_2.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
