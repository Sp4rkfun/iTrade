package iTrade.policy;

public class Policy {
	public static Policy CreatePolicy(String type, String frequency, String condition){
		switch (type) {
		case "quota":
			return new Quota(condition,frequency);
		case "comission":
			return new Comission();
		default:
			return new Policy();
		}
	}
	public void runPolicy(){
		//System.out.println("Running policy");
	}
	public static class Quota extends Policy{
		String[] condition;
		String frequency="";
		public Quota(String condition,String frequency) {
			this.condition = condition.split(" ");
		}
		public void beforeOffer(){
			
		}
		
		@Override
		public void runPolicy() {
			//
			switch (frequency) {
			case "":
				
				break;

			default:
				break;
			}
		}
	}
	public static class Comission extends Policy{
		
	}
}
