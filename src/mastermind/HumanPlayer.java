package mastermind;

import enums.*;

public class HumanPlayer
{

	private Key key;// holds the sequence the player must guess
	private HumanGuess[] guesses;// holds guess objects--the guesses of the user for reference
	private int guessTracker;/*this keeps track of what position we are at in the guesses array and what
								row we are in in the results array*/
	private boolean gameWon;// becomes true if user wins
	private int numColors;
	
	/**Constructor*/
	public HumanPlayer()
	{
		this(GuessLevel.MEDIUM, ColorLevel.MEDIUM, null);
	}
	
	public HumanPlayer(GuessLevel guessLevel, Key userKey)
	{
		this(guessLevel, ColorLevel.MEDIUM, userKey);
	}
	
	public HumanPlayer(ColorLevel colorLevel, Key userKey)
	{
		this(GuessLevel.MEDIUM, colorLevel, userKey);
	}
	
	public HumanPlayer(GuessLevel geussLevel, ColorLevel colorLevel)
	{
		this(geussLevel, colorLevel, null);
	}
	
	public HumanPlayer (GuessLevel guessLevel, ColorLevel colorLevel, Key userKey)
	{
		numColors = colorLevel.getNumColors();
		guessTracker = 0;
		guesses = new HumanGuess[guessLevel.getNumGuesses()];
		gameWon = false;
		
		if(userKey==null)
		{
			key = new Key(Mastermind.getKeySize(), numColors);// calls method to create a random sequence to guess
		}
		else
		{
			key=userKey;
		}
	}
	
	
	
	/**getGuesses: a getter method to return the array of all past guesses
	 * @return the array of past guesses*/
	public HumanGuess[] getGuesses()
	{
		if (this.guessTracker == 0)
		{
			return null;
		}
		return this.guesses;
	}

	public void checkGuess(Color[] attempt)
	{
		HumanGuess g = new HumanGuess(key.getKey(), attempt);
		g.checkForReds();
		g.checkForWhites();
		guesses[guessTracker] = g; //store this guess in the guesses array
		if (g.isAllRed())
			gameWon = true;
		guessTracker++;
	}
	
	public boolean isGameWon()//Nechama added this
	{
		return gameWon;
	}

	/**
	 * This will return the key (as a Color array) ONLY if the game is over, either because the user won or because he ran out of guesses. Otherwise, it will return null.
	 */
	public Color[] getKey()
	{		
		if (gameWon || guessTracker == guesses.length)
		{
			return key.getKey();
		}
		else
		{
			return null;
		}
	}
	
	public int getNumColors()
	{
		return numColors;
	}
	
	/**getNumGuessesMade: a getter
	 * @return the number of guesses made so far*/
	public int getNumGuessesMade()
	{
		return this.guessTracker;
	}

	/**
	 * @return total number of turns the user has in the game
	 */
	public int getTotalNumTurns()
	{
		return guesses.length;
	}
}// Game class
