package com.bibliotheque.vue.core;

import com.bibliotheque.errors.SearchStringTooSmall;
import com.bibliotheque.model.Bibliotheque;
import com.bibliotheque.model.Book;
import com.bibliotheque.model.Dvd;
import com.bibliotheque.model.SearchingWork;
import com.bibliotheque.model.Work;
import com.bibliotheque.vue.MainFrame;
import com.bibliotheque.vue.components.AccessButton;
import com.bibliotheque.vue.components.Workshow;
import com.bibliotheque.vue.components.forms.AddWorkForm;
import com.bibliotheque.vue.controllers.PageController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/**
 * Visual media repository indexing UI view tracking item records within the catalog subsystem.
 * 
 * <p>Provides granular query filters to scan multi-type collections, handles segmented 
 * multi-tier list layout pagination, and provisions hooks to request append profiles.</p>
 * 
 * @see Work
 * @see SearchingWork
 * @see MainFrame
 * 
 * @version 1.2
 */
public class Catalog extends JPanel {

    private int                     _currentPageIndex = 0;
    private final Bibliotheque      _businessLogic; 
    private final MainFrame         _viewController; 
    
    private final Vector<JPanel>    _renderedWorkPanels = new Vector<>();
    private final Vector<JButton>   _paginationTriggers = new Vector<>();
    
    private final JPanel            _displayContainer   = new JPanel();
    private final JPanel            _searchActionBar    = new JPanel();
    private final JPanel            _paginationBar      = new JPanel();

    private final JTextField        _searchField        = new JTextField(15);
    private final JComboBox<String> _searchTypeCombo    = new JComboBox<>(new String[]{
        "Titre", "Éditeur", "Type (Book/Dvd)", "Code Région (DVD)", "ISBN (Livre)"
    });
    
    private final JButton           _executeSearchBtn   = new JButton("Rechercher");
    private final JButton           _resetSearchBtn     = new JButton("X");

    /**
     * Constructs a pagination-enabled visual indexing catalog dashboard viewport panel.
     * 
     * @param handle the operational central library backend facade instance
     * @param view   the principal structural navigation view framework context
     */
    public Catalog(Bibliotheque handle, MainFrame view) {
        this._businessLogic = handle;
        this._viewController = view;
        
        this.setLayout(new BorderLayout(0, 10));
        this.setBackground(Color.WHITE);
        this.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Setup central scrollable catalog rows matrix layout container logic
        _displayContainer.setLayout(new GridLayout(0, 1, 0, 8));
        _displayContainer.setBackground(Color.WHITE);

        JPanel scrollContentWrapper = new JPanel(new BorderLayout());
        scrollContentWrapper.setBackground(Color.WHITE);
        scrollContentWrapper.add(_displayContainer, BorderLayout.NORTH);
        
        JScrollPane scrollPane = new JScrollPane(scrollContentWrapper);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // --- SECTION HAUT : SEARCH UTILITIES & PROVISIONING ACTION BAR ---
        _searchActionBar.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 8));
        _searchActionBar.setBackground(new Color(245, 245, 245));
        _searchActionBar.setBorder(BorderFactory.createEtchedBorder());
        
        JLabel filterHeaderLabel = new JLabel("Rechercher par :");
        filterHeaderLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        
        _searchActionBar.add(filterHeaderLabel);
        _searchActionBar.add(_searchTypeCombo);
        _searchActionBar.add(_searchField);
        _searchActionBar.add(_executeSearchBtn);
        _searchActionBar.add(_resetSearchBtn);
        
        AccessButton appendWorkTriggerBtn = new AccessButton("Ajouter une œuvre", () -> 
                _viewController.goForward(new AddWorkForm(_businessLogic, _viewController, this)));
        _searchActionBar.add(appendWorkTriggerBtn);

        // --- EVENT LIGATURES DISPATCH BINDING ---
        _executeSearchBtn.addActionListener(e -> this.executeParametricSearch());
        _searchField.addActionListener(e -> this.executeParametricSearch());
        _resetSearchBtn.addActionListener(e -> {
            _searchField.setText("");
            this.reloadCatalogData();
        });

        _paginationBar.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        _paginationBar.setBackground(Color.WHITE);

        // Initial populating trace
        this.reloadCatalogData();

        this.add(_searchActionBar, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
        this.add(_paginationBar, BorderLayout.SOUTH);
    }

    /**
     * Extracts complete unstructured records from the data layer collection model 
     * to rebuild full non-filtered indexing grids.
     */
    public void reloadCatalogData() {
        List<Work> completeCatalogRegistry = new ArrayList<>();
        for (int i = 0; i < _businessLogic.getCatalogue().size(); i++) {
            completeCatalogRegistry.add(_businessLogic.get(i));
        }
        this.rebuildPagination(completeCatalogRegistry);
    }

    /**
     * Resolves localized context search inputs using mapping adapters from utility filters.
     */
    private void executeParametricSearch() {
        String queryStr = _searchField.getText().trim();
        String activeCriteria = (String) _searchTypeCombo.getSelectedItem();
        
        if (queryStr.isEmpty()) {
            this.reloadCatalogData();
            return;
        }

        try {
            List<Work> hitsResultsRegistry = new ArrayList<>();

            switch (activeCriteria != null ? activeCriteria : "Titre") {
                case "Titre":
                    hitsResultsRegistry = SearchingWork.search(_businessLogic.getCatalogue(), queryStr, Work::getTitle);
                    break;
                    
                case "Éditeur":
                    hitsResultsRegistry = SearchingWork.search(_businessLogic.getCatalogue(), queryStr, Work::getEditor);
                    break;
                    
                case "Type (Book/Dvd)":
                    if (queryStr.equalsIgnoreCase("book") || queryStr.equalsIgnoreCase("livre")) {
                        List<Book> typedBooks = SearchingWork.searchByType(_businessLogic.getCatalogue(), Book.class);
                        hitsResultsRegistry.addAll(typedBooks);
                    } else if (queryStr.equalsIgnoreCase("dvd")) {
                        List<Dvd> typedDvds = SearchingWork.searchByType(_businessLogic.getCatalogue(), Dvd.class);
                        hitsResultsRegistry.addAll(typedDvds);
                    } else {
                        throw new SearchStringTooSmall("Veuillez saisir 'book', 'livre' ou 'dvd' pour filtrer par catégorie.");
                    }
                    break;

                case "Code Région (DVD)":
                    List<Dvd> matchingRegionDvds = SearchingWork.searchByRegion(_businessLogic.getCatalogue(), queryStr);
                    hitsResultsRegistry.addAll(matchingRegionDvds);
                    break;

                case "ISBN (Livre)":
                    Optional<Book> matchedIsbnBook = SearchingWork.searchByIsbn(_businessLogic.getCatalogue(), queryStr);
                    matchedIsbnBook.ifPresent(hitsResultsRegistry::add);
                    break;
            }

            _currentPageIndex = 0; 
            this.rebuildPagination(hitsResultsRegistry);

        } catch (SearchStringTooSmall errorException) {
            JOptionPane.showMessageDialog(this, 
                    errorException.getMessage(), 
                    "Erreur de recherche", 
                    JOptionPane.WARNING_MESSAGE);
        } catch (Exception unexpectedException) {
            JOptionPane.showMessageDialog(this, 
                    "Une erreur est survenue lors du traitement du filtrage.", 
                    "Erreur", 
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Clears existing visual rows vectors, rebuilding page button bounds mapping arrays.
     * 
     * @param worksToDisplay specific dataset segment arrays intended to match tracking view grids
     */
    private void rebuildPagination(List<Work> worksToDisplay) {
        _renderedWorkPanels.clear();
        _paginationTriggers.clear();
        _displayContainer.removeAll();
        _paginationBar.removeAll();

        int collectionScopeCount = worksToDisplay.size();

        for (int i = 0; i < collectionScopeCount; i++) {
            Work entityRecord = worksToDisplay.get(i);
            _renderedWorkPanels.add(new Workshow(entityRecord, _viewController, _businessLogic, this));
            
            // Build pagination layout milestones dynamically every 10 elements
            if (i % 10 == 0) {
                int trackingPageIndex = i / 10;
                JButton pageStepButton = new JButton(String.valueOf(trackingPageIndex + 1));
                pageStepButton.addActionListener(new PageController(this, trackingPageIndex));
                
                _paginationTriggers.add(pageStepButton);
                _paginationBar.add(pageStepButton);
            }
        }

        // Sanitize vector ceiling index overstep scenarios
        if (_currentPageIndex * 10 >= _renderedWorkPanels.size() && _currentPageIndex > 0) {
            _currentPageIndex = 0;
        }

        this.updateDisplay();
        
        _paginationBar.revalidate();
        _paginationBar.repaint();
    }

    private void updateDisplay() {
        _displayContainer.removeAll();

        int structuralFloorLimit = _currentPageIndex * 10;
        int structuralCeilingLimit = (_currentPageIndex + 1) * 10;

        for (int i = structuralFloorLimit; i < structuralCeilingLimit; i++) {
            if (i < _renderedWorkPanels.size()) {
                _displayContainer.add(_renderedWorkPanels.get(i));
            }
        }

        _displayContainer.revalidate(); 
        _displayContainer.repaint();    
    }

    /**
     * Checks if the evaluated index matches the active viewport configuration marker.
     * 
     * @param checkedPage theoretical target offset query input value
     * @return true if matches tracking state variables
     */
    public Boolean isCurentPage(int checkedPage) {
        return this._currentPageIndex == checkedPage;
    }

    /**
     * Navigates the rendering framework indices offset metrics to redraw segment panels.
     * 
     * @param targetPageIndex targeted logical system grid collection list step index
     */
    public void changePage(int targetPageIndex) {
        this._currentPageIndex = targetPageIndex;
        this.updateDisplay();
    }
}