package com.bibliotheque.vue.components;

import com.bibliotheque.model.Work;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Workshow extends JPanel{
    Work    handle;
    JPanel  heroConatiner = new JPanel();
    JPanel  imageContainer = new JPanel();
    JPanel  buttonContainer = new JPanel();
    JLabel  title;
    JLabel  author;
    JLabel  pubDate;

    public Workshow (Work handler) {
        this.handle = handler;
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createLineBorder(Color.black));

        imageContainer.add(new JLabel("image"));

        title = new JLabel(this.handle.getTitle());
        pubDate = new JLabel(this.handle.getPublicationDate().toString());
        author = new JLabel("test");

        heroConatiner.add(title);
        heroConatiner.add(author);
        heroConatiner.add(pubDate);
        heroConatiner.setLayout(new GridLayout(0, 1));

        buttonContainer.add(new JLabel("button"));

        this.add(heroConatiner, BorderLayout.CENTER);
        this.add(imageContainer, BorderLayout.WEST);
        this.add(buttonContainer, BorderLayout.EAST);
    }
}
