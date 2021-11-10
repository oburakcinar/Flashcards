
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.*;

import java.util.List;
import java.util.Random;

public class PracticeView {
    private FlashcardDatabase cardDatabase;
    private Flashcard card;
    private String listName;
    private List<Flashcard> list;

    public PracticeView(FlashcardDatabase cardDatabase, String listName){
        this.cardDatabase = cardDatabase;
        this.listName = listName;
        try{
            this.list = this.cardDatabase.getCardList(listName);
        } catch(Exception e){

        }
        this.card = getRandomFlashcard();
    }

    public Parent getPracticeView(){
        GridPane layout = new GridPane();
        layout.setTranslateX(300);
        layout.setVgap(10);

        if(card == null){
            layout.getChildren().clear();
            Text error = new Text("There is no item to practice! Please add new items.");
            error.setFill(Color.RED);
            layout.getChildren().add(error);
            return layout;
        }

        Text title = new Text("List name: " + listName);
        title.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 16));
        Text term = new Text("Term: " + card.getTerm());
        term.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 16));
        term.setFill(Color.GOLD);
        Text guessText = new Text("Your guess:");
        guessText.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 16));
        guessText.setFill(Color.GOLD);
        TextArea defArea = new TextArea();
        Button submitButton = new Button("Submit");
        Text message = new Text("");


        layout.add(title, 0, 0);
        layout.add(term, 0,3);
        layout.add(guessText, 0,4);
        layout.add(defArea,0,5);
        layout.add(submitButton,0,6);
        layout.add(message, 0, 7);

        submitButton.setOnMouseClicked(event -> {
            if(defArea.getText().equals(card.getDefinition())){
                if(list.size() == 0){
                    layout.getChildren().clear();
                    Text error = new Text("Congratulations! Practice is completed.");
                    error.setFill(Color.GREEN);
                    layout.getChildren().add(error);
                }
                card = getRandomFlashcard();
                defArea.clear();
                term.setText("Term: " + card.getTerm());
                message.setText("Correct!");
                message.setFill(Color.GREEN);
            } else {
                message.setText("Incorrect definition");
                message.setFill(Color.RED);
            }
        });

        return layout;
    }

    private Flashcard getRandomFlashcard(){
        try{
            Random rd = new Random();
            int num = rd.nextInt(list.size());
            return list.remove(num);
        } catch (Exception e){
            return null;
        }
    }
}
