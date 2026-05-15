package com.bibliotheque.vue;

import com.bibliotheque.model.Bibliotheque;
import com.bibliotheque.model.Librarian;
import com.bibliotheque.vue.core.Catalog;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * The main application window acting as the primary controller for view navigation.
 * 
 * <p>This frame implements a historical navigation stack (back, forward, next) 
 * reminiscent of a web browser. It manages a persistent sidebar {@link Menu} 
 * on the left and dynamically swaps content views in the center panel while tracking 
 * the application's conversational history.</p>
 * 
 * @see Menu
 * @see Catalog
 * @see Bibliotheque
 * 
 * @version 1.1
 */
public class MainFrame extends JFrame {

    /** The backend business layer facade instance. */
    public final Bibliotheque  bibliotheque;
    
    /** The active logged-in application operator. */
    public Librarian           currentUser;

    private JPanel             _menuPanel;
    private JPanel             _currentCenterPanel;
    
    private final Vector<JPanel> _historyStack = new Vector<>();
    private Integer            _historyPointer = -1;

    /**
     * Constructs and initializes the primary window frame.
     * 
     * @param bibliotheque the core management instance driving business logic
     */
    public MainFrame(Bibliotheque bibliotheque) {
        this.bibliotheque = bibliotheque;
        
        this.setLayout(new BorderLayout());
        this.getContentPane().setPreferredSize(new Dimension(720, 480));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();

        this._menuPanel = new Menu(this);
        
        // Push the Catalog view as the default startup screen
        this.goForward(new Catalog(this.bibliotheque, this));
    }

    // -------------------------------------------------------------------------
    // Navigation Operations
    // -------------------------------------------------------------------------

    /**
     * Appends a new view panel to the navigation sequence, pruning any alternative
     * forward histories if the pointer was currently sitting back in time.
     * 
     * @param panel the next {@link JPanel} view screen to load
     */
    public void goForward(JPanel panel) {
        if (!isLastInHistory()) {
            pruneHistoryFrom(this._historyPointer + 1);
        }
        this._historyStack.add(panel);
        this._historyPointer++;
        this.updateFrameHierarchy();
    }

    /**
     * Rewinds the frame's historical state pointer by one view step if applicable.
     */
    public void goBack() {
        if (!canGoBack()) return;
        this._historyPointer--;
        this.updateFrameHierarchy();
    }

    /**
     * Advances the frame's historical state pointer forward by one view step if applicable.
     */
    public void goNext() {
        if (!canGoNext()) return;
        this._historyPointer++;
        this.updateFrameHierarchy();
    }

    private void pruneHistoryFrom(int index) {
        while (this._historyStack.size() > index) {
            this._historyStack.removeLast();
        }
    }

    // -------------------------------------------------------------------------
    // View Lifecycle Synchronization
    // -------------------------------------------------------------------------

    /**
     * Clears the current frame layout structure and re-binds UI segments to mirror 
     * the state tracked by the internal pointer.
     */
    private void updateFrameHierarchy() {
        if (this._historyPointer < 0 || this._historyPointer >= this._historyStack.size()) return;

        this._currentCenterPanel = this._historyStack.get(this._historyPointer);

        this.getContentPane().removeAll();

        if (this._menuPanel != null) {
            this.getContentPane().add(this._menuPanel, BorderLayout.WEST);
        }
        if (this._currentCenterPanel != null) {
            this.getContentPane().add(this._currentCenterPanel, BorderLayout.CENTER);
        }

        this.revalidate();
        this.repaint();
    }

    // -------------------------------------------------------------------------
    // State Queries
    // -------------------------------------------------------------------------

    /**
     * Checks if it is safe to traverse backwards into view execution history.
     * 
     * @return {@code true} if an evaluation index resides prior to the current pointer
     */
    public boolean canGoBack() {
        return this._historyPointer > 0;
    }

    /**
     * Checks if a forward history vector path safely exists ahead of the current view pointer.
     * 
     * @return {@code true} if forward elements remain cached in the timeline
     */
    public boolean canGoNext() {
        return this._historyPointer >= 0 && !isLastInHistory();
    }

    private boolean isLastInHistory() {
        return this._historyStack.isEmpty() || (this._historyPointer + 1) == this._historyStack.size();
    }

    // -------------------------------------------------------------------------
    // Accessors and Mutators
    // -------------------------------------------------------------------------

    /**
     * Retrives the current main content component visible to the user.
     * 
     * @return the central UI panel layout window segment
     */
    public JPanel getCurrentPanel() {
        return this._currentCenterPanel;
    }

    /**
     * Returns total structural size dimensions tracked by historical page paths.
     * 
     * @return count size of total navigation steps
     */
    public int getHistorySize() {
        return this._historyStack.size();
    }

    /**
     * Returns chronological page stack tracking index pointer context.
     * 
     * @return present tracking stack pointer index integer
     */
    public int getCurrentIndex() {
        return this._historyPointer;
    }

    /**
     * Reassigns or toggles visual operational frame menu control layout.
     * 
     * @param menu the updated navigation component block
     */
    public void setMenu(JPanel menu) {
        this._menuPanel = menu;
        this.updateFrameHierarchy();
    }
}