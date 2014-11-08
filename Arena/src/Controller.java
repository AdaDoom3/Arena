import java.util.HashMap;
import javafx.util.Duration;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.image.ImageView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.Animation;

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
  static HashMap<String, Long> keyboard = new HashMap<String, Long>();
  static long currentKeyboard = 0;

  //
  // Constructor
  //
  public Controller(Model.Resource locale, Model.Resource user, Stage stage, Runnable runnable)
  {
    final Stage finalStage = stage;
    final Runnable finalRunnable = runnable;
    final Model.Resource finalUser = user;
    final Model.Resource finalLocale = locale;
    final Group finalGroup = new Group();
    final Scene finalScene = new Scene(finalGroup, user.getInt("width"), user.getInt("height"));
    final Timeline finalTimeline = new Timeline
    (
      new KeyFrame(Duration.seconds(user.getDouble("speed")),
      new EventHandler<ActionEvent>()
      {  
        public void handle(ActionEvent event)
        { 
          finalRunnable.run();
          finalGroup.getChildren().removeAll(finalGroup.getChildren());
          for(Model.Grid grid : layers)
          {
            for(Model.Entity entity : grid.getAll())
            {
              entity.run();
              ImageView imageView = new ImageView();
              imageView.setImage(entity.getFrame());
              if(entity.getMovePercent() < 1.0)
              {
                Model.Location location = entity.getLocation().getDisplacement
                (
                  entity.getDirection(),
                  entity.getMovePercent()
                );
                imageView.relocate
                (
                  location.getX() - entity.getBounding().getX(),
                  location.getY() - entity.getBounding().getY()
                );
              }
              else
              {
                imageView.relocate
                (
                  entity.getLocation().getX() * Model.TILE - entity.getBounding().getX(),
                  entity.getLocation().getY() * Model.TILE - entity.getBounding().getY()
                );
              }
              finalGroup.getChildren().add(imageView);
              if(!finalStage.isShowing())
              {
            	finalStage.show();
              }
            }
          }
        }  
      })
    );
    finalGroup.relocate(1.0, 1.0);
    finalTimeline.setCycleCount(Animation.INDEFINITE);  
    finalTimeline.play(); 
    finalStage.setTitle(locale.getString("title"));
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
          keyboard.replace(keyEvent.getCode().toString(), new Long(0));
        }
      }
    });
    finalScene.setOnKeyPressed(new EventHandler<KeyEvent>()
    {
      public void handle(KeyEvent keyEvent)
      {
    	currentKeyboard++;
        //System.out.println(keyEvent.getCode().toString());
        if(!keyboard.containsKey(keyEvent.getCode().toString()))
        {
          keyboard.put(keyEvent.getCode().toString(), currentKeyboard);
        }
        else
        {
          keyboard.replace(keyEvent.getCode().toString(), currentKeyboard);
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
  public static long isKeyDown(String key)
  {
    if(!keyboard.containsKey(key))
    {
      return 0;
    }
    return keyboard.get(key);
  }

  //
  // getLayer
  // Info..
  //
  public static Model.Grid getLayer(int layer)
  {
    return layers[layer];
  }

  //
  // getEntities
  // Info..
  //
  public static Model.Grid getEntities()
  {
    return getLayer(1);
  }

  //
  // getTiles
  // Info..
  //
  public static Model.Grid getTiles()
  {
    return getLayer(0);
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
      t.setLocation(location);
      t.setGrid(getLayer(layer));
      layers[layer].add((Model.Entity)t, location);
    }
    public void populate(int thisMany, Model.Direction direction)
    {
      for(int i = 0;i < thisMany;i++)
      {
        populate(direction, getLayer(layer).getRandomEmpty());
      }
    }
    public void populate(int thisMany)
    {
      for(int i = 0;i < thisMany;i++)
      {
        populate(Model.getRandomDirection(), getLayer(layer).getRandomEmpty());
      }
    }

    //
    // populatePercent
    // Info..
    //
    public void populatePercent(int percent, Model.Direction direction)
    {
      populate((int)Math.floor(getLayer(layer).getAllEmpty().length / 100) * percent, direction);
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
      populate(getLayer(layer).getAllEmpty().length);
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
    //
    public Spawner(Class<T> clazz)
    {
      super(1, clazz);
    }
  }
}