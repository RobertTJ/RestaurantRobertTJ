package restaurant.gui;

import restaurant.CashierAgent;
import restaurant.CustomerAgent;
import restaurant.HostAgent;
import restaurant.MarketAgent;
import restaurant.WaiterAgent;
import restaurant.CookAgent;
import restaurant.interfaces.Waiter;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class RestaurantPanel extends JPanel {

    //Host, cook, waiters and customers
    private HostAgent host = new HostAgent("Sarah");
    private HostGui hostGui = new HostGui(host);
   
    
    private MarketAgent market1 = new MarketAgent("Bob");
    private MarketAgent market2 = new MarketAgent("Bobby");
    private MarketAgent market3 = new MarketAgent("Bobert");
    private CashierAgent cashier = new CashierAgent("Jimmy");
    private CookAgent cook = new CookAgent("Tim");
    private CookGui cookGui = new CookGui(cook);


    private int xHome = 200, yHome = 40;

    private Vector<CustomerAgent> customers = new Vector<CustomerAgent>();
    private Vector<WaiterAgent> waiters = new Vector<WaiterAgent>();

    private JPanel restLabel = new JPanel();
    private ListPanel customerPanel = new ListPanel(this, "Customers");
    
    private JPanel group = new JPanel();

    private RestaurantGui gui; //reference to main gui
    
   
    
    
    private int Gridwidth=1;
    private int two=2;
    private int ten=10;
    private int twenty=20;

    public RestaurantPanel(RestaurantGui gui) {
        this.gui = gui;
        
        host.setGui(hostGui);
        host.setRestGui(gui);

        cashier.startThread();
        
        cook.setGui(cookGui);
        cook.startThread();
        gui.animationPanel.addGui(cookGui);
        
        market1.startThread();
        market1.SetCook(cook);
        market1.SetCashier(cashier);
        
        market2.startThread();
        market2.SetCook(cook);
        market2.SetCashier(cashier);

        market3.startThread();
        market3.SetCook(cook);
        market3.SetCashier(cashier);

        cook.AddMarket(market1);
        cook.AddMarket(market2);
        cook.AddMarket(market3);
        cashier.addMarket(market1);
        cashier.addMarket(market2);
        cashier.addMarket(market3);
        
        //gui.animationPanel.addGui(hostGui);
        host.startThread();

        setLayout(new GridLayout(Gridwidth, two, twenty, twenty));
        group.setLayout(new GridLayout(Gridwidth, two, ten, ten));

        group.add(customerPanel);

        initRestLabel();
        add(restLabel);
        add(group);
        //cook.SetPayingAttention(true);
    }

    public int getsize(){
    	return customers.size();
    }
    
    /**
     * Sets up the restaurant label that includes the menu,
     * and host and cook information
     */
    private void initRestLabel() {
        JLabel label = new JLabel();
        //restLabel.setLayout(new BoxLayout((Container)restLabel, BoxLayout.Y_AXIS));
        restLabel.setLayout(new BorderLayout());
        label.setText(
                "<html><h3><u>Tonight's Staff</u></h3><table><tr><td>host:</td><td>" + host.getName() + "</td></tr></table><h3><u> Menu</u></h3><table><tr><td>Steak</td><td>$15.99</td></tr><tr><td>Chicken</td><td>$10.99</td></tr><tr><td>Salad</td><td>$5.99</td></tr><tr><td>Pizza</td><td>$8.99</td></tr></table><br></html>");

        restLabel.setBorder(BorderFactory.createRaisedBevelBorder());
        restLabel.add(label, BorderLayout.CENTER);
        restLabel.add(new JLabel("               "), BorderLayout.EAST);
        restLabel.add(new JLabel("               "), BorderLayout.WEST);
    }
    
    public void OnBreak(Waiter w) {
    	WaiterAgent temp = new WaiterAgent("Ted");
        for (int i = 0; i < waiters.size(); i++) {
        	temp=waiters.get(i);
        	if (temp==w){
        		customerPanel.WOnBreak(i);
        	}
        }
    }
    
    public void findcust(CustomerAgent c){
    	CustomerAgent temp = new CustomerAgent("Ted");
        for (int i = 0; i < customers.size(); i++) {
        	temp=customers.get(i);
        	if (temp==c){
        		customerPanel.enable(i);
        	}
        }
    }
    
    public void actionTime(int i) {
    	
        CustomerAgent temp = new CustomerAgent("Ted");
        temp=customers.get(i);
        temp.getGui().setHungry();
    }
        
    public void BreakTime(int i) {
    	WaiterAgent temp = new WaiterAgent("Ted");
    	temp = waiters.get(i);
    	if (waiters.size() >1 )	temp.msgIWantToGoOnBreak();
    	else {
    		temp.msgCantBreakNow();
    		customerPanel.CantBreakNow(i);
    	}
    }
    
    public void BreakTimeOver(int i) {
    	WaiterAgent temp = new WaiterAgent("Ted");
    	temp = waiters.get(i);
    	temp.msgBreakOver();
    	
    }
    
    public void pause() {
    	host.Pause();
    	cook.Pause();
    	
    	market1.Pause();
    	market2.Pause();
    	market3.Pause();
    	
    	for (WaiterAgent w : waiters) {
    		w.Pause();
    	}
    	
    	for (CustomerAgent cust : customers) {
    		cust.Pause();
    	}
    }
    
    public void resume() {
    	host.Resume();
    	cook.Resume();
    	
    	market1.Resume();
    	market2.Resume();
    	market3.Resume();
    	
    	for (WaiterAgent w : waiters) {
    		w.Resume();
    	}
    	
    	for (CustomerAgent cust : customers) {
    		cust.Resume();
    	}
    }

    /**
     * Adds a customer or waiter to the appropriate list
     *
     * @param type indicates whether the person is a customer or waiter (later)
     * @param name name of person
     */
    public void addPerson(String type, String name) {

    	if (type.equals("Customers")) {
    		CustomerAgent c = new CustomerAgent(name);	
    		CustomerGui g = new CustomerGui(c, gui);

    		gui.animationPanel.addGui(g);
    		c.setHost(host);
    		c.setGui(g);
    	    c.SetCashier (cashier);
    		customers.add(c);
    		c.startThread();
    	}
    	
    	else {
    		WaiterAgent w = new WaiterAgent(name);
    		WaiterGui wg = new WaiterGui(w);
    		
    		w.setGui(wg);
    		w.SetHomePosition(xHome, yHome);
    		xHome = xHome + 30;
    		if (xHome>500) {
    			xHome = 200;
    			yHome = yHome + 30;
    		}
    	    host.msgNewWaiter(w);

    	    w.SetHost (host);
    	    w.SetCashier (cashier);
    	    w.setCook(cook);
    	        
    	    gui.animationPanel.addGui(wg);
    	    waiters.add(w);
    	    w.startThread();
    		
    	}
    }

}
