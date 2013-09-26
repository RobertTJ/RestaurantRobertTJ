package restaurant.gui;

import restaurant.CustomerAgent;
import restaurant.HostAgent;
import restaurant.WaiterAgent;
import restaurant.CookAgent;

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
    
    
    private WaiterAgent waiter = new WaiterAgent("Frank");
    private WaiterGui waiterGui = new WaiterGui(waiter);
    
   // private WaiterAgent waiter2 = new WaiterAgent("Fred");
   // private WaiterGui waiterGui2 = new WaiterGui(waiter);
    
    private CookAgent cook = new CookAgent("Tim");

    private Vector<CustomerAgent> customers = new Vector<CustomerAgent>();

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
       
        waiter.setGui(waiterGui);
        //waiter2.setGui(waiterGui2);
        host.allWaiters.add(waiter);
       // host.allWaiters.add(waiter2);

        waiter.SetHost(host);
       // waiter2.SetHost(host);
        
        waiter.setCook(cook);
       // waiter2.setCook(cook);


        
        gui.animationPanel.addGui(waiterGui);
        waiter.startThread();
      // gui.animationPanel.addGui(waiterGui2);
        //waiter2.startThread();
       
        cook.startThread();
        
        //gui.animationPanel.addGui(hostGui);
        host.startThread();

        setLayout(new GridLayout(Gridwidth, two, twenty, twenty));
        group.setLayout(new GridLayout(Gridwidth, two, ten, ten));

        group.add(customerPanel);

        initRestLabel();
        add(restLabel);
        add(group);
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

    /**
     * When a customer or waiter is clicked, this function calls
     * updatedInfoPanel() from the main gui so that person's information
     * will be shown
     *
     * @param type indicates whether the person is a customer or waiter
     * @param name name of person
     */
  /*  public void showInfo(String type, String name) {

        if (type.equals("Customers")) {

            for (int i = 0; i < customers.size(); i++) {
                CustomerAgent temp = customers.get(i);
                JCheckBox tempalso = customerPanel.stateCB2.get(i);
                
                tempalso.setSelected(temp.getGui().isHungry());
                //Is customer hungry? Hack. Should ask customerGui
                tempalso.setEnabled(!temp.getGui().isHungry());
              // Hack. Should ask customerGui
                
                
              //  if (temp.getName() == name)
               //     gui.updateInfoPanel(temp);
            }
        }
    }*/
    
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
        
    
    public void pause() {
    	host.Pause();
    	cook.Pause();
    	waiter.Pause();
    	//waiter2.Pause();
    	
    	for (CustomerAgent cust : customers) {
    		cust.Pause();
    	}
    }
    
    public void resume() {
    	host.Resume();
    	cook.Resume();
    	waiter.Resume();
    	//waiter2.Resume();
    	
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

    		gui.animationPanel.addGui(g);// dw
    		//gui.allAgents.add(c);
    		c.setHost(host);
    		c.setGui(g);
    		/*
    		 ALERT ALERT ALERT ALERT
    	 	 
    	 	 ALERT ALERT ALERT ALERT

    		 ALERT ALERT ALERT ALERT

    		 ALERT ALERT ALERT ALERT


    		 */
    		c.msgSetWaiter(waiter);//PROBLEM FIX FOR MULT WAITERS
    		customers.add(c);
    		c.startThread();
    	}
    }

}
