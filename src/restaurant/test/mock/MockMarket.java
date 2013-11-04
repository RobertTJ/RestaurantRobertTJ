package restaurant.test.mock;


import java.util.Timer;

import restaurant.CashierAgent.Check;
import restaurant.CustomerAgent;
import restaurant.MarketAgent;
//import restaurant.MarketAgent.Food;
//import restaurant.MarketAgent.Inventory;
import restaurant.WaiterAgent.Menu;
import restaurant.gui.CustomerGui;
import restaurant.gui.RestaurantGui;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Cook;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Host;
import restaurant.interfaces.Market;
import restaurant.interfaces.Waiter;

/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public class MockMarket extends Mock implements Market {

public String name;
	
	public Host host;
	public Cook cook;
	public Cashier cashier;
	Timer timer = new Timer();
	double owed=0.00;
	boolean BillPending = false;

	
	
	Inventory inventory = new Inventory();
	
	public MockMarket(String name) {
		super(name);
		// TODO Auto-generated constructor stub
		

		this.name = name;
		
		inventory.AddMore ("Pizza", 5);
		inventory.AddMore ("Steak", 5);
		inventory.AddMore ("Salad", 5);
		inventory.AddMore ("Chicken", 5);
	}

	@Override
	public void msgPayingBill(double b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgNewOrders(String type, int i) {
		// TODO Auto-generated method stub
		
	}

	private class Food {
		String choice;
		int cooktime;
		
		Food(String c) {
			choice=c;
			
			if (c == "Steak") {
				cooktime=10000;
			}
			else if (c == "Chicken") {
				cooktime=8000;
			}
			else if (c == "Salad") {
				cooktime=1000;
			}
			else {
				cooktime=5000;
			}
		}
		
		String getChoice() {
			return choice;
		}
	}
	
	public class Inventory {
		int AmountOfChicken;
		int AmountOfSteak;
		int AmountOfPizza;
		int AmountOfSalad;
		
		public Inventory() {
			AmountOfChicken = 0;
			AmountOfSteak = 0;
			AmountOfPizza = 0;
			AmountOfSalad = 0;
		}
		
		public int GetAmountOf (String foodtype) {
			if (foodtype == "Steak") {
				return AmountOfSteak;
			}
			else if (foodtype == "Chicken") {
				return AmountOfChicken;
			}
			else if (foodtype == "Pizza") {
				return AmountOfPizza;
			}
			else {
				return AmountOfSalad;
			}
		}
		
		public void AddMore (String foodtype, int amount) {
			if (foodtype == "Steak") {
				AmountOfSteak = AmountOfSteak + amount;
			}
			else if (foodtype == "Chicken") {
				AmountOfChicken = AmountOfChicken + amount;
			}
			else if (foodtype == "Pizza") {
				AmountOfPizza = AmountOfPizza + amount;
			}
			else if (foodtype == "Salad") {
				AmountOfSalad = AmountOfSalad + amount;
			}
		}
		
		public void OrderAmount (String foodtype, int amount) {
			if (foodtype == "Steak") {
				AmountOfSteak = AmountOfSteak - amount;
			}
			else if (foodtype == "Chicken") {
				AmountOfChicken = AmountOfChicken - amount;
			}
			else if (foodtype == "Pizza") {
				AmountOfPizza = AmountOfPizza - amount;
			}
			else if (foodtype == "Salad") {
				AmountOfSalad = AmountOfSalad - amount;
			}
		}
	}
	
	
	private class Order {
		//String choice;
		int amount;
		Food order;
		

		Order( int quantity, String o) {
			this.amount = quantity;
			this.order=new Food(o);
		}
		
		void setAmount (int n) {
			amount = n;
		}
		
		int getAmount () {
			return amount;
		}
		
		void setOrder (String o) {
			order.choice = o;
		}
		
		Food getOrder () {
			return order;
		}
		
	}
	
	
}
