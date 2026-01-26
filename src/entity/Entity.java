package entity;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Entity {
	public int worldX,worldY;
	public int speed;
	public String name = "";

	public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
	public String direction;

	public int spriteCounter = 0;

	public int spriteNum = 1;

	// for interactionwith obj (we predict next moves and this is used to reset back to current position)
	public int solidAreaDefaultX, solidAreaDefaultY;

	// for collision detection
	public Rectangle solidArea;
	public boolean collisionOn = false;


}
