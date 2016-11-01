package mastermind;

import exceptions.*;

public class Guess 
{
	private Color[] sequence;
	private int numReds;
	private int numWhites;

	public Guess(int numReds, int numWhites, Color[] entry) 
	{
		// validate that they don't total more than 4
		if ((numWhites + numReds) > 4)
		{
			throw new InvalidDataException("NumWhites plus NumReds cannot be greater than 4.");
		}
			
		// validate that neither number is under 0 or over 4
		if (numReds < 0 || numReds > 4)
		{
			throw new InvalidDataException("NumReds must be between 0 and 4.");
		}
		
		this.numReds = numReds;

		if (numWhites < 0 || numWhites > 4)
		{
			throw new InvalidDataException("NumWhites must be between 0 and 4.");
		}
		
		this.numWhites = numWhites;

		// validate that entry is not null
		if (entry == null)
		{
			throw new InvalidDataException("The sequence cannot be null.");
		}

		// deep copy
		sequence = new Color[entry.length];
		for (int i = 0; i < entry.length; i++)
		{
			sequence[i] = entry[i];
		}
	}

	// getters

	public Color[] getSequence() 
	{
		// deep copy
		Color[] array = new Color[sequence.length];
		for (int i = 0; i < sequence.length; i++)
		{
			array[i] = sequence[i];
		}

		return array;
	}

	public int getNumReds() 
	{
		return numReds;
	}

	public int getNumWhites() 
	{
		return numWhites;
	}

	@Override
	public String toString() 
	{
		StringBuilder str = new StringBuilder();
		str.append("Sequence: ");
		for (Color c : sequence)
		{
			str.append(c + " ");
		}
		str.append("\nNumReds: " + numReds);
		str.append("\nNumWhites: " + numWhites);
		return str.toString();
	}

}
