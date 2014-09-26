import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Rectangle;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//
// View
// Info..
//
public class View extends JFrame implements ActionListener
{

  //
  // Constants
  //
  static final long serialVersionUID = 1L;
  public static final Dimension MINIMUM_SCREEN_SIZE = new Dimension(200, 200);

  //
  // Variables
  //
  long end = 0;
  long delta = 0;
  long start = System.currentTimeMillis();
  int            width         = MINIMUM_SCREEN_SIZE.width;
  int            height        = MINIMUM_SCREEN_SIZE.height;
  Timer          timer         = null;
  JLabel         renderingArea = null;
  ActionListener delegate      = null;
  
  //
  // Constructor
  // Info...
  //
  public View(String title, boolean fullscreen, int width, int height, ActionListener delegate)
  {
    this.width      = width;
    this.height     = height;
    Rectangle workspace = getWorkspace();
    if(workspace.width  < MINIMUM_SCREEN_SIZE.width
    || workspace.height < MINIMUM_SCREEN_SIZE.height)
    {
      throw new Error("Unsupported Screen Size. Screen must be at least 200 by 200.");
    }
    if(fullscreen)
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
        this.width + this.getInsets().left + this.getInsets().right,
        workspace.width
      ),
      Math.min
      (
        this.height + this.getInsets().top  + this.getInsets().bottom,
        workspace.height
      )
    );
    this.setDefaultCloseOperation(View.EXIT_ON_CLOSE);
    this.setLocation(workspace.x, workspace.y);
    this.setTitle(title);
    this.setResizable(true);
    this.setVisible(true);
    this.setLayout(new GridBagLayout());
    this.renderingArea = new JLabel();
    this.add(renderingArea);
    this.setMaximumSize(new Dimension(workspace.width, workspace.height));
    this.setMinimumSize(new Dimension(MINIMUM_SCREEN_SIZE.width, MINIMUM_SCREEN_SIZE.height));
    this.delegate = delegate;
    this.timer = new Timer(1, this);
    this.timer.start(); 
  }

  //
  // getRenderingArea
  // Info...
  //
  public JLabel getRenderingArea()
  {
    return this.renderingArea;
  }
  
  //
  // getWorkspace
  // Info..
  //
  private Rectangle getWorkspace()
  {
    Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(getGraphicsConfiguration());
    GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    return new Rectangle
    (
      0 + insets.left,                                                            // x
      0 + insets.top,                                                             // y
      (graphicsDevice.getDisplayMode().getWidth()  - insets.left) - insets.right, // width
      (graphicsDevice.getDisplayMode().getHeight() - insets.top)  - insets.bottom // height
    );
  }

  //
  // actionPerformed
  // Info..
  //
  public void actionPerformed(ActionEvent e)
  {
    e.setSource(this.getRenderingArea());
    delegate.actionPerformed(e);
  }
  
  //
  // run
  // Info..
  //
  public static void run
  (
    String title,
    boolean fullscreen,
    int width,
    int height
  ){
  }
  
  //
  // destruct
  // Info..
  //
  public static void destruct()
  {
    
  }
}
