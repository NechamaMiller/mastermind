package mastermind;

import java.util.ArrayList;
import java.util.Stack;

import enums.*;

public class ComputerPlayer
{
	private ComputerGuess[] guesses;
	private int guessTracker;
	private ArrayList<Color> allColors;
	//private Stack<Color> colors;
	private ArrayList<Color> colorsForSureInPattern;
	private Color[] colorsInRightSpots;
	private int numColorUpTo;
	
	public ComputerPlayer(Game game, ColorLevel colorLevel)
	{
		guesses = new ComputerGuess[game.getTotalNumTurns()];
		guessTracker = 0;
		allColors = new ArrayList<>();
		colorsInRightSpots = new Color[Game.getKeySize()];
		numColorUpTo = 0;
		colorsForSureInPattern = new ArrayList<>();
		
		for(int i=0; i<colorLevel.getNumColors(); i++)
		{
			allColors.add(Color.values()[i]);
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
		if(guessTracker == 0 || (lastGuess.getNumReds() == 0 && lastGuess.getNumWhites() == 0))
		{
			Color color = allColors.get(numColorUpTo++);
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
		
		ComputerGuess lastGuess = guessTracker == 0 ? null : guesses[guessTracker-1];
		
		//we didn't figure out any colors so far
		if(lastGuess == null || lastGuess.getNumReds() + lastGuess.getNumWhites() == 0)
		{
			throw new IllegalStateException("Can't figure out sequence with no right colors");
		}
				
		else if(lastGuess.getNumReds() + lastGuess.getNumWhites() != Game.getKeySize())
		{
//			//get all the colors that we know are for sure in the pattern because they were all one color and had some reds
//			for(int i=0; i<guessTracker; i++)
//			{
//				ComputerGuess guess = guesses[i];
//				if(guess.isAllOneColor())
//				{
//					for(int j=0; j<guess.getNumReds(); j++)
//					{
//						attempt[numColorsAdded++] = guess.getSequence()[0];
//					}
//				}
//				else
//				{
//					//TODO: look at results and solve
//					Color[] pastGuessSequence = guesses[i].getSequence();
//					
//					
//				}
//			}
	
			//colors we need to use this turn
			ArrayList<Color> colorsToUse = new ArrayList<>(colorsForSureInPattern);
			
			for(int i=0; i<attempt.length; i++)
			{
				Color color = colorsInRightSpots[i];
				
				if(color != null)
				{
					attempt[i] = color;
					numColorsAdded++;
					colorsToUse.remove(color);
				}
			}
			
			while(!colorsToUse.isEmpty())
			{
				Color color = colorsToUse.get(0);
				colorsToUse.remove(color);
				boolean usedColor = false;
				
				for(int i=0; i<attempt.length && !usedColor; i++)
				{
					if(attempt[i] == null)
					{
						attempt[i] = color;
						numColorsAdded++;
						usedColor = true;
					}
				}
			}
			
			//if we don't have four yet colors, we should fill the rest of the spaces with one specific color
			if(numColorsAdded != attempt.length)
			{
				Color color = allColors.get(numColorUpTo++);
				while(numColorsAdded != attempt.length)
				{
					attempt[numColorsAdded++] = color;
				}
			}
		}
		
//		else//we figured out all the colors, we just need to put them in the right order
//		{
//			attempt = guesses[guessTracker-1].getSequence();
//		}
//		
//		while (!checkIfSequenceIsPossible(attempt))
//		{
//			//TODO
//		}
		
		return attempt;
	}
	
	//TODO
	private boolean checkIfSequenceIsPossible(Color[] sequence)
	{
		return true;
	}

	public void setLastTurnResults(int numReds, int numWhites)
	{
		setNumRedsOfLastTurn(numReds);
		setNumWhitesOfLastTurn(numWhites);
		modifyColorsInRightSpots(numReds, numWhites);
		addToForSureColorsBasedOnLastTurn(numReds, numWhites);
	}
	
	private void setNumRedsOfLastTurn(int numReds)
	{
		if(numReds<0 || numReds>Game.getKeySize())
		{
			throw new IllegalArgumentException("numReds must be between 0 and " + Game.getKeySize());
		}
	
		guesses[guessTracker-1].setNumReds(numReds);
	}

	private void setNumWhitesOfLastTurn(int numWhites)
	{
		if(numWhites<0 || numWhites>Game.getKeySize())
		{
			throw new IllegalArgumentException("numWhites must be between 0 and " + Game.getKeySize());
		}
		
		guesses[guessTracker-1].setNumWhites(numWhites);
	}

	private void addToForSureColorsBasedOnLastTurn(int numReds, int numWhites)
	{
		ComputerGuess guess = guesses[guessTracker-1];
		
		if(guess.isAllOneColor())
		{
			Color color = guess.getSequence()[0];
			
			for(int i=0; i<numReds; i++)
			{
				colorsForSureInPattern.add(color);
			}
		}
		
		else//check if there's a color that wasn't yet added to the colors to use for sure
		{ 
			int total = numReds + numWhites;
			for(Color alreadyKnownColor: colorsForSureInPattern)
			{
				for(Color guessedColor: guess.getSequence())
				{
					if(guessedColor == alreadyKnownColor)
					{
						total--;
					}
				}
			}
			
			Color color = allColors.get(numColorUpTo-1);
			
			for(int i=0; i<total; i++)
			{
				colorsForSureInPattern.add(color);
			}
		}
	}
	
	private void modifyColorsInRightSpots(int numReds, int numWhites)
	{
		//if numReds = colorsForSureInPattern.size() then every color we guessed in last turn that in the list was in right spot
		//if only got back whites, also
		//if get back extra reds then you know that we need to add a color and the color that we guessed in the right spot
		//how do we know that let's say it was the red in the right spot? because there was only 1 red in the guess and 
		
		int total = numReds + numWhites;
		Color[] lastSequence = guesses[guessTracker-1].getSequence();
		
		if(numReds == Game.getKeySize())
		{
			colorsInRightSpots = lastSequence;
		}
		
		//all the colors we already knew were in the right spots and we didn't find any new colors
		else if(numReds >= colorsForSureInPattern.size() && numWhites == 0)
		{
			 for(int i=0; i<colorsForSureInPattern.size(); i++)
			 {
				 Color color = colorsForSureInPattern.get(i);
				 int index = -1;
				 
				 for(int j=0; j<lastSequence.length && index == -1; j++)
				 {
					 if(lastSequence[j] == color)
					 {
						 index = j;
					 }
				 }
				 
				 colorsInRightSpots[index] = color;
			 }
			 
			 if(numReds > colorsForSureInPattern.size())
			 {
				 //the most recently added color was also a red
				 Color color = allColors.get(numColorUpTo-1);
				 
				 int index = -1;
				 
				 for(int j=0; j<lastSequence.length && index == -1; j++)
				 {
					 if(lastSequence[j] == color)
					 {
						 index = j;
					 }
				 }
				 
				 colorsInRightSpots[index] = color;
			 }
		}
				
	}

	public static void main(String[] args)
	{
		ComputerPlayer comp = new ComputerPlayer(new Game(), ColorLevel.MEDIUM);
		System.out.println(comp.generateGuess().toString());
		comp.setLastTurnResults(1,0);
		System.out.println(comp.generateGuess().toString());
		comp.setLastTurnResults(2,0);
		System.out.println(comp.generateGuess().toString());
		comp.setLastTurnResults(2,0);
		System.out.println(comp.generateGuess().toString());
	}
}
