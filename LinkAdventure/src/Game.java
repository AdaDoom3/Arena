import java.util.ArrayList;

//
// Game
// Info..
//
public class Game extends View
{
  
  //
  // Variables
  //
  static int numberOfPlayers = 0;
  
  //
  // State
  // Info..
  //
  enum State
  {
	
  	//
  	// Values
  	//
    IDLE("idle"),
  	MOVE_NORTH("move.north"),
  	MOVE_EAST("move.east"),
  	MOVE_SOUTH("move.south"),
  	MOVE_WEST("move.west"),
  	MELEE("melee"),
  	LOB("lob");
  	
  	//
  	// Variables
  	//
  	String key;
  	
  	//
  	// Constructor
  	//
  	State(String key)
  	{
  	  this.key = key;
  	}
  	
  	//
  	// getKey
  	// Info..
  	//
  	public String getKey()
  	{
  	  return key;
  	}
  }

  //
  // getState
  // Info..
  //
  static State getState(Model.Entity entity)
  {
    int lowest = 0;
	  ArrayList<Long> results = new ArrayList<Long>();
	  for(State state : State.values())
	  {
	    results.add(Controller.isKeyDown
	    (
	      user.getString(entity.getClass().getSimpleName() + "." + state.getKey())
	    ));
	  }
	  for(int i = 0;i < results.size();i++)
	  {
	    if(results.get(i) != 0 && results.get(i) >= results.get(lowest))
	    {
	      lowest = i;
	    }
	  }
	  if(results.get(lowest) == 0)
	  {
	    return State.IDLE;
	  }
	  return State.values()[lowest];
  }

  // 
  // Grass#
  // Info...
  //
  static class Grass1 extends Model.Entity{}
  static class Grass2 extends Model.Entity{}
  static class Grass3 extends Model.Entity{}

  //
  // Tree
  // Info..
  //
  static class Tree extends Model.Entity{}

  //
  // Rock
  // Info...
  //
  static class Rock extends Model.Entity{}

  //
  // Player
  // Info..
  // 
  static class Player extends Model.Entity
  {

    //
    // Variables
    //
    int points = 0;
    int playerNumber;

    //
    // Constructor
    //
    Player()
    {
      super
      (
        new Model.Information[]
        {
          new Model.Information("lob", 250),
          new Model.Information("walk", 250),
          new Model.Information("melee", 250)
        }
      );
      numberOfPlayers++;
      playerNumber = numberOfPlayers;
    }

    //
    // run
    // Info...
    //
    public void run()
    {
      if(!isAnimating())
      {
        switch(getState(this))
        {
          case MOVE_NORTH:
            animate("walk");
            move(Model.Direction.NORTH, 250);
            break;
          case MOVE_EAST:
            animate("walk");
            move(Model.Direction.EAST, 250);
      		  break;
          case MOVE_SOUTH:
            animate("walk");
            move(Model.Direction.SOUTH, 250);
      		  break;
          case MOVE_WEST:
            animate("walk");
            move(Model.Direction.WEST, 250);
      		  break;
          case MELEE:
            animate("melee");
    		    break;
          case LOB:
            animate("lob");
    		    break;
          case IDLE:
            break;
        }
      }
    }
  }

  //
  // Variables
  //
  static Controller.Spawner<Player> playerSpawner = new Controller.Spawner<Player>(Player.class);
  static Controller.Spawner<Tree> treeSpawner = new Controller.Spawner<Tree>(Tree.class);
  static Controller.Spawner<Rock> rockSpawner = new Controller.Spawner<Rock>(Rock.class);
  static Controller.Tiler<Grass1> grass1Tiler = new Controller.Tiler<Grass1>(Grass1.class);
  static Controller.Tiler<Grass2> grass2Tiler = new Controller.Tiler<Grass2>(Grass2.class);
  static Controller.Tiler<Grass3> grass3Tiler = new Controller.Tiler<Grass3>(Grass3.class);
  
  //
  // initialize
  // Info..
  //
  void initialize()
  {
    grass3Tiler.populatePercent(20);
    grass2Tiler.populatePercent(80);
    grass1Tiler.populateAllEmpty();
    treeSpawner.populatePercent(20, Model.Direction.NORTH);
    rockSpawner.populatePercent(50);
    playerSpawner.populate(1);
  }
  
  //
  // update
  // Info..
  //
  void update()
  {
    /*if(controller.getEntities().getCount(Player.class) < 1)
    {
      //view = null;//view.stop();
    }*/
  }
  
  //
  // main
  // Info..
  //
  void main(String[] args)
  {
    launch(args);
  }
}