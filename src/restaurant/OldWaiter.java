package restaurant;

import agent.Agent;
import restaurant.gui.HostGui;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Restaurant Waiter Agent
 */

public class OldWaiter extends Agent {
	
	
	public String state="free";
	
	private HostAgent host;
	
	

	private String name;
	private Semaphore atTable = new Semaphore(0,true);

	public HostGui hostGui = null;

	public void setHost(HostAgent host) {
		this.host = host;
	}
	
	public OldWaiter(String name) {
		super();

		this.name = name;
		// make some tables
		//host.tables = new ArrayList<Table>(NTABLES);
		//for (int ix = 1; ix <= NTABLES; ix++) {
			//tables.add(new Table(ix));//how you add to a collections
		//}
	}

	public String getMaitreDName() {
		return host.getName();
	}

	public String getName() {
		return name;
	}

	public List getWaitingCustomers() {
		return host.waitingCustomers;
	}

	public Collection getTables() {
		return host.tables;
	}
	// Messages

	/*public void msgLeavingTable(CustomerAgent cust) {
		for (Table table : host.tables) {
			if (table.getOccupant() == cust) {
				print(cust + " leaving " + table);
				table.setUnoccupied();
				stateChanged();
			}
		}
	}*/

	/*public void msgAtTable() {//from animation
		//print("msgAtTable() called");
		atTable.release();// = true;
		stateChanged();
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
		if (state=="free"){
			
		}

		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions

	public void msgSeatCustomer(CustomerAgent customer, int table) {
		/*customer.msgSitAtTable();
		//DoSeatCustomer(customer, table);
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//table.setOccupant(customer);
	//	waitingCustomers.remove(customer);
	//	hostGui.DoLeaveCustomer();*/
	}

	// The animation DoXYZ() routines
	private void DoSeatCustomer(CustomerAgent customer, Table table) {
		//Notice how we print "customer" directly. It's toString method will do it.
		//Same with "table"
		print("Seating " + customer + " at " + table);
		//hostGui.DoBringToTable(customer); 

	}

	//utilities

	public void setGui(HostGui gui) {
		hostGui = gui;
	}

	public HostGui getGui() {
		return hostGui;
	}

	private class Table {
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
	}
}

