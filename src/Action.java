import java.util.EnumMap;


public class Action {
  private EnumMap<Position.Direction, Animation> animations;
  private long                                   length;
  
  public Action(long length, String path){
    this.length     = length;
    this.animations = new EnumMap<Position.Direction, Animation>(Position.Direction.class);
    
    for(Position.Direction direction : Position.Direction.values()){
      try{
        switch(direction){
          case NORTH:
            this.animations.put(direction, new Animation(length, path+Position.Direction.NORTH.toString().toLowerCase(), false));
            break;
          case SOUTH:
            this.animations.put(direction, new Animation(length, path+Position.Direction.SOUTH.toString().toLowerCase(), false));
            break;
          case EAST:
            this.animations.put(direction, new Animation(length, path+Position.Direction.EAST.toString().toLowerCase(), false));
            break;
          case WEST:
            this.animations.put(direction, new Animation(length, path+Position.Direction.EAST.toString().toLowerCase(), true));
            break;
          default:
            this.animations.put(direction, new Animation(length, path+Position.Direction.NORTH.toString().toLowerCase(), false));
            break;
        }
      } catch(Exception e){
        if(this.animations.size() != 4){
          throw e;
        }
      }
    }
  }
  
  public long getLength(){
    return this.length;
  }
  
  public Animation getAnimation(Position.Direction direction){
    return this.animations.get(direction);
  }
}
