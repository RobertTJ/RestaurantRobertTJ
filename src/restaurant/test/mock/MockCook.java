package restaurant.test.mock;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import restaurant.CashierAgent.Check;
import restaurant.CookAgent.CookState;
//import restaurant.CookAgent.Food;
//import restaurant.CookAgent.Inventory;
import restaurant.CookAgent.Order;
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
		inventory.AddMore ("Pizza", 0);
		inventory.AddMore ("Steak", 0);
		inventory.AddMore ("Salad", 0);
		inventory.AddMore ("Chicken",0);
	}

	@Override
	public void msgAtCounter() {
	}
	@Override
	public void msgAtGrill() {
	}
	@Override
	public void msgAtFridge() {
	}
	@Override
	public void msgNewOrder(Waiter waiter, int table, String order) {
	}
	
	public List<Order> allOrders
	= Collections.synchronizedList(new ArrayList<Order>());
	
	public List<Waiter> Waiters
	= Collections.synchronizedList(new ArrayList<Waiter>());
	
	public List<Market> Markets
	= Collections.synchronizedList(new ArrayList<Market>());
	
	public List<Boolean> OutOfBeef = Collections.synchronizedList(new ArrayList<Boolean>());
	public List<Boolean> OutOfChicken = Collections.synchronizedList(new ArrayList<Boolean>());
	public List<Boolean> OutOfPizza = Collections.synchronizedList(new ArrayList<Boolean>());
	public List<Boolean> OutOfSalad = Collections.synchronizedList(new ArrayList<Boolean>());
	boolean orderedSteak=false;
	boolean orderedChicken=false;
	boolean orderedPizza=false;
	boolean orderedSalad=false;

	
	public Inventory inventory = new Inventory();
	
	public void AddMarket(Market m) {
		Markets.add(m);
		OutOfBeef.add(false); //hack, get info from markets
		OutOfChicken.add(false);
		OutOfPizza.add(false);
		OutOfSalad.add(false);
	}
	
	@Override
	public void msgOrderFullfilled(String food, int amount) {
	
		if (food == "Steak") {
			orderedSteak=false;
		}
		else if (food == "Chicken") {
			orderedChicken=false;
		}
		else if (food == "Pizza") {
			orderedPizza=false;
		}
		else if (food == "Steak") {
			orderedSalad=false;
		}
		
		inventory.AddMore(food, amount);
	}

	@Override
	public void msgCanNotFullfillOrder(String food, int amount, int available,	Market m) {
		if (available > 0) inventory.AddMore(food, available);

		if (food == "Steak") {
			orderedSteak=false;
		}
		else if (food == "Chicken") {
			orderedChicken=false;
		}
		else if (food == "Pizza") {
			orderedPizza=false;
		}
		else if (food == "Steak") {
			orderedSalad=false;
		}
		
		synchronized(Markets) {
		for (int i = 0; i < Markets.size(); i++) {
			if (m == Markets.get(i)) {
				if (food == "Steak") {
					OutOfBeef.set(i, true);
					break;
				}
				else if (food == "Chicken") {
					OutOfChicken.set(i, true);
					break;
				}
				else if (food == "Pizza") {
					OutOfPizza.set(i, true);
					break;
				}
				else if (food == "Steak") {
					OutOfSalad.set(i, true);
					break;
				}
			}
		}
		}

	}

	@Override
	public void msgAtCenter() {
	}
	
	public void Restock(String type, int k) {
		synchronized(Markets) {
		for (int i=0;i<Markets.size();i++){
			if(type == "Steak" && OutOfBeef.get(i)==false && orderedSteak==false) {
				Markets.get(i).msgNewOrders(type, k);	
				//orderedSteak=true;
				break;
			}
			else if(type == "Chicken" && OutOfChicken.get(i)==false && orderedChicken==false) {
				Markets.get(i).msgNewOrders(type, k);	
				orderedChicken=true;
				break;
			}
			else if(type== "Pizza" && OutOfPizza.get(i)==false && orderedPizza==false) {
				Markets.get(i).msgNewOrders(type, k);	
				orderedPizza=true;
				break;
			}
			else if(type == "Salad" && OutOfSalad.get(i)==false && orderedSalad==false) {
				Markets.get(i).msgNewOrders(type, k);
				orderedSalad=true;
				break;
			}
			
		}
		}
		
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
		
		Inventory() {
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
		
		public void CookOneOf (String foodtype) {
			if (foodtype == "Steak") {
				AmountOfSteak--;
			}
			else if (foodtype == "Chicken") {
				AmountOfChicken--;
			}
			else if (foodtype == "Pizza") {
				AmountOfPizza--;
			}
			else if (foodtype == "Salad") {
				AmountOfSalad--;
			}
		}
	}
	
	public class Order {
		Waiter waiter;
		//String choice;
		int table;
		Customer customer;
		CookState state;
		Food order;
		
		
		
		Order(Waiter Waiter, int tableNumber, String o) {
			this.table = tableNumber;
			this.waiter = Waiter;
			this.order=new Food(o);
			
			state=CookState.pending;
		}
		
		void setStateOutofFood() {
			state = CookState.outoffood;
		}
		
		void setStateCooking () {
			state = CookState.cooking;
		}
		
		void setStateDone () {
			state = CookState.done;
		}
		
		void setStateOut () {
			state = CookState.out;
		}
		
		CookState getState () {
			return state;
		}
		
		void setTableNumber (int n) {
			table = n;
		}
		
		int getTableNumber () {
			return table;
		}
		
		void setOrder (String o) {
			order.choice = o;
		}
		
		Food getOrder () {
			return order;
		}

		void setCustomer(Customer cust) {
			customer = cust;
		}

		Customer getCustomer() {
			return customer;
		}
		
		void setWaiter(Waiter w) {
			waiter = w;
		}

		Waiter getWaiter() {
			return waiter;
		}
		
	}
	

}
