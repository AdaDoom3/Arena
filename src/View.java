import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class View extends JFrame {
	///////////////
	// Constants //
	///////////////
	private static final long      serialVersionUID    = 1L;
	public  static final Dimension MINIMUM_SCREEN_SIZE = new Dimension(200, 200);
	
	///////////////
	// Variables //
	///////////////
	private static View     instance = null;
	private        Renderer renderer = null;
	
	//////////////////
	// getWorkspace //
	//////////////////
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
		output.width  = (screenSize.width  - insets.left) - insets.right;
		output.height = (screenSize.height - insets.top)  - insets.bottom;
		return output;
	}
	
	public void callback(){
		this.renderer.render();
	}
	
	//////////////////////
	// Singleton Getter //
	//////////////////////
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
			Settings.WIDTH  + this.getInsets().left + this.getInsets().right,
			Settings.HEIGHT + this.getInsets().top  + this.getInsets().bottom
		);
		this.setSize(Math.min(size.width, workspace.width), Math.min(size.height, workspace.height));
		this.setLocation(workspace.x, workspace.y);
	    if(Settings.FULLSCREEN){
			this.setUndecorated(true);
			this.setExtendedState(this.getExtendedState() | View.MAXIMIZED_BOTH);
		} else {
			this.setUndecorated(false);
			this.setExtendedState(this.getExtendedState() | View.NORMAL);
		}
		this.setTitle(Settings.TITLE);
		this.renderer = new Renderer(new Dimension(Settings.WIDTH, Settings.HEIGHT));
	    this.add(this.renderer);
	    this.setResizable(true);
	    this.setDefaultCloseOperation(View.EXIT_ON_CLOSE);
	    this.setVisible(true);
	}
	
	//////////////
	// Renderer //
	//////////////
	private static class Renderer extends JLabel {
		
		///////////////
		// Constants //
		///////////////
		private static final long serialVersionUID = 1L;
		
		///////////////
		// Variables //
		///////////////
		private Dimension     size     = null;
		private BufferedImage contents = null;
		
		public Renderer(Dimension size){
			this.size = size;
			this.contents = new BufferedImage(
				this.size.width,
				this.size.height,
				BufferedImage.TYPE_4BYTE_ABGR
			);
			this.setIcon(new ImageIcon(this.contents));
		}

		//////////////////
		// MappedSprite //
		//////////////////
		private static class MappedSprite {
			public final Sprite sprite;
			public final Point  coordinates;
			
			public MappedSprite(Sprite sprite, Point coordinates){
				this.sprite      = sprite;
				this.coordinates = coordinates;
			}
		}
		///////////
		// State //
		///////////
		public static class State {
			private ArrayList<MappedSprite> toDraw = null; 
			
			public State(){
				this.toDraw = new ArrayList<MappedSprite>();
			}
			
			public void addRenderingObject(MappedSprite item){
				this.toDraw.add(item);
			}
			public MappedSprite[] getRenderingObjects(){
				MappedSprite[] output = new MappedSprite[this.toDraw.size()];
				output = this.toDraw.toArray(output);
				return output;
			}
			
			public static State toState(World world){
				State output = new State();
			
				TileSet tileSet = world.getTileSet();
				for(Entity entity : world.getVisibleEntities()){
					output.addRenderingObject(
						new MappedSprite(
							entity.getSprite(tileSet), 
							world.getPixelCoordinates(entity)
						)
					);
				}
			
				return output;
			}
		}
		
		////////////
		// render //
		////////////
		public void render(){
			Graphics2D graphics = this.contents.createGraphics();
			State state = State.toState(World.getInstance());
			for(MappedSprite object : state.getRenderingObjects()){
				graphics.drawImage(object.sprite.getImage(), object.coordinates.x, object.coordinates.y, null);
			}
		}
	}
}