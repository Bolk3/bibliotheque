package com.bibliotheque.vue.components.forms;

import com.bibliotheque.model.Author;
import com.bibliotheque.model.Bibliotheque;
import com.bibliotheque.model.Book;
import com.bibliotheque.model.Dvd;
import com.bibliotheque.model.Work;
import com.bibliotheque.vue.MainFrame;
import com.bibliotheque.vue.components.AccessButton;
import com.bibliotheque.vue.core.Catalog;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

/**
 * Editorial creation workspace panel used to register new elements into the global catalog inventory.
 * 
 * <p>Handles polymorphic inputs dynamically via a radio control button matrix, switches context validation 
 * between {@link Book} and {@link Dvd} instances, resolves multi-author reference indexes, and triggers data redraws 
 * back to the tracking {@link Catalog} presentation display.</p>
 * 
 * @see Work
 * @see Book
 * @see Dvd
 * @see Catalog
 * 
 * @version 1.1
 */
public class AddWorkForm extends JPanel {

    private final Bibliotheque              _businessLogic;
    private final MainFrame                 _viewController;
    private final Catalog                   _catalogPresentationPanel;

    private final JTextField                _titleField            = new JTextField(20);
    private final JTextField                _categoryField         = new JTextField(20);
    private final JTextField                _publisherField        = new JTextField(20);
    private final JTextField                _polymorphicField      = new JTextField(20);
    
    private final JRadioButton              _bookStrategyRadio     = new JRadioButton("Livre", true);
    private final JRadioButton              _dvdStrategyRadio      = new JRadioButton("DVD");
    private final JLabel                    _dynamicIdentityLabel  = new JLabel("Numéro ISBN :");
    
    private final JList<Author>             _authorPickerList;
    private final DefaultListModel<Author>  _authorCollectionModel = new DefaultListModel<>();

    private final JComboBox<Integer>        _publicationDayCombo   = new JComboBox<>();
    private final JComboBox<String>         _publicationMonthCombo;
    private final JComboBox<Integer>        _publicationYearCombo  = new JComboBox<>();

    private final String[] _localizedMonths = {
        "Janvier", "Février", "Mars", "Avril", "Mai", "Juin", 
        "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"
    };

    /**
     * Constructs an interactive management form workspace to format and append items to the database engine.
     * 
     * @param logic        the central tracking repository layer facade instance
     * @param handler      the primary frame structure view manager coordinator
     * @param catalogPanel the master visual catalog panel handling operational workflow updates
     */
    @SuppressWarnings("deprecation")
    public AddWorkForm(Bibliotheque logic, MainFrame handler, Catalog catalogPanel) {
        this._businessLogic = logic;
        this._viewController = handler;
        this._catalogPresentationPanel = catalogPanel;

        this.setLayout(new GridBagLayout());
        this.setBackground(Color.WHITE);
        this.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints layoutConstraints = new GridBagConstraints();
        layoutConstraints.insets = new Insets(8, 10, 8, 10);
        layoutConstraints.fill = GridBagConstraints.HORIZONTAL;

        // --- SECTION BANNER HEADER ---
        JLabel formViewTitle = new JLabel("Ajouter une nouvelle œuvre");
        formViewTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        formViewTitle.setForeground(new Color(33, 33, 33));
        
        layoutConstraints.gridx = 0; 
        layoutConstraints.gridy = 0; 
        layoutConstraints.gridwidth = 2;
        layoutConstraints.anchor = GridBagConstraints.CENTER;
        this.add(formViewTitle, layoutConstraints);

        // --- GRID ROW 1: POLYMORPHIC TYPE SELECTION MATRIX ---
        layoutConstraints.gridwidth = 1;
        layoutConstraints.gridx = 0; 
        layoutConstraints.gridy = 1; 
        layoutConstraints.anchor = GridBagConstraints.LINE_END;
        this.add(new JLabel("Type d'œuvre :"), layoutConstraints);

        JPanel strategyTogglePane = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        strategyTogglePane.setBackground(Color.WHITE);
        
        ButtonGroup typeAssignmentGroup = new ButtonGroup();
        typeAssignmentGroup.add(_bookStrategyRadio);
        typeAssignmentGroup.add(_dvdStrategyRadio);
        strategyTogglePane.add(_bookStrategyRadio);
        strategyTogglePane.add(_dvdStrategyRadio);
        
        layoutConstraints.gridx = 1; 
        layoutConstraints.anchor = GridBagConstraints.LINE_START;
        this.add(strategyTogglePane, layoutConstraints);

        // Bind presentation event tracking macro handlers to type toggle actions
        _bookStrategyRadio.addActionListener(e -> _dynamicIdentityLabel.setText("Numéro ISBN :"));
        _dvdStrategyRadio.addActionListener(e -> _dynamicIdentityLabel.setText("Code Région DVD :"));

        // --- GRID ROW 2: TITLE DATA INPUT ---
        layoutConstraints.gridx = 0; 
        layoutConstraints.gridy = 2; 
        layoutConstraints.anchor = GridBagConstraints.LINE_END;
        this.add(new JLabel("Titre de l'œuvre :"), layoutConstraints);
        
        layoutConstraints.gridx = 1; 
        layoutConstraints.anchor = GridBagConstraints.LINE_START;
        this.add(_titleField, layoutConstraints);

        // --- GRID ROW 3: TIMETABLE PICKER SUITE ---
        layoutConstraints.gridx = 0; 
        layoutConstraints.gridy = 3; 
        layoutConstraints.anchor = GridBagConstraints.LINE_END;
        this.add(new JLabel("Date de parution :"), layoutConstraints);

        JPanel segmentedDatePane = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        segmentedDatePane.setBackground(Color.WHITE);
        
        for (int dayIndex = 1; dayIndex <= 31; dayIndex++) {
            _publicationDayCombo.addItem(dayIndex);
        }
        
        _publicationMonthCombo = new JComboBox<>(_localizedMonths);
        
        int currentCalendarYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int yearIndex = currentCalendarYear; yearIndex >= 1800; yearIndex--) {
            _publicationYearCombo.addItem(yearIndex);
        }

        // Initialize temporal controls to map current system day context
        Calendar systemClockInstance = Calendar.getInstance();
        _publicationDayCombo.setSelectedItem(systemClockInstance.get(Calendar.DAY_OF_MONTH));
        _publicationMonthCombo.setSelectedIndex(systemClockInstance.get(Calendar.MONTH));
        _publicationYearCombo.setSelectedItem(systemClockInstance.get(Calendar.YEAR));

        segmentedDatePane.add(_publicationDayCombo);
        segmentedDatePane.add(_publicationMonthCombo);
        segmentedDatePane.add(_publicationYearCombo);

        layoutConstraints.gridx = 1; 
        layoutConstraints.anchor = GridBagConstraints.LINE_START;
        this.add(segmentedDatePane, layoutConstraints);

        // --- GRID ROW 4: AUTHOR SELECTION COMPONENT ASSIGNMENTS ---
        layoutConstraints.gridx = 0; 
        layoutConstraints.gridy = 4; 
        layoutConstraints.anchor = GridBagConstraints.FIRST_LINE_END;
        this.add(new JLabel("Auteur(s) (Ctrl+Clic) :"), layoutConstraints);

        for (Author activeSystemAuthor : _businessLogic.getAuthors()) {
            _authorCollectionModel.addElement(activeSystemAuthor);
        }
        
        _authorPickerList = new JList<>(_authorCollectionModel);
        _authorPickerList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        _authorPickerList.setVisibleRowCount(4);
        
        _authorPickerList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Author) {
                    Author translationMappingEntity = (Author) value;
                    setText(translationMappingEntity.getFirstName() + " " + translationMappingEntity.getLastName());
                }
                return this;
            }
        });

        JScrollPane multiSelectScroller = new JScrollPane(_authorPickerList);
        layoutConstraints.gridx = 1; 
        layoutConstraints.anchor = GridBagConstraints.LINE_START;
        this.add(multiSelectScroller, layoutConstraints);

        // --- GRID ROW 5: SYSTEM REGISTRY CATEGORY CODE ---
        layoutConstraints.gridx = 0; 
        layoutConstraints.gridy = 5; 
        layoutConstraints.anchor = GridBagConstraints.LINE_END;
        this.add(new JLabel("Catégorie (Cote) :"), layoutConstraints);
        
        layoutConstraints.gridx = 1; 
        layoutConstraints.anchor = GridBagConstraints.LINE_START;
        this.add(_categoryField, layoutConstraints);

        // --- GRID ROW 6: PRODUCTION HOUSE HOUSING ---
        layoutConstraints.gridx = 0; 
        layoutConstraints.gridy = 6; 
        layoutConstraints.anchor = GridBagConstraints.LINE_END;
        this.add(new JLabel("Éditeur / Studio :"), layoutConstraints);
        
        layoutConstraints.gridx = 1; 
        layoutConstraints.anchor = GridBagConstraints.LINE_START;
        this.add(_publisherField, layoutConstraints);

        // --- GRID ROW 7: POLYMORPHIC FIELD CONTAINER ---
        layoutConstraints.gridx = 0; 
        layoutConstraints.gridy = 7; 
        layoutConstraints.anchor = GridBagConstraints.LINE_END;
        this.add(_dynamicIdentityLabel, layoutConstraints);
        
        layoutConstraints.gridx = 1; 
        layoutConstraints.anchor = GridBagConstraints.LINE_START;
        this.add(_polymorphicField, layoutConstraints);

        // --- GRID ROW 8: WORKFLOW CONTROL ACTION DECK ---
        JPanel interactionButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        interactionButtonPanel.setBackground(Color.WHITE);

        AccessButton executionCancelBtn = new AccessButton("Annuler", _viewController::goBack);

        AccessButton executionConfirmBtn = new AccessButton("Enregistrer", this::executeCatalogAdditionPipeline);

        interactionButtonPanel.add(executionCancelBtn);
        interactionButtonPanel.add(executionConfirmBtn);

        layoutConstraints.gridx = 0; 
        layoutConstraints.gridy = 8; 
        layoutConstraints.gridwidth = 2;
        layoutConstraints.insets = new Insets(25, 10, 10, 10);
        layoutConstraints.anchor = GridBagConstraints.CENTER;
        this.add(interactionButtonPanel, layoutConstraints);
    }

    @SuppressWarnings("deprecation")
    private void executeCatalogAdditionPipeline() {
        String inputTitle       = _titleField.getText().trim();
        String inputCategory    = _categoryField.getText().trim();
        String inputPublisher   = _publisherField.getText().trim();
        String inputPolymorphic = _polymorphicField.getText().trim();
        List<Author> selectedAuthorsList = _authorPickerList.getSelectedValuesList();

        if (inputTitle.isEmpty() || inputCategory.isEmpty() || inputPublisher.isEmpty() || inputPolymorphic.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                    "Veuillez renseigner l'intégralité des champs obligatoires avant de valider.", 
                    "Champs vides", 
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (selectedAuthorsList.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                    "Veuillez affecter au moins un auteur à cette œuvre (Maintenez Ctrl pour choix multiple).", 
                    "Auteur manquant", 
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Build absolute temporal stamp mapping selected combo values
            int evaluationDay   = (int) _publicationDayCombo.getSelectedItem();
            int evaluationMonth = _publicationMonthCombo.getSelectedIndex();
            int evaluationYear  = (int) _publicationYearCombo.getSelectedItem();
            Date calculatedTimelineFrame = new Date(evaluationYear - 1900, evaluationMonth, evaluationDay);

            Work instantiatedWorkEntity;
            if (_bookStrategyRadio.isSelected()) {
                instantiatedWorkEntity = new Book(inputPolymorphic, inputTitle, inputCategory, inputPublisher, calculatedTimelineFrame, _businessLogic);
            } else {
                instantiatedWorkEntity = new Dvd(inputTitle, inputCategory, inputPublisher, calculatedTimelineFrame, _businessLogic, inputPolymorphic);
            }

            // Append author relations to unified relational wrapper
            for (Author targetingAuthor : selectedAuthorsList) {
                instantiatedWorkEntity.addAuthor(targetingAuthor);
            }

            // Push instantiated item definition down to system registry maps
            _businessLogic.addWork(instantiatedWorkEntity); 
            _catalogPresentationPanel.reloadCatalogData();

            JOptionPane.showMessageDialog(this, 
                    "L'œuvre a été ajoutée avec succès au catalogue !", 
                    "Enregistrement réussi", 
                    JOptionPane.INFORMATION_MESSAGE);
            
            _viewController.goBack(); 
            
        } catch (Exception executionException) {
            JOptionPane.showMessageDialog(this, 
                    "Une erreur d'infrastructure est survenue lors de l'archivage de l'œuvre :\n" + executionException.getMessage(), 
                    "Erreur de création", 
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}