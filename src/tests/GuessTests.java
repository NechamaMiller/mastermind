package tests;

import static org.junit.Assert.*;
import org.junit.*;
import org.junit.rules.*;

import mastermind.*;
import exceptions.*;

public class GuessTests 
{
	private Color[] sequence = {Color.BLUE,Color.GREEN, Color.YELLOW, Color.RED};
	private Guess guess = new Guess(1,2,sequence);
	
	@Rule
	public final ExpectedException exception = ExpectedException.none();
	
	@Test
	public void constructorThrowsExceptionWhenNumRedsPlusNumWhitesMoreThanFour()
	{
		exception.expect(InvalidDataException.class);
	    Guess guess = new Guess(1,4,sequence);
	}
	
	@Test
	public void constructorThrowsExceptionWhenNumRedsLessThanZero()
	{
		exception.expect(InvalidDataException.class);
		Guess guess = new Guess(-1,0,sequence);
	}
	
	@Test
	public void constructorThrowsExceptionWhenNumRedsMoreThanFour()
	{
		exception.expect(InvalidDataException.class);
		Guess guess = new Guess(5,0,sequence);
	}
	
	@Test
	public void constructorThrowsExceptionWhenNumWhitesLessThanZero()
	{
		exception.expect(InvalidDataException.class);
		Guess guess = new Guess(0,-1,sequence);
	}
	
	@Test
	public void constructorThrowsExceptionWhenNumWhitesMoreThanFour()
	{
		exception.expect(InvalidDataException.class);
		Guess guess = new Guess(0,5,sequence);
	}
	
	@Test
	public void constructorThrowsExceptionWhenSequenceNull()
	{
		exception.expect(InvalidDataException.class);
		Guess guess = new Guess(1,2,null);
	}
	
	@Test
	public void GetSequenceReturnsArrayCorrectly()
	{
		//I did assertTrue instead of assertEquals because assertEquals is deprecated for comparing arrays
		assertTrue(sequence[0].equals(guess.getSequence()[0]));
		assertTrue(sequence[1].equals(guess.getSequence()[1]));
		assertTrue(sequence[2].equals(guess.getSequence()[2]));
		assertTrue(sequence[3].equals(guess.getSequence()[3]));
	}
	
	@Test
	public void ConstructorMakesDeepCopyOfSequenceArray()
	{
		Color[] sequence = {Color.BLUE,Color.BLUE,Color.BLUE,Color.BLUE};
		Guess guess = new Guess(1,2,sequence);
		sequence[0] = Color.GREEN;
		assertFalse(guess.getSequence()[0].equals(sequence[0]));
	}
	
	@Test
	public void GetSequenceReturnsDeepCopyOfSequenceArray()
	{
		Color[] sequence = guess.getSequence();
		sequence[0] = Color.WHITE;
		assertFalse(sequence[0].equals(guess.getSequence()[0]));
	}
}
