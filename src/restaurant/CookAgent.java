package restaurant;

import agent.Agent;
import restaurant.gui.HostGui;
import restaurant.HostAgent;

import java.util.*;
import java.util.concurrent.Semaphore;

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

	public CookAgent(String name) {
		super();

		this.name = name;
		// make some tables
	
	}

	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}

	// Messages

	/*public void NewOrder(string food, int table, WaiterAgent waiter) {
		
	}*/

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		/* Think of this next rule as:
            Does there exist a table and customer,
            so that table is unoccupied and customer is waiting.
            If so seat him at the table.
		 */
		
		/*for (Order order : orders){
			if(order.state=done&&order.waiter.state==free) callWaiter(o);
		}*/
		
		/*for (Order order : orders){
		if(order.state=pending) Cool(order);
	    }*/
	

		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions
	/*public void callWaiter(Order o) {
		//tell waiter to deliver food.  msgFoodready(o.f); ? somewhat off
	}*/
	
	/*public void Cook(Order o) {
		//initialize map based on food, start timer based on cooktime
	}*/

	
}

