package restaurant.test.mock;


import restaurant.CashierAgent.Check;
import restaurant.CustomerAgent;
import restaurant.WaiterAgent.Menu;
import restaurant.gui.CustomerGui;
import restaurant.gui.RestaurantGui;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Market;
import restaurant.interfaces.Waiter;

/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public class MockMarket extends Mock implements Market {

	public MockMarket(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgPayingBill(double b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgNewOrders(String type, int i) {
		// TODO Auto-generated method stub
		
	}

	
}
