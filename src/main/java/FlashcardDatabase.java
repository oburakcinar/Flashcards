import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FlashcardDatabase {
    private String databasePath;

    public FlashcardDatabase(String databasePath) {
        this.databasePath = databasePath;
    }

    public List<Flashcard> getCardList(String nameOfList) throws SQLException {
        List<Flashcard> cards = new ArrayList<>();
        try (Connection connection = createConnectionAndEnsureDatabase(nameOfList);
             ResultSet results = connection.prepareStatement("SELECT * FROM " + nameOfList).executeQuery()) {

            while (results.next()) {
                cards.add(new Flashcard(results.getString("term"), results.getString("definition")
                        ));
            }
        }
        return cards;
    }

    public ObservableList<Flashcard> getObservableCardList(String nameOfList) throws SQLException {
        ObservableList<Flashcard> cards = FXCollections.observableArrayList();
        try (Connection connection = createConnectionAndEnsureDatabase(nameOfList);
             ResultSet results = connection.prepareStatement("SELECT * FROM " + nameOfList).executeQuery()) {

            while (results.next()) {
                cards.add(new Flashcard(results.getString("term"), results.getString("definition")
                ));
            }
        }
        return cards;
    }


    public void add(String nameOfList, String termToAdd, String defToAdd) throws SQLException {
        try (Connection connection = createConnectionAndEnsureDatabase(nameOfList)) {
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO " + nameOfList + " (term, definition) VALUES (?, ?)");
            stmt.setString(1, termToAdd);
            stmt.setString(2, defToAdd);


            stmt.executeUpdate();
        }
    }



    public void updateTerm(String listName, Flashcard card, String newTerm) throws SQLException {
        try (Connection connection = createConnectionAndEnsureDatabase(listName)) {
            PreparedStatement stmt = connection.prepareStatement("UPDATE " + listName + " SET term = ? WHERE definition = ?");
            stmt.setString(1, newTerm);
            stmt.setString(2, card.getDefinition());
            stmt.executeUpdate();
        }
    }

    public void updateDefinition(String listName, Flashcard card, String newDef) throws SQLException {
        try (Connection connection = createConnectionAndEnsureDatabase(listName)) {
            PreparedStatement stmt = connection.prepareStatement("UPDATE " + listName + " SET definition = ? WHERE term = ?");
            stmt.setString(1, newDef);
            stmt.setString(2, card.getTerm());
            stmt.executeUpdate();
        }
    }

    public void removeCard(String listName, Flashcard card) throws SQLException {
        try (Connection connection = createConnectionAndEnsureDatabase(listName)) {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM " + listName + " WHERE term = ?");
            stmt.setString(1, card.getTerm());
            stmt.executeUpdate();
        }
    }

    public void removeAll(String listName) throws SQLException {
        try (Connection connection = createConnectionAndEnsureDatabase(listName)) {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM " + listName);
            stmt.executeUpdate();
        }
    }

    public Connection createConnectionAndEnsureDatabase(String str) throws SQLException {
        Connection conn = DriverManager.getConnection(this.databasePath, "sa", "");
        try {
            conn.prepareStatement("CREATE TABLE " + str + " (term varchar(1000) NOT NULL, definition varchar(255) )").execute();

        } catch (SQLException t) {
        }

        return conn;
    }

}
