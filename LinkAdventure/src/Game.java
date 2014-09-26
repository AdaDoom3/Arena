public class Game
{

  //
  // Constants
  //
  private static final Properties user = new Properties("user");
  private static final Properties locale = new Properties("locale");
  
  //
  // Variables
  //
  static int numberOfPlayers = 0;

  // 
  // Grass#
  // Info...
  //
  public static class Grass1 extends Model.Entity
  {
    public Grass1() 
    {
      
    }
  }
  public static class Grass2 extends Model.Entity
  {
  }
  public static class Grass3 extends Model.Entity
  {
  }

  //
  // Tree
  // Info..
  //
  public static class Tree extends Model.Entity
  {
  }

  //
  // Rock
  // Info...
  //
  public static class Rock extends Model.Entity
  {
  }

  //
  // Link
  // Info..
  // 
  public static class Link extends Model.Entity
  {

    //
    // Variables
    //
    int points = 0;
    int playerNumber;

    //
    // Constructor
    // Info..
    //
    public Link()
    {
      super
      (
        new Sprite.Information[]
        {
          new Sprite.Information("lob"),
          new Sprite.Information("walk", 250),
          new Sprite.Information("melee")
        }
      );
      numberOfPlayers++;
      playerNumber = numberOfPlayers;
    }

    //
    // act
    // Info...
    //
    public void act()
    {
      if(Controller.isKeyDown(user.getConstantString("north")))
      {
        move(Controller.getEntities().get(this), Position.Direction.NORTH, "walk");
      }
      else if(Controller.isKeyDown(user.getConstantString("east")))
      {
        move(Controller.getEntities().get(this), Position.Direction.EAST, "walk");
      }
      else if(Controller.isKeyDown(user.getConstantString("south")))
      {
        move(Controller.getEntities().get(this), Position.Direction.SOUTH, "walk");
      }
      else if(Controller.isKeyDown(user.getConstantString("west")))
      {
        move(Controller.getEntities().get(this), Position.Direction.WEST, "walk");
      }
      else if(Controller.isKeyDown(user.getConstantString("lob")))
      {
        animate("lob");
      }
      else if(Controller.isKeyDown(user.getConstantString("melee")))
      {
        animate("melee");
      }
    }
  }

  //
  // Model variables
  //
  static Controller.Spawner<Link> linkSpawner = new Controller.Spawner<Link>(Link.class);
  static Controller.Spawner<Tree> treeSpawner = new Controller.Spawner<Tree>(Tree.class);
  static Controller.Spawner<Rock> rockSpawner = new Controller.Spawner<Rock>(Rock.class);
  static Controller.Tiler<Grass1> grass1Tiler = new Controller.Tiler<Grass1>(Grass1.class);
  static Controller.Tiler<Grass2> grass2Tiler = new Controller.Tiler<Grass2>(Grass2.class);
  static Controller.Tiler<Grass3> grass3Tiler = new Controller.Tiler<Grass3>(Grass3.class);
  
  //
  // update
  // Info..
  //
  public static void update()
  {
    if(Controller.getEntities().getCount(Link.class) < 1)
    {
      View.destruct();
    }
  }

  //
  // main
  // Info..
  //
  public static void main(String[] args)
  {
    Controller.createGrid
    (
      user.getConstantInt("rows"),
      user.getConstantInt("columns")
    );
    grass1Tiler.populatePercent(20);
    grass1Tiler.populate(1);
    grass2Tiler.populatePercent(20);
    grass3Tiler.populateAllEmpty();
    treeSpawner.populatePercent(20, Position.Direction.NORTH);
    treeSpawner.populate(1);
    rockSpawner.populatePercent(10);
    rockSpawner.populate(1);
    linkSpawner.populate(1);
    View.run
    (
      locale.getConstantString("view.title"),
      user.getConstantBoolean("view.fullscreen"),
      user.getConstantInt("view.width"),
      user.getConstantInt("view.height")
    );
  }
}
