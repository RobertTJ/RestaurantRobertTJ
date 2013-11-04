package restaurant.test.mock;


import java.util.ArrayList;
import java.util.List;

import restaurant.CustomerAgent;
import restaurant.CashierAgent.Check;
import restaurant.WaiterAgent.MyCustomers;
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

	public List<MyCustomers> myCustomers
	= new ArrayList<MyCustomers>();
	
	public Cashier cashier;
	
	public Customer customer;

	public MockWaiter(String name) {
		super(name);

	}
	public  void msgOutOfHere(Customer c){
	
	}
	
	public  void msgCheckPlease(Customer cust){
		cashier.msgCheckPlease(cust, 0, 15.99);
	}
	
	public  void msgHereIsCheck(Customer cust, Check check){
		cust.msgHereIsYourBill(15.99);
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
