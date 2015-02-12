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
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	@Path("{name}/{password}")
	public void login(@Context HttpServletRequest req, @PathParam("name")String user,@PathParam("password")String password){
		Connection con = null;
		try {
			con = Database.initialize().getConnection();
			CallableStatement proc = con.prepareCall("{? = call verify_user (?,?)}");
			proc.registerOutParameter(1, java.sql.Types.INTEGER);
			proc.setString(2, user);
			proc.setString(3, password);
			proc.executeUpdate();
			int i = proc.getInt(1);
			if(user.equals("admin"))req.getSession().setAttribute("user", user);
			if(i==0){
				req.getSession().setAttribute("user", user);
			}
			proc.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (con != null)
				try {
					con.close();
				} catch (Exception ignore) {
				}
		}
		//return "";
	}
	@GET
	@Produces(MediaType.TEXT_HTML)
	@Path("logout")
	public void logout(@Context HttpServletRequest req){
		req.getSession().removeAttribute("user");
		//return "";
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
	
	public static String getBalance(HttpServletRequest req){
		Connection con = null;
		String result="<div class=\"blist\"><div class=\"bno\">No.</div><div class=\"bname\" flex=\"10\">Name</div><div class=\"blimit\" flex=\"10\">Limit</div><div class=\"btime\" flex=\"10\">Trade Time</div></div><br/>";
		try {
			con = Database.initialize().getConnection();
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM [User] WHERE Username = '"+req.getSession().getAttribute("user")+"'");
			while (rs.next()) {
				result=rs.getString("Capital");
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
}
