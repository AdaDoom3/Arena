import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Game extends Application
{

  //
  // Constants
  //
  private static final Model.Properties user = new Model.Properties("user");
  private static final Model.Properties locale = new Model.Properties("locale");
  
  //
  // Variables
  //
  static int numberOfPlayers = 0;

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
    // Info..
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
    // act
    // Info...
    //
    public void act()
    {/*
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
  // Engine
  // Info..
  /*
  public static Engine extends View
  (

    //
    // Constructor
    // Info..
    //
    public Engine()
    {
      home = new Home();
    }

    //
    // 
    locale.getConstantString("view.title"),
    user.getConstantBoolean("view.fullscreen"),
    user.getConstantInt("view.width"),
    user.getConstantInt("view.height"),
    user.getConstantInt("rows"),
    user.getConstantInt("columns")
  );*/

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
  // update
  // Info..
  //
  public static void update()
  {
    if(Controller.getEntities().getCount(Player.class) < 1)
    {
      //view = null;//view.stop();
    }
  }
  
  @Override
  public void start(Stage primaryStage) {
    Controller.createGrid(user.getConstantInt("rows"), user.getConstantInt("columns"));
    grass1Tiler.populatePercent(20);
    grass2Tiler.populatePercent(20);
    grass3Tiler.populateAllEmpty();
    treeSpawner.populatePercent(20, Model.Direction.NORTH);
    rockSpawner.populatePercent(10);
    playerSpawner.populate(50);

    primaryStage.setTitle(locale.getConstantString("view.title"));
    Group root = new Group();
    /*final ImageView layer0 = new ImageView(300,250);
    final ImageView layer1 = new ImageView(300,250);
    final GraphicsContext gc1 = layer1.getGraphicsContext2D();
    gc1.setFill(Color.GREEN);
    gc1.fillOval(50,50,20,20);
    
    layer1.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent e) {
        if(e.getButton() == MouseButton.PRIMARY){
          gc1.fillOval(e.getX(),e.getY(),20,20);
        }
      }
    });*/
    StackPane stackPane = new StackPane();
    
    Model.Grid all = Controller.getEntities();
    for(int x = 0;x < all.getNumberOfColumns();x++)
    {
      for(int y = 0;y < all.getNumberOfRows();y++)
      {
        if(all.get(new Model.Location(x, y)) != null)
        {
          ImageView iv = new ImageView(all.get(new Model.Location(x, y)).actions.getAnimations("stand").getFrame(Model.Direction.NORTH, 0));
          iv.setX(80);
          iv.setY(80);
          stackPane.getChildren().add(iv);
        }
      }
    }
    
    
    //layer1.toFront();   
    
    primaryStage.setScene(new Scene(stackPane));
    primaryStage.show();
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