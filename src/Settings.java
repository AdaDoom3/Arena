import java.util.*;
public class Settings{
  public static final String RESOURCE_NAME = "Settings";
  public static String getConstantString(String id){
    return ResourceBundle.getBundle(RESOURCE_NAME).getString(id.toLowerCase());
  }
  public static int getConstantInt(String id){
    return Integer.parseInt(getConstantString(id));
  }
  public static boolean getConstantBoolean(String id){
	  return Boolean.parseBoolean(getConstantString(id));
  }
  public static TileSet getConstantTileSet(String id){
	  return TileSet.valueOf(getConstantString(id));
  }
  public static final String  TITLE          = getConstantString("view.title");
  public static final int     SPEED          = getConstantInt("view.speed");
  public static final boolean FULLSCREEN     = getConstantBoolean("view.fullscreen");
  public static final int     WIDTH          = getConstantInt("view.viewport.width");
  public static final int     HEIGHT         = getConstantInt("view.viewport.height");
  public static final int     RENDERER_SPEED = getConstantInt("renderer.speed");
  public static final int     ROWS           = getConstantInt("world.rows");
  public static final int     COLUMNS        = getConstantInt("world.columns");
  public static final TileSet TILE_SET       = getConstantTileSet("world.tileSet");
}