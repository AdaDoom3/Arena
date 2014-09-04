import java.awt.Point;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class World {

  //
  // Constants
  //
  public static final int MAXIMUM_NUMBER_OF_PLAYERS = 4;

  //
  // Variables
  //
  private static World                        instance   = null;
  private        Position.Grid<Entity>        gridEntity = null;
  private        Position.Grid<Assets.Sprite> gridSprite = null;
  private        Assets.Sprite.TileSet        tileSet    = null;

  // 
  // Impulse
  // Info..
  //
  public enum Impulse
  {
    MOVE_NORTH,
    MOVE_EAST,
    MOVE_SOUTH,
    MOVE_WEST,
    MELEE,
    LOB;
  }
  
  //
  // Action
  // Info...
  //
  public enum Action {
    TILE,
    DIE,
    STAND,
    WALK,
    MELEE,
    LOB;
  }

  //
  // Constructor
  // Info...
  //
  protected World()
  {
    this.gridEntity = new Position.Grid<Entity>(Entity.class);
    this.gridSprite = new Position.Grid<Assets.Sprite>(Assets.Sprite.class);
    this.tileSet    = Properties.TILE_SET;
  }

  //
  // getGrid
  // Info...
  //
  public Position.Grid<Entity> getGridEntity() {
    return gridEntity;
  }
  public Position.Grid<Assets.Sprite> getGridSprite()
  {
    return gridSprite;
  }

  //
  // getTileSet
  // Info...
  //
  public Assets.Sprite.TileSet getTileSet()
  {
    return this.tileSet;
  }

  //
  // isValidLocation
  // Info...
  //
  public boolean isValidLocation(Point location)
  {
    // TODO Auto-generated method stub
    return false;
  }

  //
  // getPixelCoordinates
  //
  public Point getPixelCoordinates(Entity entity)
  {
    // TODO Auto-generated method stub
    return null;
  }

  //
  // 
  public int getDepth(Entity entity)
  {
    // TODO Auto-generated method stub
    return 0;
  }

  public Dimension getVisiblePixelSize()
  {
    Dimension output = new Dimension(
      Properties.COLUMNS * Properties.TILE_SET.toDimension().width,
      Properties.ROWS    * Properties.TILE_SET.toDimension().height
    );
    return output;
  }

  public Entity[] getVisibleEntities()
  {
    // TODO Auto-generated method stub
    return null;
  }

  //
  // 
  private static class WorldState {
    
    private Map<Point, BufferedImage> toDraw = null; 
    
    public WorldState() {
      this.toDraw = new HashMap<Point, BufferedImage>();
    }

    public Map<Point, BufferedImage> getCommands() {
      return this.toDraw;
    }
  }

  //
  // getInstance
  // Info...
  //
  public static World getInstance()
  {
    if(instance == null)
    {
       instance = new World();
    }
    return instance;
  }
}