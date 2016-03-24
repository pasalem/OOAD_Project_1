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

package tollbooth.gatecontroller;

import tollbooth.TollboothException;

/**
 * Description
 * @version Feb 15, 2016
 */
public class TestGateController implements GateController
{
	private boolean isOpen;
	private int scheduledFailureNum;
	
	/**
	 * Constructor for the TestGateController.
	 */
	public TestGateController()
	{
		isOpen = false;
		scheduledFailureNum = 0;
	}
	
	/*
	 * @see tollbooth.gatecontroller.GateController#open()
	 */
	@Override
	public void open() throws TollboothException
	{
		if(scheduledFailureNum > 0){
			scheduledFailureNum--;
			throw new TollboothException("Failed to open");
		}
		isOpen = true;
	}

	/*
	 * @see tollbooth.gatecontroller.GateController#close()
	 */
	@Override
	public void close() throws TollboothException
	{
		if(scheduledFailureNum > 0){
			scheduledFailureNum--;
			throw new TollboothException("Failed to close");
		}
		isOpen = false;
	}

	/*
	 * @see tollbooth.gatecontroller.GateController#reset()
	 */
	@Override
	public void reset() throws TollboothException
	{
		if(scheduledFailureNum > 0)
		{
			scheduledFailureNum--;
			throw new TollboothException("Failed to reset");
		}
		isOpen = false;
	}

	/*
	 * @see tollbooth.gatecontroller.GateController#isOpen()
	 */
	@Override
	public boolean isOpen() throws TollboothException
	{
		return isOpen;
	}

	/**
	 * Used for tests. It allows the value of isOpen to be set.
	 * @param value to set for isOpen
	 */
	public void setIsOpen(boolean value)
	{
		isOpen = value;
	}
	
	/**
	 * Schedules the next N actions to cause TollboothExceptions.
	 * @param count the number of failures to schedule
	 */
	public void scheduleNFailures(int count)
	{
		scheduledFailureNum = count;
	}
}
