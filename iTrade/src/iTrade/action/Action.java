package iTrade.action;

import static iTrade.Runtime.daily;
import static iTrade.Runtime.monthly;
import static iTrade.Runtime.weekly;
import iTrade.Database;
import iTrade.policy.Policy;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class Action {
	public String user, broker;

	public Action(String user, String broker) {
		this.user = user;
		this.broker = broker;
	}

	abstract void onAction();

	public static void addBrokerPolicy(String user, String broker) {
		Connection con = null;
		try {
			con = Database.initialize().getConnection();
			CallableStatement proc = con
					.prepareCall("{call get_broker_policies(?)}");
			// proc.registerOutParameter(1, Types.REF_CURSOR);
			proc.setString(1, broker);
			proc.executeQuery();
			ResultSet rs = proc.getResultSet();
			while (rs.next()) {
				System.out.println(rs.getString("Rule_id") + " "
						+ rs.getString("Type") + " "
						+ rs.getString("Frequency") + " "
						+ rs.getString("Condition"));
				switch (rs.getString("Frequency").trim()) {
				case "daily":
					synchronized (daily) {
						daily.add(Policy.CreatePolicy(rs.getString("Type"),rs.getString("Frequency"),rs.getString("Condition")));
					}
					break;
				case "weekly":
					synchronized (weekly) {
						weekly.add(Policy.CreatePolicy(rs.getString("Type"),rs.getString("Frequency"),rs.getString("Condition")));
					}
					break;
				case "monthly":
					monthly.add(Policy.CreatePolicy(rs.getString("Type"),rs.getString("Frequency"),rs.getString("Condition")));
					break;
				default:
					break;
				}
				// addBrokerAction(rs.getString("Username"),rs.getString("Broker_id"));
			}
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
	}

	public static void Initialize() {
		Connection con = null;
		try {
			con = Database.initialize().getConnection();
			CallableStatement proc = con.prepareCall("{call get_ub_pairs()}");
			// proc.registerOutParameter(1, Types.REF_CURSOR);
			proc.executeQuery();
			ResultSet rs = proc.getResultSet();
			while (rs.next()) {
				System.out.println(rs.getString("Username") + " "
						+ rs.getString("Broker_id"));
				addBrokerPolicy(rs.getString("Username"),
						rs.getString("Broker_id"));
			}
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
	}
}
