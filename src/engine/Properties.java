package engine;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ResourceBundle;

import javax.swing.KeyStroke;
public class Properties
{

  //
  // getConstant*
  // Info...
  //
  public static String getConstantString(String name)
  {
    return ResourceBundle.getBundle("user").getString(name.toLowerCase());
  }
  public static int getConstantInt(String name)
  {
    return Integer.parseInt(getConstantString(name));
  }
  public static boolean getConstantBoolean(String name)
  {
    return Boolean.parseBoolean(getConstantString(name));
  }
  public static Sprite.TileSet getConstantTileSet(String name)
  {
    return Sprite.TileSet.valueOf(getConstantString(name));
  }
  public static int getConstantKeyEvent(String name)
  {
    return KeyStroke.getKeyStroke(getConstantString(name)).getKeyCode();
  }
  public static Game getConstantGame(String name)
  {
    Game output = null;
    try {
      Class<?> clazz = Class.forName(getConstantString(name));
      Constructor<?> constructor = clazz.getConstructor();
      output = (Game)constructor.newInstance(new Object[] {});
    } catch (ClassNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (NoSuchMethodException | SecurityException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (InstantiationException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IllegalArgumentException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return output;
  }
  
  //
  // Constants
  //
  public static final String OWNER = getConstantString("view.owner");
  public static final boolean FULLSCREEN = getConstantBoolean("view.fullscreen");
  public static final String TITLE = getConstantString("view.title");
  public static final int WIDTH = getConstantInt("view.width");
  public static final int HEIGHT = getConstantInt("view.height");
  public static final int COLUMNS = getConstantInt("columns");
  public static final int ROWS = getConstantInt("rows");
  public static final Sprite.TileSet TILE_SET = getConstantTileSet("tileset");
  public static final int PLAYER_DELAY_MOVE = getConstantInt("player.delay.move");
  public static final int PLAYER_DELAY_BULLET = getConstantInt("player.delay.bullet");
  public static final Game GAME = getConstantGame("game.name");
}