import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.function.Consumer;
/**
 * Handle algorithms for the puzzle by treating it as a graph
 * @author Travis
 *
 */
public class Graph 
{
	private final int width,height;
	private ArrayList<Tile> tiles;
	
	public Graph(int w,int h)
	{
		width = w;
		height = h;
		tiles = new ArrayList<Tile>();
	}
	public Graph(int w, int h,ArrayList<Tile> tiles)
	{
		width = w;
		height = h;
		this.tiles = tiles;
	}
	public Graph(Gameboard gb)
	{
		height = gb.height;
		width = gb.width;
		tiles = gb.getTiles();
	}
	public abstract class Traverser
	{
		private ArrayList<Tile> visited;
		// determine which tips to go next
		public abstract byte policy(byte tips);
		// this function will be applied to current tile
		// Override this to do something more.
		// NOTE: remember to update the visited list or the loop may never ends
		public void apply(Tile tile,int x, int y)
		{
			if(!visited.contains(tile))
			{
				visited.add(tile);
			}
		}
		public final void resetHistory()
		{
			visited.clear();
		}
		public final boolean hasVisited(Tile t)
		{
			return visited.contains(t);
		}
		public final void traverse(int x, int y)
		{
			try
			{
				Tile t = tiles.get(getIndexByXY(x,y));	
				if(!hasVisited(t))
				{
					visited.add(t);
					apply(t,x,y);
					byte dir = policy(t.getTips());
					Tile next = nextTile(t,dir);
					int xy[] = getXYByIndex(tiles.indexOf(next));
					traverse(xy[0],xy[1]);
				}
			}catch(IndexOutOfBoundsException e)
			{
				return;
			}catch(StackOverflowError e)
			{
				throw new IllegalArgumentException("Invalid policy");
			}
		}
	}
	// get a reference of list of tiles
	// then work on it directly
	public void constructFullyConnectedPuzzles()
	{
		for(Tile t : tiles)
		{
			// fully connected: 1111
			t.setTips(Tile.ALL);
		}
		// then remove extra tips from the boundaries...
		// for each boundary with direction dir...
		for(byte dir : Tile.DIRS)
		{
			// for each index of the boundary of the tile dir...
			for(int ind : boundaryCoordinates(dir))
			{
				// toggle the tip of such tiles along that direction
				tiles.get(ind).toggleTips(dir);
			}
		}
	}
	// construct a random puzzles
	// the puzzle is guranteed to be valid
	public void constructRandomPuzzles()
	{
		// connect it first;
		constructFullyConnectedPuzzles();
		// now the tiles should be fully connected and is valid.
		// choose a number of random path ( from 1 to 6 )
		Random rand = new Random();
		int numPaths = rand.nextInt(6);
		// walk randomly 
		for(int i = 0; i < numPaths; i++)
		{
			// length of the path should cover at most half of the tiles
			int numSteps = rand.nextInt(width * height / 2);
			ArrayList<Tile> path = randomPath();
		}
	}

	public int[] randomCoordinates()
	{
		int res[] = new int[2];
		Random r = new Random();
		res[0] = r.nextInt(height);
		res[1] = r.nextInt(width);
		return res;
	}
	public byte randomDirection()
	{
		Random r = new Random();
		return Tile.DIRS[r.nextInt(4)];
	}
	/**
	 * Determine if a tile on a given index lies on 
	 * @param ind
	 * @param side
	 * @return
	 */
	public boolean isOnSide(int ind, byte side)
	{
		switch(side)
		{
			case Tile.UP:
				return ind >= 0 && ind < height;
			case Tile.DOWN:
				return ind >= width*(height - 1) && ind < width * height;
			case Tile.LEFT:
				return ind % width == 0 && ind >= 0 && ind < width * height;
			case Tile.RIGHT:
				return ind % (width - 1) == 0 && ind >= 0 && ind < width * height;
			case Tile.ALL: // check if the index is one boundary.
				return isOnSide(ind,Tile.UP) || isOnSide(ind,Tile.DOWN) || isOnSide(ind,Tile.LEFT) || isOnSide(ind,Tile.RIGHT);
			default:
				throw new IllegalArgumentException("The given side cannot be verified");
		}
	}
	public boolean isInRange(int x, int y)
	{
		return x * width + height < width * height && x >= 0 && y >= 0;
	}
	public int[] getXYByIndex(int ind)
	{
		int[] res = new int[2];
		res[0] = ind / width;
		res[1] = ind % width;
		return res;
	}
	public int getIndexByXY(int x, int y)
	{
		return x * width + y;
	}
	public Tile nextTile(Tile cur, byte dir)
	{
		int i = 0;
		if((cur.getTips() & dir) != 0)
		{
			switch(dir)
			{
				case Tile.UP:
					i = tiles.indexOf(cur) - width;
					break;
				case Tile.DOWN:
					i = tiles.indexOf(cur) + width;
					break;
				case Tile.LEFT:
					i = tiles.indexOf(cur) - 1;
					break;
				case Tile.RIGHT:
					i = tiles.indexOf(cur) + 1;
					break;
				default:
						throw new IllegalArgumentException("Invalid direction for getting next tile");
				
			}
			// check if the index is in range
			if(i >= 0 && i < tiles.size())
			{
				return tiles.get(i);
			}
		}
		
		// if there are no tips along the given direction 
		return null;
	}
	
	public Tile randomNextTile(Tile cur)
	{
		if(cur.numTips == 0) return null;
		Tile t;
		// randomly pick a outgoing tip
		// you have to be very unlucky to wait for so long
		while((t = nextTile(cur,randomDirection())) != null)
		{}
		return t;
	}
	
	/**
	 * get a list of tiles visited by a random path, which starts from tiles of (x,y) and has length "length"
	 * The path generated can visit a single node more than once.
	 * But each tip is guaranteed to use once only. TODO: how ?
	 * The starting tile is also counted as length. So a path with length 1 visited just the starting tile
	 * If the length is 0 then the list has size 0
	 * If the length is any other value exception will be throw
	 * 
	 * @param x
	 * @param y
	 * @param length
	 * @return List of tile visited by a random path.
	 */
	public ArrayList<Tile> randomPath(int x, int y, int length)
	{
		if(length < 0) throw new IllegalArgumentException("Length must be greater than or equal to 0");
		ArrayList<Tile> res = new ArrayList<Tile>();
		Tile cur = tiles.get(x * width + y);
		res.add(cur);
		int i = length - 1;
		while(i-- > 0)
		{
			Tile temp = randomNextTile(cur);
			// check that the tip is not used before
			int j;
			for(j = 0; j < res.size() - 1; j++)
			{
				if(tiles.get(j).equals(cur) && tiles.get(j + 1).equals(temp))
				{
					i++;
					j = -1;
					break;
				}
			}
			if(j != -1)
			{
				// can add the tip
				res.add(temp);
				cur = temp;
			}
		}
		return res;
	}
	public byte getDirectionBetweenTiles(Tile from, Tile to)
	{
		for(byte dir : Tile.DIRS)
		{
			if(nextTile(from,dir) == to) return dir;
		}
		return -1;
	}
	public int[] boundaryCoordinates(byte side)
	{
		// so here is the calculation...: number of tiles on the boundary = (w + h) * 2 - 4. 4 is the extra corner...
		int[] res;
		switch(side)
		{
			case Tile.UP:
			{	
				res = new int[width];
				for(int i = 0; i < width; i++)
				{
					res[i] = i;
				}
			}
			break;
			case Tile.DOWN:
			{
				res = new int[width];
				for(int i = width * (height - 1); i < width * height; i++)
				{
					res[i] = i;
				}
			}
			break;
			case Tile.LEFT:
			{
				res = new int[height];
				for(int i = 0; i < width*height; i += width)
				{
					res[i] = i;
				}
			}
			break;
			case Tile.RIGHT:
			{
				res = new int[height];
				for(int i = width - 1; i < width* height; i += width)
				{
					res[i] = i;
				}
			}
			break;
			case Tile.ALL:
			{
				// first gather all values
				Set<Integer> list = new HashSet<Integer>();
				// iterate through all possible directions and add it to a list
				// since it's a set no components will be duplicated
				byte dirs[] = {Tile.UP,Tile.DOWN,Tile.LEFT,Tile.RIGHT};
				for(byte tip : dirs)
				{
					for(int i : boundaryCoordinates(tip))
					{
						list.add(i);
					}
				}
				Integer[] temp = (Integer[])(list.toArray());
				res = new int[temp.length];
				// then copy the Integer Array to primitive array
				for(int i = 0; i < temp.length; i++)
				{
					res[i] = temp[i];
				}
			}
				
			default:
				throw new IllegalArgumentException("Invalid parameters for boundaryCoordinates()");

		}	
		return res;
	}
	// this is a difficult one..
	// for now just check if the number of tiles is odd 
	// and that the boundary 
	public boolean isSolvable(ArrayList<Tile> tiles)
	{
		return (width * height) % 2 != 1;
	}
}
