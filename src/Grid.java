import java.awt.*;
public class Grid<T>{
  public static final int NORTH = 0;
  public static final int EAST = 90;
  public static final int WEST = 270;
  public static final int SOUTH = 180;
  public static final int AHEAD = 0;
  public static final int LEFT = -90;
  public static final int RIGHT = 90;
  public static final int NORTHEAST = 45;
  public static final int SOUTHEAST = 135;
  public static final int SOUTHWEST = 225;
  public static final int NORTHWEST = 315;
  public static final int HALF_LEFT = -45;
  public static final int HALF_RIGHT = 45;
  public static final int FULL_CIRCLE = 360;
  public static final int HALF_CIRCLE = 180;
  public static int getRandomDirection(){
    switch(new Random().nextInt(4)){
      case 1:
        return NORTH;
      case 2:
        return EAST;
      case 3:
        return WEST;
      case 4:
        return SOUTH;
    }
    return NORTH; 
  }
  public class Location{
    private int x = 1; 
    private int y = 1;
    public Location(int x, int y){
      this.x = x; this.y = y;
    }
    public boolean equals(Location location){
      return location.getX() == getX() && location.getY() == getY();
    }
    public int getX(){
      return x;
    }
    public int getY(){
      return y;
    }
    public double getDistance(Location location){
      return Math.abs(Math.sqrt(
        Math.pow((double)(location.getX() - getX()), 2) +
        Math.pow((double)(location.getY() - getY()), 2)));
    }
    public Location getAdjacent(int direction){
      int x = 0;
      int y = 0;
      int adjustedDirection = (direction + HALF_RIGHT / 2) % FULL_CIRCLE;
      if(adjustedDirection < 0)
        adjustedDirection += FULL_CIRCLE;
      adjustedDirection = (adjustedDirection / HALF_RIGHT) * HALF_RIGHT;
      switch(adjustedDirection){
        case SOUTHEAST: 
          y =  1; 
        case EAST:      
          x =  1;
          break;
        case SOUTHWEST:
          x = -1;
        case SOUTH:     
          y =  1;
          break;
        case NORTHWEST:
          y = -1; 
        case WEST:     
          x = -1;
          break;
        case NORTHEAST: 
          x =  1; 
        case NORTH:     
          y = -1;
      }
      return new Location(getX() + x, getY() + y);
    }
  }
  Object[][] locations;
  public Grid(){
    if(ROWS <= 0 || COLUMNS <= 0){
      throw new IllegalArgumentException();
    }
    locations = new Object[COLUMNS][ROWS];
  }
  public void add(Location location, T something){
    locations[location.getX()][location.getY()] = something;
  }
  public void remove(Location location){
    locations[location.getX()][location.getY()] = null;
  }
  public boolean isInvalid(Location location){
    if(location.getX() < 0 || location.getX() >= getNumberOfRows() ||
    location.getY() < 0 || location.getY() >= getNumberOfColumns()){
      return true;
    }
    return false;
  }
  public T get(Location location){
    return (T)locations[location.getX()][location.getY()];
  }
  public int getNumberOfRows(){
    return locations.length;
  }
  public int getNumberOfColumns(){
    return locations[0].length;
  }
  public int getCount(Class object){
    int result = 0;
    for(Object objectb : getAll()){
      if(objectb.getClass() == object){
        result++;
      }
    }
    return result;
  }
  public ArrayList<T> getKind(Class object){
    ArrayList<T> result = new ArrayList<T>();
    for(Object objectb : getAll()){
      if(objectb.getClass() == object){
        result.add((T)objectb);
      }
    }
    return result;
  }
  public Object getInstance(Object object){
    for(Object objectb : getAll()){
      if(objectb.getClass() == object.getClass()){
        return objectb;
      }
    }
    return null;
  }
  public ArrayList<T> getAll(){
    ArrayList<T> result = new ArrayList<T>();
    for(int x = 0;x < ROWS;x++){
      for(int y = 0;y < COLUMNS;y++){
        if(get(new Location(x, y)) != null){
          result.add(get(new Location(x, y)));
        }
      }
    }
    return result;
  }
  public Location getRandomEmpty(){
    ArrayList<Location> emptySpots = new ArrayList<Location>();
    for(int x = 0;x < ROWS;x++){
      for(int y = 0;y < COLUMNS;y++){
        try{
          if(get(new Location(x, y)) == null){
            emptySpots.add(new Location(x, y));
          }
        }
        catch(Exception error){
          // Do nothing...
        }
      }
    }
    if(emptySpots.size() == 0){
      throw new IllegalArgumentException();
    }
    return emptySpots.get(new Random().nextInt(emptySpots.size()));
  }
}