package iTrade;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/policy")
public class Policy {
	@GET
	@Produces(MediaType.TEXT_HTML)
	@Path("{type}/{frequency}/{condition}")
	public String register(@PathParam("type") String type,@PathParam("frequency") String frequency,
			@PathParam("condition") String condition){
		return ""+db(type,frequency,condition);
	}
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	@Path("/all")
	public String all(){
		Connection con = null;
		String result="<div class=\"blist\"><div class=\"bno\">No.</div><div class=\"bname\" flex=\"10\">Type</div><div class=\"blimit\" flex=\"10\">Frequency</div><div class=\"btime\" flex=\"10\">Condition</div></div><br/>";
		try {
			con = Database.initialize().getConnection();
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM [Policy]");
			int cnt = 1;
			while (rs.next()) {
				result+="<div class=\"blist\"><div class=\"bno\">"+(cnt++)+"</div><div class=\"bname\">"+rs.getString("Type")+"</div><div class=\"blimit\">"+rs.getString("Frequency")+"</div><div class=\"btime\">"+rs.getString("Condition")+"</div>"
						+ "<div class=\"binput\"><input type=\"submit\" value=\"Add\" onClick=\"addToBroker("+rs.getString("Rule_id")+")\"></div></div><br/>";
			}
			rs.close();
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (con != null)
				try {
					con.close();
				} catch (Exception ignore) {
				}
		}
		return result;
	}
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	@Path("/all/{broker}")
	public String allByBroker(@PathParam("broker")String broker){
		Connection con = null;
		String result="<div class=\"blist\"><div class=\"bno\">No.</div><div class=\"bname\" flex=\"10\">Type</div><div class=\"blimit\" flex=\"10\">Frequency</div><div class=\"btime\" flex=\"10\">Condition</div></div><br/>";
		try {
			con = Database.initialize().getConnection();
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM [Policy], [Has_policy] WHERE Broker_id = "+broker+" AND Has_policy.Rule_id = Policy.Rule_id");
			int cnt = 1;
			while (rs.next()) {
				result+="<div class=\"blist\"><div class=\"bno\">"+(cnt++)+"</div><div class=\"bname\">"+rs.getString("Type")+"</div><div class=\"blimit\">"+rs.getString("Frequency")+"</div><div class=\"btime\">"+rs.getString("Condition")+"</div>"
						+ "<div class=\"binput\"><input type=\"submit\" value=\"Remove\" onClick=\"removeFromBroker("+rs.getString("Rule_id")+")\"></div></div><br/>";
			}
			rs.close();
			st.close();
			st = con.createStatement();
			rs = st.executeQuery("SELECT * FROM [Policy] WHERE Rule_id NOT IN(SELECT Policy.Rule_id FROM [Policy], [Has_policy] WHERE Broker_id = "+broker+" AND Has_policy.Rule_id = Policy.Rule_id)");
			cnt = 1;
			while (rs.next()) {
				result+="<div class=\"blist\"><div class=\"bno\">"+(cnt++)+"</div><div class=\"bname\">"+rs.getString("Type")+"</div><div class=\"blimit\">"+rs.getString("Frequency")+"</div><div class=\"btime\">"+rs.getString("Condition")+"</div>"
						+ "<div class=\"binput\"><input type=\"submit\" value=\"Add\" onClick=\"addToBroker("+rs.getString("Rule_id")+")\"></div></div><br/>";
			}
			rs.close();
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (con != null)
				try {
					con.close();
				} catch (Exception ignore) {
				}
		}
		return result;
	}
	@GET
	@Produces(MediaType.TEXT_HTML)
	@Path("{broker}/{policy}")
	public String addPolicyToBroker(@PathParam("broker") String broker, @PathParam("policy") String policy){
		Connection con = null;
		String result="<div class=\"blist\"><div class=\"bno\">No.</div><div class=\"bname\" flex=\"10\">Type</div><div class=\"blimit\" flex=\"10\">Frequency</div><div class=\"btime\" flex=\"10\">Condition</div></div><br/>";
		try {
			con = Database.initialize().getConnection();
			Statement st = con.createStatement();
			st.executeUpdate("INSERT INTO [Has_policy] VALUES ("+broker+","+policy+")");
			//rs.close();
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (con != null)
				try {
					con.close();
				} catch (Exception ignore) {
				}
		}
		return allByBroker(broker);
	}
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	@Path("remove/{broker}/{policy}")
	public String removePolicy(@PathParam("broker") String broker, @PathParam("policy") String policy){
		Connection con = null;
		try {
			con = Database.initialize().getConnection();
			Statement st = con.createStatement();
			st.executeUpdate("DELETE FROM [Has_policy] WHERE Broker_id ="+broker+" AND Rule_id = "+policy);
			//rs.close();
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (con != null)
				try {
					con.close();
				} catch (Exception ignore) {
				}
		}
		return allByBroker(broker);
	}
	
	public static int db(String name, String limit, String tradeTime) {


		Connection con = null;
		try {
			con = Database.initialize().getConnection();
			CallableStatement proc = con.prepareCall("{call create_policy (?,?,?)}");
			proc.setString(1, name);
			proc.setString(2, limit);
			proc.setString(3, tradeTime);
			proc.executeUpdate();
			proc.close();
			return 0;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (con != null)
				try {
					con.close();
				} catch (Exception ignore) {
				}
		}
		return 0;
	}
	
}
