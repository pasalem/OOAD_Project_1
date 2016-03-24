// $codepro.audit.disable methodJavadoc
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

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import tollbooth.gatecontroller.*;

/**
 * Test cases for the Tollbooth, TollGate class.
 * @version Feb 3, 2016
 */
public class TollboothTest
{
	// Creates a TollGate with no Controller.
	@Test
	public void createTollGateWithNoController()
	{
		final SimpleLogger logger = new TollboothLogger();
		assertNotNull(new TollGate(null, logger));
	}
	
	// Creates a TollGate with no Logger.
	@Test
	public void createTollGateWithNoLogger()
	{
		final TestGateController controller = new TestGateController();
		assertNotNull(new TollGate(controller, null));
	}
	
	// Create TollGate with a Controller.
	@Test
	public void createNewTollGateWithAController()
	{
		final SimpleLogger logger = new TollboothLogger();
		assertNotNull(new TollGate(new TestGateController(), logger));
	}
	
	// Create a TollGate that is set to closed.
	@Test
	public void newGateControllerIsClosed() throws TollboothException
	{
		final TestGateController controller = new TestGateController();
		final SimpleLogger logger = new TollboothLogger();
		final TollGate gate = new TollGate(controller, logger);
		assertFalse(gate.isOpen());
	}

	// Create a TollGate that opens after 1 open() message.
	@Test
	public void gateControllerIsOpenAfterOpenMessage() throws TollboothException
	{
		final TestGateController controller = new TestGateController();
		final SimpleLogger logger = new TollboothLogger();
		final TollGate gate = new TollGate(controller, logger);
		gate.open();
		assertTrue(gate.isOpen());
	}
	
	// Create a TollGate that opens after 1 failed open() message.
	@Test
	public void gateOpensAfterOneMalfunction() throws TollboothException
	{
		final TestGateController controller = new TestGateController();
		final SimpleLogger logger = new TollboothLogger();
		final TollGate gate = new TollGate(controller, logger);
		controller.scheduleNFailures(1);
		gate.open();
		LogMessage message = logger.getNextMessage();
		assertEquals("open: malfunction", message.getMessage());
		message = logger.getNextMessage();
		assertEquals("open: successful", message.getMessage());
	}
	
	// Create a TollGate that opens after 2 failed open() messages.
	@Test
	public void gateOpensAfterTwoMalfunctions() throws TollboothException
	{
		final TestGateController controller = new TestGateController();
		final SimpleLogger logger = new TollboothLogger();
		final TollGate gate = new TollGate(controller, logger);
		controller.scheduleNFailures(2);
		gate.open();
		LogMessage message = logger.getNextMessage();
		assertEquals("open: malfunction", message.getMessage());
		message = logger.getNextMessage();
		assertEquals("open: malfunction", message.getMessage());
		message = logger.getNextMessage();
		assertEquals("open: successful", message.getMessage());
	}
	
	// Create a TollGate that becomes unrecoverable after 3 failed close() messages.
	@Test
	public void unrecoverableCloseMalfunctionAfterThreeMalfunctions() throws TollboothException
	{
		final TestGateController controller = new TestGateController();
		final SimpleLogger logger = new TollboothLogger();
		final TollGate gate = new TollGate(controller, logger);
		controller.scheduleNFailures(3);
		controller.setIsOpen(true);
		gate.close();
		LogMessage message = logger.getNextMessage();
		assertEquals("close: malfunction", message.getMessage());
		message = logger.getNextMessage();
		assertEquals("close: malfunction", message.getMessage());
		message = logger.getNextMessage();
		assertEquals("close: unrecoverable malfunction", message.getMessage());
	}
	
	// Create a TollGate that becomes unrecoverable after 3 failed open() messages.
	@Test
	public void unrecoverableOpenMalfunctionAfterThreeFailures() throws TollboothException
	{
		final TestGateController controller = new TestGateController();
		final TollboothLogger logger = new TollboothLogger();
		final TollGate gate = new TollGate(controller, logger);
		LogMessage message = logger.getNextMessage();
		assertEquals(null, message);
		controller.scheduleNFailures(3);
		gate.open();
		message = logger.getNextMessage();
		assertEquals("open: malfunction", message.getMessage());
		message = logger.getNextMessage();
		assertEquals("open: malfunction", message.getMessage());
		message = logger.getNextMessage();
		assertEquals("open: unrecoverable malfunction", message.getMessage());
	}
	
	// Tests that the message log has 3 messages in it after the TollGate fails to open() 3 times.
	@Test
	public void testLogAfterThreeFailedOpens() throws TollboothException
	{
		final TestGateController controller = new TestGateController();
		final TollboothLogger logger = new TollboothLogger();
		final TollGate gate = new TollGate(controller, logger);
		assertEquals(0, logger.logSize());
		controller.scheduleNFailures(3);
		gate.open();
		assertEquals(3, logger.logSize());
	}
	
	// Tests that the message log has 3 messages in it after the TollGate fails to close() 3 times.
	@Test
	public void testLogAfterWithThreeFailedCloses() throws TollboothException
	{
		final TestGateController controller = new TestGateController();
		final TollboothLogger logger = new TollboothLogger();
		final TollGate gate = new TollGate(controller, logger);
		assertEquals(0, logger.logSize());
		controller.scheduleNFailures(3);
		controller.setIsOpen(true);
		gate.close();
		assertEquals(3, logger.logSize());
	}
	
	// Tests that the message log has 4 messages in it after the TollGate fails to close() 4 times.
	@Test
	public void testLogSizeOnFourFailedCloses() throws TollboothException
	{
		final TestGateController controller = new TestGateController();
		final TollboothLogger logger = new TollboothLogger();
		final TollGate gate = new TollGate(controller, logger);
		assertEquals(0, logger.logSize());
		controller.scheduleNFailures(4);
		controller.setIsOpen(true);
		gate.close();
		controller.setIsOpen(true);
		try
		{
			gate.close();
		}
		catch(TollboothException e)
		{
			assertEquals(4, logger.logSize());
		}
	}
	
	// Tests that if the TollGate is already set to open, 
	// an open() message will not be accepted or logged.
	@Test
	public void gateAlreadyOpenNoMessage() throws TollboothException
	{
		final TestGateController controller = new TestGateController();
		final SimpleLogger logger = new TollboothLogger();
		final TollGate gate = new TollGate(controller, logger);
		controller.setIsOpen(true);
		gate.open();
		LogMessage message = logger.getNextMessage();
		assertEquals(message, null);
	}
	
	// Tests that if the TollGate is already set to closed, 
	// an close() message will not be accepted or logged.
	@Test
	public void gateAlreadyClosedNoMessage() throws TollboothException
	{
		final TestGateController controller = new TestGateController();
		final SimpleLogger logger = new TollboothLogger();
		final TollGate gate = new TollGate(controller, logger);
		controller.setIsOpen(false);
		gate.close();
		LogMessage message = logger.getNextMessage();
		assertEquals(null, message);
	}
	
	// Tests that the TollGate is opened twice, by setting it to closed, 
	// opening it, and then closing and opening it again. 
	// Also makes sure that 2 opens are logged.
	@Test
	public void gateOpenedTwice() throws TollboothException
	{
		final TestGateController controller = new TestGateController();
		final SimpleLogger logger = new TollboothLogger();
		final TollGate gate = new TollGate(controller, logger);
		assertEquals(0, gate.getNumberOfOpens());
		controller.setIsOpen(false);
		gate.open();
		controller.setIsOpen(false);
		gate.open();
		assertEquals(2, gate.getNumberOfOpens());
	}
	
	// Test that the TollGate is closed, then opened, 
	// then 2 failures occur, then the gate is opened, 
	// then 4 failures occur, and then the final attempted 
	// open does not go through because the gate is now malfunctioning.
	@Test
	public void gateOpenedTwiceWithMalfunctionInBetween() throws TollboothException
	{
		final TestGateController controller = new TestGateController();
		final SimpleLogger logger = new TollboothLogger();
		final TollGate gate = new TollGate(controller, logger);
		assertEquals(0, gate.getNumberOfCloses());
		assertEquals(0, gate.getNumberOfOpens());
		controller.scheduleNFailures(1);
		controller.setIsOpen(false);
		gate.open();
		controller.setIsOpen(false);
		controller.scheduleNFailures(2);
		gate.open();
		controller.setIsOpen(false);
		controller.scheduleNFailures(4);
		gate.open();
		assertEquals(0, gate.getNumberOfCloses());
		assertEquals(2, gate.getNumberOfOpens());
	}
	
	// Test that the TollGate is opened, then closed, 
	// then 2 failures occur, then the gate is closed, 
	// then 4 failures occur, and then the final attempted 
	// close does not go through because the gate is now malfunctioning.
	@Test
	public void gateClosedTwiceWithMalfunctionInBetween() throws TollboothException
	{
		final TestGateController controller = new TestGateController();
		final SimpleLogger logger = new TollboothLogger();
		final TollGate gate = new TollGate(controller, logger);
		assertEquals(0, gate.getNumberOfOpens());
		assertEquals(0, gate.getNumberOfCloses());
		controller.scheduleNFailures(1);
		controller.setIsOpen(true);
		gate.close();
		controller.setIsOpen(true);
		controller.scheduleNFailures(2);
		gate.close();
		controller.setIsOpen(true);
		controller.scheduleNFailures(4);
		gate.close();
		assertEquals(0, gate.getNumberOfOpens());
		assertEquals(2, gate.getNumberOfCloses());
	}
	
	// Test that the TollGate is opened, then reset to closed, 
	// then 2 failures occur, then the gate is reset, 
	// then 4 failures occur, and then the final attempted reset 
	// does not go through because the gate is now malfunctioning.
	@Test
	public void gateResetTwiceWithMalfunctionInBetween() throws TollboothException
	{
		final TestGateController controller = new TestGateController();
		final SimpleLogger logger = new TollboothLogger();
		final TollGate gate = new TollGate(controller, logger);
		assertEquals(false, gate.isMalfunctioningMode());
		controller.scheduleNFailures(1);
		controller.setIsOpen(true);
		gate.reset();
		assertEquals(false, gate.isOpen());
		controller.setIsOpen(true);
		controller.scheduleNFailures(2);
		gate.reset();
		assertEquals(false, gate.isOpen());
		controller.setIsOpen(true);
		controller.scheduleNFailures(4);
		gate.reset();
		assertEquals(true, gate.isMalfunctioningMode());
	}
	
	// Test that reset() sets TollGate back to default from open state and that reset() is logged.
	@Test
	public void gateResetFromOpenState() throws TollboothException
	{
		final TestGateController controller = new TestGateController();
		final SimpleLogger logger = new TollboothLogger();
		final TollGate gate = new TollGate(controller, logger);
		assertEquals(false, gate.isMalfunctioningMode());
		controller.setIsOpen(true);
		assertEquals(0, gate.getNumberOfCloses());
		gate.reset();
		assertEquals(false, gate.isOpen());
		LogMessage message = logger.getNextMessage();
		assertEquals("reset: successful", message.getMessage());
		assertEquals(0, gate.getNumberOfCloses());
	}
	
	// Test that reset() sets TollGate back to default from closed state and that reset() is logged.
	@Test
	public void gateResetFromClosedState() throws TollboothException
	{
		final TestGateController controller = new TestGateController();
		final SimpleLogger logger = new TollboothLogger();
		final TollGate gate = new TollGate(controller, logger);
		assertEquals(false, gate.isMalfunctioningMode());
		controller.setIsOpen(false);
		assertEquals(0, gate.getNumberOfCloses());
		gate.reset();
		assertEquals(false, gate.isOpen());
		LogMessage message = logger.getNextMessage();
		assertEquals("reset: successful", message.getMessage());
		assertEquals(0, gate.getNumberOfCloses());
	}
	
	// Test that reset() set TollGate back to default from 
	// a malfunctioning state and that the reset() is logged.
	@Test
	public void gateResetFromMalfunctioningModeWhenOpen() throws TollboothException
	{
		final TestGateController controller = new TestGateController();
		final SimpleLogger logger = new TollboothLogger();
		final TollGate gate = new TollGate(controller, logger);
		LogMessage msg;
		controller.setIsOpen(true);
		controller.scheduleNFailures(3);
		assertEquals(0, gate.getNumberOfCloses());
		gate.close();
		gate.reset();
		assertEquals(false, gate.isMalfunctioningMode());
		for(int i = 0; i < 3; i++)
		{
			msg = logger.getNextMessage();
		}
		msg = logger.getNextMessage();
		assertEquals("reset: successful", msg.getMessage());
		assertEquals(0, gate.getNumberOfCloses());
	}
	
	// Tests that an TollboothException is thrown when an isOpen() 
	// request is called on a malfunctioning TollGate.
	@Test(expected=tollbooth.TollboothException.class)
	public void gateIsOpenMalfunctioningException() throws TollboothException
	{
		final TestGateController controller = new TestGateController();
		final SimpleLogger logger = new TollboothLogger();
		final TollGate gate = new TollGate(controller, logger);
		controller.scheduleNFailures(3);
		gate.open();
		gate.isOpen();
	}
	
	// Tests that exception message "open: will not respond" is sent when an open() 
	// request is sent to a malfunctioning TollGate.
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	@Test
	public void openWillNotRespondMessageOnFourthOpenMalfunction() throws TollboothException
	{
		final TestGateController controller = new TestGateController();
		final SimpleLogger logger = new TollboothLogger();
		final TollGate gate = new TollGate(controller, logger);
		controller.scheduleNFailures(4);
		gate.open();
		for(int i = 0; i < 3; i++)
		{
			logger.getNextMessage();
		}
		expectedEx.expect(tollbooth.TollboothException.class);
		expectedEx.expectMessage("open: will not respond");
		gate.open();
	}
	
	// Tests that the logger records the cause of a TollboothException.
	@Test
	public void loggerHasCauseFromThrownException() throws TollboothException
	{
		final TestGateController controller = new TestGateController();
		final SimpleLogger logger = new TollboothLogger();
		final TollGate gate = new TollGate(controller, logger);
		controller.scheduleNFailures(3);
		gate.open();
		for(int i = 0; i < 3; i++)
		{
			logger.getNextMessage();
		}
		try
		{
			gate.open();
		}
		catch(TollboothException e)
		{
			LogMessage msg = logger.getNextMessage();
			assertEquals(true, msg.hasCause());
			assertEquals("open: will not respond", msg.getCause().getMessage());
		}
	}
	
	// Tests that the logger does not have a cause from a failed request that is not an exception.
	@Test
	public void loggerHasNoCauseFromNormalFailure() throws TollboothException
	{
		final TestGateController controller = new TestGateController();
		final SimpleLogger logger = new TollboothLogger();
		final TollGate gate = new TollGate(controller, logger);
		controller.scheduleNFailures(1);
		gate.open();
		LogMessage msg = logger.getNextMessage();
		assertEquals(false, msg.hasCause());
	}
}
	
