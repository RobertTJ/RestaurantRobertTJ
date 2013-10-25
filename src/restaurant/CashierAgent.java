package restaurant;

import agent.Agent;
import restaurant.gui.HostGui;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Waiter;
import restaurant.HostAgent;
import restaurant.WaiterAgent.CustState;
import restaurant.WaiterAgent.MyCustomers;

import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.Timer;
import java.util.TimerTask;

public class CashierAgent extends Agent implements Cashier{

	private String name;
	HostAgent host;
	
	public CashierAgent(String name) {
		super();

		this.name = name;
	}
	
	public List<DebtedCustomers> Customers
	= new ArrayList<DebtedCustomers>();
	
	public void addCustomer(DebtedCustomers c) {
		Customers.add(c);
		stateChanged();
	}
	
	public void SetHost(HostAgent h) {
		this.host=h;
	}
	
	public String getMaitreDName() {
		return name;
	}
	
	//public enum PayState {payed, owed, other

	public String getName() {
		return name;
	}

	// Messages
	
	@Override
	public void msgCheckPlease(Customer C, int TableNumber, double b){
		boolean herebefore = false;
		for (DebtedCustomers cust : Customers) {
			if (cust.getCustomer() == C) {
				cust.setOwed(false);
				//cust.
				herebefore=true;
			}
		}
		if (herebefore==false) Customers.add(new DebtedCustomers(C,TableNumber,b));
		stateChanged();
	}
	
	@Override
	public void msgPayingMyBill(Customer c) {
		print (c + " has payed");
		
	}
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
	
		if(!Customers.isEmpty()) {
			for (DebtedCustomers cust : Customers) {
				if (cust.getOwed()==false) {
					GiveBillToWaiter(cust);
					cust.setOwed(true);
					return true;
				}
			}
			
			return false;
		}

		return false;
	}

	// Actions
	
	
	private void GiveBillToWaiter(DebtedCustomers CurrentCustomer) {
		print("Here is the check for " + CurrentCustomer.getCustomer());
		Check ThisCheck = new Check(CurrentCustomer.GetBill());
		CurrentCustomer.getWaiter().msgHereIsCheck(CurrentCustomer.getCustomer(), ThisCheck);
		for (int i=0;i<Customers.size();i++) {
			if (Customers.get(i) == CurrentCustomer) {
				Customers.remove(i);
			}
		}
		stateChanged();
	}
	
	
	//  Classes
	
	public class Check {
		double bill;
		
		Check(double b) {
			bill = b;
		}
		
		double GetBill() {
			return bill;
		}
	}
	
	public class DebtedCustomers {
		Customer customer;
		int tableNumber;
		String order;
		CustState currentState;
		Waiter waiter;
		double bill;
		Check CheckRepublic;
		boolean owed;

		public DebtedCustomers(Customer customer, int tableNumber, double b) {
			this.tableNumber = tableNumber;
			this.customer = customer;
			this.waiter = customer.GetWaiter();
			owed = false;
			bill = b;
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

