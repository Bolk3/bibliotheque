package com.bibliotheque.vue.components;

import com.bibliotheque.vue.controllers.AccessButtonCTR;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * Custom user interface button component enforcing decoupled functional architecture.
 * 
 * <p>Wraps standard {@link JButton} behavior with standardized styling constraints 
 * and automatically attaches an {@link AccessButtonCTR} event interceptor to redirect 
 * mouse interaction updates directly into decoupled functional {@link Runnable} actions.</p>
 * 
 * @see JButton
 * @see AccessButtonCTR
 * @see Runnable
 * 
 * @version 1.1
 */
public class AccessButton extends JButton {

    /**
     * Constructs a text-based functional execution action button.
     * 
     * @param text   the localized text string displaying across the button face
     * @param action the executable task sequence triggered upon component selection
     */
    public AccessButton(String text, Runnable action) {
        super(text);
        this.initializeComponentDecorations();
        this.addActionListener(new AccessButtonCTR(action));
    }

    /**
     * Constructs an icon-based functional execution action button.
     * 
     * @param image  the icon asset reference displaying inside the button frame
     * @param action the executable task sequence triggered upon component selection
     */
    public AccessButton(ImageIcon image, Runnable action) {
        super();
        this.setIcon(image);
        this.initializeComponentDecorations();
        this.addActionListener(new AccessButtonCTR(action));
    }

    /**
     * Restricts visual properties into standardized aesthetic application design tokens.
     */
    private void initializeComponentDecorations() {
        this.setFont(new Font("SansSerif", Font.BOLD, 12));
        this.setFocusPainted(false);
        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        // Default cohesive clean padding bounds
        this.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                BorderFactory.createEmptyBorder(6, 12, 6, 12)
        ));
    }
}