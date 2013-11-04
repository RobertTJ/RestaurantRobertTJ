package restaurant.interfaces;

import restaurant.CustomerAgent;
import restaurant.MarketAgent;
import restaurant.WaiterAgent;
import restaurant.CashierAgent.Check;
import restaurant.CookAgent.Order;
import restaurant.WaiterAgent.AgentState;
import restaurant.WaiterAgent.CustState;
import restaurant.WaiterAgent.Event;
import restaurant.WaiterAgent.MyCustomers;


public interface Cook {
	public abstract void msgAtCenter();
	public abstract void msgAtCounter();
	public abstract void msgAtGrill();
	
	public abstract void msgAtFridge();
	
	public abstract void msgNewOrder(Waiter waiter, int table, String order);
	
	public abstract void msgOrderFullfilled(String food, int amount);
	
	public abstract void msgCanNotFullfillOrder(String food, int amount, int available, Market m);

	
}


