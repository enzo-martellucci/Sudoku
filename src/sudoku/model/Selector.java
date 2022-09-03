package sudoku.model;

public class Selector
{
	// Attributes
	private final int size;
	private final int last;

	private boolean selected;
	private boolean placing;
	private boolean ready;

	private int l;
	private int c;
	private int number;


	// Constructor
	public Selector(int level)
	{
		this.size = level * level;
		this.last = this.size - 1;

		this.selected = false;
		this.placing  = false;
		this.ready    = false;
	}


	// Getters
	public boolean isSelected()
	{
		return this.selected;
	}
	public boolean isSelected(int l, int c)
	{
		return this.selected && this.l == l && this.c == c;
	}
	public boolean isPlacing() { return this.placing; }
	public boolean isReady()   { return this.ready; }

	public int getL()
	{
		return this.l;
	}
	public int getC()
	{
		return this.c;
	}
	public int getNumber() { return this.number; }


	// Methods
	public void select(char dir)
	{
		this.placing = this.ready = false;
		if (!this.selected)
		{
			if (dir == 'U' || dir == 'L')
				this.select(this.last, this.last);
			else if (dir == 'D' || dir == 'R')
				this.select(0, 0);
			return;
		}

		switch (dir)
		{
			case 'U' ->
			{
				this.l--;
				if (this.l == -1)
				{
					this.l = this.last;
					this.c--;
					if (this.c == -1)
						this.select(this.last, this.last);
				}
			}
			case 'D' ->
			{
				this.l++;
				if (this.l == this.size)
				{
					this.l = 0;
					this.c++;
					if (this.c == this.size)
						this.select(0, 0);
				}
			}
			case 'L' ->
			{
				this.c--;
				if (this.c == -1)
				{
					this.c = this.last;
					this.l--;
					if (this.l == -1)
						this.select(this.last, this.last);
				}
			}
			case 'R' ->
			{
				this.c++;
				if (this.c == this.size)
				{
					this.c = 0;
					this.l++;
					if (this.l == this.size)
						this.select(0, 0);
				}
			}
		}
	}

	public void select(int l, int c)
	{
		this.placing  = this.ready = false;
		this.selected = true;
		this.l        = l;
		this.c        = c;
	}

	public void unselect()
	{
		this.selected = false;
	}

	public boolean place(int digit)
	{
		if (this.placing)
		{
			if (this.number * 10 + digit > this.size)
				return false;
			this.number = this.number * 10 + digit;
		}
		else
		{
			if (digit == 0)
				return false;
			this.number  = digit;
			this.placing = true;
		}

		this.ready = this.number * 10 > this.size || (int) Math.log10(this.number) == (int) Math.log10(this.size);
		return true;
	}

	public int consume()
	{
		this.placing = false;
		this.ready   = false;
		return this.number;
	}
}
