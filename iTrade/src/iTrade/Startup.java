package iTrade;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

public class Startup extends HttpServlet{
@Override
public void init() throws ServletException {
	System.out.println("Starting...");
	super.init();
}
}
