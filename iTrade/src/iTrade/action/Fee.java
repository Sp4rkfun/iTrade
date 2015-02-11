package iTrade.action;

public class Fee extends Action {
	float amount;
	
	public Fee(String user, String broker, float amount){
		super(user, broker);
		this.user=user;
	}
	
	@Override
	public void onAction(){
		//Sp to handle fee deduction
	}
}
