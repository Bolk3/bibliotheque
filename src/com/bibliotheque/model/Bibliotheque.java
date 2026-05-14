package com.bibliotheque.model;

import com.bibliotheque.errors.SearchStringTooSmall;
import com.bibliotheque.errors.SearchClassNotInherits;
import java.util.*;
import java.util.stream.Collectors;

public class Bibliotheque {

    private String _nom;
    private String _adresse;
    
    private final Set<Work>      _catalogue  = new HashSet<>();
    private final Set<Author>    _authors    = new HashSet<>();
    private final Set<Member>    _members    = new HashSet<>();
    private final Set<Librarian> _librarians = new HashSet<>();
    private final Set<Event>     _events     = new HashSet<>();

    public Bibliotheque(String nom, String adresse) {
        this._nom = nom;
        this._adresse = adresse;
    }

    // --- Gestion des entités ---
    public void addWork(Work work)           { this._catalogue.add(work); }
    public void addAuthor(Author author)     { this._authors.add(author); }
    public void addMember(Member member)     { this._members.add(member); }
    public void addLibrarian(Librarian lib)  { this._librarians.add(lib); }
    public void addEvent(Event event)        { this._events.add(event); }

    // --- Logique métier : Emprunt ---
    public Borrow createBorrow(Copy copy, Member borrowedBy, Librarian validatedBy, Date expectedDate) {
        // Vérifications de sécurité
        if (copy == null || borrowedBy == null || validatedBy == null) {
            throw new IllegalArgumentException("Les paramètres ne peuvent pas être nuls.");
        }
        if (!copy.isAvailable()) {
            throw new IllegalStateException("Cette copie n'est pas disponible actuellement.");
        }
        if (borrowedBy.isBlocked()) {
            throw new IllegalStateException("Le membre est bloqué et ne peut pas emprunter.");
        }
        
        // Création de l'emprunt
        Borrow borrow = new Borrow(expectedDate, validatedBy, borrowedBy, copy);
        
        // Mise à jour des relations (Important !)
        copy.addBorrowing(borrow);      // L'exemplaire sait qu'il est emprunté
        borrowedBy.addBorrow(borrow);   // Le membre sait ce qu'il a emprunté
        
        return borrow;
    }

    // --- Recherche ---
    public List<Work> findWorksByTitle(String q) throws SearchStringTooSmall {
        return SearchingWork.search(this._catalogue, q, Work::getTitle);
    }

    public List<Borrow> getLateBorrows() {
        return this._catalogue.stream()
                .flatMap(w -> w.getCopies().stream())
                .flatMap(c -> c.getBorrowings().stream())
                .filter(b -> !b.isReturned() && b.isLate())
                .collect(Collectors.toList());
    }

    // --- Getters ---
    public String getNom() { return _nom; }
    public Set<Work> getCatalogue() { return Collections.unmodifiableSet(_catalogue); }
}