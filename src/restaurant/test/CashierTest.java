package restaurant.test;

import restaurant.CashierAgent;
import restaurant.WaiterAgent;
import restaurant.WaiterAgent.MyCustomers;
import restaurant.MarketAgent;
import restaurant.test.mock.MockCook;
import restaurant.test.mock.MockCustomer;
import restaurant.test.mock.MockHost;
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
	MockMarket market3;
	MockCook cook;
	MockHost host;
	
	
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
		market3 = new MockMarket("mockmarket3");
		cook = new MockCook("mockcook");
		host = new MockHost("mockhost");
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
		//test two complete

	}
	
	public void testThreeNormalCustomerScenario()
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
		customer.setWaiter(waiter);
		customer.wallet = 50;
		//waiter.myCustomers.add(new MyCustomers() customer);
		
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
		//step 2 - 
		market1.SendBill();
		assertEquals("Cashier should have a bill for market1, it doesn't.",cashier.Markets.get(0).GetBill(),3*9.99);

		//step 3 - pay bill
		//cashier.PayMarket(cashier.Markets.get(0));
		cashier.pickAndExecuteAnAction();
		assertFalse("Cashier should have paid the bill.  It hasn't.",cashier.CASHMONEY==100);
		assertEquals("Cashier should have not have a bill for market1, it does.",cashier.Markets.get(0).GetBill(),0.00);
		assertEquals("Market1 should not have a bill.  It does.", market1.owed, 0.00);
		
		//cook.msgNewOrder(waiter, 0, "Steak");
		//waiter.msgOrderReady("Steak", 0);
		//customer.msgDeliveredFood();
		//lead ups above
		//step 2 - get check
		waiter.msgCheckPlease(customer);
		assertEquals("Cashier should have a bill, he doesn't", cashier.Customers.get(0).customer, customer);
		assertEquals("Bill is not 15.99", cashier.Customers.get(0).bill, 15.99);
		
		//step 3
		cashier.pickAndExecuteAnAction();
		assertEquals("Cashier should no longer have customers, it does.", cashier.Customers.size(),0);
		assertEquals("Cashier should have been paid, it was not.", cashier.CASHMONEY, (115.99-9.99*3));
		assertEquals("Customer did not pay from wallet, it should have.", customer.wallet, (50-15.99));
	
		
		//test three complete

	}
	
	public void testFourCustomerOwesFromAPreviousVisit()
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
		customer.setWaiter(waiter);
		customer.wallet = 50;
		customer.bill = 15.99;
		customer.setHost(host);
		//waiter.myCustomers.add(new MyCustomers() customer);
		
		//assert everything is in default state
		assertEquals("Cashier should have 0 customers in it. It doesn't.",cashier.Customers.size(), 0);		
		assertEquals("Cashier should have 2 markets in it. It doesn't.",cashier.Markets.size(), 2);	
		
		
		assertEquals("Cook should have 2 markets in it.  It doesn't.",cook.Markets.size(),2);
		assertEquals("Cook should have 0 steak in it.  It doesn't.",cook.inventory.GetAmountOf("Steak"), 0);
		
		assertEquals("Market1 should have no bill.  It does.", market1.owed,0.00);
		assertFalse("Customer has a no bill, it should.", customer.bill==0);
		
		//step 1
		customer.goToRestaurant();
		
		assertEquals("Customer has a bill, he shouldn't.",customer.bill,0.00);
		assertEquals("Cashier should have been paid, he wasn't.",cashier.CASHMONEY, 115.99);
		
		//test four complete
	
	}
	
	public void testFiveTwoMarketsOutOfGoodsUseThird()
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
		market3.cashier = cashier;
		waiter.cashier = cashier;
		cook.AddMarket(market1);
		cook.AddMarket(market2);
		cook.AddMarket(market3);
		cashier.addMarket(market1);
		cashier.addMarket(market2);
		cashier.addMarket(market3);
		market1.SetCook(cook);
		market2.SetCook(cook);
		market3.SetCook(cook);
		cashier.CASHMONEY = 100;
		
		market1.inventory.AmountOfSteak = 0;
		market2.inventory.AmountOfSteak = 0;
		
		//assert everything is in default state
		assertEquals("Cashier should have 0 customers in it. It doesn't.",cashier.Customers.size(), 0);		
		assertEquals("Cashier should have 3 markets in it. It doesn't.",cashier.Markets.size(), 3);	
		
		
		assertEquals("Cook should have 3 markets in it.  It doesn't.",cook.Markets.size(),3);
		assertEquals("Cook should have 0 steak in it.  It doesn't.",cook.inventory.GetAmountOf("Steak"), 0);
		
		assertEquals("Market1 should have no bill.  It does.", market1.owed,0.00);
		assertEquals("Market1 should have no bill.  It does.", market2.owed,0.00);
		assertEquals("Market1 should have no bill.  It does.", market3.owed,0.00);

		
		//step 1 - restock
		cook.Restock("Steak", 2);
		assertEquals("Market1 shouldn't have a bill.  It does." + market1.owed, market1.owed,0.00);
		assertEquals("Cashier should have no bill for market1, it does.",cashier.Markets.get(0).GetBill(), 0.00);
		assertEquals("Market1 is not out of steak, it should be.", market1.inventory.GetAmountOf("Steak"),0);
		//assert it worked
		cook.Restock("Steak", 2);
		assertEquals("Market2 shouldn't have a bill.  It does." + market2.owed, market2.owed,0.00);
		assertEquals("Cashier should have no bill for market2, it does.",cashier.Markets.get(1).GetBill(), 0.00);
		assertEquals("Market2 is not out of steak, it should be.", market2.inventory.GetAmountOf("Steak"),0);
		cook.Restock("Steak", 2);
		assertEquals("Market3 should have a bill.  It doesn't." + market3.owed, market3.owed,2*9.99);
		assertEquals("Cashier should have no bill for market2, it does.",cashier.Markets.get(2).GetBill(), 0.00);
		assertEquals("Market2 is out of steak, it shouldn't be.", market3.inventory.GetAmountOf("Steak"),1);
		
		//step 2 - 
		market1.SendBill();
		assertEquals("Cashier should have a bill for market1, it doesn't.",cashier.Markets.get(0).GetBill(),0.00);
		market2.SendBill();	
		assertEquals("Cashier should have a bill for market2, it doesn't.",cashier.Markets.get(1).GetBill(),0.00);
		market3.SendBill();	
		assertEquals("Cashier should have a bill for market3, it doesn't.",cashier.Markets.get(2).GetBill(),2*9.99);

		
		//step 3 - pay bill
		//cashier.PayMarket(cashier.Markets.get(0));
		cashier.pickAndExecuteAnAction();
		assertFalse("Cashier should have paid the bill.  It hasn't.",cashier.CASHMONEY==100);
		assertEquals("Cashier should have not have a bill for market1, it does.",cashier.Markets.get(0).GetBill(),0.00);
		assertEquals("Market1 should not have a bill.  It does.", market1.owed, 0.00);
		assertEquals("Cashier should have not have a bill for market2, it does.",cashier.Markets.get(1).GetBill(),0.00);
		assertEquals("Market2 should not have a bill.  It does.", market2.owed, 0.00);
		assertEquals("Cashier should have not have a bill for market3, it does.",cashier.Markets.get(2).GetBill(),0.00);
		assertEquals("Market3 should not have a bill.  It does.", market3.owed, 0.00);
		
		//test five complete
	
	}
	public void testSixCantPayMarketsScenario()
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
		cashier.CASHMONEY = 10;
		
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
		
		//step 2 - 
		market1.SendBill();
		assertEquals("Cashier should have a bill for market1, it doesn't.",cashier.Markets.get(0).GetBill(),3*9.99);

		
		//step 3 - pay bill
		//cashier.PayMarket(cashier.Markets.get(0));
		cashier.pickAndExecuteAnAction();
		assertEquals("Cashier should be in debt now.  It isn't.",cashier.CASHMONEY,-19.97);
		assertEquals("Cashier should have not have a bill for market1, it does.",cashier.Markets.get(0).GetBill(),0.00);
		assertEquals("Market1 should not have a bill.  It does.", market1.owed, 0.00);
		//test 6 complete

	}
}
