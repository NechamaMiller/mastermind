package exceptions;

public class CantGenerateGuessException extends RuntimeException
{
	public CantGenerateGuessException()
	{
		super("Can't generate guess");
	}
	
	public CantGenerateGuessException(String message)
	{
		super(message);
	}
}
