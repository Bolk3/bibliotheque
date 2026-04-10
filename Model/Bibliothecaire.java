
import java.util.*;

/**
 * 
 */
public class Bibliothecaire extends Utilisateur {

    /**
     * Default constructor
     */
    public Bibliothecaire() {
    }

    private String          poste;
    private int             permision;
    private Set<Borrow>     empruntsValide;
    private Set<Stamp>      stampsValidated;

}