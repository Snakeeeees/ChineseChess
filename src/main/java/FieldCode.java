/**
 * Class that imitate pair. It's used in field numbering
 */

public class FieldCode {
    private char key;
    private int value;

    public FieldCode(char key, int  value){
        this.key =  key;
        this.value = value;
    }

    public char getKey() {
        return key;
    }

    public int getValue(){
        return value;
    }
}
