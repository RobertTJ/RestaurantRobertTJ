package restaurant;

import agent.Agent;
import restaurant.CashierAgent.Check;
import restaurant.CustomerAgent.AgentState;
import restaurant.gui.WaiterGui;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Waiter;
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
public class WaiterAgent extends Agent implements Waiter{
	
	public enum AgentState
	{DoingNothing, SeatingCustomer, GetOrder, TakeOrderToCook, ServeFood, GetCheck, TakeCheckToCustomer, CleanTable, Break};
	
	public AgentState state = AgentState.DoingNothing;//The start state
	
	public enum Event
	{HeLeft, GoToCashier, GotCheck, NewCustomerToSeat, DoneSeating, customerReady, GotOrder, FoodReady, customerDone, OutOfFood, WantABreak, TakeABreak};
	
	private Event event = Event.DoneSeating;
	private Event currentEvent = Event.DoneSeating;
	
	public enum CustState {LeftEarly, other, NeedCheck, BringCheck, NewOrderNeeded, Seating, Seated, ReadyToOrder, Ordered, WaitingForFood, OrderOut, Eating, Done, Leaving, Gone};

	public List<MyCustomers> myCustomers
	= new ArrayList<MyCustomers>();
	
	private List<Event> allEvents = new ArrayList<Event>();

	public int tablenumber;
	private int xHome, yHome;
	private HostAgent host;
	
	private Cashier cashier;
	
	private CookAgent cook;
	
	public boolean busy = false;
	
	private MyCustomers CurrentCustomer = new MyCustomers(new CustomerAgent("Frank"), -1);
	
	private Semaphore atTable = new Semaphore(0,true);
	private Semaphore atFront = new Semaphore (0, true);
	private Semaphore atCook = new Semaphore(0,true);
	String name;
	
	private Menu currentMenu = new Menu();

	public WaiterGui waiterGui = null;
	
	public WaiterAgent(String name) {
		super();
		this.name = name;
	}
	
	public void SetHomePosition(int x, int y) {
		waiterGui.setHomePosition(x, y);
		xHome = x;
		yHome = y;
		stateChanged();
	}
	
	public void SetCashier(Cashier a) {
		this.cashier = a;
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
	
	public void msgOutOfHere(Customer c) {
		//print("Get here?");
		for (int i=0;i<myCustomers.size();i++) {
			if (myCustomers.get(i).getCustomer()==c) {
				myCustomers.get(i).setState(CustState.LeftEarly);
				break;
			}
		}
		state=AgentState.DoingNothing;
		event=Event.HeLeft;
		allEvents.add(event);
		stateChanged();
	}
	
	public void msgCheckPlease(Customer cust) {
		for (MyCustomers myCustomer : myCustomers) {
			if (myCustomer.getCustomer() == cust) myCustomer.setState(CustState.NeedCheck);
		}
		event = Event.GoToCashier;
		allEvents.add(event);
		stateChanged();
	}
	
	public void msgHereIsCheck(Customer cust, Check check) {
		for (MyCustomers myCustomer : myCustomers) {
			if (myCustomer.getCustomer() == cust) myCustomer.setCheck(check);
		}
		event = Event.GotCheck;
		allEvents.add(event);
		stateChanged();
	}
	
	public void msgCantBreakNow() {
		print("I can't take a break now!");
		//stateChanged();
	}
	
	public void msgGetNewOrder(Customer cust) {
		for (MyCustomers myCustomer : myCustomers) {
			if (myCustomer.getCustomer() == cust) myCustomer.setState(CustState.NewOrderNeeded);
		}
		event = Event.OutOfFood;
		allEvents.add(event);
		stateChanged();
	}

	public void msgNewCustomerToSeat(Customer cust, int table){
		event = Event.NewCustomerToSeat;
		allEvents.add(event);
		CurrentCustomer= new MyCustomers(cust,table);
		CurrentCustomer.setState(CustState.Seating);
		myCustomers.add(CurrentCustomer);
		tablenumber=table;
		stateChanged();
	}

	public void msgLeavingTable(Customer cust) {
		for (MyCustomers myCustomer : myCustomers) {
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
	
	public void msgReadyToOrder(Customer cust) {
		for (MyCustomers myCustomer : myCustomers) {
			if (myCustomer.getCustomer() == cust) myCustomer.setState(CustState.ReadyToOrder);
		}
		
		event=Event.customerReady;
		allEvents.add(event);
		stateChanged();
	}
	
	public void msgOrderFood(Customer cust, String food) {
		//print("test "+ this.state);
		for (MyCustomers myCustomer : myCustomers) {
			if (myCustomer.getCustomer() == cust) {
				myCustomer.setState(CustState.Ordered);
				myCustomer.setOrder(food);
				//print("in cust execution?");
			}
		}
		//print("in cust execution?2");
	 	event=Event.GotOrder;
		allEvents.add(event);
	 	stateChanged();
	 }
	
	public void msgAtFront(){
		
		this.state = AgentState.DoingNothing;
		//print("Do I ever get here?");
		busy=false;
		host.msgImFree(this);
		//working.release();
		stateChanged();

	}
	
	public void msgAtCust() {
		atFront.release();
		stateChanged();
	}
	
	public void msgAtCook(){
		atCook.release();
		stateChanged();
	}
	
	public void msgOrderReady(String food, int table) {
		print ("Order Ready");
		for (MyCustomers myCustomer : myCustomers) {
			if (myCustomer.getTableNumber() == table) {
				myCustomer.setState(CustState.Eating);
				CurrentCustomer = myCustomer;
				}
		}
		
		event=Event.FoodReady;
		allEvents.add(event);
		stateChanged();
	}
	
	public void msgIWantToGoOnBreak() {
		event=Event.WantABreak;
		allEvents.add(event);
		stateChanged();
	}
	
	public void msgTakeABreak() {
		event=Event.TakeABreak;
		allEvents.add(event);
		stateChanged();
	}
	
	public void msgBreakOver() {
		this.Resume();
		host.msgBackFromBreak(this);
		stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		
		
		try{
		
		//if (currentEvent)
		for (Event pendingEvents : allEvents) {
			if (pendingEvents == Event.TakeABreak) {
				//print("in here execution?");
				busy=true;
				currentEvent = pendingEvents;
				//allEvents.remove(pendingEvents);
				break;
			}
		}
		
		for (Event pendingEvents : allEvents) {
			if (pendingEvents == Event.HeLeft) {
				//print("in here execution?");
				busy=true;
				currentEvent = pendingEvents;
				//allEvents.remove(pendingEvents);
				break;
			}
		}
		
		for (Event pendingEvents : allEvents) {
			if (pendingEvents == Event.WantABreak) {
				//print("in here execution?");
				busy=true;
				currentEvent = pendingEvents;
				//allEvents.remove(pendingEvents);
				break;
			}
		}
		
		for (Event pendingEvents : allEvents) {
			if (pendingEvents == Event.GotOrder && this.state == AgentState.GetOrder) {
				//print("in here execution?");
				busy=true;
				currentEvent = pendingEvents;
				//allEvents.remove(pendingEvents);
				break;
			}
		}
		
		//if (busy == true && currentEvent != Event.GotOrder) return false;
		
		
		for (Event pendingEvents : allEvents) {
			if (pendingEvents == Event.FoodReady && this.state==AgentState.DoingNothing && busy == false) {
				busy=true;
				currentEvent = pendingEvents;
				//allEvents.remove(pendingEvents);
				break;
			}
		}
		
		for (Event pendingEvents : allEvents) {
			if (pendingEvents == Event.GotCheck && this.state==AgentState.DoingNothing && busy == false) {
				busy=true;
				currentEvent = pendingEvents;
				//allEvents.remove(pendingEvents);
				break;
			}
		}
		
		for (Event pendingEvents : allEvents) {
			if (pendingEvents == Event.GoToCashier && this.state==AgentState.DoingNothing && busy == false) {
				busy=true;
				currentEvent = pendingEvents;
				//allEvents.remove(pendingEvents);
				break;
			}
		}
		
		for (Event pendingEvents : allEvents) {
			if (pendingEvents == Event.customerDone && busy == false) {
				//print("trying");
				busy=true;
				currentEvent = pendingEvents;
				//allEvents.remove(pendingEvents);
				break;
			}
		}
		
		for (Event pendingEvents : allEvents) {
			if (pendingEvents == Event.NewCustomerToSeat && busy == false) {
				//print("but this works!");
				busy=true;
				currentEvent = pendingEvents;
				//allEvents.remove(pendingEvents);
				break;
			}
		}
		for (Event pendingEvents : allEvents) {
			if (pendingEvents == Event.OutOfFood && busy == false) {
				busy=true;
				currentEvent = pendingEvents;
				//allEvents.remove(pendingEvents);
				break;
			}
		}
		for (Event pendingEvents : allEvents) {
			if (pendingEvents == Event.customerReady && busy == false) {
				busy=true;
				currentEvent = pendingEvents;
				//allEvents.remove(pendingEvents);
				break;
			}
		}
		
		
		
		if (!busy) {
			return false;
		}
		
		
					if (currentEvent==Event.HeLeft) {
						//print("Here? Not yet");
						allEvents.remove(currentEvent);
						PrepareTableEarly();
						currentEvent=null;
						return true;
					}
					if (currentEvent==Event.FoodReady) {
						this.state = AgentState.ServeFood;
						for (MyCustomers myCustomer : myCustomers) {
							if (myCustomer.getState() == CustState.WaitingForFood) {
								myCustomer.setState(CustState.OrderOut);
								CurrentCustomer = myCustomer;
								break;
								}
						}
						allEvents.remove(currentEvent);
						DeliverFoodToTable(CurrentCustomer);
						currentEvent=null;
						return true;
					}
					
					if (currentEvent == Event.GotCheck) {
						this.state = AgentState.TakeCheckToCustomer;
						for (MyCustomers myCustomer : myCustomers) {
							if (myCustomer.getState() == CustState.BringCheck) {
								myCustomer.setState(CustState.other);
								CurrentCustomer = myCustomer;
								break;
								}
						}
						allEvents.remove(currentEvent);
						BringCheckTo(CurrentCustomer);
						currentEvent=null;
						return true;
					}
					
					if (currentEvent == Event.GoToCashier) {
						this.state = AgentState.GetCheck;
						allEvents.remove(currentEvent);
						GetCheckFromCashier();
						currentEvent=null;
						return true;
					}
					
					if (currentEvent==Event.OutOfFood) {
						this.state = AgentState.GetOrder;
						allEvents.remove(currentEvent);
						GetNewCustomerOrder();
						currentEvent=null;
						return true;
					}
					
					if (currentEvent==Event.WantABreak) {
						allEvents.remove(currentEvent);
						GiveMeABreak();
						currentEvent=null;
						return true;
					}
					
					if (currentEvent==Event.TakeABreak) {
						print("testing");
						allEvents.remove(currentEvent);
						TakeABreak();
						currentEvent=null;
						return true;
					}
		
					if (currentEvent==Event.customerReady) {
						this.state = AgentState.GetOrder;
						allEvents.remove(currentEvent);
						GetCustomerOrder();
						currentEvent=null;
						return true;
					}
					
					if (currentEvent == Event.GotOrder) {
						//print ("here?");

						this.state = AgentState.TakeOrderToCook;
						//print("test2");
						for (MyCustomers myCustomer : myCustomers) {
							if (myCustomer.getState() == CustState.Ordered) {
								//myCustomer.setState(5);
								//CurrentCustomer = myCustomer;
								break;
								}
						}
						allEvents.remove(currentEvent);
						TakeOrderToKitchen(CurrentCustomer);
						currentEvent=null;
						return true;
					}
		
					if (currentEvent==Event.NewCustomerToSeat){
						this.state = AgentState.SeatingCustomer;
						//stateChanged();
						allEvents.remove(currentEvent);
						seatCustomer();//the action
						currentEvent=null;
						return true;//return true to the abstract agent to reinvoke the scheduler.
					}	
					if (currentEvent==Event.customerDone) {
						//print ("Testing");
						this.state=AgentState.CleanTable;
						allEvents.remove(currentEvent);
						PrepareTable();
						currentEvent=null;
						return true;
					}
		

		return false;
		} catch(ConcurrentModificationException e) {
			// TODO Auto-generated catch block
			return false;
		}
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions
	
	private void GetCheckFromCashier() {
		for (MyCustomers myCustomer : myCustomers) {
			if (myCustomer.getState() == CustState.NeedCheck) {
				myCustomer.setState(CustState.BringCheck);
				CurrentCustomer = myCustomer;
				break;
				}
		}
		print("Getting check for " + CurrentCustomer.getCustomer());

		cashier.msgCheckPlease(CurrentCustomer.getCustomer(), CurrentCustomer.getTableNumber(), CurrentCustomer.GetBill());
		stateChanged();
	}
	
	private void BringCheckTo(MyCustomers c) {
		print("Taking check to " + c.getCustomer());
		waiterGui.DoGoToTable(c.getCustomer(), c.getTableNumber());
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		busy=true;
		this.state=AgentState.TakeCheckToCustomer;

		c.getCustomer().msgHereIsYourBill(c.getCheck());
		waiterGui.DoLeaveCustomer();
	}
	
	private void TakeABreak() {
		this.Pause();
		stateChanged();
	}
	
	private void GiveMeABreak() {
		//print ("hmm?");
		host.msgIWantABreak(this);
		stateChanged();
	}

	private void seatCustomer() {
		waiterGui.DoGoToCustomer();
		try {
			atFront.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		busy=true;
		this.state=AgentState.SeatingCustomer;
		
		for (MyCustomers myCustomer : myCustomers) {
			if (myCustomer.getState()==CustState.Seating) {
				myCustomer.setState(CustState.Seated);
				CurrentCustomer = myCustomer;
				break;
			}
		}
		
		CurrentCustomer.getCustomer().msgFollowMeToTable(this, currentMenu);
		DoSeatCustomer(CurrentCustomer.getCustomer(), CurrentCustomer.getTableNumber());
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		busy=true;
		this.state=AgentState.SeatingCustomer;
		//event=Event.DoneSeating;
		waiterGui.DoLeaveCustomer();
	}

	// The animation DoXYZ() routines
	private void DoSeatCustomer(Customer customer, int table) {
		//Notice how we print "customer" directly. It's toString method will do it.
		//Same with "table"
		print("Seating " + customer + " at table " + table + ".  Here is a menu.");
		waiterGui.DoBringToTable(customer, table); 

	}
	
	public void GetNewCustomerOrder() {
		print("Going to get a new order");// " + this.state);
		for (MyCustomers myCustomer : myCustomers) {
			if (myCustomer.getState()==CustState.NewOrderNeeded) {
				CurrentCustomer = myCustomer;
				break;
			}
		}
		//print("hi  "+this.state);
		waiterGui.DoGoToTable(CurrentCustomer.getCustomer(), CurrentCustomer.getTableNumber());
		//print("hihi  "+this.state);

		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		busy=true;
		this.state=AgentState.GetOrder;
		//print("hihihi  "+this.state);

		CurrentCustomer.setState(CustState.Ordered);
		//print("hi5  "+this.state + "   " + busy);

		CurrentCustomer.getCustomer().msgHereForNewOrder();
		print("Here for a new order");
	}
	
	public void GetCustomerOrder() {
		print("Going to get order");// " + this.state);
		for (MyCustomers myCustomer : myCustomers) {
			if (myCustomer.getState()==CustState.ReadyToOrder) {
				CurrentCustomer = myCustomer;
				break;
			}
		}
		//print("hi  "+this.state);
		waiterGui.DoGoToTable(CurrentCustomer.getCustomer(), CurrentCustomer.getTableNumber());
		//print("hihi  "+this.state);

		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		busy=true;
		this.state=AgentState.GetOrder;
		//print("hihihi  "+this.state);

		CurrentCustomer.setState(CustState.Ordered);
		//print("hi5  "+this.state + "   " + busy);

		CurrentCustomer.getCustomer().msgHereForOrder();
		print("Here for order");
	}
	
	public void TakeOrderToKitchen(MyCustomers current) {
		print("Taking order to chef");
		waiterGui.BringOrderToCook(current.getOrder());
		try {
			atCook.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		busy=true;
		this.state=AgentState.TakeOrderToCook;
		current.setState(CustState.WaitingForFood);
		cook.msgNewOrder(this,current.getTableNumber(), current.getOrder());
		waiterGui.DoLeaveCustomer();
		print("Chef has order");
	}

	
    public void DeliverFoodToTable(MyCustomers current) {
    	waiterGui.DoGoToCook();
    	try {
			atCook.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	waiterGui.BringFoodToCustomer(current.getCustomer(), current.getTableNumber(),current.getOrder());
    	//print("Order Ready " + atTable);

    	try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
		busy=true;
		this.state=AgentState.ServeFood;
		current.setState(CustState.Eating); 
		current.getCustomer().msgDeliveredFood();
    	waiterGui.DoLeaveCustomer();
    	print("Delivered Food");
    }

	public void PrepareTable() {
		print("Table empty");
		for (int i=0;i<myCustomers.size();i++) {
			if (myCustomers.get(i).getState()==CustState.Done) {
				CurrentCustomer = myCustomers.get(i);
				myCustomers.remove(i);
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
		busy=true;
		this.state=AgentState.CleanTable;
		host.msgLeavingTable(CurrentCustomer.getCustomer(), this);
		CurrentCustomer.setState(CustState.Gone);
		
		myCustomers.remove(CurrentCustomer);
		 
		print("Table clean");
		waiterGui.DoLeaveCustomer();
	  }
	
	public void PrepareTableEarly() {
		print("Table empty");
		for (int i=0;i<myCustomers.size();i++) {
			if (myCustomers.get(i).getState()==CustState.LeftEarly) {
				CurrentCustomer = myCustomers.get(i);
				myCustomers.remove(i);
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
		busy=true;
		this.state=AgentState.CleanTable;
		host.msgLeavingTable(CurrentCustomer.getCustomer(), this);
		CurrentCustomer.setState(CustState.Gone);
		
		myCustomers.remove(CurrentCustomer);
		 
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
	
	public class Menu {
		String optionOne = "Pizza";
		String optionTwo = "Steak";
		String optionThree = "Salad";
		String optionFour = "Chicken";
		double costOne = 8.99;
		double costTwo = 15.99;
		double costThree = 5.99;
		double costFour = 10.99;
		
		Menu() {
			
		}
		
		double GetCost(String choice) {
			if (choice == "Pizza") return costOne;
			else if (choice == "Steak") return costTwo;
			else if (choice == "Salad") return costThree;
			else return costFour;
		}
		
		String ChoosePizza() {
			return optionOne;
		}
		
		String ChooseSteak() {
			return optionTwo;
		}
		
		String ChooseSalad() {
			return optionThree;
		}
		
		String ChooseChicken() {
			return optionFour;
		}
	}
	
	public class MyCustomers {
		Customer customer;
		int tableNumber;
		String order;
		CustState currentState;
		Waiter waiter;
		double bill;
		Check CheckRepublic;
		boolean owed;

		public MyCustomers(Customer customer, int tableNumber) {
			this.tableNumber = tableNumber;
			this.customer = customer;
			this.waiter = customer.GetWaiter();
			owed = false;
		}
		
		void setOwed(boolean t) {
			owed = t;
		}
		
		boolean getOwed() {
			return owed;
		}
		
		void setCheck(Check k) {
			CheckRepublic = k;
		}
		
		Check getCheck() {
			return CheckRepublic;
		}
		
		void setState (CustState s) {
			currentState = s;
		}
		
		CustState getState () {
			return currentState;
		}
		
		double GetBill() {
			return bill;
		}
		
		void setTableNumber (int n) {
			tableNumber = n;
		}
		
		int getTableNumber () {
			return tableNumber;
		}
		
		void setOrder (String o) {
			order = o;
			bill = currentMenu.GetCost(o);
		}
		
		String getOrder () {
			return order;
		}
		
		Waiter getWaiter() {
			return waiter;
		}

		void setCustomer(Customer cust) {
			customer = cust;
		}

		Customer getCustomer() {
			return customer;
		}
	}

}
	

