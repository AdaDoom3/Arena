import java.awt.Image;
import java.awt.image.BufferedImage;

public class Sprite{

	private BufferedImage image = null;
	
	public Sprite(BufferedImage image){
		this.image = image;
	}
	
	public Image getImage() {
		return this.image;
	}
}