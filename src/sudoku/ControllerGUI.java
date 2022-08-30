package sudoku;

import sudoku.model.Sudoku;
import sudoku.view.ViewGUI;

public class ControllerGUI
{
	private final Sudoku  model;
	private final ViewGUI view;


	public ControllerGUI(int size)
	{
		this.model = new Sudoku(size);
		this.view  = new ViewGUI(this, this.model);
	}

	public void place(int l, int c, int placed)
	{
		this.model.place(l, c, placed);
		this.view.maj();
	}

	public void unplace(int l, int c)
	{
		this.model.unplace(l, c);
		this.view.maj();
	}


	public static void main(String[] args)
	{
		new ControllerGUI(3);
	}
}
