package websocket.messages;

public class ErrorMessage extends ServerMessage {

    String errorMessage;

    public ErrorMessage(String error){
        super(ServerMessageType.ERROR);
        this.errorMessage = error;
    }

    public String getErrorMessage(){
        return errorMessage;
    }

}
