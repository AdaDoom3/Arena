import java.awt.Point;
import java.util.ArrayList;
import java.util.Map;

import javafx.geometry.Dimension2D;
import javafx.geometry.Dimension2DBuilder;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

//
// Controller
// Info..
//
public class Controller
{

  //
  // Constants
  //
  public static final int MAXIMUM_NUMBER_OF_PLAYERS = 4;
  public static final int TILE_SIZE = 64;
  
  //
  // Variables
  //
  static Model.Grid[] layers = null;
  static Dimension2D viewport = null;
  static ArrayList<Command> buffer = new ArrayList<Command>();

  //
  // createGrid
  // Info...
  //
  public static void createGrid(int rows, int columns)
  {
    layers = new Model.Grid[2];
    for(int i = 0;i < layers.length;i++)
    {
      layers[i] = new Model.Grid(rows, columns);
    }
    viewport = new Dimension2D(rows * TILE_SIZE, columns * TILE_SIZE);
  }

  //
  // getBuffer
  // Info..
  //
  public ArrayList<Command> getBuffer()
  {
    return buffer;
  }

  //
  // getLayer
  // Info..
  //
  public Model.Grid getLayer(int layer)
  {
    return layers[layer];
  }

  //
  // update
  // Info..
  //
  public void update(long deltaTime)
  {
    for(Model.Grid currentLayer : layers)
    {
      // for(Model.Base<?> current : currentLayer.getAll()){
        // current.update(deltaTime, currentLayer);
      // }
    }
  }
  
  public Image renderTiles(long deltaTime){
    WritableImage output = new WritableImage((int)this.viewport.getWidth(), (int)this.viewport.getHeight());
    PixelWriter pixelWriter = output.getPixelWriter();
    Map<Model.Location, Model.Entity> tiles = getTiles().mapAll();
    for(Model.Location location : tiles.keySet())
    {
      Model.Entity currentTile = tiles.get(location);
      Image tileFrame = currentTile.getFrameAtDeltaTime(deltaTime);
      PixelReader tileReader = tileFrame.getPixelReader();
      int tileX=0, tileY=0;
      pixelWriter.setPixels(location.x, location.y, (int)(tileFrame.getWidth()), (int)(tileFrame.getHeight()), tileReader, (tileX++)%((int)tileFrame.getWidth()), (tileY++)%((int)tileFrame.getHeight()));
    }
    return output;
  }
  
  public Image renderEntities(long deltaTime){
    WritableImage output = new WritableImage((int)this.viewport.getWidth(), (int)this.viewport.getHeight());
    PixelWriter pixelWriter = output.getPixelWriter();
    Map<Model.Location, Model.Entity> entities = getEntities().mapAll();
    for(Model.Location location : entities.keySet())
    {
      Model.Entity currentEntity = entities.get(location);
      Image entityFrame = currentEntity.getFrameAtDeltaTime(deltaTime);
      PixelReader entityReader = entityFrame.getPixelReader();
      int entityX=0, entityY=0;
      pixelWriter.setPixels(location.x, location.y, (int)(entityFrame.getWidth()), (int)(entityFrame.getHeight()), entityReader, (entityX++)%((int)entityFrame.getWidth()), (entityY++)%((int)entityFrame.getHeight()));
    }
    return output;
  }
  
  // 
  // setViewportSize
  // Info..
  //
  public void setViewportSize(int width, int height)
  {
    setViewportSize(new Dimension2D(width, height));
  }
//
 // setViewportSize
 // Info..
 //
 public void setViewportSize(Dimension2D size)
 {
   viewport = size;
 }

  //
  // getEntities
  // Info..
  //
  public static Model.Grid getEntities()
  {
    return layers[1];
  }

  //
  // getTiles
  //
  public static Model.Grid getTiles()
  {
    return layers[0];
  }
  
  //
  // Command
  // Info..
  //
  public class Command
  {
    
    //
    // Variables
    //
    Image image;
    Point point;
    
    //
    // Constructor
    // Info..
    //
    public Command(Image image, Point point)
    {
      this.image = image;
      this.point = point;
    }
    
    //
    // getImage
    // Info..
    //
    public Image getImage()
    {
      return image;
    }

    //
    // getPoint
    // Info..
    //
    public Point getPoint()
    {
      return point;
    }
  }

  //
  // Populator
  // Info..
  //
  public static class Populator<T extends Model.Entity>
  {

    //
    // Variables
    //
    int layer;
    Class<T> clazz;

    //
    // Constructor
    // Info..
    //
    public Populator(int layer,  Class<T> clazz)
    {
      this.layer = layer;
      this.clazz = clazz;
    }

    //
    // populate
    // Info..
    //
    public void populate(Model.Direction direction, Model.Location location)
    {
      T t = null;
      try {
        t = this.clazz.newInstance();
      } catch (Exception e) {
        e.printStackTrace();
      }
      t.setDirection(direction);
      layers[layer].add((Model.Entity)t, location);
    }
    public void populate(int thisMany, Model.Direction direction)
    {
      for(int i = 0;i < thisMany;i++)
      {
        populate(direction, layers[layer].getRandomEmpty());
      }
    }
    public void populate(int thisMany)
    {
      if(layers[layer] == null) System.out.println("SDFSDFSDFSDF");
      for(int i = 0;i < thisMany;i++)
      {
        populate(Model.getRandomDirection(), layers[layer].getRandomEmpty());
      }
    }

    //
    // populatePercent
    // Info..
    //
    public void populatePercent(int percent, Model.Direction direction)
    {
      
    }
    public void populatePercent(int percent)
    {
      
    }
  }

  //
  // Tiler
  // Info...
  //
  public static class Tiler<T extends Model.Entity> extends Populator<T>
  {

    //
    // Constructor
    // Info..
    //
    public Tiler(Class<T> clazz)
    {
      super(0, clazz);
    }

    public void populateAllEmpty() {
      
    }
  }

  //
  // Spawner
  // Info...
  //
  public static class Spawner<T extends Model.Entity> extends Populator<T>
  {

    //
    // Constructor
    // Info..
    //
    public Spawner(Class<T> clazz)
    {
      super(1, clazz);
    }
  }

  //
  // isKeyDown
  // Info..
  //
  public static boolean isKeyDown(String constantString)
  {
    // TODO Auto-generated method stub
    return false;
  }
}