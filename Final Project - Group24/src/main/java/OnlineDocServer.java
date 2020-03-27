import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class OnlineDocServer extends Application {

    private TextArea ta = new TextArea();
    private List<String> _fileLine = new ArrayList<String>();
    private int clientNo = 0;
    private List<HandleAClient> AllClients = new ArrayList<>();

    private String _filePath = "Test.txt";
    File _DeleteServerFile = new File(_filePath);
    @Override
    public void start(Stage primaryStage) {
        if(_DeleteServerFile.exists()) {
            _DeleteServerFile.delete();
        }
        Scene scene = new Scene(new ScrollPane(ta), 450, 200);
        primaryStage.setTitle("Online Doc Server"); // Set the stage title
        primaryStage.setScene(scene); // Place the scene in the stage
        primaryStage.show(); // Display the stage

        new Thread( () -> {
            try {
                // Create a server socket
                ServerSocket serverSocket = new ServerSocket(8000);
                ta.appendText("Server started at "
                        + new Date() + '\n');

                while (true) {
                    // Listen for a new connection request
                    Socket socket = serverSocket.accept();

                    // Increment clientNo
                    clientNo++;

                    Platform.runLater( () -> {
                        // Display the client number
                        ta.appendText("Starting thread for client " + clientNo +
                                " at " + new Date() + '\n');

                        // Find the client's host name, and IP address
                        InetAddress inetAddress = socket.getInetAddress();
                        ta.appendText("Client " + clientNo + "'s host name is "
                                + inetAddress.getHostName() + "\n");
                        ta.appendText("Client " + clientNo + "'s IP Address is "
                                + inetAddress.getHostAddress() + "\n");
                    });

                    // Create and start a new thread for the connection
                    new Thread(new HandleAClient(socket, clientNo)).start();
                }
            }
            catch(IOException ex) {
                System.err.println(ex);
            }
        }).start();
    }

    // Define the thread class for handling new connection
    class HandleAClient implements Runnable {
        private Socket socket; // A connected socket
        private int MyNumber;
        private DataInputStream inputFromClient;
        private DataOutputStream outputToClient;
        private String OldMessage = "";
        private String NewMessage = "";
        /**
         * Construct a thread
         */
        public HandleAClient(Socket socket, int ClientNum) {
            this.socket = socket;
            this.MyNumber = ClientNum;
            AllClients.add(this);
        }
        public Socket GetSocket(){
            return this.socket;
        }
        public int GetNumber(){
            return this.MyNumber;
        }
        public void SendString(String Msg){
            if(this.outputToClient != null){
                try {
                    this.outputToClient.writeUTF(Msg);
                    this.outputToClient.flush();
                }catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

        /**
         * Run a thread
         */
        public void run() {
            try {
                // Create data input and output streams
                this.inputFromClient = new DataInputStream(
                        socket.getInputStream());
                this.outputToClient = new DataOutputStream(
                        socket.getOutputStream());

                // Serve the client by processing messages sent from the client and storing them into the txt document,
                //once the changes are processed they are then sent to all other clients.
                while (true) {
                    // Receive Message from client
                    String inmsg = inputFromClient.readUTF();
                    if(!inmsg.equals("DONE")){
                        this.NewMessage += inmsg;
                        //System.out.println("Stuff is adding " + inmsg);
                    }
                    else if(inmsg.equals("DONE")){
                        System.out.println(this.NewMessage);
                        _FileO(_filePath, this.NewMessage);
                        this.OldMessage = this.NewMessage;
                        this.NewMessage = "";
                    }


                    //Sends the final message update to any client that is not the client which sent the data.
                    for (HandleAClient temp:AllClients) {
                        if(temp.GetNumber() != this.MyNumber){
                            _testFileIn(_filePath, temp);
                        }
                    }
                    Platform.runLater(() -> {
                        ta.appendText("Client " + this.MyNumber + " updated the file." + '\n');
                    });
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    void _FileO(String _filePath, String _line) throws IOException {
        System.out.println(_line);
        BufferedWriter _out = new BufferedWriter(new FileWriter(_filePath));
        _out.write(_line);
        _out.flush();
        _out.close();
    }
    void _FileI(String _filePath) {
        File _file = new File(_filePath);


        if (_file.exists()) {
            String _nextLine;
            try {
                Scanner _search = new Scanner(_file);
                while (_search.hasNextLine()) {

                    _nextLine = _search.nextLine();
                    if (_nextLine != null) {
                        _fileLine.add(_nextLine);
                    }

                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    void _testFileIn(String filepath, HandleAClient temp) {
        _FileI(filepath);
        for (int i = 0; i < _fileLine.size(); i++) { //Currently prints out the lines being edited real time
            if (_fileLine.get(i) != null) { //Checks for null before printing
                temp.SendString(_fileLine.get(i)); //Send to server
            }
        }
        temp.SendString("DONE");
        _fileLine.clear();
    }


    public static void main(String[] args) {
        launch(args);
    }
}