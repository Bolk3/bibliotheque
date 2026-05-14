package com.bibliotheque.vue;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.bibliotheque.model.Work;
import com.bibliotheque.vue.components.Bookshow;
import com.bibliotheque.vue.controllers.PageController;

public class Catalog extends JPanel{
    Integer         page = 0;
    Vector<Work>    handle;
    Vector<JPanel>  display = new Vector<>();
    Vector<JButton> pageButtons = new Vector<>();
    JPanel          displayHandle = new JPanel();
    JPanel          searchDisplay = new JPanel();
    JPanel          pageDisplay = new JPanel();

    public Catalog() {
        this.setLayout(new BorderLayout());

        for (int i = 0; i < 30; i++) {
            Work current = handle.get(i);
            display.add(new Bookshow(current));
            if (i < 10)
                displayHandle.add(display.get(i));
            if (i % 10 == 0) {
                pageButtons.add(new JButton("" + (i / 10) + 1));
                pageButtons.get(i / 10).addActionListener(new PageController(this, i / 10));
                pageDisplay.add(pageButtons.get(i / 10));
            }
        }
        displayHandle.setLayout(new GridLayout(0, 1));

        searchDisplay.add(new JLabel("search Display"));

        this.add(displayHandle, BorderLayout.CENTER);
        this.add(searchDisplay, BorderLayout.NORTH);
        this.add(pageDisplay, BorderLayout.SOUTH);
    }

    private void    updateDisplay() {
        displayHandle.removeAll();

        for (int i = (page * 10); i < (page + 1) * 10; i++) {
            displayHandle.add(display.get(i));
        }
    }

    public Boolean isCurentPage(int page) {
        return this.page == page;
    }

    public void changePage(int page) {
        this.page = page;

        updateDisplay();
    }
}
