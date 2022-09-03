package sudoku;

import sudoku.model.Selector;
import sudoku.model.Sudoku;
import sudoku.view.ViewGUI;

public class ControllerGUI
{
	// Attributes
	private final Sudoku   sudoku;
	private final Selector selector;
	private final ViewGUI  view;


	// Constructor
	public ControllerGUI(int level)
	{
		this.sudoku   = new Sudoku(level);
		this.selector = new Selector(level);
		this.view     = new ViewGUI(this, this.sudoku, this.selector);
	}


	// Methods
	public void select(char direction)
	{
		this.selector.select(direction);
		this.view.maj();
	}

	public void select(int l, int c)
	{
		this.selector.select(l, c);
		this.view.maj();
	}

	public void unselect()
	{
		this.selector.unselect();
		this.view.maj();
	}

	public void place(int digit)
	{
		if (!this.selector.isSelected() || !this.selector.place(digit))
			return;

		if (this.selector.isReady())
		{
			this.sudoku.place(this.selector.getL(), this.selector.getC(), this.selector.consume());
			this.selector.select('R');
		}

		this.view.maj();
	}

	public void place()
	{
		this.sudoku.place(this.selector.getL(), this.selector.getC(), this.selector.consume());
		this.selector.select('R');
		this.view.maj();
	}

	public void remove()
	{
		if (!this.selector.isSelected())
			return;

		this.sudoku.remove(this.selector.getL(), this.selector.getC());
		this.selector.select('R');
		this.view.maj();
	}


	// Main
	public static void main(String[] args)
	{
		new ControllerGUI(5);
	}
}
