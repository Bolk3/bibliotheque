package com.bibliotheque.vue.librarian;

import com.bibliotheque.model.Bibliotheque;
import com.bibliotheque.model.Borrow;
import com.bibliotheque.model.ExtensionStamp;
import com.bibliotheque.model.Librarian;
import com.bibliotheque.model.ReturnStamp;
import com.bibliotheque.model.Stamp;
import com.bibliotheque.vue.MainFrame;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

/**
 * Visual display monitoring log detailing all operations authorized by an explicit {@link Librarian}.
 * 
 * <p>Splits analytical overview tracks into two layout segments: historical check-out loan authorizations 
 * and secondary transactional life-cycle overrides (returns, extension approvals) managed via data {@link Stamp} records.</p>
 * 
 * @see Librarian
 * @see Stamp
 * @see Borrow
 * @see MainFrame
 * 
 * @version 1.1
 */
public class LibrarianActionHistory extends JPanel {

    private final DefaultTableModel _borrowTableModel;
    private final DefaultTableModel _stampTableModel;
    private final JTable            _borrowTable;
    private final JTable            _stampTable;
    private final SimpleDateFormat  _dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Constructs the operational audit trail log screen interface.
     * 
     * @param librarian the specific administrative operator profile context to audit
     * @param logic     the core library management backend subsystem facade
     * @param handler   the primary frame viewport manager context routing layout updates
     */
    public LibrarianActionHistory(Librarian librarian, Bibliotheque logic, MainFrame handler) {
        this.setLayout(new BorderLayout(10, 10));
        this.setBackground(Color.WHITE);

        // --- SECTION HAUT : INITIALIZE ADMINISTRATIVE HEADLINE OVERVIEW ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("Historique de l'agent : " + librarian.getFirstName() + " " + librarian.getLastName(), SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        headerPanel.add(titleLabel, BorderLayout.NORTH);
        
        JLabel subtitleLabel = new JLabel("Poste : " + librarian.getPosition() + " | Accès : Niveau " + librarian.getPermission(), SwingConstants.CENTER);
        subtitleLabel.setForeground(Color.GRAY);
        headerPanel.add(subtitleLabel, BorderLayout.SOUTH);
        
        this.add(headerPanel, BorderLayout.NORTH);

        // --- SECTION CENTRALE : BI-GRID MANAGEMENT ARRAYS ---
        JPanel splitTablesContainer = new JPanel(new GridLayout(2, 1, 10, 10));
        splitTablesContainer.setBackground(Color.WHITE);

        // =====================================================================
        // 1. DATA VECTOR MAPPING: VALIDATED OUTBOUND MEDIA BORROWS
        // =====================================================================
        String[] borrowColumns = {"Œuvre", "Emprunteur", "Date Emprunt", "Échéance Prévue"};
        _borrowTableModel = new DefaultTableModel(borrowColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { 
                return false; 
            }
        };
        
        this.populateBorrowLogs(librarian);
        
        JPanel borrowPanelWrapper = new JPanel(new BorderLayout());
        borrowPanelWrapper.setBorder(BorderFactory.createTitledBorder("Emprunts Validés"));
        _borrowTable = new JTable(_borrowTableModel);
        borrowPanelWrapper.add(new JScrollPane(_borrowTable), BorderLayout.CENTER);
        splitTablesContainer.add(borrowPanelWrapper);

        // =====================================================================
        // 2. DATA VECTOR MAPPING: LIFECYCLE TRACKING OVERRIDES (STAMPS)
        // =====================================================================
        String[] stampColumns = {"Type d'Action", "Œuvre concernée", "Date de validation"};
        _stampTableModel = new DefaultTableModel(stampColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { 
                return false; 
            }
        };
        
        this.populateStampLogs(librarian);
        
        JPanel stampPanelWrapper = new JPanel(new BorderLayout());
        stampPanelWrapper.setBorder(BorderFactory.createTitledBorder("Retours & Prolongations Enregistrés"));
        _stampTable = new JTable(_stampTableModel);
        stampPanelWrapper.add(new JScrollPane(_stampTable), BorderLayout.CENTER);
        splitTablesContainer.add(stampPanelWrapper);

        this.add(splitTablesContainer, BorderLayout.CENTER);

        // --- SECTION BAS : NAVIGATION ACTIONS CONTROL BAR ---
        JButton btnBack = new JButton("Retour à la liste");
        btnBack.addActionListener(e -> handler.goBack());
        
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.add(btnBack);
        this.add(footerPanel, BorderLayout.SOUTH);
        
        this.revalidate();
        this.repaint();
    }

    // -------------------------------------------------------------------------
    // Synchronous Matrix Adapters
    // -------------------------------------------------------------------------

    private void populateBorrowLogs(Librarian librarian) {
        if (librarian.getValidatedBorrows() == null) return;

        for (Borrow borrowRecord : librarian.getValidatedBorrows()) {
            if (borrowRecord == null) continue;

            String workTitle = (borrowRecord.getCopy() != null && borrowRecord.getCopy().getReference() != null) 
                    ? borrowRecord.getCopy().getReference().getTitle() 
                    : "Inconnu";
            
            String borrowerIdentity = (borrowRecord.getBorrower() != null) 
                    ? borrowRecord.getBorrower().toString() 
                    : "Inconnu";
            
            String startFormatted = (borrowRecord.getStartDate() != null) ? _dateFormat.format(borrowRecord.getStartDate()) : "-";
            String expectedFormatted = (borrowRecord.getExpectedDate() != null) ? _dateFormat.format(borrowRecord.getExpectedDate()) : "-";

            _borrowTableModel.addRow(new Object[]{
                workTitle,
                borrowerIdentity,
                startFormatted,
                expectedFormatted
            });
        }
    }

    private void populateStampLogs(Librarian librarian) {
        if (librarian.getValidatedStamps() == null) return;

        for (Stamp actionStamp : librarian.getValidatedStamps()) {
            if (actionStamp == null) continue;

            String actionTypeDescription;
            if (actionStamp instanceof ReturnStamp) {
                actionTypeDescription = "🔄 Retour Œuvre";
            } else if (actionStamp instanceof ExtensionStamp) {
                actionTypeDescription = "⏳ Prolongation";
            } else {
                actionTypeDescription = "📝 " + actionStamp.getClass().getSimpleName().replace("Stamp", "");
            }

            String targetedMediaTitle = "Aucune référence";
            if (actionStamp.getReference() != null 
                    && actionStamp.getReference().getCopy() != null 
                    && actionStamp.getReference().getCopy().getReference() != null) {
                targetedMediaTitle = actionStamp.getReference().getCopy().getReference().getTitle();
            }
            
            String logTimestampFormatted = (actionStamp.getTimestamp() != null) ? _dateFormat.format(actionStamp.getTimestamp()) : "-";

            _stampTableModel.addRow(new Object[]{
                actionTypeDescription,
                targetedMediaTitle,
                logTimestampFormatted
            });
        }
    }
}