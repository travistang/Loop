import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;


public class GamePanel extends JPanel implements MouseListener{

	private BufferedImage tileImages[][];
	private final int width,height; // number of tiles instead of component size
	private int imgSize; // record the image size. This is done during the construction of the Panel.
	// this assumes that the tile images are squares and it takes the height of the image as the image size.
	GameController controller;
	/**
	 * Create the panel.
	 */
	public GamePanel(int w,int h) {
		width = w;
		height = h;
		tileImages = new BufferedImage[w][h];
	}
	
	public void setTileImage(int x, int y, String imagePath) throws IOException
	{
		try
		{
			tileImages[x][y] = ImageIO.read(new File(imagePath));
			int xx;
			if(imgSize < ( xx = tileImages[x][y].getHeight())) 
			{
				imgSize = xx;
			}
		}catch(IndexOutOfBoundsException e)
		{
			
		}
	}
	
	public void rotate(int x, int y)
	{
		try
		{
			//source : http://www.java2s.com/Code/Java/Advanced-Graphics/RotatingaBufferedImage.htm
			AffineTransform tx = new AffineTransform();
			BufferedImage bimg = tileImages[x][y];
			tx.rotate(Math.PI/2,bimg.getWidth()/2,bimg.getHeight()/2);
			AffineTransformOp op = new AffineTransformOp(tx,AffineTransformOp.TYPE_BILINEAR);
			tileImages[x][y] = op.filter(bimg, null);
		}catch(IndexOutOfBoundsException e)
		{
			
		}
	}
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		// TODO:draw all the tile Images
		for(int i = 0; i < height; i ++)
		{
			for(int j = 0; j < width; j ++)
			{
				int xcoord = i*imgSize, ycoord = j * imgSize;
				g.drawImage(tileImages[i][j], xcoord,ycoord,null);
			}
		}
	}
	@Override
	public void mouseClicked(MouseEvent e)
	{
		// resolve the coordinates of the tiles clicked
		//debug
		System.out.println("clicked");
		int x = (e.getX() / imgSize) % imgSize,
				y = (e.getY() / imgSize) % imgSize;
		// notify game controller for that
		try
		{
			controller.notify(x,y);
		}catch(NullPointerException exp)
		{
			// do nothing
			System.err.println("Game controller is not associated to the game panel");
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
