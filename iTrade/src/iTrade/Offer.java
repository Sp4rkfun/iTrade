package iTrade;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("/offer")
public class Offer {
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	@Path("{price}/{type}/{quantity}/{broker}/{fund}")
	public String submit(@PathParam("price") String price, @PathParam("type") String type, @PathParam("quantity") int quantity,
			@PathParam("broker") int broker, @PathParam("fund") String fund){
		Connection con = null;
		String result="<div class=\"blist\"><div class=\"bno\">No.</div><div class=\"bname\" flex=\"10\">Name</div><div class=\"blimit\" flex=\"10\">Limit</div><div class=\"btime\" flex=\"10\">Trade Time</div></div><br/>";
		try {
			con = Database.initialize().getConnection();
			Statement st = con.createStatement();
			st.executeUpdate("INSERT INTO [Offers] (Price, Type, Desired_quantity, Broker_id, Fund_id) VALUES "
					+ "('"+price+"','"+type+"',"+quantity+","+broker+",'"+fund+"')");
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
}
