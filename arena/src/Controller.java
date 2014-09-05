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
  private Position.Grid<Model.Base> gridModels;
  private Position.Grid<Sprite.Animation> gridTiles;
  private Sprite.TileSet tileSet;
  private BufferedImage image;

  //
  // Constructor
  // Info...
  //
  protected Controller()
  {
    this.gridModels = new Position.Grid<Model.Base>(Model.Base.class);
    this.gridTiles = new Position.Grid<Sprite.Animation>(Sprite.Animation.class);
    this.tileSet = Properties.TILE_SET;
  }

  //
  // getModels
  // Info...
  //
  public Position.Grid<Model.Base> getGridModels()
  {
    return gridModels;
  }

  //
  // getTiles
  // Info..
  //
  public Position.Grid<Sprite.Animation> getTiles()
  {
    return gridTiles;
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