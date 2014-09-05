import java.awt.Point;
import java.lang.reflect.Array;
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
  // Info...
  //
  public static class Grid<T>
  {

    //
    // Variables
    //
    T[][] locations;
    Class<T> classT;

    // 
    // Constructor
    // Info..
    //
    @SuppressWarnings("unchecked")
    public Grid(Class<T> classT)
    {
      if(Properties.ROWS < 1 || Properties.COLUMNS < 1)
      {
        throw new IllegalArgumentException();
      }
      this.classT = classT;
      this.locations = (T[][]) Array.newInstance(classT , Properties.ROWS, Properties.COLUMNS);
    }

    // 
    // add
    // Info...
    //
    public void add(Location location, T something)
    {
      locations[location.x][location.y] = something;
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
    public T get(Location location)
    {
      return locations[location.x][location.y];
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
    public int getCount(T item)
    {
      int result = 0;
      for(T[] row : locations)
      {
        for(T location : row)
        {
          if( location.equals(item))
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
    @SuppressWarnings("unchecked")
    public T[] getKind(T item)
    {
      ArrayList<T> result = new ArrayList<T>();
      for(T[] row : locations)
      {
        for(T location : row)
        {
          if( location.equals(item))
          {
            result.add(location);
          }
        }
      }
      return result.toArray((T[])(Array.newInstance(this.classT, result.size())));
    }

    //
    // getRandomEmpty
    // Info...
    //
    public Location getRandomEmpty() {
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
  }
}