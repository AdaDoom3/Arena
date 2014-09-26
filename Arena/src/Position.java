import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

//
// Position
// Info..
//
public class Position
{
    
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
    // mapKind
    // Info...
    //
    public Map<Location, Model.Entity> mapAll(Model.Entity item)
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
}