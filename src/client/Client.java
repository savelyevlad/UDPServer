package client;

import java.net.InetAddress;

public class Client implements Runnable {

    private Id id;

    private Integer number;

    public Integer getPORT() {
        return PORT;
    }

    private Integer PORT;

    public InetAddress getInetAddress() {
        return inetAddress;
    }

    private InetAddress inetAddress;

    public Client(Id id, Integer number) {
        this.id = id;
        this.number = number;
    }

    @Override
    public void run() {
    }
}
