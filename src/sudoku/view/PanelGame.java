package sudoku.view;

import sudoku.ControllerGUI;
import sudoku.model.Sudoku;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static sudoku.view.ViewSettings.*;

public class PanelGame extends JPanel
{
	private final ControllerGUI ctrl;
	private final Sudoku        model;

	private Point o;
	private int   box;
	private int   full;

	private int lSelected;
	private int cSelected;

	public PanelGame(ControllerGUI ctrl, Sudoku model, Dimension screen)
	{
		this.ctrl  = ctrl;
		this.model = model;

		int size = (int) Math.min(0.9 * screen.width, 0.9 * screen.height);
		this.setPreferredSize(new Dimension(size, size));

		this.addKeyListener(new Keyboard());
		this.addMouseListener(new Mouse());
		this.setFocusTraversalKeysEnabled(false);
		this.unselect();
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		// Compute size
		int[][] grid  = this.model.getGrid();
		int     level = this.model.getLevel();
		int     size  = this.model.getSize();

		int w         = this.getWidth(), h = this.getHeight();
		int borderSum = (level + 1) * BORDER_D + (size - level) * BORDER_S;

		this.box  = Math.min((w - borderSum) / (size + 2), (h - borderSum) / (size + 2));
		this.full = borderSum + size * this.box;
		this.o    = new Point((w - this.full) / 2, (h - this.full) / 2);

		// Clear
		g.setColor(BG_COLOR);
		g.setFont(new Font("Arial", Font.BOLD, (int) Math.max(12, Math.min(45, 0.75 * this.box))));
		g.fillRect(0, 0, w, h);
		g.setColor(BOX_COLOR);
		g.fillRect(this.o.x + BORDER_D, this.o.y + BORDER_D, this.full - BORDER_D, this.full - BORDER_D);
		g.setColor(GRID_COLOR);

		// Paint grid
		g.fillRect(this.o.x, this.o.y, this.full, BORDER_D);
		g.fillRect(this.o.x, this.o.y + this.full - BORDER_D, this.full, BORDER_D);
		g.fillRect(this.o.x, this.o.y, BORDER_D, full);
		g.fillRect(this.o.x + this.full - BORDER_D, this.o.y, BORDER_D, this.full);

		Point p = new Point(this.o.x + BORDER_D + this.box, this.o.y + BORDER_D + this.box);
		for (int i = 0; i < size - 1; i++)
		{
			int border = i % level == level - 1 ? BORDER_D : BORDER_S;

			g.fillRect(p.x, this.o.y, border, this.full);
			g.fillRect(this.o.x, p.y, this.full, border);

			p.x += border + this.box;
			p.y += border + this.box;
		}

		// Paint numbers
		FontMetrics metrics = g.getFontMetrics();
		g.setColor(NUM_COLOR);

		p.y = this.o.y + BORDER_D;
		for (int l = 0; l < size; l++)
		{
			p.x = this.o.x + BORDER_D;
			for (int c = 0; c < size; c++)
			{
				if (l == this.lSelected && c == this.cSelected)
				{
					g.setColor(SELECTED_COLOR);
					g.fillRect(p.x, p.y, this.box, this.box);
					g.setColor(NUM_COLOR);
				}

				if (grid[l][c] != 0)
				{
					String number = String.valueOf(grid[l][c]);
					g.drawString(number, p.x + this.box / 2 - metrics.stringWidth(number) / 2, p.y + this.box / 2 + metrics.getHeight() / 2 - metrics.getDescent());
				}

				p.x += this.box + (c % level == level - 1 ? BORDER_D : BORDER_S);
			}
			p.y += this.box + (l % level == level - 1 ? BORDER_D : BORDER_S);
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

	public void mouseAction(MouseEvent e)
	{
		Point p = new Point(e.getX(), e.getY());
		Point o = new Point(this.o.x + BORDER_S + BORDER_H, this.o.y + BORDER_S + BORDER_H);

		int level = this.model.getLevel();
		int full  = this.full - (BORDER_D + BORDER_S + BORDER_H);
		int box   = this.box + BORDER_S;

		if (p.x < o.x || p.y < o.y || p.x > o.x + full || p.y > o.y + full)
		{
			this.unselect();
			return;
		}

		p.setLocation(p.x - o.x, p.y - o.y);
		this.lSelected = (p.y - ((p.y / (level * box + BORDER_S)) * BORDER_S)) / box;
		this.cSelected = (p.x - ((p.x / (level * box + BORDER_S)) * BORDER_S)) / box;

		this.repaint();
	}

	public void keyboardAction(KeyEvent e)
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

	private class Keyboard extends KeyAdapter
	{
		public void keyPressed(KeyEvent e)
		{
			PanelGame.this.keyboardAction(e);
		}
	}

	private class Mouse extends MouseAdapter
	{
		public void mousePressed(MouseEvent e)
		{
			PanelGame.this.mouseAction(e);
		}
	}
}
