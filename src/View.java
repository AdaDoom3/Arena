import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;

public class View extends JFrame implements ActionListener {
	///////////////
	// Constants //
	///////////////
	private static final long      serialVersionUID    = 1L;
	public  static final Dimension MINIMUM_SCREEN_SIZE = new Dimension(200, 200);
	
	///////////////
	// Variables //
	///////////////
	public  Timer     timer      = null;
	private boolean   running    = false;
	
	/////////////////////
	// Private Classes //
	/////////////////////

	private static class RenderingObject {
		public final Sprite sprite;
		public final Point  coordinates;
		
		public RenderingObject(Sprite sprite, Point coordinates){
			this.sprite      = sprite;
			this.coordinates = coordinates;
		}
	}
	
	private static class RenderingState {
		
		private ArrayList<RenderingObject> toDraw = null; 
		
		public RenderingState(){
			this.toDraw = new ArrayList<RenderingObject>();
		}
		
		public void addRenderingObject(RenderingObject item){
			this.toDraw.add(item);
		}
		public RenderingObject[] getRenderingObjects(){
			return (RenderingObject[]) this.toDraw.toArray();
		}
		
		public static RenderingState toRenderingState(World world){
			RenderingState output = new RenderingState();
			
			TileSet tileSet = world.getTileSet();
			for(Entity entity : world.getVisibleEntities()){
				output.addRenderingObject(
					new RenderingObject(
						entity.getSprite(tileSet), 
						world.getPixelCoordinates(entity)
					)
				);
			}
			
			return output;
		}
	}
	
	/////////////////////////////
	// Singleton Instantiation //
	/////////////////////////////
	private static View instance = null;
	public static View getInstance() {
      if(instance == null) {
         instance = new View();
      }
      return instance;
	}
	
	private Rectangle getWorkspace(){
		GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		Dimension screenSize = new Dimension(
			graphicsDevice.getDisplayMode().getWidth(),
			graphicsDevice.getDisplayMode().getHeight()
		);
		
		Rectangle output = new Rectangle();
		Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(getGraphicsConfiguration());
		output.x = 0 + insets.left;
		output.y = 0 + insets.top;
		output.width = (screenSize.width - insets.left) - insets.right;
		output.height = (screenSize.height - insets.top) - insets.bottom;
		return output;
	}
	
	/////////////////
	// Constructor //
	/////////////////
	protected View(){
		Rectangle workspace = getWorkspace();
		if(workspace.width  < MINIMUM_SCREEN_SIZE.width
		|| workspace.height < MINIMUM_SCREEN_SIZE.height){
			throw new Error("Unsupported Screen Size. Screen must be at least 200x200");
		}
		this.setMaximumSize(new Dimension(
			workspace.width,
			workspace.height
		));
		this.setMinimumSize(new Dimension(
			MINIMUM_SCREEN_SIZE.width,
			MINIMUM_SCREEN_SIZE.height
		));
		Dimension size = new Dimension(
			Settings.WIDTH + this.getInsets().left + this.getInsets().right,
			Settings.HEIGHT + this.getInsets().top  + this.getInsets().bottom
		);
		this.setSize(Math.min(size.width, workspace.width), Math.min(size.height, workspace.height));
		this.setLocation(workspace.x, workspace.y);
	    this.running = true;
	    if(Settings.FULLSCREEN){
			this.setUndecorated(true);
			this.setExtendedState(this.getExtendedState() | View.MAXIMIZED_BOTH);
		} else {
			this.setUndecorated(false);
			this.setExtendedState(this.getExtendedState() | View.NORMAL);
		}
		this.setTitle(Settings.TITLE);
	    this.add(new JLabel());
	    this.setResizable(true);
	    this.setDefaultCloseOperation(View.EXIT_ON_CLOSE);
	    this.setVisible(true);
	    this.timer = new Timer(Settings.RENDERER_SPEED, this);
	}
	/////////////////
	// renderWorld //
	/////////////////
	private void renderWorld(){
    	Dimension size = World.getInstance().getVisiblePixelSize();
	    BufferedImage buffer = new BufferedImage(
	    	size.width,
	    	size.height,
	    	BufferedImage.TYPE_4BYTE_ABGR
	    );
	    Graphics2D graphics = buffer.createGraphics();
	    RenderingState state = RenderingState.toRenderingState(World.getInstance());
	    for(RenderingObject object : state.getRenderingObjects()){
	    	graphics.drawImage(object.sprite.getImage(), object.coordinates.x, object.coordinates.y, null);
	    }
	}
	/////////////////////
	// Event Callbacks //
	/////////////////////
	public void actionPerformed(ActionEvent e) {
		if(this.running){
			this.renderWorld();
		} else {
			this.dispose();
		}
	}

	///////////////
	// isRunning //
	///////////////
	public synchronized boolean isRunning(){
		return this.running;
	}
	////////////////
	// setRunning //
	////////////////
	public synchronized void setRunning(boolean item){
		this.running = false;
	}
}