import static org.junit.Assert.*;

import org.junit.Test;


public class TileTest {
	@Test
	public void testTileByte() {
		Tile tile = new Tile(Tile.UP);
		assertEquals("Tile(byte) works",Tile.UP,tile.getTips());
		
	}

	@Test
	public void testTileBooleanArray() {
		boolean tips[] = {true,true,false,true};
		Tile tile = new Tile(tips);
		assertEquals("Tile(boolean[]) works",Tile.UP | Tile.RIGHT | Tile.LEFT, tile.getTips());
	}

	@Test
	public void testTile() {
		Tile tile = new Tile();
		assertEquals("Tile()",0,tile.getTips());
	}

	@Test
	public void testSetTips() {
		Tile tile = new Tile(Tile.UP);
		tile.setTips(Tile.DOWN);
		assertEquals("setTips()",Tile.DOWN,tile.getTips());
	}

	@Test
	public void testToggleTips() {
		Tile tile = new Tile((byte)(Tile.UP | Tile.DOWN | Tile.LEFT));
		tile.toggleTips(Tile.RIGHT);
		assertEquals("toggleTips()",Tile.ALL,tile.getTips());
	}

	@Test
	public void testRotate() {
		Tile tile = new Tile((byte)(Tile.UP | Tile.DOWN));
		tile.rotate();
		assertEquals("rotate()",Tile.LEFT | Tile.RIGHT, tile.getTips());
	}

	@Test
	public void testNumberOfTips() {
		Tile tile = new Tile((byte)(Tile.UP | Tile.LEFT | Tile.DOWN));
		assertEquals("numberOfTips()",3,tile.numberOfTips());
	}

}
