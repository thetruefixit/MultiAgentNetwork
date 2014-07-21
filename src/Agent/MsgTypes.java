package Agent;

/**
 * Created by Fixit on 30.05.2014.
 */
public class MsgTypes {
    public static class Message<T> {
        public T data;
        public String operation = new String();
        public Message(T data, String operation) {
            this.data = data;
            this.operation = operation;
        }
    }
    public static class Data<T> {
        public T data;
        public String operation = "";
    }
}
