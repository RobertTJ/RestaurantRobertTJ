package restaurant.test.mock;


import restaurant.CustomerAgent;
import restaurant.CashierAgent.Check;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Waiter;

/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public class MockWaiter extends Mock implements Waiter {

	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public Cashier cashier;
	
	public Customer customer;

	public MockWaiter(String name) {
		super(name);

	}
	public  void msgOutOfHere(Customer c){
	
	}
	
	public  void msgCheckPlease(Customer cust){
		cashier.msgCheckPlease(customer, 0, 6.99);
	}
	
	public  void msgHereIsCheck(Customer cust, Check check){
		customer.msgHereIsYourBill(check);
	}
	
	public  void msgCantBreakNow(){
		
	}
	
	public  void msgGetNewOrder(Customer cust){
		
	}

	public  void msgNewCustomerToSeat(Customer cust, int table){
		
	}
	
	public  void msgLeavingTable(Customer cust){
		
	}

	public  void msgAtTable(){
		
	}
	
	public  void msgReadyToOrder(Customer cust){
		
	}
	
	public  void msgOrderFood(Customer cust, String food){
		
	}
	
	public  void msgAtFront(){
		
	}
	
	public  void msgAtCook(){
		
	}
	
	public  void msgOrderReady(String food, int table){
		
	}
	
	public  void msgIWantToGoOnBreak(){
		
	}
	
	public  void msgTakeABreak(){
		
	}
	
	public  void msgBreakOver(){
		
	}
}
