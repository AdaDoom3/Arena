import javafx.application.Application;
import javafx.stage.Stage;

//
// View
// Info..
//
public class View extends Application implements Runnable
{
  
  //
  // Variables
  //
  boolean doSetup = true;
  static Model.Resource user = new Model.Resource("user");
  static Model.Resource locale = new Model.Resource("locale");
  
  //
  // initialize
  // Info..
  //
  void initialize(){}
  
  //
  // update
  // Info..
  //
  void update(){}
	
  //
  // run
  // Info..
  //
  public void run()
  {
	if(doSetup)
	{
      initialize();
      doSetup = false;
	}
	update();
  }
	
  //
  // start
  // Info..
  //
  public void start(Stage stage)
  {
    new Controller(locale, user, stage, this);
  }
}