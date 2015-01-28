package iTrade;

import java.sql.CallableStatement;
import java.sql.Connection;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

@Path("/broker")
public class Broker {
	@GET
	@Produces(MediaType.TEXT_HTML)
	@Path("{name}/{password}/{difficulty}")
	public String register(@PathParam("name") String name,@PathParam("limit") String limit,
			@PathParam("tradeTime") String tradeTime){
		return ""+db(name,Integer.parseInt(limit),Integer.parseInt(tradeTime));
		
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
