import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.*;
import java.sql.SQLException;
import java.util.List;

public class CardListView {
    private ListDatabase listDatabase;
    private FlashcardDatabase cardDatabase;


    public CardListView(ListDatabase listDatabase, FlashcardDatabase cardDatabase) {
        this.listDatabase = listDatabase;
        this.cardDatabase = cardDatabase;
    }

    public Parent getView() throws SQLException {
        CardView cardView = new CardView(cardDatabase, listDatabase);

        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(15, 0, 15, 30));


        VBox listLayout = new VBox();
        Text title = new Text("List names");
        title.setTranslateX(60);
        title.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 16));


        ListView cardList = new ListView();

        listLayout.getChildren().addAll(title,cardList);
        listLayout.setSpacing(10);

        List<String> list = listDatabase.getList();
        list.stream().forEach(item -> cardList.getItems().add(item));


        Text info = new Text(300,80, "Click on a list to show its content");
        info.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 16));
        layout.getChildren().add(info);


        cardList.setOnMouseClicked(event -> {
            String currentListName = (String) cardList.getSelectionModel().getSelectedItem();
            if(currentListName != null) {
                layout.setLeft(cardView.getView(currentListName));
                layout.getChildren().remove(info);
            }
        });

        layout.setLeft(listLayout);
        layout.setMinSize(1366,700);

        return layout;
    }

}


