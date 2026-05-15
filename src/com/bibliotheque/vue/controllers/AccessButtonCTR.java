package com.bibliotheque.vue.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Functional adapter controller linking UI actions to execution tasks.
 * 
 * <p>Implements {@link ActionListener} to receive visual component click events 
 * and immediately delegates processing logic to a decoupling functional command callback.</p>
 * 
 * @see ActionListener
 * @see Runnable
 * 
 * @version 1.1
 */
public class AccessButtonCTR implements ActionListener {

    private final Runnable _commandDelegate;

    /**
     * Constructs a decoupled event forwarding controller.
     * 
     * @param action the executable task sequence to execute upon interaction events
     */
    public AccessButtonCTR(Runnable action) {
        this._commandDelegate = action;
    }

    /**
     * Intercepts UI dispatch updates and forwards execution flows to the wrapped command thread.
     * 
     * @param event the structural semantic contextual execution payload parameter
     */
    @Override
    public void actionPerformed(ActionEvent event) { 
        if (this._commandDelegate != null) {
            this._commandDelegate.run();
        }
    }
}