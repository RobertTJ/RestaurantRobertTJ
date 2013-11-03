package restaurant.interfaces;

import restaurant.WaiterAgent.MyCustomers;
import restaurant.test.mock.EventLog;

/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface Cashier {
	/**
	 * @param total The cost according to the cashier
	 *
	 * Sent by the cashier prompting the customer's money after the customer has approached the cashier.
	 */
	EventLog log = new EventLog();
	
	public abstract void msgFoodBill(Market m, double b);
	
	public abstract void msgCheckPlease(Customer C, int TableNumber, double b);
	
	public abstract void msgPayingMyBill(Customer c, double b);

}