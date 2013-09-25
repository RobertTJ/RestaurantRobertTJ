package restaurant;

import agent.Agent;
import restaurant.CustomerAgent.AgentState;
import restaurant.gui.WaiterGui;
import restaurant.CookAgent;

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
	{DoingNothing, SeatingCustomer, GetOrder, TakeOrderToCook, ServeFood, CleanTable};
	
	public AgentState state = AgentState.DoingNothing;//The start state
	
	public enum Event
	{NewCustomerToSeat, DoneSeating, customerReady, GotOrder, FoodReady, customerDone};
	
	private Event event = Event.DoneSeating;
	private Event currentEvent = Event.DoneSeating;
	
	public enum CustState {Seating, Seated, ReadyToOrder, Ordered, WaitingForFood, OrderOut, Eating, Done, Leaving, Gone};

	public List<Customers> myCustomers
	= new ArrayList<Customers>();
	
	private List<Event> allEvents = new ArrayList<Event>();

	public int tablenumber;
	private HostAgent host;
	
	private CookAgent cook;
	
	public boolean busy = false;
	
	private Customers CurrentCustomer;
	
	private Semaphore atTable = new Semaphore(0,true);
	private Semaphore atCook = new Semaphore(0,true);
	String name;

	public WaiterGui waiterGui = null;
	
	public WaiterAgent(String name) {
		super();
		
		this.name = name;
	}
	
	public void setCook(CookAgent c) {
		this.cook=c;
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
		event = Event.NewCustomerToSeat;
		allEvents.add(event);
		CurrentCustomer= new Customers(cust,table);
		CurrentCustomer.setState(CustState.Seating);
		myCustomers.add(CurrentCustomer);
		tablenumber=table;
		stateChanged();
	}

	public void msgLeavingTable(CustomerAgent cust) {
		for (Customers myCustomer : myCustomers) {
			if (myCustomer.getCustomer() == cust) {
				myCustomer.setState(CustState.Done);
				//myCustomers.remove(myCustomer);
			}
		}
		event=Event.customerDone;
		allEvents.add(event);
		stateChanged();
	}

	public void msgAtTable() {//from animation
		//print("msgAtTable() called");
		atTable.release();// = true;
		stateChanged();
	}
	
	public void msgReadyToOrder(CustomerAgent cust) {
		for (Customers myCustomer : myCustomers) {
			if (myCustomer.getCustomer() == cust) myCustomer.setState(CustState.ReadyToOrder);
		}
		
		event=Event.customerReady;
		allEvents.add(event);
		stateChanged();
	}
	
	public void msgOrderFood(CustomerAgent cust, String food) {
		print("test "+ this.state);
		for (Customers myCustomer : myCustomers) {
			if (myCustomer.getCustomer() == cust) {
				myCustomer.setState(CustState.Ordered);
				myCustomer.setOrder(food);
			}
		}
		
	 	event=Event.GotOrder;
		allEvents.add(event);
	 	stateChanged();
	 }
	
	public void msgAtFront(){
		state = AgentState.DoingNothing;
		busy=false;
		host.msgImFree();
		stateChanged();
	}
	
	public void msgAtCook(){
		atCook.release();
		stateChanged();
	}
	
	public void msgOrderReady(String food, int table) {
		for (Customers myCustomer : myCustomers) {
			if (myCustomer.getTableNumber() == table) {
				myCustomer.setState(CustState.Eating);
				CurrentCustomer = myCustomer;
				}
		}
		
		event=Event.FoodReady;
		allEvents.add(event);
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
		
		//if (!busy)
		for (Event pendingEvents : allEvents) {
			if (pendingEvents == Event.GotOrder) { //&& state == AgentState.GetOrder) {
				busy=true;
				currentEvent = pendingEvents;
				allEvents.remove(pendingEvents);
				break;
			}
		}
		for (Event pendingEvents : allEvents) {
			if (pendingEvents == Event.FoodReady && state == AgentState.DoingNothing && busy==false) {
				busy=true;
				currentEvent = pendingEvents;
				allEvents.remove(pendingEvents);
				break;
			}
		}
		for (Event pendingEvents : allEvents) {
			if (pendingEvents == Event.customerDone && state == AgentState.DoingNothing && busy==false) {
				//print("trying");
				busy=true;
				currentEvent = pendingEvents;
				allEvents.remove(pendingEvents);
				break;
			}
		}
		for (Event pendingEvents : allEvents) {
			if (pendingEvents == Event.NewCustomerToSeat && state == AgentState.DoingNothing && busy==false) {
				busy=true;
				currentEvent = pendingEvents;
				allEvents.remove(pendingEvents);
				break;
			}
		}
		for (Event pendingEvents : allEvents) {
			if (pendingEvents == Event.customerReady && state == AgentState.DoingNothing && busy==false) {
				busy=true;
				currentEvent = pendingEvents;
				allEvents.remove(pendingEvents);
				break;
			}
		}
		
		if (!busy) {
			return false;
		}
		
					if (currentEvent==Event.FoodReady) {
						state = AgentState.ServeFood;
						for (Customers myCustomer : myCustomers) {
							if (myCustomer.getState() == CustState.WaitingForFood) {
								myCustomer.setState(CustState.OrderOut);
								CurrentCustomer = myCustomer;
								break;
								}
						}
						DeliverFoodToTable(CurrentCustomer);
						return true;
					}
		
					if (currentEvent==Event.customerReady) {
						state = AgentState.GetOrder;
						GetCustomerOrder();
						return true;
					}
					
					if (currentEvent == Event.GotOrder) {
						state = AgentState.TakeOrderToCook;
						print("test2");
						for (Customers myCustomer : myCustomers) {
							if (myCustomer.getState() == CustState.Ordered) {
								//myCustomer.setState(5);
								//CurrentCustomer = myCustomer;
								break;
								}
						}
						TakeOrderToKitchen(CurrentCustomer);
					}
		
					if (currentEvent==Event.NewCustomerToSeat){
						state = AgentState.SeatingCustomer;
						//stateChanged();
						seatCustomer();//the action
						return true;//return true to the abstract agent to reinvoke the scheduler.
					}	
					if (currentEvent==Event.customerDone) {
						//print ("Testing");
						state=AgentState.CleanTable;
						PrepareTable();
						return true;
					}
		

		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions

	private void seatCustomer() {
		for (Customers myCustomer : myCustomers) {
			if (myCustomer.getState()==CustState.Seating) {
				myCustomer.setState(CustState.Seated);
				CurrentCustomer = myCustomer;
				break;
			}
		}
	
		
		CurrentCustomer.getCustomer().msgSitAtTable();
		DoSeatCustomer(CurrentCustomer.getCustomer(), CurrentCustomer.getTableNumber());
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
	
	public void GetCustomerOrder() {
		print("Going to get order");
		for (Customers myCustomer : myCustomers) {
			if (myCustomer.getState()==CustState.ReadyToOrder) {
				CurrentCustomer = myCustomer;
				break;
			}
		}
		waiterGui.DoGoToTable(CurrentCustomer.getCustomer(), CurrentCustomer.getTableNumber());
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		CurrentCustomer.setState(CustState.Ordered);

		CurrentCustomer.getCustomer().msgHereForOrder();
		print("Here for order");
	}
	
	public void TakeOrderToKitchen(Customers current) {
		print("Taking order to chef");
		waiterGui.DoGoToCook();
		try {
			atCook.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		current.setState(CustState.WaitingForFood);
		cook.msgNewOrder(this,current.getTableNumber(), current.getOrder());
		waiterGui.DoLeaveCustomer();
		print("Chef has order");
	}

	
    public void DeliverFoodToTable(Customers current) {
    	print("Order Ready");
    	waiterGui.DoGoToTable(current.getCustomer(), current.getTableNumber());
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		current.setState(CustState.Eating); 
		current.getCustomer().msgDeliveredFood();
    	waiterGui.DoLeaveCustomer();
    	print("Delivered Food");
    }

	public void PrepareTable() {
		print("Table empty");
		for (Customers myCustomer : myCustomers) {
			if (myCustomer.getState()==CustState.Done) {
				CurrentCustomer = myCustomer;
				break;
			}
		}
		CurrentCustomer.setState(CustState.Leaving);
		waiterGui.DoGoToTable(CurrentCustomer.getCustomer(), CurrentCustomer.getTableNumber());
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		host.msgLeavingTable(CurrentCustomer.getCustomer());
		CurrentCustomer.setState(CustState.Gone);
		print("Table clean");
		waiterGui.DoLeaveCustomer();
	  }
	 
	
	//utilities

	public void setGui(WaiterGui gui) {
		waiterGui = gui;
	}

	public WaiterGui getGui() {
		return waiterGui;
	}
	
	private class Customers {
		CustomerAgent customer;
		int tableNumber;
		String order;
		CustState currentState;

		Customers(CustomerAgent customer, int tableNumber) {
			this.tableNumber = tableNumber;
			this.customer = customer;
		}
		
		void setState (CustState s) {
			currentState = s;
		}
		
		CustState getState () {
			return currentState;
		}
		
		void setTableNumber (int n) {
			tableNumber = n;
		}
		
		int getTableNumber () {
			return tableNumber;
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
	}

}
	

