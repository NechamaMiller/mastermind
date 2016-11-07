package tests;

import static org.junit.Assert.*;
import org.junit.*;
import mastermind.*;
import exceptions.*;

public class GuessTests 
{
	private static Color[] sequence = {Color.BLUE,Color.GREEN, Color.YELLOW, Color.RED};
	private static Guess guess = new Guess(1,2,sequence);
	
	@Test(expected = InvalidDataException.class)
	public void constructorThrowsExceptionWhenNumRedsPlusNumWhitesMoreThanFour()
	{
	    Guess guess = new Guess(1,4,sequence);
	}
	
	@Test(expected = InvalidDataException.class)
	public void constructorThrowsExceptionWhenNumRedsLessThanZero()
	{
		Guess guess = new Guess(-1,0,sequence);
	}
	
	@Test(expected = InvalidDataException.class)
	public void constructorThrowsExceptionWhenNumRedsMoreThanFour()
	{
		Guess guess = new Guess(5,0,sequence);
	}
	
	@Test(expected = InvalidDataException.class)
	public void constructorThrowsExceptionWhenNumWhitesLessThanZero()
	{
		Guess guess = new Guess(0,-1,sequence);
	}
	
	@Test(expected = InvalidDataException.class)
	public void constructorThrowsExceptionWhenNumWhitesMoreThanFour()
	{
		Guess guess = new Guess(0,5,sequence);
	}
	
	@Test(expected = InvalidDataException.class)
	public void constructorThrowsExceptionWhenSequenceIsNull()
	{
		Guess guess = new Guess(1,2,null);
	}
	
	@Test
	public void getSequenceReturnsArrayCorrectly()
	{
		assertArrayEquals(sequence, guess.getSequence());
	}
	
	//I can't use assertNotSame for the next 2 tests because then I won't know if a deep copy was done in constructor, in returning method, or in both
	@Test
	public void constructorMakesDeepCopyOfSequenceArray()
	{
		Color[] sequence = {Color.BLUE,Color.BLUE,Color.BLUE,Color.BLUE};
		Guess guess = new Guess(1,2,sequence);
		sequence[0] = Color.GREEN;
		assertFalse(guess.getSequence()[0].equals(sequence[0]));
	}
	
	@Test
	public void getSequenceReturnsDeepCopyOfSequenceArray()
	{
		Color[] sequence = guess.getSequence();
		sequence[0] = Color.WHITE;
		assertFalse(sequence[0].equals(guess.getSequence()[0]));
	}
	
	//This makes sure that the toString() works
	public static void main(String[] args)
	{
		System.out.println(guess);
	}
}
