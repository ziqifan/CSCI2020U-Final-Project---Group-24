import javafx.application.Platform;
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
    private boolean SettingTA = false;
    ClientBackend(TextArea ta) {
        try {
            ta.setText("Joining online server....");
            //Create a socket to connect to the server
            Socket socket = new Socket("localhost" , 8000);

            //Create an input stream to receive data to the server
            fromServer = new DataInputStream(socket.getInputStream());

            //Create an output stream to send data to the server
            toServer = new DataOutputStream(socket.getOutputStream());
            ta.clear();
            ta.setText("Server joined!");
        }
        catch (IOException ex){
            ta.appendText(ex.toString() + '\n');
        }

        new Thread(() -> {
            try {
                while (true) {
                    String InMsg = fromServer.readUTF();
                    if(!InMsg.equals("DONE")){
                        this.NewTextToDisplay += InMsg + '\n';
                        SettingTA = true;
                    }
                    else if(InMsg.equals("DONE")){
                        Platform.runLater(() -> {
                            int CaretPos = ta.getCaretPosition();
                            ta.clear();
                            ta.setText(NewTextToDisplay);
                            ta.positionCaret(CaretPos);
                            OldTextDisplayed = NewTextToDisplay;
                            NewTextToDisplay = "";
                            SettingTA = false;

                        });

                    }
                }
            }catch (IOException ex){
                ta.appendText(ex.toString() + '\n');
            }
        }).start();

    }

    void sendToServer(String _currentWords) throws IOException {
        if(!SettingTA) {
            _currentLine = _currentWords.split("\\n"); //Gets the document and splits it into lines based on listener
            for (int i = 0; i < _currentLine.length; i++) { //Currently prints out the lines being edited real time
                if (_currentLine[i] != null) { //Checks for null before printing
                    //System.out.println(_currentLine[i]); //Send to server
                    //_FileO("src/Resources/Test.txt", _currentWords);
                    toServer.writeUTF(_currentLine[i] + '\n');
                    toServer.flush();
                }
            }
            toServer.writeUTF("DONE");
            toServer.flush();
        }
    }

}


