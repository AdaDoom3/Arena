import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

public class Entity {
	
	///////////////
	// Constants //
	///////////////
	public static final Point    DEFAULT_LOCATION = new Point(0,0);
	public static final int      DEFAULT_DEPTH    = 0;
	public static final Sprite[] DEFAULT_SPRITES  = null;
	
	///////////////
	// Variables //
	///////////////
	private Point    location = DEFAULT_LOCATION;
	private int      depth    = DEFAULT_DEPTH;
	private Sprite[] sprites  = DEFAULT_SPRITES;
	
	/////////////////
	// Constructor //
	/////////////////
	public Entity(){
		
	}
	
	public Point getLocation(){
		return this.location;
	}
	public void setLocation(Point location){
		World world = World.getInstance();
		if(world.isValidLocation(location)){
			this.location = location;
		}
	}

	public Sprite getSprite(TileSet tileSet) {
		BufferedImage image = new BufferedImage(256,256,BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D    graphics = image.createGraphics();
		Font font = new Font("Serif", Font.PLAIN, 32);
		graphics.setFont(font);
		graphics.setColor(Color.black);
		graphics.drawString("Fuck You", 100, 100);
		return new Sprite(image);
	}
}
