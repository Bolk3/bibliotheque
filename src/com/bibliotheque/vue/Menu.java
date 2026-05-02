package com.bibliotheque.vue;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import com.bibliotheque.vue.components.AccessButton;

public class Menu extends JPanel{
    JPanel          control = new JPanel();
    AccessButton    back;
    AccessButton    next;
    AccessButton    catalog;
    AccessButton    Member;
    
    public Menu(MainFrame handler) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        back = new AccessButton("back", () -> handler.goBack());
        next = new AccessButton("next", () -> handler.goNext());
        catalog = new AccessButton("Catalog", () -> handler.goForward(new Catalog()));
        Member = new AccessButton("Member", () -> handler.goForward(new Member()));
        control.add(back);
        control.add(next);

        add(control);
        add(catalog);
        add(Member);
    }
}
