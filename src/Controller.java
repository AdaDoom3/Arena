import java.awt.Point;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

//
// Controller
// Info...
//
public class Controller
{

  //
  // Constants
  //
  public static final int MAXIMUM_NUMBER_OF_PLAYERS = 4;

  //
  // Variables
  //
  private static Controller instance;
  private Position.Grid<Model.Base> gridEntity;
  private Position.Grid<Sprite.Base> gridSprite;
  private Sprite.TileSet tileSet;
  private BufferedImage image;

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
  // Constructor
  // Info...
  //
  protected Controller()
  {
    this.gridEntity = new Position.Grid<Model.Base>(Model.Base.class);
    this.gridSprite = new Position.Grid<Sprite.Base>(Sprite.Base.class);
    this.tileSet = Properties.TILE_SET;
  }

  //
  // getGridModels
  // Info...
  //
  public Position.Grid<Model.Base> getGridModels()
  {
    return gridEntity;
  }

  //
  // getGridSprites
  // Info..
  //
  public Position.Grid<Sprite.Base> getGridSprites()
  {
    return gridSprite;
  }

  //
  // getTileSet
  // Info...
  //
  public Sprite.TileSet getTileSet()
  {
    return this.tileSet;
  }

  //
  //
  //
  //
  public Dimension getVisiblePixelSize()
  {
    Dimension output = new Dimension
    (
      Properties.COLUMNS * Properties.TILE_SET.toDimension().width,
      Properties.ROWS    * Properties.TILE_SET.toDimension().height
    );
    return output;
  }

  //
  // renderToBuffer
  // Info..
  //
  public void renderToBuffer()
  {
    // Dimension size = World.getInstance().getVisiblePixelSize();
    // BufferedImage buffer = new BufferedImage (
    //  size.width,
    //  size.height,
    //  BufferedImage.TYPE_4BYTE_ABGR
    // );
    // Graphics2D graphics = buffer.createGraphics();
    // for(commands command : World.getCommands())
    // {
    //  graphics.drawImage
    //  (
    //    object.sprite.getImage(), // image
    //    object.coordinates.x,     // x
    //    object.coordinates.y,     // y
    //    null                      // 
    //  );
    // }
  }

  //
  // getInstance
  // Info...
  //
  public static Controller getInstance()
  {
    if(instance == null)
    {
       instance = new Controller();
    }
    return instance;
  }
}