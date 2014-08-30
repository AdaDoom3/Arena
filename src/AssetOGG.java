public class AssetOGG{
  public static Clip getSound(String path){
    try{
      Clip sound = (Clip)AudioSystem.getLine(new Line.Info(Clip.class));
      sound.open(AudioSystem.getAudioInputStream(new File(path + EXTENSION_AUDIO)));
      return sound;
    }catch(Exception error){return null;}
  }
  public static void playSound(String path){
    getSound(path).start();
  }
}
