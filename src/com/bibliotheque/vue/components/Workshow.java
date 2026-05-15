package com.bibliotheque.vue.components;

import com.bibliotheque.model.Author;
import com.bibliotheque.model.Bibliotheque;
import com.bibliotheque.model.Book;
import com.bibliotheque.model.Work;
import com.bibliotheque.vue.MainFrame;
import com.bibliotheque.vue.core.Catalog;
import com.bibliotheque.vue.work.WorkInfoPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Visual display thumbnail component rendering abbreviated overview metadata for a single {@link Work}.
 * 
 * <p>Formats publication attributes dynamically, determines polymorphic taxonomy representations 
 * (Books vs. DVDs), and applies adaptive grid dimension constraints relative to its parent {@link Catalog}.</p>
 * 
 * @see Work
 * @see Catalog
 * @see MainFrame
 * 
 * @version 1.1
 */
public class Workshow extends JPanel {

    private final Work              _workRecord;
    private final Catalog           _parentCatalogContext; 
    
    private final JPanel            _metadataContainer  = new JPanel();
    private final JPanel            _thumbnailContainer = new JPanel();
    private final JPanel            _actionContainer    = new JPanel();
    
    private final JLabel            _typeBadgeLabel     = new JLabel();
    private final JLabel            _titleLabel;
    private final JLabel            _authorLabel;
    private final JLabel            _publicationLabel;
    
    private final SimpleDateFormat  _dateFormatter      = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Constructs a single catalog entry overview display block.
     * 
     * @param handler the central business entity context reference being mapped
     * @param view    the primary application navigation view framework manager
     * @param bib     the central system data layer manager facade
     * @param catalog the containing paginated view catalog tracking reference
     */
    public Workshow(Work handler, MainFrame view, Bibliotheque bib, Catalog catalog) {
        this._workRecord = handler;
        this._parentCatalogContext = catalog;
        
        this.setLayout(new BorderLayout(15, 0));
        this.setBackground(Color.WHITE);
        this.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));

        // --- SECTION GAUCHE : THUMBNAIL PLACEHOLDER CONTAINER ---
        _thumbnailContainer.setBackground(new Color(245, 245, 245));
        _thumbnailContainer.setPreferredSize(new Dimension(60, 0));
        _thumbnailContainer.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        
        JLabel mediaIconPlaceholder = new JLabel("Œuvre", JLabel.CENTER);
        mediaIconPlaceholder.setFont(new Font("SansSerif", Font.ITALIC, 10));
        mediaIconPlaceholder.setForeground(Color.GRAY);
        _thumbnailContainer.setLayout(new BorderLayout());
        _thumbnailContainer.add(mediaIconPlaceholder, BorderLayout.CENTER);

        // --- SECTION CENTRAL : METADATA TYPOGRAPHY RESOLUTIONS ---
        _metadataContainer.setBackground(Color.WHITE);
        _metadataContainer.setLayout(new GridLayout(4, 1, 2, 2));

        // Evaluate inheritance chains to apply distinct metadata taxonomy badges
        _typeBadgeLabel.setFont(new Font("SansSerif", Font.BOLD, 11));
        if (this._workRecord instanceof Book) {
            _typeBadgeLabel.setText("[LIVRE]");
            _typeBadgeLabel.setForeground(new Color(0, 102, 204));
        } else {
            _typeBadgeLabel.setText("[DVD]");
            _typeBadgeLabel.setForeground(new Color(153, 0, 153));
        }

        _titleLabel = new JLabel(this._workRecord.getTitle());
        _titleLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        _titleLabel.setForeground(new Color(33, 33, 33));

        _authorLabel = new JLabel(this.resolveAuthorsFormatting());
        _authorLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        _authorLabel.setForeground(Color.DARK_GRAY);

        String dateStringValue = (this._workRecord.getPublicationDate() != null) 
                ? "Parution : " + _dateFormatter.format(this._workRecord.getPublicationDate()) 
                : "Date de parution inconnue";
        _publicationLabel = new JLabel(dateStringValue);
        _publicationLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        _publicationLabel.setForeground(Color.GRAY);

        _metadataContainer.add(_typeBadgeLabel);
        _metadataContainer.add(_titleLabel);
        _metadataContainer.add(_authorLabel);
        _metadataContainer.add(_publicationLabel);

        // --- SECTION DROITE : CONTEXTUAL REDIRECTION TRIGGERS ---
        _actionContainer.setBackground(Color.WHITE);
        _actionContainer.setLayout(new BorderLayout());
        
        AccessButton viewDetailsButton = new AccessButton("Voir", () -> 
                view.goForward(new WorkInfoPanel(handler, bib, view)));
        _actionContainer.add(viewDetailsButton, BorderLayout.CENTER);

        // Bind layouts back to root composite panel context
        this.add(_thumbnailContainer, BorderLayout.WEST);
        this.add(_metadataContainer, BorderLayout.CENTER);
        this.add(_actionContainer, BorderLayout.EAST);
    }

    private String resolveAuthorsFormatting() {
        if (this._workRecord.getAuthors() == null || this._workRecord.getAuthors().isEmpty()) {
            return "Auteur inconnu";
        }

        StringBuilder authorsBuilder = new StringBuilder();
        int totalAuthors = this._workRecord.getAuthors().size();
        int iteratorCounter = 0;
        
        for (Author authorRecord : this._workRecord.getAuthors()) {
            authorsBuilder.append(authorRecord.getFirstName())
                          .append(" ")
                          .append(authorRecord.getLastName());
            iteratorCounter++;
            
            if (iteratorCounter < totalAuthors) {
                authorsBuilder.append(", ");
            }
        }
        return authorsBuilder.toString();
    }

    /**
     * Derives component execution heights relative to live display bounds of the container shell.
     * 
     * @return proportional dimension layouts matching calculated scale targets
     */
    @Override
    public Dimension getPreferredSize() {
        if (_parentCatalogContext != null && _parentCatalogContext.getHeight() > 0) {
            // Partition internal catalog heights equally into 11 parts to account for layout padding
            int dynamicCalculatedHeight = _parentCatalogContext.getHeight() / 11; 
            return new Dimension(super.getPreferredSize().width, Math.max(dynamicCalculatedHeight, 85));
        }
        return new Dimension(super.getPreferredSize().width, 85);
    }
}