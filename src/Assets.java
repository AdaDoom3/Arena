import java.io.File;
import java.util.EnumMap;
import java.util.ArrayList;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.AffineTransformOp;
import javax.imageio.ImageIO;
import javax.sound.sampled.Line;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioSystem;
public class Assets
{

  //
  // Constants
  //
  private static final String PATH_IMAGE = ".png";
  private static final String PATH_AUDIO = ".ogg"; 

  //
  // loadSound
  // Info...
  //
  private static Clip loadSound(String path)
  {
    Clip sound = (Clip)AudioSystem.getLine(new Line.Info(Clip.class));
    sound.open(AudioSystem.getAudioInputStream(new File(path + PATH_AUDIO)));
    return sound;
  }

  //
  // loadImage
  // Info...
  //
  private static BufferedImage loadImage(String path, boolean doFlipHorzontally)
  {
    BufferedImage result = ImageIO.read(new File(path + PATH_IMAGE));
    if(doFlipHorzontally)
    {
      result = new AffineTransformOp
      (
        AffineTransform.getScaleInstance(-1, 1).translate(-result.getWidth(null), 0),
        AffineTransformOp.TYPE_NEAREST_NEIGHBOR
      ).filter(result, null);
    }
    return result;
  }

  //
  // Animation
  // Info...
  //
  private class Animation
  {

    //
    // Variables
    //
    private ArrayList<BufferedImage> frames;

    //
    // Constructor
    // Info...
    //
    public Animation(String path, boolean doFlipHorzontally)
    {
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
    // getFrameAtPercent
    // Info..
    //
    public BufferedImage getFrameAtPercent(int percent)
    {
      return frames.get(percent);
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
  }

  //
  // Sprite
  // Info..
  //
  public static class Sprite
  {

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
      private TileSet(int site)
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
    }
      
    //
    // Action
    // Info...
    //
    public enum Action
    {

      //
      // Values
      //
      TILE,
      DIE,
      STAND,
      WALK,
      MELEE,
      LOB;
    }

    //
    // Sliding
    // Info..
    //
    public class Sliding
    {

      //
      // Variables
      // Info..
      //
      private EnumMap<TileSet, EnumMap<Position.Direction, BufferedImage>> poses;

      //
      // Constructor
      // Info...
      //
      public Sliding(String path)
      {
        poses = new EnumMap<TileSet, EnumMap<Position.Direction, BufferedImage>>();
        currentPoses = new EnumMap<Position.Direction, BufferedImage>();
        for(TileSet tileSet : TileSet.values())
        {
          currentPoses.put
          (
            Position.Direction.NORTH,
            loadImage(path + tileSet + Action.TILE.name().toLowerCase(), false)
          );
          for(Position.Direction direction : Position.Direction.values())
          {
            if(direction != Position.Direction.NORTH)
            {

            }
          }
          poses.put(tileSet, currentPoses);
        }
      }

      //
      // getImage
      // Info...
      //
      public BufferedImage getImage
      (
        int action,
        int percent,
        Position.Direction direction,
        TileSet tileSet
      ){
        if(action != TILE)
        {
          throw new IllegalArgumentException();
        }
        return poses.get(tileSet).get(action + direction);
      }

    }

    //
    // Positionable
    // Info..
    //
    public class Positionable extends Sliding
    {

      //
      // Variables
      //
      private EnumMap<TileSet, EnumMap<Position.Direction, Animation>> die;
      private EnumMap<TileSet, Point> bounding;
      private Clip audioDie;

      //
      // Constructor
      // Info...
      //
      public Positionable(String path)
      {
        for(TileSet tileSet : TileSet.values())
        {
          die.put(tileSet, new Animation(path + PATH_DIE));
          basePoses = new ArrayList<BufferedImage>();
          basePoses.add(getImage(path + PATH_STAND + PATH_SOUTH), STAND + SOUTH);
          basePoses.add(getImage(path + PATH_STAND + PATH_EAST),  STAND + EAST);
          basePoses.add(getImage(path + PATH_STAND + PATH_NORTH), STAND + NORTH);
          basePoses.add(getImage(path + PATH_STAND + PATH_WEST),  STAND + WEST);
          // Make sure images and death animation are the same size
          BufferedImage bounding = getImage(path + PATH_BOUNDING_POINT);
          // get bounding point
        }
      }

      //
      // getImage
      // Info...
      //
      public BufferedImage getImage
      (
        int action,
        int percent,
        Position.Direction direction,
        TileSet tileSet
      ){
        switch(action)
        {
          case STAND:
            return basePoses[tileSet].get(action + direction);
            break;
          case DIE:
            if(percent == 0 && audioDie != null)
            {
              audioDie.play();
            }
            return die[tileSet].get(action + direction).getFrameAtPercent(percent);
            break;
          default:
            throw new IllegalArgumentException();
            break;
        }
      }

      //
      // getBoundingPoint
      // Info...
      //
      public Point getBoundingPoint()
      {
        return boundingPoint;
      }

      //
      // getDimension
      // Info...
      //
      public Dimension getDimension(TileSet tileSet)
      {
        return new Dimension(pose.get(tileSet).getWidth(), pose.get(tileSet).getHeight());
      }

      //
      // getDieDelay
      // Info...
      //
      public long getDieDelay()
      {
        return audioDie.getMicrosecondLength();
      }
    }

    //
    // Walking
    // Info...
    //
    public class Walking extends Positionable
    {

      //
      // Constants
      //
      private static final String PATH_BOUNDING_POINT = "bounding";

      //
      // Variables
      //
      private EnumMap<TileSet, ArrayList<Animation>> walk;

      //
      // Constructor
      // Info..
      //
      public Walking(String path)
      {
        super(path);
      }

      //
      // getImage
      // Info...
      //
      public BufferedImage getImage
      (
        int action,
        int percent,
        Position.Direction direction,
        TileSet tileSet
      ){
        if(action != WALK)
        {
          return super.getImage(direction, action, percent, tileSet);
        }
        return walk.get(tileSet).get(action + direction).getFrameAtPercent(percent);
      }
    }

    //
    // Attacking
    // Info..
    //
    public class Attacking extends Walking
    {

      //
      // Variables
      //
      private EnumMap<TileSet, EnumMap<Position.Direction, Animation>> melee;
      private EnumMap<TileSet, EnumMap<Position.Direction, Animation>> lob;
      private Clip audioMelee;
      private Clip audioLob;

      //
      // Constructor
      // Info..
      //
      public Attacking(String path)
      {
        super(path);
      }


      //
      // getImage
      // Info...
      //
      public BufferedImage getImage
      (
        int action,
        int percent,
        Position.Direction direction,
        TileSet tileSet
      ){
        switch(action)
        {
          case MELEE:
            if(percent == 0 && audioMelee != null)
            {
              audioMelee.play();
            }
            return melee.get(tileSet).get(action + direction).getFrameAtPercent(percent);
            break;
          case LOB:
            if(percent == 0 && audioLob != null)
            {
              audioLob.play();
            }
            return lob.get(tileSet).get(action + direction).getFrameAtPercent(percent);
            break;
          default:
            return super.getImage(direction, action, percent, tileSet);
            break;
        }
      }

      //
      // getMeleeDelay
      // Info...
      //
      public long getMeleeDelay()
      {
        return audioMelee.getMicrosecondLength();
      }

      //
      // getLobDelay
      // Info...
      //
      public long getLobDelay()
      {
        return audioLob.getMicrosecondLength();
      }
    }
  }
}