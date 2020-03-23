import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClientBackend extends OnlineDocument {
    private String[] _currentLine;
    private List<String> _fileLine = new ArrayList<String>();

    ClientBackend() {
    }

    void sendToServer(String _currentWords) throws IOException {
        _currentLine = _currentWords.split("\\n"); //Gets the document and splits it into lines based on listener
        for (int i = 0; i < _currentLine.length; i++) { //Currently prints out the lines being edited real time
            if (_currentLine[i] != null) { //Checks for null before printing
                //System.out.println(_currentLine[i]); //Send to server
                _FileO("src/Test.txt", _currentWords);
            }
        }
        _testFileIn();
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

    void _FileO(String _filePath, String _line) throws IOException {
        BufferedWriter _out = new BufferedWriter(new FileWriter(_filePath));
        _out.write(_line);
        _out.flush();
        _out.close();
    }

    void _testFileIn() {
        _FileI("src/Test.txt");
        for (int i = 0; i < _fileLine.size(); i++) { //Currently prints out the lines being edited real time
            if (_fileLine.get(i) != null) { //Checks for null before printing
                System.out.println(_fileLine.get(i)); //Send to server
            }
        }
    }
}


