package restaurant.interfaces;

import restaurant.CustomerAgent;
import restaurant.CashierAgent.Check;
import restaurant.WaiterAgent.AgentState;
import restaurant.WaiterAgent.CustState;
import restaurant.WaiterAgent.Event;
import restaurant.WaiterAgent.MyCustomers;

/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface Market {
	public abstract void msgPayingBill (double b);
	
}


