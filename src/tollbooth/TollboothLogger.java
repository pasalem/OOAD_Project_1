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
	public Queue<LogMessage> messageQueue = new LinkedList<LogMessage>();
	
	// Accept a message and add it to the message queue.
	public void accept(LogMessage message) 
	{
		messageQueue.offer(message);
	}

	// Remove and return the next message from the message queue.
	// Return the LogMessage object or null if there is no object in the queue.
	public LogMessage getNextMessage() 
	{
		return messageQueue.poll();
	}

}
