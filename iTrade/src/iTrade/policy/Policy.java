package iTrade.policy;

public class Policy {
	public static Policy CreatePolicy(String type){
		switch (type) {
		case "quota":
			return new Quota();

		default:
			return new Policy();
		}
	}
	public void runPolicy(){
		
	}
	public static class Quota extends Policy{
		
	}
}
