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
		return null;
	}

	public int getDepth(Entity entity) {
		// TODO Auto-generated method stub
		return 0;
	}

	public Dimension getVisiblePixelSize() {
		Dimension output = new Dimension(
			Settings.VIEW_COLUMNS * Settings.TILE_SET.toDimension().width,
			Settings.VIEW_ROWS    * Settings.TILE_SET.toDimension().height
		);
		return output;
	}

	public Entity[] getVisibleEntities() {
		// TODO Auto-generated method stub
		return null;
	}
}