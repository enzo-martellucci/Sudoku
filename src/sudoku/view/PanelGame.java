package sudoku.view;

import sudoku.ControllerGUI;
import sudoku.model.Sudoku;

import javax.swing.*;
import java.awt.*;

public class PanelGame extends JPanel
{
	private static final int BORDER_S = 2;
	private static final int BORDER_L = 4;

	private final ControllerGUI ctrl;
	private final Sudoku        model;

	public PanelGame(ControllerGUI ctrl, Sudoku model, Dimension screen)
	{
		this.ctrl  = ctrl;
		this.model = model;

		int size = (int) Math.min(0.9 * screen.width, 0.9 * screen.height);
		this.setPreferredSize(new Dimension(size, size));
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		// Compute size
		int[][] grid  = this.model.getGrid();
		int     size  = this.model.getSize();
		int     sizeC = size * size;

		int w = this.getWidth();
		int h = this.getHeight();

		int borderSum = (sizeC - size) * BORDER_S + (size + 1) * BORDER_L;
		int box       = Math.min((w - borderSum) / (sizeC + 2), (h - borderSum) / (sizeC + 2));
		int line      = borderSum + sizeC * box;

		int oX = (w - line) / 2, oY = (h - line) / 2;

		// Clear
		g.setColor(Color.LIGHT_GRAY);
		g.setFont(new Font("Arial", Font.BOLD, Math.max(12, Math.min(w, h) / 20)));
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		g.setColor(Color.BLACK);

		// Paint grid
		String      number;
		FontMetrics metrics = g.getFontMetrics();
		int         x, y;

		g.fillRect(oX, oY, line, BORDER_L);
		g.fillRect(oX, oY + line - BORDER_L, line, BORDER_L);
		g.fillRect(oX, oY, BORDER_L, line);
		g.fillRect(oX + line - BORDER_L, oY, BORDER_L, line);

		x = oX + BORDER_L + box;
		y = oY + BORDER_L + box;
		for (int l = 0; l < grid.length - 1; l++)
		{
			int border = l % size == size - 1 ? BORDER_L : BORDER_S;

			g.fillRect(x, oY, border, line);
			g.fillRect(oX, y, line, border);

			x += border + box;
			y += border + box;
		}

		// Paint numbers
		y = oY + BORDER_L;
		for (int l = 0; l < grid.length; l++)
		{
			x = oX + BORDER_L;
			for (int c = 0; c < grid[l].length; c++)
			{
				g.setColor(Color.WHITE);
				g.fillRect(x, y, box, box);

				if (grid[l][c] != 0)
				{
					g.setColor(Color.BLACK);
					number = String.valueOf(grid[l][c]);
					g.drawString(number, x + box / 2 - metrics.charWidth(number.charAt(0)) / 2, y + box / 2 + metrics.getHeight() / 2 - metrics.getDescent());
				}

				x += box + (c % size == size - 1 ? BORDER_L : BORDER_S);
			}
			y += box + (l % size == size - 1 ? BORDER_L : BORDER_S);
		}
	}
}
