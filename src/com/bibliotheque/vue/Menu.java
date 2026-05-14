package com.bibliotheque.vue;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.bibliotheque.vue.components.AccessButton;

public class Menu extends JPanel{
    JPanel          control = new JPanel();
    AccessButton    back;
    AccessButton    next;
    AccessButton    catalog;
    AccessButton    Member;

    public Menu(MainFrame handler) {
        this.setBackground(new Color(211, 211, 211));
        setLayout(new BorderLayout()); // ← BorderLayout sur le Menu

        back = new AccessButton(
                loadIcon("/assets/arrow_back_ios_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24.png"),
                () -> handler.goBack()
                );
        next = new AccessButton(
                loadIcon("/assets/arrow_forward_ios_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24.png"),
                () -> handler.goNext()
                );
        catalog = new AccessButton("Catalog", () -> handler.goForward(new Catalog()));
        Member  = new AccessButton("Member",  () -> handler.goForward(new Member()));

        control.setLayout(new GridLayout(1, 2));
        control.setBackground(new Color(74, 85, 104));
        noLayout(back);
        noLayout(next);
        control.add(back);
        control.add(next);

        JPanel nav = new JPanel();
        nav.setLayout(new GridLayout(2, 1));
        nav.setBackground(new Color(74, 85, 104));
        nav.add(catalog);
        nav.add(Member);

        add(control, BorderLayout.NORTH);
        add(nav, BorderLayout.SOUTH);
    }

    private ImageIcon loadIcon(String path) {
        URL url = getClass().getResource(path);
        if (url == null) {
            System.err.println("No image found" + path);
            return null;
        }
        return new ImageIcon(url);
    }

    private void    noLayout(JButton button) {
        button.setBorder(null);
        button.setBackground(null);
    }
}
