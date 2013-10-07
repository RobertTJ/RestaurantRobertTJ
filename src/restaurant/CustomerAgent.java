package restaurant;

import restaurant.WaiterAgent.Menu;
import restaurant.gui.CustomerGui;
import restaurant.gui.RestaurantGui;
import agent.Agent;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Restaurant customer agent.
 */
public class CustomerAgent extends Agent {
	private String name;
	private String order = "Salad";
	private int hungerLevel = 5;        // determines length of meal
	Timer timer = new Timer();
	private CustomerGui customerGui;
	private Menu menu;

	// agent correspondents
	private HostAgent host;
	private WaiterAgent waiter;
	int select;

	//    private boolean isHungry = false; //hack for gui
	public enum AgentState
	{DoingNothing, WaitingInRestaurant, BeingSeated, Seated, Ordered, WaitingForFood, Eating, DoneEating, Leaving};
	private AgentState state = AgentState.DoingNothing;//The start state

	public enum AgentEvent 
	{none, gotHungry, followWaiter, seated, beingHelped, beingHelpedAgain, gotFood, doneEating, doneLeaving};
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
	}

	/**
	 * hack to establish connection to Host agent.
	 */
	public void setHost(HostAgent host) {
		this.host = host;
	}
	
	public void SetWaiter(WaiterAgent w) {
		this.waiter=w;
	}

	public String getCustomerName() {
		return name;
	}
	// Messages

	public void gotHungry() {//from animation
		print("I'm hungry");
		event = AgentEvent.gotHungry;
		stateChanged();
	}
	
	public void msgFollowMeToTable(WaiterAgent w, Menu m) {
		this.waiter=w;
		this.menu = m;
		//print("Received msgSitAtTable");
		event = AgentEvent.followWaiter;
		stateChanged();
	}

	public void msgAnimationFinishedGoToSeat() {
		//from animation
		event = AgentEvent.seated;
		stateChanged();
	}
	
	public void msgAnimationFinishedLeaveRestaurant() {
		//from animation
		event = AgentEvent.doneLeaving;
		stateChanged();
	}
	
	public void msgHereForNewOrder() {
		event = AgentEvent.beingHelpedAgain;
		stateChanged();
	}
	
	public void msgHereForOrder() {
		event = AgentEvent.beingHelped;
		stateChanged();
	}
	
	public void msgDeliveredFood() {
		event=AgentEvent.gotFood;
		stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		//	CustomerAgent is a finite state machine

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

	private void goToRestaurant() {
		Do("Going to restaurant");
		host.msgIWantFood(this);//send our instance, so he can respond to us
	}

	private void SitDown() {
		
		//Do("Being seated. Going to table");
		//customerGui.DoGoToSeat(1);//hack; only one table
		customerGui.DoGoToSeat(1);
	}
	
	private void PickUpAndPeruseMenu() {
	CustomerAgent temp = this;
		timer.schedule(new TimerTask() {
			Object cookies = 1;
			public void run() {
				//look at menu, call waiter when ready
				CallWaiter();
				stateChanged();
			}
		},
		10000);
	}
	
	public void CallWaiter() {
		waiter.msgReadyToOrder(this);
		print("I am ready to order");
	}
	
private void OrderNewFood() {
		
		Random generator = new Random();
		select++;
		if (select == 4) {
			select = 0;
		}
				
		//print("Randomed " + select);
		
		if (select == 0) {
			order = menu.ChooseOne();
		}
		else if (select == 1) {
			order = menu.ChooseTwo();
		}
		else if (select == 2) {
			order = menu.ChooseThree();
		}
		else {
			order = menu.ChooseFour();
		}
		print("I want " + order + " now");
		waiter.msgOrderFood(this, this.getOrder());
		//print("in cust execution?");
		event=null;
		stateChanged();
	}
	
	private void OrderFood() {
		print("Check me");
		Random generator = new Random();
		select = generator.nextInt(4);
		select = 0;
				
		//print("Randomed " + select);
		
		if (select == 0) {
			order = menu.ChooseOne();
		}
		else if (select == 1) {
			order = menu.ChooseTwo();
		}
		else if (select == 2) {
			order = menu.ChooseThree();
		}
		else {
			order = menu.ChooseFour();
		}
		print("I want " + order);
		waiter.msgOrderFood(this, this.getOrder());
		//print("in cust execution?");
		stateChanged();
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

