package client;

import java.net.InetAddress;

public class Client {

    private Id id;

    public Integer getNumber() {
        return number;
    }

    private Integer number;

    public Integer getPORT() {
        return PORT;
    }

    private Integer PORT;

    public InetAddress getInetAddress() {
        return inetAddress;
    }

    private InetAddress inetAddress;

    public Client(Id id, Integer number, InetAddress inetAddress, Integer PORT) {
        this.id = id;
        this.number = number;
        this.inetAddress = inetAddress;
        this.PORT = PORT;
    }
}
