import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import javax.imageio.ImageIO;
import javax.sound.sampled.Clip;

//
// Sprite
// Info..
//
public class Sprite
{
    
  //
  // Constants
  //
  static final String PATH_IMAGE = ".png";
  static final String PATH_ART = "art";
  static final int TILE = 64;
  static final String PATH_BOUNDING = "bounding";

  //
  // rotateImage
  // Info..
  //
  private static BufferedImage rotateImage(int degrees, BufferedImage image)
  {/*
    BufferedImage result = image;
    AffineTransform tranformation = AffineTransform.getScaleInstance(-1, 1);
    tranformation.translate(-result.getWidth(), 0);
    result = new AffineTransformOp
    (
      tranformation,
      AffineTransformOp.TYPE_NEAREST_NEIGHBOR
    ).filter(result, null);
    return result;*/return image;
  }

  //
  // Information
  // Info..
  //
  public static class Information
  {

    //
    // Variables
    //
    String name;
    long duration;

    //
    // Constructors
    // Info...
    //
    public Information(String name)
    {
      this.name = name;
    }
    public Information(String name, long duration)
    {
      this.name = name;
      this.duration = duration;
    }

    //
    // getName
    // Info..
    //
    public String getName()
    {
      return name;
    }

    //
    // getDuration
    // Info..
    //
    public long getDuration() throws Exception
    {
      if(duration > 0)
      {
        throw new Exception("No supplied duration");
      }
      return duration;
    }
  }

  //
  // Animations
  // Info...
  //
  public static class Animations
  {

    //
    // Variables
    //
    EnumMap<Position.Direction, ArrayList<BufferedImage>> frames =
      new EnumMap<Position.Direction, ArrayList<BufferedImage>>(Position.Direction.class);
    Position.Location bounding;
    long duration;
    Clip audio;

    //
    // Constructor
    // Info..
    //
    public Animations(String path, Information information) throws Error
    {
      ArrayList<BufferedImage> temporary = new ArrayList<BufferedImage>();
      ArrayList<BufferedImage> result;
      boolean success = false;
      final String PATH_BASE = PATH_ART + "/" + path + "/" + TILE;
      try
      {
        temporary.add(ImageIO.read(new File(PATH_BASE + PATH_BOUNDING + PATH_IMAGE)));
        for(int y = 0;y < temporary.get(0).getHeight() && !success;y++)
        {
          for(int x = 0;x < temporary.get(0).getWidth() && !success;x++)
          {
            Color color = new Color(temporary.get(0).getRGB(x, y), true);
            if(color.getAlpha() > 0)
            {
              bounding = new Position.Location(x + 1, y + 1);
              success = true;
            }
          }
          if(y == temporary.get(0).getHeight() - 1 && !success)
          {
            throw new Exception();
          }
        }
      }
      catch(Exception noBoundingImage)
      {
        bounding = new Position.Location(1, 1);
      }
      temporary.clear();
      try
      {
        duration = information.getDuration();
      }
      catch(Exception noDuration)
      {
        try
        {
          audio = loadSound();
          duration = audio.getMicrosecondLength();
        }
        catch(Exception noClip)
        {
          duration = 0;
        }
      }
      try
      {
        temporary.add(ImageIO.read(new File(PATH_BASE + information.getName() + PATH_IMAGE)));
        for(Position.Direction direction : Position.Direction.values())
        {
          result = new ArrayList<BufferedImage>();
          result.add(rotateImage(direction.getDegrees(), temporary.get(0)));
          frames.put(direction, result);
        }
      }
      catch(Exception noSingleDirectionWithNoNumber)
      {
        try
        {          
          for(int i = 1;;i++)
          {
            temporary.add(ImageIO.read(new File
            (
              PATH_BASE + information.getName() + i + PATH_IMAGE
            )));
          }
        }
        catch(Exception endOfSingleDirectionWithNumber)
        {
          if(temporary.size() > 0)
          {
            for(Position.Direction direction : Position.Direction.values())
            {
              result = new ArrayList<BufferedImage>();
              for(BufferedImage image : temporary)
              {
                result.add(rotateImage(direction.getDegrees(), image));
              }
              frames.put(direction, result);
            }
          }
          else
          {
            try
            {
              for(Position.Direction direction : Position.Direction.values())
              {
                result = new ArrayList<BufferedImage>();
                result.add(ImageIO.read(new File
                (
                  PATH_BASE + information.getName() + direction.name() + PATH_IMAGE
                )));
                frames.put(direction, result);
              }
            }
            catch(Exception noDirectionWithNoNumber)
            {
              for(Position.Direction direction : Position.Direction.values())
              {
                result = new ArrayList<BufferedImage>();
                try
                {
                  for(int i = 1;;i++)
                  {
                    result.add(ImageIO.read(new File
                    (
                      PATH_BASE + information.getName() + direction.name() + i + PATH_IMAGE
                    )));
                  }
                }
                catch(Exception endOfDirectionWithNumber)
                {
                  if(result.size() < 1)
                  {
                    throw new Error("Missing direction in animated action");
                  }
                }
                frames.put(direction, result);
              }
            }
          }
        }
      }
    }

    private Clip loadSound() {
      return null;
    }

    //
    // getFrame
    // Info..
    //
    public BufferedImage getFrame(Position.Direction direction, int number) // number start at index 0??
    {
      return frames.get(direction).get(number);
    }

    //
    // getFrameAtDeltaTime
    // Info..
    //
    public BufferedImage getFrameAtDeltaTime(Position.Direction direction, long deltaTime)
    {
      long timeFromStart = Math.abs(deltaTime) % this.duration;
      int frame = (int)(this.duration / timeFromStart);
      return getFrame(direction, frame);
    }

    //
    // getNumberOfFrames
    // Info...
    //
    public int getNumberOfFrames(Position.Direction direction)
    {
      return frames.get(direction).size();
    }

    //
    // getDimension
    // Info...
    //
    public Dimension getDimension()
    {
      return new Dimension
      (
        frames.get(Position.Direction.NORTH).get(0).getWidth(),
        frames.get(Position.Direction.NORTH).get(0).getHeight()
      );
    }
  }

  //
  // Actions
  // Info..
  //
  public static class Actions
  {

    //
    // Variables
    //
    HashMap<String, Animations> actingAnimations = new HashMap<String, Animations>();

    //
    // Constructor
    // Info..
    //
    public Actions(String path, Information[] informations) throws Error
    {
      for(Information information : informations)
      {
        actingAnimations.put(information.getName(), new Animations(path, information));
      }
    }

    //
    // getAnimations
    // Info..
    //
    public Animations getAnimations(String action)
    {
      return actingAnimations.get(action);
    }
  }
}