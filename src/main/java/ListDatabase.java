import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ListDatabase {
    private String databasePath;

    public ListDatabase(String databasePath) {
        this.databasePath = databasePath;
    }


    public List<String> getList() throws SQLException {
        List<String> items = new ArrayList<>();
        try (Connection connection = createConnectionAndEnsureDatabase();
             ResultSet results = connection.prepareStatement("SELECT * FROM List").executeQuery()) {

            while (results.next()) {
                items.add(results.getString("name"));

            }
        }
        return items;
    }


    public void addToList(String listName) throws SQLException {
        try (Connection connection = createConnectionAndEnsureDatabase()) {
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO List (name) VALUES (?)");
            stmt.setString(1, listName);



            stmt.executeUpdate();
        }
    }

    public void removeItem(String listName) throws SQLException {
        try (Connection connection = createConnectionAndEnsureDatabase()) {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM List WHERE name = ?");
            stmt.setString(1, listName);
            stmt.executeUpdate();
        }
    }

    public Connection createConnectionAndEnsureDatabase() throws SQLException {
        Connection conn = DriverManager.getConnection(this.databasePath, "sa", "");
        try {
            conn.prepareStatement("CREATE TABLE List (name varchar(255) NOT NULL)").execute();

        } catch (SQLException t) {
        }

        return conn;
    }

}
