public class Runner{
  public static void main(String[] args){
    Grid<Sprite> level = new Grid<Sprite>();
    String result = "";
    Long start = System.currentTimeMillis();
    Long current = System.currentTimeMillis() - SPEED;
    frame.setSize(COLUMNS * IMAGE_SIZE + insets.left + insets.right, ROWS * IMAGE_SIZE + insets.top);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setResizable(false);
    try{frame.setIconImage(new ImageIcon(ImageIO.read(new File(ICON + EXTENSION_IMAGE))).getImage());
    }catch(Exception error){}
    for(int i = 1;i <= getConstant("player.count");i++) new Player(i).put(level);
    for(int i = 1;i <= getConstant("shield.count");i++) new Shield().put(level);
    for(int i = 1;i <= getConstant("bomb.count");i++) new Bomb().put(level);
    for(int i = 1;i <= getConstant("bot.count");i++) new Bot().put(level);
    Clip song = getSound(SOUND_SONG);
    int startCount = getConstant("bot.count");
    song.start();
    label = new JLabel(new ImageIcon(buffer));
    frame.add(label);
    while(true)if(current + SPEED < System.currentTimeMillis()){
      if((System.currentTimeMillis() - start) / 1000 > TIME_LIMIT){
        if(level.getCount(Bot.class) > 0) result = TEXT_COOP_LOSE;
        else result = TEXT_VS_TIE;
        break;
      }
      if(startCount > 0 && level.getCount(Bot.class) <= 0){
        result = TEXT_COOP_WIN + (System.currentTimeMillis() - start) / 1000 + " seconds";
        break;
      }
      if(startCount <= 0 && level.getCount(Player.class) < 2){
        Player winner = (Player)level.getInstance(new Player());
        result = TEXT_VS_WIN + winner.getID();
        break;
      }
      if(startCount > 0 && level.getCount(Player.class) <= 0){
        result = TEXT_COOP_DIE;
        break;
      }
      for(Sprite sprite : level.getAll()) try{sprite.step();}catch(Exception error){}
      current = System.currentTimeMillis();
    }
    song.stop();
    playSound(SOUND_END);
    JOptionPane.showMessageDialog(null, result, TITLE, JOptionPane.PLAIN_MESSAGE, null);
    frame.dispose();
  }
}