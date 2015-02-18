package iTrade.policy;

import iTrade.Database;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Stack;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

public class Parser {
	static class Node{
		public String date, price;
		public Node(String date, String price){
			this.date=date; this.price=price;
		}
	}
	public static void Parse(){
		Reader in;
		Stack<Node> nodes = new Stack<Node>();
		try {
			System.out.println(new File(".").getAbsolutePath());
			in = new FileReader("table (1).csv");
			Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(in);
			for (CSVRecord record : records) {
			   nodes.push(new Node(record.get(0),record.get(1)));
			}
			Connection con = Database.initialize().getConnection();
			while(!nodes.isEmpty()){
				Node pop = nodes.pop();
				Statement st = con.createStatement();
				st.executeUpdate(
				"UPDATE history SET JPM = "+pop.price+" WHERE CONVERT(DATE,'"+pop.date+"') = history.date");
				st.close();
			}
			con.close();
			System.out.println("COMPLETE!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
