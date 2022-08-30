package sudoku.view;

import sudoku.ControllerGUI;
import sudoku.model.Sudoku;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class PanelGame extends JPanel implements KeyListener
{
	private static final int BORDER_S = 2;
	private static final int BORDER_L = 4;

	private final ControllerGUI ctrl;
	private final Sudoku        model;

	private int lSelected;
	private int cSelected;

	public PanelGame(ControllerGUI ctrl, Sudoku model, Dimension screen)
	{
		this.ctrl  = ctrl;
		this.model = model;

		int size = (int) Math.min(0.9 * screen.width, 0.9 * screen.height);
		this.setPreferredSize(new Dimension(size, size));

		this.addKeyListener(this);
		this.setFocusTraversalKeysEnabled(false);
		this.unselect();
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		// Compute size
		int[][] grid   = this.model.getGrid();
		int     size   = this.model.getSize();
		int     length = grid.length;

		int w = this.getWidth();
		int h = this.getHeight();

		int borderSum = (length - size) * BORDER_S + (size + 1) * BORDER_L;
		int box       = Math.min((w - borderSum) / (length + 2), (h - borderSum) / (length + 2));
		int line      = borderSum + length * box;

		Point o = new Point((w - line) / 2, (h - line) / 2);

		// Clear
		g.setColor(Color.LIGHT_GRAY);
		g.setFont(new Font("Arial", Font.BOLD, (int) Math.max(12, Math.min(w, h) / (20 + Math.log10(length) * 5))));
		g.fillRect(0, 0, w, h);
		g.setColor(Color.BLACK);

		// Paint grid
		String      number;
		FontMetrics metrics = g.getFontMetrics();
		Point       p       = new Point(o.x + BORDER_L + box, o.y + BORDER_L + box);

		g.fillRect(o.x, o.y, line, BORDER_L);
		g.fillRect(o.x, o.y + line - BORDER_L, line, BORDER_L);
		g.fillRect(o.x, o.y, BORDER_L, line);
		g.fillRect(o.x + line - BORDER_L, o.y, BORDER_L, line);

		for (int l = 0; l < length - 1; l++)
		{
			int border = l % size == size - 1 ? BORDER_L : BORDER_S;

			g.fillRect(p.x, o.y, border, line);
			g.fillRect(o.x, p.y, line, border);

			p.x += border + box;
			p.y += border + box;
		}

		// Paint numbers
		p.y = o.y + BORDER_L;
		for (int l = 0; l < length; l++)
		{
			p.x = o.x + BORDER_L;
			for (int c = 0; c < length; c++)
			{
				g.setColor(l == this.lSelected && c == this.cSelected ? Color.ORANGE : Color.WHITE);
				g.fillRect(p.x, p.y, box, box);

				if (grid[l][c] != 0)
				{
					g.setColor(Color.BLACK);
					number = String.valueOf(grid[l][c]);
					g.drawString(number, p.x + box / 2 - metrics.stringWidth(number) / 2, p.y + box / 2 + metrics.getHeight() / 2 - metrics.getDescent());
				}

				p.x += box + (c % size == size - 1 ? BORDER_L : BORDER_S);
			}
			p.y += box + (l % size == size - 1 ? BORDER_L : BORDER_S);
		}
	}

	private void unselect()
	{
		this.lSelected = -1;
		this.cSelected = -1;
		this.repaint();
	}

	private void select(char dir)
	{
		int length = this.model.getGrid().length;

		if (this.lSelected == -1)
		{
			this.lSelected = this.cSelected = dir == 'U' || dir == 'L' ? length - 1 : 0;
			this.repaint();
			return;
		}

		switch (dir)
		{
			case 'U':
				this.lSelected--;
				if (this.lSelected == -1)
				{
					this.lSelected = length - 1;
					this.cSelected--;
					if (this.cSelected == -1)
						this.cSelected = length - 1;
				}
				break;
			case 'D':
				this.lSelected++;
				if (this.lSelected == length)
				{
					this.lSelected = 0;
					this.cSelected++;
					if (this.cSelected == length)
						this.cSelected = 0;
				}
				break;
			case 'L':
				this.cSelected--;
				if (this.cSelected == -1)
				{
					this.cSelected = length - 1;
					this.lSelected--;
					if (this.lSelected == -1)
						this.lSelected = length - 1;
				}
				break;
			case 'R':
				this.cSelected++;
				if (this.cSelected == length)
				{
					this.cSelected = 0;
					this.lSelected++;
					if (this.lSelected == length)
						this.lSelected = 0;
				}
				break;
		}

		this.repaint();
	}

	public void keyPressed(KeyEvent e)
	{
		if (e.getKeyCode() == KeyEvent.VK_KP_UP || e.getKeyCode() == KeyEvent.VK_UP)
			this.select('U');
		else if (e.getKeyCode() == KeyEvent.VK_KP_LEFT || e.getKeyCode() == KeyEvent.VK_LEFT)
			this.select('L');
		else if (e.getKeyCode() == KeyEvent.VK_KP_DOWN || e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_ENTER)
			this.select('D');
		else if (e.getKeyCode() == KeyEvent.VK_KP_RIGHT || e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_TAB || e.getKeyCode() == KeyEvent.VK_SPACE)
			this.select('R');
		else if (e.getKeyChar() >= '1' && e.getKeyChar() <= '9')
		{
			if (this.lSelected == -1)
				return;
			this.ctrl.place(this.lSelected, this.cSelected, Character.getNumericValue(e.getKeyChar()));
			this.select('R');
		}
		else if (e.getKeyCode() == KeyEvent.VK_DELETE)
		{
			if (this.lSelected == -1)
				return;
			this.ctrl.unplace(this.lSelected, this.cSelected);
			this.select('R');
		}
		else if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
			this.unselect();
	}
	public void keyReleased(KeyEvent e)
	{

	}
	public void keyTyped(KeyEvent e)
	{

	}
}
