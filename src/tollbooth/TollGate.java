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
	
	/**
	 * Constructor that takes the actual gate controller and the logger.
	 * @param controller the GateController object.
	 * @param logger the SimpleLogger object.
	 */
	public TollGate(GateController controller, SimpleLogger logger) {
		this.controller = controller;
		this.logger = logger;
	}
	
	/**
	 * Open the gate.
	 * @throws TollboothException
	 */
	public void open() throws TollboothException
	{
		controller.open();
		
		//If there is a malfunction
		if (malfunction)
			// Log the malfunction
			TollboothLogger.accept();
			// Retry N times to open
			controller.open();
			controller.open();
			controller.open();
		// If still malfunctioning, set the gate to refuse open/close requests
		if still not responding
			set gateStatus to locked
		// If the gate is set to not accept requests, log it and do nothing
		if gateStatus == locked;
			TollboothLogger.accept();
		// Throw a TollboothException after logging if the gate can't be opened	
		throw TollboothException;	
			
	}
	
	/**
	 * Close the gate
	 * @throws TollboothException
	 */
	public void close() throws TollboothException
	{
		controller.close();
		
		//If there is a malfunction
		if (malfunction)
			// Log the malfunction
			TollboothLogger.accept();
			// Retry N times to close
			controller.open();
			controller.open();
			controller.open();
		// If still malfunctioning, set the gate to refuse open/close requests	
		if still not responding
				set gateStatus to locked
		// If the gate is set to not accept requests, log it and do nothing
		if gateStatus == locked;
		TollboothLogger.accept();
		// Throw a TollboothException after logging if the gate can't be closed
		throw TollboothException	
	}
	
	/**
	 * Reset the gate to the state it was in when created with the exception of the
	 * statistics.
	 * @throws TollboothException
	 */
	public void reset() throws TollboothException
	{
		gateState = closed
		// The statistics of the gate are not affected
		do not affect stats
	}
	
	/**
	 * @return true if the gate is open
	 * @throws TollboothException 
	 */
	public boolean isOpen() throws TollboothException
	{
		return controller.isOpen();
	}
	
	/**
	 * @return the number of times that the gate has been opened (that is, the
	 *  open method has successfully been executed) since the object was created.
	 */
	public int getNumberOfOpens()
	{
		return numOfOpens;
	}
	
	
	/**
	 * @return the number of times that the gate has been closed (that is, the
	 *  close method has successfully been executed) since the object was created.
	 */
	public int getNumberOfCloses()
	{
		return numOfCloses;
	}
}
