package restaurant;

import agent.Agent;
//import restaurant.gui.HostGui;
import restaurant.HostAgent;
//import restaurant.CookAgent.Order;

import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MarketAgent extends Agent {

	private String name;
	HostAgent host;
	CookAgent cook;
	

	public MarketAgent(String name) {
		super();

		this.name = name;
		
		inventory.AddMore ("Pizza", 1);
		inventory.AddMore ("Steak", 2);
		inventory.AddMore ("Salad", 3);
		inventory.AddMore ("Chicken", 4);
	}
	
	Inventory inventory = new Inventory();
	
	public void SetHost(HostAgent h) {
		this.host=h;
	}
	
	public void SetCook(CookAgent c) {
		this.cook=c;
	}
	
	public String getName() {
		return name;
	}
	
	public List<Order> allOrders
	= new ArrayList<Order>();
	 

	// Messages
	
	public void msgNewOrders (String order, int amount) {
		allOrders.add(new Order(amount, order));
		stateChanged();
	}

	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		/* Think of this next rule as:
            Does there exist a table and customer,
            so that table is unoccupied and customer is waiting.
            If so seat him at the table.
		 */
		
		for (Order order : allOrders){
			if (order.getAmount() < inventory.GetAmountOf(order.getOrder().getChoice())) {
				print("order:" + order.getAmount() + " vs " + inventory.GetAmountOf(order.getOrder().getChoice()));

				FullfillOrder(order);
				print("order:" + order.getAmount() + " vs " + inventory.GetAmountOf(order.getOrder().getChoice()));
				return true;
			}

			else if (order.getAmount() >= inventory.GetAmountOf(order.getOrder().getChoice())) {
				CanNotFullfillOrder(order);
				return true;
			}
			
		}

		return false;
	}

	// Actions
	
	private void FullfillOrder(Order o) {
		cook.msgOrderFullfilled(o.getOrder().getChoice(), o.getAmount());
		inventory.OrderAmount(o.getOrder().getChoice(), o.getAmount());
		allOrders.remove(o);
		print("Delivererd " + o.getAmount() + " " + o.getOrder().getChoice());
		print("Order fullfilled.");
		stateChanged();
	}
	
	private void CanNotFullfillOrder(Order o) {
		cook.msgCanNotFullfillOrder(o.getOrder().getChoice(), o.getAmount(), inventory.GetAmountOf(o.getOrder().getChoice()), this);
		inventory.OrderAmount(o.getOrder().getChoice(), inventory.GetAmountOf(o.getOrder().getChoice()));
		print("Delivererd " + inventory.GetAmountOf(o.getOrder().getChoice()) + " " + o.getOrder().getChoice());
		print("Out of " + o.getOrder().getChoice());
		allOrders.remove(o);
		stateChanged();
	}
	
	// Classes
	
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

