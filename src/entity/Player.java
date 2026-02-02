package entity;

import main.GamePanel;
import main.KeyHandler;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity {
	GamePanel gp;
	KeyHandler keyHandler;

	// key count
	public int hasKey = 0;

	// where player drawn on screen - at the centre - doesn't change
	public final int screenX;
	public final int screenY;

	public Player(GamePanel gamePanel, KeyHandler keyHandler) {
		this.gp = gamePanel;
		this.keyHandler = keyHandler;

		this.name = "Player";

		screenX = gp.screenWidth / 2 - gp.tileSize / 2;
		screenY = gp.screenHeight / 2 - gp.tileSize / 2;

		// for collision detection
		solidArea = new Rectangle(8, 16, 32, 32 - 2); // full player width height is 48

		// for object interaction
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;

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
		if (keyHandler.downPressed || keyHandler.upPressed || keyHandler.leftPressed || keyHandler.rightPressed) {
			// add above if so that character animation is only when up down left or right is pressed
			if (keyHandler.upPressed) {
				direction = "up";
			} else if (keyHandler.downPressed) {
				direction = "down";
			} else if (keyHandler.leftPressed) {
				direction = "left";
			} else if (keyHandler.rightPressed) {
				direction = "right";
			}

			// check for tile collision
			collisionOn = false;
			gp.collisionChecker.checkTile(this);

			// check object collision
			int objIndex = gp.collisionChecker.checkObject(this, true);// true is player
			pickUpObject(objIndex);

			// if no collision, then move
			if (!collisionOn) {
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
			if (spriteCounter >= 12) {
				if (spriteNum == 1) spriteNum = 2;
				else if (spriteNum == 2) spriteNum = 1;
				spriteCounter = 0;
			}
		}
	}

	public void pickUpObject(int index) {
		if (index != 999) {
			// we have touched an object
			String objName = gp.obj[index].name;
			switch (objName) {
				case "Key":
					gp.playSoundEffect(1);
					hasKey++;
					gp.obj[index] = null;
					gp.ui.showMessage("Key Collected");

					break;
				case "Door":
					if (hasKey > 0) {
						gp.playSoundEffect(3);
						gp.obj[index] = null;
						hasKey--;
						gp.ui.showMessage("You opened the door");

					} else {
						gp.ui.showMessage("You need a key to open the door");
					}
					break;
				case "Boots":
					gp.playSoundEffect(2);
					speed += 2;
					gp.obj[index] = null;
					gp.ui.showMessage("Speed up");
					break;
				case "Chest":
					gp.ui.gameOver = true;
					gp.stopMusic();
					gp.playSoundEffect(4);
					break;
			}
		}
	}

	public void draw(Graphics2D g2) {
		//g.setColor(Color.WHITE);
		//g.fillRect(x, y, gamePanel.tileSize, gamePanel.tileSize);
		BufferedImage image = null;
		switch (direction) {
			case "up":
				if (spriteNum == 1) image = up1;
				if (spriteNum == 2) image = up2;
				break;
			case "down":
				if (spriteNum == 1) image = down1;
				if (spriteNum == 2) image = down2;
				break;
			case "left":
				if (spriteNum == 1) image = left1;
				if (spriteNum == 2) image = left2;
				break;
			case "right":
				if (spriteNum == 1) image = right1;
				if (spriteNum == 2) image = right2;
				break;
		}
		// don't need params gp.tileSize scaling
		g2.drawImage(image, screenX, screenY, null);
		/*
		//show collision area
		g2.setColor(Color.RED);
		g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
		 */

	}

	public void getPlayerImage() {
		up1 = setupImage("boy_up_1");
		up2 = setupImage("boy_up_2");
		down1 = setupImage("boy_down_1");
		down2 = setupImage("boy_down_2");
		left1 = setupImage("boy_left_1");
		left2 = setupImage("boy_left_2");
		right1 = setupImage("boy_right_1");
		right2 = setupImage("boy_right_2");
	}

	public BufferedImage setupImage(String imageName) {
		UtilityTool ut = new UtilityTool();
		BufferedImage image = null;
		try {
			image = ImageIO.read(getClass().getResourceAsStream("/player/" + imageName + ".png"));
			image = ut.scaleImage(image, gp.tileSize, gp.tileSize);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}
}
