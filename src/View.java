import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
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
	private static final long serialVersionUID = 1L;
	
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
	
	/////////////////
	// Constructor //
	/////////////////
	protected View(){
		this.setTitle(Settings.TITLE);
		Dimension renderingAreaSize = World.getInstance().getVisiblePixelSize();
		this.setSize(
			renderingAreaSize.width  + this.getInsets().left + this.getInsets().right,
			renderingAreaSize.height + this.getInsets().top  + this.getInsets().bottom
		);
	    this.setDefaultCloseOperation(View.EXIT_ON_CLOSE);
	    this.setResizable(false);
	    JLabel renderingArea = new JLabel();
	    this.add(renderingArea);
	    if(Settings.FULLSCREEN){
			this.setUndecorated(true);
			this.setExtendedState(this.getExtendedState() | View.MAXIMIZED_BOTH);
		} else {
			this.setUndecorated(false);
			this.setExtendedState(this.getExtendedState() | View.NORMAL);
		}
	    this.running = true;
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