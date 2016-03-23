/**
 * 
 */
package tollbooth.gatecontroller;

import tollbooth.TollboothException;
import wpi.hw.xyzco.GateControllerException;

/**
 * @author Peter
 *
 */
public class MalfunctioningGate implements GateController
{
	private final wpi.hw.xyzco.GateController brokenController;
	
	/**
	 * Constructor for the XYZ Co. gate controller adapter.
	 * @param controller an XYZ Co. gate controller.
	 */
	public MalfunctioningGate(wpi.hw.xyzco.GateController brokenController)
	{
		this.brokenController = brokenController;
	}
	
	/*
	 * @see tollbooth.gatecontroller.GateController#open()
	 */
	@Override
	public void open() throws TollboothException
	{
		try {
			brokenController.open();
		} catch (GateControllerException e) {
			throw new TollboothException("Hardware threw exception on open", e);
		}
	}

	/*
	 * @see tollbooth.gatecontroller.GateController#close()
	 */
	@Override
	public void close() throws TollboothException
	{
		try {
			brokenController.close();
		} catch (GateControllerException e) {
			throw new TollboothException("Hardware threw exception on close", e);
		}
	}

	/*
	 * @see tollbooth.gatecontroller.GateController#reset()
	 */
	@Override
	public void reset() throws TollboothException
	{
		try {
			brokenController.reset();
		} catch (GateControllerException e) {
			throw new TollboothException("Hardware threw exception on reset", e);
		}
	}

	/*
	 * @see tollbooth.gatecontroller.GateController#isOpen()
	 */
	@Override
	public boolean isOpen() throws TollboothException
	{
		try {
			return brokenController.isOpen();
		} catch (GateControllerException e) {
			throw new TollboothException("Hardware threw exception on isOpen", e);
		}
	}

}
