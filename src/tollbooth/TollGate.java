/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Copyright ©2016 Gary F. Pollice
 *******************************************************************************/

package tollbooth;

import tollbooth.gatecontroller.GateController;

/**
 * The TollGate contains everything about a tollgate in a tollbooth.
 * @version Feb 3, 2016
 */
public class TollGate
{
	private final GateController controller;
	private final SimpleLogger logger;
	private int openCount;
	private int closeCount;
	private final int maxFailures;
	private boolean malfunctioningMode;
	
	/**
	 * Constructor that takes the gate controller and the logger.
	 * @param controller the GateController object.
	 * @param logger the SimpleLogger object.
	 */
	public TollGate(GateController controller, SimpleLogger logger) {
		this.controller = controller;
		this.logger = logger;
		openCount = 0;
		closeCount = 0;
		maxFailures = 3;
		malfunctioningMode = false;
	}
	
	// Enum of all possible requests to a TollGate state: open(), close(), and reset()
	public enum Request
	{
		OPEN, CLOSE, RESET
	}
	
	/**
	 * Open the gate.
	 * @throws TollboothException
	 */
	public void open() throws TollboothException
	{
		if(controller.isOpen()){
			return;
		}
		this.requestDispatch(controller, Request.OPEN);
			
	}
	
	/**
	 * Close the gate.
	 * @throws TollboothException
	 */
	public void close() throws TollboothException
	{
		if(!controller.isOpen()){
			return;
		}
		this.requestDispatch(controller, Request.CLOSE);
	}
	
	/**
	 * Reset the gate to the state it was in when created with the exception of the
	 * statistics.
	 * @throws TollboothException
	 */
	public void reset() throws TollboothException
	{
		this.requestDispatch(controller, Request.RESET);
	}
	
	/**
	 * Runs a method from the controller API based on the Request passed to it.
	 * @param action an enum specifying which Request to take
	 * @throws TollboothException
	 */
	public void takeControllerRequest(Request action) throws TollboothException
	{
		switch(action)
		{
		case OPEN:
			controller.open();
			//Increment if no exception is thrown
			openCount++;
			break;
		case CLOSE:
			controller.close();
			//Increment if no exception is thrown
			closeCount++;
			break;
		case RESET:
			controller.reset();
			//Set malfunctioning mode back to false
			malfunctioningMode = false;
			break;
		}
	}

	/**
	 * Attempts to run the specified Request up to maxFailure times.
	 * This method also produces log messages detailing the status 
	 * of the Requests and whether or not they executed successfully.
	 * @param controller - the GateController object
	 * @param action - the Request to perform on the controller
	 * @throws TollboothException
	 */
	public void requestDispatch(GateController controller, Request action) throws TollboothException
	{
		String logMessage;
		String requestName = getRequestName(action);
		
		//Attempt to close until success or until maxFailures attempts
		for(int attempt = 1; attempt <= maxFailures; attempt++)
		{
			try
			{
				//if in unresponsive mode, throw exception unless Request is reset
				if(malfunctioningMode && action != Request.RESET)
				{
					logMessage = String.format("%s: will not respond", requestName);
					throw new TollboothException(logMessage);
				}
				//make a call to the gate controller API
				this.takeControllerRequest(action);
				//Log successful Request taken
				logMessage = String.format("%s: successful", requestName);
				logger.accept(new LogMessage(logMessage));
				return;
			} 
			catch(TollboothException e) 
			{
				//Throw the exception if in unresponsive mode
				if(malfunctioningMode)
				{
					logger.accept(new LogMessage(e.getMessage(), e));
					throw e;
				}
				//If this is the third try, set unrecoverable mode
				if(attempt == maxFailures)
				{
					logMessage = String.format("%s: unrecoverable malfunction", requestName);
					logger.accept(new LogMessage(logMessage));
					malfunctioningMode = true;
				//Otherwise just log another malfunction
				} 
				else
				{
					logMessage = String.format("%s: malfunction", requestName);
					logger.accept(new LogMessage(logMessage));
				}
			}
		}
	}
	
	
	/**
	 * Given an Request enum type, returns the name of the Request. 
	 * Used for generating detailed logging messages
	 * @param action - the Request to perform
	 * @return the name of the Request
	 */
	public static String getRequestName(Request action){
		String RequestName = "Unknown";
		switch(action){
			case OPEN:
				RequestName = "open";
				break;
			case CLOSE:
				RequestName = "close";
				break;
			case RESET:
				RequestName = "reset";
				break;
		}
		return RequestName;
	}
	
	/**
	 * @return true if the gate is open
	 * @throws TollboothException 
	 */
	public boolean isOpen() throws TollboothException
	{
		if(malfunctioningMode)
		{
			throw new TollboothException("Gate is in unresponsive mode");
		} else
		{
			return controller.isOpen();
		}
	}
	
	/**
	 * @return the number of times that the gate has been opened (that is, the
	 *  open method has successfully been executed) since the object was created.
	 */
	public int getNumberOfOpens()
	{
		return openCount;
	}
	
	
	/**
	 * @return the number of times that the gate has been closed (that is, the
	 *  close method has successfully been executed) since the object was created.
	 */
	public int getNumberOfCloses()
	{
		return closeCount;
	}
	
	/**
	 * Gets whether or not the gate is in functioning mode
	 * @return true if malfunctioning, false otherwise
	 */
	public boolean isMalfunctioningMode()
	{
		return malfunctioningMode;
	}
}
