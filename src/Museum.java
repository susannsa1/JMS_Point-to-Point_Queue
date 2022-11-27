import java.io.Serializable;

public class Museum implements Serializable {
    private final String name;
    private Art[] artObject;

    //Contructor of name and artObject
    public Museum(String name, Art[] artObject) {
        this.name = name;
        this.artObject = artObject;
    }

    // Getter Name
    public String getName() {
        return name;
    }

    // Getter Artobject
    public Art[] getArtObject() {
        return artObject;
    }

    //Created SearchByName
    public Art SearchByName(String name) {
        for (int i = 0; i < artObject.length; i++) {
            if (name.equals(getArtObject()[i].getName())) {
                return getArtObject()[i];
            }
        }
        return null;
    }

    //Created AddArt
    public void addArt(String name, String artist, int value) {
        Art[] array = new Art[getArtObject().length + 1];

        int length = getArtObject().length;
        for (int i = 0; i < length; i++) {
            array[i] = getArtObject()[i];
        }

        Art art = new Art(name, artist, value);

        array[getArtObject().length] = art;
        artObject = array;
    }
}
