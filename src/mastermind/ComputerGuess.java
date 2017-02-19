package mastermind;

import enums.Color;

public class ComputerGuess extends Guess
{
	public ComputerGuess(Color[] sequence)
	{
		if(sequence == null)
		{
			throw new NullPointerException("sequence cannot be null");
		}
		
		this.sequence = new Color[sequence.length];
		
		for (int i = 0; i < sequence.length; i++)
		{
			this.sequence[i] = sequence[i];
			numReds = 0;
			numWhites = 0;
		}
	}

	public void setNumReds(int numReds)
	{
		if(numReds<0 || numReds+numWhites>Mastermind.getKeySize())
		{
			throw new IllegalArgumentException("numReds must be greater than 0 and numReds + numwhites can't be more than " + Mastermind.getKeySize());
		}
		
		this.numReds = numReds;
	}

	public void setNumWhites(int numWhites)
	{
		if(numWhites<0 || numWhites+numReds>Mastermind.getKeySize())
		{
			throw new IllegalArgumentException("numWhites must be greater than 0 and numReds + numwhites can't be more than " + Mastermind.getKeySize());
		}
		
		this.numWhites = numWhites;
	}		
	
	public boolean isAllOneColor()
	{
		Color color = sequence[0];
		
		for(int i=1;i<sequence.length; i++)
		{
			if(sequence[i] != color)
			{
				return false;
			}
		}
		
		return true;
	}	
}
