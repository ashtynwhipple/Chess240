package websocket.messages;

public class Error extends ServerMessage {

    String error;

    public Error(String error){
        super(ServerMessageType.ERROR);
        this.error = error;
    }

    public String getErrorMessage(){
        return error;
    }

}
