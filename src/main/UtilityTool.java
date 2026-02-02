package main;

import java.awt.*;
import java.awt.image.BufferedImage;

public class UtilityTool {
	public BufferedImage scaleImage(BufferedImage img, int width, int height) {
		// scale tile upfront
		// TODO if resizing may get Blank or Black tiles preset img.GetType to 2?
		BufferedImage image = new BufferedImage(width, height, img.getType());
		Graphics2D g2 = image.createGraphics();
		// image drawn in scaledImage
		g2.drawImage(img, 0, 0, width, height, null);
		g2.dispose();
		return image;
	}
}
