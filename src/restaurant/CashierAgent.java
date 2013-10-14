package restaurant;

import agent.Agent;
import restaurant.gui.HostGui;
import restaurant.HostAgent;
import restaurant.WaiterAgent.MyCustomers;

import java.util.*;
import java.util.concurrent.Semaphore;

import java.util.Timer;
import java.util.TimerTask;

public class CashierAgent extends Agent {

	private String name;
	HostAgent host;
	
	public CashierAgent(String name) {
		super();

		this.name = name;
	}
	
	public List<MyCustomers> Customers
	= new ArrayList<MyCustomers>();
	
	public void addCustomer(MyCustomers c) {
		Customers.add(c);
		stateChanged();
	}
	
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
	
	public void msgCheckPlease(MyCustomers C){
		boolean herebefore = false;
		for (MyCustomers cust : Customers) {
			if (cust == C) {
				cust.setOwed(false);
				herebefore=true;
			}
		}
		if (herebefore==false) Customers.add(C);
		stateChanged();
	}
	
	public void msgPayingMyBill(CustomerAgent c) {
		print (c + " has payed his bill");
		for (MyCustomers cust : Customers) {
			if (cust.getCustomer() == c) {
				cust.setOwed(false);
			}
		}
	}
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
	
		if(!Customers.isEmpty()) {
			for (MyCustomers cust : Customers) {
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
	
	
	private void GiveBillToWaiter(MyCustomers CurrentCustomer) {
		print("Here is the check for " + CurrentCustomer.getCustomer());
		Check ThisCheck = new Check(CurrentCustomer.GetBill());
		CurrentCustomer.getWaiter().msgHereIsCheck(CurrentCustomer.getCustomer(), ThisCheck);
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
	
	
}

