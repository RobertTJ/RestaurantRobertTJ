package restaurant;

import agent.Agent;
import restaurant.CustomerAgent.AgentState;
import restaurant.gui.HostGui;
import restaurant.gui.RestaurantGui;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Waiter;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class HostAgent extends Agent {
	static final int NTABLES = 3;//a global for the number of tables.
	//Notice that we implement waitingCustomers using ArrayList, but type it
	//with List semantics.
	public List<Customer> waitingCustomers
	= new ArrayList<Customer>();
	
	public List<Boolean> messaged = new ArrayList<Boolean>();
	
	public List<MyWaiters> allWaiters
	= new ArrayList<MyWaiters>();
	//public Vector<Integer> customersServed = new Vector<Integer>(10);
	public Collection<Table> tables;
	//note that tables is typed with Collection semantics.
	//Later we will see how it is implemented
	public RestaurantGui RestGUI;
	
	public enum AgentState
	{DoingNothing, SeatingCustomer};
	private AgentState state = AgentState.DoingNothing;//The start state

	private String name;

	public HostGui hostGui = null;
	
	private int LeastBusyWaiter=0;
	private int WaiterOnBreak=-1;
	
	public void setRestGui (RestaurantGui g) {
		RestGUI = g;
	}

	public HostAgent(String name) {
		super();

		this.name = name;
		// make some tables
		tables = new ArrayList<Table>(NTABLES);
		for (int ix = 1; ix <= NTABLES; ix++) {
			tables.add(new Table(ix));//how you add to a collections
		}
	}

	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}

	public List getWaitingCustomers() {
		return waitingCustomers;
	}

	public Collection getTables() {
		return tables;
	}
	// Messages
	
	public void msgNewWaiter(Waiter w) {
		allWaiters.add(new MyWaiters(w));
		
		stateChanged();
	}
	
	public void msgImFree(Waiter w) {
		stateChanged();
	}

	public void msgIWantFood(Customer cust) {
		waitingCustomers.add(cust);
		messaged.add(false);
		stateChanged();
	}

	public void msgLeavingTable(Customer cust, Waiter w) { //TO DO - add waiter pass to call myWaiters customer--
		for (Table table : tables) {
			if (table.getOccupant() == cust) {
				print(cust + " leaving " + table);
				table.setUnoccupied();
			}
		}
		
		for ( int i =0; i<allWaiters.size();i++) {
			if (w == allWaiters.get(i).GetWaiter()) {
				allWaiters.get(i).CustomerDone();
			}
		}
		stateChanged();

	}
	
	public void msgIWantABreak(Waiter w) {
		for ( int i =0; i<allWaiters.size();i++) {
			if (w == allWaiters.get(i).GetWaiter()) {
				allWaiters.get(i).IWantABreak();
				print(allWaiters.get(i).GetWaiter().getName() + " wants a break");
			}
		}
		stateChanged();
	}
	
	public void msgBackFromBreak(Waiter w) {
		for ( int i =0; i<allWaiters.size();i++) {
			if (w == allWaiters.get(i).GetWaiter()) {
				allWaiters.get(i).BackFromBreak();
				print(allWaiters.get(i).GetWaiter().getName() + " is back from break");
				WaiterOnBreak = -1;
			}
		}
		stateChanged();
	}
	
	public void msgIWontWait(Customer c) {
		print(c + " does not want to wait and has left");
		waitingCustomers.remove(c);
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
		//if (WaiterOnBreak)
		
		for ( int i =0; i<allWaiters.size();i++) {
			if (allWaiters.get(i).GetWantABreak() == true && allWaiters.get(i).GetNumberOfCustomers()==0) {
				if (WaiterOnBreak == -1 && allWaiters.size()>1) {
					Break(i);
					return true;
				}
			}
		}
		
		
		if(!allWaiters.isEmpty())
		{
		for (Table table : tables) {
			if (!table.isOccupied()) {
				if (!waitingCustomers.isEmpty()) {
					LeastBusyWaiter = 0;
					if (WaiterOnBreak == 0) LeastBusyWaiter=1;
					for ( int i =0; i<allWaiters.size();i++) {
						if (WaiterOnBreak == i) i++;
						if (allWaiters.get(i).GetNumberOfCustomers() < allWaiters.get(LeastBusyWaiter).GetNumberOfCustomers() && i != WaiterOnBreak) {
							LeastBusyWaiter = i;
						}
					}
					
					
					
					seatCustomer( allWaiters.get(LeastBusyWaiter).GetWaiter(), waitingCustomers.get(0), table);
					allWaiters.get(LeastBusyWaiter).AddCustomer();
					
					
					// increments around the waiter list, assigned each subsequent customer to 
					//the next waiter in allWaiters and loops back around to the first at the end

					return true;//return true to the abstract agent to re-invoke the scheduler.	
				}
			}
		}
		}
		
		
		if(!waitingCustomers.isEmpty()){
			for (Table table : tables) {
				if (!table.isOccupied()) {
					return false;
				}
			}
			
			for (int i = 0; i<waitingCustomers.size();i++) {
				if (messaged.get(i) == false) {
					messaged.set(i, true);
					RestaurantFull(i);
				}
			}
		}
			
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions
	
	private void Break(int i) {
		allWaiters.get(i).TakeABreak();
		allWaiters.get(i).GetWaiter().msgTakeABreak();
		WaiterOnBreak=i;
		RestGUI.OnABreak(allWaiters.get(i).GetWaiter());
		print(allWaiters.get(i).GetWaiter().getName() + " is taking a break");
	}

	private void seatCustomer(Waiter waiter, Customer customer, Table table) {
		waiter.msgNewCustomerToSeat(customer, table.getTable());		
		print(waiter.getName() + " seating " + customer + " at " + table);
		
		table.setOccupant(customer);
		waitingCustomers.remove(customer);
		messaged.remove(0);
	}
	
	private void RestaurantFull(int i) {
		waitingCustomers.get(i).msgRestaurantFull();
	}


	//utilities

	public void setGui(HostGui gui) {
		hostGui = gui;
	}

	public HostGui getGui() {
		return hostGui;
	}
	
	private class MyWaiters {
		Waiter waiter;
		boolean WantABreak;
		boolean OnBreak;
		int NumberOfCustomers;
		
		MyWaiters (Waiter w) {
			waiter = w;
			WantABreak =false;
			OnBreak = false;
			NumberOfCustomers = 0;
		}
		
		public Waiter GetWaiter() {
			return waiter;
		}
		
		public int GetNumberOfCustomers() {
			return NumberOfCustomers;
		}
		
		public boolean GetOnBreak() {
			return OnBreak;
		}
		
		public boolean GetWantABreak() {
			return WantABreak;
		}
		
		public void IWantABreak() {
			WantABreak = true;
		}
		
		public void TakeABreak() {
			OnBreak = true;
			WantABreak = false;
		}
		
		public void BackFromBreak() {
			OnBreak = false;
		}
		
		public void AddCustomer() {
			NumberOfCustomers++;
		}
		
		public void CustomerDone() {
			NumberOfCustomers--;
		}
	}
	

	private class Table {
		Customer occupiedBy;
		int tableNumber;

		Table(int tableNumber) {
			this.tableNumber = tableNumber;
		}
		
		public int getTable() {
			return tableNumber;
		}

		void setOccupant(Customer cust) {
			occupiedBy = cust;
		}

		void setUnoccupied() {
			occupiedBy = null;
		}

		Customer getOccupant() {
			return occupiedBy;
		}

		boolean isOccupied() {
			return occupiedBy != null;
		}

		public String toString() {
			return "table " + tableNumber;
		}
	}
}

