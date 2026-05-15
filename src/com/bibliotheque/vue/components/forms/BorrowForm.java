package com.bibliotheque.vue.components.forms;

import com.bibliotheque.model.Bibliotheque;
import com.bibliotheque.model.Copy;
import com.bibliotheque.model.Librarian;
import com.bibliotheque.model.Member;
import com.bibliotheque.vue.MainFrame;
import com.bibliotheque.vue.components.AccessButton;
import com.bibliotheque.vue.work.WorkInfoPanel;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.border.EmptyBorder;

/**
 * Editorial transactional form workspace used to initialize a new physical {@link Copy} book allocation.
 * 
 * <p>Binds selected subscriber accounts and supervising library staff instances under custom layout constraints,
 * enforcing dynamic temporal limits through a date spinner component before committing changes.</p>
 * 
 * @see Copy
 * @see Member
 * @see Librarian
 * @see WorkInfoPanel
 * 
 * @version 1.1
 */
public class BorrowForm extends JPanel {

    private final Bibliotheque             _businessLogic;
    private final Copy                     _targetedCopyContext;
    private final MainFrame                 _viewController;
    private final WorkInfoPanel             _parentPresentationPanel;

    private final JComboBox<MemberWrapper>    _memberSelectionCombo;
    private final JComboBox<LibrarianWrapper> _librarianSelectionCombo;
    private final JSpinner                    _expectedReturnDatePicker;

    /**
     * UI presentation wrapper ensuring decoupled, standardized rendering context maps for Member elements.
     */
    private static class MemberWrapper {
        private final Member _memberEntity;

        public MemberWrapper(Member member) {
            this._memberEntity = member;
        }

        public Member getEntity() {
            return _memberEntity;
        }

        @Override
        public String toString() {
            return _memberEntity.getFirstName() + " " + _memberEntity.getLastName() + " (" + _memberEntity.getEmail() + ")";
        }
    }

    /**
     * UI presentation wrapper ensuring decoupled, standardized rendering context maps for Librarian elements.
     */
    private static class LibrarianWrapper {
        private final Librarian _librarianEntity;

        public LibrarianWrapper(Librarian librarian) {
            this._librarianEntity = librarian;
        }

        public Librarian getEntity() {
            return _librarianEntity;
        }

        @Override
        public String toString() {
            return _librarianEntity.getFirstName() + " " + _librarianEntity.getLastName();
        }
    }

    /**
     * Constructs a workflow panel layout targeted at staging physical items for circulation allocation records.
     * 
     * @param logic       the central tracking repository layer facade instance
     * @param copy        the specific inventory resource unit targeted for checkout
     * @param handler     the primary frame structure view manager coordinator
     * @param parentPanel the tracking dashboard panel handling downstream view updates
     */
    public BorrowForm(Bibliotheque logic, Copy copy, MainFrame handler, WorkInfoPanel parentPanel) {
        this._businessLogic = logic;
        this._targetedCopyContext = copy;
        this._viewController = handler;
        this._parentPresentationPanel = parentPanel;

        this.setLayout(new GridBagLayout());
        this.setBackground(Color.WHITE);
        this.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints layoutConstraints = new GridBagConstraints();
        layoutConstraints.insets = new Insets(10, 10, 10, 10);
        layoutConstraints.fill = GridBagConstraints.HORIZONTAL;

        // --- SECTION BANNER HEADER ---
        JLabel formViewTitle = new JLabel("Nouvel Emprunt");
        formViewTitle.setFont(new Font("SansSerif", Font.BOLD, 22));
        formViewTitle.setForeground(new Color(33, 33, 33));
        
        layoutConstraints.gridx = 0; 
        layoutConstraints.gridy = 0; 
        layoutConstraints.gridwidth = 2;
        layoutConstraints.anchor = GridBagConstraints.CENTER;
        this.add(formViewTitle, layoutConstraints);

        // --- GRID ROW 1: CATALOGUE METRICS ---
        layoutConstraints.gridwidth = 1;
        layoutConstraints.gridy = 1;
        layoutConstraints.gridx = 0; 
        layoutConstraints.anchor = GridBagConstraints.LINE_END;
        this.add(new JLabel("Ouvrage :"), layoutConstraints);
        
        layoutConstraints.gridx = 1;
        layoutConstraints.anchor = GridBagConstraints.LINE_START;
        JLabel referenceTitleLabel = new JLabel(_targetedCopyContext.getReference().getTitle());
        referenceTitleLabel.setFont(new Font("SansSerif", Font.ITALIC | Font.BOLD, 14));
        this.add(referenceTitleLabel, layoutConstraints);

        // --- GRID ROW 2: BORROWING MEMBER MAPPING ---
        layoutConstraints.gridx = 0; 
        layoutConstraints.gridy = 2;
        layoutConstraints.anchor = GridBagConstraints.LINE_END;
        this.add(new JLabel("Membre emprunteur :"), layoutConstraints);
        
        layoutConstraints.gridx = 1;
        layoutConstraints.anchor = GridBagConstraints.LINE_START;
        _memberSelectionCombo = new JComboBox<>();
        for (Member systemMember : _businessLogic.getMembers()) {
            _memberSelectionCombo.addItem(new MemberWrapper(systemMember));
        }
        this.add(_memberSelectionCombo, layoutConstraints);

        // --- GRID ROW 3: AUTHORIZING MANAGEMENT CONTROLLER ---
        layoutConstraints.gridx = 0; 
        layoutConstraints.gridy = 3;
        layoutConstraints.anchor = GridBagConstraints.LINE_END;
        this.add(new JLabel("Validé par :"), layoutConstraints);
        
        layoutConstraints.gridx = 1;
        layoutConstraints.anchor = GridBagConstraints.LINE_START;
        _librarianSelectionCombo = new JComboBox<>();
        
        LibrarianWrapper preselectedSessionProfile = null;
        for (Librarian activeStaff : _businessLogic.getLibrarians()) {
            LibrarianWrapper packagedStaff = new LibrarianWrapper(activeStaff);
            _librarianSelectionCombo.addItem(packagedStaff);
            
            if (handler.currentUser != null && activeStaff.getEmail().equals(handler.currentUser.getEmail())) {
                preselectedSessionProfile = packagedStaff;
            }
        }
        
        if (preselectedSessionProfile != null) {
            _librarianSelectionCombo.setSelectedItem(preselectedSessionProfile);
        }
        this.add(_librarianSelectionCombo, layoutConstraints);

        // --- GRID ROW 4: DURATION SCHEDULER BOUNDS ---
        layoutConstraints.gridx = 0; 
        layoutConstraints.gridy = 4;
        layoutConstraints.anchor = GridBagConstraints.LINE_END;
        this.add(new JLabel("Date de retour prévue :"), layoutConstraints);
        
        layoutConstraints.gridx = 1;
        layoutConstraints.anchor = GridBagConstraints.LINE_START;
        
        Calendar absoluteOffsetFrame = Calendar.getInstance();
        Date currentTimelineInstant = absoluteOffsetFrame.getTime();
        absoluteOffsetFrame.add(Calendar.DAY_OF_MONTH, 14);
        Date defaultEstimatedReturnDate = absoluteOffsetFrame.getTime();

        SpinnerDateModel modelStateConstraints = new SpinnerDateModel(
                defaultEstimatedReturnDate, 
                currentTimelineInstant, 
                null, 
                Calendar.DAY_OF_MONTH
        );
        _expectedReturnDatePicker = new JSpinner(modelStateConstraints);
        JSpinner.DateEditor textSegmentEditor = new JSpinner.DateEditor(_expectedReturnDatePicker, "dd/MM/yyyy");
        _expectedReturnDatePicker.setEditor(textSegmentEditor);
        this.add(_expectedReturnDatePicker, layoutConstraints);

        // --- GRID ROW 5: WORKFLOW CONTROL ACTION DECK ---
        JPanel interactionButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        interactionButtonPanel.setBackground(Color.WHITE);

        AccessButton executionCancelBtn = new AccessButton("Annuler", _viewController::goBack);
        AccessButton executionConfirmBtn = new AccessButton("Confirmer l'emprunt", this::processBorrowTransactionPipeline);

        interactionButtonPanel.add(executionCancelBtn);
        interactionButtonPanel.add(executionConfirmBtn);

        layoutConstraints.gridx = 0; 
        layoutConstraints.gridy = 5; 
        layoutConstraints.gridwidth = 2;
        layoutConstraints.insets = new Insets(30, 10, 10, 10);
        layoutConstraints.anchor = GridBagConstraints.CENTER;
        this.add(interactionButtonPanel, layoutConstraints);
    }

    private void processBorrowTransactionPipeline() {
        MemberWrapper activeMemberContext = (MemberWrapper) _memberSelectionCombo.getSelectedItem();
        LibrarianWrapper activeLibrarianContext = (LibrarianWrapper) _librarianSelectionCombo.getSelectedItem();
        Date targetedReturnTimelineDate = (Date) _expectedReturnDatePicker.getValue();

        if (activeMemberContext == null || activeLibrarianContext == null) {
            JOptionPane.showMessageDialog(this, 
                    "Saisie invalide ou profils de compte d'infrastructure manquants.", 
                    "Champs requis", 
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Commit lease initialization parameter models to backend registration maps
            _businessLogic.createBorrow(
                    _targetedCopyContext, 
                    activeMemberContext.getEntity(), 
                    activeLibrarianContext.getEntity(), 
                    targetedReturnTimelineDate
            );
            
            // Sync structural changes down to presentation component caches
            _parentPresentationPanel.updateCopiesTable();
            
            JOptionPane.showMessageDialog(this, 
                    "L'emprunt a été validé et enregistré avec succès !", 
                    "Opération validée", 
                    JOptionPane.INFORMATION_MESSAGE);
            
            _viewController.goBack();
            
        } catch (Exception executionException) {
            JOptionPane.showMessageDialog(this, 
                    "Erreur d'infrastructure lors de la création de la fiche d'emprunt :\n" + executionException.getMessage(), 
                    "Échec d'allocation", 
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}