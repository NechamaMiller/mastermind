package tests;

import static org.junit.Assert.*;
import org.junit.*;

import enums.Color;
import enums.ColorLevel;
import enums.GuessLevel;
import mastermind.*;
import exceptions.*;

public class GameTests {
	/*
	 * TL Note: because Game does not contain any setters, it is difficult to test many methods. 
	 * Therefore, Game does not have so many tests, but its components (Key and Guess) were broken 
	 * out and tested separately.
	 */
	private Game game;
	@Before
	public void setUp()
	{
		game = new Game();
	}
	
	@Test
	public void testGetKeyAfterTenGuesses()
	{
		Color[] attempt = {Color.RED, Color.GREEN, Color.ORANGE, Color.RED};
		game.checkGuess(attempt);
		game.checkGuess(attempt);
		game.checkGuess(attempt);
		game.checkGuess(attempt);
		game.checkGuess(attempt);
		game.checkGuess(attempt);
		game.checkGuess(attempt);
		game.checkGuess(attempt);
		game.checkGuess(attempt);
		game.checkGuess(attempt);
		for (Color c : game.getKey())
			System.out.print(c.toString() + " ");		
	}
	
	@Test (expected = NullPointerException.class)
	public void testGetKeyAfterNineGuesses()
	{
		Color[] attempt = {Color.RED, Color.GREEN, Color.ORANGE, Color.RED};
		game.checkGuess(attempt);
		game.checkGuess(attempt);
		game.checkGuess(attempt);
		game.checkGuess(attempt);
		game.checkGuess(attempt);
		game.checkGuess(attempt);
		game.checkGuess(attempt);
		game.checkGuess(attempt);
		game.checkGuess(attempt);
		for (Color c : game.getKey())
			System.out.print(c.toString() + " ");		
	}
	
	@Test
	public void testGetKeyReturnsNullWhenGameNotWon()
	{
		assertNull(game.getKey());
	}
	
	@Test
	public void testGetNumGuessesAfterThreeGuesses()
	{
		game.checkGuess(new Color[] {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW});
		game.checkGuess(new Color[] {Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE});
		game.checkGuess(new Color[] {Color.RED, Color.YELLOW, Color.RED, Color.YELLOW});
		
		assertEquals(3, game.getNumGuessesMade());
	}
	
	@Test
	public void testGetGuessesContainsTheGuessesAfterThreeGuesses()
	{
		Color[] guess1 = {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW};
		Color[] guess2 = {Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE};
		Color[] guess3 = {Color.RED, Color.YELLOW, Color.RED, Color.YELLOW};
		
		game.checkGuess(guess1);
		game.checkGuess(guess2);
		game.checkGuess(guess3);
		
		assertArrayEquals(guess1, game.getGuesses()[0].getSequence());
		assertArrayEquals(guess2, game.getGuesses()[1].getSequence());
		assertArrayEquals(guess3, game.getGuesses()[2].getSequence());
	}
	
	@Test
	public void ConstructorSetsGuessAndColorLevelToRightValues()
	{
		assertEquals(GuessLevel.MEDIUM.getNumGuesses(), game.getTotalNumTurns());
		assertEquals(ColorLevel.MEDIUM.getNumColors(), game.getNumColors());
	}
}
