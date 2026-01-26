package main;

import entity.Entity;

public class CollisionChecker {
	private final GamePanel gp;

	public CollisionChecker(GamePanel gp) {
		this.gp = gp;
	}

	public void checkTile(Entity entity) {
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
		switch (entity.direction) {
			case "up":
				entityTopRow = (entityTopWorldY - entity.speed) / gp.tileSize;
				tileNum1 = gp.tileManager.mapTileNum[entityLeftCol][entityTopRow]; // l p r player collide with l or r
				tileNum2 = gp.tileManager.mapTileNum[entityRightCol][entityTopRow];
				// is player hitting a solid tile?
				if (gp.tileManager.tile[tileNum1].collision || gp.tileManager.tile[tileNum2].collision) {
					entity.collisionOn = true;
				}
				break;
			case "down":
				entityBottomRow = (entityBottomWorldY + entity.speed) / gp.tileSize;
				tileNum1 = gp.tileManager.mapTileNum[entityLeftCol][entityBottomRow];
				tileNum2 = gp.tileManager.mapTileNum[entityRightCol][entityBottomRow];
				// is player hitting a solid tile?
				if (gp.tileManager.tile[tileNum1].collision || gp.tileManager.tile[tileNum2].collision) {
					entity.collisionOn = true;
				}
				break;
			case "left":
				entityLeftCol = (entityLeftWorldX - entity.speed) / gp.tileSize;
				tileNum1 = gp.tileManager.mapTileNum[entityLeftCol][entityTopRow];
				tileNum2 = gp.tileManager.mapTileNum[entityLeftCol][entityBottomRow];
				// is player hitting a solid tile?
				if (gp.tileManager.tile[tileNum1].collision || gp.tileManager.tile[tileNum2].collision) {
					entity.collisionOn = true;
				}
				break;
			case "right":
				entityRightCol = (entityRightWorldX + entity.speed) / gp.tileSize;
				tileNum1 = gp.tileManager.mapTileNum[entityRightCol][entityTopRow];
				tileNum2 = gp.tileManager.mapTileNum[entityRightCol][entityBottomRow];
				// is player hitting a solid tile?
				if (gp.tileManager.tile[tileNum1].collision || gp.tileManager.tile[tileNum2].collision) {
					entity.collisionOn = true;
				}
				break;
		}
	}

	public int checkObject(Entity entity, boolean player) {
		int index = 999;
		for (int i = 0; i < gp.obj.length; i++) { // obj is our store of objects on screen
			if (gp.obj[i] != null) {
				// get entity's solid area position
				entity.solidArea.x = entity.worldX + entity.solidArea.x;
				entity.solidArea.y = entity.worldY + entity.solidArea.y;

				// get the objects solid area position
				// note though that we have set objects solidArea x and y to 0 and so we only use
				// + solidArea.x if we change the objects hit rectangle
				gp.obj[i].solidArea.x = gp.obj[i].worldX + gp.obj[i].solidArea.x;
				gp.obj[i].solidArea.y = gp.obj[i].worldY + gp.obj[i].solidArea.y;

				// make next forecast move to test intersect
				switch (entity.direction) {
					case "up":
						entity.solidArea.y -= entity.speed;
						if (entity.solidArea.intersects(gp.obj[i].solidArea)) {
							if (gp.obj[i].collision) {
								entity.collisionOn = true;
							}
							if(player)index = i;
						}
						break;
					case "down":
						entity.solidArea.y += entity.speed;
						if (entity.solidArea.intersects(gp.obj[i].solidArea)) {
							if (gp.obj[i].collision) {
								entity.collisionOn = true;
							}
							if(player)index = i;
						}
						break;
					case "left":
						entity.solidArea.x -= entity.speed;
						if (entity.solidArea.intersects(gp.obj[i].solidArea)) {
							if (gp.obj[i].collision) {
								entity.collisionOn = true;
							}
							if(player)index = i;
						}
						break;
					case "right":
						entity.solidArea.x += entity.speed;
						if (entity.solidArea.intersects(gp.obj[i].solidArea)) {
							if (gp.obj[i].collision) {
								entity.collisionOn = true;
							}
							if(player)index = i;
						}
						break;
				}
				// reset entity's solid area position
				entity.solidArea.x = entity.solidAreaDefaultX;
				entity.solidArea.y = entity.solidAreaDefaultY;
				gp.obj[i].solidArea.x = gp.obj[i].solidAreaDefaultX;
				gp.obj[i].solidArea.y = gp.obj[i].solidAreaDefaultY;
			}
		}
		return index;
	}
}
