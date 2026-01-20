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
	int mapTileNum[][];

	public TileManager(GamePanel gp) {
		this.gp = gp;
		tile = new Tile[10]; // 10 kinds of tiles
		mapTileNum = new int[gp.maxScreenCol][gp.maxScreenRow];
		getTileImage();
		loadMap("/tile_maps/map01.txt");
	}

	public void getTileImage() {
		try {
			tile[0] = new Tile();
			tile[0].image = ImageIO.read(getClass().getResourceAsStream("/tiles/grass.png"));
			tile[1] = new Tile();
			tile[1].image = ImageIO.read(getClass().getResourceAsStream("/tiles/wall.png"));
			tile[2] = new Tile();
			tile[2].image = ImageIO.read(getClass().getResourceAsStream("/tiles/water.png"));
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
			while(col < gp.maxScreenCol && row < gp.maxScreenRow) {
				String line = br.readLine();
				while(col < gp.maxScreenCol) {
					String[] numbers = line.split(" ");
					int num = Integer.parseInt(numbers[col]);
					mapTileNum[col][row] = num;
					col++;

				}
				if(col == gp.maxScreenCol) { //goto next row
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
//		g2.drawImage(tile[0].image, 0, 0, gp.tileSize, gp.tileSize, null);
//		g2.drawImage(tile[1].image, 0 + 48, 0, gp.tileSize, gp.tileSize, null);
//		g2.drawImage(tile[2].image, 0 + 48 + 48, 0, gp.tileSize, gp.tileSize, null);
		int col = 0;
		int row = 0;
		int x = 0;
		int y = 0;
		while(col < gp.maxScreenCol && row < gp.maxScreenRow) {
			// mapTileNum tells us the tile type number at this position
			int tileNum = mapTileNum[col][row];
			// tile is an array of tile types
			g2.drawImage(tile[tileNum].image, x, y, gp.tileSize, gp.tileSize, null);
			col++;
			x += gp.tileSize;
			if( col == gp.maxScreenCol ) {
				col = 0;
				row++;
				x = 0;
				y += gp.tileSize;
			}
		}



	}
}
