import java.awt.Dimension;

public enum TileSet {
	SMALL(64,  64),
	LARGE(128, 128);
	
	private final int width;
	private final int height;
	TileSet(int width, int height){
		this.width  = width;
		this.height = height;
	}
	
	public Dimension toDimension(){
		return new Dimension(this.width, this.height);
	}
}
