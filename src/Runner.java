
public class Runner{

public static void main(String[] args){
    View  view  = View.getInstance();
    World world = World.getInstance();
    
	boolean running = true;
    while(running){
    	world.callback();
    	view.callback();
    }
  }
}