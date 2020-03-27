import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.TextArea;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClientBackend extends OnlineDocClient {
    private String[] _currentLine;
    private List<String> _fileLine = new ArrayList<String>();
    //IO streams
    private DataOutputStream toServer = null;
    private DataInputStream fromServer = null;
    private String OldTextDisplayed = "";
    private String NewTextToDisplay = "";
    private String NewInMsg = "";
    private boolean SettingTA = false;
    private ChangeListener<String> MyListener;
    private int CaretPosition = 0;
    private TextArea MyTextArea;
    ClientBackend(TextArea ta) {
        try {
            MyTextArea = ta;
            ta.setText("Joining online server....");
            //Create a socket to connect to the server
            Socket socket = new Socket("localhost" , 8000);

            //Create an input stream to receive data to the server
            fromServer = new DataInputStream(socket.getInputStream());

            //Create an output stream to send data to the server
            toServer = new DataOutputStream(socket.getOutputStream());
            ta.clear();
            //ta.setText("Server joined!");
        }
        catch (IOException ex){
            ta.appendText(ex.toString() + '\n');
        }

        //Creating a new thread to listen for messages from the server, if one is sent it will create
        //a new string and keep adding to that string until a message "DONE" is sent from the server
        //once the "DONE" message is received the text area is cleared and rewritten with the new
        //string, afterwards the caret position is set to resemble exactly where it used to be
        //(since clearing and setting text repositions the caret back to the beginning)
        new Thread(() -> {
            try {
                while (true) {
                    String InMsg = fromServer.readUTF();
                    if(!InMsg.equals("DONE")){
                        System.out.println("Second");
                        this.RemoveListener();
                        this.SettingTA = true;
                        if(!NewInMsg.equals("")){
                            this.NewTextToDisplay += this.NewInMsg + '\n';
                        }
                        this.NewInMsg = InMsg;
                    }
                    else if(InMsg.equals("DONE")){
                        System.out.println("Third");
                        if(!NewInMsg.equals("")){
                            this.NewTextToDisplay += this.NewInMsg + '\n';
                            this.NewInMsg = "";
                        }
                        int CaretPos = ta.getCaretPosition();
                        String tempstring = NewTextToDisplay;
                        Platform.runLater(() -> {

                            System.out.println(CaretPos);
                            ta.clear();
                            ta.setText(tempstring);
                            ta.positionCaret(CaretPosition);
                            OldTextDisplayed = NewTextToDisplay;
                            NewTextToDisplay = "";
                            this.SettingTA = false;
                            this.AddListener();
                        });
                        try {
                            Thread.sleep(25);
                        } catch(InterruptedException ex) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            }catch (IOException ex){
                ta.appendText(ex.toString() + '\n');
            }
        }).start();

    }

    //Dissects the current string by using \n as a separator, then sends those lines to the server to be
    //processed into the .txt file.
    //Once done dissecting, a "DONE" message will be sent to the server to notify it of completion.
    void sendToServer(String _currentWords, TextArea ta, int offset) throws IOException {
        if(this.SettingTA == false) {
            System.out.println("First");
            //this.SettingTA = true;
            this.CaretPosition = ta.getCaretPosition() + offset;
            _currentLine = _currentWords.split("\\n");
            for (int i = 0; i < _currentLine.length; i++) {
                if (!_currentLine[i].equals(null)) {


                    toServer.writeUTF(_currentLine[i] + '\n');
                    toServer.flush();
                }
            }
            toServer.writeUTF("DONE");
            toServer.flush();

        }
    }

    void SetListener(ChangeListener<String> templistener){
        this.MyListener = templistener;
    }
    void AddListener(){
        this.MyTextArea.textProperty().addListener(this.MyListener);
    }
    void RemoveListener(){
        this.MyTextArea.textProperty().removeListener(this.MyListener);
    }

    public String[] getCurrent(){
        return _currentLine; //Returns our string array that holds all of our lines from the textArea
    }

    public void _FileO(String _filePath, String _line) throws IOException {
        System.out.println(_line);
        BufferedWriter _out = new BufferedWriter(new FileWriter(_filePath, true)); //We need to append each line of our file so this must be true
        _out.append(_line);//Appends our line so we can contiune to write each line
        _out.newLine(); //Creates a newline after the first line is written
        _out.flush(); //Flushes our buffer so we don't read the same string again on the next call
        _out.close(); //Closes our file
    }

    public void _FileI(File _file, TextArea _txt) {

        if (_file.exists()) { //Checks if file exists
            String _nextLine;
            try {
                Scanner _search = new Scanner(_file); //Creates scannar to search file
                while (_search.hasNextLine()) { //While nextline exists

                    _nextLine = _search.nextLine(); //Sets our nextline string to whatever our next line is
                    if (_nextLine != null) {
                        _txt.appendText(_nextLine + '\n'); //Appends our current line to our textarea then adds a new line
                    }

                }
            } catch (FileNotFoundException e) { //Error checks
                e.printStackTrace();
            }
        }
    }
}


