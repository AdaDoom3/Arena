import java.awt.im.InputContext;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ResourceBundle;
import javax.swing.KeyStroke;

//
// Properties
// Info..
//
public class Properties
{

  //
  // Variables
  //
  public ResourceBundle bundle = null;
  
  //
  // Constructor
  // Info..
  //
  public Properties(String name)
  {
    File resource = new File(".");
    URL[] urls = new URL[1];
    try
    {
      urls[0]=resource.toURI().toURL();
    }
    catch (MalformedURLException e)
    {
      e.printStackTrace();
    }
    ClassLoader loader = new URLClassLoader(urls);
    this.bundle = ResourceBundle.getBundle(name, InputContext.getInstance().getLocale(), loader);
  }
  
  //
  // getConstantString
  // Info..
  //
  public String getConstantString(String name)
  {
    return this.bundle.getString(name.toLowerCase());
  }

  //
  // getConstantInfo
  // Info...
  //
  public int getConstantInt(String name)
  {
    return Integer.parseInt(this.getConstantString(name));
  }

  //
  // getConstantBoolean
  // Info..
  //
  public boolean getConstantBoolean(String name)
  {
    return Boolean.parseBoolean(this.getConstantString(name));
  }

  //
  // getConstantKeyEvent
  // Info..
  //
  public int getConstantKeyEvent(String name)
  {
    return KeyStroke.getKeyStroke(this.getConstantString(name)).getKeyCode();
  }
}