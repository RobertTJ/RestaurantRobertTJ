package restaurant;

import agent.Agent;
import restaurant.gui.CookGui;
import restaurant.HostAgent;
import restaurant.interfaces.Cook;


import restaurant.interfaces.Customer;
import restaurant.interfaces.Host;
import restaurant.interfaces.Market;
import restaurant.interfaces.Waiter;

import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class CookAgent extends Agent implements Cook{

	private String name;
	//List<Order> orders= new <ListArray>Order();
	Host host;
	
	boolean PayingAttention = true;
	private Semaphore atFridge = new Semaphore(0,true);
	private Semaphore atCounter = new Semaphore(0,true);
	private Semaphore atGrill = new Semaphore(0,true);
	private Semaphore atCenter = new Semaphore(0,true);

	
	//public HostGui hostGui = null;
	
	public enum CookState {pending, cooking, done, out, outoffood};
	Timer timer = new Timer();
	
	
	CookGui gui= null;

	public CookAgent(String name) {
		super();

		this.name = name;
		PayingAttention = true;
		inventory.AddMore ("Pizza", 1);
		inventory.AddMore ("Steak", 1);
		inventory.AddMore ("Salad", 1);
		inventory.AddMore ("Chicken",1);
		stateChanged();
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

	
	Inventory inventory = new Inventory();
	
	public void AddMarket(Market m) {
		Markets.add(m);
		OutOfBeef.add(false); //hack, get info from markets
		OutOfChicken.add(false);
		OutOfPizza.add(false);
		OutOfSalad.add(false);
		stateChanged();
	}
	
	public void SetHost(Host h) {
		this.host=h;
		stateChanged();
	}
	
	public void setGui(CookGui g) {
		this.gui = g;
	}
	
	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}
	
	public void msgPickedUp(int t) {
		gui.PickedUp(t);
	}

	// Messages
	@Override
	public void msgAtCenter() {
		atCenter.release();
		stateChanged();
	}
	@Override
	public void msgAtCounter() {
		atCounter.release();
		stateChanged();
	}
	@Override
	public void msgAtGrill() {
		atGrill.release();
		stateChanged();
	}
	@Override
	public void msgAtFridge() {
		atFridge.release();
		stateChanged();
	}
	@Override
	public void msgNewOrder(Waiter waiter, int table, String order) {
		allOrders.add(new Order(waiter,table, order));
		stateChanged();
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
		stateChanged();
	}
	
	public void msgCanNotFullfillOrder(String food, int amount, int available, Market m) {
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
		stateChanged();
	}

	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
	
		synchronized(allOrders) {
		for (Order order : allOrders){
			if(order.state==CookState.done) {
				
				order.setStateOut();
				callWaiter(order);
				
				return true;
			}
		}
		}
		
		if (PayingAttention==true && Markets.isEmpty()==false) {
			if (inventory.GetAmountOf("Steak") <= 1 && orderedSteak==false) {
				Restock("Steak");
				return true;
			}
			if (inventory.GetAmountOf("Chicken") <= 1 && orderedChicken==false) {
				Restock("Chicken");
				return true;
			}
			if (inventory.GetAmountOf("Pizza") <= 1 && orderedPizza==false) {
				Restock("Pizza");
				return true;
			}
			if (inventory.GetAmountOf("Salad") <= 1 && orderedSalad==false) {
				Restock("Salad");
				return true;
			}
		}
		
		synchronized(allOrders) {
		for (Order order : allOrders){
			if(order.state==CookState.pending && inventory.GetAmountOf(order.order.getChoice()) == 0) {
				print("Out of " + order.order.getChoice());
				order.setStateOutofFood();
				GetMoreFood(order);
				
				return true;
			}
		}
		}
		
		synchronized(allOrders){
		for (Order order : allOrders){
			if(order.state==CookState.pending && inventory.GetAmountOf(order.order.getChoice()) > 0) {
				
				order.setStateCooking();
				Cook(order);
				
				return true;
			}
		}
		}

		return false;
	}

	// Actions
	private void GetMoreFood(Order o) {
		o.getWaiter().msgGetNewOrder(o.getCustomer());
		
		synchronized(Markets) {
		for (int i=0;i<Markets.size();i++){
			if(o.getOrder().getChoice() == "Steak" && OutOfBeef.get(i)==false && orderedSteak==false) {
				orderedSteak=true;
				Markets.get(i).msgNewOrders(o.getOrder().getChoice(), 3);	
				break;
			}
			else if(o.getOrder().getChoice() == "Chicken" && OutOfChicken.get(i)==false && orderedChicken==false) {
				orderedChicken=true;
				Markets.get(i).msgNewOrders(o.getOrder().getChoice(), 3);	
				break;
			}
			else if(o.getOrder().getChoice() == "Pizza" && OutOfPizza.get(i)==false && orderedPizza==false) {
				orderedPizza=true;
				Markets.get(i).msgNewOrders(o.getOrder().getChoice(), 3);	
				break;
			}
			else if(o.getOrder().getChoice() == "Salad" && OutOfSalad.get(i)==false && orderedSalad==false) {
				orderedSalad=true;
				Markets.get(i).msgNewOrders(o.getOrder().getChoice(), 3);	
				break;
			}
			
		}
		}
		
		stateChanged();
	}
	
	private void Restock(String type) {
		synchronized(Markets) {
		for (int i=0;i<Markets.size();i++){
			if(type == "Steak" && OutOfBeef.get(i)==false && orderedSteak==false) {
				Markets.get(i).msgNewOrders(type, 3);	
				orderedSteak=true;
				break;
			}
			else if(type == "Chicken" && OutOfChicken.get(i)==false && orderedChicken==false) {
				Markets.get(i).msgNewOrders(type, 3);	
				orderedChicken=true;
				break;
			}
			else if(type== "Pizza" && OutOfPizza.get(i)==false && orderedPizza==false) {
				Markets.get(i).msgNewOrders(type, 3);	
				orderedPizza=true;
				break;
			}
			else if(type == "Salad" && OutOfSalad.get(i)==false && orderedSalad==false) {
				Markets.get(i).msgNewOrders(type, 3);
				orderedSalad=true;
				break;
			}
			
		}
		}
		
		stateChanged();
	}
	
	private void callWaiter(Order o) {
		gui.DoGoToGetFood(o.getTableNumber());
		try {
			atGrill.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		gui.DoGoToCounter(o.getOrder().getChoice(), o.getTableNumber());
		try {
			atCounter.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		o.getWaiter().msgOrderReady(o.order.getChoice(), o.getTableNumber());
		gui.DoGoToCenter();
		try {
			atCenter.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		stateChanged();
	}
	
	private void Cook(final Order o) {	
		
		
		inventory.CookOneOf(o.order.getChoice());
		print("There are " + inventory.GetAmountOf(o.order.getChoice()) +" " + o.order.getChoice() + "left now");
		
		if (inventory.GetAmountOf(o.order.getChoice())==0) {
			Restock(o.getOrder().getChoice());
		}
		
		gui.DoGoToFridge(o.getOrder().getChoice(), o.getTableNumber());
		try {
			atFridge.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		gui.DoGoToGrill(o.getTableNumber());
		try {
			atGrill.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		print("Started cooking " + o.order.getChoice());
		timer.schedule(new TimerTask() {
			Object cook = 1;
			public void run() {
				//look at menu, call waiter when ready
				o.setStateDone();
				print("Finished cooking " + o.order.getChoice());
				//waiter.msgReadyToOrder(temp);
				stateChanged();
			}
		},
		o.order.cooktime);
		
		gui.DoGoToCenter();
		try {
			atCenter.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		stateChanged();
	}

	//Extra
	
	public void SetPayingAttention(boolean Attentive) {
		PayingAttention = Attentive;
	}
	
	//  Classes
	
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
	
	private class Inventory {
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

