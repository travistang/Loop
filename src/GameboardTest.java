import static org.junit.Assert.*;

import org.junit.Test;


public class GameboardTest {

	@Test
	public void testGameboardIntInt() {
		Gameboard b = new Gameboard(10,12);
		assertEquals("Gameboard(int,int)",120,b.size());
	}

	@Test
	public void testGameboardByteArrayArray() {
		byte array[][] = {{Tile.UP | Tile.LEFT,Tile.RIGHT | Tile.DOWN},
						  {Tile.RIGHT | Tile.UP,Tile.DOWN | Tile.LEFT}};
		Gameboard b = new Gameboard(array);
		assertEquals("Gameboard(byte[][])",4,b.size());
		assertEquals("Gameboard(byte[][])",2,b.freeTips(0,0));
	}

	@Test
	public void testSize() {
		Gameboard b = new Gameboard(10,12);
		assertEquals("size() 1",120,b.size());
		byte array[][] = {{Tile.UP | Tile.LEFT,Tile.RIGHT | Tile.DOWN},
				  {Tile.RIGHT | Tile.UP,Tile.DOWN | Tile.LEFT}};
		b = new Gameboard(array);
		assertEquals("size() 2",4,b.size());
	}

	@Test
	public void testFreeTips() {
		Gameboard b = new Gameboard(4,4);
		Graph g = new Graph(b);
		g.constructFullyConnectedPuzzles();
		assertEquals("freeTips() 1", b.freeTips(0,0),0);
		assertEquals("freeTips() 2", b.freeTips(1,1),0);
		assertEquals("freeTips() 3", b.freeTips(3,3),0);
	}

	@Test
	public void testReset() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsSolved() {
		fail("Not yet implemented");
	}

	@Test
	public void testRotate() {
		fail("Not yet implemented");
	}

}
