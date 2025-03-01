package exception;

public class StatusException extends Exception{

    private final int status;

    public StatusException(String message, int status){
        super(message);
        this.status = status;
    }

    public int getStatus(){
        return status;
    }

}
