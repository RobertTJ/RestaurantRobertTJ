package restaurant.test.mock;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import restaurant.CashierAgent.Check;
//import restaurant.MarketAgent.Order;
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
	public double owed=0.00;
	boolean BillPending = false;

	public List<Order> allOrders
	= Collections.synchronizedList(new ArrayList<Order>());
	
	public Inventory inventory = new Inventory();
	
	public void SetCook(Cook c) {
		this.cook=c;
	}
	
	public MockMarket(String name) {
		super(name);
		// TODO Auto-generated constructor stub
		

		this.name = name;
		
		inventory.AddMore ("Pizza", 3);
		inventory.AddMore ("Steak", 3);
		inventory.AddMore ("Salad", 3);
		inventory.AddMore ("Chicken", 3);
	}

	@Override
	public void msgPayingBill(double b) {
		owed = owed - b;
		BillPending = false;		
	}

	@Override
	public void msgNewOrders(String order, int amount) {
		allOrders.add(new Order(amount, order));	
		
		if (amount<inventory.AmountOfSteak) {
			FullfillOrder(allOrders.get(0));
		}
		else {
			CanNotFullfillOrder(allOrders.get(0));
		}
	}
	
	public void SendBill() {
		cashier.msgFoodBill(this, owed);
	}

	
	public void FullfillOrder( Order o) {

		inventory.OrderAmount(o.getOrder().getChoice(), o.getAmount());
		allOrders.remove(o);
		

				cook.msgOrderFullfilled(o.getOrder().getChoice(), o.getAmount());
		
		if (o.getOrder().getChoice() == "Steak") {
			owed = o.getAmount() * 9.99;
		}
		else if (o.getOrder().getChoice() == "Chicken") {
			owed = o.getAmount() * 7.99;		}
		else if (o.getOrder().getChoice() == "Salad") {
			owed = o.getAmount() * 2.99;		}
		else {
			owed = o.getAmount() * 5.99;		}
	}
	
	
	public void CanNotFullfillOrder( Order o) {
		int thisorder = inventory.GetAmountOf(o.getOrder().getChoice());
		
		allOrders.remove(o);

		
	cook.msgCanNotFullfillOrder(o.getOrder().getChoice(), o.getAmount(), inventory.GetAmountOf(o.getOrder().getChoice()), this);
				
		inventory.OrderAmount(o.getOrder().getChoice(), inventory.GetAmountOf(o.getOrder().getChoice()));

	
		if (o.getOrder().getChoice() == "Steak") {
			owed = thisorder * 9.99;
		}
		else if (o.getOrder().getChoice() == "Chicken") {
			owed = thisorder * 7.99;		}
		else if (o.getOrder().getChoice() == "Salad") {
			owed = thisorder * 2.99;		}
		else {
			owed = thisorder * 5.99;		}
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
		public int AmountOfChicken;
		public int AmountOfSteak;
		public int AmountOfPizza;
		public int AmountOfSalad;
		
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
