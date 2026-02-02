package object;

import main.GamePanel;
import main.UtilityTool;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SuperObject {
	public BufferedImage image;
	public String name;
	public boolean collision = false;
	public int worldX,worldY;
	// object collision area is whole rectangle, you could override this for specific objects
	public Rectangle solidArea = new Rectangle(0,0,48,48);
	public int solidAreaDefaultX = 0;
	public int solidAreaDefaultY = 0;
	// scale image
	UtilityTool utilityTool = new UtilityTool();

	public void draw(Graphics g2, GamePanel gp){
		// see TileManager for explanation
		int screenX = worldX - gp.player.worldX + gp.player.screenX;
		int screenY = worldY - gp.player.worldY + gp.player.screenY;
		if ((worldX + gp.tileSize > gp.player.worldX - gp.player.screenX) &&
				(worldX - gp.tileSize < gp.player.worldX + gp.player.screenX) &&
				(worldY + gp.tileSize > gp.player.worldY - gp.player.screenY) &&
				(worldY - gp.tileSize < gp.player.worldY + gp.player.screenY)) {
			g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
		}
	}
}
