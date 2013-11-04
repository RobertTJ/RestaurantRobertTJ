package restaurant.interfaces;

import restaurant.CustomerAgent;
import restaurant.CashierAgent.Check;
import restaurant.HostAgent.MyWaiters;
import restaurant.HostAgent.Table;
import restaurant.WaiterAgent.AgentState;
import restaurant.WaiterAgent.CustState;
import restaurant.WaiterAgent.Event;
import restaurant.WaiterAgent.MyCustomers;


public interface Host {
	public abstract void msgNewWaiter(Waiter w);
	
	public abstract void msgImFree(Waiter w);

	public abstract void msgIWantFood(Customer cust);

	public abstract void msgLeavingTable(Customer cust, Waiter w);
	
	public abstract void msgIWantABreak(Waiter w);
	
	public abstract void msgBackFromBreak(Waiter w);
	
	public abstract void msgIWontWait(Customer c);
}


