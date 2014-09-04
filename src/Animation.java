import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Animation {

    //
    // Variables
    //
    private long                     length;
    private ArrayList<BufferedImage> frames;
    
    //
    // Constructor
    // Info...
    //
    public Animation(long length, String path, boolean doFlipHorzontally) {
      this.length = length;
      try {
        for(int i = 0;;i++) {
          frames.add(Assets.loadImage(path + i, doFlipHorzontally));
        }
      }
      catch(Exception e) {
        if(frames.size() < 1) {
          throw e;
        }
      }
    }

    //
    // getFrame
    // Info..
    //
    public BufferedImage getFrame(int number) {
      return frames.get(number);
    }

    //
    // getFrameAtDeltaTime
    // Info..
    //
    public BufferedImage getFrameAtDeltaTime(long deltaTime) {
      long timeFromStart = Math.abs(deltaTime) % this.length;
      int frame = (int)(this.length / timeFromStart);
      return getFrame(frame);
    }
    
    //
    // getFrameAtPercentage
    // Info..
    //
    public BufferedImage getFrameAtPercentage(int percentage) {
      long adjusted = Math.abs(percentage) % 100;
      int frame = (int)((this.frames.size()/100)*adjusted);
      return getFrame(frame);
    }

    //
    // getNumberOfFrames
    // Info...
    //
    public int getNumberOfFrames() {
      return frames.size();
    }

    //
    // getDimension
    // Info...
    //
    public Dimension getDimension() {
      return new Dimension(frames.get(0).getWidth(), frames.get(0).getHeight());
    }
    
    public long getLength(){
      return this.length;
    }
  }