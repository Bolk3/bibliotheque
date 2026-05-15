package com.bibliotheque.vue;

import com.bibliotheque.vue.components.AccessButton;
import com.bibliotheque.vue.core.Authors;
import com.bibliotheque.vue.core.Catalog;
import com.bibliotheque.vue.core.LibrarianView;
import com.bibliotheque.vue.core.SettingsView;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * Sidebar or main navigation menu panel for the application interface.
 * 
 * <p>Provides back/forward history navigation and entry points to core views
 * such as the catalog, members management, author indices, system settings, 
 * and staff directories.</p>
 * 
 * @see MainFrame
 * @see AccessButton
 * 
 * @version 1.0
 */
public class Menu extends JPanel {

    private final JPanel _controlPanel = new JPanel();
    private final JPanel _navigationPanel = new JPanel();
    
    private AccessButton _backButton;
    private AccessButton _nextButton;
    private AccessButton _catalogButton;
    private AccessButton _memberButton; 
    private AccessButton _authorsButton;
    private AccessButton _settingsButton;
    private AccessButton _librariansButton;

    /**
     * Constructs the main navigation menu panel.
     * 
     * @param handler the application {@link MainFrame} driving layout changes
     */
    public Menu(MainFrame handler) {
        this.setBackground(new Color(211, 211, 211));
        this.setLayout(new BorderLayout());

        initializeNavigationButtons(handler);
        initializeMenuButtons(handler);
        buildLayouts();
    }

    private void initializeNavigationButtons(MainFrame handler) {
        _backButton = new AccessButton(
                loadIcon("/assets/arrow_back_ios_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24.png"),
                handler::goBack
        );
        _nextButton = new AccessButton(
                loadIcon("/assets/arrow_forward_ios_24dp_E3E3E3_FILL0_wght400_GRAD0_opsz24.png"),
                handler::goNext
        );

        stripButtonStyle(_backButton);
        stripButtonStyle(_nextButton);
    }

    private void initializeMenuButtons(MainFrame handler) {
        _catalogButton = new AccessButton("Catalogue", () -> 
                handler.goForward(new Catalog(handler.bibliotheque, handler)));
        
        _memberButton = new AccessButton("Membres", () -> 
                handler.goForward(new com.bibliotheque.vue.core.Member(handler.bibliotheque, handler)));
        
        _authorsButton = new AccessButton("Auteurs", () -> 
                handler.goForward(new Authors(handler.bibliotheque, handler)));
        
        _settingsButton = new AccessButton("Paramètres", () -> 
                handler.goForward(new SettingsView(handler, handler.currentUser)));
        
        _librariansButton = new AccessButton("Équipe", () -> 
                handler.goForward(new LibrarianView(handler.bibliotheque, handler)));
    }

    private void buildLayouts() {
        // Layout controls (Top)
        _controlPanel.setLayout(new GridLayout(1, 2));
        _controlPanel.setBackground(new Color(74, 85, 104));
        _controlPanel.add(_backButton);
        _controlPanel.add(_nextButton);

        // Layout navigation (Bottom)
        _navigationPanel.setLayout(new GridLayout(5, 1));
        _navigationPanel.setBackground(new Color(74, 85, 104));
        _navigationPanel.add(_catalogButton);
        _navigationPanel.add(_memberButton);
        _navigationPanel.add(_authorsButton);
        _navigationPanel.add(_librariansButton);
        _navigationPanel.add(_settingsButton);

        this.add(_controlPanel, BorderLayout.NORTH);
        this.add(_navigationPanel, BorderLayout.SOUTH);
    }

    private ImageIcon loadIcon(String path) {
        URL url = getClass().getResource(path);
        if (url == null) {
            System.err.println("Resource asset image not found: " + path);
            return null;
        }
        return new ImageIcon(url);
    }

    private void stripButtonStyle(JButton button) {
        button.setBorder(null);
        button.setBackground(null);
    }
}