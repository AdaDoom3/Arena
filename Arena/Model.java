import java.util.HashMap;
import java.lang.reflect.Array;

//
// Model
// Info..
//
public class Model 
{

  //
  // Constants
  //
  static final Sprite.Information[] defaultInfomrations = new Sprite.Information[]
  {
    new Sprite.Information("stand")
  };

  //
  // Variables
  //
  static HashMap<String, Sprite.Actions> classActs = new HashMap<String, Sprite.Actions>();
  Sprite.Actions generic = new Sprite.Actions("generic", new Sprite.Information[]
  {
    new Sprite.Information("die")
  });

  //
  // Entity
  // Info..
  //
  public static class Entity
  {

    //
    // Variables
    //
    Position.Direction direction;
    Sprite.Actions actions;
    
    private <T> T[] concatenate (T[] A, T[] B) {
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
    public Entity(Sprite.Information[] informations)
    {
      if(!classActs.containsKey(getClass().getName()))
      {
        classActs.put
        (
          getClass().getSimpleName(),
          new Sprite.Actions(getClass().getSimpleName(), concatenate(defaultInfomrations, informations))
        );
      }
      actions = classActs.get(getClass().getName());
    }
    public Entity()
    {
      this(new Sprite.Information[]{});
    }
    
    //
    // getDirection
    // Info..
    //
    public Position.Direction getDirection()
    {
      return direction;
    }
    
    //
    // setDirection
    // Info..
    //
    public void setDirection(Position.Direction item)
    {
      this.direction = item;
    }
    
    //
    // animate
    // Info..
    //
    public void animate(String action)
    {
      //return this.actions.getAnimations(action)
    }

    //
    // move
    // Info..
    //
    public void move(Position.Location location, Position.Direction direction, String action)
    {
      //Position.Location destination = location.getAdjacent(direction);
      animate(action);
    }
    
    //
    // step
    // Info..
    //
    public void step()
    {
      //if(!is)
    }
  }
}