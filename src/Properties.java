import java.util.ResourceBundle;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
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
  public static String getConstantKeyEvent(String name)
  {
    return KeyStroke.getKeyStroke(getConstantString(name)).toString();
  }
  
  //
  // Constants
  //
  public static final boolean FULLSCREEN = getConstantBoolean("view.fullscreen");
  public static final String TITLE = getConstantString("view.title");
  public static final int WIDTH = getConstantInt("view.viewport.width");
  public static final int HEIGHT = getConstantInt("view.viewport.height");
  public static final int COLUMNS = getConstantInt("world.columns");
  public static final int ROWS = getConstantInt("world.rows");
  public static final Sprite.TileSet TILE_SET = getConstantTileSet("world.tileset");
  public static final int PLAYER_DELAY_MOVE = getConstantInt("entity.player.delay.move");
  public static final int PLAYER_DELAY_BULLET = getConstantInt("entity.player.delay.bullet");
  public static final String MOVE_NORTH = getConstantKeyEvent("entity.player.move.north");
  public static final String MOVE_EAST = getConstantKeyEvent("entity.player.move.east");
  public static final String MOVE_SOUTH = getConstantKeyEvent("entity.player.move.south");
  public static final String MOVE_WEST = getConstantKeyEvent("entity.player.move.west");
  public static final String LOB = getConstantKeyEvent("entity.player.lob");
  public static final String MELEE = getConstantKeyEvent("entity.player.melee");
}