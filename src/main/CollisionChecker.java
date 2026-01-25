package main;

import entity.Entity;

public class CollisionChecker {
	private final GamePanel gp;

	public CollisionChecker(GamePanel gp) {
		this.gp = gp;
	}

	public void checkTile(Entity entity){
		// e.g. solidArea = new Rectangle(8, 16, 32, 32 - 2;
		int entityLeftWorldX = entity.worldX + entity.solidArea.x;
		int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
		int entityTopWorldY = entity.worldY + entity.solidArea.y;
		int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;

		// convert to tile coordinates
		int entityLeftCol = entityLeftWorldX / gp.tileSize;
		int entityRightCol = entityRightWorldX / gp.tileSize;
		int entityTopRow = entityTopWorldY / gp.tileSize;
		int entityBottomRow = entityBottomWorldY / gp.tileSize;

		// idea is that collision can occur between only two tiles
		int tileNum1, tileNum2;

		// below originalTileSize is 16, Tile size is that * scale = 48 where scale is 3
		switch(entity.direction) {
			case "up":
				entityTopRow = (entityTopWorldY - entity.speed)/ gp.tileSize;
				tileNum1 = gp.tileManager.mapTileNum[entityLeftCol][entityTopRow]; // l p r player collide with l or r
				tileNum2 = gp.tileManager.mapTileNum[entityRightCol][entityTopRow];
				// is player hitting a solid tile?
				if(gp.tileManager.tile[tileNum1].collision || gp.tileManager.tile[tileNum2].collision){
					entity.collisionOn = true;
				}
				break;
			case "down":
				entityBottomRow = (entityBottomWorldY + entity.speed)/ gp.tileSize;
				tileNum1 = gp.tileManager.mapTileNum[entityLeftCol][entityBottomRow];
				tileNum2 = gp.tileManager.mapTileNum[entityRightCol][entityBottomRow];
				// is player hitting a solid tile?
				if(gp.tileManager.tile[tileNum1].collision || gp.tileManager.tile[tileNum2].collision){
					entity.collisionOn = true;
				}
				break;
			case "left":
				entityLeftCol = (entityLeftWorldX - entity.speed)/ gp.tileSize;
				tileNum1 = gp.tileManager.mapTileNum[entityLeftCol][entityTopRow];
				tileNum2 = gp.tileManager.mapTileNum[entityLeftCol][entityBottomRow];
				// is player hitting a solid tile?
				if(gp.tileManager.tile[tileNum1].collision || gp.tileManager.tile[tileNum2].collision){
					entity.collisionOn = true;
				}
				break;
			case "right":
				entityRightCol = (entityRightWorldX + entity.speed)/ gp.tileSize;
				tileNum1 = gp.tileManager.mapTileNum[entityRightCol][entityTopRow];
				tileNum2 = gp.tileManager.mapTileNum[entityRightCol][entityBottomRow];
				// is player hitting a solid tile?
				if(gp.tileManager.tile[tileNum1].collision || gp.tileManager.tile[tileNum2].collision){
					entity.collisionOn = true;
				}
				break;
		}
	}
}
