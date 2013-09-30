package restaurant.gui;

import restaurant.CustomerAgent;
import restaurant.HostAgent;
//import restaurant.gui.WaiterGui.orderState;

import java.awt.*;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import java.awt.image.BufferedImage;

public class CustomerGui implements Gui{

	private CustomerAgent agent = null;
	private boolean isPresent = false;
	private boolean isHungry = false;

	//private HostAgent host;
	RestaurantGui gui;

	private int xPos, yPos;
	private int xDestination, yDestination;
	private enum Command {noCommand, GoToSeat, LeaveRestaurant};
	private Command command=Command.noCommand;
	private int offscreenfourty = -40;
	private int shifttwenty = 20;
	private JLabel picture;
    private ImageIcon pic;
    private BufferedImage image;
    private enum FoodState {noFood, EatingFood};
    private String order;
    
    FoodState Food = FoodState.noFood;

    

	public static final int xTable = 200;
	public static final int yTable = 250;

	public CustomerGui(CustomerAgent c, RestaurantGui gui){ //HostAgent m) {
		agent = c;
		xPos = offscreenfourty;
		yPos = offscreenfourty;
		xDestination = offscreenfourty;
		yDestination = offscreenfourty;
		//maitreD = m;
		this.gui = gui;
	}

	public void updatePosition() {
		if (xPos < xDestination)
			xPos++;
		else if (xPos > xDestination)
			xPos--;

		if (yPos < yDestination)
			yPos++;
		else if (yPos > yDestination)
			yPos--;

		if (xPos == xDestination && yPos == yDestination) {
			if (command==Command.GoToSeat) agent.msgAnimationFinishedGoToSeat();
			else if (command==Command.LeaveRestaurant) {
				agent.msgAnimationFinishedLeaveRestaurant();
				//System.out.println("about to call gui.setCustomerEnabled(agent);");
				isHungry = false;
				gui.setCustomerEnabled(agent);
			}
			command=Command.noCommand;
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fillRect(xPos, yPos, shifttwenty, shifttwenty);
		g.setColor(Color.BLACK);
		 if (Food == FoodState.EatingFood) {
	        	g.drawString(order, xPos, yPos-offscreenfourty);
	        }
		
	//	image=ImageIO.read(new File("image/customerstickfigure.jpg"));
		//pic = new ImageIcon("image/customerstickfigure.jpg");
	    //picture = new JLabel(pic);
	}

	public boolean isPresent() {
		return isPresent;
	}
	public void setHungry() {
		isHungry = true;
		agent.gotHungry();
		setPresent(true);
	}
	public boolean isHungry() {
		return isHungry;
	}

	public void setPresent(boolean p) {
		isPresent = p;
	}

	public void DoGoToSeat(int seatnumber) {//later you will map seatnumber to table coordinates.
		//xDestination = xTable;
		//yDestination = yTable;
		command = Command.GoToSeat;

	}
	
	public void EatTime(String choice) {
		if (choice=="Steak") {
    		order="ST";
    	}
    	else if (choice=="Chicken") {
    		order="CK";
    	}
    	else if (choice=="Pizza") {
    		order="PZ";
    	}
    	else {
    		order="SD";
    	}
		Food=FoodState.EatingFood;
	}
	
	public void msgGoToXY(int x, int y){
		xDestination = x;
		yDestination = y;
	}

	public void DoExitRestaurant() {
		Food=FoodState.noFood;
		xDestination = offscreenfourty;
		yDestination = offscreenfourty;
		command = Command.LeaveRestaurant;
	}
}
