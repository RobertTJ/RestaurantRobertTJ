package restaurant;

import agent.Agent;
import restaurant.gui.HostGui;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Host;
import restaurant.interfaces.Market;
import restaurant.interfaces.Waiter;
import restaurant.HostAgent;
import restaurant.WaiterAgent.CustState;
import restaurant.WaiterAgent.MyCustomers;
import restaurant.MarketAgent;

import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.Timer;
import java.util.TimerTask;

public class CashierAgent extends Agent implements Cashier{

	private String name;
	Host host;
	
	public double CASHMONEY = 50;
	
	public CashierAgent(String name) {
		super();

		this.name = name;
	}
	
	public List<DebtedCustomers> Customers
	= Collections.synchronizedList(new ArrayList<DebtedCustomers>());
	
	public List<MarketBills> Markets
	= Collections.synchronizedList(new ArrayList<MarketBills>());
	
	public void addMarket(Market m) {
		Markets.add(new MarketBills(m));
	}
	
	public void addCustomer(DebtedCustomers c) {
		Customers.add(c);
		stateChanged();
	}
	
	public void SetHost(Host h) {
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
	
	public void msgFoodBill(Market m, double b) {
		synchronized(Markets) {
			
		for (MarketBills market : Markets) {
			if (market.GetMarket() == m) {
				market.SetBill(b);
			}
		}
		}
		stateChanged();
	}
	
	@Override
	public void msgCheckPlease(Customer C, int TableNumber, double b){
		boolean herebefore = false;
		synchronized(Customers) {
		for (DebtedCustomers cust : Customers) {
			if (cust.getCustomer() == C) {
				cust.setOwed(false);
				//cust.
				herebefore=true;
			}
		}
		}
		//if (herebefore==false)
			Customers.add(new DebtedCustomers(C,TableNumber,b));
		stateChanged();
	}
	
	@Override
	public void msgPayingMyBill(Customer c, double b) {
		print (c + " has payed");
		CASHMONEY = CASHMONEY + b;
	}
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
	
		if(!Customers.isEmpty()) {
			synchronized(Customers) {
			for (DebtedCustomers cust : Customers) {
				if (cust.getOwed()==false) {
					GiveBillToWaiter(cust);
					cust.setOwed(true);
					return true;
				}
			}
			}
		}
		synchronized(Markets){
		for (MarketBills market : Markets) {
			if (market.GetBill() != 0.00) {
				PayMarket(market);
			}
		}
		}

		return false;
	}

	// Actions
	
	public void PayMarket(MarketBills market) {
		
		market.GetMarket().msgPayingBill(market.GetBill());
		CASHMONEY = CASHMONEY - market.GetBill();
		market.SetBill(0.00);
		if (CASHMONEY < 0.00) {
			print("We are now terribly in debt to awful people.  If we don't become flush soon we'll be shut down!");
			
		}
	}
	
	private void GiveBillToWaiter(DebtedCustomers CurrentCustomer) {
		print("Here is the check for " + CurrentCustomer.getCustomer());
		Check ThisCheck = new Check(CurrentCustomer.GetBill());
		CurrentCustomer.getWaiter().msgHereIsCheck(CurrentCustomer.getCustomer(), ThisCheck);
		synchronized(Customers) {
		for (int i=0;i<Customers.size();i++) {
			if (Customers.get(i) == CurrentCustomer) {
				Customers.remove(i);
			}
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
	
	public class MarketBills {
		Market market;
		double owed;
		
		
		public MarketBills (Market m) {
			market = m;
		}
		
		public Market GetMarket() {
			return market;
		}
		
		public void SetBill(double b) {
			owed = b;
		}
		
		public double GetBill() {
			return owed;
		}
		
	}
	
	public class DebtedCustomers {
		public Customer customer;
		int tableNumber;
		String order;
		CustState currentState;
		Waiter waiter;
		public double bill;
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

