package iTrade.action;

public class Drop extends Action {
	public Drop(String user, String broker){
		super(user, broker);
	}
	
	public void onAction(){
		//sp to drop user broker relation and liquidate funds
		//Remove all associations between user and broker from runtime
	}
}
