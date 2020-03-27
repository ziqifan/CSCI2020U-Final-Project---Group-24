import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.*;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class OnlineDocClient extends Application {
    // Create a Text Area
    private TextArea textArea = new TextArea();

    private double _startMouseX;
    private double _startMouseY;
    private double _endMouseX;
    private double _endMouseY;

    //Create Border Pane
    private BorderPane borderPane = new BorderPane();
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

        //Implement Exit Button Image
        Image imageX = new Image("Resources/Images/X.png");
        ImageView imageViewX = new ImageView(imageX);
        // Set image view to appropriate size
        imageViewX.setFitWidth(25);
        imageViewX.setFitHeight(25);
        // Implement Exit Button
        Button exitButton = new Button("", imageViewX);
        // Set cursor to hand when touch the button
        exitButton.setCursor(Cursor.HAND);
        // Set Button color
        exitButton.setStyle("-fx-border-color: #F4F4F4; -fx-background-color: #F4F4F4;");
        // Set action when mouse enter
        exitButton.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        exitButton.setStyle("-fx-border-color: white; -fx-background-color: white;");
                    }
                });
        // Set action when mouse exit
        exitButton.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        exitButton.setStyle("-fx-border-color: #F4F4F4; -fx-background-color: #F4F4F4;");
                    }
                });
        // Set action when mouse clicked
        exitButton.setOnAction(e->{
            primaryStage.close();
        });
        // Set exit Button to appropriate position
        exitButton.setLayoutX(345);
        exitButton.setLayoutY(15);

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
        pane.getChildren().addAll(exitButton ,imageView, text,textField, button);

        // Name Scene
        Scene scene = new Scene(pane, 400, 450);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Welcome");
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();
    }

    void documentStage(String name){
        //Initalizes client backend class
        ClientBackend clientObj = new ClientBackend(textArea);
        //Initial stage
        Stage stage = new Stage();

        // Implement logo image
        Image image = new Image("Resources/Images/logo.png");
        ImageView imageView = new ImageView(image);
        // Set image view to appropriate size
        imageView.setFitWidth(120);
        imageView.setFitHeight(90);


        //Implement Exit Button Image
        Image imageX = new Image("Resources/Images/X.png");
        ImageView imageViewX = new ImageView(imageX);
        // Set image view to appropriate size
        imageViewX.setFitWidth(20);
        imageViewX.setFitHeight(20);
        // Implement Exit Button
        Button exitButton = new Button("", imageViewX);
        // Set cursor to hand when touch the button
        exitButton.setCursor(Cursor.HAND);
        // Set Button color
        exitButton.setStyle("-fx-border-color: #F4F4F4; -fx-background-color: #F4F4F4;");
        // Set action when mouse enter
        exitButton.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        exitButton.setStyle("-fx-border-color: white; -fx-background-color: white;");
                    }
                });
        // Set action when mouse exit
        exitButton.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        exitButton.setStyle("-fx-border-color: #F4F4F4; -fx-background-color: #F4F4F4;");
                    }
                });
        // Set action when mouse clicked
        exitButton.setOnAction(e->{
            stage.close();
        });

        //Implement Minimize Button Image
        Image imageM = new Image("Resources/Images/-.png");
        ImageView imageViewM = new ImageView(imageM);
        // Set image view to appropriate size
        imageViewM.setFitWidth(20);
        imageViewM.setFitHeight(20);
        // Implement Minimize Button
        Button miniButton = new Button("", imageViewM);
        // Set cursor to hand when touch the button
        miniButton.setCursor(Cursor.HAND);
        // Set Button color
        miniButton.setStyle("-fx-border-color: #F4F4F4; -fx-background-color: #F4F4F4;");
        // Set action when mouse enter
        miniButton.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        miniButton.setStyle("-fx-border-color: white; -fx-background-color: white;");
                    }
                });
        // Set action when mouse exit
        miniButton.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        miniButton.setStyle("-fx-border-color: #F4F4F4; -fx-background-color: #F4F4F4;");
                    }
                });
        // Set action when mouse clicked
        miniButton.setOnAction(e->{
            stage.setIconified(true);
        });


        // Implement Title text
        Text text = new Text("  Online Document - New");
        // Set the font and color of text
        text.setFont(Font.font(null, FontWeight.BOLD, FontPosture.REGULAR, 25));
        text.setFill(Color.valueOf("#302D22"));

        // Create File Menu
        Menu fileMenu = new Menu("File");
        fileMenu.setStyle("-fx-font-size: 15px;");

        // Create menu items
        MenuItem itemOpen = new MenuItem("Open");
        MenuItem itemDownload = new MenuItem("Download");
        FileChooser fileChooser = new FileChooser();
        _filePicker(fileChooser);
        itemOpen.setOnAction(e->{
            File selectedFile = fileChooser.showOpenDialog(stage);
            if(selectedFile != null) {
                clientObj._FileI(selectedFile, textArea); //Handles opening a txt file
            }
        });

        itemDownload.setOnAction(e-> {
            File selectedFile = fileChooser.showSaveDialog(stage);
            if (clientObj.getCurrent() != null && selectedFile != null) {
                if(selectedFile.length() > 0){ //Checks if the user chooses to replace an existing file. This makes it so .append works
                    selectedFile.delete();
                }
                for (int i = 0; i < clientObj.getCurrent().length; i++) { //Gets the size of our textarea array
                    if (clientObj.getCurrent()[i] != null) {
                        try {
                            clientObj._FileO(selectedFile.getPath(), clientObj.getCurrent()[i]); //Writes each line in a for loop to our text file
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        });

        // Add items to menu
        fileMenu.getItems().addAll(itemOpen);
        fileMenu.getItems().addAll(itemDownload);

        // Create Edit Menu
        Menu editMenu = new Menu("Edit");
        editMenu.setStyle("-fx-font-size: 15px;");
        MenuItem _selectAll = new MenuItem("Select All");
        MenuItem _Highlight = new MenuItem("Highlight");

        editMenu.getItems().addAll(_selectAll, _Highlight);
        _selectAll.setOnAction(e->{
            textArea.selectAll();
        });

        textArea.setOnMouseClicked(new EventHandler<MouseEvent>() { //Keeps track of first click when selecting
            @Override
            public void handle(MouseEvent event) {
                _startMouseX = event.getSceneX();
                _startMouseY = event.getSceneY();

                System.out.println(_startMouseX + " " + _startMouseY);
            }
        });

        textArea.setOnMouseReleased(new EventHandler<MouseEvent>() {//Keeps track of release point when selecting
            @Override
            public void handle(MouseEvent event) {
                _endMouseX = event.getSceneX();
                _endMouseY = event.getSceneY();
            }
        });

        _Highlight.setOnAction(e->{
            //textArea.setBlendMode(BlendMode.ADD);
            Text _selected = new Text(textArea.getSelectedText()); //Gets the text that was selected prior to pressing highlight
            _selected.setFont(textArea.getFont());

            //We create a textbox to get the bounds of our width and height to know how big our rectangle needs to be according to font and size of text
            double _txtWidth = _selected.getBoundsInLocal().getWidth();
            double _txtHeight = _selected.getBoundsInLocal().getHeight();
            double _txtX = _selected.getBoundsInLocal().getMaxX();
            double _txtY = _selected.getBoundsInLocal().getMinY();

            //Creates our rectangle based on the demensions of our text that we selected and where we clicked in our given textarea
            Rectangle _rec = new Rectangle(_startMouseX - _endMouseX,_startMouseY - _endMouseY + _txtY,_txtWidth,_txtHeight);
            _rec.setBlendMode(BlendMode.DARKEN); //We use darken blending to have our yellow rectangle appear behind our text.


            //_rec.setFill(Color.TRANSPARENT);
            _rec.setFill(Color.YELLOW); //Sets our highlight to yellow
            borderPane.getChildren().add(_rec); //Adds ot to our borderPane

        });

        // Create Add-on Menu
        Menu addOnMenu = new Menu("Add-ons");
        addOnMenu.setStyle("-fx-font-size: 15px;");
        // Create Help Menu
        Menu helpMenu = new Menu("Help");
        helpMenu.setStyle("-fx-font-size: 15px;");
        MenuItem _helpItem = new MenuItem("READ ME");
        helpMenu.getItems().addAll(_helpItem);
        _helpItem.setOnAction(e->{
            File _readMeFile = new File("Resources/README.md");
            try {
                Desktop.getDesktop().open(_readMeFile); //Opens our README file
            }catch (Exception ex){
                System.err.println("Error opening README.MD");
                ex.printStackTrace();
            }
        });

        // Create and add menu to the menu bar
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu, editMenu, addOnMenu, helpMenu);
        // Set menu bar's color
        menuBar.setStyle("-fx-border-color: #F4F4F4; -fx-background-color: #F4F4F4;");

        // Implement a text area as the document
        textArea.setPrefSize(1000,1000);
        textArea.setPromptText("Hello " + name + ", please type to start your document."); //Creates prompt text
        textArea.setFocusTraversable(false);
        //Adds a listener to the textArea so we can see how the user is editing in real time. Sends to server.
        //textArea.textProperty().addListener((_Observer, _previousVal, _currentVal) -> {
        ChangeListener<String> TempListener = new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String _previousVal, String  _currentVal) {
                try {
                    //System.out.println("CUR: " + _currentVal);
                    //System.out.println("PREV: " + _previousVal);

                    if(!_currentVal.equals(_previousVal) && !_currentVal.isEmpty() && !_previousVal.isEmpty()){
                        //clientObj.RemoveListener();
                        int offset =  _currentVal.length() - _previousVal.length();
                        //System.out.println("PREV LENGTH: " + _previousVal.length());
                        //System.out.println("CUR LENGTH: " + _currentVal.length());
                        clientObj.sendToServer(_currentVal, textArea, offset);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        clientObj.SetListener(TempListener);
        clientObj.AddListener();
        //});

        // Implement a small menu for manipulate text in text area
        // Text Font
        ComboBox<String> textFont = new ComboBox<>();
        textFont.getItems().addAll("Agency FB", "Algerian", "Arial", "Artifakt Element Italic", "Constantia");
        textFont.setPromptText("Text Font");
        textFont.setOnAction(e->{
            textArea.setFont(Font.font(textFont.getValue(), textArea.getFont().getSize()));
        });
        textFont.setStyle("-fx-border-color: #F4F4F4; -fx-background-color: #F4F4F4;");
        // Text Size
        ComboBox<Integer> textSize = new ComboBox<>();
        textSize.getItems().addAll(1, 2, 3, 4, 5, 6, 8, 12, 15, 18, 20, 30, 50);
        textSize.setPromptText("Text Size");
        textSize.setOnAction(e->{
            textArea.setFont(Font.font(textArea.getFont().getFamily(),textSize.getValue()));
        });
        textSize.setStyle("-fx-border-color: #F4F4F4; -fx-background-color: #F4F4F4;");
        // Text Color
        ColorPicker textColor = new ColorPicker();
        textColor.setPromptText("Text Color");
        textColor.setOnAction(e->{
            textArea.setStyle("-fx-text-fill: #"+ textColor.getValue().toString().substring(1).substring(1) + ";");
        });
        textColor.setStyle("-fx-border-color: #F4F4F4; -fx-background-color: #F4F4F4;");

        // Implement a Online User text
        Text userText = new Text("Online\n Users:");
        // Set the font and color of text
        userText.setFont(Font.font(null, FontWeight.BOLD, FontPosture.REGULAR, 15));
        userText.setFill(Color.valueOf("#302D22"));
        userText.setTextAlignment(TextAlignment.CENTER);

        // Implement HBox for Exit and Minimize Buttons
        HBox ButtonBox = new HBox();
        ButtonBox.setAlignment(Pos.CENTER_RIGHT);
        ButtonBox.setSpacing(5);
        ButtonBox.setPadding(new Insets(5,5,5,5));
        ButtonBox.getChildren().addAll(miniButton, exitButton);

        // Implement HBox for Logo and title
        HBox LogoBox = new HBox();
        LogoBox.setSpacing(30);
        LogoBox.setPadding(new Insets(0,5,5,5));
        // Implement VBox for tile and Menu
        VBox TitleBox = new VBox();
        TitleBox.setAlignment(Pos.BOTTOM_LEFT);
        TitleBox.getChildren().addAll(text, menuBar);
        LogoBox.getChildren().addAll(imageView, TitleBox);

        // Draw a horizontal line to separate area
        Line line = new Line();
        line.setStartX(0);
        line.setStartY(50);
        line.setEndX(980);
        line.setEndY(50);
        line.setStyle("-fx-stroke: #B4B4B4; -fx-stroke-width: 3");

        // Draw a Vertical line to separate area
        Line line2 = new Line();
        line2.setStartX(0);
        line2.setStartY(0);
        line2.setEndX(0);
        line2.setEndY(650);
        line2.setStyle("-fx-stroke: #B4B4B4; -fx-stroke-width: 3");

        // Implement a Vbox for top area
        VBox menuBox = new VBox();
        menuBox.setPadding(new Insets(0,0,0,10));
        menuBox.getChildren().addAll(ButtonBox ,LogoBox, line);

        // Implement a HBox for text setting
        HBox textHBox = new HBox();
        textHBox.getChildren().addAll(textFont, textSize, textColor);

        // Implement a Vbox for centre area
        VBox textBox = new VBox();
        textBox.setPadding(new Insets(10,10,10,10));
        textBox.getChildren().addAll(textHBox ,textArea);

        // Implement a Vbox for online users
        VBox userBox = new VBox();
        userBox.setPadding(new Insets(10,20,10,10));
        userBox.getChildren().addAll(userText, addNewUserOnScreen(name));

        // Implement a Hbox for right area
        HBox lineBox = new HBox();
        lineBox.getChildren().addAll(line2, userBox);

        // Implement and setup the border pane
        borderPane.setTop(menuBox);
        borderPane.setCenter(textBox);
        borderPane.setRight(lineBox);

        // Set up the scene
        Scene scene = new Scene(borderPane,1000,800);
        stage.setScene(scene);
        stage.setTitle("Online Document");
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }

    TextArea getTextArea(){
        return textArea;
    }

    public StackPane addNewUserOnScreen(String name){
        StackPane tmp = new StackPane();
        tmp.setAlignment(Pos.CENTER);

        Circle circle = new Circle();
        circle.setRadius(30);
        circle.setFill(Color.WHITE);
        circle.setStroke(Color.GRAY);
        String firstLetter = name.charAt(0) + "";

        Text text = new Text(firstLetter.toUpperCase());
        text.setFont(Font.font(null, FontWeight.BOLD, FontPosture.REGULAR, 20));

        tmp.getChildren().addAll(circle,text);

        return tmp;
    }

    public void  _filePicker(FileChooser _file){ //Handles the type of file extensions our user can open/save
        _file.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Word Document", "*.docx"),
                new FileChooser.ExtensionFilter("Text Document", "*.txt"),
                new FileChooser.ExtensionFilter("Read Me Document", "*.md")
        );
    }

}
