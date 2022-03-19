import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class Identificateur {
    private String nom ;
    private String typ;

    private Map<String,Object> mapInformationSupp;

    public String getNom() {
        return nom;
    }

    public String getTyp() {
        return typ;
    }

    public Identificateur(String nom){
        this.nom = nom;
        this.typ = null;
        this.mapInformationSupp = new HashMap<String,Object>();
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    public Map<String, Object> getMapInformationSupp() {
        return mapInformationSupp;
    }
}
