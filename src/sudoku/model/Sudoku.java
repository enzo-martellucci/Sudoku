package sudoku.model;

public class Sudoku
{
	private final int[][] grid;
	private       int     nbPlaced;

	public Sudoku()
	{
		this.grid     = new int[9][9];
		this.nbPlaced = 0;

		for (int l = 0; l < this.grid.length; l++)
		     this.grid[l][l] = l + 1;
	}

	public int[][] getGrid()
	{
		return this.grid;
	}
	public int getNbPlaced()
	{
		return this.nbPlaced;
	}
	public int getTotal()
	{
		return this.grid.length * this.grid[0].length;
	}
	
	public void place(int l, int c, int placed)
	{
		this.grid[l][c] = placed;
		this.nbPlaced++;
	}
}
