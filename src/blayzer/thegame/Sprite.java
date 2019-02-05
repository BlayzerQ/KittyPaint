package blayzer.thegame;

import java.awt.Graphics;
import java.awt.Image;

public class Sprite {
	private Image image;
	public int X;
	public int Y;
	
	public Sprite(Image image) {
		this.image = image;
	}
	
	public int getWidth() {
		return image.getWidth(null);
	}

	public int getHeight() {
		return image.getHeight(null);
	}
	
	public void draw(Graphics g, int x, int y, int wight, int height) {
		g.drawImage(image, x, y, wight, height, null);
	}
}