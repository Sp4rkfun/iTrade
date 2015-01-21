package iTrade;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.ws.rs.PathParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

// Plain old Java Object it does not extend as class or implements 
// an interface

// The class registers its methods for the HTTP GET request using the @GET annotation. 
// Using the @Produces annotation, it defines that it can deliver several MIME types,
// text, XML and HTML. 

// The browser requests per default the HTML MIME type.

//Sets the path to base URL + /hello
@Path("/hello")
public class Hello {

	// This method is called if TEXT_PLAIN is request
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String sayPlainTextHello() {
		return "Hello Jersey";
	}

	// This method is called if XML is request
	@GET
	@Produces(MediaType.TEXT_XML)
	public String sayXMLHello() {
		return "<?xml version=\"1.0\"?>" + "<hello> Hello Jersey" + "</hello>";
	}

	// This method is called if HTML is request
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String sayHtmlHello() {
		db();
		return "<html> " + "<title>" + "Hello Jersey" + "</title>"
				+ "<body><h1>" + "Hello Jersey" + "</body></h1>" + "</html> ";
	}

	@GET
	@Produces(MediaType.TEXT_HTML)
	@Path("{name}")
	public String sayHi(@PathParam("name") String name) {
		return "<html> " + "<title>" + "Hello, " + name + "</title>"
				+ "<body><h1>" + "Hello, " + name + "</body></h1>" + "</html> ";
	}

	public static void db() {
		System.out.println("printin");
		PoolProperties p = new PoolProperties();
		p.setUrl("jdbc:sqlserver://titan.csse.rose-hulman.edu;databasename=itrade42");
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		p.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		p.setUsername("raspst");
		p.setPassword("****************");
		p.setJmxEnabled(true);
		p.setTestWhileIdle(false);
		p.setTestOnBorrow(true);
		p.setValidationQuery("SELECT 1");
		p.setTestOnReturn(false);
		p.setValidationInterval(30000);
		p.setTimeBetweenEvictionRunsMillis(30000);
		p.setMaxActive(100);
		p.setInitialSize(10);
		p.setMaxWait(10000);
		p.setRemoveAbandonedTimeout(60);
		p.setMinEvictableIdleTimeMillis(30000);
		p.setMinIdle(10);
		p.setLogAbandoned(true);
		p.setRemoveAbandoned(true);
		p.setJdbcInterceptors("org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"
				+ "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
		DataSource datasource = new DataSource();
		datasource.setPoolProperties(p);

		Connection con = null;
		try {
			con = datasource.getConnection();
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM [User]");
			int cnt = 1;
			while (rs.next()) {
				System.out.println((cnt++) 
						+ " User:" + rs.getString("Username") + " Password:"
						+ rs.getString("Password"));
			}
			rs.close();
			st.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (con != null)
				try {
					con.close();
				} catch (Exception ignore) {
				}
		}
	}
}