import javax.swing.Timer;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.util.ArrayList;
import java.awt.Point;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Rectangle;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
public class View extends JFrame implements ActionListener{

  //
  // Constants
  //
  public static final Dimension MINIMUM_SCREEN_SIZE = new Dimension(200, 200);

  //
  // Variables
  //
  public Timer timer = null;
  private boolean running = false;
  private static View instance = null;

  //
  // Constructor
  // Info...
  //
  protected View()
  {
    Rectangle workspace = getWorkspace();
    if(workspace.width  < MINIMUM_SCREEN_SIZE.width
    || workspace.height < MINIMUM_SCREEN_SIZE.height)
    {
      throw new Error("Unsupported Screen Size. Screen must be at least 200 by 200.");
    }
    if(Settings.FULLSCREEN)
    {
      this.setUndecorated(true);
      this.setExtendedState(this.getExtendedState() | View.MAXIMIZED_BOTH);
    }
    else
    {
      this.setUndecorated(false);
      this.setExtendedState(this.getExtendedState() | View.NORMAL);
    }
    this.setSize
    (
      Math.min
      (
        Settings.WIDTH + this.getInsets().left + this.getInsets().right,
        workspace.width
      ),
      Math.min
      (
        Settings.HEIGHT + this.getInsets().top  + this.getInsets().bottom,
        workspace.height
      )
    );
    this.setDefaultCloseOperation(View.EXIT_ON_CLOSE);
    this.setLocation(workspace.x, workspace.y);
    this.setTitle(Settings.TITLE);
    this.setResizable(true);
    this.setVisible(true);
    this.running = true;
    this.timer = new Timer(Settings.RENDERER_SPEED, this);
    this.add(new JLabel());
    this.setMaximumSize(new Dimension(workspace.width, workspace.height));
    this.setMinimumSize(new Dimension(MINIMUM_SCREEN_SIZE.width, MINIMUM_SCREEN_SIZE.height));
  }

  //
  // getWorkspace
  // Info..
  //
  private Rectangle getWorkspace()
  {
    Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(getGraphicsConfiguration());
    GraphicsDevice graphicsDevice =
      GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    return new Rectangle
    (
      (graphicsDevice.getDisplayMode().getHeight() - insets.top) - insets.bottom, // height
      (graphicsDevice.getDisplayMode().getWidth() - insets.left) - insets.right,  // width
      0 + insets.left,                                                            // x
      0 + insets.top                                                              // y
    );
  }

  //
  // actionPerformed
  // Info...
  //
  public void actionPerformed(ActionEvent e)
  {
    if(this.running)
    {
      Dimension size = World.getVisiblePixelSize();
      BufferedImage buffer = new BufferedImage
      (
        size.width,
        size.height,
        BufferedImage.TYPE_4BYTE_ABGR
      );
      Graphics2D graphics = buffer.createGraphics();
      for(commands command : World.getCommands())
      {
        graphics.drawImage
        (
          object.sprite.getImage(), // image
          object.coordinates.x,     // x
          object.coordinates.y,     // y
          null                      // 
        );
      }
    }
    else
    {
      this.dispose();
    }
  }

  //
  // run
  // info...
  //
  public static void run()
  {
    if(instance == null)
    {
      instance = new View();
    }
  }

  //
  // isRunning
  // Info...
  //
  public synchronized boolean isRunning()
  {
    return this.running;
  }

  //
  // setRunning
  // Info...
  //
  public synchronized void setRunning(boolean item)
  {
    this.running = item;
  }
}
