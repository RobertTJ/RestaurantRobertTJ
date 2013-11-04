package restaurant.gui;


import restaurant.CookAgent;
import restaurant.CustomerAgent;
import restaurant.HostAgent;
import restaurant.WaiterAgent;
import restaurant.interfaces.Customer;

import java.awt.*;

public class CookGui implements Gui {

    private CookAgent agent = null;
    private String order;
    private enum orderState {noOrder, TakingFoodToCounter};
    
    orderState OrderState = orderState.noOrder;

    private int xPos = 650, yPos = 50;//default cook position
    private int xDestination = 650, yDestination = 50;//default start position
    private int xHome = 650, yHome = 50;
    private int shifttwenty = 20;
    private int xWait  = -20, yWait = -20;

    public static final int xCounter = 600;
    public static final int yCounter = 50;
    
    public static final int xGrill = 650;
    public static final int yGrill = 20;
    
    public static final int xFridge = 730;
    public static final int yFridge = 50;
    
    //public static final int cookX = 900;
    //public static final int cookY = 150;

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
        		& (xDestination ==xGrill) & (yDestination == yGrill)) {
           agent.msgAtGrill();
        }
        else if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xCounter) & (yDestination == yCounter)) {
           agent.msgAtCounter();
           OrderState = orderState.noOrder;
        }
        else if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xFridge-40) & (yDestination == yFridge)) {
           agent.msgAtFridge();
        }

    }
    
    public void DoGoToCounter(String o) {
    	order = o; 
    	OrderState=orderState.TakingFoodToCounter;
    	xDestination=xCounter;//+20;
    	yDestination=yCounter;
    }
    
    public void DoGoToGrill() {
    	//order=choice;
    	//OrderState=orderState.takingOrderToCook;
    	xDestination=xGrill;
    	yDestination=yGrill;//+20;
    }
    
    public void DoGoToFridge() {
    	//order=choice;
    	//OrderState=orderState.takingOrderToCook;
    	xDestination=xFridge-40;
    	yDestination=yFridge;
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.RED);
        g.fillRect(xPos, yPos, shifttwenty, shifttwenty);
        g.setColor(Color.ORANGE);
        g.fillRect(xCounter-shifttwenty, yCounter-shifttwenty, shifttwenty, shifttwenty*3);
        g.setColor(Color.BLACK);
        g.fillRect(xGrill-shifttwenty, yGrill-shifttwenty, shifttwenty*3, shifttwenty);
        g.setColor(Color.BLUE);
        g.fillRect(xFridge-shifttwenty, yFridge-shifttwenty, shifttwenty, shifttwenty*3);
		g.setColor(Color.BLACK);
        if (OrderState == orderState.TakingFoodToCounter) {
        	//g.drawString(order, xPos, yPos);
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
}
