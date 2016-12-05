package mastermind;

import java.util.Arrays;

import enums.*;
import exceptions.*;

public class Guess 
{
	private Color[] sequence;
	private int numReds;
	private int numWhites;
	
	private Color[] key;
	private int[] results;
	private boolean[] found;
	
	public Guess(Color[] key, Color[] attempt)
	{
		if (key == null)
		{
			throw new InvalidDataException("The key cannot be null.");
		}
		this.key = key;
		
		if (attempt == null)
		{
			throw new InvalidDataException("The sequence cannot be null.");
		}
		
		if (key.length != attempt.length)
		{
			throw new InvalidDataException("Key and sequence must be the same length");
		}
		
		results = new int[key.length];
		found = new boolean[key.length];
		numReds = 0;
		numWhites = 0;
		
		// deep copy
		sequence = new Color[attempt.length];
		for (int i = 0; i < attempt.length; i++)
		{
			sequence[i] = attempt[i];
		}
	}
	
	public void checkForReds()
	{
		// check for reds
		for (int index = 0; index < key.length; index++)
		{
			// if right color right place, the row of the turn up to in game and the column up to in loop
			// gets a 1 to symbolize a red
			if (sequence[index].equals(key[index]))
			{
				results[index] = 1; //signifies a red
				numReds++;// total red pegs updated
				found[index] = true;
			}
		}
	}
	
	public void checkForWhites()
	{
		// check for whites
		if (numReds != key.length)
		{
			//loops once per each position in the key
			for (int index = 0; index < key.length; index++)
			{
				//loops once per each possible position in the attempt
				for (int x = 0; x < sequence.length; x++)
				{
					//as long as not same place (the current peg we're looking at and the
					//position we're comparing it to- because then would be red), and this peg
					//in the attempt hasn't been found yet
					if (x != index && !found[x] && sequence[index].equals(key[x]))
					{
						results[index] = 2;
						numWhites++;
						found[x] = true;
					}
				}
			}
			
		}
		
	} // checkGuess
	
	
	public boolean  isAllRed()
	{
		return (numReds == key.length);
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
	
	public int[] getResults()
	{
		return results; //no need for deep copy, I think
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
		str.append("\nReds: " + numReds);
		str.append("\nWhites: " + numWhites);
		return str.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Guess other = (Guess) obj;
		if (!Arrays.equals(sequence, other.sequence))
			return false;
		return true;
	}
}

