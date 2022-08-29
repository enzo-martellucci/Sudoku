package sudoku;

import sudoku.model.Sudoku;
import sudoku.view.ViewCUI;

public class ControllerCUI
{
	private Sudoku  model;
	private ViewCUI view;

	public ControllerCUI()
	{
		this.model = new Sudoku();
		this.view  = new ViewCUI(this, this.model);

		char action = ' ';
		while (action != 'Q')
		{
			action = this.view.chooseOptions();

			switch (action)
			{
				case 'D' -> this.view.showGame();
				case 'F' -> this.view.fill();
				case 'A' -> this.autoSolve();
			}
		}

		this.view.quit();
	}

	public void autoSolve()
	{
		System.out.println("TODO");
	}

	public void place(int l, int c, int placed)
	{
		this.model.place(l, c, placed);
	}


	public static void main(String[] args)
	{
		new ControllerCUI();
	}
}
