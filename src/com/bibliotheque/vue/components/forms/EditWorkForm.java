package com.bibliotheque.vue.components.forms;

import com.bibliotheque.model.Author;
import com.bibliotheque.model.Bibliotheque;
import com.bibliotheque.model.Book;
import com.bibliotheque.model.Dvd;
import com.bibliotheque.model.Work;
import com.bibliotheque.vue.MainFrame;
import com.bibliotheque.vue.components.AccessButton;
import com.bibliotheque.vue.work.WorkInfoPanel;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

/**
 * Editorial configuration workspace panel for modifying existing library catalog items.
 * 
 * <p>Manages polymorphic form fields dynamically depending on whether the targeted instance 
 * is a {@link Book} or a {@link Dvd}, resolves multi-author reference indexes, and synchronizes 
 * mutated states back to the tracking {@link WorkInfoPanel}.</p>
 * 
 * @see Work
 * @see Book
 * @see Dvd
 * @see WorkInfoPanel
 * 
 * @version 1.1
 */
public class EditWorkForm extends JPanel {

    private final Bibliotheque              _businessLogic;
    private final Work                      _activeWorkContext;
    private final MainFrame                 _viewController;
    private final WorkInfoPanel             _parentPresentationPanel;

    private final JTextField                _titleField            = new JTextField(20);
    private final JTextField                _categoryField         = new JTextField(20);
    private final JTextField                _publisherField        = new JTextField(20);
    private final JTextField                _polymorphicField      = new JTextField(20);
    
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
     * Constructs an operational modification workspace for updates to active inventory elements.
     * 
     * @param logic       the central tracking repository layer facade instance
     * @param work        the target catalog item instance requiring state alteration
     * @param handler     the primary frame structure view manager coordinator
     * @param parentPanel the tracking dashboard panel handling downstream view updates
     */
    @SuppressWarnings("deprecation")
    public EditWorkForm(Bibliotheque logic, Work work, MainFrame handler, WorkInfoPanel parentPanel) {
        this._businessLogic = logic;
        this._activeWorkContext = work;
        this._viewController = handler;
        this._parentPresentationPanel = parentPanel;

        this.setLayout(new GridBagLayout());
        this.setBackground(Color.WHITE);
        this.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints layoutConstraints = new GridBagConstraints();
        layoutConstraints.insets = new Insets(8, 10, 8, 10);
        layoutConstraints.fill = GridBagConstraints.HORIZONTAL;

        // --- SECTION BANNER HEADER ---
        JLabel bannerViewTitle = new JLabel("Modifier l'œuvre");
        bannerViewTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        bannerViewTitle.setForeground(new Color(33, 33, 33));
        
        layoutConstraints.gridx = 0; 
        layoutConstraints.gridy = 0; 
        layoutConstraints.gridwidth = 2;
        layoutConstraints.anchor = GridBagConstraints.CENTER;
        this.add(bannerViewTitle, layoutConstraints);

        // Map data from target record to input containers
        _titleField.setText(_activeWorkContext.getTitle());
        _categoryField.setText(_activeWorkContext.getCategory());
        _publisherField.setText(_activeWorkContext.getEditor());

        // --- GRID ROW 1: TITLE DATA MATRIX ---
        layoutConstraints.gridwidth = 1;
        layoutConstraints.gridx = 0; 
        layoutConstraints.gridy = 1; 
        layoutConstraints.anchor = GridBagConstraints.LINE_END;
        this.add(new JLabel("Titre :"), layoutConstraints);
        
        layoutConstraints.gridx = 1; 
        layoutConstraints.anchor = GridBagConstraints.LINE_START;
        this.add(_titleField, layoutConstraints);

        // --- GRID ROW 2: TIMETABLE PICKER SUITE ---
        layoutConstraints.gridx = 0; 
        layoutConstraints.gridy = 2; 
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

        // Parse legacy values and preselect combo models
        if (_activeWorkContext.getPublicationDate() != null) {
            Date explicitReleaseDate = _activeWorkContext.getPublicationDate();
            _publicationDayCombo.setSelectedItem(explicitReleaseDate.getDate());
            _publicationMonthCombo.setSelectedIndex(explicitReleaseDate.getMonth());
            _publicationYearCombo.setSelectedItem(explicitReleaseDate.getYear() + 1900);
        }

        segmentedDatePane.add(_publicationDayCombo);
        segmentedDatePane.add(_publicationMonthCombo);
        segmentedDatePane.add(_publicationYearCombo);

        layoutConstraints.gridx = 1; 
        layoutConstraints.anchor = GridBagConstraints.LINE_START;
        this.add(segmentedDatePane, layoutConstraints);

        // --- GRID ROW 3: AUTHOR COLLECTION ASSIGNMENTS ---
        layoutConstraints.gridx = 0; 
        layoutConstraints.gridy = 3; 
        layoutConstraints.anchor = GridBagConstraints.FIRST_LINE_END;
        this.add(new JLabel("Auteur(s) (Ctrl+Clic) :"), layoutConstraints);

        List<Integer> selectedModelIndices = new ArrayList<>();
        int absoluteIndexTrack = 0;

        for (Author systemicAuthor : _businessLogic.getAuthors()) {
            _authorCollectionModel.addElement(systemicAuthor);
            if (_activeWorkContext.getAuthors() != null && _activeWorkContext.getAuthors().contains(systemicAuthor)) {
                selectedModelIndices.add(absoluteIndexTrack);
            }
            absoluteIndexTrack++;
        }

        _authorPickerList = new JList<>(_authorCollectionModel);
        _authorPickerList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        _authorPickerList.setVisibleRowCount(4);

        _authorPickerList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Author) {
                    Author mappingEntity = (Author) value;
                    setText(mappingEntity.getFirstName() + " " + mappingEntity.getLastName());
                }
                return this;
            }
        });

        int[] structuralIndicesArray = selectedModelIndices.stream().mapToInt(i -> i).toArray();
        _authorPickerList.setSelectedIndices(structuralIndicesArray);

        JScrollPane multiSelectScroller = new JScrollPane(_authorPickerList);
        layoutConstraints.gridx = 1; 
        layoutConstraints.anchor = GridBagConstraints.LINE_START;
        this.add(multiSelectScroller, layoutConstraints);

        // --- GRID ROW 4: SYSTEM REGISTRY CATEGORY CODE ---
        layoutConstraints.gridx = 0; 
        layoutConstraints.gridy = 4; 
        layoutConstraints.anchor = GridBagConstraints.LINE_END;
        this.add(new JLabel("Catégorie (Cote) :"), layoutConstraints);
        
        layoutConstraints.gridx = 1; 
        layoutConstraints.anchor = GridBagConstraints.LINE_START;
        this.add(_categoryField, layoutConstraints);

        // --- GRID ROW 5: PRODUCTION HOUSE HOUSING ---
        layoutConstraints.gridx = 0; 
        layoutConstraints.gridy = 5; 
        layoutConstraints.anchor = GridBagConstraints.LINE_END;
        this.add(new JLabel("Éditeur :"), layoutConstraints);
        
        layoutConstraints.gridx = 1; 
        layoutConstraints.anchor = GridBagConstraints.LINE_START;
        this.add(_publisherField, layoutConstraints);

        // --- GRID ROW 6: POLYMORPHIC STRATEGY FIELDS (ISBN vs DVD REGION CODE) ---
        layoutConstraints.gridx = 0; 
        layoutConstraints.gridy = 6; 
        layoutConstraints.anchor = GridBagConstraints.LINE_END;
        JLabel dynamicIdentityLabel = new JLabel();
        
        if (_activeWorkContext instanceof Book) {
            dynamicIdentityLabel.setText("ISBN :");
            _polymorphicField.setText(((Book) _activeWorkContext).getIsbn());
        } else {
            dynamicIdentityLabel.setText("Région DVD :");
            _polymorphicField.setText(((Dvd) _activeWorkContext).getRegion());
        }
        this.add(dynamicIdentityLabel, layoutConstraints);
        
        layoutConstraints.gridx = 1; 
        layoutConstraints.anchor = GridBagConstraints.LINE_START;
        this.add(_polymorphicField, layoutConstraints);

        // --- GRID ROW 7: WORKFLOW CONTROL ACTION DECK ---
        JPanel interactionButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        interactionButtonPanel.setBackground(Color.WHITE);

        AccessButton executionCancelBtn = new AccessButton("Annuler", _viewController::goBack);

        AccessButton executionConfirmBtn = new AccessButton("Enregistrer", this::executeCatalogModificationPipeline);

        interactionButtonPanel.add(executionCancelBtn);
        interactionButtonPanel.add(executionConfirmBtn);

        layoutConstraints.gridx = 0; 
        layoutConstraints.gridy = 7; 
        layoutConstraints.gridwidth = 2;
        layoutConstraints.insets = new Insets(25, 10, 10, 10);
        layoutConstraints.anchor = GridBagConstraints.CENTER;
        this.add(interactionButtonPanel, layoutConstraints);
    }

    @SuppressWarnings("deprecation")
    private void executeCatalogModificationPipeline() {
        String inputTitle       = _titleField.getText().trim();
        String inputCategory    = _categoryField.getText().trim();
        String inputPublisher   = _publisherField.getText().trim();
        String inputPolymorphic = _polymorphicField.getText().trim();
        List<Author> selectedAuthorsList = _authorPickerList.getSelectedValuesList();

        if (inputTitle.isEmpty() || inputCategory.isEmpty() || inputPublisher.isEmpty() || inputPolymorphic.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Veuillez renseigner l'intégralité des champs obligatoires.",
                    "Données manquantes",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (selectedAuthorsList.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "L'œuvre doit être rattachée à au moins un auteur référencé.",
                    "Auteur manquant",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int evaluationDay   = (int) _publicationDayCombo.getSelectedItem();
            int evaluationMonth = _publicationMonthCombo.getSelectedIndex();
            int evaluationYear  = (int) _publicationYearCombo.getSelectedItem();
            Date updatedTimelineFrame = new Date(evaluationYear - 1900, evaluationMonth, evaluationDay);

            _businessLogic.updateWork(
                _activeWorkContext,
                inputTitle,
                inputCategory,
                inputPublisher,
                updatedTimelineFrame,
                selectedAuthorsList,
                inputPolymorphic
            );

            _parentPresentationPanel.updateWorkDetails();
            _parentPresentationPanel.updateCopiesTable();

            JOptionPane.showMessageDialog(this,
                    "L'œuvre a été modifiée avec succès !",
                    "Modification enregistrée",
                    JOptionPane.INFORMATION_MESSAGE);

            _viewController.goBack();

        } catch (Exception executionException) {
            JOptionPane.showMessageDialog(this,
                    "Une erreur système est survenue lors de l'archivage des modifications :\n" + executionException.getMessage(),
                    "Échec d'infrastructure",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}