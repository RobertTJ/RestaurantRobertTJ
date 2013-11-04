package restaurant.test.mock;


import restaurant.CashierAgent.Check;
import restaurant.CustomerAgent;
import restaurant.WaiterAgent.Menu;
import restaurant.gui.CustomerGui;
import restaurant.gui.RestaurantGui;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Host;
import restaurant.interfaces.Waiter;


public class MockHost extends Mock implements Host {

	public MockHost(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgNewWaiter(Waiter w) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgImFree(Waiter w) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgIWantFood(Customer cust) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgLeavingTable(Customer cust, Waiter w) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgIWantABreak(Waiter w) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgBackFromBreak(Waiter w) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgIWontWait(Customer c) {
		// TODO Auto-generated method stub
		
	}

	
}
