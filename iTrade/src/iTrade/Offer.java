package iTrade;

import java.sql.CallableStatement;
import java.sql.Connection;
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
			CallableStatement proc = con.prepareCall("{call make_offer(?,?,?,?,?,?)}");
			//proc.setString(1,(String) req.getSession().getAttribute("user"));
			proc.setString(1, price);
			proc.setString(2, type);
			proc.setInt(3, quantity);
			proc.setInt(4, broker);
			proc.setString(5, fund);
			proc.registerOutParameter(6, Types.INTEGER);
			
			proc.executeUpdate();
			result=""+proc.getInt(6);
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
