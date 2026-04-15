package Vue.elements;

import Model.Oeuvre;
import java.awt.image.BufferedImage;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class WorkDisplayCatalogue extends JPanel{
    Oeuvre          displayed;
    BufferedImage   image;
    JLabel          title;
    JLabel          pubDate;
    JButton         getDisplay;
    Vector<JLabel>  authors;

    public  WorkDisplayCatalogue(Oeuvre toDisplay) {
        displayed = toDisplay;
    }
}
