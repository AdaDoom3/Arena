import java.util.Set;
import java.util.Random;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.ArrayList;
import java.util.Properties;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.awt.Point;
import java.awt.Dimension;
import java.lang.reflect.Array;
import javax.sound.sampled.Clip;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;

//
// Model
// Info..
//
public class Model 
{
    
  //
  // Constants
  //
  public static final int TILE = 64;
  static final String PATH_ART = "art";
  static final String PATH_IMAGE = ".png";
  static final String PATH_DATA = "./data/";
  static final String PATH_BOUNDING = "bounding";
  static final String PATH_PROPERTIES = ".properties";
  static final Actions genericActions = new Actions("generic", new Information[]
  {
    new Information("die")
  });
  static final Information[] requiredInformations = new Information[]
  {
    new Information("stand")
  };

  //
  // Variables
  //
  static HashMap<String, Actions> classActs = new HashMap<String, Actions>();

  //
  // loadSound
  // Info..
  //
  static Clip loadSound(String path)
  {
    return null;
  }
  
  //
  // loadImage
  // Info..
  //
  static Image loadImage(String path, Dimension dimension) throws Exception
  {
    File file = new File(PATH_ART + "/" + path + PATH_IMAGE);
    if(!file.exists())
    {
      throw new Exception();
    }
    Image image = new Image(file.toURI().toString());
    /*if(dimension.getWidth() != image.getWidth() || dimension.getHeight() != image.getHeight())
    {
      throw new Exception("Error loading asset " + path + ". Bad dimensions.");
    }*/
    //System.out.println(file.toURI().toString());
    return image;
  }

  //
  // rotateImage
  // Info..
  //
  static Image rotateImage(int degrees, Image image)
  {/*
    Image result = image;
    AffineTransform tranformation = AffineTransform.getScaleInstance(-1, 1);
    tranformation.translate(-result.getWidth(), 0);
    result = new AffineTransformOp
    (
      tranformation,
      AffineTransformOp.TYPE_NEAREST_NEIGHBOR
    ).filter(result, null);
    return result;*/return image;
  }
    
  //
  // Direction
  // Info..
  //
  public enum Direction
  {
    
    //
    // Values
    // 
    NORTH(0),
    EAST(90),
    SOUTH(180),
    WEST(270);

    //
    // Variables
    // 
    int degrees;

    //
    // Constructor
    //
    Direction(int degrees)
    {
      this.degrees = degrees;
    }

    //
    // getDegrees
    // Info..
    //
    public int getDegrees()
    {
      return degrees;
    }
  }

  //
  // getRandomDirection
  // Info...
  //
  public static Direction getRandomDirection()
  {
    return Direction.values()[new Random().nextInt(Direction.values().length)];
  }

  //
  // Location
  // Info...
  //
  public static class Location extends Point
  {

	//
	// Constants
	//
    static final long serialVersionUID = 0;

    //
    // Constructors
    // Info...
    //
    public Location(int x, int y)
    {
      this.setLocation(x, y);
    }
    public Location(Point point)
    {
      this.setLocation(point);
    }

    //
    // getDistance
    // Info...
    //
    public double getDistance(Point point)
    {
      return Math.abs(Math.sqrt
      (
        Math.pow((double)(point.getX() - getX()), 2) +
        Math.pow((double)(point.getY() - getY()), 2))
      );
    }

    //
    // getAdjacent
    // Info...
    //
    public Location getAdjacent(Direction direction)
    {
      int x = 0;
      int y = 0;
      switch(direction)
      {
        case EAST:
          x =  1;
          break;
        case SOUTH:
          y =  1;
          break;
        case WEST:
          x = -1;
          break;
        case NORTH:
          y = -1;
          break;
      }
      return new Location(this.x + x, this.y + y);
    }
  }

  //
  // Grid
  // Info.
  //
  public static class Grid
  {

    //
    // Variables
    //
    Model.Entity[][] locations = null;

    // 
    // Constructor
    // Info..
    //
    public Grid(int rows, int columns)
    {
      if(rows < 1 || columns < 1)
      {
        throw new IllegalArgumentException();
      }
      locations = new Model.Entity[rows][columns];
    }
    public Grid(Dimension size)
    {
      this(size.width, size.height);
    }

    // 
    // add
    // Info...
    //
    public void add(Model.Entity entity, Location location)
    {
      locations[location.x][location.y] = entity;
      locations[location.x][location.y].setLocation(location);
    }

    //
    // remove
    // Info...
    //
    public void remove(Location location)
    {
      locations[location.x][location.y] = null;
    }

    //
    // isInvalid
    // Info...
    //
    public boolean isInvalid(Location location)
    {
      if(location.getX() < 0 || location.getX() >= getNumberOfRows()
      || location.getY() < 0 || location.getY() >= getNumberOfColumns())
      {
        return true;
      }
      return false;
    }

    //
    // get
    // Info...
    //
    public Model.Entity get(Location location)
    {
      return locations[location.x][location.y];
    }
    public Location get(Model.Entity entity)
    {
      for(int x = 0;x < locations.length;x++)
      {
        for(int y = 0;y < locations[x].length;y++)
        {
          if(locations[x][y].equals(entity))
          {
            return new Location(x, y);
          }
        }
      }
      return null;
    }

    //
    // getNumberOfRows
    // Info...
    //
    public int getNumberOfRows()
    {
      return locations.length;
    }

    //
    // getNumberOfColumns
    // Info...
    //
    public int getNumberOfColumns()
    {
      return locations[0].length;
    }

    //
    // getCount
    // Info...
    //
    public int getCount(Class<? extends Model.Entity> clazz)
    {
      int result = 0;
      for(Model.Entity[] row : locations)
      {
        for(Model.Entity location : row)
        {
          if( clazz.isInstance(location) )
          {
            result++;
          }
        }
      }
      return result;
    }

    // 
    // getKind
    // Info...
    //
    public Model.Entity[] getKind(Class<? extends Model.Entity> clazz)
    {
      ArrayList<Model.Entity> result = new ArrayList<Model.Entity>();
      for(int x = 0;x < locations.length;x++)
      {
        for(int y = 0;y < locations[x].length;y++)
        {
          if(clazz.isInstance(locations[x][y]))
          {
            result.add(locations[x][y]);
          }
        }
      }
      return result.toArray(new Model.Entity[result.size()]);
    }
    
    //
    // getAll
    // Info...
    //
    public Model.Entity[] getAll()
    {
      ArrayList<Model.Entity> result = new ArrayList<Model.Entity>();
      for(int x = 0;x < locations.length;x++)
      {
        for(int y = 0;y < locations[x].length;y++)
        {
          if(locations[x][y] != null)
          {
            result.add(locations[x][y]);
          }
        }
      }
      return result.toArray(new Model.Entity[result.size()]);
    }
    
    //
    // getAllEmpty
    // Info..
    //
    public Location[] getAllEmpty()
    {
      ArrayList<Location> emptySpots = new ArrayList<Location>();
      for(int x = 0; x < locations.length; x++)
      {
        for(int y = 0; y < locations[x].length; y++)
        {
          try
          {
            if(locations[x][y] == null)
            {
              emptySpots.add(new Location(x, y));
            }
          }
          catch(Exception error)
          {
            // Do nothing...
          }
        }
      }     
      return emptySpots.toArray(new Location[0]);
    }

    //
    // getRandomEmpty
    // Info...
    //
    public Location getRandomEmpty()
    {
      Location[] emptySpots = getAllEmpty();
      if(emptySpots.length == 0)
      {
        throw new IllegalArgumentException();
      }
      return emptySpots[new Random().nextInt(emptySpots.length)];
    }
    
    //
    // getAdjacent
    // Info...
    //
    public Location getAdjacent(Location location, Direction direction)
    {
      Location result = location.getAdjacent(direction);
      if(this.isInvalid(result))
      {
        throw new IllegalStateException();
      }
      return result;
    }
    
    //
    // getDegreesToward
    // Info..
    //
    public int getDegreesToward(Location item, Location target)
    {
      int dx = target.x - item.x;
      int dy = target.y - item.y;
      return (int)Math.toDegrees(Math.atan2(-dy, dx));
    }
  }

  //
  // Information
  // Info..
  //
  public static class Information
  {

    //
    // Variables
    //
    String name;
    long duration;

    //
    // Constructors
    // Info...
    //
    public Information(String name)
    {
      this.name = name;
      duration = 0;
    }
    public Information(String name, long duration)
    {
      this.name = name;
      this.duration = duration;
    }

    //
    // getName
    // Info..
    //
    public String getName()
    {
      return name;
    }

    //
    // getDuration
    // Info..
    //
    public long getDuration() throws Exception
    {
      if(duration == 0)
      {
        throw new Exception("No supplied duration");
      }
      return duration;
    }
  }

  //
  // Animations
  // Info...
  //
  public static class Animations
  {

    //
    // Variables
    //
    EnumMap<Direction, ArrayList<Image>> frames =
      new EnumMap<Direction, ArrayList<Image>>(Direction.class);
    long duration;
    Clip audio;

    //
    // Constructor
    // Info..
    //
    public Animations(Dimension dimension, String path, Information information) throws Error
    {
      ArrayList<Image> temporary = new ArrayList<Image>();
      ArrayList<Image> result;
      final String PATH_BASE = path + "/" + TILE;
      try
      {
        duration = information.getDuration();
      }
      catch(Exception noDuration)
      {
        try
        {
          audio = loadSound(PATH_BASE);
          duration = audio.getMicrosecondLength();
        }
        catch(Exception noClip)
        {
          duration = 0;
        }
      }
      try
      {
        temporary.add(loadImage(PATH_BASE + information.getName(), dimension));
        for(Direction direction : Direction.values())
        {
          result = new ArrayList<Image>();
          result.add(rotateImage(direction.getDegrees(), temporary.get(0)));
          frames.put(direction, result);
        }
      }
      catch(Exception noSingleDirectionWithNoNumber)
      {
        try
        {          
          for(int i = 1;;i++)
          {
            temporary.add(loadImage(PATH_BASE + information.getName() + i, dimension));
          }
        }
        catch(Exception endOfSingleDirectionWithNumber)
        {
          if(temporary.size() > 0)
          {
            for(Direction direction : Direction.values())
            {
              result = new ArrayList<Image>();
              for(Image image : temporary)
              {
                result.add(rotateImage(direction.getDegrees(), image));
              }
              frames.put(direction, result);
            }
          }
          else
          {
            try
            {
              for(Direction direction : Direction.values())
              {
                result = new ArrayList<Image>();
                result.add(loadImage
                (
                  PATH_BASE + information.getName() + direction.name(),
                  dimension
                ));
                frames.put(direction, result);
              }
            }
            catch(Exception noDirectionWithNoNumber)
            {
              for(Direction direction : Direction.values())
              {
                result = new ArrayList<Image>();
                try
                {
                  for(int i = 1;;i++)
                  {
                    result.add(loadImage
                    (
                      PATH_BASE + information.getName() + direction.name() + i,
                      dimension
                    ));
                  }
                }
                catch(Exception endOfDirectionWithNumber)
                {
                  if(result.size() < 1)
                  {
                    throw new Error("Missing direction in animated action");
                  }
                }
                frames.put(direction, result);
              }
            }
          }
        }
      }
    }

    //
    // getFrame
    // Info..
    //
    public Image getFrame(Direction direction, int number) 
    {// number start at index 0??
      return frames.get(direction).get(number);
    }

    //
    // getFrameAtDeltaTime
    // Info..
    //
    public Image getFrameAtDeltaTime(Direction direction, long deltaTime)
    {
      //long timeFromStart = Math.abs(deltaTime) % this.duration;
      //int frame = 0;//(int)(this.duration / timeFromStart);
      if(this.duration == 0)
      {
    	return getFrame(direction, 0);
      }
      return getFrame(direction, (int)((double)(System.currentTimeMillis() - deltaTime) / (double)duration * frames.get(direction).size()) % frames.get(direction).size());
    }

    //
    // getNumberOfFrames
    // Info...
    //
    public int getNumberOfFrames(Direction direction)
    {
      return frames.get(direction).size();
    }

    //
    // getDimension
    // Info...
    //
    public Dimension getDimension()
    {
      return new Dimension
      (
        (int)frames.get(Direction.NORTH).get(0).getWidth(),
        (int)frames.get(Direction.NORTH).get(0).getHeight()
      );
    }
    
    //
    // getDuration
    // Info...
    //
    public long getDuration()
    {
      return duration;
    }
  }

  //
  // Actions
  // Info..
  //
  public static class Actions
  {

    //
    // Variables
    //
    Location bounding;
    HashMap<String, Animations> actingAnimations = new HashMap<String, Animations>();

    //
    // Constructor
    //
    public Actions(String path, Information[] informations) throws Error
    {
      Image image;
      Dimension dimension = new Dimension(TILE, TILE);
      boolean success = false;
      try
      {
        image = loadImage(path + "/" + TILE + PATH_BOUNDING, null);
        dimension.setSize(image.getWidth(), image.getHeight());
        for(int y = 0;y < image.getHeight() && !success;y++)
        {
          for(int x = 0;x < image.getWidth() && !success;x++)
          {
            Color color = image.getPixelReader().getColor(x, y);
            if(color.getOpacity() > 0.0)
            {
              bounding = new Location(x + 1, y + 1);
              success = true;
            }
          }
          if(y == image.getHeight() - 1 && !success)
          {
            throw new Exception();
          }
        }
      }
      catch(Exception noBoundingImage)
      {
        bounding = new Location(1, 1);
      }
      for(Information information : informations)
      {
        actingAnimations.put(information.getName(), new Animations(dimension, path, information));
      }
    }

    //
    // getAnimations
    // Info..
    //
    public Animations getAnimations(String action)
    {
      return actingAnimations.get(action);
    }
    public Set<String> getAnimations()
    {
      return this.actingAnimations.keySet();
    }
    
    //
    // getBounding
    // Info..
    //
    public Location getBounding()
    {
      return bounding;
    }
  }

  //
  // Entity
  // Info..
  //
  public static class Entity implements Runnable
  {

    //
    // Variables
    //
    Direction direction;
    Location location;
    Actions actions;
    String action;
    String defaultAction = "stand";
    long lastActionStart = 1;
    long lastMove;
    long lastMoveStart;
    Grid grid;
    
    //
    // concatenate
    // Info..
    //
    private <T> T[] concatenate (T[] A, T[] B)
    {
      int aLen = A.length;
      int bLen = B.length;
      @SuppressWarnings("unchecked")
      T[] C = (T[]) Array.newInstance(A.getClass().getComponentType(), aLen + bLen);
      System.arraycopy(A, 0, C, 0, aLen);
      System.arraycopy(B, 0, C, aLen, bLen);
      return C;
    }

    //
    // Constructors
    //
    public Entity(Information[] informations)
    {
      if(!classActs.containsKey(getClass().getSimpleName()))
      {
        classActs.put
        (
          getClass().getSimpleName(),
          new Actions
          (
            getClass().getSimpleName(), concatenate(requiredInformations, informations)
          )
        );
      }
      defaultAction = requiredInformations[0].name;
      actions = classActs.get(getClass().getSimpleName());
      action = defaultAction;
    }
    public Entity()
    {
      this(new Information[]{});
    }
    
    //
    // run
    // Info..
    //
    public void run(){}

    //
    // animate
    // Info..
    //
    public void animate(String action)
    {
      if(!actions.getAnimations().contains(action))
      {
        throw new Error("Animation " + action + " is not in " + this.toString());
      }
      this.lastActionStart = System.currentTimeMillis();
      this.action = action;
    }
    
    //
    // move
    // Info..
    //
    public void move(Direction direction, long duration)
    {
      if(lastMove + lastMoveStart >= System.currentTimeMillis())
      {
    	return;
      }
      lastMoveStart = 1;
      lastMove = 1;
      try
      {
        if(grid.get(grid.getAdjacent(location, direction)) == null)
        {
          setDirection(direction);
          grid.remove(location);
          grid.remove(grid.getAdjacent(location, direction));
          grid.add(this, grid.getAdjacent(location, direction));
          lastMoveStart = System.currentTimeMillis();
          lastMove = duration;
        }
      }
      catch(Exception exception)
      {
    	//
      }
    }
    
    //
    // getMovePercent
    // Info..
    //
    public double getMovePercent()
    {
      if(lastMove == 1)
      {
    	return 1.0;
      }
      return (double)(System.currentTimeMillis() - lastMoveStart) / (double)lastMove;
    }
    
    //
    // getAction
    // Info..
    //
    public String getAction()
    {
      return action;
    }
    
    //
    // getDirection
    // Info..
    //
    public Direction getDirection()
    {
      return direction;
    }
    
    //
    // getLocation
    // Info..
    //
    public Location getLocation()
    {
      return location;
    }
    
    //
    // setDirection
    // Info..
    //
    public void setDirection(Direction direction)
    {
      this.direction = direction;
    }
    
    //
    // setLocation
    // Info..
    //
    public void setLocation(Location location)
    {
      this.location = location;
    }
    
    //
    // setGrid
    // Info..
    //
    public void setGrid(Grid grid)
    {
      this.grid = grid;
    }
    
    //
    // getFrame
    // Info..
    //
    public Image getFrame()
    {
      if(actions.getAnimations(action).getDuration() < System.currentTimeMillis() - lastActionStart)
      {
    	action = defaultAction;
      }
      return actions.getAnimations(action).getFrameAtDeltaTime(direction, lastActionStart);
    }
    
    //
    // getBounding
    // Info..
    //
    public Location getBounding()
    {
      return actions.getBounding();
    }
  }

  //
  // Resource
  // Info..
  //
  public static class Resource extends Properties
  {

    //
    // Constants
    //
    static final long serialVersionUID = 0;
    
    //
    // Variables
    //
    String path = "";
    
    //
    // Constructor
    // Info..
    //
    public Resource(String path)
    {
      this.path = path;
      try
      {
        load(new FileReader(PATH_DATA + path + PATH_PROPERTIES));
      }
      catch(Exception exception)
      {
        System.out.println("No resource found at path " + PATH_DATA + path + PATH_PROPERTIES);
      }
    }
    
    //
    // getString
    // Info..
    //
    public String getString(String name)
    {
      return getProperty(name.toLowerCase());
    }

    //
    // getInt
    // Info...
    //
    public int getInt(String name)
    {
      return Integer.parseInt(this.getString(name));
    }

    //
    // getBoolean
    // Info..
    //
    public boolean getBoolean(String name)
    {
      return Boolean.parseBoolean(this.getString(name));
    }

    //
    // getDouble
    // Info..
    //
    public double getDouble(String name)
    {
      return Double.parseDouble(this.getString(name));
    }
    
    //
    // save
    // Info..
    //
    public void save()
    {
      try
      {
        store(new FileWriter(PATH_DATA + path + PATH_PROPERTIES), "");
      }
      catch(Exception exception)
      {
        System.out.println("Failed to save at path " + path);
      }
    }
  }
  
  //
  // MenuItem
  // Private class used for creating an array of assorted menu item for the Menu class
  //
  public class MenuItem
  {

    //
    // Constructor
    //
    public MenuItem(Point point, int width, int height){}

    //
    // doDisplay
    // Hides or shows a current item while maintaining data selected
    //
    public void doDisplay(boolean value){}
  }
/*
  //
  // Button
  // Selectable button that will trigger a change in menu if the “link” is valid
  //
  public class Button extends MenuItem
  {

    //
    // Constructor
    //
    public Button(Point point, int width, int height, String link, String label){}
  }

  //
  // Decoration
  // Non-interactive free floating item for use in creating images and title texts
  //
  public class Decoration extends MenuItem
  {

    //
    // Constructor
    //
    public Decoration(Point point, int width, int height, String label){}
    public Decoration(Point point, int width, int height, BufferedImage image){}
  }

  //
  // Choice
  // Interactive text that will rotate through options
  //
  public class Choice extends MenuItem
  {

    //
    // Constructor
    //
    public Choice(Point point, int width, int height, HashMap<String, ?> enumMap){}

    //
    // getString
    // Return the currently selected enumeration identifier
    //
    public String getString()
    {
      return "";
    }
  }
  
  //
  // Menu
  // Aggregate of menu items with an identifier for linking and displaying
  //
  public class Menu
  {

    //
    // Constructor
    //
    public Menu(ArrayList<MenuItem> menuItems, String id){}

    //
    // doDisplay
    // Hides or shows a current menu while maintaining data selected
    //
    public void doDisplay(boolean value){}
  }*/
}