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
public interface Waiter {
	
	public abstract String getName();
	
	public abstract void msgOutOfHere(Customer c);
	
	public abstract void msgCheckPlease(Customer cust);
	
	public abstract void msgHereIsCheck(Customer cust, Check check);
	
	public abstract void msgCantBreakNow();
	
	public abstract void msgGetNewOrder(Customer cust);

	public abstract void msgNewCustomerToSeat(Customer cust, int table);
	
	public abstract void msgLeavingTable(Customer cust);

	public abstract void msgAtTable() ;
	
	public abstract void msgReadyToOrder(Customer cust);
	
	public abstract void msgOrderFood(Customer cust, String food);
	
	public abstract void msgAtFront();
	
	public abstract void msgAtCook();
	
	public abstract void msgOrderReady(String food, int table);
	
	public abstract void msgIWantToGoOnBreak();
	
	public abstract void msgTakeABreak();
	
	public abstract void msgBreakOver();
}


