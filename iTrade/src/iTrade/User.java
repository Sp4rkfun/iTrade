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

@Path("/user")
public class User {
	@GET
	@Produces(MediaType.TEXT_HTML)
	@Path("{name}/{password}/{difficulty}")
	public String register(@PathParam("name") String name,@PathParam("password") String password,
			@PathParam("difficulty") String difficulty){
		return ""+db(name,password,Integer.parseInt(difficulty));
		
	}
	
	public static int db(String user, String password, int difficulty) {


		Connection con = null;
		try {
			con = Database.initialize().getConnection();
			CallableStatement proc = con.prepareCall("{? = call register_user (?,?,?)}");
			proc.registerOutParameter(1, java.sql.Types.INTEGER);
			proc.setString(2, user);
			proc.setString(3, password);
			proc.setInt(4, difficulty);
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
