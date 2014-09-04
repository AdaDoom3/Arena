import javax.swing.Timer;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Rectangle;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

public class View extends JFrame {

  private static final long serialVersionUID = 1L;

  //
  // Constants
  //
  public static final Dimension MINIMUM_SCREEN_SIZE = new Dimension(200, 200);

  //
  // Variables
  //
  public Timer timer = null;
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
    if(Properties.FULLSCREEN)
    {
      this.setUndecorated(true);
      this.setExtendedState(this.getExtendedState() | View.MAXIMIZED_BOTH);
    }
    else
    {
      this.setUndecorated(false);
      this.setExtendedState(this.getExtendedState() | View.NORMAL);
    }
    this.setSize (
      Math.min (
        Properties.WIDTH + this.getInsets().left + this.getInsets().right,
        workspace.width
      ),
      Math.min (
        Properties.HEIGHT + this.getInsets().top  + this.getInsets().bottom,
        workspace.height
      )
    );
    this.setDefaultCloseOperation(View.EXIT_ON_CLOSE);
    this.setLocation(workspace.x, workspace.y);
    this.setTitle(Properties.TITLE);
    this.setResizable(true);
    this.setVisible(true);
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
    GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    return new Rectangle (
      0 + insets.left,                                                             // x
      0 + insets.top,                                                              // y
      (graphicsDevice.getDisplayMode().getHeight() - insets.top)  - insets.bottom, // height
      (graphicsDevice.getDisplayMode().getWidth()  - insets.left) - insets.right   // width
    );
  }

//  //
//  // actionPerformed
//  // Info...
//  //
//  public void actionPerformed(ActionEvent e)
//  {
//      Dimension size = World.getInstance().getVisiblePixelSize();
//      BufferedImage buffer = new BufferedImage (
//        size.width,
//        size.height,
//        BufferedImage.TYPE_4BYTE_ABGR
//      );
//      Graphics2D graphics = buffer.createGraphics();
//      for(commands command : World.getCommands())
//      {
//        graphics.drawImage
//        (
//          object.sprite.getImage(), // image
//          object.coordinates.x,     // x
//          object.coordinates.y,     // y
//          null                      // 
//        );
//      }
//  }

  //
  // getInstance
  // info...
  //
  public static View getInstance()
  {
    if(instance == null)
    {
      instance = new View();
    }
    return instance;
  }
}
