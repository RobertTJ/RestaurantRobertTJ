package restaurant;

import agent.Agent;
import restaurant.gui.HostGui;
import restaurant.HostAgent;

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
public class CookAgent extends Agent {

	private String name;
	//List<Order> orders= new <ListArray>Order();
	HostAgent host;
	
	//public HostGui hostGui = null;
	
	public enum CookState {pending, cooking, done, out};
	Timer timer = new Timer();

	public CookAgent(String name) {
		super();

		this.name = name;
		// make some tables
	
	}

	public List<Order> allOrders
	= new ArrayList<Order>();
	
	public List<WaiterAgent> Waiters
	= new ArrayList<WaiterAgent>();
	
	public void SetHost(HostAgent h) {
		this.host=h;
	}
	
	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}

	// Messages
	
	public void msgNewOrder(WaiterAgent waiter, int table, String order) {
		allOrders.add(new Order(waiter,table, order));
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
			if(order.state==CookState.done) {
				order.setStateOut();
				callWaiter(order);
				
				return true;
			}
		}
		
		for (Order order : allOrders){
			if(order.state==CookState.pending) {
				order.setStateCooking();
				Cook(order);
				
				return true;
			}
		}

		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions
	public void callWaiter(Order o) {
		o.getWaiter().msgOrderReady(o.getOrder(), o.getTableNumber());
	}
	
	public void Cook(final Order o) {
		int cooktime;
		if (o.order == "Steak") {
			cooktime=10000;
		}
		else if (o.order == "Chicken") {
			cooktime=8000;
		}
		else if (o.order == "Salad") {
			cooktime=1000;
		}
		else {
			cooktime=5000;
		}
		
		timer.schedule(new TimerTask() {
			Object cook = 1;
			public void run() {
				//look at menu, call waiter when ready
				o.setStateDone();
				//waiter.msgReadyToOrder(temp);
				stateChanged();
			}
		},
		cooktime);
	}

	private class Order {
		WaiterAgent waiter;
		String order;
		int table;
		CustomerAgent customer;
		CookState state;
		
		
		
		Order(WaiterAgent Waiter, int tableNumber, String o) {
			this.table = tableNumber;
			this.waiter = Waiter;
			this.order=o;
			state=CookState.pending;
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
			order = o;
		}
		
		String getOrder () {
			return order;
		}

		void setCustomer(CustomerAgent cust) {
			customer = cust;
		}

		CustomerAgent getCustomer() {
			return customer;
		}
		
		void setWaiter(WaiterAgent w) {
			waiter = w;
		}

		WaiterAgent getWaiter() {
			return waiter;
		}
		
	}
	
	
}

