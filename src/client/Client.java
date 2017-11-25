package client;

import java.net.InetAddress;
import java.util.LinkedList;

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

    private LinkedList<Client> listenets = new LinkedList<>();

    public LinkedList<Client> getListenets() {
        return listenets;
    }
}
