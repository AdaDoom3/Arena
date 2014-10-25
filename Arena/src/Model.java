import javafx.scene.paint.Color;

import java.awt.Dimension;

import javafx.scene.image.Image;

import java.util.EnumMap;

import javax.imageio.ImageIO;
import javax.sound.sampled.Clip;

import java.lang.reflect.Array;
import java.awt.im.InputContext;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ResourceBundle;

import javax.swing.KeyStroke;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

//
// Model
// Info..
//
public class Model 
{
    
  //
  // Constants
  //
  static final String PATH_IMAGE = ".png";
  static final String PATH_ART = "file:/art";
  static final int TILE = 64;
  static final String PATH_BOUNDING = "bounding";
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
  private static Clip loadSound(String path)
  {
    return null;
  }

  //
  // rotateImage
  // Info..
  //
  private static Image rotateImage(int degrees, Image image)
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
    private int degrees;

    //
    // Constructor
    //
    private Direction(int degrees)
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

    private static final long serialVersionUID = 1L;

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
      switch(direction){
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
      this.locations = new Model.Entity[rows][columns];
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
      for(int i=0; i < locations.length; i++)
      {
        for(int j=0; j < locations[i].length; j++)
        {
          if( locations[i][j].equals(entity))
          {
            return new Location(i, j);
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
      for(int i=0; i < locations.length; i++)
      {
        for(int j=0; j < locations[i].length; j++)
        {
          if( clazz.isInstance(locations[i][j]) )
          {
            result.add(locations[i][j]);
          }
        }
      }
      return result.toArray(new Model.Entity[result.size()]);
    }
    
    //
    // mapKind
    // Info...
    //
    public Map<Location, Model.Entity> mapKind(Class<? extends Model.Entity> clazz)
    {
      Map<Location, Model.Entity> result = new HashMap<Location, Model.Entity>();
      for(int i=0; i < locations.length; i++)
      {
        for(int j=0; j < locations[i].length; j++)
        {
          if( clazz.isInstance(locations[i][j]) )
          {
            result.put(new Location(i, j), locations[i][j]);
          }
        }
      }
      return result;
    }
    
    //
    // getAll
    // Info...
    //
    public Model.Entity[] getAll()
    {
      ArrayList<Model.Entity> result = new ArrayList<Model.Entity>();
      for(int i=0; i < locations.length; i++)
      {
        for(int j=0; j < locations[i].length; j++)
        {
          result.add(locations[i][j]);
        }
      }
      return result.toArray(new Model.Entity[result.size()]);
    }
    
    //
    // mapAll
    // Info...
    //
    public Map<Location, Model.Entity> mapAll()
    {
      Map<Location, Model.Entity> result = new HashMap<Location, Model.Entity>();
      for(int i=0; i < locations.length; i++)
      {
        for(int j=0; j < locations[i].length; j++)
        {
          result.put(new Location(i, j), locations[i][j]);
        }
      }
      return result;
    }

    //
    // getRandomEmpty
    // Info...
    //
    public Location getRandomEmpty()
    {
      ArrayList<Location> emptySpots = new ArrayList<Location>();
      for(int x = 0; x < locations.length; x++)
      {
        for(int y = 0; y < locations[x].length; y++)
        {
          try
          {
            if(locations[x][y] == null) {
              emptySpots.add(new Location(x, y));
            }
          }
          catch(Exception error)
          {
            // Do nothing...
          }
        }
      }
      if(emptySpots.size() == 0)
      {
        throw new IllegalArgumentException();
      }
      return emptySpots.get(new Random().nextInt(emptySpots.size()));
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
      int angle = (int)Math.toDegrees(Math.atan2(-dy, dx));
      
      return angle;
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
      if(duration > 0)
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
    Location bounding;
    long duration;
    Clip audio;

    //
    // Constructor
    // Info..
    //
    public Animations(String path, Information information) throws Error
    {
      ArrayList<Image> temporary = new ArrayList<Image>();
      ArrayList<Image> result;
      boolean success = false;
      final String PATH_BASE = PATH_ART + "/" + path + "/" + TILE;
      try
      {
        temporary.add(new Image(PATH_BASE + PATH_BOUNDING + PATH_IMAGE));
        for(int y = 0;y < temporary.get(0).getHeight() && !success;y++)
        {
          for(int x = 0;x < temporary.get(0).getWidth() && !success;x++)
          {
            Color color = temporary.get(0).getPixelReader().getColor(x, y);
            if(color.getOpacity() > 0.0)
            {
              bounding = new Location(x + 1, y + 1);
              success = true;
            }
          }
          if(y == temporary.get(0).getHeight() - 1 && !success)
          {
            throw new Exception();
          }
        }
      }
      catch(Exception noBoundingImage)
      {
        bounding = new Location(1, 1);
      }
      temporary.clear();
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
        temporary.add(new Image(PATH_BASE + information.getName() + PATH_IMAGE));
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
            temporary.add(new Image(PATH_BASE + information.getName() + i + PATH_IMAGE));
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
                result.add(new Image
                (
                  PATH_BASE + information.getName() + direction.name() + PATH_IMAGE
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
                    result.add(new Image(
                    (
                      PATH_BASE + information.getName() + direction.name() + i + PATH_IMAGE
                    )));
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
      long timeFromStart = Math.abs(deltaTime) % this.duration;
      int frame = (int)(this.duration / timeFromStart);
      return getFrame(direction, frame);
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
    HashMap<String, Animations> actingAnimations = new HashMap<String, Animations>();

    //
    // Constructor
    // Info..
    //
    public Actions(String path, Information[] informations) throws Error
    {
      for(Information information : informations)
      {
        actingAnimations.put(information.getName(), new Animations(path, information));
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
    
    public Set<String> getAnimations(){
      return this.actingAnimations.keySet();
    }
  }

  //
  // Properties
  // Info..
  //
  public static class Properties {

    //
    // Variables
    //
    public ResourceBundle bundle = null;
    
    //
    // Constructor
    // Info..
    //
    public Properties(String name)
    {
      File resource = new File("./data");
      URL[] urls = new URL[1];
      try
      {
        urls[0]=resource.toURI().toURL();
      }
      catch (MalformedURLException e)
      {
        e.printStackTrace();
      }
      ClassLoader loader = new URLClassLoader(urls);
      this.bundle = ResourceBundle.getBundle(name, InputContext.getInstance().getLocale(), loader);
    }
    
    //
    // getConstantString
    // Info..
    //
    public String getConstantString(String name)
    {
      return this.bundle.getString(name.toLowerCase());
    }

    //
    // getConstantInfo
    // Info...
    //
    public int getConstantInt(String name)
    {
      return Integer.parseInt(this.getConstantString(name));
    }

    //
    // getConstantBoolean
    // Info..
    //
    public boolean getConstantBoolean(String name)
    {
      return Boolean.parseBoolean(this.getConstantString(name));
    }

    //
    // getConstantKeyEvent
    // Info..
    //
    public int getConstantKeyEvent(String name)
    {
      return KeyStroke.getKeyStroke(this.getConstantString(name)).getKeyCode();
    }
  }

  //
  // Entity
  // Info..
  //
  public static class Entity
  {

    //
    // Variables
    //
    Direction direction;
    public Actions actions;
    public String currentAction;
    
    //
    // 
    // Info..
    //
    private <T> T[] concatenate (T[] A, T[] B)
    {
      int aLen = A.length;
      int bLen = B.length;
      @SuppressWarnings("unchecked")
      T[] C = (T[]) Array.newInstance(A.getClass().getComponentType(), aLen+bLen);
      System.arraycopy(A, 0, C, 0, aLen);
      System.arraycopy(B, 0, C, aLen, bLen);
      return C;
    }

    //
    // Constructor
    // Info...
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
      actions = classActs.get(getClass().getSimpleName());
      this.currentAction = informations[0].name;
    }
    public Entity()
    {
      this(new Information[]{});
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
    // setDirection
    // Info..
    //
    public void setDirection(Direction item)
    {
      this.direction = item;
    }

    //
    // setDirection
    // Info..
    //
    public void setCurrentAction(String name)
    {
      //TODO : check name against all valid names
      // SO YOU DON'T WANT TO CHECK
      // if( actions.getAnimations().contains(name) ) { throw new Error("Animation "+name+" is not in "+this.toString()); }
      // OH LOOK I JUST SAVED YOU putting Print statements telling exactly what entity and which animation were failing
      // if this were a complicated application it would save you so much debugging time with 1 line of code and < 1ms instructions of execution time.
      // 1 frame of rendering at 60fps takes about 16.6666666667ms, so you can do this statement about infinity number of times per rendered frame.
      
      // if I were even more efficient I'd say if(debugging && myConditional){} and it would short circuit after 1 boolean check in production.
      // instead you want to set it without checking
      if( actions.getAnimations().contains(name) )
      {
        throw new Error("Animation "+name+" is not in "+this.toString());
      }
      this.currentAction = name;
    }
    
    public void step(long deltaTime){
      // idk what to happen
    }
    
    //
    // getFrameAtDeltaTime
    // Info..
    //
    public Image getFrameAtDeltaTime(long deltaTime) {
      return this.actions.getAnimations(this.currentAction).getFrameAtDeltaTime(this.direction, deltaTime);
    }
  }
}