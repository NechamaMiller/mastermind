package mastermind;

import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import enums.*;

public class Mastermind
{
	private static Game game;
	private static Scanner input = new Scanner(System.in);
	private static ColorLevel colorLevel;
	private static JFrame frame;

	public static void main(String[] args)
	{
		// NM made this to use for enclosing dialog boxes so will always be on top of screent
		// (used by more than one method, so it's a field)
		frame = new JFrame();
		frame.setAlwaysOnTop(true);
		frame.setVisible(false);// you don't actually want to see the JFrame, it's just to hold the JOptionPanes
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		greeting();

		System.out.println();

		GuessLevel guessLevel = getGuessLevel();
		System.out.println();

		colorLevel = getColorLevel();
		System.out.println();

		Key userKey;

		// find out if want multiplayer
		// KR
		Boolean multiplayer = multiplayer();
		System.out.println();

		if (multiplayer)
		{
			userKey = enterKey();
		}
		else
		{
			userKey = null;
		}
		
		game = new Game(guessLevel, colorLevel, userKey);

		System.out.println();

		System.out.println("You will have " + game.getTotalNumTurns() + " turns to guess the code picking from "
				+ game.getNumColors() + " colors.");

		do
		{
			game.checkGuess(getGuess());

			if (game.isGameWon())
			{
				System.out.println("You guessed the code! It was " + printKey() + ". You win!!!");
			}
			else
			{
				System.out.println("\nHere are your guesses so far:");
				for (int i = 0; i < game.getNumGuessesMade(); i++)
				{
					System.out.println("\nGuess number " + (i + 1) + ":\n" + game.getGuesses()[i]);
				}
			}
		}
		while (game.getNumGuessesMade() < game.getTotalNumTurns() && !game.isGameWon());

		if (!(game.isGameWon()))
		{
			System.out.println("Game over! You lose! The code was " + printKey() + ". Better luck next time...");
		}
		
		System.exit(0);// need because of dialog boxes
	}

	public static void greeting()
	{
		System.out.println("Welcome to Mastermind. Here are the rules of the game:\n"
				+ "Before we start, I will let you pick what level you want to be on as far as the number of turns you can have and the number of color choices in the code.\n"
				+ "You can also choose to use my default levels.\n"
				+ "Then, I will generate a secret code. Or, you can have a friend enter the code. Your job is to break that code!\n"
				+ "The number of colors to choose from will depend on your level, but the total colors are "
				+ displayColors() + ".\n"// KR don't have a ColorLevel yet so return values of enum in array and count using .length
				+ "My code can be any combination of " + Game.getKeySize() + " of these colors - repeats are allowed.\n"
				+ "The number of turns you will have will also depend on your level.\n"
				+ "On each turn, you will enter " + Game.getKeySize()
				+ " colors, and I will tell you how many whites you scored on this round, and how many reds.\n"
				+ "A \"white\" means you got the right color, but in the wrong place and a \"red\" means you got a color in the right place.\n"
				+ "If you guess my code before you run out of turns, you win!\n" + "Good luck!");
	}

	public static String displayColors()
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
	public static Color[] getGuess()
	{
		System.out.println("\nEnter the next sequence you would like to guess, separated by spaces." + "\nYou can choose any "
				+ Game.getKeySize() + " of the following colors: " + displayColors());

		String key = input.nextLine().trim();

		return getColorSequenceFromString(key, false);
	}

	/**
	 * Colors passed in must be ready to be sent to the Color enum (i.e. already capitalized)
	 */
	public static boolean isValidColor(String input)
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

	public static String printKey()
	{
		StringBuilder sb = new StringBuilder();

		for (Color color : game.getKey())
		{
			sb.append(color.toString() + " ");
		}

		return sb.toString().trim();
	}

	public static GuessLevel getGuessLevel()
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

	public static ColorLevel getColorLevel()
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

	public static boolean multiplayer()
	{
		input.nextLine();// clear keyboard buffer
		System.out.println("Do you want a friend to enter the key to guess instead of the computer?"
				+ " enter \"y\" for yes or \"n\" for no");
		String yn = input.nextLine().toLowerCase();
		char enterKey = yn.charAt(0);
		while (enterKey != 'y' && enterKey != 'n')
		{
			System.out.println("Invalid entry. Please enter \"y\" or \"n\"");
			yn = input.nextLine().toLowerCase();
			enterKey = yn.charAt(0);
		}
		if (enterKey == 'y')
		{
			return true;
		}
		else
			return false;

	}

	// KR but basically copied the getGuess method
	// NM made it be able to use dialog box
	public static Key enterKey()
	{
		String key;

		do
		{
			key = JOptionPane.showInputDialog(frame, "Enter the secret code for your friend to guess, separated by spaces."
					+ "\nYou can choose any " + Game.getKeySize() + " of the following colors: " + displayColors());
		}
		while (key == null);// if they pressed cancel on the dialog box without entering anything

		return new Key(getColorSequenceFromString(key, true));// creates and returns a new Key instantiated from the second constructor
	}

	private static Color[] getColorSequenceFromString(String entry, boolean useDialogBox)
	{
		Color[] colors = new Color[Game.getKeySize()];

		//get rid of extra spaces so can split on a string
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
}
