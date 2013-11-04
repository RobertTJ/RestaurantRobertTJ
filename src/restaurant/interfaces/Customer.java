package restaurant.interfaces;

import java.util.Random;

import restaurant.WaiterAgent;
import restaurant.CashierAgent.Check;
import restaurant.CustomerAgent.AgentEvent;
import restaurant.WaiterAgent.Menu;
import restaurant.gui.CustomerGui;

/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public interface Customer {
	
	public abstract Waiter GetWaiter();
	
	public abstract CustomerGui getGui();
	
	public abstract void gotHungry();
	
	public abstract void msgRestaurantFull();
	
	public abstract void msgHereIsYourBill(double k);
	
	public abstract void msgFollowMeToTable(Waiter w, Menu m);

	public abstract void msgAnimationFinishedGoToSeat();
	
	public abstract void msgAnimationFinishedLeaveRestaurant();
	
	public abstract void msgHereForNewOrder();
	
	public abstract void msgHereForOrder();
	
	public abstract void msgDeliveredFood();

}