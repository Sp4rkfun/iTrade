package iTrade;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

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
		String result="<div class=\"blist\"><div class=\"bno\">No.</div><div class=\"bname\" flex=\"10\">Name</div><div class=\"blimit\" flex=\"10\">Share Price</div><div class=\"btime\" flex=\"10\">Industry</div></div><br/>";
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
			
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM [Fund] WHERE Ticker = '"+name+"'");
			int cnt = 1;
			while (rs.next()) {
				result="<div id=\"sprice\">"+rs.getString("Share_Price")+"</div>"+
						"<div><label for=\"bselect\">Select Broker:</label><div class=\"binput\" id=\"bselecter\"><select id=\"bselect\"></select></div></div>"+
						"<div><label for=\"btype\">Select Type:</label><div class=\"binput\" id=\"btyper\"><select id=\"btype\">"
						+ "<option>Buy</option></select></div></div><input id=\"shares\" type=\"text\"></br><input id=\"subshares\" type=\"submit\" onClick=\"submitOffer();\">";
				
				//result+="<div class=\"blist\"><div class=\"bno\">"+(cnt++)+"</div><div class=\"bname\">"+rs.getString("Ticker")+
				//		"</div><div class=\"blimit\">"+rs.getString("Share_Price")+
				//		"</div><div class=\"btime\">"+rs.getString("Type")+"</div>"+
				//		"<div class=\"binput\"><input class=\"create\" type=\"submit\" value=\"Select\" onClick=\"selectFund('"+rs.getString("Ticker")+"');\"></div></div><br/>";
					//	+ "<div class=\"binput\"><input type=\"submit\" value=\"Add\" onClick=\"addToBroker("+rs.getString("Rule_id")+")\"></div></div><br/>";
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
	@Path("/user")
	public String userShares(@Context HttpServletRequest req){
		Connection con = null;
		String result="<div class=\"blist\"><div class=\"bno\">No.</div><div class=\"bname\" flex=\"10\">Ticker</div><div class=\"blimit\" flex=\"10\">Share Price</div><div class=\"btime\" flex=\"10\">Industry</div></div><br/>";
		try {
			con = Database.initialize().getConnection();
			CallableStatement proc = con.prepareCall("{call User_Brokers_view(?,?,?)}");
			proc.setString(1,(String) req.getSession().getAttribute("user"));
			proc.registerOutParameter(2, Types.INTEGER);
			proc.registerOutParameter(3, Types.FLOAT);
			proc.execute();
			//ResultSet rs = proc.getResultSet();
			//int cnt = 1;
			/*while (rs.next()) {
				result="<div class=\"blist\"><div class=\"bno\">"+cnt++ +"</div><div id=\"sprice\">"+rs.getString("f.Ticker")+"</div>"+
						"<div><label for=\"bselect\">Select Broker:</label><div class=\"binput\" id=\"bselecter\"><select id=\"bselect\"></select></div></div>"+
						"<div><label for=\"btype\">Select Type:</label><div class=\"binput\" id=\"btyper\"><select id=\"btype\">"
						+ "<option>Buy</option></select></div></div><input id=\"shares\" type=\"text\"></br><input id=\"subshares\" type=\"submit\" onClick=\"submitOffer();\">";
				
				//result+="<div class=\"blist\"><div class=\"bno\">"+(cnt++)+"</div><div class=\"bname\">"+rs.getString("Ticker")+
				//		"</div><div class=\"blimit\">"+rs.getString("Share_Price")+
				//		"</div><div class=\"btime\">"+rs.getString("Type")+"</div>"+
				//		"<div class=\"binput\"><input class=\"create\" type=\"submit\" value=\"Select\" onClick=\"selectFund('"+rs.getString("Ticker")+"');\"></div></div><br/>";
					//	+ "<div class=\"binput\"><input type=\"submit\" value=\"Add\" onClick=\"addToBroker("+rs.getString("Rule_id")+")\"></div></div><br/>";
			}*/
			result=""+proc.getFloat(3);
			System.out.println(result);
			//rs.close();
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
