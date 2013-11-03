package restaurant.gui;


import restaurant.CustomerAgent;
import restaurant.HostAgent;
import restaurant.WaiterAgent;
import restaurant.interfaces.Customer;

import java.awt.*;

public class WaiterGui implements Gui {

    private WaiterAgent agent = null;
    private String order;
    private enum orderState {noOrder, takingOrderToCook, TakingFoodToCustomer};
    
    orderState OrderState = orderState.noOrder;

    private int xPos = -20, yPos = -20;//default waiter position
    private int xDestination = -20, yDestination = -20;//default start position
    private int xHome = -20, yHome = -20;
    private int shifttwenty = 20;
    private int xWait  = -20, yWait = -20;

    public static final int xTable1 = 200;
    public static final int yTable1 = 250;
    
    public static final int xTable2 = 420;
    public static final int yTable2 = 250;
    
    public static final int xTable3 = 640;
    public static final int yTable3 = 250;
    
    public static final int cookX = 900;
    public static final int cookY = 150;

    public WaiterGui(WaiterAgent agent) {
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
        		& (xDestination == xTable1 + shifttwenty) & (yDestination == yTable1 - shifttwenty)) {
           agent.msgAtTable();
        }
        else if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xTable2 + shifttwenty) & (yDestination == yTable2 - shifttwenty)) {
           agent.msgAtTable();
        }
        else if (xPos == xDestination && yPos == yDestination
        		& (xDestination == xTable3 + shifttwenty) & (yDestination == yTable3 - shifttwenty)) {
           agent.msgAtTable();
        }
        else if (xPos == xDestination && yPos == yDestination
        		&& (xDestination == xHome) && (yDestination == yHome)) {
           agent.msgAtFront();
        }
        else if (xPos == xDestination && yPos == yDestination
        		&& (xDestination == xWait) && (yDestination == yWait)) {
           agent.msgAtCust();
        }
        else if (xPos == xDestination && yPos == yDestination
        		& (xDestination == cookX) & (yDestination == cookY)) {
           agent.msgAtCook();
        }
    }
    
    public void DoGoToCook() {
    	//order=choice;
    	//OrderState=orderState.takingOrderToCook;
    	xDestination=cookX;
    	yDestination=cookY;
    }
    
    public void BringOrderToCook(String choice) {
    	order=choice;
    	OrderState=orderState.takingOrderToCook;
    	xDestination=cookX;
    	yDestination=cookY;
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.MAGENTA);
        g.fillRect(xPos, yPos, shifttwenty, shifttwenty);
		g.setColor(Color.BLACK);
        if (OrderState == orderState.TakingFoodToCustomer) {
        	g.drawString(order, xPos, yPos);
        }
    }

    public boolean isPresent() {
        return true;
    }

    public void DoBringToTable(Customer customer, int n) {
        if (n==1){
        	xDestination = xTable1 + shifttwenty;
            yDestination = yTable1 - shifttwenty;
            customer.getGui().msgGoToXY(xTable1,yTable1);

        }
        else if (n==2){
        	xDestination = xTable2 + shifttwenty;
            yDestination = yTable2 - shifttwenty;
            customer.getGui().msgGoToXY(xTable2,yTable2);

        }
        else if (n==3){
        	xDestination = xTable3 + shifttwenty;
            yDestination = yTable3 - shifttwenty;
            customer.getGui().msgGoToXY(xTable3,yTable3);

        }
    }
    
    public void DoGoToFront() {
    	xDestination = -20;
    	yDestination = -20;
    }
    
    public void DoGoToTable(Customer customer, int n) {
        if (n==1){
        	xDestination = xTable1 + shifttwenty;
            yDestination = yTable1 - shifttwenty;
            
        }
        else if (n==2){
        	xDestination = xTable2 + shifttwenty;
            yDestination = yTable2 - shifttwenty;

        }
        else if (n==3){
        	xDestination = xTable3 + shifttwenty;
            yDestination = yTable3 - shifttwenty;
            
        }
    }
        
    public void BringFoodToCustomer(Customer customer, int tableN, String choice) {
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
    	
    	OrderState=orderState.TakingFoodToCustomer;
            if (tableN==1){
            	xDestination = xTable1 + shifttwenty;
                yDestination = yTable1 - shifttwenty;
                //customer.msgDeliveredFood();

            }
            else if (tableN==2){
            	xDestination = xTable2 + shifttwenty;
                yDestination = yTable2 - shifttwenty;
                //customer.msgDeliveredFood();

            }
            else if (tableN==3){
            	xDestination = xTable3 + shifttwenty;
                yDestination = yTable3 - shifttwenty;
                //customer.msgDeliveredFood();

            }
        
    	
        //needs to be changed based on what table they are going to, interacts with host agent
    }

    public void DoLeaveCustomer() {
    	OrderState=orderState.noOrder;
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
