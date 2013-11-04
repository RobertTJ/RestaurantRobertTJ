package restaurant.test;

import restaurant.CashierAgent;
import restaurant.WaiterAgent;
import restaurant.WaiterAgent.MyCustomers;
import restaurant.MarketAgent;
import restaurant.test.mock.MockCook;
import restaurant.test.mock.MockCustomer;
import restaurant.test.mock.MockMarket;
import restaurant.test.mock.MockWaiter;
import junit.framework.*;


public class CashierTest extends TestCase
{
	//these are instantiated for each test separately via the setUp() method.
	CashierAgent cashier;
	MockWaiter waiter;
	MockCustomer customer;
	MyCustomers ThisGuy;
	MockMarket market1;
	MockMarket market2;
	MockCook cook;
	
	
	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();		
		cashier = new CashierAgent("cashier");		
		customer = new MockCustomer("mockcustomer");		
		waiter = new MockWaiter("mockwaiter");
		market1 = new MockMarket("mockmarket1");
		market2 = new MockMarket("mockmarket2");
		cook = new MockCook("mockcook");
	}	
	/**
	 * This tests the cashier under very simple terms: one customer is ready to pay the exact bill.
	 */
	public void testOneNormalMarketScenario()
	{
		try {
			setUp();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		customer.cashier = cashier;
		market1.cashier = cashier;
		market2.cashier = cashier;
		waiter.cashier = cashier;
		cook.AddMarket(market1);
		cook.AddMarket(market2);
		cashier.addMarket(market1);
		cashier.addMarket(market2);
		market1.SetCook(cook);
		market2.SetCook(cook);
		cashier.CASHMONEY = 100;
		
		//assert everything is in default state
		assertEquals("Cashier should have 0 customers in it. It doesn't.",cashier.Customers.size(), 0);		
		assertEquals("Cashier should have 2 markets in it. It doesn't.",cashier.Markets.size(), 2);	
		
		
		assertEquals("Cook should have 2 markets in it.  It doesn't.",cook.Markets.size(),2);
		assertEquals("Cook should have 0 steak in it.  It doesn't.",cook.inventory.GetAmountOf("Steak"), 0);
		
		assertEquals("Market1 should have no bill.  It does.", market1.owed,0.00);
		
		//step 1 - restock
		cook.Restock("Steak", 3);
		assertEquals("Market1 should have a bill.  It doesn't.", market1.owed,3*9.99);
		assertEquals("Cashier should have no bill for market1, it does.",cashier.Markets.get(0).GetBill(), 0.00);
		//assert it worked
		
		//step 2 - 
		market1.SendBill();
		assertEquals("Cashier should have a bill for market1, it doesn't.",cashier.Markets.get(0).GetBill(),3*9.99);
		
		//step 3 - pay bill
		cashier.PayMarket(cashier.Markets.get(0));
		assertFalse("Cashier should have paid the bill.  It hasn't.",cashier.CASHMONEY==100);
		assertEquals("Cashier should have nothave a bill for market1, it does.",cashier.Markets.get(0).GetBill(),0.00);
		assertEquals("Market1 should not have a bill.  It does.", market1.owed, 0.00);
		
		//test one complete

	}
	
	public void testTwoNormalMarketScenario()
	{
		try {
			setUp();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		customer.cashier = cashier;
		market1.cashier = cashier;
		market2.cashier = cashier;
		waiter.cashier = cashier;
		cook.AddMarket(market1);
		cook.AddMarket(market2);
		cashier.addMarket(market1);
		cashier.addMarket(market2);
		market1.SetCook(cook);
		market2.SetCook(cook);
		cashier.CASHMONEY = 100;
		
		//assert everything is in default state
		assertEquals("Cashier should have 0 customers in it. It doesn't.",cashier.Customers.size(), 0);		
		assertEquals("Cashier should have 2 markets in it. It doesn't.",cashier.Markets.size(), 2);	
		
		
		assertEquals("Cook should have 2 markets in it.  It doesn't.",cook.Markets.size(),2);
		assertEquals("Cook should have 0 steak in it.  It doesn't.",cook.inventory.GetAmountOf("Steak"), 0);
		
		assertEquals("Market1 should have no bill.  It does.", market1.owed,0.00);
		
		//step 1 - restock
		cook.Restock("Steak", 3);
		assertEquals("Market1 should have a bill.  It doesn't." + market1.owed, market1.owed,3*9.99);
		assertEquals("Cashier should have no bill for market1, it does.",cashier.Markets.get(0).GetBill(), 0.00);
		assertEquals("Market1 is not out of steak, it should be.", market1.inventory.GetAmountOf("Steak"),0);
		//assert it worked
		cook.Restock("Steak", 3);
		assertEquals("Market2 should have a bill.  It doesn't." + market2.owed, market2.owed,3*9.99);
		assertEquals("Cashier should have no bill for market2, it does.",cashier.Markets.get(1).GetBill(), 0.00);
		assertEquals("Market2 is not out of steak, it should be.", market2.inventory.GetAmountOf("Steak"),0);
		
		//step 2 - 
		market1.SendBill();
		assertEquals("Cashier should have a bill for market1, it doesn't.",cashier.Markets.get(0).GetBill(),3*9.99);
		market2.SendBill();	
		assertEquals("Cashier should have a bill for market2, it doesn't.",cashier.Markets.get(1).GetBill(),3*9.99);

		
		//step 3 - pay bill
		//cashier.PayMarket(cashier.Markets.get(0));
		cashier.pickAndExecuteAnAction();
		assertFalse("Cashier should have paid the bill.  It hasn't.",cashier.CASHMONEY==100);
		assertEquals("Cashier should have not have a bill for market1, it does.",cashier.Markets.get(0).GetBill(),0.00);
		assertEquals("Market1 should not have a bill.  It does.", market1.owed, 0.00);
		cashier.pickAndExecuteAnAction();
		assertEquals("Cashier should have not have a bill for market2, it does.",cashier.Markets.get(1).GetBill(),0.00);
		assertEquals("Market2 should not have a bill.  It does.", market2.owed, 0.00);
		//test one complete

	}
}

		


		
		//ThisGuy = new MyCustomers(customer, 0);
		//cashier.msgCheckPlease(customer, 0,6.99);
		//cashier.GiveBillToWaiter(ThisGuy);
		
	/*	//setUp() runs first before this test!
		
		//step 1 of the test
		//public Bill(Cashier, Customer, int tableNum, double price) {
		Bill bill = new Bill(cashier, customer, 2, 7.98);
		cashier.HereIsBill(bill);//send the message from a waiter

		//check postconditions for step 1 and preconditions for step 2
		assertEquals("MockWaiter should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: "
						+ waiter.log.toString(), 0, waiter.log.size());
		
		assertEquals("Cashier should have 1 bill in it. It doesn't.", cashier.bills.size(), 1);
		
		assertFalse("Cashier's scheduler should have returned false (no actions to do on a bill from a waiter), but didn't.", cashier.pickAndExecuteAnAction());
		
		assertEquals(
				"MockWaiter should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockWaiter's event log reads: "
						+ waiter.log.toString(), 0, waiter.log.size());
		
		assertEquals(
				"MockCustomer should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: "
						+ waiter.log.toString(), 0, waiter.log.size());
		
		//step 2 of the test
		cashier.ReadyToPay(customer, bill);
		
		//check postconditions for step 2 / preconditions for step 3
		assertTrue("CashierBill should contain a bill with state == customerApproached. It doesn't.",
				cashier.bills.get(0).state == cashierBillState.customerApproached);
		
		assertTrue("Cashier should have logged \"Received ReadyToPay\" but didn't. His log reads instead: " 
				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received ReadyToPay"));

		assertTrue("CashierBill should contain a bill of price = $7.98. It contains something else instead: $" 
				+ cashier.bills.get(0).bill.netCost, cashier.bills.get(0).bill.netCost == 7.98);
		
		assertTrue("CashierBill should contain a bill with the right customer in it. It doesn't.", 
					cashier.bills.get(0).bill.customer == customer);
		
		
		//step 3
		//NOTE: I called the scheduler in the assertTrue statement below (to succintly check the return value at the same time)
		assertTrue("Cashier's scheduler should have returned true (needs to react to customer's ReadyToPay), but didn't.", 
					cashier.pickAndExecuteAnAction());
		
		//check postconditions for step 3 / preconditions for step 4
		assertTrue("MockCustomer should have logged an event for receiving \"HereIsYourTotal\" with the correct balance, but his last event logged reads instead: " 
				+ customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Received HereIsYourTotal from cashier. Total = 7.98"));
	
			
		assertTrue("Cashier should have logged \"Received HereIsMyPayment\" but didn't. His log reads instead: " 
				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received HereIsMyPayment"));
		
		
		assertTrue("CashierBill should contain changeDue == 0.0. It contains something else instead: $" 
				+ cashier.bills.get(0).changeDue, cashier.bills.get(0).changeDue == 0);
		
		
		
		//step 4
		assertTrue("Cashier's scheduler should have returned true (needs to react to customer's ReadyToPay), but didn't.", 
					cashier.pickAndExecuteAnAction());
		
		//check postconditions for step 4
		assertTrue("MockCustomer should have logged an event for receiving \"HereIsYourChange\" with the correct change, but his last event logged reads instead: " 
				+ customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Received HereIsYourChange from cashier. Change = 0.0"));
	
		
		assertTrue("CashierBill should contain a bill with state == done. It doesn't.",
				cashier.bills.get(0).state == cashierBillState.done);
		
		assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
				cashier.pickAndExecuteAnAction());
		

	}//end one normal customer scenario
	
	
}*/
