public class ClientBackend extends OnlineDocument {
private String[] _currentLine;
ClientBackend(){ }

void sendToServer(String _currentWords){
    _currentLine = _currentWords.split("\\n"); //Gets the document and splits it into lines based on listener
    for(int i = 0; i< _currentLine.length; i++) { //Currently prints out the lines being edited real time
        if (_currentLine[i] != null) { //Checks for null before printing
            System.out.println(_currentLine[i]); //Send to server
        }
    }

    }

}


