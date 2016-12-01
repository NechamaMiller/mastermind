package tests;

import static org.junit.Assert.*;
import org.junit.*;

import enums.Color;
import mastermind.*;
import exceptions.*;

public class KeyTests
{
	@Test
	public void testKeyCreatedByConstructorMatchesArrayRetrievedByGetKey()
	{
		Key k = new Key(4,6);
		assertTrue(k.equals(k.getKey()));
	}
	
	@Test (expected = InvalidDataException.class)
	public void testKeyCreatedWithNegativeSizeThrowsException()
	{
		Key k = new Key(-5,6);
	}
	
	@Test
	public void testTwoKeyObjectsOfDifferentLengthsAreNotTheSame()
	{
		Key key1 = new Key(4,6);
		Key key2 = new Key(5,6);
		assertFalse(key1.equals(key2));
		assertFalse(key1.getKey().equals(key2.getKey()));
	}
	
	@Test
	public void testKeyEqualsMethodReturnsFalseWhenPassedNull()
	{
		Key k = new Key(4,6);
		assertFalse(k.equals(null));
	}
	
	@Test
	public void testKeyEqualsMethodReturnsTrueWhenArraysAreEqual()
	{
		Key k = new Key(4,6);
		Color[] array = k.getKey();
		assertTrue(k.equals(array));
	}
	
	@Test
	public void keyOnlyConsistsOfRightNumberOfColorsWithOneColor()
	{
		Key k = new Key(4,1);
		Color[] array = k.getKey();

		for (int i=0; i<array.length; i++)
		{
			assertEquals(array[i], Color.values()[0]);
		}
	}
	
	@Test
	public void keyOnlyConsistsOfRightNumberOfColorsWithManyColors()
	{
		Key k = new Key(4,2);
		Color[] array = k.getKey();
		
		for (int i=0; i<array.length; i++)
		{
			assertTrue(array[i].equals(Color.values()[0]) || array[i].equals(Color.values()[1]));
		}
	}
}
