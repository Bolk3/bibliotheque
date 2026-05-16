<h1 align="center">Gestion d'une bibliotheque</h1>

*Renald Sonnet, Badreddine Zaidi*

*le 16/05/2026*

<h2>1 - Introduction</h2>

<h3>1.1 - Choix du projet</h3>
<p>
Au vu des sujets proposées, la gestion d’une bibliothèque a bien plus retenu notre attention qu’un sujet libre. Ce choix, il est vrai, s'est imposé comme une évidence : le domaine apparaît en effet comme un modèle logique clair et bien délimité: gestion des œuvres, des membres, des emprunts et du personnel; ce qui paraît pertinent pour une modélisation UML rigoureuse et une adéquation correcte des principes orientés objet vu en classe. 
</p>

<p>
Notre modélisation initiale s'est cependant révélée insuffisamment complexe au regard des 
attentes pour un projet en binôme. Suite aux retours du professeur, nous avons enrichi le 
diagramme UML, ce qui a conduit à revoir une partie de l'architecture du code en conséquence.
</p>

<h3>1.2 - Philosophie de travail</h3>
<p>
Afin de produire un code maintenable et lisible, nous avons tenté d'appliquer 
un ensemble de conventions tout au long du développement :
</p>
<ul>
    <li>Préfixer les attributs privés d'un underscore (<code>_nomVariable</code>)</li>
    <li>Rédiger le code en anglais (noms de variables, méthodes, classes)</li>
    <li>Documenter toutes les méthodes et classes via des commentaires Javadoc</li>
    <li>Écrire des tests unitaires pour le model logique ainsi que des scénarios 
    fonctionnels plus complets</li>
    <li>Privilégier les méthodes et attributs statiques lorsque cela est pertinent</li>
</ul>
<p>
Le respect de ces conventions a été inégal au fil du projet, notamment en raison 
des contraintes de temps. Les écarts constatés sont détaillés dans la section 
Améliorations.
</p>

---

<h2>2 - Fonctionnement</h2>

<h3>2.1 - Architecture & modélisation UML</h3>

![uml](./images/Screenshot_20260402_091052.png) 

<p>En observent l'uml nous pouvons distunguer la formation de quatre parties bien distinct:</p>

- Le Gestionnaire logique (Bibloitheque)
- Une partie de gestion des oeuvres
- Une partie de gestion des personnes et personel
- Une partie de gestion d'evenement 

<p>Comme demandé au debut du projet nous utilisons le paradigme Model - Vue - Controller comme enseigner en cours</p>

<h3>2.2 - Fonctionement Reel</h3>

<p>
L'application se présente sous la forme d'un <code>JFrame</code> principal divisé en deux zones : 
un menu de navigation fixe sur la gauche permettant de basculer entre les différentes vues, 
et une zone centrale qui se met à jour en fonction de la sélection.
</p>
<p>
Les vues implémentées couvrent la gestion du catalogue, des membres, du personnel, des auteurs 
et des copies d'œuvres. Pour chacune de ces entités, il est possible d'ajouter de nouveaux 
enregistrements et de modifier les existants. La gestion des emprunts est également fonctionnelle, 
ainsi que la consultation de l'historique des membres et du personnel.
</p>
<p>
Le catalogue dispose d'un système de filtrage permettant de rechercher une œuvre par titre, 
éditeur, type (livre ou DVD), ISBN pour les livres, ou région pour les DVDs.
</p>
<p>
La saisie des données est sécurisée par une classe utilitaire statique <code>ValidationUtils.java</code> qui 
normalise et valide les entrées via des expressions régulières, garantissant la cohérence 
des noms, prénoms et adresses email enregistrés.
</p>

<h3>2.3 - Fonctionalité non implementé</h3>

<p>
Par manque de temps, certaines fonctionnalités prévues dans la modélisation UML n'ont pas pu 
être implémentées :
</p>
<ul>
    <li>La <strong>suppression</strong> des entités (membres, œuvres, personnel, copies) — 
    seuls l'ajout et la modification sont disponibles</li>
    <li>L'affichage des <strong>images de couverture</strong> des œuvres dans le catalogue</li>
    <li>Les vues relatives à la <strong>gestion des événements</strong>, pourtant modélisées 
    dans l'UML</li>
</ul>

---

<h2>3 - Tests</h2>
<p>
Pour simplifier la compilation ainsi que les tests à effectuer, nous avons opté pour 
l'utilisation de l'outil <a href="https://www.gnu.org/software/make/">GNU Make</a>. 
Bien qu'issu de l'écosystème Linux, Make est également disponible sur Windows et Mac.
</p>
<p>
Make fonctionne à partir d'un fichier <code>Makefile</code> qui définit des règles de 
compilation et d'exécution. Chaque règle, appelée <em>cible</em>, regroupe un ensemble 
de commandes shell exécutées en une seule instruction <code>make [cible]</code>. 
Cela évite de retaper manuellement les commandes <code>javac</code> et <code>java</code> 
à chaque modification du code.
</p>
<h3>3.1 - Mise en place (Makefile)</h3>
<p>Les commandes disponibles sont les suivantes :</p>

| Command       | Use                          |
| ------------- | ---------------------------- |
| `make`        | Compile the project          |
| `make run`    | Launch the app               |
| `make clean`  | Delete `out/`                |
| `make fclean` | Delete `out/` and `lib/`     |
| `make re`     | fclean + make                |
| `make setup`  | Download JUnit 5 via curl    |
| `make test`   | Compile and run the tests    |

<h3>3.2 - Tests Unitaires</h3>
<p>
Les tests unitaires ont été réalisés avec le framework 
<a href="https://junit.org/junit5/">JUnit 5</a>. La librairie peut être récupérée 
automatiquement via la commande <code>make setup</code> qui utilise <code>curl</code> 
pour télécharger le jar nécessaire dans le dossier <code>lib/</code>.
</p>
<p>
La suite de tests couvre l'intégralité du modèle logique, organisée en 13 classes 
de test pour 124 tests, tous passants. Chaque classe de test cible une classe du modèle 
et est subdivisée en groupes thématiques :
</p>
<ul>
    <li><strong>ValidationUtilsTest</strong>: validation des noms, prénoms et emails 
    via regex, normalisation des chaînes accentuées et des adresses email</li>
    <li><strong>UserTest</strong>: validation des données d'identité, requêtes normalisées, 
    rejet des entrées invalides</li>
    <li><strong>AuthorTest</strong>: construction, mutateurs, requêtes normalisées, 
    gestion des œuvres associées</li>
    <li><strong>MemberTest</strong>: état initial, gestion des pénalités et du blocage, 
    contrôle des droits d'accès par niveau de permission</li>
    <li><strong>LibrarianTest</strong>: historique des emprunts et tampons validés</li>
    <li><strong>SpeakerTest</strong>: assignation aux événements, correspondance de spécialité</li>
    <li><strong>BorrowTest</strong>: cycle complet d'un emprunt : initialisation, prolongation, 
    retour, détection des dégradations, calcul du temps écoulé</li>
    <li><strong>StampTest</strong>: intégrité des données, copies défensives des dates, 
    cas limites pour <code>ExtensionStamp</code> et <code>ReturnStamp</code></li>
    <li><strong>CopyTest</strong>: disponibilité, cycle de vie des emprunts, détection 
    du retard, encapsulation des collections</li>
    <li><strong>WorksTest</strong>: gestion des auteurs et des copies, setters communs, 
    spécificités de <code>Book</code> (ISBN) et <code>Dvd</code> (région)</li>
    <li><strong>SearchingWorkTest</strong>: recherche générique par titre et éditeur, 
    recherche par ISBN, par région, par date de publication, filtrage par type</li>
    <li><strong>BibliothequeTest</strong>: création d'emprunts, règles métier, 
    suppression en cascade des auteurs, mise à jour des œuvres, détection des retards</li>
    <li><strong>ExceptionsTest</strong>: vérification des messages et types des exceptions 
    personnalisées (<code>RegexFormatError</code>, <code>SearchStringTooSmall</code>, 
    <code>SearchClassNotInherits</code>)</li>
</ul>

---

<h2>4 - Améliorations</h2>

<h3>4.1 - Amélioration organisationel</h3>
<p>
La gestion du temps a constitué l'un des principaux défis de ce projet. La charge de travail 
n'ayant pas pu être répartie équitablement au sein du binôme, une partie significative du 
développement a dû être réalisée dans un délai très contraint. Une meilleure planification 
en amont, avec des jalons intermédiaires et une répartition claire des tâches dès le début, 
aurait permis d'aborder la fin du projet plus sereinement et de livrer un résultat plus complet.
</p>

<h3>4.2 - Amélioration structurel</h3>
<p>
Si les conventions de nommage et les principes de base ont été respectés en début de projet, 
leur application s'est progressivement dégradée au fil du développement, laissant place 
par endroits à du code moins structuré. La pression temporelle et la taille du projet
plus importante que prévu ont rendu difficile le maintien d'une cohérence globale. 
Les nombreuses ramifications du modèle, relations entre œuvres, copies, emprunts, membres 
et personnel, ont complexifié l'architecture au point de rendre certaines parties difficiles 
à appréhender. Une simplification du processus de création d'une œuvre, tant au niveau 
de l'interface utilisateur que de la logique métier, aurait allégé cette complexité. 
L'utilisation de design patterns supplémentaires, comme le pattern <em>Builder</em> pour 
la construction des objets complexes, aurait pu constituer une piste sérieuse.
</p>

<h3>4.3 - Gestion des erreurs</h3>
<p>
Le projet dispose d'une base solide en matière de gestion des erreurs : des exceptions 
personnalisées (<code>RegexFormatError</code>, <code>SearchStringTooSmall</code>, 
<code>SearchClassNotInherits</code>) et une suite de tests unitaires couvrant l'intégralité 
du modèle logique. Cependant, il n'existe pas de système de journalisation en console 
aucun log n'est émis lors des opérations critiques (création d'emprunt, retour, 
prolongation, erreur de validation). L'intégration d'une librairie comme 
<code>java.util.logging</code> ou <a href="https://www.slf4j.org/">SLF4J</a> aurait 
permis de tracer l'activité du système et de faciliter le débogage.
</p>

<h3>4.4- Amélioration du processus de développement</h3>
<p>
L'utilisation de GitHub a représenté un véritable apprentissage au cours de ce projet. 
Au-delà du versionnage basique, nous avons découvert et mis en pratique des opérations 
plus avancées telles que le merge de branches, le rebase et la création de pull requests. 
Ces pratiques, issues du workflow professionnel standard, n'avaient pas été pleinement 
exploitées en début de projet. Une adoption plus précoce de ces mécanismes avec une 
branche par fonctionnalité et des pull requests systématiques aurait amélioré la 
traçabilité du travail et facilité la collaboration.
</p>
---

<h2>5 - Conclusion</h2>

<p>
Ce projet de Programmation Orientée Objet nous a permis de confronter les concepts 
vus en cours à la réalité d'un développement de taille significative. Si le modèle 
logique constitue le point fort de notre travail, avec une architecture MVC, une 
modélisation UML aboutie et une suite de 124 tests unitaires couvrant l'intégralité 
des classes métier, l'interface graphique et le respect des conventions sur la durée 
ont souffert des contraintes rencontrées en fin de projet.
</p>
<p>
Ce projet a été riche en apprentissages pour nous deux. Gérer un projet de cette ampleur 
nous a appris qu'une bonne organisation dès le départ est indispensable. Nous avons également 
pris conscience que travailler en groupe demande autant de qualités humaines que techniques : 
de l'humilité, de la communication et la capacité à faire face aux imprévus. Sur le plan 
technique, la mise en place de tests unitaires nous a montré à quel point cela sécurise le 
développement, et l'utilisation avancée de Git via le merge, le rebase et les pull requests 
nous a permis de progresser concrètement sur la gestion de version.
</p>