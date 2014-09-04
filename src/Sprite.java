import java.io.File;
import java.awt.Dimension;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.AffineTransformOp;
import java.util.ArrayList;
import java.util.EnumMap;
import javax.imageio.ImageIO;
import javax.sound.sampled.Line;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioSystem;

//
// Sprite
// Info..
//
public class Sprite
{

  //
  // Constants
  //
  private static final String PATH_ART   = "art";
  private static final String PATH_IMAGE = ".png";
  private static final String PATH_AUDIO = ".ogg"; 

  //
  // loadSound
  // Info...
  //
  private static Clip loadSound(String path) throws Exception
  {
    Clip sound = null;
    sound = (Clip)AudioSystem.getLine(new Line.Info(Clip.class));
    sound.open(AudioSystem.getAudioInputStream(new File(path + PATH_AUDIO)));
    return sound;
  }

  //
  // loadImage
  // Info...
  //
  private static BufferedImage loadImage
  (
    String path,
    boolean doFlipHorzontally,
    int degreeRotation
  ) 
  throws Exception
  {
    BufferedImage result = null;
    result = ImageIO.read(new File(path + PATH_IMAGE));
    if(doFlipHorzontally)
    {
      AffineTransform tranformation = AffineTransform.getScaleInstance(-1, 1);
      tranformation.translate(-result.getWidth(), 0);
      result = new AffineTransformOp(
          tranformation,
        AffineTransformOp.TYPE_NEAREST_NEIGHBOR
      ).filter(result, null);
    }
    return result;
  }

  //
  // TileSet
  // Info...
  //
  public enum TileSet
  {

    //
    // Values
    //
    SMALL(64),
    LARGE(128);

    //
    // Variables
    //
    private int size;

    // 
    // Constructor
    //
    private TileSet(int size)
    {
      this.size = size;
    }

    //
    // getSize
    // Info..
    //
    public int getSize()
    {
      return size;
    }
    
    public Dimension toDimension()
    {
      return new Dimension(this.size, this.size);
    }
  }

  //
  // ActionState
  // Info...
  //
  public enum ActionState
  {

    //
    // Values
    //
    DIE,
    LOB,
    MOVE,
    STAND,
    MELEE;
  }

  //
  // Animation
  // Info..
  //
  private class Animation
  {

    //
    // Variables
    //
    private long length;
    private ArrayList<BufferedImage> frames;
    
    //
    // Constructor
    // Info...
    //
    public Animation(long length, String path, boolean doFlipHorzontally)
    {
      this.length = length;
      try
      {
        for(int i = 0;;i++)
        {
          frames.add(loadImage(path + i, doFlipHorzontally));
        }
      }
      catch(Exception error)
      {
        if(frames.size() < 1)
        {
          throw error;
        }
      }
    }

    //
    // getFrame
    // Info..
    //
    public BufferedImage getFrame(int number)
    {
      return frames.get(number);
    }

    //
    // getFrameAtDeltaTime
    // Info..
    //
    public BufferedImage getFrameAtDeltaTime(long deltaTime)
    {
      long timeFromStart = Math.abs(deltaTime) % this.length;
      int frame = (int)(this.length / timeFromStart);
      return getFrame(frame);
    }
    
    //
    // getFrameAtPercentage
    // Info..
    //
    public BufferedImage getFrameAtPercentage(int percentage)
    {
      long adjusted = Math.abs(percentage) % 100;
      int frame = (int)((this.frames.size()/100)*adjusted);
      return getFrame(frame);
    }

    //
    // getNumberOfFrames
    // Info...
    //
    public int getNumberOfFrames()
    {
      return frames.size();
    }

    //
    // getDimension
    // Info...
    //
    public Dimension getDimension()
    {
      return new Dimension(frames.get(0).getWidth(), frames.get(0).getHeight());
    }
    
    //
    // getLength
    // Info..
    //
    public long getLength()
    {
      return this.length;
    }
  }
  
  //
  // Action
  // Info..
  //
  public class Action
  {

    //
    // Variables
    //
    private EnumMap<Position.Direction, Animation> animations;
    private long length;
    private Clip audio;
    
    //
    // Constructor
    // Info..
    //
    public Action(String path, String pathAudio)
    {
      //Action(path, audio.)
    }
    public Action(String path, long length)
    {
      this.length = length;
      this.animations = new EnumMap<Position.Direction, Animation>(Position.Direction.class);
      for(Position.Direction direction : Position.Direction.values())
      {
        if(direction == Position.Direction.WEST)
        {
          this.animations.put(direction, new Animation
          (
            length,                                                  // length
            path + Position.Direction.EAST.toString().toLowerCase(), // path
            true                                                     // doFLipHorizontally
          ));
        }
        else
        {
          this.animations.put(direction, new Animation
          (
            length,                                    // length
            path + direction.toString().toLowerCase(), // path
            false                                      // doFLipHorizontally
          ));
        }
      }
      // Assure the animations are the same dimensions
    }
    
    //
    // getLength
    // Info..
    //
    public long getLength()
    {
      return this.length;
    }
    
    //
    // getAnimation
    // Info..
    //
    public Animation getAnimation(Position.Direction direction)
    {
      return this.animations.get(direction);
    }

    //
    // playAudio
    // Info...
    //
    public void playAudio()
    {
      if(audio != null)
      {
        
      }
    }

    //
    // getDimension
    // Info..
    //
    public Dimension getDimension()
    {
      return animations.get(0).getDimension();
    }
  }
  
  //
  // Base
  // Info..
  //
  public class Base
  {

    //
    // Constants
    //
    private static final String PATH_BOUNDING = "bounding";

    //
    // Variables
    //
    private EnumMap<TileSet, EnumMap<Position.Direction, BufferedImage>> pose;
    private EnumMap<TileSet, EnumMap<ActionState, Action>> actions;
    private ActionState currentState;
    private String path;
    private Point bounding;

    //
    // Constructor
    // Info..
    //
    public Base(String path)
    {
      //load bounding
    }

    //
    // addAction
    // Info..
    //
    public void addAction(ActionState actionState)
    {
      //actions.put(new Action)
      // Assure action is the right size
    }
  }
  /*

  //
  // Tiler
  // Info..
  //
  public class Tiler
  {
    
    // 
    // Variables
    //
    

  }

  //
  // Link
  // Info..
  //
  public static class Link extends Base
  {

    //
    // Constructor
    // Info..
    //
    public Link
    {
      super("Link");
      addAction(ActionState.DIE);
      addAction(ActionState.MOVE);
      addAction(ActionState.STAND);
      addAction(ActionState.MELEE);
      addAction(ActionState.LOB);
    }
  }
  */
}