package mastermind;

import java.util.Random;

public class Game
{

	private Color[] key = new Color[4];// holds the sequence the player must guess
	private Guess[] guesses;// holds guess objects--the guesses of the user for reference
	private int[][] results;/*holds the status of each peg--red, white, or none
							this is NOT for player use but rather for the programmers'/computer's use for future advancements
							such as tracking logical consistency*/
	private int guessTracker;/*this keeps track of what position we are at in the guesses array and what
								row we are in in the results array*/
	private boolean gameWon;// becomes true if user wins

	/**Constructor*/
	public Game()
	{
		this.key = generateKey();// calls method to create a random sequence to guess
		this.guessTracker = 0;
		// the following hardcoded as the player can only have 10 guesses and the sequence can only be four long
		guesses = new Guess[10];
		results = new int[10][4];
		gameWon = false;

	}

	/**generateKey--generates a random sequence to be guessed by player
	 * this is a private method only used within this class
	 * @return an array of the sequence of the enum type Color*/
	private Color[] generateKey()
	{

		// in a for loop to populate each of 4 spaces in array
		for (int index = 0; index < key.length; index++)
		{
			/*create a Random obj to generate a number randomly 0-5
			 * purposely not doing 1-6 because ordinality of enum starts at 0 */
			Random rand = new Random();
			int num = rand.nextInt(6);

			Color col = Color.values()[num];

			key[index] = col;// set each location to the color determined by the Random
		} // for
		return key;
	}// generateKey()

	/**getGuesses: a getter method to return the array of all past guesses
	 * @return the array of past guesses*/
	public Guess[] getGuesses()
	{
		if (this.guessTracker == 0)
		{
			// TODO then need to let user know because empty array
		}
		return this.guesses;
	}

	/**getNumGuesses: a getter
	 * @return the number of guesses made so far*/
	public int getNumGuesses()
	{
		return this.guessTracker;
	}

	public void checkGuess(Color[] attempt)
	{

		int numReds = 0;
		int numWhites = 0;
		// boolean found=false;Nechama moved this to inside the for loop because its for each element
		boolean[] found = new boolean[4];// Nechama added this so that is something is red, it doesn't also get counted as a white if there are duplicates

		// check for reds
		for (int index = 0; index < key.length; index++)
		{
			// if right color right place the row of the turn up to in game and the column up to in loop
			// gets a 1 to symbolize a red
			if (attempt[index].equals(key[index]))
			{
				results[guessTracker][index] = 1;
				numReds++;// and total red pegs updated
				found[index] = true;
			}
		}

		// check for whites
		if (numReds != 4)
		{
			for (int index = 0; index < key.length; index++)
			{
				for (int x = 0; x < key.length; x++)
				{
					if (x != index && !found[x] && attempt[index].equals(key[x]))
					{
						results[guessTracker][index] = 2;
						numWhites++;
						found[x] = true;
					}
				}
			}
		}
		else
		{
			gameWon = true;
		}
		// create new guess object
		Guess guess = new Guess(numReds, numWhites, attempt);
		// insert that new object into the guesses array
		guesses[guessTracker] = guess;
		// increment the guess tracker for the next turn
		guessTracker++;
	}

	// checkGuess

	public boolean isGameWon()// Nechama added this
	{
		return gameWon;
	}

	/**
	 * This will return the key (as a String) ONLY if the game is over, either because the user one or because he ran out of guesses. Otherwise, it will return null.
	 */
	public String getKey()
	{
		if (gameWon || guessTracker == guesses.length)
		{
			StringBuilder sb = new StringBuilder();

			for (Color color : key)
			{
				sb.append(color.toString() + " ");
			}

			return sb.toString();
		}
		else
		{
			return null;
		}
	}
}// Game class
