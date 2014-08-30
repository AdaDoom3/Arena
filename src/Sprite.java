public static class Sprite{
  public class Animation{

  }
  public class Base{
    private BufferedImage image; 
    private Location location = null; 
    private Grid<Sprite> level = null; 
    private int health = 100;
    private int duration = 0;
    private int direction = 0;  
    public void die(){
      // For overriding...
    }
    public BufferedImage getImage(){
      return image;
    }
    public Location getLocation(){
      return location;
    }
    public Grid<Sprite> getGrid(){
      return level;
    }
    public int getDirection(){
      return direction;
    }
    public int getHealth(){
      return health;
    }
    public void damage(int amount){
      if(health > 0){
        health = health - amount;
        if(health <= 0){
          remove();
        }
      }
    }
    public void setHealth(int amount){
      if(amount < 0 || isInvincible()){
        throw new IllegalArgumentException();
      }
      health = amount;
    }
    public void setDirection(int direction){
      this.direction = direction % FULL_CIRCLE;
      if(direction < 0){
        this.direction += FULL_CIRCLE;
      }
    }
    public void remove(){
      if(getGrid() != null && getGrid().get(getLocation()) != this){
        throw new IllegalStateException();
      }
      getGrid().remove(getLocation());
      die();
      level = null;
      location = null;
    }
    public int getDirectionToward(Location target){
      int dx = target.getX() - getLocation().getX();
      int dy = target.getY() - getLocation().getY();
      int angle = (int)Math.toDegrees(Math.atan2(-dy, dx));
      int compassAngle = RIGHT - angle;
      compassAngle += HALF_RIGHT / 2;
      if(compassAngle < 0){
        compassAngle += FULL_CIRCLE;
      }
      return (compassAngle / HALF_RIGHT) * HALF_RIGHT;
    }
    public void put(Grid<Sprite> level, Location location){
      if(level != null && level.isInvalid(location)){
        throw new IllegalStateException();
      }
      Sprite sprite = level.get(location);
      if(sprite != null){
        sprite.remove();
      }
      level.add(location, this);
      this.location = location;
      this.level = level;
      if(image == null){
        loadImage();
      }
    }
    private void loadImage(){
      try{
        this.image = ImageIO.read(new File(getClass().getSimpleName().toLowerCase() + EXTENSION_IMAGE));
      }
      catch(Exception error){
        try{
          this.image = ImageIO.read(new File(getClass().getName().toLowerCase() + EXTENSION_IMAGE));
        }
        catch(Exception error2){
          // Do nothing...
        }
      }
    }
    public void put(Grid<Sprite> level){
      put(level, level.getRandomEmpty());
    }
    public Location getAdjacent(int direction){
      Location result = getLocation().getAdjacent(direction);
      if(getGrid().isInvalid(result)){
        throw new IllegalStateException();
      }
      return result;
    }
    public Location getAdjacent(){
      return getAdjacent(getDirection());
    }
  }
  public class Moving extends Base{
    public boolean canMove(Location location){
      if(getGrid().get(location) != null || getGrid().isInvalid(location)){
        return false;
      }
      return true;
    }
    public void move(Location location){
      if(getGrid() == null || getGrid().isInvalid(location)){
        throw new IllegalStateException();
      }
      getGrid().remove(getLocation());
      if(getGrid().get(location) != null){
        getGrid().get(location).remove();
      }
      put(getGrid(), location);
      this.location = location;
    }
  }
  public static class Bullet extends Moving{
    public final int BULLET_TIMING = getConstant("bullet.timing");
    public final String BULLET_SHOOT = getConstantString("bullet.shoot");
    private class BulletExplosion extends Sprite{}
    Long lastMove = System.currentTimeMillis() - BULLET_TIMING;
    boolean isFirstTime = true;
    Sprite owner = null; public Sprite getOwner(){return owner;}
    public Bullet(int direction){
      setDirection(direction);
    }
    public void put(Grid<Sprite> level, Location location){
      if(isFirstTime){
        //playSound(BULLET_SHOOT);
        isFirstTime = false;
      }
      super.put(level, location);
      owner = getGrid().get(getAdjacent(HALF_CIRCLE + getDirection()));
    }
    public void explode(){
      BulletExplosion explosion = new BulletExplosion();
      Grid<Sprite> level2 = getGrid();
      Location location = getLocation();
      explosion.setDirection(getDirection());
      remove();
      explosion.put(level2, location);
      explosion.fade(3);
    }
    public void act(){
      if(lastMove + BULLET_TIMING < System.currentTimeMillis()){
        Location nextLocation = null;
        try{
          nextLocation = getAdjacent();
          Sprite sprite = getGrid().get(nextLocation);
          if(sprite instanceof Bullet || sprite instanceof BulletExplosion || sprite == null){
            move(nextLocation);
            lastMove = System.currentTimeMillis();
          }else{
            if(!sprite.isInvincible()){
              playSound(SOUND_EXPLODE_BULLET);
              sprite.damage(1);
            }
            explode();
          }
        }catch(Exception error){explode();}
      }
    }
  }
  public class Attacking extends Moving{
  }
  public static class Bot extends Sprite{
    public final int BOT_TIMING_MOVE = getConstant("bot.timing.move");
    public final int BOT_TIMING_SHOOT = getConstant("bot.timing.shoot");
    public final int START_HEALTH = getConstant("bot.health");
    private boolean canMove = false;
    private boolean canShoot = false;
    private Long currentMove = System.currentTimeMillis() - BOT_TIMING_MOVE;
    private Long currentShoot = System.currentTimeMillis() - BOT_TIMING_SHOOT;
    public Bot(){
      setHealth(START_HEALTH);
    }
    public void die(){
      playSound(SOUND_EXPLODE_PLAYER);
    }
    private boolean attack(){
      Location current;
      int range;
      for(Sprite sprite : getGrid().getKind(Player.class))
        for(int i = (sprite.getDirection() + RIGHT) % FULL_CIRCLE;;i = (i + RIGHT) % FULL_CIRCLE){
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
  }
  public class Player extends Attacking{
    public final int START_HEALTH = getConstant("player.health");
    private ArrayList<String> moveKeys = new ArrayList<String>();
    private ArrayList<Boolean> state = new ArrayList<Boolean>();
    private String shootKey;
    private String previous;
    private KeyEventDispatcher dispatcher;
    private int id; public int getID(){return id;}
    public Player(){}
    public Player(int id){
      this.id = id;
      moveKeys.add(getConstantString("player." + id + ".up").toUpperCase()); state.add(false);
      moveKeys.add(getConstantString("player." + id + ".right").toUpperCase()); state.add(false);
      moveKeys.add(getConstantString("player." + id + ".down").toUpperCase()); state.add(false);
      moveKeys.add(getConstantString("player." + id + ".left").toUpperCase()); state.add(false);
      shootKey = getConstantString("player." + id + ".shoot").toUpperCase(); state.add(false);
      setHealth(START_HEALTH);
      try{
        color = (Color)Color.class.getField(getConstantString(
          "player." + id + ".color").toUpperCase()).get(null);
      }catch(Exception error){}
      dispatcher = new KeyEventDispatcher(){
        public boolean dispatchKeyEvent(KeyEvent event){
          String key = KeyStroke.getKeyStrokeForEvent(event).toString();
          if(!key.equals("pressed " + previous))
          for(int i,k = i = 0;i < moveKeys.size();i++){
            if(key.equals("pressed " + moveKeys.get(i))){
              if(!state.get(i)){
                state.set(i, true);
                Location next = null;
                try{next = getAdjacent(k);}catch(Exception error){}
                if(next != null && !getGrid().isInvalid(next) &&
                !(getGrid().get(next) instanceof Sprite)) move(next);
                setDirection(k);
              }
            }else if(key.equals("released " + moveKeys.get(i))) state.set(i, false);
            k = (RIGHT + k) % FULL_CIRCLE;
          }
          if(key.equals("pressed " + shootKey)){
            if(!state.get(4)){ 
              state.set(4, true);
              try{
                Location location = getAdjacent();
                if(!(getGrid().get(location) instanceof Sprite) && !getGrid().isInvalid(location))
                  new Bullet(getDirection()).put(getGrid(), location);
              }catch(Exception error){}
            }
          }else if(key.equals("released " + shootKey)) state.set(4, false);
          return false;
        }
      };
      KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(dispatcher);
    }
    public void die(){
      playSound(SOUND_EXPLODE_PLAYER);
      KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(dispatcher);
    }
  }
}