import java.io.File;
import java.io.IOException;
import java.awt.Dimension;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.AffineTransformOp;

import javax.imageio.ImageIO;
import javax.sound.sampled.Line;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

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
  public static Clip loadSound(String path) {
    Clip sound = null;
    try {
      sound = (Clip)AudioSystem.getLine(new Line.Info(Clip.class));
      sound.open(AudioSystem.getAudioInputStream(new File(path + PATH_AUDIO)));
    } catch (LineUnavailableException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (UnsupportedAudioFileException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return sound;
  }

  //
  // loadImage
  // Info...
  //
  public static BufferedImage loadImage(String path, boolean doFlipHorzontally) {
    BufferedImage result = null;
    try {
      result = ImageIO.read(new File(path + PATH_IMAGE));
      if(doFlipHorzontally) {
        AffineTransform tranformation = AffineTransform.getScaleInstance(-1, 1);
        tranformation.translate(-result.getWidth(), 0);
        result = new AffineTransformOp(
            tranformation,
          AffineTransformOp.TYPE_NEAREST_NEIGHBOR
        ).filter(result, null);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return result;
  }

  //
  // Animation
  // Info...
  //
  
  //
  // Sprite
  // Info..
  //
  public static class Sprite {

    //
    // TileSet
    // Info...
    //
    public enum TileSet {

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
      private TileSet(int size) {
        this.size = size;
      }

      //
      // getSize
      // Info..
      //
      public int getSize() {
        return size;
      }
      
      public Dimension toDimension(){
        return new Dimension(this.size, this.size);
      }
    }
  }
}