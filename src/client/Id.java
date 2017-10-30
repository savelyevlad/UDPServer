package client;

import java.net.InetAddress;

public class Id {

    public InetAddress getInetAddress() {
        return inetAddress;
    }

    public void setInetAddress(InetAddress inetAddress) {
        this.inetAddress = inetAddress;
    }

    private InetAddress inetAddress;

    public Integer getPORT() {
        return PORT;
    }

    public void setPORT(Integer PORT) {
        this.PORT = PORT;
    }

    private Integer PORT;

    public Id(InetAddress inetAddress, Integer PORT) {
        this.inetAddress = inetAddress;
        this.PORT = PORT;
    }

    @Override
    public String toString() {
        return inetAddress.toString() + " " + PORT;
    }

    @Override
    public boolean equals(Object obj) {
        try {
            Id id = (Id) obj;
            return inetAddress.equals(id.getInetAddress()) && PORT.equals(id.getPORT());
        }
        catch (Exception e) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return inetAddress.toString().hashCode() ^ PORT;
    }
}
