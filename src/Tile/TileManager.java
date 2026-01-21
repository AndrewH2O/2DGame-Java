package Tile;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;

public class TileManager {
	GamePanel gp;
	Tile[] tile;
	int[][] mapTileNum;

	public TileManager(GamePanel gp) {
		this.gp = gp;
		tile = new Tile[10]; // 10 kinds of tiles
		mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];
		getTileImage();
		loadMap("/tile_maps/world01.txt");
	}

	public void getTileImage() {
		try {
			tile[0] = new Tile();
			tile[0].image = ImageIO.read(getClass().getResourceAsStream("/tiles/grass.png"));
			tile[1] = new Tile();
			tile[1].image = ImageIO.read(getClass().getResourceAsStream("/tiles/wall.png"));
			tile[2] = new Tile();
			tile[2].image = ImageIO.read(getClass().getResourceAsStream("/tiles/water.png"));
			tile[3] = new Tile();
			tile[3].image = ImageIO.read(getClass().getResourceAsStream("/tiles/earth.png"));
			tile[4] = new Tile();
			tile[4].image = ImageIO.read(getClass().getResourceAsStream("/tiles/tree.png"));
			tile[5] = new Tile();
			tile[5].image = ImageIO.read(getClass().getResourceAsStream("/tiles/sand.png"));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loadMap(String mapFilePath) {
		try {
			InputStream is = getClass().getResourceAsStream(mapFilePath);
			BufferedReader br = new BufferedReader(new java.io.InputStreamReader(is));

			int col = 0;
			int row = 0;
			while(col < gp.maxWorldCol && row < gp.maxWorldRow) {
				String line = br.readLine();
				while(col < gp.maxWorldCol) {
					String[] numbers = line.split(" ");
					int num = Integer.parseInt(numbers[col]);
					mapTileNum[col][row] = num;
					col++;

				}
				if(col == gp.maxWorldCol) { //goto next row
					col = 0;
					row++;
				}
			}
			br.close();

		} catch (Exception e) {
			System.out.println("Error loading map: " + e.getMessage());
		}
	}

	public void draw(Graphics2D g2) {

		int worldCol = 0;
		int worldRow = 0;

		while(worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {
			// mapTileNum tells us the tile type number at this position
			int tileNum = mapTileNum[worldCol][worldRow];


			// worldCol = 0, worldRow = 0, worldX = 0 * 48, worldY = 0 * 48
			int worldX = gp.tileSize * worldCol;
			int worldY = gp.tileSize * worldRow;
			// if player is x = 500 and y = 500 away from world 0,0
			// then draw at 500 pixels to left and 500 up so -500, -500
			// which is -ve gp.player.worldX and -ve gp.player.worldY.
			// Then as players position is always at the centre of the screen,
			// qej for +ve gp.player.screenX and +ve gp.player.screenY

			int screenX = worldX - gp.player.worldX + gp.player.screenX;
			int screenY = worldY - gp.player.worldY + gp.player.screenY;

			// tile is an array of tile types
			g2.drawImage(tile[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
			worldCol++;

			if( worldCol == gp.maxWorldCol ) {
				worldCol = 0;
				worldRow++;

			}
		}



	}
}
