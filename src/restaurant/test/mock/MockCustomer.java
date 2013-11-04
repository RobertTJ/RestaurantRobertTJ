package restaurant.test.mock;


import restaurant.CashierAgent.Check;
import restaurant.CustomerAgent;
import restaurant.WaiterAgent.Menu;
import restaurant.gui.CustomerGui;
import restaurant.gui.RestaurantGui;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Customer;
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
	
	public Waiter GetWaiter() {
		return null;
		
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
	
	public  void msgHereIsYourBill(Check k){
		//cashier.msgPayingMyBill(this);
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
/*
	@Override
	public void HereIsYourTotal(double total) {
		log.add(new LoggedEvent("Received HereIsYourTotal from cashier. Total = "+ total));

		if(this.name.toLowerCase().contains("thief")){
			//test the non-normative scenario where the customer has no money if their name contains the string "theif"
			cashier.IAmShort(this, 0);

		}else if (this.name.toLowerCase().contains("rich")){
			//test the non-normative scenario where the customer overpays if their name contains the string "rich"
			cashier.HereIsMyPayment(this, Math.ceil(total));

		}else{
			//test the normative scenario
			cashier.HereIsMyPayment(this, total);
		}
	}

	@Override
	public void HereIsYourChange(double total) {
		log.add(new LoggedEvent("Received HereIsYourChange from cashier. Change = "+ total));
	}

	@Override
	public void YouOweUs(double remaining_cost) {
		log.add(new LoggedEvent("Received YouOweUs from cashier. Debt = "+ remaining_cost));
	}
*/
}
