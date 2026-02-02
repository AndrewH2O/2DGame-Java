package Tile;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;

public class TileManager {
	GamePanel gp;
	public Tile[] tile; // stors diff types of tile
	public int[][] mapTileNum;

	public TileManager(GamePanel gp) {
		this.gp = gp;
		tile = new Tile[50];
		mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];
		getTileImage();
		loadMap("/tile_maps/worldV2.txt");
	}

	public void getTileImage() {
		// tiles 0-9 are placeholder tiles
		setupTile(0, "grass00", false);
		setupTile(1, "grass00", false);
		setupTile(2, "grass00", false);
		setupTile(3, "grass00", false);
		setupTile(4, "grass00", false);
		setupTile(5, "grass00", false);
		setupTile(6, "grass00", false);
		setupTile(7, "grass00", false);
		setupTile(8, "grass00", false);
		setupTile(9, "grass00", false);

		// use 2 digit numbers make map spacing consistent avoid mix 1 and 2digit
		setupTile(10, "grass00", false);
		setupTile(11, "grass01", false);
		setupTile(12, "water00", true);
		setupTile(13, "water01", true);
		setupTile(14, "water02", true);

		setupTile(15, "water03", true);
		setupTile(16, "water04", true);
		setupTile(17, "water05", true);
		setupTile(18, "water06", true);
		setupTile(19, "water07", true);

		setupTile(20, "water08", true);
		setupTile(21, "water09", true);
		setupTile(22, "water10", true);
		setupTile(23, "water11", true);
		setupTile(24, "water12", true);

		setupTile(25, "water13", true);
		setupTile(26, "road00", false);
		setupTile(27, "road01", false);
		setupTile(28, "road02", false);
		setupTile(29, "road03", false);

		setupTile(30, "road04", false);
		setupTile(31, "road05", false);
		setupTile(32, "road06", false);
		setupTile(33, "road07", false);
		setupTile(34, "road08", false);

		setupTile(35, "road09", false);
		setupTile(36, "road10", false);
		setupTile(37, "road11", false);
		setupTile(38, "road12", false);
		setupTile(39, "earth", false);

		setupTile(40, "wall", true);
		setupTile(41, "tree", true);
	}

	public void setupTile(int index, String imageName, boolean collision) {
		UtilityTool ut = new UtilityTool();
		try {
			tile[index] = new Tile();
			tile[index].image = ImageIO.read(getClass().getResourceAsStream("/tiles/new_version/" + imageName + ".png"));
			tile[index].image = ut.scaleImage(tile[index].image, gp.tileSize, gp.tileSize);
			tile[index].collision = collision;
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
			while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
				String line = br.readLine();
				while (col < gp.maxWorldCol) {
					String[] numbers = line.split(" ");
					int num = Integer.parseInt(numbers[col]);
					mapTileNum[col][row] = num;
					col++;

				}
				if (col == gp.maxWorldCol) { //goto next row
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

		while (worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {
			// mapTileNum tells us the tile type number at this position
			int tileNum = mapTileNum[worldCol][worldRow];


			// worldCol = 0, worldRow = 0, worldX = 0 * 48, worldY = 0 * 48
			int worldX = gp.tileSize * worldCol;
			int worldY = gp.tileSize * worldRow;

			// if player is x = 500 and y = 500 away from world 0,0
			// then draw at 500 pixels to left and 500 up so -500, -500
			// which is -ve gp.player.worldX and -ve gp.player.worldY.
			// Then as players position is always at the centre of the screen,
			// adj for +ve gp.player.screenX and +ve gp.player.screenY
			int screenX = worldX - gp.player.worldX + gp.player.screenX;
			int screenY = worldY - gp.player.worldY + gp.player.screenY;

			// tile is an array of tile types

			// Only draw visible screen area within screen bounds
			//   | <------screenX ----- Player @ centre ------screenX ---> |
			// and the same for Y.
			// The tileSize adj is so that we don't see the edge of the screen filled with black tiles
			// we overlap the boundary by tileSize around each edge
			if ((worldX + gp.tileSize > gp.player.worldX - gp.player.screenX) &&
					(worldX - gp.tileSize < gp.player.worldX + gp.player.screenX) &&
					(worldY + gp.tileSize > gp.player.worldY - gp.player.screenY) &&
					(worldY - gp.tileSize < gp.player.worldY + gp.player.screenY)) {
				// don't need width height after scaling
				g2.drawImage(tile[tileNum].image, screenX, screenY, null);
			}
			worldCol++;

			if (worldCol == gp.maxWorldCol) {
				worldCol = 0;
				worldRow++;

			}
		}


	}
}
