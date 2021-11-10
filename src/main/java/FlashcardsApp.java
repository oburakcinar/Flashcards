import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.SQLException;

public class FlashcardsApp extends Application {
    private ListDatabase listDatabase;
    private FlashcardDatabase cardDatabase;





    public void init() throws SQLException{
        String listDatabasePath = "jdbc:h2:./list-database";
        listDatabase = new ListDatabase(listDatabasePath);
        String cardDatabasePath = "jdbc:h2:./card-database";
        cardDatabase = new FlashcardDatabase(cardDatabasePath);
    }

    public void start(Stage window) throws SQLException{
        CardListView cardListView = new CardListView(listDatabase, cardDatabase);

        BorderPane layout = new BorderPane();

        Button listButton = new Button("Show lists");
        Button addListButton = new Button("Add list");

        HBox buttonMenu = new HBox();
        buttonMenu.setSpacing(10);
        buttonMenu.setPadding(new Insets(20,0,10,30));
        buttonMenu.getChildren().addAll(listButton, addListButton);

        layout.setTop(buttonMenu);
        layout.setCenter(cardListView.getView());


        listButton.setOnMouseClicked(event -> {
            try{
                layout.setCenter(cardListView.getView());
            } catch (Exception e){

            }

        });

        addListButton.setOnMouseClicked(event -> {
            layout.setCenter(addListView());
        });


        Scene scene = new Scene(layout,1366,700);

        
        window.setScene(scene);
        window.show();
    }


    private Parent addListView(){
        GridPane layout = new GridPane();
        layout.setVgap(10);
        layout.setPadding(new Insets(5,0,0,27));

        Text newList = new Text("New list name: ");
        TextField newListField = new TextField();
        Button addNewListButton = new Button("Add new list");
        Text message = new Text("");
        layout.add(newList,0,1);
        layout.add(newListField,1,1);
        layout.add(addNewListButton,1,2);
        layout.add(message,1,3);

        addNewListButton.setOnMouseClicked(event -> {
            try{
                if(!newListField.getText().equals("")){
                    listDatabase.addToList(newListField.getText());
                    cardDatabase.createConnectionAndEnsureDatabase(newListField.getText());
                    newListField.clear();
                    message.setFill(Color.GREEN);
                    message.setText("List successfully added");
                } else {
                    message.setFill(Color.RED);
                    message.setText("List name can not be blank!");
                }
            } catch (Exception e){

            }

        });

        return layout;
    }

    public static void main(String[] args) {
        launch(FlashcardsApp.class);
    }
}
