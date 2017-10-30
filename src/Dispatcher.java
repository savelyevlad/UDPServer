import sandbox.Test;

public class Dispatcher {

    public static void main(String[] args) {

//        new Thread(new Test()).start();
        new Thread(new Server()).start();
    }
}
