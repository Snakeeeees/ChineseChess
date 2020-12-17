import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Player implements Runnable{
    private Socket socket;
    private Scanner input;
    private PrintWriter output;
    private Game game;
    private boolean ready = false;

    private int number = -1;

    public Player(Socket socket, Game game){
        this.socket = socket;
        this.game = game;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getNumber(){
        return number;
    }

    public void write(String message){
        if(ready) {
            output.println("MESSAGE" + message);
        }
    }

    @Override
    public void run() {
        while(number == -1){
            try {
                wait(100);
            } catch (InterruptedException e) {}
        }

        try {
            setup();
            ready = true;
            processCommands();
        }
        catch (IOException e) {}
        finally {
                game.stopGame();
            try {
                game.playerLeft(number);
                socket.close();
            } catch (IOException e) {}
        }

    }

    private void setup() throws IOException {
        input = new Scanner(socket.getInputStream());
        output = new PrintWriter(socket.getOutputStream());
        output.println("MESSAGE Welcome Player " + number);
    }

    private void processCommands(){
        while(input.hasNextLine()){
            String command = input.nextLine();

            if(command.startsWith("QUIT")){
                return;
            }
            else if(command.startsWith("MOVE")){
                int temp1 = command.indexOf(" ", 1) + 1;
                int temp2 = command.indexOf(" ", temp1);

                String fieldFromS = command.substring(temp1, temp2);      // second word
                String fieldToS = command.substring(temp2 + 1);       // third word

                try {
                    FieldCode fieldFrom = new FieldCode(fieldFromS.charAt(0), Integer.parseInt(fieldFromS.substring(1)));
                    FieldCode fieldTo = new FieldCode(fieldToS.charAt(0), Integer.parseInt(fieldToS.substring(1)));
                    game.move(fieldFrom, fieldTo, number);
                }
                catch (NumberFormatException e){
                    output.println("MESSAGE Wrong field codes: " + fieldFromS + " " + fieldToS);
                }
                catch (MoveException e){
                    output.println("MESSAGE" + e.getMessage());
                }
            }

        }
    }

    public void endGame(){
            output.println("GAME_OVER");
    }
}
