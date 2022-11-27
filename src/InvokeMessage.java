import java.io.Serializable;

public class InvokeMessage implements Serializable {
    private int museum;
    private int method;
    private String name;
    private String artist;
    private int value;
    private String msg;






    public InvokeMessage() {

    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getMuseum() {
        return museum;
    }

    public void setMuseum(int museum) {
        this.museum = museum;
    }

    public int getMethod() {
        return method;
    }

    public void setMethod(int method) {
        this.method = method;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
