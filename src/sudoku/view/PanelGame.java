package sudoku.view;

import sudoku.ControllerGUI;
import sudoku.model.Selector;
import sudoku.model.Sudoku;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static java.awt.event.KeyEvent.*;
import static sudoku.view.ViewSettings.*;

public class PanelGame extends JPanel
{
	// Attributes
	private final ControllerGUI ctrl;
	private final Sudoku        sudoku;
	private final Selector      selector;

	private Point o;
	private int   box;
	private int   full;


	// Constructor
	public PanelGame(ControllerGUI ctrl, Sudoku sudoku, Selector selector, Dimension screen)
	{
		this.ctrl     = ctrl;
		this.sudoku   = sudoku;
		this.selector = selector;

		int size = (int) Math.min(0.9 * screen.width, 0.9 * screen.height);
		this.setPreferredSize(new Dimension(size, size));

		this.addKeyListener(new Keyboard());
		this.addMouseListener(new Mouse());
		this.setFocusTraversalKeysEnabled(false);
	}


	// Methods
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		// Compute size
		int[][] grid  = this.sudoku.getGrid();
		int     level = this.sudoku.getLevel();
		int     size  = this.sudoku.getSize();

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
		int         halfH   = metrics.getHeight() / 2 - metrics.getDescent();
		g.setColor(NUM_COLOR);

		p.y = this.o.y + BORDER_D;
		for (int l = 0; l < size; l++)
		{
			p.x = this.o.x + BORDER_D;
			for (int c = 0; c < size; c++)
			{
				if (this.selector.isSelected(l, c))
				{
					g.setColor(SELECTED_COLOR);
					g.fillRect(p.x, p.y, this.box, this.box);
					g.setColor(NUM_COLOR);

					if (this.selector.isPlacing())
					{
						String number = this.selector.getNumber() + "_".repeat((int) Math.log10(size) - (int) Math.log10(this.selector.getNumber()));
						g.drawString(number, p.x + this.box / 2 - metrics.stringWidth(number) / 2, p.y + this.box / 2 + halfH);
					}
				}

				if (grid[l][c] != 0)
				{
					String number = String.valueOf(grid[l][c]);
					g.drawString(number, p.x + this.box / 2 - metrics.stringWidth(number) / 2, p.y + this.box / 2 + halfH);
				}

				p.x += this.box + (c % level == level - 1 ? BORDER_D : BORDER_S);
			}
			p.y += this.box + (l % level == level - 1 ? BORDER_D : BORDER_S);
		}
	}

	public void mouseAction(MouseEvent e)
	{
		Point p = new Point(e.getX(), e.getY());
		Point o = new Point(this.o.x + BORDER_S + BORDER_H, this.o.y + BORDER_S + BORDER_H);

		int level = this.sudoku.getLevel();
		int full  = this.full - (BORDER_D + BORDER_S + BORDER_H);
		int box   = this.box + BORDER_S;

		if (p.x < o.x || p.y < o.y || p.x > o.x + full || p.y > o.y + full)
		{
			this.ctrl.unselect();
			return;
		}

		p.setLocation(p.x - o.x, p.y - o.y);
		int lSelected = (p.y - ((p.y / (level * box + BORDER_S)) * BORDER_S)) / box;
		int cSelected = (p.x - ((p.x / (level * box + BORDER_S)) * BORDER_S)) / box;
		this.ctrl.select(lSelected, cSelected);
	}

	public void keyboardAction(KeyEvent e)
	{
		switch (e.getKeyCode())
		{
			case VK_KP_UP, VK_UP -> this.ctrl.select('U');
			case VK_KP_DOWN, VK_DOWN -> this.ctrl.select('D');
			case VK_KP_LEFT, VK_LEFT -> this.ctrl.select('L');
			case VK_KP_RIGHT, VK_RIGHT, VK_SPACE -> this.ctrl.select('R');
			case VK_DELETE, VK_BACK_SPACE -> this.ctrl.remove();
			case VK_ESCAPE -> this.ctrl.unselect();
			case VK_ENTER ->
			{
				if (this.selector.isPlacing())
					this.ctrl.place();
				else
					this.ctrl.select('D');
			}
			case VK_TAB ->
			{
				if (this.selector.isPlacing())
					this.ctrl.place();
				else
					this.ctrl.select('R');
			}
			default ->
			{
				if (e.getKeyChar() >= '0' && e.getKeyChar() <= '9')
					this.ctrl.place(Character.getNumericValue(e.getKeyChar()));
			}
		}
	}


	// Listeners
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
