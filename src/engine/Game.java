package engine;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

public abstract class Game implements KeyListener, MouseListener {
  public abstract void GameLoopCallback(long deltaTime, Controller controller);
}
