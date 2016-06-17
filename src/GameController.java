
public class GameController {
	private GamePanel panel;
	private Gameboard board;
	GameController()
	{
		panel = null;
		board = null;
	}
	GameController(GamePanel panel,Gameboard board)
	{
		this.panel = panel;
		this.board = board;
	}
	/**
	 * This function is used by the GamePanel to notify the controller.
	 * It should only be triggered by the mouse click event
	 * and it is the responsibility of the GamePanel to resolve the coordinates
	 * of the corresponding tiles ( in terms of number of tiles instead of pixels)
	 * @param x
	 * @param y
	 */
	public void notify(int x, int y)
	{
		// rotate the corresponding tiles
		// should be rotated by the controllers. i.e. here
		panel.rotate(x, y);
		// now the models
		panel.rotate(x, y);
		update();
	}
	public void setGamePanel(GamePanel panel)
	{
		this.panel = panel;
	}
	/**
	 * this updates a few things..
	 * 1. the game condition: if the game has been solved then just end the game
	 * 2. what? I cant think of it...
	 * TODO: perhaps undo?
	 */
	public void update()
	{
		if(board.isSolved())
		{
			//er.. do something that notifies the user that it's solved.
			System.out.println("You won!");
		}
	}
	/**
	 * Reset the game view
	 */
	public void reset()
	{
		board.reset();
	}
}
