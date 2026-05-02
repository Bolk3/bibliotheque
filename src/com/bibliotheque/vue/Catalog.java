package com.bibliotheque.vue;

import java.util.Set;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.bibliotheque.model.Work;

public class Catalog extends JPanel{
    Set<Work>       handle;
    Vector<JPanel>  display = new Vector<>();
    JLabel          test = new JLabel("catalog");

    public Catalog() {
        this.add(test);
    }
}
