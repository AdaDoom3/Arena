import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;
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
  public class Location extends Point
  {

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
    public Location getAdjacent(int direction)
    {
      int x = 0;
      int y = 0;
      switch(adjustedDirection){
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
      return new Location(getX() + x, getY() + y);
    }
  }

  //
  // Grid
  // Info...
  //
  public class Grid<T>
  {

    //
    // Variables
    //
    Object[][] locations;

    // 
    // Constructor
    // Info..
    //
    public Grid()
    {
      if(Properties.ROWS < 1 || Properties.COLUMNS < 1)
      {
        throw new IllegalArgumentException();
      }
      locations = new Object[Properties.COLUMNS][Properties.ROWS];
    }

    // 
    // add
    // Info...
    //
    public void add(Location location, T something)
    {
      locations[location.getX()][location.getY()] = something;
    }

    //
    // remove
    // Info...
    //
    public void remove(Location location)
    {
      locations[location.getX()][location.getY()] = null;
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
    public T get(Location location)
    {
      return (T)locations[location.getX()][location.getY()];
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
    public int getCount(Class object)
    {
      int result = 0;
      for(Object currentObject : getAll())
      {
        if(currentObject.getClass() == object)
        {
          result++;
        }
      }
      return result;
    }

    // 
    // getKind
    // Info...
    //
    public ArrayList<T> getKind(Class object)
    {
      ArrayList<T> result = new ArrayList<T>();
      for(Object currentObject : getAll())
      {
        if(currentObject.getClass() == object)
        {
          result.add((T)currentObject);
        }
      }
      return result;
    }

    //
    // getInstance
    // Info...
    //
    public Object getInstance(Object object)
    {
      for(Object currentObject : getAll())
      {
        if(currentObject.getClass() == object.getClass())
        {
          return currentObject;
        }
      }
      return null;
    }

    //
    // getAll
    // Info...
    //
    public ArrayList<T> getAll()
    {
      ArrayList<T> result = new ArrayList<T>();
      for(int x = 0;x < Properties.ROWS;x++)
      {
        for(int y = 0;y < Properties.COLUMNS;y++)
        {
          if(get(new Location(x, y)) != null)
          {
            result.add(get(new Location(x, y)));
          }
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
      for(int x = 0;x < Properties.ROWS;x++)
      {
        for(int y = 0;y < Properties.COLUMNS;y++)
        {
          try
          {
            if(get(new Location(x, y)) == null)
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
      if(emptySpots.size() == 0)
      {
        throw new IllegalArgumentException();
      }
      return emptySpots.get(new Random().nextInt(emptySpots.size()));
    }
  }
}