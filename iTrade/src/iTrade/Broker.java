package iTrade;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

@Path("/broker")
public class Broker {
	@GET
	@Produces(MediaType.TEXT_HTML)
	@Path("{name}/{limit}/{tradeTime}")
	public String register(@PathParam("name") String name,@PathParam("limit") String limit,
			@PathParam("tradeTime") String tradeTime){
		return ""+db(name,Integer.parseInt(limit),Integer.parseInt(tradeTime));
		
	}
	@GET
	@Produces(MediaType.TEXT_HTML)
	@Path("/all")
	public String list(@Context HttpServletRequest req){
		Connection con = null;
		String header = "Available";
		if(req.getSession().getAttribute("user")==null)header = "Brokers";
		String result="<span class=\"fundsheader\">"+header+"</span>"
				+ "<div class=\"blistt\"><div class=\"bnot\">No.</div><div class=\"bnamet\">Name</div><div class=\"blimitt\">Limit</div><div class=\"btimet\">Trade Time</div>"
				+ "<div class=\"btimet\">Commission</div><div class=\"btimet\">Initial Fee</div><div class=\"btimet\">Minimum Deposit</div><div class=\"bgap\"></div></div><br/>";
		try {
			con = Database.initialize().getConnection();
			CallableStatement st = con.prepareCall("{call display_brokers(?)}");
			st.setString(1,(String) req.getSession().getAttribute("user"));
			ResultSet rs = st.executeQuery();
			int cnt = 1;
			while (rs.next()) {
				result+="<div class=\"blist\"><div class=\"bno\">"+(cnt++)+"</div><div class=\"bname\">"+
			rs.getString("Name")+"</div><div class=\"blimit\">"+
						rs.getInt("Limit")+"</div>"
								+ "<div class=\"btime\">"+rs.getInt("Trade_time")+"</div>"
								+ "<div class=\"btime\">"+rs.getInt("commission")+"</div>"
								+ "<div class=\"btime\">"+rs.getInt("initial_fee")+"</div>"
								+ "<div class=\"btime\">"+rs.getInt("initial_deposit")+"</div>"
						+ "<div class=\"binput\"><input type=\"submit\" value=\"Select\" onClick=\"policies("+rs.getInt("Broker_id")+",'"+rs.getString("Name")+"');\"></div></div><br/>";
			}
			result+="</div>";
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
	@Path("/all/has")
	public String listUserBrokers(@Context HttpServletRequest req){
		if(req.getSession().getAttribute("user")==null)return "<div class=\"innerbubble\">";
		Connection con = null;
		String result="<div class=\"innerbubble\"><div class=\"innerbubble\"><span class=\"fundsheader\">Using</span>"
				+ "<div class=\"blistt\"><div class=\"bnot\">No.</div><div class=\"bnamet\">Name</div><div class=\"blimitt\" >Limit</div><div class=\"btimet\" >Trade Time</div>"
				+ "<div class=\"btimet\" >Commission</div><div class=\"btimet\">Initial Fee</div><div class=\"btimet\" >Minimum Deposit</div><div class=\"bgap\"></div></div><br/>";
		try {
			con = Database.initialize().getConnection();
			CallableStatement st = con.prepareCall("{call User_Brokers_view(?)}");
			st.setString(1, (String) req.getSession().getAttribute("user"));
			ResultSet rs = st.executeQuery();
			int cnt = 1;
			while (rs.next()) {
				result+="<div class=\"blist\"><div class=\"bno\">"+(cnt++)+"</div>"
						+ "<div class=\"bname\">"+rs.getString("Name")+
						"</div><div class=\"blimit\">"+rs.getInt("Limit")+
						"</div><div class=\"btime\">"+rs.getInt("Trade_time")+"</div>"
						+"<div class=\"btime\">"+rs.getInt("commission")+"</div><div class=\"btime\">"+
						rs.getInt("initial_fee")+"</div>"+"<div class=\"btime\">"+rs.getInt("initial_deposit")+
						"</div><div class=\"binput\"><input type=\"submit\" value=\"Select\" onClick=\"policies("+rs.getInt("Broker_id")+",'"+rs.getString("Name")+"');\"></div></div><br/>";
			}
			//result+="</div>";
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
	@Path("/all/has/dropdown")
	public String brokerDropdown(@Context HttpServletRequest req){
		Connection con = null;
		String result="";
		try {
			con = Database.initialize().getConnection();
			CallableStatement proc = con.prepareCall("{call User_Brokers_view(?)}");
			proc.setString(1,(String) req.getSession().getAttribute("user"));
			proc.executeQuery();
			ResultSet rs = proc.getResultSet();
			int cnt = 1;
			while (rs.next()) {
				result+="<option value=\""+rs.getInt("Broker_id")+"\">"+rs.getString("Name")+": "+rs.getString("Investment")+"</option>";
			}
			rs.close();
			proc.close();
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
	@Path("{id}")
	public String allPolicies(@PathParam("id") int id){
		Connection con = null;
		String result="<div class=\"innerbubble\"><div class=\"blistt\"><div class=\"bno\">No.</div><div class=\"bnamet\" >Name</div><div class=\"blimitt\" >Limit</div><div class=\"btimet\" >Trade Time</div></div><br/>";
		try {
			con = Database.initialize().getConnection();
			CallableStatement st = con.prepareCall("{call display_brokers()}");
			ResultSet rs = st.executeQuery();
			int cnt = 1;
			while (rs.next()) {
				String s="<div>";
				CallableStatement sta = con.prepareCall("{call display_actions(?,?)}");
				sta.setInt(1, id);
				sta.setInt(2, Integer.parseInt(rs.getString("Rule_id")));
				ResultSet rss = sta.executeQuery();
				while(rss.next()){
					s+=rss.getString("Type")+" "+rss.getString("Effect")+"<br>";
				}
				s+="</div>";
				System.out.println(s);
				rss.close();
				sta.close();
				result+="<div class=\"blist\"><div class=\"bno\">"+(cnt++)+"</div><div class=\"bname\">"+rs.getString("Name")+"</div><div class=\"blimit\">"+rs.getInt("Limit")+"</div><div class=\"btime\">"+rs.getInt("Trade_time")+"</div>"
						+ "<div class=\"binput\">"+s+"<input type=\"submit\" value=\"Add\"></div></div><br/></div>";
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
	@Path("add/{id}/{amt}")
	public String addBroker(@Context HttpServletRequest req, @PathParam("id") int id, @PathParam("amt") Float amount){
		Connection con = null;
		String result="<div class=\"blistt\"><div class=\"bno\">No.</div><div class=\"bname\" >Name</div><div class=\"blimit\" >Limit</div><div class=\"btime\" >Trade Time</div></div><br/>";
		try {
			con = Database.initialize().getConnection();
			//Statement st = con.createStatement();
//			CallableStatement st = con.prepareCall("{call update_user_capital(?,?)}");
//			st.setString(1, (String) req.getSession().getAttribute("user")); 
//			st.setFloat(2, amount);
//			st.executeQuery();
//			st.executeUpdate("INSERT INTO [Has_fund_user] VALUES ('"+req.getSession().getAttribute("user")+"', 'BRKR"+id+"', "+amount+")");
//			st.close();
			CallableStatement st = con.prepareCall("{call add_broker_user(?,?,?)}");
			st.setString(1, (String) req.getSession().getAttribute("user"));
			st.setInt(2, id);
			st.setFloat(3, amount);
			st.executeUpdate();
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
	public static int db(String name, int limit, int tradeTime) {


		Connection con = null;
		try {
			con = Database.initialize().getConnection();
			CallableStatement proc = con.prepareCall("{? = call create_broker (?,?,?)}");
			proc.registerOutParameter(1, java.sql.Types.INTEGER);
			proc.setString(2, name);
			proc.setInt(3, limit);
			proc.setInt(4, tradeTime);
			proc.executeUpdate();
			int i = proc.getInt(1);
			proc.close();
			return i;
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
