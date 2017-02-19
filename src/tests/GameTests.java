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
	private HumanPlayer humanPlayer;
	@Before
	public void setUp()
	{
		humanPlayer = new HumanPlayer();
	}
	
	@Test
	public void testGetKeyAfterTenGuesses()
	{
		Color[] attempt = {Color.RED, Color.GREEN, Color.ORANGE, Color.RED};
		humanPlayer.checkGuess(attempt);
		humanPlayer.checkGuess(attempt);
		humanPlayer.checkGuess(attempt);
		humanPlayer.checkGuess(attempt);
		humanPlayer.checkGuess(attempt);
		humanPlayer.checkGuess(attempt);
		humanPlayer.checkGuess(attempt);
		humanPlayer.checkGuess(attempt);
		humanPlayer.checkGuess(attempt);
		humanPlayer.checkGuess(attempt);
		for (Color c : humanPlayer.getKey())
			System.out.print(c.toString() + " ");		
	}
	
	@Test (expected = NullPointerException.class)
	public void testGetKeyAfterNineGuesses()
	{
		Color[] attempt = {Color.RED, Color.GREEN, Color.ORANGE, Color.RED};
		humanPlayer.checkGuess(attempt);
		humanPlayer.checkGuess(attempt);
		humanPlayer.checkGuess(attempt);
		humanPlayer.checkGuess(attempt);
		humanPlayer.checkGuess(attempt);
		humanPlayer.checkGuess(attempt);
		humanPlayer.checkGuess(attempt);
		humanPlayer.checkGuess(attempt);
		humanPlayer.checkGuess(attempt);
		for (Color c : humanPlayer.getKey())
			System.out.print(c.toString() + " ");		
	}
	
	@Test
	public void testGetKeyReturnsNullWhenGameNotWon()
	{
		assertNull(humanPlayer.getKey());
	}
	
	@Test
	public void testGetNumGuessesAfterThreeGuesses()
	{
		humanPlayer.checkGuess(new Color[] {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW});
		humanPlayer.checkGuess(new Color[] {Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE});
		humanPlayer.checkGuess(new Color[] {Color.RED, Color.YELLOW, Color.RED, Color.YELLOW});
		
		assertEquals(3, humanPlayer.getNumGuessesMade());
	}
	
	@Test
	public void testGetGuessesContainsTheGuessesAfterThreeGuesses()
	{
		Color[] guess1 = {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW};
		Color[] guess2 = {Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE};
		Color[] guess3 = {Color.RED, Color.YELLOW, Color.RED, Color.YELLOW};
		
		humanPlayer.checkGuess(guess1);
		humanPlayer.checkGuess(guess2);
		humanPlayer.checkGuess(guess3);
		
		assertArrayEquals(guess1, humanPlayer.getGuesses()[0].getSequence());
		assertArrayEquals(guess2, humanPlayer.getGuesses()[1].getSequence());
		assertArrayEquals(guess3, humanPlayer.getGuesses()[2].getSequence());
	}
	
	@Test
	public void ConstructorSetsGuessAndColorLevelToRightValues()
	{
		assertEquals(GuessLevel.MEDIUM.getNumGuesses(), humanPlayer.getTotalNumTurns());
		assertEquals(ColorLevel.MEDIUM.getNumColors(), humanPlayer.getNumColors());
	}
}
