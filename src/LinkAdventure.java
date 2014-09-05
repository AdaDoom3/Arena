

import engine.Controller;
import engine.Game;
import engine.Properties;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;


public class LinkAdventure extends Game {

  // 
  // Impulse
  // Info..
  //
  public enum Impulse
  {
    MOVE_NORTH(Properties.getConstantKeyEvent("player.move.north")),
    MOVE_EAST(Properties.getConstantKeyEvent("player.move.east")),
    MOVE_SOUTH(Properties.getConstantKeyEvent("player.move.south")),
    MOVE_WEST(Properties.getConstantKeyEvent("player.move.west")),
    MELEE(Properties.getConstantKeyEvent("player.melee")),
    LOB(Properties.getConstantKeyEvent("player.lob"));
    
    private int keyCode;
    
    private Impulse(int keyCode){
      this.keyCode = keyCode;
    }
    
    public char toChar(){
      return (char)this.keyCode;
    }
  }

  //
  // ActionState
  // Info...
  //
  public enum ActionState
  {
    //
    // Values
    //
    DIE,
    LOB,
    MOVE,
    STAND,
    MELEE;
  }
/*
  
  //
  // Link
  // Info..
  //
  public static class Link extends Base
  {

    //
    // Constructor
    // Info..
    //
    public Link
    {
      super("Link");
      addAction(ActionState.DIE);
      addAction(ActionState.MOVE);
      addAction(ActionState.STAND);
      addAction(ActionState.MELEE);
      addAction(ActionState.LOB);
    }
  }
  
*/  
  
  @Override
  public void keyTyped(KeyEvent e) {
    // TODO Auto-generated method stub
  }

  @Override
  public void keyPressed(KeyEvent e) {
    System.out.println("Key Pressed: " + e.getKeyChar());
  }

  @Override
  public void keyReleased(KeyEvent e) {
    System.out.println("Key Released: " + e.getKeyChar());
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    // TODO Auto-generated method stub
  }

  @Override
  public void mousePressed(MouseEvent e) {
    System.out.println("Mouse Pressed: " + e.getButton() + " ("+e.getX()+", "+e.getY()+")");
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    System.out.println("Mouse Released: " + e.getButton() + " ("+e.getX()+", "+e.getY()+")");
  }

  @Override
  public void mouseEntered(MouseEvent e) {
    System.out.println("Mouse Entered" + " (" + e.getXOnScreen() + ", " + e.getYOnScreen() + ")");
  }

  @Override
  public void mouseExited(MouseEvent e) {
    System.out.println("Mouse Exited" + " (" + e.getXOnScreen() + ", " + e.getYOnScreen() + ")");
  }

  @Override
  public void GameLoopCallback(long deltaTime, Controller controller) {
    // TODO Auto-generated method stub
  }

}
