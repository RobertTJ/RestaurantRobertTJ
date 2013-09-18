package restaurant.gui;


import restaurant.CustomerAgent;
import restaurant.HostAgent;

import java.awt.*;

public class HostGui implements Gui {

    private HostAgent agent = null;

    private int xPos = -20, yPos = -20;//default waiter position
    private int xDestination = -20, yDestination = -20;//default start position
    private int shifttwenty = 20;

    public static final int xTable1 = 200;
    public static final int yTable1 = 250;
    
    public static final int xTable2 = 420;
    public static final int yTable2 = 250;
    
    public static final int xTable3 = 640;
    public static final int yTable3 = 250;

    public HostGui(HostAgent agent) {
        this.agent = agent;
    }


   /* public HostGui(WaiterAgent agent) {
        this.agent = agent;
    }*/

    
    public void updatePosition() {
        if (xPos < xDestination)
            xPos++;
        else if (xPos > xDestination)
            xPos--;

        if (yPos < yDestination)
            yPos++;
        else if (yPos > yDestination)
            yPos--;

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
        		& (xDestination ==-shifttwenty) & (yDestination == - shifttwenty)) {
           agent.msgAtFront();
        }
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.MAGENTA);
        g.fillRect(xPos, yPos, shifttwenty, shifttwenty);
    }

    public boolean isPresent() {
        return true;
    }

    public void DoBringToTable(CustomerAgent customer, int n) {
        if (n==1){
        	xDestination = xTable1 + shifttwenty;
            yDestination = yTable1 - shifttwenty;
            customer.msgGoToDest(xTable1,yTable1);

        }
        else if (n==2){
        	xDestination = xTable2 + shifttwenty;
            yDestination = yTable2 - shifttwenty;
            customer.msgGoToDest(xTable2,yTable2);

        }
        else if (n==3){
        	xDestination = xTable3 + shifttwenty;
            yDestination = yTable3 - shifttwenty;
            customer.msgGoToDest(xTable3,yTable3);

        }
        
    	
        //needs to be changed based on what table they are going to, interacts with host agent
    }

    public void DoLeaveCustomer() {
        xDestination = -shifttwenty;
        yDestination = -shifttwenty;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
}
