import java.awt.Dimension;
import java.awt.Point;

public class World{
	private static World instance = null;
	public static World getInstance() {
      if(instance == null) {
         instance = new World();
      }
      return instance;
	}
	
	private Grid<Entity> grid    = null;
	private TileSet      tileSet = TileSet.LARGE;
	
	protected World(){
		
	}
	
	public synchronized Grid<Entity> getGrid(){
		return grid;
	}
	public synchronized TileSet getTileSet(){
		return this.tileSet;
	}

	public boolean isValidLocation(Point location) {
		// TODO Auto-generated method stub
		return false;
	}

	public Point getPixelCoordinates(Entity entity) {
		// TODO Auto-generated method stub
		return new Point(0,0);
	}

	public int getDepth(Entity entity) {
		// TODO Auto-generated method stub
		return 0;
	}

	public Entity[] getVisibleEntities() {
		// TODO Auto-generated method stub
		return new Entity[]{ new Entity() };
	}

	public void callback() {
		// TODO Auto-generated method stub
		
	}
}