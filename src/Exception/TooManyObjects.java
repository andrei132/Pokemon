package Exception;

public class TooManyObjects extends Exception{
    public TooManyObjects () {
        super("You can give to your pokemon max 3 item");
    }
}
