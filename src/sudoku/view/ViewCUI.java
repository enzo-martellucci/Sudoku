package sudoku.view;

import sudoku.ControllerCUI;
import sudoku.model.Sudoku;

import java.util.Scanner;

public class ViewCUI
{
	private static final String CLEAR    = "\033[H\033[2J";
	private static final String RESET    = "\u001B[0m";
	private static final String BG_GREEN = "\u001B[42m";
	private static final String RED      = "\u001B[31m";

	private final Scanner console;

	private final ControllerCUI ctrl;
	private final Sudoku        model;

	private final String line;
	private final String col;

	public ViewCUI(ControllerCUI ctrl, Sudoku model)
	{
		this.console = new Scanner(System.in);

		this.ctrl  = ctrl;
		this.model = model;

		int[][] grid = this.model.getGrid();

		this.line = RED + "---" + "+---".repeat(grid[0].length / 3 - 1) + RESET;
		this.col  = RED + '|' + RESET;
	}

	public void showGame()
	{
		this.showGame(-1, -1);
	}

	public void showGame(int lSelected, int cSelected)
	{
		int[][]       grid    = this.model.getGrid();
		StringBuilder display = new StringBuilder();

		display.append(String.format("Sudoku (%2d / %2d)\n\n", this.model.getNbPlaced(), this.model.getTotal()));
		for (int l = 0; l < grid.length; l++)
		{
			for (int c = 0; c < grid[l].length; c++)
			{
				if (l == lSelected && c == cSelected)
					display.append(BG_GREEN).append(grid[l][c] == 0 ? "." : grid[l][c]).append(RESET);
				else
					display.append(grid[l][c] == 0 ? "." : grid[l][c]);

				if (c % 3 == 2 && c != grid[l].length - 1)
					display.append(this.col);
			}

			display.append('\n');
			if (l % 3 == 2 && l != grid.length - 1)
				display.append(this.line).append('\n');
		}
		display.append('\n');

		System.out.println(CLEAR);
		System.out.println(display);
	}

	public char chooseOptions()
	{
		String choice;

		System.out.println("What do you want to do :");
		System.out.println("[D]isplay the grid");
		System.out.println("[F]ill the grid");
		System.out.println("[A]uto solve the grid");
		System.out.println("[Q]uit");

		System.out.print("Your choice : ");
		choice = this.console.nextLine();
		return choice == null || choice.isBlank() || choice.length() > 1 ? ' ' : choice.charAt(0);
	}

	public void fill()
	{
		int[][] grid = this.model.getGrid();
		String  input;

		out:
		for (int l = 0; l < grid.length; l++)
			for (int c = 0; c < grid[0].length; c++)
			{
				this.showGame(l, c);

				do
				{
					System.out.print("Your choice (1-9, Q to quit, Enter to skip) : ");
					input = console.nextLine();
				}
				while (input != null && !input.isEmpty() && (input.length() > 1 || input.charAt(0) != 'Q' && (input.charAt(0) < '1' || input.charAt(0) > '9')));

				if (input == null || input.isEmpty())
					continue;
				if (input.charAt(0) == 'Q')
					break out;

				this.ctrl.place(l, c, Integer.parseInt(input));
			}
	}

	public void quit()
	{
		System.out.println("Good Bye!");
	}
}
