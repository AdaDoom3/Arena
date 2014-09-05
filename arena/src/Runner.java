
//
// Runner
// Info..
//
public class Runner 
{

  // 
  // Impulse
  // Info..
  //
  public enum Impulse
  {
    MOVE_NORTH,
    MOVE_EAST,
    MOVE_SOUTH,
    MOVE_WEST,
    MELEE,
    LOB;
  }

  //
  // ActionState
  // Info...
  //
  public enum ActionState
  {

    //
    // Values
    //
    DIE,
    LOB,
    MOVE,
    STAND,
    MELEE;
  }
/*
  
  //
  // Link
  // Info..
  //
  public static class Link extends Base
  {

    //
    // Constructor
    // Info..
    //
    public Link
    {
      super("Link");
      addAction(ActionState.DIE);
      addAction(ActionState.MOVE);
      addAction(ActionState.STAND);
      addAction(ActionState.MELEE);
      addAction(ActionState.LOB);
    }
  }
  
*/  
	
  //
  // main
  // Info..
  //
  public static void main(String[] args)
  {
    View view = View.getInstance();
  }
}