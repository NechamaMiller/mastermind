package mastermind;

import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import enums.*;
import exceptions.*;

public class Mastermind
{
	private static Scanner input = new Scanner(System.in);
	private static GuessLevel guessLevel;
	private static ColorLevel colorLevel;
	private static JFrame frame;
	private final static int KEY_SIZE = 4;

	public static void main(String[] args)
	{
		// NM made this to use for enclosing dialog boxes so will always be on top of screen
		// (used by more than one method, so it's a field)
		frame = new JFrame();
		frame.setAlwaysOnTop(true);
		frame.setVisible(false);// you don't actually want to see the JFrame, it's just to hold the JOptionPanes
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		greeting();

		System.out.println();

		boolean humanGuesser = playerWillGuessCode();
		System.out.println();

		if (humanGuesser)
		{
			humanGuesser();
		}
		else
		{
			computerGuesser();
		}

	}

	public static void greeting()
	{
		System.out.println("Welcome to Mastermind. Here are the rules of the game:\n"
				+ "Before we start, I will let you pick what level you want to be on as far as the number of turns you can have and the number of color choices in the code.\n"
				+ "Then, I will generate a secret code. Or, you can have a friend enter the code. Your job is to break that code!\n"
				+ "The number of colors to choose from will depend on your level, but the total colors are "
				+ displayColors() + ".\n" + "My code can be any combination of " + KEY_SIZE
				+ " of these colors - repeats are allowed.\n"
				+ "The number of turns you will have will also depend on your level.\n"
				+ "On each turn, you will enter " + KEY_SIZE
				+ " colors, and I will tell you how many whites you scored on this round, and how many reds.\n"
				+ "A \"white\" means you got the right color, but in the wrong place and a \"red\" means you got a color in the right place.\n"
				+ "If you guess my code before you run out of turns, you win!\n"
				+ "Getting bored of playing this way? You can also try to see if you can make a code that the computer can't guess!\n"
				+ "Using this option, you pick the code and the computer has to guess it, using the same rules as above.\n"
				+ "Good luck!");
	}

	private static void humanGuesser()
	{
		guessLevel = getGuessLevel();
		System.out.println();
	
		colorLevel = getColorLevel();
		System.out.println();
	
		input.nextLine();// clear keyboard buffer
	
		Boolean multiplayer = multiplayer();
		System.out.println();
	
		Key userKey;
	
		if (multiplayer)
		{
			userKey = enterKey(true);
		}
		else
		{
			userKey = null;
		}
	
		HumanPlayer humanPlayer = new HumanPlayer(guessLevel, colorLevel, userKey);
	
		System.out.println();
	
		System.out.println("You will have " + humanPlayer.getTotalNumTurns()
				+ " turns to guess the code picking from  the following colors: " + displayColors());
	
		do
		{
			humanPlayer.checkGuess(getGuess());
	
			if (humanPlayer.isGameWon())
			{
				System.out
						.println("You guessed the code! It was " + printColorSequence(humanPlayer.getKey()) + ". You win!!!");
			}
			else
			{
				System.out.println("\nHere are your guesses so far:");
				for (int i = 0; i < humanPlayer.getNumGuessesMade(); i++)
				{
					System.out.println("\nGuess number " + (i + 1) + ":\n" + humanPlayer.getGuesses()[i]);
				}
			}
		}
		while (humanPlayer.getNumGuessesMade() < humanPlayer.getTotalNumTurns() && !humanPlayer.isGameWon());
	
		if (!(humanPlayer.isGameWon()))
		{
			System.out.println("Game over! You lose! The code was " + printColorSequence(humanPlayer.getKey())
					+ ". Better luck next time...");
		}
	
		System.exit(0);// need because of dialog boxes
	
	}

	private static void computerGuesser()
	{
		guessLevel = GuessLevel.MEDIUM;
		colorLevel = ColorLevel.MEDIUM;
	
		System.out.println("I will have " + guessLevel.getNumGuesses()
				+ " turns to guess your code, picking from the following colors: " + displayColors() + ".\n");
	
		Key key = enterKey(false);
	
		boolean giveReminder = giveReminder();
	
		ComputerPlayer player = new ComputerPlayer(guessLevel, colorLevel);
	
		System.out.println();
	
		while (!player.isGameOver())
		{
			Color[] attempt = null;
			try
			{
				attempt = player.generateGuess();
			}
			catch (CantGenerateGuessException e)//if computer couldn't guess code
			{
				input.nextLine();
				boolean wasIncorrect = getBoolInput("I can't seem to guess your code, are you sure you entered all your reds and whites corrrectly?"
						+ "\nIf you entered something incorrectly, please enter \"y\"\n");				
				
				System.out.println();
				
				if(!wasIncorrect)
				{
					System.out.println("Well in that case, you win! Great game!");
					System.exit(0);
				}
				
				else
				{
					System.out.println("Okay, so I'm going to start my guesses over. Make sure to give right input this time!");
					player = new ComputerPlayer(guessLevel, colorLevel);
					attempt = player.generateGuess();
				}
			}
	
			System.out.println("Hmm... My next guess is " + printColorSequence(attempt));
	
			System.out.println();
	
			if (giveReminder)
			{
				System.out.println("Just a reminder, your key was " + printColorSequence(key.getKey()) + ".");
				System.out.println();
			}
	
			System.out.print("How many reds did I get this turn?");
			int numReds = input.nextInt();
			System.out.print("How many whites did I get this turn?");
			int numWhites = input.nextInt();
			player.setLastTurnResults(numReds, numWhites);
	
			while (numReds + numWhites > KEY_SIZE || numReds < 0 || numWhites < 0)
			{
				System.out.println("Oops! You entered something invalid. Please try again:");
	
				System.out.print("How many reds did I get this turn?");
				numReds = input.nextInt();
				System.out.print("How many whites did I get this turn?");
				numWhites = input.nextInt();
				player.setLastTurnResults(numReds, numWhites);
			}
	
			System.out.println();
		}
	
		if (player.isGameWon())
		{
			System.out.println("Yay! I guessed your code!");
		}
		else
		{
			System.out.println("You win! I ran out of turns to guess your code.");
		}
	}

	private static String displayColors()
	{
		StringBuilder sb = new StringBuilder();

		int endOfLoop = (colorLevel == null) ? Color.values().length : colorLevel.getNumColors();

		for (int i = 0; i < endOfLoop; i++)
		{
			sb.append(Color.values()[i].toString().toLowerCase());

			if (i < endOfLoop - 2)
			{
				sb.append(", ");
			}
			else if (i == endOfLoop - 2)
			{
				sb.append(" and ");
			}
		}

		return sb.toString();
	}

	// edited by NM
	private static Color[] getGuess()
	{
		System.out.println("\nEnter the next sequence you would like to guess, separated by spaces."
				+ "\nYou can choose any " + KEY_SIZE + " of the following colors: " + displayColors());

		String key = input.nextLine().trim();

		return getColorSequenceFromString(key, false);
	}

	/**
	 * Colors passed in must be ready to be sent to the Color enum (i.e. already capitalized)
	 */
	private static boolean isValidColor(String input)
	{
		if (input == null)
		{
			return false;
		}

		for (int i = 0; i < colorLevel.getNumColors(); i++)
		{
			Color color = Color.values()[i];
			if (color.toString().equals(input))
			{
				return true;
			}
		}

		return false;
	}

	private static String printColorSequence(Color[] sequence)
	{
		StringBuilder sb = new StringBuilder();

		for (Color color : sequence)
		{
			sb.append(color.toString() + " ");
		}

		return sb.toString().trim();
	}

	private static GuessLevel getGuessLevel()
	{
		System.out.println("What guess level do you want to play on?");

		for (GuessLevel level : GuessLevel.values())
		{
			System.out.println((level.ordinal() + 1) + ": " + level.toString().toLowerCase() + " ("
					+ level.getNumGuesses() + " guesses)");
		}

		System.out.print("Enter number here:");
		int choice = input.nextInt();

		while (choice < 1 || choice > GuessLevel.values().length)
		{
			System.out
					.print("Your number must be between 1 and " + GuessLevel.values().length + ". Please try again: ");
			choice = input.nextInt();
		}

		return GuessLevel.values()[choice - 1];
	}

	private static ColorLevel getColorLevel()
	{
		System.out.println("What color-choice level do you want to play on?");

		for (ColorLevel level : ColorLevel.values())
		{
			System.out.println((level.ordinal() + 1) + ": " + level.toString().toLowerCase() + " ("
					+ level.getNumColors() + " color choices)");
		}

		System.out.print("Enter number here:");
		int choice = input.nextInt();

		while (choice < 1 || choice > ColorLevel.values().length)
		{
			System.out
					.print("Your number must be between 1 and " + ColorLevel.values().length + ". Please try again: ");
			choice = input.nextInt();
		}

		return ColorLevel.values()[choice - 1];
	}

	private static boolean playerWillGuessCode()
	{
		return getBoolInput("Do you want to guess the code(y), or should the computer try to guess your code(n)?\n");
	}

	public static boolean multiplayer()
	{
		return getBoolInput("Do you want a friend to enter the key to guess instead of the computer?\n");
	}

	public static Key enterKey(boolean useDialogBox)
	{
		String key;

		String message = "Enter the secret code, separated by spaces." + "\nYou can choose any " + KEY_SIZE
				+ " of the following colors: " + displayColors();

		if (useDialogBox)
		{
			do
			{
				key = JOptionPane.showInputDialog(frame, message);
			}
			while (key == null);// if they pressed cancel on the dialog box without entering anything
		}
		else
		{
			System.out.println(message);
			key = input.nextLine();
		}

		return new Key(getColorSequenceFromString(key, useDialogBox));// creates and returns a new Key instantiated from the second constructor
	}

	private static Color[] getColorSequenceFromString(String entry, boolean useDialogBox)
	{
		Color[] colors = new Color[KEY_SIZE];

		// get rid of extra spaces so can split on a string
		entry = getRidOfExtraSpacesInString(entry);

		String[] entryColors = entry.split(" ");

		while (entryColors.length != colors.length)
		{
			String string = "You did not enter " + colors.length + " colors. Please try again.";

			if (useDialogBox)
			{
				do
				{
					entry = JOptionPane.showInputDialog(frame, string);
				}
				while (entry == null);// if they pressed cancel on the dialog box without entering anything
			}
			else
			{
				System.out.println(string);
				entry = input.nextLine();
			}

			entry = getRidOfExtraSpacesInString(entry);

			entryColors = entry.split(" ");
		}

		for (int i = 0; i < colors.length; i++)
		{
			String color = entryColors[i].toUpperCase();

			while (!(isValidColor(color)))
			{
				String string = "Color number " + (i + 1) + " was invalid. Please enter it again:";

				if (useDialogBox)
				{
					color = null;

					do
					{
						color = JOptionPane.showInputDialog(frame, string);

					}
					while (color == null);
				}

				else
				{
					System.out.println(string);
					color = input.nextLine();
				}
				color = color.toUpperCase().trim();
			}

			colors[i] = Color.valueOf(color);
		}

		return colors;
	}

	/**
	 * removes leading and trailing whitespace and 2 spaces in a row
	 * @return
	 */
	private static String getRidOfExtraSpacesInString(String entry)
	{
		entry = entry.trim();

		// get rid of any 2 spaces in a row, so can split on a space
		while (entry.contains("  "))
		{
			entry = entry.replace("  ", " ");
		}

		return entry;
	}

	private static boolean giveReminder()
	{
		return getBoolInput("Do you want to be reminded what your code was before every turn?\n");
	}	
	
	private static boolean getBoolInput(String message)
	{
		System.out.println(message + "Enter \"y\" for yes or \"n\" for no");

		String yn = input.nextLine().toLowerCase();
		char enterKey = yn.charAt(0);
		while (enterKey != 'y' && enterKey != 'n')
		{
			System.out.println("Invalid entry. Please enter \"y\" or \"n\"");
			yn = input.nextLine().toLowerCase();
			enterKey = yn.charAt(0);
		}

		return enterKey == 'y';
	}
	
	public static int getKeySize()
	{
		return KEY_SIZE;
	}
}
