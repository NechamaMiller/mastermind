package mastermind;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

import enums.*;
import exceptions.*;

public class ComputerPlayer
{
	private ComputerGuess[] guesses;
	private int guessTracker;
	private ArrayList<Color> allColors;
	private ArrayList<Color> colorsForSureInPattern;
	private Color[] colorsInRightSpots;
	private int numColorUpTo;
	//to prevent an infinite loop if the computer couldn't guess the code in this many tries, it should give up
	private final int MAX_TRIES_PER_TURN = 25;
	
	public ComputerPlayer(Game game, ColorLevel colorLevel)
	{
		guesses = new ComputerGuess[game.getTotalNumTurns()];
		guessTracker = 0;
		allColors = new ArrayList<>();
		colorsInRightSpots = new Color[Game.getKeySize()];
		numColorUpTo = 0;
		colorsForSureInPattern = new ArrayList<>();

		for (int i = 0; i < colorLevel.getNumColors(); i++)
		{
			allColors.add(Color.values()[i]);
		}

		Collections.shuffle(allColors);// puts the colors in a random order to make it less predictable
	}

	/*
	 * Algorithm: start with one color in all 4 places, see if there are any reds (right color in the right spot)
	 * As long as there are 0 reds, try another color.
	 * If there are any reds, then keep the right amount of that color in the code, and use another color for the rest of the code
	 * Now use guess and check (basically) to figure out the right spots for those colors
	 */
	public Color[] generateGuess()
	{
		Color[] attempt = new Color[Game.getKeySize()];

		ComputerGuess lastGuess = guessTracker == 0 ? null : guesses[guessTracker - 1];

		/*
		 * if we didn't yet guess any of the right color then take another color from the arraylist to try 
		 */
		if (guessTracker == 0 || colorsForSureInPattern.size() == 0)
		{
			Color color = allColors.get(numColorUpTo++);
			for (int i = 0; i < attempt.length; i++)
			{
				attempt[i] = color;
			}
		}
		else if (lastGuess.getNumReds() == Game.getKeySize())
		{
			throw new IllegalStateException("Computer already won the game.");
		}

		else
		{
			attempt = getSequenceToTry(lastGuess);
		}

		ComputerGuess guess = new ComputerGuess(attempt);
		guesses[guessTracker++] = guess;

		return attempt;
	}

	/**
	 * This method should only be called when we know at least one right color
	 */
	private Color[] getSequenceToTry(ComputerGuess lastGuess)
	{
		Color[] attempt = null;

		// we didn't figure out any colors so far
		if (lastGuess == null || lastGuess.getNumReds() + lastGuess.getNumWhites() == 0)
		{
			throw new IllegalStateException("Can't figure out sequence with no right colors");
		}

		else if (colorsForSureInPattern.size() != 0)
		{
			// colors we need to use this turn
			// copy constructor for ArrayList
			//ArrayList<Color> colorsToStillUse = new ArrayList<>(colorsForSureInPattern);

			//attempt = addSpotsKnownForSure(colorsToStillUse);
			//attempt = addOtherKnownColors(attempt, colorsToStillUse, lastGuess);
			int numTries = 0;
			
			// check if makes sense with other guesses' reds and whites
			Random generator = new Random(System.currentTimeMillis());
			
			do
			{
				numTries++;
				ArrayList<Color> colorsToStillUse = new ArrayList<>(colorsForSureInPattern);
//				for (Color color : attempt)
//				{
//					if (color != null)
//					{
//						colorsToStillUse.add(color);
//					}
//				}
//
//				attempt = new Color[attempt.length];

				attempt = addSpotsKnownForSure(colorsToStillUse);

				while (!colorsToStillUse.isEmpty())
				{
					Color color = colorsToStillUse.remove(0);

					int index = generator.nextInt(Game.getKeySize());
					while (attempt[index] != null)
					{
						index = generator.nextInt(Game.getKeySize());
					}

					attempt[index] = color;
				}
			}
			while (!isAttemptPlausible(attempt) && numTries <= MAX_TRIES_PER_TURN);

			if(numTries > MAX_TRIES_PER_TURN)
			{
				throw new ComputerLostGameException("Computer couldn't guess the code");
			}
			
			//then we will need to add another color
			if(colorsForSureInPattern.size() < attempt.length)
			{
				if(numColorUpTo == allColors.size())
				{
					throw new ComputerLostGameException("Computer couldn't guess the code.");
				}
				
				Color color = allColors.get(numColorUpTo++);
				for (int i = 0; i < attempt.length; i++)
				{
					if (attempt[i] == null)
					{
						attempt[i] = color;
					}
				}
			}
		}

		return attempt;
	}

	private Color[] addSpotsKnownForSure(ArrayList<Color> colorsToStillUse)
	{
		Color[] attempt = new Color[Game.getKeySize()];

		for (int i = 0; i < colorsInRightSpots.length; i++)
		{
			Color color = colorsInRightSpots[i];

			if (color != null)
			{
				attempt[i] = color;
				colorsToStillUse.remove(color);
			}
		}

		return attempt;
	}

//	private Color[] addOtherKnownColors(Color[] attempt, ArrayList<Color> colorsToStillUse, ComputerGuess lastGuess)
//	{
//		// int numWhites = lastGuess.getNumWhites();
//
//		while (!colorsToStillUse.isEmpty())
//		{
//			Color color = colorsToStillUse.get(0);
//			colorsToStillUse.remove(color);
//			boolean usedColor = false;
//
//			if (lastGuess.getNumWhites() != 0)
//			// if(numWhites > 0)
//			{
//				for (int i = 0; i < attempt.length && !usedColor; i++)
//				{
//					if (attempt[i] == null)
//					{
//						boolean colorHereBefore = false;
//						for (int j = 0; j < guessTracker && !colorHereBefore; j++)
//						{
//							// should only look at guesses that gave us back whites to decide that a spot is wrong for a color
//							if (guesses[j].getNumWhites() > 0 && guesses[j].getSequence()[i] == color)
//							{
//								colorHereBefore = true;
//							}
//						}
//
//						if (!colorHereBefore)
//						{
//							attempt[i] = color;
//							usedColor = true;
//						}
//					}
//				}
//				// r _ _ _
//				if (!usedColor)
//				{
//					Color[] lastSequence = lastGuess.getSequence();
//
//					for (int i = 0; i < lastSequence.length && !usedColor; i++)
//					{
//						if (attempt[i] == null && lastSequence[i] == color)
//						{
//							attempt[i] = color;
//							usedColor = true;
//						}
//					}
//				}
//			}
			// else
			// {
			// Color[] lastSequence = lastGuess.getSequence();
			//
			// for(int i=0; i<lastSequence.length && !usedColor; i++)
			// {
			// if(attempt[i] == null && lastSequence[i] == color)
			// {
			// attempt[i] = color;
			// usedColor = true;
			// }
			// }
			// }

			/*
			 * if didn't use color yet because let's say our last guess was RED ORANGE YELLOW YELLOW
			 * and we get back whites, we might still need to keep yellow in spot 3 or 4, but the other loop
			 * won't let because yellow had already been in both those spots in a guess that generated whites
			*/
//			for (int i = 0; i < attempt.length && !usedColor; i++)
//			{
//				if (attempt[i] == null)
//				{
//					attempt[i] = color;
//					usedColor = true;
//				}
//			}
//		}
//
//		return attempt;
//	}

	/**
	 * checks this attempt against all other previous attempts to make sure it is plausible
	 * @param attempt attempt to check
	 * @return true if attempt is plausible, false otherwise
	 */
	private boolean isAttemptPlausible(Color[] attempt)
	{
		boolean plausible = true;
		for (int i = 0; i < guessTracker && plausible; i++)
		{
			boolean[] foundInSequence = new boolean[attempt.length];
			boolean[] foundInAttempt = new boolean[attempt.length];

			ComputerGuess guess = guesses[i];
			Color[] sequence = guess.getSequence();
			int numReds = 0;

			// can our attempt work with other guess's numReds?
			for (int j = 0; j < sequence.length; j++)
			{
				if (attempt[j] == sequence[j])
				{
					numReds++;
					foundInSequence[j] = foundInAttempt[j] = true;
				}
			}

			if (numReds != guess.getNumReds())
			{
				plausible = false;
			}

			if (plausible)
			{

				// can our attempt work with other guess's numWhites?
				int numWhites = 0;

				for (int attemptIndex = 0; attemptIndex < attempt.length; attemptIndex++)
				{
					for (int sequenceIndex = 0; sequenceIndex < sequence.length; sequenceIndex++)
					{
						// as long as not same place (the current peg we're looking at and the
						// position we're comparing it to- because then would be red), and this peg
						// in the attempt hasn't been found yet
						if (sequenceIndex != attemptIndex && sequence[sequenceIndex].equals(attempt[attemptIndex])
								&& !foundInSequence[sequenceIndex] && !foundInAttempt[attemptIndex])
						{
							numWhites++;
							foundInSequence[sequenceIndex] = true;
							foundInAttempt[attemptIndex] = true;
						}
					}
				}

				if (numWhites != guess.getNumWhites())
				{
					plausible = false;
				}
			}
		}

		return plausible;
	}

	public void setLastTurnResults(int numReds, int numWhites)
	{
		setNumRedsOfLastTurn(numReds);
		setNumWhitesOfLastTurn(numWhites);
		addToRightColorsBasedOnLastTurn(numReds, numWhites);
	}

	private void setNumRedsOfLastTurn(int numReds)
	{
		if (numReds < 0 || numReds > Game.getKeySize())
		{
			throw new IllegalArgumentException("numReds must be between 0 and " + Game.getKeySize());
		}

		guesses[guessTracker - 1].setNumReds(numReds);
	}

	private void setNumWhitesOfLastTurn(int numWhites)
	{
		if (numWhites < 0 || numWhites > Game.getKeySize())
		{
			throw new IllegalArgumentException("numWhites must be between 0 and " + Game.getKeySize());
		}

		guesses[guessTracker - 1].setNumWhites(numWhites);
	}

	private void addToRightColorsBasedOnLastTurn(int numReds, int numWhites)
	{
		Color[] lastSequence = guesses[guessTracker - 1].getSequence();

		if (numReds == Game.getKeySize())
		{
			colorsInRightSpots = lastSequence;
		}

		// we figured out colors to add to colorsInRightSpots
		else if (numReds >= colorsForSureInPattern.size() && numWhites == 0)
		{
			for (int i = 0; i < colorsForSureInPattern.size(); i++)
			{
				Color color = colorsForSureInPattern.get(i);
				int index = -1;

				for (int j = 0; j < lastSequence.length && index == -1; j++)
				{
					if (lastSequence[j] == color)
					{
						index = j;
					}
				}

				colorsInRightSpots[index] = color;
			}
		}

		if (numReds + numWhites > colorsForSureInPattern.size())
		{
			// the most recently added color is also in the pattern
			for(int i=colorsForSureInPattern.size(); i<numReds + numWhites; i++)
			{
				Color color = allColors.get(numColorUpTo - 1);
				colorsForSureInPattern.add(color);
			}
		}
	}

	public static void main(String[] args)
	{
		Scanner input = new Scanner(System.in);

		ComputerPlayer comp = new ComputerPlayer(new Game(), ColorLevel.MEDIUM);

		int numReds = 0;

		for (int i = 0; i < GuessLevel.MEDIUM.getNumGuesses() && numReds != 4; i++)
		{
			Color[] attempt = comp.generateGuess();

			for (Color color : attempt)
			{
				System.out.print(color + " ");
			}

			System.out.println();

			System.out.print("Enter numReds of last guess:");
			numReds = input.nextInt();
			System.out.print("Enter numWhites of last guess:");
			int numWhites = input.nextInt();
			comp.setLastTurnResults(numReds, numWhites);

			System.out.println();
		}

		if (numReds == 4)
		{
			System.out.println("Yay! I guessed your code!");
		}
		else
		{
			System.out.println("You win! I couldn't guess your code.");
		}
	}
}
