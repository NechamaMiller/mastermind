//TODO: fix numWhites when red

package mastermind;

import java.util.Scanner;

import enums.*;

public class Mastermind 
{	
	private static Game game;
	private static Scanner input = new Scanner(System.in);
	
	public static void main (String[] args)
	{
		greeting();
		System.out.println();
		
		GuessLevel guessLevel = getGuessLevel();
		System.out.println();
		
		ColorLevel colorLevel = getColorLevel();
		System.out.println();
		
		game = new Game(guessLevel, colorLevel);	
		System.out.println("You will have " + game.getTotalNumTurns() + " turns to guess my code picking from " + game.getNumColors() + " colors.\n");
		
		do
		{
			game.checkGuess(getGuess());
			
			if (game.isGameWon())
			{
				System.out.print("You guessed my code! It was " + printKey() + ". You win!!!");
			}
			else
			{
				System.out.println("\nHere are your guesses so far:");
				for (int i=0; i<game.getNumGuessesMade(); i++)
				{
					System.out.println("\nGuess number " + (i+1) + ":\n" + game.getGuesses()[i]);
				}
			}
		}
		while (game.getNumGuessesMade()<game.getTotalNumTurns() && !game.isGameWon());
		
		if (!(game.isGameWon()))
		{
			System.out.println("Game over! You lose! My code was " + printKey() + ". Better luck next time...");
		}
	}
	
	public static void greeting()
	{
		System.out.println("Welcome to Mastermind. Here are the rules of the game:\n"
				+ "Before we start, I will let you pick what level you want to be on as far as the number of turns you can have and the number of color choices in the code.\n"
				+ "You can also choose to use my default levels.\n"
				+ "Then, I will generate a secret code. Your job is to break that code!\n"
				+ "The number of colors to choose from will depend on your level, but the total colors are " + displayColors() + ".\n"
				+ "My code can be any combination of " + Game.getKeySize() + " of these colors - repeats are allowed.\n"
				+ "The number of turns you will have will also depend on your level.\n"
				+ "On each turn, you will enter " + Game.getKeySize() + " colors, and I will tell you how many whites you scored on this round, and how many reds.\n"
				+ "A \"white\" means you got the right color, but in the wrong place and a \"red\" means you got a color in the right place.\n"
				+ "If you guess my code before you run out of turns, you win!\n"
				+ "Good luck!");
	}
	
	public static String displayColors()
	{
		StringBuilder sb = new StringBuilder();
		
		int endOfLoop = (game == null) ? Color.values().length : game.getNumColors();

		for (int i=0; i<endOfLoop; i++)
		{
			sb.append(Color.values()[i].toString().toLowerCase());
			
			if (i<endOfLoop-2)
			{
				sb.append(", ");
			}
			else if(i == endOfLoop-2)
			{
				sb.append(" and ");
			}
		}
		
		return sb.toString();
	}
	
	public static Color[] getGuess()
	{
		Color[] colors = new Color[Game.getKeySize()];
		
		
		System.out.println("\nEnter the next sequence you would like to guess."
				+ "\nYou can choose any " + Game.getKeySize() + " of the following colors: " + displayColors());			
		
		for (int i=0; i<colors.length;i++)
		{
			String color = input.next().toUpperCase();
			
			while (!(isValidColor(color)))
			{
				//TL added a more descriptive error message that makes it clear to the user that they have to 
				//re-enter all colors (not only erroneous one) again, because they were overlooked
				System.out.println("Color number " + (i+1) + " wasn't valid. Please try entering it "
						+ (i < 3 ? "(and the rest of your sequence from that number on) " : "") + "again:");
				input.nextLine(); //clear keyboard buffer
				color = input.next().toUpperCase();
			}
			
			colors[i] = Color.valueOf(color);
		}
		
		input.nextLine();//clear keyboard buffer in case user mistakenly entered something extra

		return colors;
	}
	
	public static boolean isValidColor(String input)
	{
		if (input == null)
		{
			return false;
		}
				
		for (int i=0; i<game.getNumColors(); i++)
		{
			Color color = Color.values()[i];
			if (color.toString().equals(input.toUpperCase()))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public static String printKey ()
	{
		StringBuilder sb = new StringBuilder();
		
		for (Color color: game.getKey())
		{
			sb.append(color.toString() + " ");
		}
		
		return sb.toString().trim();
	}
	
	public static GuessLevel getGuessLevel()
	{
		System.out.println("What guess level do you want to play on?");
		
		for (GuessLevel level: GuessLevel.values())
		{
			System.out.println((level.ordinal()+1) + ": " + level.toString().toLowerCase() + " (" + level.getNumGuesses() + " guesses)");
		}
		
		System.out.print("Enter number here:");
		int choice = input.nextInt();
		
		while (choice<1 || choice>GuessLevel.values().length)
		{
			System.out.print("Your number must be between 1 and " + GuessLevel.values().length + ". Please try again: ");
			choice = input.nextInt();
		}
		
		return GuessLevel.values()[choice-1];
	}

	public static ColorLevel getColorLevel()
	{
		System.out.println("What color-choice level do you want to play on?");
		
		for (ColorLevel level: ColorLevel.values())
		{
			System.out.println((level.ordinal()+1) + ": " + level.toString().toLowerCase() + " (" + level.getNumColors() + " color choices)");
		}
		
		System.out.print("Enter number here:");
		int choice = input.nextInt();
		
		while (choice<1 || choice>ColorLevel.values().length)
		{
			System.out.print("Your number must be between 1 and " + ColorLevel.values().length + ". Please try again: ");
			choice = input.nextInt();
		}

		return ColorLevel.values()[choice-1];
	}

}