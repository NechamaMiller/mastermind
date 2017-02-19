package mastermind;

import enums.*;
import exceptions.*;

public class HumanGuess extends Guess
{
	private Color[] key;
	private boolean[] foundInKey;
	private boolean[] foundInSequence;

	public HumanGuess(Color[] key, Color[] attempt)
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

		foundInKey = new boolean[key.length];
		foundInSequence = new boolean[key.length];
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
		for (int index = 0; index < key.length; index++)
		{
			if (sequence[index].equals(key[index]))
			{
				numReds++;// total red pegs updated
				foundInKey[index] = true;
				foundInSequence[index] = true;
			}
		}
	}

	public void checkForWhites()
	{
		if (!isAllRed())
		{
			// loops once per each position in the key
			for (int keyIndex = 0; keyIndex < key.length; keyIndex++)
			{
				// loops once per each possible position in the attempt
				for (int sequenceIndex = 0; sequenceIndex < sequence.length; sequenceIndex++)
				{
					// as long as not same place (the current peg we're looking at and the position we're comparing it to- because then would be red),
					// and this peg in the attempt hasn't been found yet
					if (sequenceIndex != keyIndex && sequence[sequenceIndex].equals(key[keyIndex])
							&& !foundInKey[keyIndex] && !foundInSequence[sequenceIndex])
					{
						numWhites++;
						foundInKey[keyIndex] = true;
						foundInSequence[sequenceIndex] = true;
					}
				}
			}

		}

	}

	public boolean isAllRed()
	{
		return (numReds == key.length);
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append(super.toString() + "\n");
		sb.append("results: ");
		sb.append(numReds + " reds, ");
		sb.append(numWhites + " whites");
		
		return sb.toString();
	}
}
