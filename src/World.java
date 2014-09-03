import java.awt.Point;
import java.awt.Dimension;
public static class World
{

  //
  // Constants
  //
  public static final int MAXIMUM_NUMBER_OF_PLAYERS = 4;

  //
  // Variables
  //
  private static World instance = null;
  private Grid<Entity> gridEntity = null;
  private Grid<Sprite> gridSprite = null;
  private Assets.TileSet tileSet = Assets.TileSet.LARGE;

  // 
  // Impulse
  // Info..
  //
  public enum Impulse
  {

    //
    // Values
    //
    MOVE_NORTH,
    MOVE_EAST,
    MOVE_SOUTH,
    MOVE_WEST,
    MELEE,
    LOB;
  }

  //
  // Constructor
  // Info...
  //
  protected World()
  {
    keys.put(MOVE_NORTH, Data.PLAYER_MOVE_NORTH);

  }

  //
  // Command
  // Info...
  //
  private static class Command
  {
    public final BufferedImage image;
    public final Point coordinates;
    public RenderingObject(BufferedImage image, Point coordinates)
    {
      this.image = image;
      this.coordinates = coordinates;
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

  //
  // getGrid
  // Info...
  //
  public synchronized Grid<Entity> getGrid()
  {
    return grid;
  }

  //
  // getTileSet
  // Info...
  //
  public synchronized TileSet getTileSet()
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
      Settings.VIEW_COLUMNS * Settings.TILE_SET.toDimension().width,
      Settings.VIEW_ROWS    * Settings.TILE_SET.toDimension().height
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
  private static class RenderingState
  {
    
    private ArrayList<RenderingObject> toDraw = null; 
    
    public RenderingState()
    {
      this.toDraw = new ArrayList<RenderingObject>();
    }
    
    public void addRenderingObject(RenderingObject item)
    {
      this.toDraw.add(item);
    }

    public RenderingObject[] getRenderingObjects()
    {
      return (RenderingObject[]) this.toDraw.toArray();
    }
    
    public static RenderingState toRenderingState(World world)
    {
      RenderingState output = new RenderingState();
      TileSet tileSet = world.getTileSet();
      for(Entity entity : world.getVisibleEntities())
      {
        output.addRenderingObject(new RenderingObject
        (
          entity.getSprite(tileSet), 
          world.getPixelCoordinates(entity)
        ));
      }
      return output;
    }
  }
}