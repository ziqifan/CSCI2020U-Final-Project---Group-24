import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;

public class OnlineDocClient extends Application {
    // Create a Text Area
    private TextArea textArea = new TextArea();
    @Override
    public void start(Stage primaryStage) throws Exception {

        // Initial the pan
        Pane pane = new Pane();

        // Implement logo image
        Image image = new Image("Resources/Images/logo.png");
        ImageView imageView = new ImageView(image);
        // Set image view to appropriate size
        imageView.setFitWidth(160);
        imageView.setFitHeight(120);
        // Set image view to appropriate position
        imageView.setX(50);
        imageView.setY(50);

        // Implement a welcome text
        Text text = new Text("Welcome to the online document!\nplease enter your name below.");
        // Set the font and color of text
        text.setFont(Font.font(null, FontWeight.BOLD, FontPosture.REGULAR, 18));
        text.setFill(Color.valueOf("#302D22"));
        // Set text to appropriate position
        text.setX(50);
        text.setY(230);

        // Implement a textfield for user to enter the name
        TextField textField = new TextField();
        // Set the style of text field
        textField.setStyle("-fx-background-radius:10; -fx-background-color:#38BDB0; -fx-text-fill: white; -fx-font-size: 13px;");
        // Set the width of text field
        textField.setPrefColumnCount(23);
        // Set text field to appropriate position
        textField.setTranslateX(50);
        textField.setTranslateY(280);

        // Implement a button for enter a new stage
        Button button = new Button("ENTER");
        // Set the style of button
        button.setStyle("-fx-background-radius:3; -fx-base: #1BACAC;-fx-text-fill: white; -fx-font-size: 15px; -fx-font-weight: BOLD; -fx-pref-width: 100px;");
        // Set button to appropriate position
        button.setTranslateX(245);
        button.setTranslateY(350);

        button.setOnAction(e->{
            // Go to next stage if user entered the name
            if(!textField.getText().trim().isEmpty()){
                System.out.println(textField.getText());
                documentStage(textField.getText());
                primaryStage.close();
            }

        });


        // Add everything to pane
        pane.getChildren().addAll(imageView, text,textField, button);

        // Name Scene
        Scene scene = new Scene(pane, 400, 450);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Welcome");
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();
    }

    void documentStage(String name){
        ClientBackend clientObj = new ClientBackend(textArea);
        Stage stage = new Stage();
        Pane pane = new Pane();
        VBox vBox = new VBox();


        // Create Menu
        Menu fileMenu = new Menu("File");

        // Create menu items
        MenuItem itemDownload = new MenuItem("Download");
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Word Document", "*.docx"),
                new FileChooser.ExtensionFilter("Text Document", "*.txt")
        );
        itemDownload.setOnAction(e->{
            File selectedFile = fileChooser.showOpenDialog(stage);

        });

        // Add items to menu
        fileMenu.getItems().addAll(itemDownload);

        // Create and add menu to the menu bar
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(fileMenu);


        textArea.setPrefSize(1000,800);
        textArea.setPromptText("Hello " + name + "," + " please type to start your document."); //Creates prompt text
        textArea.setFocusTraversable(false);

        //Adds a listener to the textArea so we can see how the user is editing in real time. Sends to server.
        textArea.textProperty().addListener((_Observer, _previousVal, _currentVal) -> {
            try {
                clientObj.sendToServer(_currentVal);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });



        vBox.getChildren().addAll(menuBar, textArea);
        pane.getChildren().add(vBox);
        Scene scene = new Scene(pane,1000,800);
        stage.setScene(scene);
        stage.setTitle("Online Document");
        //stage.setMaximized(true);
        stage.show();
    }

    TextArea getTextArea(){
        return textArea;
    }

}
