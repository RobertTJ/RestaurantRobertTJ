package restaurant;

import agent.Agent;
import restaurant.CustomerAgent.AgentState;
import restaurant.gui.WaiterGui;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class WaiterAgent extends Agent {
	
	public enum AgentState
	{DoingNothing, SeatingCustomer};
	public AgentState state = AgentState.DoingNothing;//The start state
	public enum Event
	{NewCustomerToSeat, DoneSeating};
	private Event event = Event.DoneSeating;

	public int tablenumber;
	private HostAgent host;
	public boolean busy = false;
	
	private CustomerAgent custtobeseated;
	
	private Semaphore atTable = new Semaphore(0,true);
	String name;
	//private Semaphore atTable = new Semaphore(0,true);

	public WaiterGui waiterGui = null;
	
	public WaiterAgent(String name) {
		super();
		
		this.name = name;
	}
	
	public void SetHost(HostAgent h) {
		this.host=h;
	}

	public String getMaitreDName() {
		return host.getName();
	}

	public String getName() {
		return name;
	}
	// Messages

	public void msgNewCustomerToSeat(CustomerAgent cust, int table){
		//busy=true;
		event = Event.NewCustomerToSeat;
		custtobeseated=cust;
		tablenumber=table;
		stateChanged();
	}

	public void msgLeavingTable(CustomerAgent cust) {
		host.msgLeavingTable(cust);
	}

	public void msgAtTable() {//from animation
		//print("msgAtTable() called");
		atTable.release();// = true;
		stateChanged();
	}
	
	/*public void msgReadyToOrder() {
		event=Event.customerReady;
	}*/
	
	/*public void msgOrderFood(string f) {
	 	event=Event.GotOrder;
	 }*/
	
	/*public void msgOrderReady(string f, int t) {
		event=Event.FoodReady;
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
		
					/*if (event==Event.FoodReady) {
						state=ServeFood;
						DelieverFoodToTable(string f, int t);
					}*/
		
					/*if (event==Event.CustomerReady) {
						state=GetOrder;
						GetCustomerOrder(int t)
					}*/
		
					/*if (event==GotOrder && state==GetOrder) {
						state=AgentState.TakeOrderToCook;
					}*/
		
					if (event==Event.NewCustomerToSeat){
						state = AgentState.SeatingCustomer;
						busy=true;
						print("Busy!");
						//stateChanged();
						seatCustomer(custtobeseated, tablenumber);//the action
						return true;//return true to the abstract agent to reinvoke the scheduler.
					}	
				/*	if (event==Event.customerDone) {
						state=AgentState.cleantable;
						prepareTable(CustomerAgent c, int t);
					}*/
		

		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions

	private void seatCustomer(CustomerAgent customer, int table) {
		customer.msgSitAtTable();
		DoSeatCustomer(customer, table);
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		event=Event.DoneSeating;
		waiterGui.DoLeaveCustomer();
	}

	// The animation DoXYZ() routines
	private void DoSeatCustomer(CustomerAgent customer, int table) {
		//Notice how we print "customer" directly. It's toString method will do it.
		//Same with "table"
		print("Seating " + customer + " at " + table);
		waiterGui.DoBringToTable(customer, table); 

	}
	
	public void msgAtFront(){
		state = AgentState.DoingNothing;
		busy=false;
		//print("I'm free now!");
		host.msgImFree();
		//atTable.release();
		stateChanged();
	}
	
	/*public void DeliverFoodToTable(string f, int t) {
		DoGoToTable //animation
		t.custtobeseated.msgDelieverFood(f);
		state=AgentState.DoingNothing;
		stateChange();
	}*/
	
	/*public void GetCustomerOrder(int t) {
	  
	  }
	 */

	/*public void GiveCookOrder(WaiterAgent waiter, string food, int table) {
	  
	  }
	 */
	
	/*PrepareTable(CustomerAgent c, int t) {
	  
	  }
	 */
	
	//utilities

	public void setGui(WaiterGui gui) {
		waiterGui = gui;
	}

	public WaiterGui getGui() {
		return waiterGui;
	}

	/*private class Table {
		CustomerAgent occupiedBy;
		int tableNumber;

		Table(int tableNumber) {
			this.tableNumber = tableNumber;
		}

		void setOccupant(CustomerAgent cust) {
			occupiedBy = cust;
		}

		void setUnoccupied() {
			occupiedBy = null;
		}

		CustomerAgent getOccupant() {
			return occupiedBy;
		}

		boolean isOccupied() {
			return occupiedBy != null;
		}

		public String toString() {
			return "table " + tableNumber;
		}
	}*/
}

