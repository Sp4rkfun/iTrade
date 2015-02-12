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


@Path("/offer")
public class Offer {
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	@Path("{price}/{type}/{quantity}/{broker}/{fund}")
	public String submit(@Context HttpServletRequest req, @PathParam("price") String price, @PathParam("type") String type, @PathParam("quantity") int quantity,
			@PathParam("broker") int broker, @PathParam("fund") String fund){
		Connection con = null;
		String result="<div class=\"blist\"><div class=\"bno\">No.</div><div class=\"bname\" flex=\"10\">Ticker</div><div class=\"blimit\" flex=\"10\">Share Price</div><div class=\"btime\" flex=\"10\">Industry</div></div><br/>";
		try {
			con = Database.initialize().getConnection();
			CallableStatement proc = con.prepareCall("{call make_offer(?,?,?,?,?,?,?)}");
			proc.setString(1, price);
			proc.setString(2, type);
			proc.setInt(3, quantity);
			proc.setInt(4, broker);
			proc.setString(5, fund);
			proc.setString(6,(String) req.getSession().getAttribute("user"));
			proc.registerOutParameter(7, Types.INTEGER);			
			proc.executeUpdate();
			int val = proc.getInt(7);
			proc.close();
			if(val!=-1){
				if(type=="buy"){
					proc = con.prepareCall("{call match_buy_offer(?,?,?)}");
					proc.setString(1, price);
					proc.setInt(2, quantity);
					proc.setString(3, fund);
					proc.executeQuery();
					ResultSet rs = proc.getResultSet();
					int seller=-1;
					while(rs.next()){
						seller = rs.getInt("Offer_id");
						break;
					}
					rs.close();
					proc.close();
					if(seller!=-1){
						proc = con.prepareCall("{call perform_transaction(?,?)}");
						proc.setInt(1, val);
						proc.setInt(2, seller);
						proc.executeUpdate();
						proc.close();
					}
				}
				else{
					proc = con.prepareCall("{call match_sell_offer(?,?,?)}");
					proc.setString(1, price);
					proc.setInt(2, quantity);
					proc.setString(3, fund);
					proc.executeQuery();
					ResultSet rs = proc.getResultSet();
					int buyer=-1;
					while(rs.next()){
						buyer = rs.getInt("Offer_id");
						break;
					}
					rs.close();
					proc.close();
					if(buyer!=-1){
						proc = con.prepareCall("{call perform_transaction(?,?)}");
						proc.setInt(1, buyer);
						proc.setInt(2, val);
						proc.executeUpdate();
						proc.close();
					}
				}
			}
			System.out.println(result);
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
