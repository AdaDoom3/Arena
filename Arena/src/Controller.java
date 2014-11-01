import java.util.HashMap;
import javafx.util.Duration;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.stage.WindowEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

//
// Controller
// Info..
//
public class Controller
{
  
  //
  // Variables
  //
  static Model.Grid[] layers;
  HashMap<String, Boolean> keyboard = new HashMap<String, Boolean>();

  //
  // Constructor
  //
  public Controller(Model.Resource user, Model.Resource locale, Stage stage, Runnable runnable)
  {
    final Stage finalStage = stage;
    final Runnable finalRunnable = runnable;
    final Model.Resource finalUser = user;
    final Model.Resource finalLocale = locale;
    final Group finalGroup = new Group();
    final Scene finalScene = new Scene
    (
      finalGroup,
      user.getInt("view.width"),
      user.getInt("view.height"),
      Color.WHITE
    );
    final Timeline finalTimeline = new Timeline
    (
      new KeyFrame(Duration.seconds(0.05), new EventHandler<ActionEvent>()
      {  
        public void handle(ActionEvent event)
        { 
          finalGroup.getChildren().removeAll(finalGroup.getChildren());
          finalRunnable.run();
          for(Model.Grid grid : layers)
          {
            for(Model.Entity entity : grid.getAll())
            {
              entity.run();
              ImageView imageView = new ImageView();
              imageView.setImage(entity.getFrame());
              imageView.relocate
              (
                entity.getLocation().getX() * Model.TILE - entity.getBounding().getX(),
                entity.getLocation().getY() * Model.TILE - entity.getBounding().getY()
              );
              finalGroup.getChildren().add(imageView);
            }
          }
          finalStage.show();
        }  
      })
    );
    finalGroup.relocate(1.0, 1.0);
    finalTimeline.setCycleCount(Animation.INDEFINITE);  
    finalTimeline.play(); 
    finalStage.setTitle(locale.getString("view.title"));
    finalStage.setScene(finalScene);
    finalStage.setOnCloseRequest(new EventHandler<WindowEvent>()
    {
      public void handle(WindowEvent event)
      {
        finalTimeline.stop();
        finalUser.save();
        finalLocale.save();
      }
    });
    finalScene.setOnKeyReleased(new EventHandler<KeyEvent>()
    {
      public void handle(KeyEvent keyEvent)
      {
        if(keyboard.containsKey(keyEvent.getCode().toString()))
        {
          keyboard.replace(keyEvent.getCode().toString(), false);
        }
      }
    });
    finalScene.setOnKeyPressed(new EventHandler<KeyEvent>()
    {
      public void handle(KeyEvent keyEvent)
      {
        System.out.println(keyEvent.getCode().toString());
        if(!keyboard.containsKey(keyEvent.getCode().toString()))
        {
          keyboard.put(keyEvent.getCode().toString(), false);
        }
        else
        {
          keyboard.replace(keyEvent.getCode().toString(), true);
        }        
      }
    });
    layers = new Model.Grid[2];
    for(int i = 0;i < layers.length;i++)
    {
      layers[i] = new Model.Grid(user.getInt("rows"), user.getInt("columns"));
    }
  }
  
  //
  // isKeyDown
  // Info..
  //
  public boolean isKeyDown(String key)
  {
    if(!keyboard.containsKey(key))
    {
      return false;
    }
    return keyboard.get(key);
  }

  //
  // getLayer
  // Info..
  //
  public Model.Grid getLayer(int layer)
  {
    return layers[layer];
  }

  //
  // getEntities
  // Info..
  //
  public static Model.Grid getEntities()
  {
    return layers[1];
  }

  //
  // getTiles
  // Info..
  //
  public static Model.Grid getTiles()
  {
    return layers[0];
  }
  
  //
  // Populator
  // Info..
  //
  public static class Populator<T extends Model.Entity>
  {

    //
    // Variables
    //
    int layer;
    Class<T> clazz;

    //
    // Constructor
    //
    public Populator(int layer,  Class<T> clazz)
    {
      this.layer = layer;
      this.clazz = clazz;
    }

    //
    // populate
    // Info..
    //
    public void populate(Model.Direction direction, Model.Location location)
    {
      T t = null;
      try
      {
        t = this.clazz.newInstance();
      }
      catch (Exception exception)
      {
        exception.printStackTrace();
      }
      t.setDirection(direction);
      layers[layer].add((Model.Entity)t, location);
    }
    public void populate(int thisMany, Model.Direction direction)
    {
      for(int i = 0;i < thisMany;i++)
      {
        populate(direction, layers[layer].getRandomEmpty());
      }
    }
    public void populate(int thisMany)
    {
      for(int i = 0;i < thisMany;i++)
      {
        populate(Model.getRandomDirection(), layers[layer].getRandomEmpty());
      }
    }

    //
    // populatePercent
    // Info..
    //
    public void populatePercent(int percent, Model.Direction direction)
    {
      populate((int)Math.floor(layers[layer].getAllEmpty().length / 100) * percent, direction);
    }
    public void populatePercent(int percent)
    {
      populatePercent(percent, Model.getRandomDirection());
    }
  }

  //
  // Tiler
  // Info...
  //
  public static class Tiler<T extends Model.Entity> extends Populator<T>
  {

    //
    // Constructor
    //
    public Tiler(Class<T> clazz)
    {
      super(0, clazz);
    }

    //
    // populateAllEmpty
    // Info..
    //
    public void populateAllEmpty()
    {
      populate(layers[layer].getAllEmpty().length);
    }
  }

  //
  // Spawner
  // Info...
  //
  public static class Spawner<T extends Model.Entity> extends Populator<T>
  {

    //
    // Constructor
    // Info..
    //
    public Spawner(Class<T> clazz)
    {
      super(1, clazz);
    }
  }
}