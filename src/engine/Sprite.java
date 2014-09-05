package engine;
import java.io.File;
import java.awt.Point;
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
  // Animation
  // Info..
  //
  public static class Animation
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
    public Animation(long length, String path, boolean doFlipHorzontally) throws Exception
    {
      this.length = length;
      try
      {
        for(int i = 0;;i++)
        {
          frames.add(loadImage(path + i, doFlipHorzontally, 0));
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
    // Constants
    //
    private static final String PATH_BOUNDING = "bounding";

    //
    // Variables
    //
    private EnumMap<TileSet, EnumMap<Position.Direction, Animation>> animations;
    private Point bounding;
    private Clip audio;
    private long length;
    
    //
    // Constructor
    // Info..
    //
    public Action(String path, String pathAudio) throws Exception
    {
      //Action(path, audio.)
    }
    public Action(String path, long length) throws Exception
    {
      this.length = length;
      animations = new EnumMap<TileSet, EnumMap<Position.Direction, Animation>>(TileSet.class);
      for(TileSet tileSet : TileSet.values())
      {
    	EnumMap<Position.Direction, Animation> currentAnimations =
    	  new EnumMap <Position.Direction, Animation>(Position.Direction.class);
        for(Position.Direction direction : Position.Direction.values())
        {
          if(direction == Position.Direction.WEST)
          {
            currentAnimations.put(direction, new Animation
            (
              length,                                                  // length
              path + Position.Direction.EAST.toString().toLowerCase(), // path
              true                                                     // doFLipHorizontally
            ));
          }
          else
          {
        	currentAnimations.put(direction, new Animation
            (
              length,                                    // length
              path + direction.toString().toLowerCase(), // path
              false                                      // doFLipHorizontally
            ));
          }
        }
        animations.put(tileSet, currentAnimations);
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
    public Animation getAnimation(TileSet tileSet, Position.Direction direction)
    {
      return this.animations.get(tileSet).get(direction);
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
    public Dimension getDimension(TileSet tileSet)
    {
      return animations.get(tileSet).get(0).getDimension();
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
*/
}