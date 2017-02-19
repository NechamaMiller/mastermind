package mastermind;

import enums.Color;

public abstract class Guess
{
	//need to be package access so subclasses can use
	Color[] sequence;
	int numReds;
	int numWhites;
	
	public int getNumReds()
	{
		return numReds;
	}

	public int getNumWhites()
	{
		return numWhites;
	}
	
	public Color[] getSequence()
	{
		Color[] temp = new Color[sequence.length];
		
		for(int i=0; i<temp.length; i++)
		{
			temp[i] = sequence[i];
		}
		
		return temp;		
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append("sequence: ");
		
		for(Color color: sequence)
		{
			sb.append(color + " ");
		}
		
		return sb.toString();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj == this)
		{
			return true;
		}
		
		if(obj == null)
		{
			return false;
		}
		
		if(! (obj instanceof ComputerGuess))
		{
			return false;
		}
		
		ComputerGuess guess2 = (ComputerGuess)obj;
		
		for(int i=0; i<sequence.length; i++)
		{
			if(guess2.sequence[i] != sequence[i])
			{
				return false;
			}
		}
		
		return true;
	}
}
