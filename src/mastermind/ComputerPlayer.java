package mastermind;

import java.util.Stack;

import enums.*;

public class ComputerPlayer
{
	private ComputerGuess[] guesses;
	private int guessTracker;
	private Stack<Color> colors;
	
	public ComputerPlayer(Game game, int numColors)
	{
		guesses = new ComputerGuess[game.getTotalNumTurns()];
		guessTracker = 0;
		colors = new Stack<>();
		
		for(int i=0; i<numColors; i++)
		{
			colors.push(Color.values()[i]);
		}
	}
	
	/*
	 * Algorithm: start with one color in all 4 places, see if there are any reds (right color in the right spot)
	 * As long as there are 0 reds, try another color.
	 * If there are any reds, then keep the right amount of that color in the code, and use another color for the rest of the code
	 * Now use guess and check (basically) to figure out the right spots for those colors
	 */
	public ComputerGuess generateGuess()
	{
		Color[] attempt = new Color[Game.getKeySize()];
		
		ComputerGuess lastGuess = guessTracker == 0? null : guesses[guessTracker-1];
		
		/*
		 * if we didn't yet guess any of the right color, either because it's the first turn 
		 * or because so far all our guesses returned 0 reds (and there can't be any whites if all 4 colors are the same)
		 * then pop another color from the stack to try 
		 */
		if(guessTracker == 0 || lastGuess.getNumReds() == 0)
		{
			Color color = colors.pop();
			for(int i=0; i<attempt.length;i++)
			{
				attempt[i] = color;
			}
		}
		else if(lastGuess.getNumReds() == Game.getKeySize())
		{
			throw new IllegalStateException("Computer already won the game.");
		}
		
		else 
		{
			attempt = getSequenceToTry();
		}
		
		ComputerGuess guess = new ComputerGuess(attempt);
		guesses[guessTracker++] = guess;
		
		return guess;
	}
	
	/**
	 * This method should only be called when we know at least one right color
	 */
	private Color[] getSequenceToTry()
	{
		Color[] attempt = new Color[Game.getKeySize()];
		int numColorsAdded = 0;
		
		ComputerGuess lastGuess = guesses[guessTracker-1];
		
		//we didn't figure out any colors so far
		if(lastGuess.getNumReds() + lastGuess.getNumWhites() == 0)
		{
			throw new IllegalStateException("Can't figure out sequence with no right colors");
		}
		
		else if(lastGuess.getNumReds() + lastGuess.getNumWhites() != Game.getKeySize())
		{
			//get all the colors that we know are for sure in the pattern because they were all one color and had some reds
			for(int i=0; i<guessTracker; i++)
			{
				ComputerGuess guess = guesses[i];
				if(guess.isAllOneColor())
				{
					for(int j=0; j<guess.getNumReds(); j++)
					{
						attempt[numColorsAdded++] = guess.getSequence()[0];
					}
				}
				else
				{
					//TODO: look at results and solve
					Color[] pastGuessSequence = guesses[i].getSequence();
					
					
				}
			}
			
			
			
			//if we don't have four yet colors, we should fill the rest of the spaces with one specific color
			if(numColorsAdded != attempt.length)
			{
				Color color = colors.pop();
				while(numColorsAdded != attempt.length)
				{
					attempt[numColorsAdded++] = color;
				}
			}
		}
		
		else//we figured out all the colors, we just need to put them in the right order
		{
			attempt = guesses[guessTracker-1].getSequence();
		}
		
		while (!checkIfSequenceIsPossible(attempt))
		{
			//TODO
		}
		
		return attempt;
	}
	
	//TODO
	private boolean checkIfSequenceIsPossible(Color[] sequence)
	{
		return true;
	}

	public void setNumRedsOfLastTurn(int numReds)
	{
		if(numReds<0 || numReds>Game.getKeySize())
		{
			throw new IllegalArgumentException("numReds must be between 0 and " + Game.getKeySize());
		}
		
		guesses[guessTracker-1].setNumReds(numReds);
	}

	public void setNumWhitesOfLastTurn(int numWhites)
	{
		if(numWhites<0 || numWhites>Game.getKeySize())
		{
			throw new IllegalArgumentException("numWhites must be between 0 and " + Game.getKeySize());
		}
		
		guesses[guessTracker-1].setNumWhites(numWhites);
	}

	public static void main(String[] args)
	{
		ComputerPlayer comp = new ComputerPlayer(new Game(), 6);
		System.out.println(comp.generateGuess().toString());
		comp.setNumRedsOfLastTurn(1);
		System.out.println(comp.generateGuess().toString());
		comp.setNumRedsOfLastTurn(2);
		System.out.println(comp.generateGuess().toString());
	}
}
