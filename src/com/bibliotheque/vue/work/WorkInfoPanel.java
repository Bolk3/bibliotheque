package com.bibliotheque.vue.work;

import com.bibliotheque.model.Bibliotheque;
import com.bibliotheque.model.Book;
import com.bibliotheque.model.Copy;
import com.bibliotheque.model.Dvd;
import com.bibliotheque.model.Work;
import com.bibliotheque.vue.MainFrame;
import com.bibliotheque.vue.components.AccessButton;
import com.bibliotheque.vue.components.forms.AddCopyForm;
import com.bibliotheque.vue.components.forms.BorrowForm;
import com.bibliotheque.vue.components.forms.EditWorkForm;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.util.stream.Collectors;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

/**
 * Visual display container rendering detailed metadata indices and physical item 
 * copy tracks for a specific cataloged media entity.
 * 
 * <p>Exposes critical operation interactions including borrowing transactions, 
 * inventory expansions, and textual record overrides.</p>
 * 
 * @see Work
 * @see MainFrame
 * 
 * @version 1.1
 */
public class WorkInfoPanel extends JPanel {

    private Work                  _workContext;
    private final Bibliotheque    _businessLogic;
    private final MainFrame       _viewController;
    
    private final JTable          _tableCopies;
    private final DefaultTableModel _tableModel;
    private final JLabel          _titleLabel;
    private final JLabel          _authorsLabel;
    private final JLabel          _metadataLabel;
    private final JLabel          _mediaSpecificLabel;

    /**
     * Constructs a presentation detail container view for an explicit work record.
     * 
     * @param work    the targeted intellectual work to reference
     * @param logic   the operational API controller subsystem
     * @param vue     the tracking application frame view coordinator
     */
    public WorkInfoPanel(Work work, Bibliotheque logic, MainFrame vue) {
        this._workContext = work;
        this._businessLogic = logic;
        this._viewController = vue;
        
        this.setLayout(new BorderLayout(15, 15));
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        this.setBackground(Color.WHITE);

        // --- HIERARCHICAL WORK DETAILS PANEL (NORTH) ---
        JPanel headerPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        headerPanel.setBackground(Color.WHITE);

        _titleLabel = new JLabel();
        _titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        headerPanel.add(_titleLabel);

        _authorsLabel = new JLabel();
        _authorsLabel.setFont(new Font("SansSerif", Font.ITALIC, 14));
        _authorsLabel.setForeground(Color.DARK_GRAY);
        headerPanel.add(_authorsLabel);

        _metadataLabel = new JLabel();
        _metadataLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        _metadataLabel.setForeground(Color.GRAY);
        headerPanel.add(_metadataLabel);

        _mediaSpecificLabel = new JLabel();
        _mediaSpecificLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        _mediaSpecificLabel.setForeground(new Color(41, 128, 185));
        headerPanel.add(_mediaSpecificLabel);

        this.updateWorkDetails();
        this.add(headerPanel, BorderLayout.NORTH);

        // --- PHYSICAL INVENTORY GRID INTERFACE (CENTER) ---
        String[] columns = {"N°", "État Physique", "Disponibilité"};
        _tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { 
                return false; 
            }
        };
        
        this.updateCopiesTable();
        _tableCopies = new JTable(_tableModel);
        _tableCopies.setRowHeight(22);
        this.add(new JScrollPane(_tableCopies), BorderLayout.CENTER);

        // --- DISPATCH ACTIONS PANEL (SOUTH) ---
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        actionsPanel.setBackground(Color.WHITE);

        AccessButton borrowButton = new AccessButton("Emprunter", this::handleBorrowAction);
        AccessButton addCopyButton = new AccessButton("Ajouter une copie", () -> 
                new AddCopyForm(this._businessLogic, this._workContext, this));
        AccessButton editWorkButton = new AccessButton("Modifier l'œuvre", () -> 
                this._viewController.goForward(new EditWorkForm(this._businessLogic, this._workContext, this._viewController, this)));

        actionsPanel.add(editWorkButton);
        actionsPanel.add(new JSeparator(SwingConstants.VERTICAL)); 
        actionsPanel.add(addCopyButton);
        actionsPanel.add(borrowButton);

        this.add(actionsPanel, BorderLayout.SOUTH);
    }

    // -------------------------------------------------------------------------
    // Execution Handlers
    // -------------------------------------------------------------------------

    private void handleBorrowAction() {
        int selectedRow = _tableCopies.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez une copie dans le tableau.");
            return;
        }

        Copy chosenCopy = _workContext.getCopies().get(selectedRow);
        if (chosenCopy.isAvailable()) {
            _viewController.goForward(new BorrowForm(_businessLogic, chosenCopy, _viewController, this)); 
        } else {
            JOptionPane.showMessageDialog(this, "Cette copie est déjà empruntée.");
        }
    }

    // -------------------------------------------------------------------------
    // Synchronization Operations
    // -------------------------------------------------------------------------

    /**
     * Swaps out the current internal model reference context.
     * 
     * @param newHandle the replacement structural work data element
     */
    public void setHandle(Work newHandle) {
        this._workContext = newHandle;
    }

    /**
     * Re-reads metadata definitions inside the bounded work record to modify textual visual labels.
     */
    public void updateWorkDetails() {
        String mediaType = (_workContext instanceof Book) ? "Livre" : "DVD";
        _titleLabel.setText(_workContext.getTitle() + " [" + mediaType + "]");

        String authorsJoined = _workContext.getAuthors().stream()
                .map(a -> a.getFirstName() + " " + a.getLastName())
                .collect(Collectors.joining(", "));
        
        if (authorsJoined.isEmpty()) {
            authorsJoined = "Aucun auteur enregistré";
        }
        _authorsLabel.setText("Auteur(s) : " + authorsJoined);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = (_workContext.getPublicationDate() != null) 
                ? dateFormat.format(_workContext.getPublicationDate()) 
                : "Inconnue";

        _metadataLabel.setText(String.format("Éditeur : %s   |   Cote de rangement : %s   |   Date d'édition : %s", 
                _workContext.getEditor(), _workContext.getCategory(), formattedDate));

        if (_workContext instanceof Book) {
            _mediaSpecificLabel.setText("Code ISBN : " + ((Book) _workContext).getIsbn());
        } else if (_workContext instanceof Dvd) {
            _mediaSpecificLabel.setText("Zone Région Vidéo : " + ((Dvd) _workContext).getRegion());
        }
    }

    /**
     * Purges and recalculates data table tracking structures mapping physical media item copies.
     */
    public void updateCopiesTable() {
        _tableModel.setRowCount(0);
        int physicalIndex = 1;
        
        for (Copy copyItem : _workContext.getCopies()) {
            String trackingAvailability;
            if (copyItem.isAvailable()) {
                trackingAvailability = "Libre";
            } else {
                trackingAvailability = copyItem.isLate() ? "Emprunté (EN RETARD)" : "Emprunté";
            }

            _tableModel.addRow(new Object[]{
                "Copie #" + physicalIndex++,
                copyItem.getState(),
                trackingAvailability
            });
        }
    }
}