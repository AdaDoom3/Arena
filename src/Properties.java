import java.util.ResourceBundle;

import javax.swing.KeyStroke;

import javaawt.event.KeyEvent;

public class Properties
{

  //
  // getConstant*
  // Info...
  //
  public static String getConstantString(String id)
  {
    return ResourceBundle.getBundle("user").getString(id.toLowerCase());
  }
  public static int getConstantInt(String id)
  {
    return Integer.parseInt(getConstantString(id));
  }
  public static boolean getConstantBoolean(String id)
  {
	  return Boolean.parseBoolean(getConstantString(id));
  }
  public static Assets.Sprite.TileSet getConstantTileSet(String id)
  {
	  return Assets.Sprite.TileSet.valueOf(getConstantString(id));
  }
  public static int getConstantKeyEvent(String id)
  {
    return KeyStroke.getKeyStroke(Character. getConstantString(id), 0).getKeyCode();
  }

  public static String foo = KeyEvent.VK_W.toString();
  
  //
  // Constants
  //
  public static final boolean FULLSCREEN             = getConstantBoolean("view.fullscreen");
  public static final String  TITLE                  = getConstantString("view.title");
  public static final int     WIDTH                  = getConstantInt("view.viewport.width");
  public static final int     HEIGHT                 = getConstantInt("view.viewport.height");
  public static final int     COLUMNS                = getConstantInt("world.columns");
  public static final int     ROWS                   = getConstantInt("world.rows");
  public static final Assets.Sprite.TileSet TILE_SET = getConstantTileSet("world.tileset");
  public static final int     PLAYER_DELAY_MOVE      = getConstantInt("entity.player.delay.move");
  public static final int     PLAYER_DELAY_BULLET    = getConstantInt("entity.player.delay.bullet");
  public static final int     MOVE_NORTH             = getConstantKeyEvent("entity.player.move.north");
  public static final int     MOVE_EAST              = getConstantKeyEvent("entity.player.move.east");
  public static final int     MOVE_SOUTH             = getConstantKeyEvent("entity.player.move.south");
  public static final int     MOVE_WEST              = getConstantKeyEvent("entity.player.move.west");
  public static final int     LOB                    = getConstantKeyEvent("entity.player.lob");
  public static final int     MELEE                  = getConstantKeyEvent("entity.player.melee");
}