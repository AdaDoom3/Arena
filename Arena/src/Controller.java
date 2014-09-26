import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

//
// Controller
// Info..
//
public class Controller implements ActionListener
{

  //
  // Constants
  //
  public static final int MAXIMUM_NUMBER_OF_PLAYERS = 4;
  public static final int TILE_SIZE = 64;
  
  //
  // Variables
  //
  static Position.Grid[] layers;
  static BufferedImage image;
  static ArrayList<Command> buffer = new ArrayList<Command>();

  //
  // createGrid
  // Info...
  //
  public static void createGrid(int rows, int columns)
  {
    layers = new Position.Grid[2];
    for(int i = 0;i < layers.length;i++)
    {
      layers[i] = new Position.Grid(rows, columns);
    }
    image = new BufferedImage(rows * TILE_SIZE, columns * TILE_SIZE, BufferedImage.TYPE_4BYTE_ABGR);
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
  public Position.Grid getLayer(int layer)
  {
    return layers[layer];
  }

  //
  // update
  // Info..
  //
  public void update(long deltaTime)
  {
    for(Position.Grid currentLayer : layers)
    {
      // for(Model.Base<?> current : currentLayer.getAll()){
        // current.update(deltaTime, currentLayer);
      // }
    }
  }

  // 
  // setViewportSize
  // Info..
  //
  public void setViewportSize(Dimension interiorSize)
  {
    int powerX = interiorSize.width / TILE_SIZE;
    int powerY = interiorSize.height / TILE_SIZE;
    Dimension viewportSize = new Dimension(TILE_SIZE * powerX, TILE_SIZE * powerY);
    BufferedImage image = new BufferedImage(viewportSize.width, viewportSize.height, BufferedImage.TYPE_INT_ARGB);
    Graphics2D graphics = image.createGraphics();
    graphics.setPaint ( Color.BLACK );
    graphics.fillRect ( 0, 0, this.image.getWidth(), this.image.getHeight() );
    // Command.buffer.add(new Command(image, new Point(0,0)));
  }

  //
  // actionPerformed
  // Info..
  //
  public void actionPerformed(ActionEvent e) {
    /* Graphics2D graphics = image.createGraphics();
    for(Command command : Command.buffer)
    {
      graphics.drawImage(
        command.getImage(),   // image
        command.getPoint().x, // x
        command.getPoint().y, // y
        null                  // 
      );
      Command.buffer.remove(command);
    } */
    JLabel label = (JLabel) e.getSource();
    ImageIcon image = new ImageIcon(this.image); 
    label.setIcon(image);
  }

  //
  // getEntities
  // Info..
  //
  public static Position.Grid getEntities()
  {
    return layers[1];
  }

  //
  // getTiles
  //
  public static Position.Grid getTiles()
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
    public void populate(Position.Direction direction, Position.Location location)
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
    public void populate(int thisMany, Position.Direction direction)
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
        populate(Position.getRandomDirection(), layers[layer].getRandomEmpty());
      }
    }

    //
    // populatePercent
    // Info..
    //
    public void populatePercent(int percent, Position.Direction direction)
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