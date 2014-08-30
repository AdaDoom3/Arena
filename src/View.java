
    BufferedImage background = null;
    JLabel label = new JLabel();
    try{background = ImageIO.read(new File(BACKGROUND + EXTENSION_IMAGE));}catch(Exception error){}
    BufferedImage buffer = new BufferedImage(COLUMNS * IMAGE_SIZE, ROWS * IMAGE_SIZE, background.getType());
    Graphics2D graphics = buffer.createGraphics();
    JFrame frame = new JFrame(TITLE);
    frame.setVisible(true);
    Insets insets = frame.getInsets();
      for(Sprite sprite : level.getAll()){
        if(sprite.color != null) graphics.setXORMode(sprite.color);
        AffineTransform transform = AffineTransform.getTranslateInstance(
          sprite.getLocation().getX() * IMAGE_SIZE,
          sprite.getLocation().getY() * IMAGE_SIZE);
        transform.rotate(Math.toRadians(sprite.getDirection()), IMAGE_SIZE / 2.0, IMAGE_SIZE / 2.0);
        graphics.drawImage(sprite.getImage(), transform, null);
        if(sprite.color != null) graphics.setPaintMode();
      }
      label.repaint(0, 0, 0, frame.getWidth(), frame.getHeight());
      frame.setVisible(true);
      graphics.drawImage(background, 0, 0, null);