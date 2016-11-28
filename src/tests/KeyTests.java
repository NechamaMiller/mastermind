package tests;

import static org.junit.Assert.*;
import org.junit.*;
import mastermind.*;
import exceptions.*;

public class KeyTests
{
	@Test
	public void testKeyCreatedByConstructorMatchesArrayRetrievedByGetKey()
	{
		Key k = new Key(4);
		assertTrue(k.equals(k.getKey()));
	}
	
	@Test (expected = InvalidDataException.class)
	public void testKeyCreatedWithNegativeSizeThrowsException()
	{
		Key k = new Key(-5);
	}
	
	@Test
	public void testTwoKeyObjectsOfDifferentLengthsAreNotTheSame()
	{
		Key key1 = new Key(4);
		Key key2 = new Key(5);
		assertFalse(key1.equals(key2));
		assertFalse(key1.getKey().equals(key2.getKey()));
	}
	
	@Test
	public void testKeyEqualsMethodReturnsFalseWhenPassedNull()
	{
		Key k = new Key(4);
		assertFalse(k.equals(null));
	}
	
	@Test
	public void testKeyEqualsMethodReturnsTrueWhenArraysAreEqual()
	{
		Key k = new Key(4);
		Color[] array = k.getKey();
		assertTrue(k.equals(array));
	}
}
