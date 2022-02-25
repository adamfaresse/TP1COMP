import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class Identificateur {
    private String nom ;
    private String genre;
    private Map<String,String> mapInformationSupp;

    public String getNom() {
        return nom;
    }

    public String getGenre() {
        return genre;
    }

    public Identificateur(String nom, String genre){
        this.nom = nom;
        this.genre = genre;
        this.mapInformationSupp = new HashMap<String,String>();
    }
}
