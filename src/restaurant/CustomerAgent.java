package restaurant;

import restaurant.CashierAgent.Check;
import restaurant.WaiterAgent.Menu;
import restaurant.gui.CustomerGui;
import restaurant.gui.RestaurantGui;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Waiter;
import agent.Agent;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Restaurant customer agent.
 */
public class CustomerAgent extends Agent implements Customer {
	private String name;
	private String order = "Salad";
	private int hungerLevel = 5;        // determines length of meal
	Timer timer = new Timer();
	private CustomerGui customerGui;
	private Menu menu;
	private double Wallet;
	private double bill=0.00;

	// agent correspondents
	private HostAgent host;
	private Waiter waiter = null;
	private Cashier cashier;
	int select;
	int payday=0;

	//    private boolean isHungry = false; //hack for gui
	public enum AgentState
	{DoingNothing, WaitingInRestaurant, BeingSeated, Seated, Ordered, WaitingForFood, Eating, DoneEating, WaitingForCheck, Leaving};
	private AgentState state = AgentState.DoingNothing;//The start state

	public enum AgentEvent 
	{LeaveEarly, none, gotHungry, followWaiter, seated, beingHelped, beingHelpedAgain, gotFood, doneEating, GotCheck, doneLeaving};
	AgentEvent event = AgentEvent.none;

	/**
	 * Constructor for CustomerAgent class
	 *
	 * @param name name of the customer
	 * @param gui  reference to the customergui so the customer can send it messages
	 */
	public CustomerAgent(String name){
		super();
		this.name = name;
		Wallet = 3.00;
	}

	/**
	 * hack to establish connection to Host agent.
	 */
	
	public void setHost(HostAgent host) {
		this.host = host;
	}
	
	public void SetCashier(Cashier a) {
		this.cashier = a;
	}
	
	public void SetWaiter(Waiter w) {
		this.waiter=w;
	}
	
	public Waiter GetWaiter() {
		return waiter;
	}

	public String getCustomerName() {
		return name;
	}
	// Messages
	@Override
	public void gotHungry() {//from animation
		print("I'm hungry");
		if(payday==0) {
			Wallet=Wallet + 12;	
			print("Payday! Whoooo!");
		}
		payday++; 
		if (payday == 2 )payday=0;

		event = AgentEvent.gotHungry;
		stateChanged();
	}
	@Override
	public void msgRestaurantFull() {
		Random generator = new Random();
		select = generator.nextInt(2);
		if(select == 1) {
			event = AgentEvent.LeaveEarly;
			stateChanged();
		}
	}
	@Override
	public void msgHereIsYourBill(Check k) {
		if (k!= null) bill = k.GetBill();
		event = AgentEvent.GotCheck;
		stateChanged();
	}
	@Override
	public void msgFollowMeToTable(Waiter w, Menu m) {
		this.waiter=w;
		this.menu = m;
		//print("Received msgSitAtTable");
		event = AgentEvent.followWaiter;
		stateChanged();
	}
	@Override
	public void msgAnimationFinishedGoToSeat() {
		//from animation
		event = AgentEvent.seated;
		stateChanged();
	}
	@Override
	public void msgAnimationFinishedLeaveRestaurant() {
		//from animation
		event = AgentEvent.doneLeaving;
		stateChanged();
	}
	@Override
	public void msgHereForNewOrder() {
		event = AgentEvent.beingHelpedAgain;
		stateChanged();
	}
	@Override
	public void msgHereForOrder() {
		event = AgentEvent.beingHelped;
		stateChanged();
	}
	@Override
	public void msgDeliveredFood() {
		event=AgentEvent.gotFood;
		stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		//	CustomerAgent is a finite state machine

		if (event == AgentEvent.LeaveEarly) {
			state = AgentState.DoingNothing;
			Leave();
		}
		
		if (state == AgentState.DoingNothing && event == AgentEvent.gotHungry ){
			state = AgentState.WaitingInRestaurant;
			goToRestaurant();
			return true;
		}
		if (state == AgentState.WaitingInRestaurant && event == AgentEvent.followWaiter ){
			state = AgentState.BeingSeated;
			SitDown();
			return true;
		}
		if (state == AgentState.BeingSeated && event == AgentEvent.seated){
			state = AgentState.Seated;
			PickUpAndPeruseMenu();
			return true;
		}
		if (state == AgentState.Seated && event == AgentEvent.beingHelped) {
			//print("In customer?");
			state=AgentState.Ordered;
			OrderFood();
			return true;
		}
		if ((state == AgentState.Ordered || state == AgentState.WaitingForFood) && event == AgentEvent.beingHelpedAgain) {
			state = AgentState.WaitingForFood;
			OrderNewFood();
			return true;
		}
		if ((state == AgentState.Ordered || state == AgentState.WaitingForFood) && event == AgentEvent.gotFood) {
			state = AgentState.Eating;
			EatFood();
			return true;
		}
		
		if (state == AgentState.Eating && event == AgentEvent.doneEating){
			state = AgentState.WaitingForCheck;
			AskForCheck();
			return true;
		}
		
		if (state == AgentState.WaitingForCheck && event == AgentEvent.GotCheck){
			state = AgentState.Leaving;
			leaveTable();
			return true;
		}
		if (state == AgentState.Leaving && event == AgentEvent.doneLeaving){
			state = AgentState.DoingNothing;
			//no action
			return true;
		}
		return false;
	}

	// Actions
	
	private void Leave() {
		print("I'm leaving");
		if (waiter != null) {
			waiter.msgOutOfHere(this);
		}
		else {
			host.msgIWontWait(this);
		}
		state=AgentState.DoingNothing;
		customerGui.DoExitRestaurant();
		stateChanged();
	}
	
	private void AskForCheck() {
		waiter.msgCheckPlease(this);
	}

	private void goToRestaurant() {
		Do("Going to restaurant");
		if ( bill != 0.00 && Wallet > bill) {
			//if money is owed, pay
			cashier.msgPayingMyBill(this, bill);
			Wallet = Wallet - bill;
			bill=0.00;
			print("I have $" + Wallet + " left");
			host.msgIWantFood(this);
		}
		else if (bill != 0.00 && Wallet < bill) {
			//if money is owed and can't pay
			print("I owe the restaurant money, guess I have to wait here");
			//start timer/dishes stuff
		}
		else {
			host.msgIWantFood(this);//send our instance, so he can respond to us
		}
	}

	private void SitDown() {
		
		//Do("Being seated. Going to table");
		//customerGui.DoGoToSeat(1);//hack; only one table
		customerGui.DoGoToSeat(1);
	}
	
	private void PickUpAndPeruseMenu() {
		timer.schedule(new TimerTask() {
			Object cookies = 1;
			public void run() {
				//look at menu, call waiter when ready
				CallWaiter();
				stateChanged();
			}
		},
		3000);
	}
	
	public void CallWaiter() {
		waiter.msgReadyToOrder(this);
		print("I am ready to order");
	}
	
	private void OrderNewFood() {
		
		Random generator = new Random();
		if ( Wallet>= 9 || Wallet<6){
			if (select == 0) {
				select ++;
			}
			else select--;
			
			if (select == 0) {
				order = menu.ChooseChicken();
			}
			else if (select == 1) {
				order = menu.ChoosePizza();
			}
			else if (select == 2) {
				order = menu.ChooseChicken();
			}
			else {
				order = menu.ChooseSteak();
			}
			print("I want " + order);
			waiter.msgOrderFood(this, this.getOrder());
			//print("in cust execution?");
			stateChanged();
		}
		
		else if (Wallet>=6 && Wallet<9) {
			print("I can't afford anything");
			this.Leave();	
		}
	}
	
	private void OrderFood() {
		//print("Check me");
		Random generator = new Random();
		
		if ( Wallet>= 6){
			if (Wallet>=16) {
				select = generator.nextInt(4);
			}
			else if (Wallet>=11) {
				select = generator.nextInt(3);
			}
			else if (Wallet>=9) {
				select = generator.nextInt(2);
			}
			else if (Wallet>=6) {
				select = 0;
			}
			
			if (select == 0) {
				order = menu.ChooseChicken();
			}
			else if (select == 1) {
				order = menu.ChoosePizza();
			}
			else if (select == 2) {
				order = menu.ChooseChicken();
			}
			else {
				order = menu.ChooseSteak();
			}
			print("I want " + order);
			waiter.msgOrderFood(this, this.getOrder());
			//print("in cust execution?");
			stateChanged();
		}
		
		else {
			select = generator.nextInt(2);
			if (select == 0) {
				print("I can't afford anything");
				//stateChanged();
				this.Leave();	
			}
			else {
				print("I can't afford anything.  Guess I'll dine and dash.");
				
				select = generator.nextInt(4);
				
				if (select == 0) {
					order = menu.ChooseChicken();
				}
				else if (select == 1) {
					order = menu.ChoosePizza();
				}
				else if (select == 2) {
					order = menu.ChooseChicken();
				}
				else {
					order = menu.ChooseSteak();
				}
				print("I want " + order);
				waiter.msgOrderFood(this, this.getOrder());
				stateChanged();
			}
		}
	}

	private void EatFood() {
		Do("Eating Food");
		customerGui.EatTime(order);
		//This next complicated line creates and starts a timer thread.
		//We schedule a deadline of getHungerLevel()*1000 milliseconds.
		//When that time elapses, it will call back to the run routine
		//located in the anonymous class created right there inline:
		//TimerTask is an interface that we implement right there inline.
		//Since Java does not all us to pass functions, only objects.
		//So, we use Java syntactic mechanism to create an
		//anonymous inner class that has the public method run() in it.
		timer.schedule(new TimerTask() {
			Object cookie = 1;
			public void run() {
				//print("Done eating, cookie=" + cookie);
				print("Done eating");
				event = AgentEvent.doneEating;
				//isHungry = false;
				stateChanged();
			}
		},
		5000);//getHungerLevel() * 1000);//how long to wait before running task
	}

	private void leaveTable() {
		Do("Leaving.");
		if (Wallet>=bill) {
			cashier.msgPayingMyBill(this, bill);
			Wallet = Wallet - bill;
			bill = 0.00;
			print("I have $" + Wallet + " left");
			
		}
		waiter.msgLeavingTable(this);
		customerGui.DoExitRestaurant();
	}

	// Accessors, etc.

	public String getName() {
		return name;
	}
	
	public String getOrder() {
		return order;
	}
	
	public int getHungerLevel() {
		return hungerLevel;
	}

	public void setHungerLevel(int hungerLevel) {
		this.hungerLevel = hungerLevel;
		//could be a state change. Maybe you don't
		//need to eat until hunger lever is > 5?
	}

	public String toString() {
		return "customer " + getName();
	}

	public void setGui(CustomerGui g) {
		customerGui = g;
	}

	public CustomerGui getGui() {
		return customerGui;
	}
	
	
	
}

