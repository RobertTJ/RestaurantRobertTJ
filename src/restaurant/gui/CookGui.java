package restaurant.gui;


import restaurant.CookAgent;
import restaurant.CustomerAgent;
import restaurant.HostAgent;
import restaurant.WaiterAgent;
import restaurant.interfaces.Customer;

import java.awt.*;
import java.util.concurrent.Semaphore;

public class CookGui implements Gui {

    private CookAgent agent = null;
    private String o;
    private enum orderState {noOrder, InFridge, TakingFoodToCounter, OnCounter, OnGrill, Cooked, TakingFoodToGrill};
    private enum Happenings {nothing, neworder, tocounter, tocook};
    
   // orderState OrderState = orderState.noOrder;

    private int xPos = 650, yPos = 50;//default cook position
    private int xDestination = 650, yDestination = 50;//default start position
    private int xHome = 650, yHome = 50;
    private int shifttwenty = 20;
    private int xWait  = -20, yWait = -20;
    
    private Semaphore atFridge = new Semaphore(0,true);
	private Semaphore atCounter = new Semaphore(0,true);
	private Semaphore atGrill = new Semaphore(0,true);
	private Semaphore atCenter = new Semaphore(0,true);

    public static final int xCounter = 600;
    public static final int yCounter = 50;
    
    public static final int xGrill = 650;
    public static final int yGrill = 20;
    
    public static final int xFridge = 730;
    public static final int yFridge = 50;
    
    private Orders OrderOne = new Orders("Random", 1);
    private Orders OrderTwo = new Orders("Random", 2);
    private Orders OrderThree = new Orders("Random", 3);

    public CookGui(CookAgent agent) {
        this.agent = agent;
    }
    
    
    public void setHomePosition(int x, int y) {
    	xHome=x;
    	yHome=y;
    	xDestination = xHome;
    	yDestination = yHome;
    }
    
    public void updatePosition() {
        if (xPos < xDestination)
            xPos=xPos+2;
        else if (xPos > xDestination)
            xPos=xPos-2;

        if (yPos < yDestination)
            yPos=yPos+2;
        else if (yPos > yDestination)
            yPos=yPos-2;

        
        	if (xPos == xDestination && yPos == yDestination
        		& (xDestination ==xHome) & (yDestination == yHome)) {
           agent.msgAtCenter();
           atCenter.release();
        	}
           else if (xPos == xDestination && yPos == yDestination
        		& (xDestination ==xGrill) & (yDestination == yGrill)) {
           agent.msgAtGrill();
           atGrill.release();
           
            if (OrderOne.GetState() == orderState.TakingFoodToGrill) {
       			OrderOne.SetState(orderState.OnGrill);
       		}
       		else if (OrderTwo.GetState() == orderState.TakingFoodToGrill) {
       			OrderTwo.SetState(orderState.OnGrill);
       		}
       		else if (OrderThree.GetState() == orderState.TakingFoodToGrill) {
       			OrderThree.SetState(orderState.OnGrill);
       		}
       		else if (OrderOne.GetState() == orderState.Cooked) {
       			OrderOne.SetState(orderState.TakingFoodToCounter);
       		}
       		else if (OrderTwo.GetState() == orderState.Cooked) {
       			OrderTwo.SetState(orderState.TakingFoodToCounter);
       		}
       		else if (OrderThree.GetState() == orderState.Cooked) {
       			OrderThree.SetState(orderState.TakingFoodToCounter);
       		}
        }
        else if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xCounter) & (yDestination == yCounter)) {
           agent.msgAtCounter();
           atCounter.release();

            if (OrderOne.GetState() == orderState.TakingFoodToCounter) {
      			OrderOne.SetState(orderState.OnCounter);
      		}
      		else if (OrderTwo.GetState() == orderState.TakingFoodToCounter) {
      			OrderTwo.SetState(orderState.OnCounter);
      		}
      		else if (OrderThree.GetState() == orderState.TakingFoodToCounter) {
      			OrderThree.SetState(orderState.OnCounter);
      		}
           
        }
        else if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xFridge-40) & (yDestination == yFridge)) {
           agent.msgAtFridge();   
           atFridge.release();
        }

    }
    
    public void DoGoToCounter(String o, int t) {
    	if (t == 1) {
    		OrderOne.SetState(orderState.TakingFoodToCounter);
    	}
    	else if (t == 2) {
    		OrderTwo.SetState(orderState.TakingFoodToCounter);
    	}
    	else if (t == 3) {
    		OrderThree.SetState(orderState.TakingFoodToCounter);
    	}
    	
    	xDestination=xCounter;//+20;
    	yDestination=yCounter;
    	try {
			atCounter.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
   
    
    public void PickedUp(int t) {
    	if (t == 1 ) {
    		OrderOne.SetState(orderState.noOrder);
    	}
    	else if (t == 2 ) {
    		OrderTwo.SetState(orderState.noOrder);
    	}
    	else if (t == 3 ) {
    		OrderThree.SetState(orderState.noOrder);
    	}
    }
    
    public void DoGoToGrill(int t) {
    	if (t == 1 ) {
    		OrderOne.SetState(orderState.TakingFoodToGrill);
    	}
    	else if (t == 2) {
    		OrderTwo.SetState(orderState.TakingFoodToGrill);
    	}
    	else if (t == 3 ) {
    		OrderThree.SetState(orderState.TakingFoodToGrill);
    	}
    	
    	xDestination=xGrill;
    	yDestination=yGrill;//+20;
    	try {
			atGrill.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void DoGoToGetFood(int t) {
    	if (t == 1 ) {
    		OrderOne.SetState(orderState.Cooked);
    	}
    	else if (t == 2 ) {
    		OrderTwo.SetState(orderState.Cooked);
    	}
    	else if (t == 3) {
    		OrderThree.SetState(orderState.Cooked);
    	}
    	
    	xDestination=xGrill;
    	yDestination=yGrill;//+20;
    	try {
			atGrill.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    }
    
    public void DoGoToFridge(String o, int t) {
    	if (t == 1) {
    		OrderOne.SetState(orderState.InFridge);
    		OrderOne.SetOrder(o);    		
    	}
    	else if (t == 2) {
    		OrderTwo.SetState(orderState.InFridge);
    		OrderTwo.SetOrder(o);    		
    	}
    	else if (t == 3) {
    		OrderThree.SetState(orderState.InFridge);
    		OrderThree.SetOrder(o);    		
    	}
    	
    	xDestination=xFridge-40;
    	yDestination=yFridge;
    	try {
			atFridge.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(xPos, yPos, shifttwenty, shifttwenty);
        g.setColor(Color.ORANGE);
        g.fillRect(xCounter-shifttwenty, yCounter-shifttwenty, shifttwenty, shifttwenty*3);
        g.setColor(Color.YELLOW);
        g.fillRect(xGrill-shifttwenty, yGrill-shifttwenty, shifttwenty*3, shifttwenty);
        g.setColor(Color.BLUE);
        g.fillRect(xFridge-shifttwenty, yFridge-shifttwenty, shifttwenty, shifttwenty*3);
		g.setColor(Color.BLACK);
		
		if (OrderOne.GetState() == orderState.Cooked || OrderOne.GetState() == orderState.OnCounter||OrderOne.GetState() == orderState.TakingFoodToCounter) {
			g.setColor(Color.BLACK);
		}
		else {
			g.setColor(Color.RED);
		}
		if (OrderOne.GetState()==orderState.TakingFoodToCounter || OrderOne.GetState()==orderState.TakingFoodToGrill) {
			g.drawString(OrderOne.GetOrder(), xPos, yPos);
		}
		else if (OrderOne.GetState()==orderState.OnGrill || OrderOne.GetState()==orderState.Cooked) {
			g.drawString(OrderOne.GetOrder(), xGrill-20, yGrill);
		}
		else if (OrderOne.GetState()==orderState.OnCounter) {
			g.drawString(OrderOne.GetOrder(), xCounter-20, yCounter-5);
		}
		
		if (OrderTwo.GetState() == orderState.Cooked || OrderTwo.GetState() == orderState.OnCounter||OrderTwo.GetState() == orderState.TakingFoodToCounter) {
			g.setColor(Color.BLACK);
		}
		else {
			g.setColor(Color.RED);
		}
		if (OrderTwo.GetState()==orderState.TakingFoodToCounter || OrderTwo.GetState()==orderState.TakingFoodToGrill) {
			g.drawString(OrderTwo.GetOrder(), xPos, yPos);
		}
		else if (OrderTwo.GetState()==orderState.OnGrill || OrderTwo.GetState()==orderState.Cooked) {
			g.drawString(OrderTwo.GetOrder(), xGrill, yGrill);
		}
		else if (OrderTwo.GetState()==orderState.OnCounter) {
			g.drawString(OrderTwo.GetOrder(), xCounter-20, yCounter+20);
		}
		
		if (OrderThree.GetState() == orderState.Cooked || OrderThree.GetState() == orderState.OnCounter||OrderThree.GetState() == orderState.TakingFoodToCounter) {
			g.setColor(Color.BLACK);
		}
		else {
			g.setColor(Color.RED);
		}
		if (OrderThree.GetState()==orderState.TakingFoodToCounter || OrderThree.GetState()==orderState.TakingFoodToGrill) {
			g.drawString(OrderThree.GetOrder(), xPos, yPos);
		}
		else if (OrderThree.GetState()==orderState.OnGrill || OrderThree.GetState()==orderState.Cooked) {
			g.drawString(OrderThree.GetOrder(), xGrill+20, yGrill);
		}
		else if (OrderThree.GetState()==orderState.OnCounter) {
			g.drawString(OrderThree.GetOrder(), xCounter-20, yCounter+35);
		}
        
    }

    public boolean isPresent() {
        return true;
    }

    public void DoGoToCenter() {
    	xDestination = xHome;
    	yDestination = yHome;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
    
    public class Events {
    	int table;
    	Happenings Event;
    	
    	public Events(Happenings e, int t) {
    		Event = e;
    		table =t;
		}
    	
    	public int GetTable() {
    		return table;
    	}
    	public Happenings GetEvent() {
    		return Event;
    	}
    }
    
    public class Orders {
    	String order;
    	orderState OrderState =orderState.noOrder;
    	int table;
    	
    	public Orders(String choice, int t) {
    		if (choice=="Steak") {
        		order="ST?";
        	}
        	else if (choice=="Chicken") {
        		order="CK?";
        	}
        	else if (choice=="Pizza") {
        		order="PZ?";
        	}
        	else {
        		order="SD?";
        	}
    		table = t;
    	}
    	
    	public int GetTable() {
    		return table;
    	}
    	
    	public void SetState(orderState o) {
    		OrderState=o;
    	}
    	
    	public orderState GetState() {
    		return OrderState;
    	}
    	
    	public String GetOrder() {
    		return order;
    	}
    	
    	public void SetOrder(String choice) {
    		if (choice=="Steak") {
        		order="ST?";
        	}
        	else if (choice=="Chicken") {
        		order="CK?";
        	}
        	else if (choice=="Pizza") {
        		order="PZ?";
        	}
        	else {
        		order="SD?";
        	}
    	}
    }
}
