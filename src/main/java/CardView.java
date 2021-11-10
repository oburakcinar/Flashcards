
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class CardView {
    private FlashcardDatabase cardDatabase;
    private ListDatabase listDatabase;

    public CardView(FlashcardDatabase cardDatabase, ListDatabase listDatabase){
        this.cardDatabase = cardDatabase;
        this.listDatabase = listDatabase;
    }

    public Parent getView(String listName){
        BorderPane layout = new BorderPane();
        TableView tableView = createTable(listName);

        layout.setLeft(tableView);

        VBox rightPanel = new VBox();
        rightPanel.setPadding(new Insets(15,95,15,15));
        rightPanel.setSpacing(20);

        GridPane updatePanel = new GridPane();
        updatePanel.setVgap(10);
        updatePanel.setHgap(5);

        HBox subMenu = new HBox();
        Button newItemButton = new Button("Add new Item");
        newItemButton.setTranslateX(105);
        Button clearButton = new Button("Delete list");
        clearButton.setTranslateX(105);
        Button practiceButton = new Button("Practice");
        practiceButton.setTranslateX(105);

        subMenu.getChildren().addAll(newItemButton, clearButton, practiceButton);
        subMenu.setSpacing(10);

        Text info = new Text("Click on an item to edit or delete it.");
        info.setTranslateX(50);

        Text updateTerm = new Text("Update term: ");
        TextArea updateTermArea = new TextArea();
        updateTermArea.setWrapText(true);
        Button updateTermButton = new Button("Update");
        Text updateDef = new Text("Update definition: ");
        TextArea updateDefArea = new TextArea();
        updateDefArea.setWrapText(true);
        Button updateDefButton = new Button("Update");
        Text updateMessage = new Text(" ");
        updateMessage.setFill(Color.GREEN);
        Button deleteButton = new Button("Delete");

        updateTermArea.setMaxWidth(250);
        updateTermArea.setMaxHeight(75);
        updateDefArea.setMaxWidth(250);
        updateDefArea.setMaxHeight(75);

        updatePanel.add(updateTerm, 0,1);
        updatePanel.add(updateTermArea, 1,1);
        updatePanel.add(updateTermButton, 2,1);
        updatePanel.add(updateDef, 0,2);
        updatePanel.add(updateDefArea, 1,2);
        updatePanel.add(updateDefButton, 2,2);
        updatePanel.add(updateMessage, 1,3);
        updatePanel.add(deleteButton, 1,4);

        rightPanel.getChildren().addAll(subMenu, info);

        tableView.setOnMouseClicked((event) -> {
            if(tableView.getSelectionModel().getSelectedItem() != null){
                rightPanel.getChildren().clear();
                rightPanel.getChildren().addAll(subMenu, updatePanel);
            }

        });


        clearButton.setOnMouseClicked(event -> {
            layout.getChildren().clear();
            try{
                cardDatabase.removeAll(listName);
                listDatabase.removeItem(listName);
                updateTable(tableView,listName);
                layout.setCenter(new CardListView(listDatabase, cardDatabase).getView());
                layout.setScaleX(1.047);
                layout.setScaleX(1.047);
            } catch(Exception e){

            }

        });

        newItemButton.setOnMouseClicked(event -> {
            rightPanel.getChildren().clear();
            rightPanel.getChildren().addAll(subMenu, createAddCardView(tableView, listName));
        });

        practiceButton.setOnMouseClicked(event -> {
            layout.getChildren().clear();
            layout.setCenter(new PracticeView(cardDatabase, listName).getPracticeView());
        });

        deleteButton.setOnMouseClicked(event-> {
            try{
                Flashcard currentCard = (Flashcard) tableView.getSelectionModel().getSelectedItem();
                cardDatabase.removeCard(listName, currentCard);
                updateTable(tableView,listName);
                updateDefArea.clear();
                updateTermArea.clear();
                rightPanel.getChildren().remove(updatePanel);
                rightPanel.getChildren().add(info);
            } catch (Exception e){

            }

        });

        updateTermButton.setOnMouseClicked(event-> {
            try{
                if(!updateTermArea.getText().equals("")){
                    Flashcard currentCard = (Flashcard) tableView.getSelectionModel().getSelectedItem();
                    cardDatabase.updateTerm(listName, currentCard, updateTermArea.getText());
                    updateTable(tableView,listName);
                    rightPanel.getChildren().remove(updatePanel);
                    updateTermArea.clear();
                    updateDefArea.clear();
                }

            } catch (Exception e){

            }

        });

        updateDefButton.setOnMouseClicked(event-> {
            try{
                if(!updateDefArea.getText().equals("")){
                    Flashcard currentCard = (Flashcard) tableView.getSelectionModel().getSelectedItem();
                    cardDatabase.updateDefinition(listName, currentCard, updateDefArea.getText());
                    updateTable(tableView,listName);
                    rightPanel.getChildren().remove(updatePanel);
                    updateDefArea.clear();
                    updateTermArea.clear();
                }

            } catch (Exception e){

            }

        });

        layout.setRight(rightPanel);

        return layout;
    }

    private TableView createTable(String listName) {
        try {
            ObservableList<Flashcard> data = cardDatabase.getObservableCardList(listName);
            TableView<Flashcard> tableView = new TableView<Flashcard>(data);
            tableView.setMaxHeight(600);


            TableColumn<Flashcard, String> column1 = new TableColumn<>("Term");
            column1.setCellValueFactory(new PropertyValueFactory<>("term"));
            column1.setStyle("-fx-alignment: CENTER;");
            column1.setMinWidth(250);

            TableColumn<Flashcard, String> column2 = new TableColumn<>("Definition");
            column2.setCellValueFactory(new PropertyValueFactory<>("definition"));
            column2.setStyle("-fx-alignment: CENTER;");
            column2.setMinWidth(350);

            tableView.getColumns().add(column1);
            tableView.getColumns().add(column2);

            return tableView;

        } catch(Exception e){
            return null;
        }
    }

    private void updateTable(TableView tableView, String listName){
        try {
            tableView.getItems().clear();
            for (Flashcard card : cardDatabase.getCardList(listName)) {
                tableView.getItems().add(card);
            }
        } catch (Exception e) {

        }
    }


    private Parent createAddCardView(TableView tableView, String listName){
        GridPane layout = new GridPane();


        Text term = new Text("Term: ");
        TextArea termArea = new TextArea();
        termArea.setWrapText(true);
        Text definition = new Text("Definition: ");
        TextArea definitionArea = new TextArea();
        definitionArea.setWrapText(true);
        Button addButton = new Button("Add");
        Text message = new Text("");


        layout.add(term, 0, 0);
        layout.add(termArea, 1, 0);
        layout.add(definition, 0, 1);
        layout.add(definitionArea, 1, 1);
        layout.add(addButton, 1,2);
        layout.add(message, 1,3);

        termArea.setMaxWidth(250);
        termArea.setMaxHeight(75);
        definitionArea.setMaxWidth(250);
        definitionArea.setMaxHeight(75);

        layout.setHgap(6);
        layout.setVgap(15);
        layout.setPadding(new Insets(30,0,0,100));

        addButton.setOnMouseClicked((event) -> {
            String termToAdd = termArea.getText();
            String defToAdd = definitionArea.getText();
            if(!termToAdd.equals("") && !defToAdd.equals("")) {
                boolean bool = true;
                try{
                    for (Flashcard card : cardDatabase.getCardList(listName)){
                        if(card.getTerm().equals(termToAdd) && card.getDefinition().equals(defToAdd)) {
                            bool = false;
                            message.setText("Instrument already exists");
                            message.setFill(Color.RED);
                        }
                    }
                } catch(Exception e){

                }

                if(bool) {
                    try {
                        cardDatabase.add(listName, termToAdd, defToAdd);
                        termArea.clear();
                        definitionArea.clear();
                        message.setText("Instrument successfully added");
                        message.setFill(Color.GREEN);
                        updateTable(tableView, listName);
                    }catch (Exception e){

                    }

                }

        } else {
            message.setText("Please fill all the fields!");
            message.setFill(Color.RED);
        }

    });


        return layout;
    }

}
