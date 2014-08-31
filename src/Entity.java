import java.awt.Point;

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
		return null;
	}
}
