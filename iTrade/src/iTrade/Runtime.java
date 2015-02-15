package iTrade;

import java.util.ArrayList;

import org.joda.time.LocalDate;

import iTrade.action.Action;
import iTrade.policy.Parser;
import iTrade.policy.Policy;
public class Runtime extends Thread{
	long duration;
	long temp;
	private final int DAYLENGTH=4000;//120000
	long day=DAYLENGTH,week=7*DAYLENGTH,month=4*7*DAYLENGTH;
	long past=System.currentTimeMillis();
	public static ArrayList<Policy> daily = new ArrayList<Policy>();
	public static ArrayList<Policy> weekly = new ArrayList<Policy>();
	public static ArrayList<Policy> monthly = new ArrayList<Policy>();
	public static LocalDate date = new LocalDate(1900, 1, 1);
	@Override
	public void run() {	
		//Parser.Parse();
		//Action.Initialize();
		while(true){
			try {
				Thread.sleep(5000);
				temp=System.currentTimeMillis();
				duration+=temp-past;
				past=temp;				
				if(duration>=day){
					date = date.plusDays(1);
					//System.out.println("New Day"+date.toString());
					runDailyPolicy();
					day+=DAYLENGTH;
				}
				if(duration>=week){
					//System.out.println("New Week");
					runWeeklyPolicy();
					week+=7*DAYLENGTH;
				}
				if(duration>=month){
					//System.out.println("New Month");
					runMonthlyPolicy();
					month+=4*7*DAYLENGTH;
				}
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void initialize(){
		
	}
	
	public void runTradePolicy(String user){
		
	}
	
	public void runDailyPolicy(){
		synchronized (daily) {
			for(Policy p:daily){
				p.runPolicy();
			}
		}
	}

	public void runWeeklyPolicy(){
		synchronized (weekly) {
			for(Policy p:weekly){
				p.runPolicy();
			}
		}
	}

	public void runMonthlyPolicy(){
		synchronized (monthly) {
			for(Policy p:monthly){
				p.runPolicy();
			}
		}
	}
}
