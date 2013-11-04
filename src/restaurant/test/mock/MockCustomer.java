package restaurant.test.mock;


import restaurant.CashierAgent.Check;
import restaurant.CustomerAgent.AgentState;
import restaurant.CustomerAgent;
import restaurant.WaiterAgent.Menu;
import restaurant.gui.CustomerGui;
import restaurant.gui.RestaurantGui;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Host;
import restaurant.interfaces.Waiter;

/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public class MockCustomer extends Mock implements Customer {

	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public Cashier cashier;
	public Host host;
	public Waiter waiter;
	public double wallet;
	public double bill;
	
	public void setWaiter(Waiter w) {
		waiter= w;
	}
	
	public void setHost(Host h) {
		host =h;
	}
	
	public Waiter GetWaiter() {
		return waiter;
		
	}

	public MockCustomer(String name) {
		super(name);

	}
	
	public  CustomerGui getGui(){
		return (new CustomerGui(new CustomerAgent("Mock"), new RestaurantGui()));
	}
	
	public  void gotHungry(){
		
	}
	
	public  void msgRestaurantFull(){
		
	}
	
	public  void msgHereIsYourBill(double k){
		cashier.msgPayingMyBill(this, k);
		wallet=wallet-k;
	}
	
	public  void msgFollowMeToTable(Waiter w, Menu m){
		
	}

	public  void msgAnimationFinishedGoToSeat(){
		
	}
	
	public  void msgAnimationFinishedLeaveRestaurant(){
		
	}
	
	public  void msgHereForNewOrder(){
		
	}
	
	public  void msgHereForOrder(){
		
	}
	
	public  void msgDeliveredFood(){
		
	}
	
	public void goToRestaurant() {
			
		

		if ( bill != 0.00 && wallet > bill) {
			//if money is owed, pay
			cashier.msgPayingMyBill(this, bill);
			wallet = wallet - bill;
			bill=0.00;
			host.msgIWantFood(this);
		}
		else if (bill != 0.00 && wallet < bill) {
			//if money is owed and can't pay
			//print("I owe the restaurant money, guess I have to wait here");
			//start timer/dishes stuff
		}
		else {
			host.msgIWantFood(this);//send our instance, so he can respond to us
		}
				
	}

}
