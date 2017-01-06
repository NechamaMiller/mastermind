package mastermind;

import java.util.ArrayList;
import java.util.Scanner;

import enums.*;

public class ComputerPlayer
{
	private ComputerGuess[] guesses;
	private int guessTracker;
	private ArrayList<Color> allColors;
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
		
		ComputerGuess lastGuess = guessTracker == 0 ? null : guesses[guessTracker-1];
		
		/*
		 * if we didn't yet guess any of the right color then take another color from the arraylist to try 
		 */
		if(guessTracker == 0 || colorsForSureInPattern.size() == 0)
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
		
		//return attempt;
		return guess;
	}
	
	/**
	 * This method should only be called when we know at least one right color
	 */
	private Color[] getSequenceToTry()
	{
		Color[] attempt = new Color[Game.getKeySize()];
		ComputerGuess lastGuess = guessTracker == 0 ? null : guesses[guessTracker-1];
		
		//we didn't figure out any colors so far
		if(lastGuess == null || lastGuess.getNumReds() + lastGuess.getNumWhites() == 0)
		{
			throw new IllegalStateException("Can't figure out sequence with no right colors");
		}
				
		else if(colorsForSureInPattern.size() == 0)
		{
			//colors we need to use this turn
			//copy constructor for ArrayList
			ArrayList<Color> colorsToStillUse = new ArrayList<>(colorsForSureInPattern);
			
			for(int i=0; i<colorsInRightSpots.length; i++)
			{
				Color color = colorsInRightSpots[i];
				
				if(color != null)
				{
					attempt[i] = color;
					colorsToStillUse.remove(color);
				}
			}		
			
			while(!colorsToStillUse.isEmpty())
			{
				Color color = colorsToStillUse.get(0);
				colorsToStillUse.remove(color);
				boolean usedColor = false;
				
				if (lastGuess.getNumWhites() != 0)
				{
					//TODO make sure for sure use color
					for (int i = 0; i < attempt.length && !usedColor; i++)
					{
						if (attempt[i] == null)
						{
							boolean colorHereBefore = false;
							for (int j = 0; j < guessTracker && !colorHereBefore; j++)
							{
								//should only look at guesses that gave us back whites to decide that a spot is wrong for a color
								if (guesses[j].getNumWhites() >0 && guesses[j].getSequence()[i] == color)//TODO it's not a guess that had whites, what is it?
								{
									colorHereBefore = true;
								}
							}

							if (!colorHereBefore)
							{
								attempt[i] = color;
								usedColor = true;
							}
						}
					} 
				}
				//TODO if didn't use all colors in colorsForSureInPattern, then use
				else
				{
					for(int i=0; i<attempt.length && !usedColor; i++)
					{
						if(attempt[i] == null)
						{
							attempt[i] = color;
							usedColor = true;
						}
					}
				}
			}
			
			//if not yet 4 colors
			Color color = allColors.get(numColorUpTo++);
			for(int i=0; i<attempt.length; i++)
			{
				if(attempt[i] == null)
				{
					attempt[i] = color;
				}
			}
		}
	
		return attempt;
	}
	
	public void setLastTurnResults(int numReds, int numWhites)
	{
		setNumRedsOfLastTurn(numReds);
		setNumWhitesOfLastTurn(numWhites);
		addToRightColorsBasedOnLastTurn(numReds, numWhites);
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
	
	private void addToRightColorsBasedOnLastTurn(int numReds, int numWhites)
	{
		Color[] lastSequence = guesses[guessTracker-1].getSequence();
		
		if(numReds == Game.getKeySize())
		{
			colorsInRightSpots = lastSequence;
		}
		
		//we figured out colors to add to colorsInRightSpots
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
		}
		
		if(numReds + numWhites > colorsForSureInPattern.size())
		{
			//the most recently added color is also in the pattern
			 Color color = allColors.get(numColorUpTo-1);
			 
			 colorsForSureInPattern.add(color);
		}
				
	}

	public static void main(String[] args)
	{
		Scanner input = new Scanner(System.in);
	
		ComputerPlayer comp = new ComputerPlayer(new Game(), ColorLevel.MEDIUM);
		
		System.out.println(comp.generateGuess().toString());
		System.out.print("Enter numReds of last guess:");
		int numReds = input.nextInt();
		System.out.print("Enter numWhites of last guess:");
		int numWhites = input.nextInt();
		comp.setLastTurnResults(numReds, numWhites);
		
		System.out.println();
		System.out.println(comp.generateGuess().toString());
		System.out.print("Enter numReds of last guess:");
		numReds = input.nextInt();
		System.out.print("Enter numWhites of last guess:");
		numWhites = input.nextInt();
		comp.setLastTurnResults(numReds, numWhites);
		
		System.out.println();
		System.out.println(comp.generateGuess().toString());
		System.out.print("Enter numReds of last guess:");
		numReds = input.nextInt();
		System.out.print("Enter numWhites of last guess:");
		numWhites = input.nextInt();
		comp.setLastTurnResults(numReds, numWhites);
		
		System.out.println();
		System.out.println(comp.generateGuess().toString());
		System.out.print("Enter numReds of last guess:");
		numReds = input.nextInt();
		System.out.print("Enter numWhites of last guess:");
		numWhites = input.nextInt();
		comp.setLastTurnResults(numReds, numWhites);
		
		System.out.println();
		System.out.println(comp.generateGuess().toString());
		System.out.print("Enter numReds of last guess:");
		numReds = input.nextInt();
		System.out.print("Enter numWhites of last guess:");
		numWhites = input.nextInt();
		comp.setLastTurnResults(numReds, numWhites);
		
		System.out.println();
		System.out.println(comp.generateGuess().toString());
		System.out.print("Enter numReds of last guess:");
		numReds = input.nextInt();
		System.out.print("Enter numWhites of last guess:");
		numWhites = input.nextInt();
		comp.setLastTurnResults(numReds, numWhites);
		
		System.out.println();
		System.out.println(comp.generateGuess().toString());
	}
}
