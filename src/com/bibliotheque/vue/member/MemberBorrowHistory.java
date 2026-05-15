package com.bibliotheque.vue.member;

import com.bibliotheque.model.Bibliotheque;
import com.bibliotheque.model.Borrow;
import com.bibliotheque.model.Member;
import com.bibliotheque.vue.MainFrame;
import com.bibliotheque.vue.components.AccessButton;
import com.bibliotheque.vue.components.forms.ExtendBorrowForm;
import com.bibliotheque.vue.components.forms.ReturnBookForm;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Window;
import java.text.SimpleDateFormat;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

/**
 * Visual panel container listing a historical log of media loans assigned to a single library user.
 * 
 * <p>Implements functional interactive action grids enabling explicit real-time loan item returns
 * or expected due date renewal adjustments directly from the table structure layout.</p>
 * 
 * @see Member
 * @see Borrow
 * @see MainFrame
 * 
 * @version 1.1
 */
public class MemberBorrowHistory extends JPanel {

    private final DefaultTableModel _tableModel;
    private final JTable            _historyTable;
    private final Member            _currentMember;
    private final Bibliotheque      _businessLogic;

    /**
     * Constructs the historical loan logging interaction layout view.
     * 
     * @param member  the targeted user profile whose borrow logs are pulled
     * @param logic   the system business operational layer controller
     * @param handler the overarching visual frame navigation router context
     */
    public MemberBorrowHistory(Member member, Bibliotheque logic, MainFrame handler) {
        this._currentMember = member;
        this._businessLogic = logic;
        
        this.setLayout(new BorderLayout(15, 15));
        this.setBackground(Color.WHITE);
        this.setBorder(new EmptyBorder(20, 20, 20, 20));

        // --- SECTION HAUT : IDENTITY EN-TÊTE ---
        JPanel headerPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        headerPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Historique des emprunts");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        
        JLabel userLabel = new JLabel("Membre : " + member.getFirstName() + " " + member.getLastName());
        userLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        userLabel.setForeground(Color.GRAY);

        headerPanel.add(titleLabel);
        headerPanel.add(userLabel);
        this.add(headerPanel, BorderLayout.NORTH);

        // --- SECTION CENTRALE : HISTORY INTERACTIVE TABLE GRID ---
        String[] columns = {"Ouvrage", "Date d'échéance", "Statut", "Action Retour", "Action Prolongation"};
        _tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3 || column == 4; 
            }
        };
        
        _historyTable = new JTable(_tableModel);
        _historyTable.setRowHeight(32);
        _historyTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        
        this.updateTableData();

        // Bind Custom Buttons Renderers and Cell Editors across the target action tracks
        _historyTable.getColumnModel().getColumn(3).setCellRenderer(new ReturnButtonRendererEditor.Renderer());
        _historyTable.getColumnModel().getColumn(3).setCellEditor(new ReturnButtonRendererEditor.Editor(_historyTable, logic));

        _historyTable.getColumnModel().getColumn(4).setCellRenderer(new ExtendButtonRendererEditor.Renderer());
        _historyTable.getColumnModel().getColumn(4).setCellEditor(new ExtendButtonRendererEditor.Editor(_historyTable, logic));

        JScrollPane scrollPane = new JScrollPane(_historyTable);
        scrollPane.getViewport().setBackground(Color.WHITE);
        this.add(scrollPane, BorderLayout.CENTER);

        // --- SECTION BAS : NAVIGATION ACTIONS ---
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        southPanel.setBackground(Color.WHITE);

        AccessButton btnBack = new AccessButton("Retour à la liste", handler::goBack);

        southPanel.add(btnBack);
        this.add(southPanel, BorderLayout.SOUTH);
    }

    /**
     * Re-scans active data collections assigned to the member instance profile to sync the layout data structure.
     */
    public void updateTableData() {
        _tableModel.setRowCount(0);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        for (Borrow borrowRecord : _currentMember.getBorrows()) {
            String operationalStatus = borrowRecord.isReturned() ? "✅ Rendu" : (borrowRecord.isLate() ? "🛑 EN RETARD" : "📖 En cours");
            _tableModel.addRow(new Object[]{
                borrowRecord.getCopy().getReference().getTitle(),
                borrowRecord.getExpectedDate() != null ? dateFormat.format(borrowRecord.getExpectedDate()) : "N/A",
                operationalStatus,
                borrowRecord, 
                borrowRecord  
            });
        }
    }

    // =========================================================================
    // LOAN CLOSURE INTERACTION GRAPHICS (COLUMN 3)
    // =========================================================================
    private static class ReturnButtonRendererEditor {
        
        public static class Renderer extends JButton implements TableCellRenderer {
            public Renderer() { 
                this.setOpaque(true); 
            }
            
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                if (value instanceof Borrow) {
                    Borrow borrowRecord = (Borrow) value;
                    if (borrowRecord.isReturned()) {
                        return new JLabel("");
                    }
                    this.setText("Retourner");
                    this.setBackground(new Color(40, 167, 69));
                    this.setForeground(Color.WHITE);
                    return this;
                }
                return new JLabel("");
            }
        }

        public static class Editor extends DefaultCellEditor {
            private final JButton _actionButton;
            private Borrow        _borrowReference;
            
            public Editor(JTable targetTable, Bibliotheque logic) {
                super(new JCheckBox());
                _actionButton = new JButton("Retourner");
                _actionButton.addActionListener(e -> {
                    if (_borrowReference != null && !_borrowReference.isReturned()) {
                        Window parentWindow = SwingUtilities.getWindowAncestor(_actionButton);
                        ReturnBookForm returnForm = new ReturnBookForm(parentWindow, _borrowReference, logic);
                        returnForm.setVisible(true);
                        
                        if (returnForm.isConfirmed()) {
                            MemberBorrowHistory historyPanel = (MemberBorrowHistory) SwingUtilities.getAncestorOfClass(MemberBorrowHistory.class, targetTable);
                            if (historyPanel != null) {
                                historyPanel.updateTableData();
                            }
                        }
                    }
                    this.fireEditingStopped();
                });
            }
            
            @Override
            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                _borrowReference = (value instanceof Borrow) ? (Borrow) value : null;
                return _actionButton;
            }
            
            @Override
            public Object getCellEditorValue() { 
                return _borrowReference; 
            }
        }
    }

    // =========================================================================
    // DUE DATE RENEWAL INTERACTION GRAPHICS (COLUMN 4)
    // =========================================================================
    private static class ExtendButtonRendererEditor {
        
        public static class Renderer extends JButton implements TableCellRenderer {
            public Renderer() { 
                this.setOpaque(true); 
            }
            
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                if (value instanceof Borrow) {
                    Borrow borrowRecord = (Borrow) value;
                    if (borrowRecord.isReturned()) {
                        return new JLabel(""); 
                    }
                    this.setText("Prolonger...");
                    this.setBackground(new Color(0, 123, 255));
                    this.setForeground(Color.WHITE);
                    return this;
                }
                return new JLabel("");
            }
        }

        public static class Editor extends DefaultCellEditor {
            private final JButton _actionButton;
            private Borrow        _borrowReference;
            
            public Editor(JTable targetTable, Bibliotheque logic) {
                super(new JCheckBox());
                _actionButton = new JButton("Prolonger...");
                _actionButton.addActionListener(e -> {
                    if (_borrowReference != null && !_borrowReference.isReturned()) {
                        Window parentWindow = SwingUtilities.getWindowAncestor(_actionButton);
                        
                        ExtendBorrowForm extendForm = new ExtendBorrowForm(parentWindow, _borrowReference, logic);
                        extendForm.setVisible(true);
                        
                        if (extendForm.isConfirmed()) {
                            JOptionPane.showMessageDialog(parentWindow, "L'emprunt a été prolongé avec succès !");
                            MemberBorrowHistory historyPanel = (MemberBorrowHistory) SwingUtilities.getAncestorOfClass(MemberBorrowHistory.class, targetTable);
                            if (historyPanel != null) {
                                historyPanel.updateTableData();
                            }
                        }
                    }
                    this.fireEditingStopped();
                });
            }
            
            @Override
            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                _borrowReference = (value instanceof Borrow) ? (Borrow) value : null;
                return _actionButton;
            }
            
            @Override
            public Object getCellEditorValue() { 
                return _borrowReference; 
            }
        }
    }
}