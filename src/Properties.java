import java.util.ResourceBundle;
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
  public static TileSet getConstantTileSet(String id)
  {
	  return TileSet.valueOf(getConstantString(id));
  }

  //
  // Constants
  //
  public static final boolean TILE_SET = getConstantBoolean("world.");
  public static final boolean FULLSCREEN = getConstantBoolean("view.fullscreen");
  public static final String TITLE = getConstantString("view.title");
  public static final int WIDTH = getConstantInt("view.viewport.width");
  public static final int HEIGHT = getConstantInt("view.viewport.height");
  public static final int FRAME_RATE = getConstantInt("view.framerate");
  public static final int COLUMNS = getConstantInt("world.columns");
  public static final int ROWS = getConstantInt("world.rows");
  public static final int PLAYER_DELAY_MOVE = getConstantInt("entity.player.delay.move");
  public static final int PLAYER_DELAY_BULLET = getConstantInt("entity.player.delay.bullet");
  public static final String MOVE_NORTH = getConstantString("entity.player.move.north");
  public static final String MOVE_EAST = getConstantString("entity.player.move.east");
  public static final String MOVE_SOUTH = getConstantString("entity.player.move.south");
  public static final String MOVE_WEST = getConstantString("entity.player.move.west");
  public static final String LOB = getConstantString("entity.player.lob");
  public static final String MELEE = getConstantString("entity.player.melee");
}