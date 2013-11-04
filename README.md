##Restaurant Project Repository

###Student Information
  + Name: Robert Trent Jones
  + USC Email: roberttj@usc.edu
  + USC ID: 9333270714

###Resources
  + [Restaurant v1](http://www-scf.usc.edu/~csci201/readings/restaurant-v1.html)
  + [Agent Roadmap](http://www-scf.usc.edu/~csci201/readings/agent-roadmap.html)

###Compiling Code
  + Code can be compiled from the RestaurantGui.java file.  Clicking Run in Eclipse while in RestaurantGui.java should launch the restaurant similution without any errors.  All requirements were, to the best of my knowledge, met.

###GUI Instructions
  + The text field is where the name for any customer and/or waiter is input. Customers are added by clicking the "Add Customer" button, at which point the current text in the text field becomes the new customer's name and his/her initial hunger status is determined by the state of the "Hungry?" checkbox next to the "Add Customer" button: selected to start hungry and unselected to start not hungry.
  
  + Waiters are added by clicking the "Add Waiter" button, at which point the current text in the text field becomes the new waiter's name.  There is no list of waiters shown, but they are added when the button is pressed.
   
  + The "Pause" button pauses the actions of each agent after they have completed their current action, but before they attempt to select another action.  After the "Pause" button is pressed it is replaced by a "Resume" button in the same location.  Pressing the "Resume" button resumes normal agent action and replaces the "Resume" button with the "Pause" button once more.
  
  + A checkbox is available for a waiter to go on break. He will only go on break if there is at least one other waiter, no other waiters are on break, and he has no customers. If there is no other waiter the checkbox will immediately return to unselected and enabled. If the waiter cannot go on break now because another waiter is on break or he has customers the checkbox will remain selected and become disabled. When he can next take a break he will. When a waiter is on break the checkbox is selected and enabled. Unchecking the box will return the waiter to work.
  
###Non-Normative Scenarios
  + All Non-Normative Scenarios work, though they can be difficult to create from normal function.

###Design Documents
  +Design documents are in the Design Doc folder in the restaurant_roberttj repository.  They are in PDF/JPG format.

###Unit Testing
  +Unit testing can be run by clicking Run in Eclipse while in CashierTest.java
