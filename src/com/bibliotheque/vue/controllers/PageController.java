package com.bibliotheque.vue.controllers;

import com.bibliotheque.vue.core.Catalog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Event-driven tracking controller managing dynamic catalog page transitions.
 * 
 * <p>Implements {@link ActionListener} to intercept view pagination interaction triggers 
 * and routes index state changes back to the coordinated target {@link Catalog} component view.</p>
 * 
 * @see Catalog
 * @see ActionListener
 * 
 * @version 1.1
 */
public class PageController implements ActionListener {

    private final Catalog _catalogHandler;
    private final int     _targetPageIndex;

    /**
     * Constructs a stateful pagination tracking event listener.
     * 
     * @param handler the parent interactive catalog roster display layout to notify
     * @param page    the explicit destination logical index offset assigned to this controller
     */
    public PageController(Catalog handler, int page) {
        this._catalogHandler = handler;
        this._targetPageIndex = page;
    }

    /**
     * Intercepts navigation interaction events to trigger segment page updates.
     * 
     * @param event the structural structural component layout context event payload
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        if (this._catalogHandler != null) {
            this._catalogHandler.changePage(this._targetPageIndex);
        }
    }
}