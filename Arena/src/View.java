import javafx.application.Application;
import javafx.stage.Stage;

//
// View
// Info..
//
public class View extends Application implements Runnable
{
  
  //
  // run
  // Info..
  //
  public void run(){}
	
  //
  // start
  // Info..
  //
  public void start(Stage stage)
  {
    new Controller(new Model.Resource("user"), new Model.Resource("locale"), stage, this);
  }
}