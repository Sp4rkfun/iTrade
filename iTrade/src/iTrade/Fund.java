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

@Path("/fund")
public class Fund {

	@GET
	@Produces(MediaType.TEXT_HTML)
	@Path("/all")
	public String all(){
		Connection con = null;
		String result="<div class=\"innerbubble\"><div class=\"blistt\"><div class=\"bnot\">No.</div><div class=\"bnamet\" flex=\"10\">Name</div><div class=\"blimitt\" flex=\"10\">Share Price</div><div class=\"btime\" flex=\"10\">Industry</div></div><br/>";
		try {
			con = Database.initialize().getConnection();
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM [Fund] WHERE Type != 'Broker'");
			int cnt = 1;
			while (rs.next()) {
				result+="<div class=\"blist\"><div class=\"bno\">"+(cnt++)+"</div><div class=\"bname\">"+rs.getString("Ticker")+
						"</div><div class=\"blimit\">"+rs.getString("Share_Price")+
						"</div><div class=\"btime\">"+rs.getString("Type")+"</div>"+
						"<div class=\"binput\"><input class=\"create\" type=\"submit\" value=\"Select\" onClick=\"selectFund('"+rs.getString("Ticker")+"');\"></div></div><br/>";
					//	+ "<div class=\"binput\"><input type=\"submit\" value=\"Add\" onClick=\"addToBroker("+rs.getString("Rule_id")+")\"></div></div><br/>";
			}
			rs.close();
			st.close();
			result+="</div>";
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
	@Path("/query/{name}")
	public String transact(@PathParam("name") String name){
		Connection con = null;
		String result="<div class=\"blist\"><div class=\"bno\">No.</div><div class=\"bname\" flex=\"10\">Name</div><div class=\"blimit\" flex=\"10\">Share Price</div><div class=\"btime\" flex=\"10\">Industry</div></div><br/>";
		try {
			con = Database.initialize().getConnection();
			CallableStatement proc = con.prepareCall("{call display_fund(?)}");
			proc.setString(1,name);
			proc.executeQuery();
			ResultSet rs = proc.getResultSet();
			int cnt = 1;
			while (rs.next()) {
				result=	"<div id=\"sprice\">"+rs.getString("Share_Price")+"</div><div><label for=\"bselect\">Select Broker:</label><div class=\"binput\" id=\"bselecter\"><select id=\"bselect\"></select></div></div>"+
						"<div><label for=\"btype\">Select Type:</label><div class=\"binput\" id=\"btyper\"><select id=\"btype\">"
						+ "<option>buy</option><option>sell</option></select></div></div><label for=\"shares\">Select Shares:</label><input id=\"shares\" onkeydown=\"adjustEstimate();\" type=\"text\"></br>"+
						"<div>Share Price: "+rs.getString("Share_Price")+"</div><input id=\"subshares\" type=\"submit\" onClick=\"submitOffer();\">";
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
	@Path("/user/funds")
	public String userShares(@Context HttpServletRequest req){
		Connection con = null;
		String result="<div class=\"innerbubble\"><span class=\"fundsheader\">Shares</span>"
				+ "<div class=\"blistt\"><div class=\"bnot\">No.</div><div class=\"bnamet\" flex=\"10\">Ticker</div><div class=\"blimitt\" flex=\"10\">Shares Owned</div></div><br/>";
		try {
			con = Database.initialize().getConnection();
			CallableStatement proc = con.prepareCall("{call User_Shares_view(?)}");
			proc.setString(1,(String) req.getSession().getAttribute("user"));
			proc.executeQuery();
			ResultSet rs = proc.getResultSet();
			int cnt = 1;
			while (rs.next()) {				
				result+="<div class=\"blist\"><div class=\"bno\">"+(cnt++)+"</div><div class=\"bname\">"+rs.getString("Ticker")+
						"</div><div class=\"blimit\">"+rs.getString("Shares")+
						"</div></div><br/>";
						//+ "<div class=\"binput\"><input type=\"submit\" value=\"Add\" onClick=\"addToBroker("+rs.getString("Rule_id")+")\"></div></div><br/>";
			}
			rs.close();
			proc.close();
			result+="</div>";
			proc = con.prepareCall("{call User_Brokers_view(?)}");
			proc.setString(1,(String) req.getSession().getAttribute("user"));
			proc.executeQuery();
			rs = proc.getResultSet();
			cnt = 1;
			result+="<div class=\"innerbubble\"><span class=\"fundsheader\">Brokers</span>"
					+ "<div class=\"blistt\"><div class=\"bnot\">No.</div><div class=\"bnamet\" flex=\"10\">Name</div><div class=\"blimitt\" flex=\"10\">Investment</div></div><br/>";
			while (rs.next()) {				
				result+="<div class=\"blist\"><div class=\"bno\">"+(cnt++)+"</div><div class=\"bname\">"+rs.getString("Name")+
						"</div><div class=\"blimit\">"+rs.getString("Investment")+
						"</div></div><br/>";
				CallableStatement proc1 = con.prepareCall("{call transaction_history(?,?)}");
				proc1.setString(1,(String) req.getSession().getAttribute("user"));
				proc1.setString(2, rs.getString("Broker_id"));
				proc1.executeQuery();
				ResultSet rs1 = proc1.getResultSet();
				int cnt1 = 1;
				while (rs1.next()) {				
					int mult = 1;
					if(rs1.getString("Buyer_user").equals(req.getSession().getAttribute("user")))
						mult = -1;
					result+="<div class=\"blist\"><div class=\"bno\">"+(cnt1++)+"</div><div class=\"bname\">"+rs1.getString("Fund_id")+
							"</div><div class=\"bname\">"+rs1.getString("Time")+
							"</div><div class=\"blimit\">"+mult*Float.parseFloat(rs1.getString("Sale_price"))*rs1.getInt("No_of_shares")+
							"</div><div class=\"blimit\">"+Float.parseFloat(rs1.getString("Sale_price"))+
							"</div><div class=\"blimit\">"+rs1.getInt("No_of_shares")+
							"</div></div><br/>";
							//+ "<div class=\"binput\"><input type=\"submit\" value=\"Add\" onClick=\"addToBroker("+rs.getString("Rule_id")+")\"></div></div><br/>";
				}
				rs1.close();
				proc1.close();
						//+ "<div class=\"binput\"><input type=\"submit\" value=\"Add\" onClick=\"addToBroker("+rs.getString("Rule_id")+")\"></div></div><br/>";
			}
			rs.close();
			proc.close();
			result+="</div>";
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
	@Path("/user")
	public String userFunds(@Context HttpServletRequest req){
		Connection con = null;
		String result="<div class=\"blist\"><div class=\"bno\">No.</div><div class=\"bname\" flex=\"10\">Ticker</div><div class=\"blimit\" flex=\"10\">Shares Owned</div></div><br/>";
		try {
			con = Database.initialize().getConnection();
			CallableStatement proc = con.prepareCall("{call User_Shares_view(?)}");
			proc.setString(1,(String) req.getSession().getAttribute("user"));
			proc.executeQuery();
			ResultSet rs = proc.getResultSet();
			int cnt = 1;
			while (rs.next()) {				
				result+="<div class=\"blist\"><div class=\"bno\">"+(cnt++)+"</div><div class=\"bname\">"+rs.getString("Ticker")+
						"</div><div class=\"blimit\">"+rs.getString("Shares")+
						"</div></div><br/>";
						//+ "<div class=\"binput\"><input type=\"submit\" value=\"Add\" onClick=\"addToBroker("+rs.getString("Rule_id")+")\"></div></div><br/>";
			}
			rs.close();
			proc.close();
			proc = con.prepareCall("{call User_Brokers_view(?)}");
			proc.setString(1,(String) req.getSession().getAttribute("user"));
			proc.executeQuery();
			rs = proc.getResultSet();
			cnt = 1;
			while (rs.next()) {				
				result+="<div class=\"blist\"><div class=\"bno\">No.</div><div class=\"bname\" flex=\"10\">Name</div><div class=\"blimit\" flex=\"10\">Investment</div></div><br/>";
				result+="<div class=\"blist\"><div class=\"bno\">"+(cnt++)+"</div><div class=\"bname\">"+rs.getString("Name")+
						"</div><div class=\"blimit\">"+rs.getString("Investment")+
						"</div></div><br/>";
				result+="<div class=\"blist\"><div class=\"bno\">No.</div><div class=\"bname\" flex=\"10\">Fund</div><div class=\"bname\" flex=\"10\">Date</div><div class=\"blimit\" flex=\"10\">Total</div>"
						+ "<div class=\"blimit\" flex=\"10\">Share Price</div><div class=\"blimit\" flex=\"10\">Shares</div></div><br/>";
						//+ "<div class=\"binput\"><input type=\"submit\" value=\"Add\" onClick=\"addToBroker("+rs.getString("Rule_id")+")\"></div></div><br/>";
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
	
}

//result+="<div class=\"blist\"><div class=\"bno\">"+(cnt++)+"</div><div class=\"bname\">"+rs.getString("Ticker")+
//		"</div><div class=\"blimit\">"+rs.getString("Share_Price")+
//		"</div><div class=\"btime\">"+rs.getString("Type")+"</div>"+
//		"<div class=\"binput\"><input class=\"create\" type=\"submit\" value=\"Select\" onClick=\"selectFund('"+rs.getString("Ticker")+"');\"></div></div><br/>";
	//	+ "<div class=\"binput\"><input type=\"submit\" value=\"Add\" onClick=\"addToBroker("+rs.getString("Rule_id")+")\"></div></div><br/>";
