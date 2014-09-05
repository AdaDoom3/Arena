package engine;

//
// Runner
// Info..
//
public class Runner 
{

  private static boolean running = true;
  
  //
  // main
  // Info..
  //
  public static void main(String[] args)
  {
    View view             = View.getInstance();
    Controller controller = Controller.getInstance();
    view.addKeyListener(Properties.GAME);
    view.addMouseListener(Properties.GAME);
    
    long deltaTime = 0;
    long startTime = System.currentTimeMillis();
    long endTime = System.currentTimeMillis();
    while(running){
      Properties.GAME.GameLoopCallback(deltaTime, controller);
      endTime   = System.currentTimeMillis();
      deltaTime = endTime - startTime;
      startTime = endTime;
    }
  }
}