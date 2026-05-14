package com.bibliotheque.vue.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.bibliotheque.vue.Catalog;

public class PageController implements ActionListener{

    Catalog handler;
    int     page;

    public PageController(Catalog handler, int page) {
        this.handler = handler;
        this.page = page;
    }

    public void actionPerformed(ActionEvent e) {
        handler.changePage(this.page);
    }
}
