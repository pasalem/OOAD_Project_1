/**
 * 
 */
package tollbooth;

import java.util.LinkedList;
import java.util.Queue;
/**
 * @author Peter
 *
 */
public class TollboothLogger implements SimpleLogger 
{
	// Creating the LogMessage queue messageQueue.
	private Queue<LogMessage> messageQueue;
	
	/**
	 * Constructor for TollboothLogger.
	 */
	public TollboothLogger()
	{
		messageQueue = new LinkedList<LogMessage>();
	}
	
	// Accept a message and add it to the message queue.
	public void accept(LogMessage message) 
	{
		messageQueue.add(message);
	}

	// Remove and return the next message from the message queue.
	// Return the LogMessage object or null if there is no object in the queue.
	public LogMessage getNextMessage() 
	{
		if(messageQueue.isEmpty())
		{
			return null;
		} 
		else
		{
			return messageQueue.poll();
		}
	}

	
	/**
	 * Method logSize.
	 * @return the size of the LogMessage queue.
	 */
	public int logSize()
	{
		return messageQueue.size();
	}
}
