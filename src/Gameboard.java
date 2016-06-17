import java.util.ArrayList;
import java.util.Random;
public class Gameboard {
	final int width,height;
	private ArrayList<Tile> tiles;
	private ArrayList<Byte> initialTiles; // for the reset purposes. This stores the layout of the tips only
	public Gameboard(int w, int h)
	{
		width = w;
		height = h;
		tiles = new ArrayList<Tile>();
		initialTiles = new ArrayList<Byte>();
		
		// I guess a need a helper class for constructing random puzzles using the graph properties
		Graph graph = new Graph(width,height);
		
	}
	public Gameboard(byte tips[][])
	{
		int h = tips.length, w = tips[0].length;
		width = w;
		height = h;
		tiles = new ArrayList<Tile>();
		for(int i = 0; i < h; i++)
		{
			for(int j = 0; j < w; j++)
			{
				tiles.add(new Tile(tips[i][j]));
			}
		}
		if(tiles.size() == 0) 
			throw new IllegalArgumentException("invalid 2D tips array");
	}
	
	public final int size()
	{
		return width * height;
	}
	
	public int freeTips(int x, int y)
	{
		int res = 0;
		byte mask = 0;
		// if the adjacentTiles has a tip pointing to the tile on (x,y), then the tip is not free
		ArrayList<Tile> adjTiles = outGoingTiles(x,y,true);
		Tile adjTile = null;
		for(int i  = 0; i < adjTiles.size();i++)
		{

			if( ( adjTile = adjTiles.get(i)) != null)
			{
				switch(i)
				{
					case 0: //up
						mask = 8;
						break;
					case 1: //left
						mask = 4;
						break;
					case 2: // down
						mask = 2;
						break;	
					case 3: // right
						mask = 1;
						break;
					default:
							throw new IllegalArgumentException("Number of out Going Tiles should not exceed 4");
				}
				if((mask & adjTile.getTips()) != 0)
				{
					res++;
				}
			}
		}
		return res;

	}
	public void reset()
	{
		int i = 0,attempts = 0;
		try
		{
			for(Tile t : tiles)
			{
				while(t.getTips() != initialTiles.get(i))
				{
					if(attempts >= 4) // prevent the game from hanging there in case something goes wrong
					{
						throw new RuntimeException("model is corrupted");
					}
					t.rotate();
					attempts++;
				}
				i++;
			}
		}catch(IndexOutOfBoundsException e)
		{
			throw new IndexOutOfBoundsException("List of tiles is corrupted");
		}
	}
	/**
	 * Check if the gameboard is solved by checking if the number of freeTips is 0
	 * @return
	 */
	public boolean isSolved()
	{
		for(int x = 0; x < height; x++)
		{
			for(int y = 0; y < width; y++)
			{
				if(freeTips(x,y) > 0) return false;
			}
		}
		return true;
	}
	public void rotate(int x,int y)
	{
		try
		{
			getTileByXY(x,y).rotate();	
		}catch(Exception e)
		{}		
	}
	private ArrayList<Tile> adjacentTiles(int x, int y)
	{
		ArrayList<Tile> res  = new ArrayList<Tile>();
		int xs[] = {x - 1, x, x + 1};
		int ys[] = {y - 1, y, y + 1};
		for(int tx : xs)
		{
			for(int ty : ys)
			{
				if(tx == x && ty == y) continue;
				try
				{
					// c is the coordinates of the adjacent tile
					int c = getIndexByXY(x,y);
					res.add(tiles.get(c));
				}catch(IllegalArgumentException e)
				{
					// out of bounds
					continue;
				}
			}
		}
		if(res.isEmpty()) return null;
		return res;
	}
	
	/**
	 * Get a list o tiles that the tips on the given coordinates are pointing to.
	 * If mathPosition is set to true, then the list returned will have exactly 4 elements,
	 * which corresponds to the 4 sides of the tile. If there is no tip in a side,
	 * the corresponding element will be null.
	 * @param x
	 * @param y
	 * @param matchPosition
	 * @return list of tiles that the tips on the given coordinates are pointing to.
	 */
	private ArrayList<Tile> outGoingTiles(int x,int y,boolean matchPosition)
	{
		ArrayList<Tile> res = new ArrayList<Tile>();
		byte tips = getTileByXY(x,y).getTips();
		byte mask = 8;
		while(mask > 0)
		{
			// has tile to the current position
			if ( (mask & tips) != 0)
			{
				try
				{
					switch(mask)
					{
						case 8: // up
							res.add(getTileByXY(x - 1,y));
							break;
						case 4: // left
							res.add(getTileByXY(x,y - 1));
							break;
						case 2: // down
							res.add(getTileByXY(x + 1,y));
							break;
						case 1: // right
							res.add(getTileByXY(x,y + 1));
							break;
						default:
							// what?
							throw new RuntimeException("unexpected mask");
							
					}
				}catch(IllegalArgumentException e)
				{
					// do nothing: it's normal to come to here
				}
				
			}else
			{
				if(matchPosition)
				{
					res.add(null);
				}
			}
			// and update the masks afterwards
			mask >>>= 1;
		}
		
		return res;
	}
	private int getIndexByXY(int x, int y)
	{
		int res = x * width + y;
		if(res < 0 || res >= x * y) 
			throw new IllegalArgumentException("invalid x,y coordinates");
		return res;
	}
	private Tile getTileByXY(int x, int y)
	{
		return tiles.get(getIndexByXY(x,y));
		
	}
}
