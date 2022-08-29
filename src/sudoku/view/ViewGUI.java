package sudoku.view;

import sudoku.ControllerGUI;
import sudoku.model.Sudoku;

import javax.swing.*;
import java.awt.*;

public class ViewGUI extends JFrame
{
	private final PanelGame panelGame;

	public ViewGUI(ControllerGUI ctrl, Sudoku model)
	{
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

		this.panelGame = new PanelGame(ctrl, model, screen);
		this.add(this.panelGame, BorderLayout.CENTER);

		this.setTitle("Sudoku Solver");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setLocation(screen.width / 2 - this.getWidth() / 2, screen.height / 2 - this.getHeight() / 2);
		this.setVisible(true);
	}

	public void maj()
	{
		this.panelGame.repaint();
	}
}
