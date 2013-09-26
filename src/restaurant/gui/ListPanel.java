package restaurant.gui;

import restaurant.CustomerAgent;
import restaurant.HostAgent;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Subpanel of restaurantPanel.
 * This holds the scroll panes for the customers and, later, for waiters
 */
public class ListPanel extends JPanel implements ActionListener {

    public JScrollPane pane =
            new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private JPanel view = new JPanel();
    private List<JButton> list = new ArrayList<JButton>();
    private JButton addPersonB = new JButton("Add");
    private JButton Pause = new JButton("Pause");
    private JButton Resume = new JButton("Resume");

    private RestaurantPanel restPanel;
    private String type;
    private JTextField newcustomer;
    private JPanel entryline;
    List<JCheckBox> stateCB2 = new ArrayList<JCheckBox>();
    private int k=0;
    private int textwidth = 200;
    private int textheight = 24;
    private int one = 1;
    private int oneten = 110;
    private int seven = 7;
    private int rows = 0;
    private int columns = 2;


    /**
     * Constructor for ListPanel.  Sets up all the gui
     *
     * @param rp   reference to the restaurant panel
     * @param type indicates if this is for customers or waiters
     */
    public ListPanel(RestaurantPanel rp, String type) {
        restPanel = rp;
        this.type = type;

        setLayout(new BoxLayout((Container) this, BoxLayout.Y_AXIS));
        add(new JLabel("<html><pre> <u>" + type + "</u><br></pre></html>"));

        entryline = new JPanel();
        entryline.setLayout(new BoxLayout(entryline, BoxLayout.X_AXIS));
        
        newcustomer = new JTextField(one);
        newcustomer.setPreferredSize( new Dimension( textwidth,textheight ) );
        newcustomer.setMinimumSize( new Dimension( textwidth, textheight ) );
        newcustomer.setMaximumSize( new Dimension( textwidth, textheight ) );


        entryline.add(newcustomer);
        
        Pause.addActionListener(this);
        Resume.addActionListener(this);
        addPersonB.addActionListener(this);
        entryline.add(addPersonB);
        entryline.add(Pause);
        entryline.add(Resume);
        Resume.setVisible(false);
       
        add(entryline);
        
        view.setLayout(new GridLayout(rows,columns));
        pane.setViewportView(view);
        add(pane);
    }

    /**
     * Method from the ActionListener interface.
     * Handles the event of the add button being pressed
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addPersonB) {
        	// Chapter 2.19 describes showInputDialog()
            addPerson(newcustomer.getText());
        }
        else if (e.getSource() == Pause) {
        	restPanel.pause();
        	Pause.setVisible(false);
        	Resume.setVisible(true);
        }
        else if (e.getSource() == Resume) {
        	restPanel.resume();
        	Pause.setVisible(true);
        	Resume.setVisible(false);
        }
        
       
        else {
        	for  (int i = 0; i < list.size(); i++) {
     		   JCheckBox tempalso = new JCheckBox();
     		   tempalso=stateCB2.get(i);
     		  
               if (e.getSource() == tempalso) { 
            	   restPanel.actionTime(i);
                   tempalso.setEnabled(false);                 
                   }
               }
        }
        
        
        
      /*  else {
        	
        	for (JButton temp:list){
                if (e.getSource() == temp)
                    restPanel.showInfo(type, temp.getText());
                //noted - not needed for lab 2, displays info on bottom, moving to right
            }
        }
      */
    }
    
    
    public void enable(int i) {
    	JCheckBox temp=new JCheckBox();
    	temp = stateCB2.get(i);
    	temp.setEnabled(true);
        temp.setSelected(false);
    }

    /**
     * If the add button is pressed, this function creates
     * a spot for it in the scroll pane, and tells the restaurant panel
     * to add a new person.
     *
     * @param name name of new person
     */
    public void addPerson(String name) {
        if (name != null) {
        	k=k+1;
            JButton button = new JButton(name);
            button.setBackground(Color.white);

            Dimension paneSize = pane.getSize();
            Dimension buttonSize = new Dimension(paneSize.width - oneten,
                    (int) (paneSize.height / seven));
            button.setPreferredSize(buttonSize);
            button.setMinimumSize(buttonSize);
            button.setMaximumSize(buttonSize);
            button.addActionListener(this);
            list.add(button);
            view.add(button);
            
            JCheckBox checkit = new JCheckBox("Hungry?"); 
            checkit.setVisible(true);
            checkit.addActionListener(this);
            
            stateCB2.add(checkit);
            view.add(checkit);
            
            restPanel.addPerson(type, name);//puts customer on list
           // restPanel.showInfo(type, name);//puts hungry button on panel

            
            validate();
        }
    }
    
    
    
    
}
