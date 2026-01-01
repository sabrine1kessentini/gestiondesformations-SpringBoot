# Rapport de Solution - Application de Gestion d'un Centre de Formation

**Projet :** Application Web de Gestion de Formation  
**Framework :** Spring Boot 3.2.0  
**Date :** 2025  
**Auteur :** Projet réalisé dans le cadre du cours d'Architectures Logicielles Évoluées - Framework Spring

---

## Table des Matières

1. [Spécification](#1-spécification)
2. [Architecture](#2-architecture)
3. [Conception Détaillée](#3-conception-détaillée)
4. [Captures de Réalisation](#4-captures-de-réalisation)

---

## 1. Spécification

### 1.1 Contexte et Objectifs

L'application de gestion d'un centre de formation est une solution web complète permettant de gérer l'ensemble des activités pédagogiques d'un établissement de formation. Elle répond aux besoins de trois types d'utilisateurs principaux : les administrateurs, les formateurs et les étudiants.

### 1.2 Besoins Fonctionnels

#### 1.2.1 Gestion des Utilisateurs

**Acteurs :** Administrateur

**Fonctionnalités :**
- **Gestion des Étudiants :**
  - Création, modification, suppression d'étudiants
  - Attribution d'un matricule unique
  - Association à un groupe
  - Consultation de la liste des étudiants
  - Calcul automatique de la moyenne générale

- **Gestion des Formateurs :**
  - Création, modification, suppression de formateurs
  - Attribution d'une spécialité
  - Association aux cours

#### 1.2.2 Gestion Pédagogique

**Acteurs :** Administrateur, Formateur

**Fonctionnalités :**
- **Gestion des Cours :**
  - Création de cours avec code unique, titre, description
  - Association à un formateur, une spécialité, une session
  - Association à un ou plusieurs groupes
  - Consultation des statistiques (nombre d'inscrits, taux de réussite)

- **Gestion des Groupes :**
  - Création et gestion des groupes d'étudiants
  - Association des étudiants aux groupes

- **Gestion des Spécialités :**
  - Création et gestion des spécialités (Informatique, Réseaux, IA, etc.)

- **Gestion des Sessions :**
  - Création de sessions (Semestre, Année)
  - Définition des périodes (date début, date fin)

#### 1.2.3 Gestion des Inscriptions

**Acteurs :** Étudiant, Administrateur

**Fonctionnalités :**
- Inscription en ligne aux cours disponibles
- Annulation d'inscription
- Consultation des cours disponibles
- Consultation des cours auxquels l'étudiant est inscrit
- Vérification de non-duplication d'inscription
- Envoi automatique d'emails de notification lors des inscriptions/désinscriptions

#### 1.2.4 Gestion des Notes

**Acteurs :** Formateur, Administrateur

**Fonctionnalités :**
- Attribution de notes aux étudiants pour un cours
- Consultation des notes par l'étudiant
- Calcul automatique de la moyenne générale de l'étudiant
- Calcul automatique du taux de réussite par cours

#### 1.2.5 Gestion du Planning

**Acteurs :** Administrateur, Formateur, Étudiant

**Fonctionnalités :**
- Planification des séances (CM, TD, TP)
- Définition de la date/heure de début et fin
- Attribution d'une salle
- Consultation de l'emploi du temps par étudiant
- Consultation de l'emploi du temps par formateur
- Détection de conflits de planning

#### 1.2.6 Reporting et Statistiques

**Acteurs :** Administrateur, Formateur

**Fonctionnalités :**
- Tableau de bord avec statistiques globales
- Statistiques par cours (nombre d'inscrits, taux de réussite)
- Statistiques par étudiant (moyenne générale, nombre de cours)
- Liste des cours les plus suivis
- Génération de rapports PDF (notes par cours)

### 1.3 Besoins Non-Fonctionnels

#### 1.3.1 Sécurité
- Authentification par nom d'utilisateur et mot de passe
- Autorisation basée sur les rôles (ADMIN, FORMATEUR, ETUDIANT)
- Chiffrement des mots de passe avec BCrypt
- Protection CSRF
- Sessions HTTP sécurisées

#### 1.3.2 Performance
- Support de bases de données relationnelles (H2 pour développement, MySQL pour production)
- Optimisation des requêtes JPA
- Cache Thymeleaf désactivé en développement

#### 1.3.3 Maintenabilité
- Architecture en couches (Controller, Service, Repository)
- Séparation des responsabilités
- Code modulaire et réutilisable
- Documentation du code

#### 1.3.4 Disponibilité
- Support Docker pour déploiement
- Configuration par profils Spring (dev, prod)
- Persistance des données entre les redémarrages

### 1.4 Contraintes Techniques

- **Langage :** Java 17+
- **Framework :** Spring Boot 3.2.0
- **Base de données :** H2 (développement) / MySQL (production)
- **Interface :** Thymeleaf (SSR) + Bootstrap 5
- **API :** REST (JSON)
- **Build :** Maven 3.6+

---

## 2. Architecture

### 2.1 Architecture Globale

L'application suit une architecture en couches (Layered Architecture) avec séparation claire des responsabilités :

```
┌─────────────────────────────────────────────────────────┐
│                    Couche Présentation                   │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  │
│  │  Thymeleaf   │  │  REST API    │  │   Security   │  │
│  │  (SSR)       │  │  (CSR)       │  │   Filter     │  │
│  └──────────────┘  └──────────────┘  └──────────────┘  │
└─────────────────────────────────────────────────────────┘
                        ↓
┌─────────────────────────────────────────────────────────┐
│                    Couche Contrôleur                    │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  │
│  │ AdminController│ │ FormateurCtrl │  │ EtudiantCtrl│  │
│  │ + API REST   │  │ + API REST    │  │ + API REST │  │
│  └──────────────┘  └──────────────┘  └──────────────┘  │
└─────────────────────────────────────────────────────────┘
                        ↓
┌─────────────────────────────────────────────────────────┐
│                    Couche Service                        │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  │
│  │ EtudiantService│ │ CoursService │  │ Inscription │  │
│  │ FormateurSvc  │ │ NoteService  │  │ Service     │  │
│  │ ReportingSvc  │ │ EmailService │  │ SeanceSvc   │  │
│  └──────────────┘  └──────────────┘  └──────────────┘  │
└─────────────────────────────────────────────────────────┘
                        ↓
┌─────────────────────────────────────────────────────────┐
│                    Couche Persistance                    │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  │
│  │ Repository   │  │   JPA/Hibernate│ │   Database   │  │
│  │  Interfaces  │  │   ORM         │  │   (H2/MySQL) │  │
│  └──────────────┘  └──────────────┘  └──────────────┘  │
└─────────────────────────────────────────────────────────┘
```

### 2.2 Patterns Architecturaux

#### 2.2.1 MVC (Model-View-Controller)
- **Model :** Entités JPA (Entity)
- **View :** Templates Thymeleaf
- **Controller :** Contrôleurs Spring MVC

#### 2.2.2 Repository Pattern
- Abstraction de l'accès aux données via Spring Data JPA
- Interfaces Repository qui étendent `JpaRepository`
- Réduction du code boilerplate

#### 2.2.3 Service Layer Pattern
- Logique métier encapsulée dans les services
- Transactions gérées au niveau service
- Réutilisabilité entre contrôleurs SSR et REST

#### 2.2.4 DTO Pattern
- Utilisé pour les transferts de données (ChangePasswordDTO, UpdateProfileDTO)
- Séparation entre entités JPA et données transférées

#### 2.2.5 Inheritance Strategy (JPA)
- Utilisation de l'héritage JOINED pour Utilisateur/Etudiant/Formateur
- Table parente `utilisateurs` et tables enfants `etudiants`, `formateurs`

### 2.3 Technologies Utilisées

#### 2.3.1 Backend
- **Spring Boot 3.2.0** : Framework principal
- **Spring MVC** : Gestion des requêtes HTTP
- **Spring Data JPA** : Accès aux données
- **Spring Security** : Authentification et autorisation
- **Hibernate** : ORM
- **JasperReports** : Génération de rapports PDF

#### 2.3.2 Frontend
- **Thymeleaf** : Moteur de templates (Server-Side Rendering)
- **Bootstrap 5** : Framework CSS
- **Bootstrap Icons** : Icônes

#### 2.3.3 Base de Données
- **H2 Database** : Base de données en mémoire pour le développement
- **MySQL 8.0** : Base de données de production

#### 2.3.4 Outils
- **Maven** : Gestion des dépendances et build
- **Docker** : Containerisation
- **Spring Boot DevTools** : Outils de développement

### 2.4 Structure du Projet

```
gestion-formation/
├── src/
│   ├── main/
│   │   ├── java/com/iit/formation/
│   │   │   ├── config/              # Configuration (Security, DataInitializer)
│   │   │   ├── controller/          # Contrôleurs
│   │   │   │   ├── api/             # Contrôleurs REST
│   │   │   │   ├── AdminController.java
│   │   │   │   ├── FormateurController.java
│   │   │   │   ├── EtudiantController.java
│   │   │   │   └── LoginController.java
│   │   │   ├── dto/                 # Data Transfer Objects
│   │   │   ├── entity/              # Entités JPA
│   │   │   ├── repository/          # Interfaces Repository
│   │   │   ├── service/             # Services métier
│   │   │   └── GestionFormationApplication.java
│   │   └── resources/
│   │       ├── templates/           # Templates Thymeleaf
│   │       │   ├── admin/           # Interface admin
│   │       │   ├── formateur/       # Interface formateur
│   │       │   ├── etudiant/        # Interface étudiant
│   │       │   └── login.html
│   │       ├── static/              # Ressources statiques
│   │       │   └── css/
│   │       ├── reports/             # Templates JasperReports
│   │       └── application.properties
│   └── test/                        # Tests unitaires
├── data/                            # Données H2 (persistantes)
├── docker-compose.yml
├── Dockerfile
└── pom.xml
```

### 2.5 Flux de Données

#### 2.5.1 Flux SSR (Server-Side Rendering)
```
Client (Navigateur)
    ↓ HTTP Request
Controller (Thymeleaf)
    ↓ Appel Service
Service Layer
    ↓ Appel Repository
Repository (JPA)
    ↓ Requête SQL
Base de Données
    ↓ Résultat
Repository → Service → Controller → Thymeleaf Template → HTML → Client
```

#### 2.5.2 Flux REST API
```
Client (SPA/Mobile)
    ↓ HTTP Request (JSON)
REST Controller
    ↓ Appel Service
Service Layer
    ↓ Appel Repository
Repository (JPA)
    ↓ Requête SQL
Base de Données
    ↓ Résultat
Repository → Service → Controller → JSON Response → Client
```

---

## 3. Conception Détaillée

### 3.1 Modèle de Données

#### 3.1.1 Diagramme de Classes (Entités)

**Note :** Insérer ici le diagramme de classes UML des entités

#### 3.1.2 Entités Principales

##### Utilisateur (Classe Abstraite)
```java
@Entity
@Table(name = "utilisateurs")
@Inheritance(strategy = InheritanceType.JOINED)
public class Utilisateur {
    - Long id
    - String username (unique)
    - String password (encrypted)
    - String email (unique)
    - String nom
    - String prenom
    - Set<Role> roles
    - boolean active
}
```

**Relations :**
- Héritage : Etudiant, Formateur héritent de Utilisateur

##### Etudiant
```java
@Entity
@Table(name = "etudiants")
@PrimaryKeyJoinColumn(name = "utilisateur_id")
public class Etudiant extends Utilisateur {
    - String matricule (unique)
    - LocalDate dateInscription
    - Groupe groupe (ManyToOne)
    - List<Inscription> inscriptions (OneToMany)
    - List<Note> notes (OneToMany)
    
    + double getMoyenneGenerale()
}
```

**Relations :**
- ManyToOne avec Groupe
- OneToMany avec Inscription
- OneToMany avec Note

##### Formateur
```java
@Entity
@Table(name = "formateurs")
@PrimaryKeyJoinColumn(name = "utilisateur_id")
public class Formateur extends Utilisateur {
    - String specialite
    
    - List<Cours> cours (OneToMany)
}
```

**Relations :**
- OneToMany avec Cours

##### Cours
```java
@Entity
@Table(name = "cours")
public class Cours {
    - Long id
    - String code (unique)
    - String titre
    - String description
    - Formateur formateur (ManyToOne)
    - Specialite specialite (ManyToOne)
    - Session session (ManyToOne)
    - List<Groupe> groupes (ManyToMany)
    - List<Inscription> inscriptions (OneToMany)
    - List<Note> notes (OneToMany)
    - List<Seance> seances (OneToMany)
    
    + int getNombreInscrits()
    + double getTauxReussite()
}
```

**Relations :**
- ManyToOne avec Formateur
- ManyToOne avec Specialite
- ManyToOne avec Session
- ManyToMany avec Groupe
- OneToMany avec Inscription
- OneToMany avec Note
- OneToMany avec Seance

##### Inscription
```java
@Entity
@Table(name = "inscriptions")
public class Inscription {
    - Long id
    - LocalDateTime dateInscription
    - Etudiant etudiant (ManyToOne)
    - Cours cours (ManyToOne)
    - StatutInscription statut (ACTIVE, ANNULEE)
}
```

**Contraintes :**
- UniqueConstraint sur (etudiant_id, cours_id)

##### Note
```java
@Entity
@Table(name = "notes")
public class Note {
    - Long id
    - double valeur
    - String commentaire
    - Etudiant etudiant (ManyToOne)
    - Cours cours (ManyToOne)
}
```

##### Seance
```java
@Entity
@Table(name = "seances")
public class Seance {
    - Long id
    - LocalDateTime dateHeureDebut
    - LocalDateTime dateHeureFin
    - String salle
    - Cours cours (ManyToOne)
    - String type (CM, TD, TP)
}
```

##### Groupe
```java
@Entity
@Table(name = "groupes")
public class Groupe {
    - Long id
    - String code
    - String description
    - List<Etudiant> etudiants (OneToMany)
    - List<Cours> cours (ManyToMany)
}
```

##### Specialite
```java
@Entity
@Table(name = "specialites")
public class Specialite {
    - Long id
    - String nom
    - String description
    - List<Cours> cours (OneToMany)
}
```

##### Session
```java
@Entity
@Table(name = "sessions")
public class Session {
    - Long id
    - String nom
    - LocalDate dateDebut
    - LocalDate dateFin
    - TypeSession type (SEMESTRE, ANNEE)
    - List<Cours> cours (OneToMany)
}
```

#### 3.1.3 Diagramme de Relations

**Note :** Insérer ici le diagramme ER (Entity-Relationship) de la base de données

**Relations principales :**
- Utilisateur ← Etudiant (héritage JOINED)
- Utilisateur ← Formateur (héritage JOINED)
- Etudiant → Groupe (ManyToOne)
- Etudiant → Inscription (OneToMany)
- Etudiant → Note (OneToMany)
- Formateur → Cours (OneToMany)
- Cours → Formateur (ManyToOne)
- Cours → Specialite (ManyToOne)
- Cours → Session (ManyToOne)
- Cours ↔ Groupe (ManyToMany)
- Cours → Inscription (OneToMany)
- Cours → Note (OneToMany)
- Cours → Seance (OneToMany)

### 3.2 Couche Service

#### 3.2.1 Services Principaux

##### EtudiantService
**Responsabilités :**
- CRUD des étudiants
- Calcul de la moyenne générale
- Recherche par matricule

**Méthodes principales :**
```java
List<Etudiant> getAllEtudiants()
Optional<Etudiant> getEtudiantById(Long id)
Optional<Etudiant> getEtudiantByMatricule(String matricule)
Etudiant createEtudiant(Etudiant etudiant)
Etudiant updateEtudiant(Long id, Etudiant etudiant)
void deleteEtudiant(Long id)
double getMoyenneGenerale(Long etudiantId)
```

##### FormateurService
**Responsabilités :**
- CRUD des formateurs
- Gestion des spécialités

**Méthodes principales :**
```java
List<Formateur> getAllFormateurs()
Optional<Formateur> getFormateurById(Long id)
Formateur createFormateur(Formateur formateur)
Formateur updateFormateur(Long id, Formateur formateur)
void deleteFormateur(Long id)
```

##### CoursService
**Responsabilités :**
- CRUD des cours
- Gestion des associations avec groupes
- Calcul des statistiques

**Méthodes principales :**
```java
List<Cours> getAllCours()
Optional<Cours> getCoursById(Long id)
Cours createCours(Cours cours)
Cours updateCours(Long id, Cours cours)
void deleteCours(Long id)
void ajouterGroupe(Long coursId, Long groupeId)
void retirerGroupe(Long coursId, Long groupeId)
```

##### InscriptionService
**Responsabilités :**
- Gestion des inscriptions
- Vérification de non-duplication
- Envoi d'emails de notification

**Méthodes principales :**
```java
List<Inscription> getAllInscriptions()
Inscription inscrireEtudiant(Long etudiantId, Long coursId)
void annulerInscription(Long inscriptionId)
void annulerInscription(Long etudiantId, Long coursId)
List<Inscription> getInscriptionsByEtudiant(Long etudiantId)
List<Inscription> getInscriptionsByCours(Long coursId)
```

**Logique métier :**
- Vérification de l'existence d'une inscription active
- Réactivation d'une inscription annulée si nécessaire
- Envoi d'email asynchrone (ne bloque pas l'inscription en cas d'erreur)

##### NoteService
**Responsabilités :**
- Attribution de notes
- Consultation des notes

**Méthodes principales :**
```java
List<Note> getAllNotes()
Note attribuerNote(Long etudiantId, Long coursId, double valeur, String commentaire)
List<Note> getNotesByEtudiant(Long etudiantId)
List<Note> getNotesByCours(Long coursId)
```

##### SeanceService
**Responsabilités :**
- Planification des séances
- Détection de conflits
- Gestion des emplois du temps

**Méthodes principales :**
```java
List<Seance> getAllSeances()
Seance planifierSeance(Long coursId, LocalDateTime debut, LocalDateTime fin, String salle, String type)
void updateSeance(Long id, LocalDateTime debut, LocalDateTime fin, String salle, String type)
void deleteSeance(Long id)
List<Seance> getSeancesByEtudiant(Long etudiantId)
List<Seance> getSeancesByFormateur(Long formateurId)
```

##### ReportingService
**Responsabilités :**
- Calcul de statistiques
- Génération de tableaux de bord

**Méthodes principales :**
```java
Map<String, Object> getTableauDeBord()
Map<String, Object> getStatistiquesEtudiant(Long etudiantId)
Map<String, Object> getStatistiquesCours(Long coursId)
List<Cours> getCoursLesPlusSuivis()
```

##### EmailService
**Responsabilités :**
- Envoi d'emails de notification
- Gestion des erreurs d'envoi

**Méthodes principales :**
```java
void envoyerEmailInscription(Etudiant etudiant, Cours cours)
void envoyerEmailDesinscription(Etudiant etudiant, Cours cours, Formateur formateur)
```

**Configuration :**
- Support SMTP (Gmail, Mailtrap)
- Mode simulation si SMTP non configuré (System.out.println)

##### PDFReportService
**Responsabilités :**
- Génération de rapports PDF avec JasperReports

**Méthodes principales :**
```java
byte[] genererRapportNotes(Long coursId)
```

### 3.3 Couche Contrôleur

#### 3.3.1 Contrôleurs Thymeleaf (SSR)

##### AdminController
**Mapping :** `/admin/**`  
**Rôle requis :** ADMIN

**Endpoints principaux :**
- `GET /admin/dashboard` - Tableau de bord
- `GET /admin/etudiants` - Liste des étudiants
- `GET /admin/etudiants/new` - Formulaire création étudiant
- `POST /admin/etudiants` - Création étudiant
- `GET /admin/etudiants/{id}/edit` - Formulaire modification
- `POST /admin/etudiants/{id}` - Modification
- `POST /admin/etudiants/{id}/delete` - Suppression
- (Mêmes patterns pour formateurs, cours, groupes, spécialités, sessions, séances)
- `GET /admin/cours/{id}/statistiques` - Statistiques d'un cours
- `GET /admin/cours/{id}/rapport-pdf` - Génération PDF

##### FormateurController
**Mapping :** `/formateur/**`  
**Rôle requis :** FORMATEUR ou ADMIN

**Endpoints principaux :**
- `GET /formateur/dashboard` - Tableau de bord formateur
- `GET /formateur/cours` - Liste des cours du formateur
- `GET /formateur/cours/{id}/notes` - Gestion des notes

##### EtudiantController
**Mapping :** `/etudiant/**`  
**Rôle requis :** ETUDIANT ou ADMIN

**Endpoints principaux :**
- `GET /etudiant/dashboard` - Tableau de bord étudiant
- `GET /etudiant/cours/disponibles` - Cours disponibles pour inscription
- `GET /etudiant/cours` - Cours auxquels l'étudiant est inscrit
- `GET /etudiant/notes` - Notes de l'étudiant
- `GET /etudiant/emploi-du-temps` - Emploi du temps

##### LoginController
**Mapping :** `/login`, `/logout`  
**Accès :** Public

**Endpoints :**
- `GET /login` - Page de connexion
- `POST /login` - Traitement de la connexion (géré par Spring Security)
- `POST /logout` - Déconnexion

##### ProfilController
**Mapping :** `/profil/**`  
**Rôle requis :** Authentifié

**Endpoints :**
- `GET /profil` - Affichage du profil
- `POST /profil` - Modification du profil
- `POST /profil/changer-mot-de-passe` - Changement de mot de passe

#### 3.3.2 Contrôleurs REST API

**Mapping de base :** `/api/**`  
**Format :** JSON  
**Authentification :** Requise

##### EtudiantRestController
**Mapping :** `/api/etudiants`

**Endpoints :**
- `GET /api/etudiants` - Liste des étudiants
- `GET /api/etudiants/{id}` - Détails d'un étudiant
- `GET /api/etudiants/matricule/{matricule}` - Recherche par matricule
- `POST /api/etudiants` - Création
- `PUT /api/etudiants/{id}` - Modification
- `DELETE /api/etudiants/{id}` - Suppression
- `GET /api/etudiants/{id}/moyenne` - Moyenne générale

##### CoursRestController
**Mapping :** `/api/cours`

**Endpoints :**
- `GET /api/cours` - Liste des cours
- `GET /api/cours/{id}` - Détails d'un cours
- `POST /api/cours` - Création
- `PUT /api/cours/{id}` - Modification
- `DELETE /api/cours/{id}` - Suppression

##### InscriptionRestController
**Mapping :** `/api/inscriptions`

**Endpoints :**
- `GET /api/inscriptions` - Liste des inscriptions
- `GET /api/inscriptions/{id}` - Détails
- `POST /api/inscriptions` - Inscription à un cours
- `DELETE /api/inscriptions/{id}` - Annulation

##### NoteRestController
**Mapping :** `/api/notes`

**Endpoints :**
- `GET /api/notes` - Liste des notes
- `GET /api/notes/{id}` - Détails
- `POST /api/notes` - Attribution d'une note
- `PUT /api/notes/{id}` - Modification
- `DELETE /api/notes/{id}` - Suppression

##### SeanceRestController
**Mapping :** `/api/seances`

**Endpoints :**
- `GET /api/seances` - Liste des séances
- `GET /api/seances/{id}` - Détails
- `POST /api/seances` - Planification
- `PUT /api/seances/{id}` - Modification
- `DELETE /api/seances/{id}` - Suppression

##### ReportingRestController
**Mapping :** `/api/reporting`

**Endpoints :**
- `GET /api/reporting/dashboard` - Tableau de bord
- `GET /api/reporting/etudiant/{id}` - Statistiques étudiant
- `GET /api/reporting/cours/{id}` - Statistiques cours
- `GET /api/reporting/cours-populaires` - Cours les plus suivis

##### ProfilRestController
**Mapping :** `/api/profil`

**Endpoints :**
- `GET /api/profil` - Profil de l'utilisateur connecté
- `PUT /api/profil` - Modification du profil
- `POST /api/profil/changer-mot-de-passe` - Changement de mot de passe

### 3.4 Configuration

#### 3.4.1 SecurityConfig

**Fonctionnalités :**
- Configuration de Spring Security
- Définition des règles d'autorisation par rôle
- Configuration de l'authentification par formulaire
- Gestion de la déconnexion
- Protection CSRF

**Règles d'autorisation :**
```java
- "/", "/login", "/css/**", "/js/**", "/h2-console/**" → permitAll()
- "/profil/**" → authenticated()
- "/admin/**" → hasRole("ADMIN")
- "/formateur/**" → hasAnyRole("ADMIN", "FORMATEUR")
- "/etudiant/**" → hasAnyRole("ADMIN", "ETUDIANT")
- "/api/**" → authenticated()
- Autres → authenticated()
```

**Authentification :**
- UserDetailsService personnalisé basé sur UtilisateurRepository
- PasswordEncoder : BCryptPasswordEncoder
- Page de login : `/login`
- Redirection après login : `/dashboard`

#### 3.4.2 DataInitializer

**Fonctionnalités :**
- Initialisation des données de test au démarrage
- Création d'un compte admin par défaut
- Création de spécialités, groupes, sessions
- Création de formateurs et étudiants de test

**Comptes par défaut :**
- Admin : `admin` / `admin`
- Formateur : `formateur1` / `formateur1`
- Étudiant : `etudiant1` / `etudiant1`

#### 3.4.3 application.properties

**Configuration principale :**
- Port : 8080
- Base de données : H2 (dev) / MySQL (prod)
- JPA : `ddl-auto=update`
- Thymeleaf : cache désactivé en dev
- Mail : Configuration SMTP (Gmail)
- Logging : DEBUG pour développement

### 3.5 Diagrammes de Séquence

#### 3.5.1 Inscription d'un Étudiant à un Cours

**Note :** Insérer ici le diagramme de séquence UML pour l'inscription

**Flux :**
1. Étudiant accède à `/etudiant/cours/disponibles`
2. Étudiant sélectionne un cours et clique sur "S'inscrire"
3. POST `/api/inscriptions` ou via formulaire
4. InscriptionRestController → InscriptionService.inscrireEtudiant()
5. Vérification de l'existence d'une inscription
6. Si inscription annulée → réactivation
7. Si nouvelle inscription → création
8. Sauvegarde en base de données
9. Envoi d'email (asynchrone, non bloquant)
10. Retour de confirmation

#### 3.5.2 Attribution d'une Note

**Note :** Insérer ici le diagramme de séquence UML pour l'attribution de note

**Flux :**
1. Formateur accède à `/formateur/cours/{id}/notes`
2. Formulaire de saisie de note
3. POST avec données (etudiantId, coursId, valeur, commentaire)
4. FormateurController → NoteService.attribuerNote()
5. Vérification de l'existence de l'étudiant et du cours
6. Création ou mise à jour de la note
7. Sauvegarde en base de données
8. Retour de confirmation

#### 3.5.3 Planification d'une Séance

**Flux :**
1. Administrateur accède à `/admin/seances/new`
2. Formulaire de planification
3. POST avec données (coursId, dateHeureDebut, dateHeureFin, salle, type)
4. AdminController → SeanceService.planifierSeance()
5. Vérification des conflits (optionnel)
6. Création de la séance
7. Sauvegarde en base de données
8. Retour de confirmation

### 3.6 Diagramme de Composants

**Note :** Insérer ici le diagramme de composants UML

**Composants principaux :**
- **Web Layer** : Controllers (Admin, Formateur, Etudiant, API)
- **Business Layer** : Services (Etudiant, Cours, Inscription, Note, etc.)
- **Data Layer** : Repositories (JPA)
- **Security** : SecurityConfig, UserDetailsService
- **Presentation** : Thymeleaf Templates, Static Resources
- **External** : Database (H2/MySQL), SMTP Server

---

## 4. Captures de Réalisation

### 4.1 Diagrammes UML

#### 4.1.1 Diagramme de Cas d'Utilisation

**Note :** Insérer ici le diagramme de cas d'utilisation

**Acteurs :**
- Administrateur
- Formateur
- Étudiant

**Cas d'utilisation principaux :**
- Gérer les étudiants
- Gérer les formateurs
- Gérer les cours
- S'inscrire à un cours
- Annuler une inscription
- Attribuer une note
- Consulter les notes
- Planifier une séance
- Consulter l'emploi du temps
- Générer un rapport PDF
- Consulter les statistiques

#### 4.1.2 Diagramme de Classes

**Note :** Insérer ici le diagramme de classes complet avec toutes les entités et leurs relations

#### 4.1.3 Diagramme de Séquence

**Note :** Insérer ici les diagrammes de séquence pour :
- Inscription d'un étudiant
- Attribution d'une note
- Planification d'une séance
- Authentification

#### 4.1.4 Diagramme de Composants

**Note :** Insérer ici le diagramme de composants montrant l'architecture de l'application

### 4.2 Captures d'Écran

#### 4.2.1 Interface d'Administration

**Page de Connexion**
- **Note :** Insérer capture d'écran de la page `/login`

**Tableau de Bord Administrateur**
- **Note :** Insérer capture d'écran de `/admin/dashboard`
- Affiche : nombre total d'étudiants, cours, formateurs, sessions
- Liste des cours les plus suivis

**Gestion des Étudiants**
- **Note :** Insérer capture d'écran de `/admin/etudiants`
- Liste des étudiants avec actions (modifier, supprimer)
- Formulaire de création/modification

**Gestion des Formateurs**
- **Note :** Insérer capture d'écran de `/admin/formateurs`
- Liste des formateurs avec leurs spécialités

**Gestion des Cours**
- **Note :** Insérer capture d'écran de `/admin/cours`
- Liste des cours avec statistiques
- Formulaire de création avec sélection de formateur, spécialité, session, groupes

**Statistiques d'un Cours**
- **Note :** Insérer capture d'écran de `/admin/cours/{id}/statistiques`
- Nombre d'inscrits
- Taux de réussite
- Liste des notes
- Bouton de génération PDF

**Gestion des Séances**
- **Note :** Insérer capture d'écran de `/admin/seances`
- Liste des séances planifiées
- Formulaire de planification avec sélection de cours, date/heure, salle, type

#### 4.2.2 Interface Formateur

**Tableau de Bord Formateur**
- **Note :** Insérer capture d'écran de `/formateur/dashboard`
- Vue d'ensemble des cours du formateur

**Gestion des Notes**
- **Note :** Insérer capture d'écran de `/formateur/cours/{id}/notes`
- Liste des étudiants inscrits
- Formulaire d'attribution de notes

#### 4.2.3 Interface Étudiant

**Tableau de Bord Étudiant**
- **Note :** Insérer capture d'écran de `/etudiant/dashboard`
- Vue d'ensemble des cours et notes

**Cours Disponibles**
- **Note :** Insérer capture d'écran de `/etudiant/cours/disponibles`
- Liste des cours disponibles pour inscription
- Bouton "S'inscrire" pour chaque cours

**Mes Cours**
- **Note :** Insérer capture d'écran de `/etudiant/cours`
- Liste des cours auxquels l'étudiant est inscrit
- Possibilité d'annuler l'inscription

**Mes Notes**
- **Note :** Insérer capture d'écran de `/etudiant/notes`
- Liste des notes par cours
- Moyenne générale affichée

**Emploi du Temps**
- **Note :** Insérer capture d'écran de `/etudiant/emploi-du-temps`
- Calendrier ou liste des séances
- Affichage : date, heure, cours, salle, type

### 4.3 Tests et Validation

#### 4.3.1 Tests Unitaires

**Note :** Insérer ici les résultats des tests unitaires

**Services testés :**
- EmailServiceTest
- InscriptionServiceTest

#### 4.3.2 Tests d'Intégration

**Note :** Insérer ici les résultats des tests d'intégration

**Scénarios testés :**
- Inscription d'un étudiant à un cours
- Attribution d'une note
- Génération de rapport PDF

### 4.4 Déploiement

#### 4.4.1 Configuration Docker

**Note :** Insérer capture d'écran ou description de :
- docker-compose.yml
- Dockerfile
- Déploiement avec Docker Compose

#### 4.4.2 Base de Données

**Note :** Insérer capture d'écran de :
- Structure des tables dans H2 Console
- Structure des tables dans MySQL (si applicable)

### 4.5 Fonctionnalités Avancées

#### 4.5.1 Génération de Rapports PDF

**Note :** Insérer capture d'écran d'un rapport PDF généré avec JasperReports
- Rapport de notes pour un cours
- Format professionnel avec logo, en-tête, tableau des notes

#### 4.5.2 Envoi d'Emails

**Note :** Insérer capture d'écran ou description de :
- Email d'inscription reçu
- Email de désinscription reçu
- Configuration SMTP

### 4.6 Performance et Optimisation

**Note :** Insérer ici les métriques de performance si disponibles :
- Temps de réponse des pages
- Temps de chargement des listes
- Performance des requêtes SQL

---

## Conclusion

Cette application de gestion de formation offre une solution complète et moderne pour la gestion pédagogique d'un centre de formation. Elle combine une interface web conviviale (SSR avec Thymeleaf) et une API REST pour une utilisation flexible.

### Points Forts

1. **Architecture modulaire** : Séparation claire des responsabilités
2. **Sécurité robuste** : Authentification et autorisation par rôles
3. **Double interface** : SSR pour l'administration, API REST pour intégration
4. **Fonctionnalités complètes** : Gestion complète du cycle de vie pédagogique
5. **Extensibilité** : Architecture permettant l'ajout facile de nouvelles fonctionnalités

### Améliorations Futures

1. **Notifications en temps réel** : WebSockets pour les notifications
2. **Interface mobile** : Application mobile native ou PWA
3. **Analytics avancés** : Tableaux de bord plus détaillés avec graphiques
4. **Export de données** : Export Excel, CSV
5. **Gestion de fichiers** : Upload de documents, devoirs
6. **Chat intégré** : Communication entre formateurs et étudiants

---

**Fin du Rapport**


