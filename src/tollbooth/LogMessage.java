/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design. The course was
 * taken at Worcester Polytechnic Institute. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License
 * v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Copyright ©2016 Gary F. Pollice
 *******************************************************************************/

 
package tollbooth;

/**
 * The LogMessage is a simple data structure that contains information about any system
 * malfunction.
 * 
 * @version Feb 18, 2016
 */
public class LogMessage
{
	private final String message;
	private final Throwable cause;

	/**
	 * Create a LogMessage object with a message and no cause.
	 * @param message - the descriptive message
	 */
	public LogMessage(String message)
	{
		this(message, null);
	}

	/**
	 * Create a LogMessage object with a message and a cause.
	 * @param message - the descriptive message in the log
	 * @param cause - the Throwable object, an Exception that was the cause for the message
	 */
	public LogMessage(String message, Throwable cause)
	{
		this.message = message;
		this.cause = cause;
	}
	
	/**
	 * Checks if the LogMessage has a cause.
	 * @return true if the LogMessage has a cause
	 */
	public boolean hasCause()
	{
		return cause != null;
	}

	/**
	 * Gets the message from the log.
	 * @return the message
	 */
	public String getMessage()
	{
		return message;
	}

	/**
	 * Gets the cause of the thrown exception.
	 * @return the cause
	 */
	public Throwable getCause()
	{
		return cause;
	}
}
