package restaurant.test.mock;


import restaurant.CashierAgent.Check;
import restaurant.CustomerAgent;
import restaurant.WaiterAgent.Menu;
import restaurant.gui.CustomerGui;
import restaurant.gui.RestaurantGui;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Cook;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Market;
import restaurant.interfaces.Waiter;


public class MockCook extends Mock implements Cook {

	public MockCook(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgAtCounter() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtGrill() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtFridge() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgNewOrder(Waiter waiter, int table, String order) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOrderFullfilled(String food, int amount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgCanNotFullfillOrder(String food, int amount, int available,
			Market m) {
		// TODO Auto-generated method stub
		
	}



}
