import java.util.ArrayList;

//
// Model
// Info..
//
public class Model
{

  //
  // Base
  // Info..
  //
  public static class Base
  {

    //
    // Variables
    //
    private Position.Location location; 
    private Position.Location locationNext;
    private Position.Grid<Model.Base> grid; 
    private ArrayList actions;
    private int health;
    private Position.Direction direction; 
    private long progress;

    //
    // Constructor
    // Info...
    //
    public Base(Position.Grid<Model.Base> grid, long progress)
    {
      health = 100;
      this.grid = grid;
      this.progress = progress;
    }

    //
    // getLocation
    // Info..
    //
    public Position.Location getLocation()
    {
      return location;
    }

    //
    // getGrid
    // Info...
    //
    public Position.Grid<Model.Base> getGrid()
    {
      return grid;
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
    // getHealth
    // Info..
    //
    public int getHealth()
    {
      return health;
    }

    //
    // damage
    // Info..
    //
    public void damage(int amount)
    {
      if(health > 0)
      {
        health = health - amount;
        if(health <= 0)
        {
          remove();
        }
      }
    }

    //
    // setHealth
    // Info..
    //
    public void setHealth(int amount)
    {
      if(amount < 0)
      {
        throw new IllegalArgumentException();
      }
      health = amount;
    }

    //
    // setDirection
    // Info...
    //
    public void setDirection(Position.Direction direction)
    {
      this.direction = direction;
    }

    //
    // remove
    // Info...
    //
    public void remove()
    {
      if(getGrid() != null && getGrid().get(getLocation()) != this)
      {
        throw new IllegalStateException();
      }
      getGrid().remove(getLocation());
      die();
      grid = null;
      location = null;
    }

    //
    // getDegreesToward
    // Info..
    //
    public int getDegreesToward(Position.Location target)
    {
      int dx = target.x - getLocation().x;
      int dy = target.y - getLocation().y;
      int angle = (int)Math.toDegrees(Math.atan2(-dy, dx));
      /* OOP 
      int compassAngle = RIGHT - angle;
      compassAngle += HALF_RIGHT / 2;
      if(compassAngle < 0)
      {
        compassAngle += FULL_CIRCLE;
      }
      return (compassAngle / HALF_RIGHT) * HALF_RIGHT;*/
      return 0;
    }

    //
    // put
    // Info..
    //
    public void put(Position.Grid<Model.Base> grid, Position.Location location)
    {
      if(grid != null && grid.isInvalid(location))
      {
        throw new IllegalStateException();
      }
      Model.Base model = grid.get(location);
      if(model != null)
      {
        model.remove();
      }
      grid.add(location, this);
      this.location = location;
      this.grid = grid;
    }
    public void put(Position.Grid<Model.Base> grid)
    {
      put(grid, grid.getRandomEmpty());
    }

    //
    // getAdjacent
    // Info...
    //
    public Position.Location getAdjacent(Position.Direction direction)
    {
      Position.Location result = location.getAdjacent(direction);
      if(grid.isInvalid(result))
      {
        throw new IllegalStateException();
      }
      return result;
    }
    public Position.Location getAdjacent()
    {
      return getAdjacent(direction);
    }

    // 
    // canMove
    // Info..
    //
    public boolean canMove(Position.Location location)
    {
      if(grid.get(location) != null || grid.isInvalid(location))
      {
        return false;
      }
      return true;
    }

    //
    // move
    // Info..
    //
    public void move(Position.Location location)
    {
      if(grid == null || grid.isInvalid(location))
      {
        throw new IllegalStateException();
      }
      grid.remove(getLocation());
      if(grid.get(location) != null)
      {
        grid.get(location).remove();
      }
      put(grid, location);
      this.location = location;
    }

    //
    // die
    // Info...
    //
    public void die()
    {
      // For overriding...
    }
  }
/*
  //
  // Bullet
  // Info...
  //
  public static class Bullet extends Base
  {

    //
    // Variables
    // 
    Long lastMove = System.currentTimeMillis() - BULLET_TIMING;
    boolean isFirstTime = true;
    Model.Base owner = null;

    //
    // getOwner
    // Info..
    //
    public Sprite getOwner()
    {
      return owner;
    }

    //
    // Constructor
    // Info..
    //
    public Bullet(int direction)
    {
      setDirection(direction);
    }

    //
    // Put
    // Info..
    //
    public void put(Grid<Sprite> level, Location location)
    {
      if(isFirstTime)
      {
        //playSound(BULLET_SHOOT);
        isFirstTime = false;
      }
      super.put(level, location);
      owner = getGrid().get(getAdjacent(HALF_CIRCLE + getDirection()));
    }

    //
    // explode
    // Info..
    //
    public void explode()
    {
      BulletExplosion explosion = new BulletExplosion();
      Grid<Sprite> level2 = getGrid();
      Location location = getLocation();
      explosion.setDirection(getDirection());
      remove();
      explosion.put(level2, location);
      explosion.fade(3);
    }

    //
    // act
    // Info..
    //
    public void act()
    {
      if(lastMove + BULLET_TIMING < System.currentTimeMillis())
      {
        Location nextLocation = null;
        try
        {
          nextLocation = getAdjacent();
          Sprite sprite = getGrid().get(nextLocation);
          if(sprite instanceof Bullet || sprite instanceof BulletExplosion || sprite == null)
          {
            move(nextLocation);
            lastMove = System.currentTimeMillis();
          }
          else
          {
            if(!sprite.isInvincible())
            {
              playSound(SOUND_EXPLODE_BULLET);
              sprite.damage(1);
            }
            explode();
          }
        }
        catch(Exception error)
        {
          explode();
        }
      }
    }
  }

  //
  // Player
  // Info..
  //
  public class Player extends Base
  {
    public final int START_HEALTH = getConstant("player.health");
    private ArrayList<String> moveKeys = new ArrayList<String>();
    private ArrayList<Boolean> state = new ArrayList<Boolean>();
    private String shootKey;
    private String previous;
    private KeyEventDispatcher dispatcher;
    private int id;
    public int getID()
    {
      return id;
    }
    public Player(int id)
    {
      this.id = id;
      moveKeys.add(getConstantString("player." + id + ".up").toUpperCase()); state.add(false);
      moveKeys.add(getConstantString("player." + id + ".right").toUpperCase()); state.add(false);
      moveKeys.add(getConstantString("player." + id + ".down").toUpperCase()); state.add(false);
      moveKeys.add(getConstantString("player." + id + ".left").toUpperCase()); state.add(false);
      shootKey = getConstantString("player." + id + ".shoot").toUpperCase(); state.add(false);
      setHealth(START_HEALTH);
      try
      {
        color = (Color)Color.class.getField(getConstantString(
          "player." + id + ".color").toUpperCase()).get(null);
      }
      catch(Exception error)
      {
        // Do nothing...
      }
      dispatcher = new KeyEventDispatcher()
      {
        public boolean dispatchKeyEvent(KeyEvent event)
        {
          String key = KeyStroke.getKeyStrokeForEvent(event).toString();
          if(!key.equals("pressed " + previous))
          {
            for(int i,k = i = 0;i < moveKeys.size();i++)
            {
              if(key.equals("pressed " + moveKeys.get(i)))
              {
                if(!state.get(i))
                {
                  state.set(i, true);
                  Location next = null;
                  try
                  {
                    next = getAdjacent(k);
                  }
                  catch(Exception error)
                  {

                  }
                  if(next != null && !getGrid().isInvalid(next)
                  && !(getGrid().get(next) instanceof Sprite))
                  {
                    move(next);
                  }
                  setDirection(k);
                }
              }
              else if(key.equals("released " + moveKeys.get(i)))
              {
                state.set(i, false);
              }
              k = (RIGHT + k) % FULL_CIRCLE;
            }
          if(key.equals("pressed " + shootKey))
          {
            if(!state.get(4))
            { 
              state.set(4, true);
              try
              {
                Location location = getAdjacent();
                if(!(getGrid().get(location) instanceof Sprite) && !getGrid().isInvalid(location))
                {
                  new Bullet(getDirection()).put(getGrid(), location);
                }
              }
              catch(Exception error)
              {
                // Do nothing...
              }
            }
          }
          else if(key.equals("released " + shootKey))
          {
            state.set(4, false);
          }
          return false;
        }
      };
      KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(dispatcher);
    }
    public void die()
    {
      playSound(SOUND_EXPLODE_PLAYER);
      KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(dispatcher);
    }
  }
  public static class Bot extends Sprite
  {
    public final int BOT_TIMING_MOVE = getConstant("bot.timing.move");
    public final int BOT_TIMING_SHOOT = getConstant("bot.timing.shoot");
    public final int START_HEALTH = getConstant("bot.health");
    private boolean canMove = false;
    private boolean canShoot = false;
    private Long currentMove = System.currentTimeMillis() - BOT_TIMING_MOVE;
    private Long currentShoot = System.currentTimeMillis() - BOT_TIMING_SHOOT;
    public Bot()
    {
      setHealth(START_HEALTH);
    }
    public void die()
    {
      playSound(SOUND_EXPLODE_PLAYER);
    }
    private boolean attack()
    {
      Location current;
      int range;
      for(Sprite sprite : getGrid().getKind(Player.class))
        for(int i = (sprite.getDirection() + RIGHT) % FULL_CIRCLE;;i = (i + RIGHT) % FULL_CIRCLE)
        {
          current = sprite.getLocation();
          range = 0;
          try{
            while(true){
              current = current.getAdjacent(i);
              range++;
              if(range > 1 && getLocation().equals(current)){
                if(getDirection() == getDirectionToward(sprite.getLocation())){
                  if(canShoot){
                    new Bullet(getDirection()).put(getGrid(), getAdjacent());
                    return true;
                  }
                }else if(range > 2){
                  if(canMove){
                    setDirection(getDirectionToward(sprite.getLocation()));
                    move(getAdjacent());
                  }
                  return true;
                }
              }else if(getGrid().get(current) != null) throw new Exception();
            }
          }catch(Exception error){}
          if(i == sprite.getDirection()) break;
        }
      return false;
    }
    private boolean moveToAttack(){
      if(canMove){
        double least = ROWS + 0.0;
        Location closest = null;
        Location next = null;
        Location current;
        int range;
        for(Sprite sprite : getGrid().getKind(Player.class))
          for(int i = (sprite.getDirection() + RIGHT) % FULL_CIRCLE;
          i!= sprite.getDirection();i = (i + RIGHT) % FULL_CIRCLE){
            current = sprite.getLocation();
            range = 0;
            try{
              while(true){
                current = current.getAdjacent(i);
                if(getGrid().get(current) != this && getGrid().get(current) != null)
                  throw new Exception();
                range++;
                if(range > 2 && current.getDistance(getLocation()) < least){
                  least = current.getDistance(getLocation());
                  closest = current;
                }
              }
            }catch(Exception error){}
          }
        if(closest != null){
          least = ROWS + 0.0;
          for(int i = NORTH;i != FULL_CIRCLE;i += HALF_RIGHT)
            try{
              if(getGrid().get(getAdjacent(i)) == null && closest.getDistance(getAdjacent(i)) < least){
                least = closest.getDistance(getAdjacent(i));
                next = getAdjacent(i);
              }
            }catch(Exception error){}
          if(next != null){
            if((getDirectionToward(next) % RIGHT) != 0)
              setDirection(getDirectionToward(next) - HALF_RIGHT);
            else setDirection(getDirectionToward(next));
            move(next);
            return true;
          }
        }
      }
      return false;
    }
    public void act(){
      if(currentMove + BOT_TIMING_MOVE < System.currentTimeMillis()){
        canMove = true;
        currentMove = System.currentTimeMillis();
      }
      if(currentShoot + BOT_TIMING_SHOOT < System.currentTimeMillis()){
        canShoot = true;
        currentShoot = System.currentTimeMillis();
      } 
      if(!attack()) moveToAttack();
      canMove = false;
      canShoot = false;
    }
  }*/
}