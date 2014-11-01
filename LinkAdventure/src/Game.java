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
  boolean doSetup = true;

  // 
  // Grass#
  // Info...
  //
  public static class Grass1 extends Model.Entity{}
  public static class Grass2 extends Model.Entity{}
  public static class Grass3 extends Model.Entity{}

  //
  // Tree
  // Info..
  //
  public static class Tree extends Model.Entity{}

  //
  // Rock
  // Info...
  //
  public static class Rock extends Model.Entity{}

  //
  // Player
  // Info..
  // 
  public static class Player extends Model.Entity
  {

    //
    // Variables
    //
    int points = 0;
    int playerNumber;

    //
    // Constructor
    //
    public Player()
    {
      super
      (
        new Model.Information[]
        {
          new Model.Information("lob"),
          new Model.Information("walk", 250),
          new Model.Information("melee")
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
      /*
      if(Controller.isKeyDown(user.getConstantString("north")))
      {
        move(Controller.getEntities().get(this), Model.Direction.NORTH, "walk");
      }
      else if(Controller.isKeyDown(user.getConstantString("east")))
      {
        move(Controller.getEntities().get(this), Model.Direction.EAST, "walk");
      }
      else if(Controller.isKeyDown(user.getConstantString("south")))
      {
        move(Controller.getEntities().get(this), Model.Direction.SOUTH, "walk");
      }
      else if(Controller.isKeyDown(user.getConstantString("west")))
      {
        move(Controller.getEntities().get(this), Model.Direction.WEST, "walk");
      }
      else if(Controller.isKeyDown(user.getConstantString("lob")))
      {
        animate("lob");
      }
      else if(Controller.isKeyDown(user.getConstantString("melee")))
      {
        animate("melee");
      }*/
    }
  }

  //
  // Game variables
  //
  static Controller.Spawner<Player> playerSpawner = new Controller.Spawner<Player>(Player.class);
  static Controller.Spawner<Tree> treeSpawner = new Controller.Spawner<Tree>(Tree.class);
  static Controller.Spawner<Rock> rockSpawner = new Controller.Spawner<Rock>(Rock.class);
  static Controller.Tiler<Grass1> grass1Tiler = new Controller.Tiler<Grass1>(Grass1.class);
  static Controller.Tiler<Grass2> grass2Tiler = new Controller.Tiler<Grass2>(Grass2.class);
  static Controller.Tiler<Grass3> grass3Tiler = new Controller.Tiler<Grass3>(Grass3.class);
  
  //
  // run
  // Info..
  //
  public void run()
  {
    if(doSetup)
    {
      grass3Tiler.populatePercent(20);
      grass2Tiler.populatePercent(80);
      grass1Tiler.populateAllEmpty();
      treeSpawner.populatePercent(20, Model.Direction.NORTH);
      rockSpawner.populatePercent(50);
      playerSpawner.populate(1);
      doSetup = false;
    }
    /*if(controller.getEntities().getCount(Player.class) < 1)
    {
      //view = null;//view.stop();
    }*/
  }
  
  //
  // main
  // Info..
  //
  public static void main(String[] args)
  {
    launch(args);
  }
}