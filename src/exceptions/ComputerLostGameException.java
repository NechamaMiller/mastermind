package exceptions;

public class ComputerLostGameException extends RuntimeException
{
	public ComputerLostGameException()
	{
		super("Computer lost game.");
	}
	
	public ComputerLostGameException(String message)
	{
		super(message);
	}
}
