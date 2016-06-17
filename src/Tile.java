import java.util.Arrays;


public class Tile {
	private byte tips;
	public final int numTips = numberOfTips();
	
	public static final byte UP = 8; // 1000
	public static final byte RIGHT = 4; // 0100
	public static final byte DOWN = 2; // 0010
	public static final byte LEFT = 1; // 0001
	public static final byte ALL = UP & RIGHT & DOWN & LEFT; // 1111
	public static final byte DIRS[] = {UP,RIGHT,DOWN,LEFT};
	public static final byte NONE = 0; // 0000

	public Tile(byte tips)
	{
		this.tips = tips;
	}
	public Tile(boolean tips[])
	{
		if(tips.length == 4)
		{
			int mask = 8;
			for(boolean l : tips)
			{
				if(l) this.tips += mask;
				mask >>>= 1;
			}			
		}
	}
	public Tile()
	{
		this.tips = 0;
		
	}
	// only getters are defined as it will be messy if it's possible to modify tips
	public byte getTips()
	{
		return tips;
	}
	// ok, so setters matters as well...
	public void setTips(byte tips)
	{
		this.tips = tips;
	}
	public void toggleTips(byte mask)
	{
		// no multiple toggling...
		if(Arrays.asList(DIRS).contains(mask))
		{
			throw new IllegalArgumentException("The given mask is invalid");
		}
		//TODO: test this
		if((tips & mask) == 0 ) tips &= mask;
		else tips &= ~mask;
	}
	// shift all the Tile clockwise
	public void rotate()
	{
		boolean b = tips % 2 != 0;
		tips >>>= 1;
		if (b)
		{
			tips |= 8; // 1000 in binary
		}
	}
	
	public int numberOfTips()
	{
		int mask = 8,count = 0;
		while(mask > 0)
		{
			if((mask & tips) > 0)
			{
				count++;
			}
			mask >>>= 1;
		}
		return count;
	}
	
}
