package blayzer.thegame;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class TheGame extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;

	public static int WIDTH = 1400;
	public static int HEIGHT = 800;
	public static String NAME = "KittyPaint";
	
	public static Sprite hero;
	public static Sprite background;
	
	public int heroWidth = 50;
	public int heroHeight = 50;
	public int heroCachedWidth = 50;
	public int heroCachedHeight = 50;
	
	private boolean leftPressed = false;
	private boolean rightPressed = false;
	private boolean downPressed = false;
	private boolean upPressed = false;
	
	Random random = new Random();
	
	public void start() {
		new Thread(this).start();
	}
	
	@Override
	public void run() {
		
		long lastTime = System.currentTimeMillis();
		long delta;
		
		init();
		
		while(true){
			delta = System.currentTimeMillis() - lastTime;
			
			try {
				TimeUnit.MILLISECONDS.sleep(3);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			lastTime = System.currentTimeMillis();
			render();
			update(delta);
		}
	}
	
	public void init() {
		addKeyListener(new KeyInputHandler());
		
		hero = getSprite("blayzer/thegame/assets/kitty.png");
		//background = getSprite("blayzer/thegame/assets/background.png");
		
		heroWidth = hero.getWidth();
		heroHeight = hero.getHeight();
		
		addMouseWheelListener(new MouseWheelListener() {
		    @Override
		    public void mouseWheelMoved(MouseWheelEvent e) {
	            if (e.getWheelRotation() < 0)
	            {
	            	heroWidth += 10;
	            	heroHeight += 10;
	            }
	            else
	            {
	            	heroWidth -= 10;
	            	heroHeight -= 10;
	            }
		    }
		  });
	}
	
	public void render() {
		BufferStrategy bs = getBufferStrategy(); 
		if (bs == null) {
			createBufferStrategy(2); //create BufferStrategy for canvas
			requestFocus();
			return;
		}
			
		Graphics g = bs.getDrawGraphics(); //getting Graphics from BufferStrategy
		
		//g.setColor(Color.CYAN);
		//background.draw(g, 0, 0, WIDTH, HEIGHT);
		//g.fillRect(0, 0, getWidth(), getHeight());
		
		hero.draw(g, hero.X, hero.Y, heroWidth, heroHeight); //Draw a hero
		g.dispose();
		bs.show();
	}
	
	public void update(long delta){
		if (leftPressed)
			hero.X--;
		if (rightPressed)
			hero.X++;
		if(upPressed)
			hero.Y--;
		if(downPressed)
			hero.Y++;
	}
	
	public Sprite getSprite(String path) {
		BufferedImage sourceImage = null;
			
		try {
			URL url = this.getClass().getClassLoader().getResource(path);
			sourceImage = ImageIO.read(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
			
		return new Sprite(Toolkit.getDefaultToolkit().createImage(sourceImage.getSource()));
	}
	
	public static void main(String[] args){
		
		TheGame game = new TheGame();
		game.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		JFrame frame = new JFrame(TheGame.NAME);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(game, BorderLayout.CENTER);
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);
		
		game.start();
	}
	
	private class KeyInputHandler extends KeyAdapter {
		public void keyPressed(KeyEvent e) {			
			switch(e.getKeyCode()) {
				case KeyEvent.VK_A:
					leftPressed = true;
					break;
				case KeyEvent.VK_D:
					rightPressed = true;
					break;
				case KeyEvent.VK_S:
					downPressed = true;
					break;
				case KeyEvent.VK_W:
					upPressed = true;
					break;
				case KeyEvent.VK_SPACE:
					heroCachedWidth = heroWidth;
					heroCachedHeight = heroHeight;
					heroWidth = 0;
					heroHeight = 0;
					makeScreen();
					break;
			}
		}
		
		public void keyReleased(KeyEvent e) {		
			switch(e.getKeyCode()) {
				case KeyEvent.VK_A:
					leftPressed = false;
					break;
				case KeyEvent.VK_D:
					rightPressed = false;
					break;
				case KeyEvent.VK_S:
					downPressed = false;
					break;
				case KeyEvent.VK_W:
					upPressed = false;
					break;
				case KeyEvent.VK_SPACE:
					heroWidth = heroCachedWidth;
					heroHeight = heroCachedHeight;
					break;
				}
		}
	}
	
	public static void makeScreen() {
		try {
			BufferedImage image = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
			ImageIO.write(image, "png", new File(System.getProperty("user.dir"), "screen.png"));
		} catch (IOException | HeadlessException | AWTException e) {
			System.out.println("IO exception" + e);
		}
	}

}