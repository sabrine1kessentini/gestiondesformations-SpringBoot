# Rapport de Solution - Système de Gestion de Formation

## Table des Matières

1. [Introduction](#introduction)
2. [Spécification](#spécification)
3. [Architecture](#architecture)
4. [Conception Détaillée](#conception-détaillée)
5. [Diagrammes UML](#diagrammes-uml)
6. [Captures de Réalisation](#captures-de-réalisation)
7. [Technologies Utilisées](#technologies-utilisées)
8. [Conclusion](#conclusion)

---

## Introduction

Ce document présente la solution complète d'un système de gestion de formation développé avec Spring Boot. L'application permet la gestion des étudiants, formateurs, cours, inscriptions, notes et planning dans un centre de formation.

### Objectifs du Projet

- Automatiser la gestion administrative d'un centre de formation
- Fournir des interfaces utilisateur modernes pour différents rôles
- Implémenter une architecture hybride SSR/CSR
- Assurer la sécurité et l'authentification des utilisateurs
- Générer des rapports et statistiques

---

## Spécification

### 2.1 Besoins Fonctionnels

#### 2.1.1 Gestion des Utilisateurs

**Rôle : Administrateur**
- Créer, modifier, supprimer des étudiants
- Créer, modifier, supprimer des formateurs
- Gérer les groupes d'étudiants
- Gérer les spécialités
- Gérer les sessions académiques
- Consulter les statistiques globales
- Visualiser les emails simulés (mode développement)

**Rôle : Formateur**
- Consulter ses cours assignés
- Gérer les notes des étudiants inscrits à ses cours
- Consulter les notifications (inscriptions/désinscriptions)
- Consulter son profil et modifier ses informations

**Rôle : Étudiant**
- Consulter les cours disponibles
- S'inscrire à un cours
- Se désinscrire d'un cours
- Consulter ses notes et moyenne générale
- Consulter son emploi du temps
- Consulter son profil et modifier ses informations

#### 2.1.2 Gestion des Cours

- Création de cours avec code unique
- Association d'un formateur à un cours (obligatoire)
- Association de groupes à un cours (plusieurs groupes possibles)
- Association d'une spécialité et d'une session
- Planification de séances (CM, TD, TP)
- Détection de conflits dans le planning

#### 2.1.3 Gestion des Inscriptions

- Inscription d'un étudiant à un cours
- Vérification de l'unicité (un étudiant ne peut s'inscrire qu'une fois à un cours)
- Annulation d'inscription
- Réactivation d'inscription annulée
- Envoi automatique d'emails de confirmation
- Notifications in-app pour les formateurs

#### 2.1.4 Gestion des Notes

- Attribution de notes par le formateur
- Calcul automatique de la moyenne générale
- Calcul du taux de réussite par cours
- Consultation des notes par l'étudiant

#### 2.1.5 Notifications et Emails

- Email automatique à l'étudiant lors de l'inscription
- Email automatique au formateur lors d'une inscription/désinscription
- Notifications in-app pour les formateurs
- Mode simulation (MockMailService) pour le développement
- Mode réel avec configuration SMTP

### 2.2 Besoins Non-Fonctionnels

- **Sécurité** : Authentification et autorisation par rôle (Spring Security)
- **Performance** : Optimisation des requêtes JPA
- **Maintenabilité** : Architecture en couches (MVC)
- **Extensibilité** : API REST pour intégration future
- **Interface utilisateur** : Design moderne et responsive (Bootstrap 5)

---

## Architecture

### 3.1 Configuration par Profils

L'application utilise les **profils Spring Boot** pour gérer différentes configurations selon l'environnement :

- **Profil `dev`** : H2 en mémoire (développement)
- **Profil `prod`** : MySQL ou PostgreSQL (production)

Cette approche permet une séparation claire entre l'environnement de développement et de production, avec des configurations optimisées pour chaque contexte.

### 3.2 Architecture Globale

L'application suit une architecture en couches (Layered Architecture) avec séparation claire des responsabilités :

```
┌─────────────────────────────────────────────────────────┐
│                    Présentation Layer                    │
│  ┌──────────────┐              ┌──────────────┐         │
│  │   SSR (Web)  │              │   CSR (API)  │         │
│  │  Thymeleaf   │              │   REST API   │         │
│  └──────────────┘              └──────────────┘         │
└─────────────────────────────────────────────────────────┘
                        ↓
┌─────────────────────────────────────────────────────────┐
│                    Controllers Layer                     │
│  ┌──────────────┐              ┌──────────────┐         │
│  │ Web Controllers│             │ REST Controllers│      │
│  └──────────────┘              └──────────────┘         │
└─────────────────────────────────────────────────────────┘
                        ↓
┌─────────────────────────────────────────────────────────┐
│                    Services Layer                        │
│  ┌──────────────────────────────────────────────────┐   │
│  │  Business Logic Services                         │   │
│  │  (InscriptionService, EmailService, etc.)        │   │
│  └──────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────┘
                        ↓
┌─────────────────────────────────────────────────────────┐
│                    Data Access Layer                     │
│  ┌──────────────────────────────────────────────────┐   │
│  │  Spring Data JPA Repositories                    │   │
│  └──────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────┘
                        ↓
┌─────────────────────────────────────────────────────────┐
│                    Database Layer                        │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  │
│  │  H2 (Dev)    │  │ MySQL (Prod) │  │PostgreSQL(Prod)│ │
│  │  (mémoire)   │  │              │  │              │  │
│  └──────────────┘  └──────────────┘  └──────────────┘  │
└─────────────────────────────────────────────────────────┘
```

### 3.3 Architecture SSR/CSR

L'application supporte deux modes de rendu :

**SSR (Server-Side Rendering) - Thymeleaf**
- Utilisé pour les interfaces d'administration
- Rendu côté serveur avec templates Thymeleaf
- Navigation complète via liens HTML
- Authentification via sessions

**CSR (Client-Side Rendering) - API REST**
- API REST disponible sous `/api/*`
- Format JSON pour les réponses
- Peut être consommée par une application SPA (Angular, React, Vue.js)
- Authentification via JWT (à implémenter)

### 3.4 Patterns de Conception Utilisés

- **Repository Pattern** : Abstraction de l'accès aux données
- **Service Layer Pattern** : Logique métier séparée des controllers
- **DTO Pattern** : Transfert de données optimisé
- **Strategy Pattern** : EmailService avec MockMailService et JavaMailSender
- **Template Method** : Structure commune pour les controllers

---

## Conception Détaillée

### 4.1 Modèle de Données

#### 4.1.1 Entités Principales

**Utilisateur** (Classe abstraite)
- `id` : Long (PK)
- `username` : String (unique)
- `password` : String (encodé)
- `email` : String (unique)
- `nom` : String
- `prenom` : String
- `roles` : Set<Role> (ADMIN, FORMATEUR, ETUDIANT)
- `active` : boolean

**Etudiant** (Hérite de Utilisateur)
- `matricule` : String (unique)
- `dateInscription` : LocalDate
- `groupe` : Groupe (ManyToOne)
- `inscriptions` : List<Inscription> (OneToMany)
- `notes` : List<Note> (OneToMany)

**Formateur** (Hérite de Utilisateur)
- `specialite` : String
- `cours` : List<Cours> (OneToMany)
- `notifications` : List<Notification> (OneToMany)

**Cours**
- `id` : Long (PK)
- `code` : String (unique)
- `titre` : String
- `description` : String
- `formateur` : Formateur (ManyToOne, obligatoire)
- `specialite` : Specialite (ManyToOne)
- `session` : Session (ManyToOne)
- `groupes` : List<Groupe> (ManyToMany)
- `inscriptions` : List<Inscription> (OneToMany)
- `notes` : List<Note> (OneToMany)
- `seances` : List<Seance> (OneToMany)

**Inscription**
- `id` : Long (PK)
- `dateInscription` : LocalDateTime
- `etudiant` : Etudiant (ManyToOne)
- `cours` : Cours (ManyToOne)
- `statut` : StatutInscription (ACTIVE, ANNULEE)
- Contrainte unique : (etudiant_id, cours_id)

**Note**
- `id` : Long (PK)
- `valeur` : Double
- `commentaire` : String
- `etudiant` : Etudiant (ManyToOne)
- `cours` : Cours (ManyToOne)
- Contrainte unique : (etudiant_id, cours_id)

**Groupe**
- `id` : Long (PK)
- `nom` : String (unique)
- `description` : String
- `etudiants` : List<Etudiant> (OneToMany)
- `cours` : List<Cours> (ManyToMany)

**Seance**
- `id` : Long (PK)
- `dateHeureDebut` : LocalDateTime
- `dateHeureFin` : LocalDateTime
- `salle` : String
- `type` : String (CM, TD, TP)
- `cours` : Cours (ManyToOne)

**Notification**
- `id` : Long (PK)
- `formateur` : Formateur (ManyToOne)
- `type` : TypeNotification (INSCRIPTION, DESINSCRIPTION)
- `message` : String
- `dateCreation` : LocalDateTime
- `estLue` : boolean

#### 4.1.2 Relations Principales

```
Utilisateur (abstrait)
    ├── Etudiant
    │   ├── ManyToOne → Groupe
    │   ├── OneToMany → Inscription
    │   └── OneToMany → Note
    └── Formateur
        ├── OneToMany → Cours
        └── OneToMany → Notification

Cours
    ├── ManyToOne → Formateur (obligatoire)
    ├── ManyToOne → Specialite
    ├── ManyToOne → Session
    ├── ManyToMany → Groupe
    ├── OneToMany → Inscription
    ├── OneToMany → Note
    └── OneToMany → Seance

Inscription
    ├── ManyToOne → Etudiant
    └── ManyToOne → Cours
    (Contrainte unique: etudiant + cours)

Note
    ├── ManyToOne → Etudiant
    └── ManyToOne → Cours
    (Contrainte unique: etudiant + cours)
```

### 4.2 Services Métier

#### 4.2.1 InscriptionService

**Responsabilités :**
- Gérer les inscriptions des étudiants aux cours
- Vérifier l'unicité des inscriptions
- Gérer les statuts (ACTIVE, ANNULEE)
- Déclencher l'envoi d'emails
- Créer les notifications pour les formateurs

**Méthodes principales :**
- `inscrireEtudiant(Long etudiantId, Long coursId)` : Inscrit un étudiant à un cours
- `annulerInscription(Long inscriptionId)` : Annule une inscription
- `getInscriptionsByEtudiant(Long etudiantId)` : Récupère les inscriptions d'un étudiant
- `getInscriptionsByCours(Long coursId)` : Récupère les inscriptions d'un cours

#### 4.2.2 EmailService

**Responsabilités :**
- Envoyer des emails réels (si configuré)
- Déléguer au MockMailService en mode simulation
- Gérer les erreurs d'envoi sans bloquer le processus

**Méthodes principales :**
- `envoyerEmailInscription(Etudiant, Cours)` : Envoie un email d'inscription
- `envoyerEmailDesinscription(Etudiant, Cours, Formateur)` : Envoie un email de désinscription
- `estModeSimulation()` : Vérifie si le mode simulation est activé

#### 4.2.3 MockMailService

**Responsabilités :**
- Simuler l'envoi d'emails en mode développement
- Stocker les emails dans la base de données
- Afficher les emails dans la console
- Permettre la visualisation via l'interface web

#### 4.2.4 NotificationService

**Responsabilités :**
- Créer des notifications pour les formateurs
- Gérer le statut de lecture des notifications
- Compter les notifications non lues

### 4.3 Controllers

#### 4.3.1 Web Controllers (SSR)

- **AdminController** : Gestion administrative (CRUD complet)
- **FormateurController** : Interface formateur (cours, notes, notifications)
- **EtudiantController** : Interface étudiant (inscriptions, notes, emploi du temps)
- **ProfilController** : Gestion du profil utilisateur

#### 4.3.2 REST Controllers (CSR)

- **EtudiantRestController** : API REST pour les étudiants
- **CoursRestController** : API REST pour les cours
- **InscriptionRestController** : API REST pour les inscriptions
- **NoteRestController** : API REST pour les notes
- **NotificationRestController** : API REST pour les notifications

### 4.4 Sécurité

**Spring Security Configuration :**
- Authentification par formulaire
- Rôles : ADMIN, FORMATEUR, ETUDIANT
- Routes protégées par rôle
- Encodage des mots de passe (BCrypt)
- Sessions HTTP

**Routes protégées :**
- `/admin/*` : Accessible uniquement aux ADMIN
- `/formateur/*` : Accessible aux FORMATEUR et ADMIN
- `/etudiant/*` : Accessible aux ETUDIANT et ADMIN
- `/api/*` : Accessible selon le rôle

---

## Diagrammes UML

### 5.1 Diagramme de Cas d'Utilisation

#### Acteurs
- **Administrateur** : Gère l'ensemble du système
- **Formateur** : Gère ses cours et notes
- **Étudiant** : Consulte et s'inscrit aux cours

#### Cas d'utilisation par acteur

**Administrateur :**
- Gérer les étudiants (créer, modifier, supprimer, lister)
- Gérer les formateurs (créer, modifier, supprimer, lister)
- Gérer les cours (créer, modifier, supprimer, lister)
- Gérer les groupes (créer, modifier, supprimer, lister)
- Gérer les spécialités (créer, modifier, supprimer, lister)
- Gérer les sessions (créer, modifier, supprimer, lister)
- Gérer les séances (créer, modifier, supprimer, lister)
- Consulter les statistiques
- Générer des rapports PDF
- Visualiser les emails simulés

**Formateur :**
- Consulter ses cours
- Gérer les notes (attribuer, modifier)
- Consulter les notifications
- Marquer les notifications comme lues
- Consulter son profil
- Modifier son profil

**Étudiant :**
- Consulter les cours disponibles
- S'inscrire à un cours
- Se désinscrire d'un cours
- Consulter ses cours inscrits
- Consulter ses notes
- Consulter son emploi du temps
- Consulter son profil
- Modifier son profil
- Changer son mot de passe

#### Diagramme textuel (à créer avec un outil UML)

```
┌─────────────────────────────────────────────────────────┐
│                    Système de Gestion                   │
│                      de Formation                        │
└─────────────────────────────────────────────────────────┘
         │              │              │
         │              │              │
    ┌────▼────┐   ┌────▼────┐   ┌────▼────┐
    │ Admin   │   │Formateur│   │ Étudiant│
    └────┬────┘   └────┬────┘   └────┬────┘
         │             │              │
         │             │              │
    [Gérer étudiants]  │              │
    [Gérer formateurs] │              │
    [Gérer cours]      │              │
    [Statistiques]     │              │
                       │              │
                  [Gérer notes]         │
                  [Notifications]    │
                                      │
                              [S'inscrire]
                              [Consulter notes]
                              [Emploi du temps]
```

### 5.2 Diagramme de Classes

#### Description des relations principales

**Héritage :**
- `Etudiant extends Utilisateur`
- `Formateur extends Utilisateur`

**Associations :**
- `Cours` ↔ `Formateur` : ManyToOne (un cours a un formateur, un formateur a plusieurs cours)
- `Cours` ↔ `Groupe` : ManyToMany (un cours peut être suivi par plusieurs groupes, un groupe peut suivre plusieurs cours)
- `Etudiant` ↔ `Groupe` : ManyToOne (un étudiant appartient à un groupe, un groupe contient plusieurs étudiants)
- `Etudiant` ↔ `Cours` (via Inscription) : ManyToMany (un étudiant peut s'inscrire à plusieurs cours, un cours peut avoir plusieurs étudiants)
- `Inscription` : Association entre `Etudiant` et `Cours` avec attributs (date, statut)
- `Note` : Association entre `Etudiant` et `Cours` avec attributs (valeur, commentaire)
- `Seance` ↔ `Cours` : ManyToOne (une séance appartient à un cours, un cours a plusieurs séances)
- `Notification` ↔ `Formateur` : ManyToOne (une notification est pour un formateur, un formateur a plusieurs notifications)

#### Diagramme textuel (à créer avec un outil UML)

```
┌─────────────────────┐
│    Utilisateur      │
│  (abstrait)         │
├─────────────────────┤
│ +id: Long           │
│ +username: String   │
│ +password: String   │
│ +email: String      │
│ +nom: String        │
│ +prenom: String      │
│ +roles: Set<Role>   │
└──────────┬──────────┘
           │
     ┌─────┴─────┐
     │           │
┌────▼────┐ ┌───▼──────┐
│ Étudiant│ │Formateur │
├─────────┤ ├──────────┤
│+matricule│ │+specialite│
└────┬────┘ └────┬─────┘
     │           │
     │           │
     │      ┌────▼────┐
     │      │  Cours  │
     │      ├─────────┤
     │      │+code    │
     │      │+titre   │
     │      └────┬────┘
     │           │
     │      ┌────▼────┐
     │      │Inscription│
     │      ├─────────┤
     │      │+date    │
     │      │+statut  │
     │      └─────────┘
     │
┌────▼────┐
│  Groupe │
├─────────┤
│+nom     │
└─────────┘
```

### 5.3 Diagramme de Séquence - "Inscrire un étudiant à un cours"

#### Scénario principal

1. **Étudiant** : Accède à la page "Cours Disponibles"
2. **Étudiant** : Clique sur "S'inscrire" pour un cours
3. **EtudiantController** : Reçoit la requête POST `/etudiant/inscrire/{coursId}`
4. **EtudiantController** : Récupère l'étudiant authentifié
5. **EtudiantController** : Appelle `InscriptionService.inscrireEtudiant()`
6. **InscriptionService** : Vérifie si une inscription existe déjà
7. **InscriptionService** : Récupère l'étudiant et le cours depuis les repositories
8. **InscriptionService** : Crée une nouvelle inscription
9. **InscriptionRepository** : Sauvegarde l'inscription
10. **InscriptionService** : Appelle `EmailService.envoyerEmailInscription()`
11. **EmailService** : Vérifie le mode (simulation ou réel)
12. **EmailService** : Si simulation → `MockMailService.envoyerEmailInscription()`
13. **MockMailService** : Sauvegarde l'email dans la base de données
14. **EmailService** : Si réel → Envoie l'email via `JavaMailSender`
15. **InscriptionService** : Appelle `NotificationService.creerNotificationInscription()`
16. **NotificationService** : Crée une notification pour le formateur
17. **NotificationRepository** : Sauvegarde la notification
18. **EtudiantController** : Redirige vers "Cours Disponibles" avec message de succès

#### Diagramme textuel (à créer avec un outil UML)

```
Étudiant → EtudiantController → InscriptionService → InscriptionRepository
                                      │
                                      ├→ EmailService → MockMailService → MockEmailRepository
                                      │                    │
                                      │                    └→ Console (affichage)
                                      │
                                      └→ NotificationService → NotificationRepository
```

### 5.4 Diagramme de Composants - Architecture SSR/CSR

#### Composants principaux

**Couche Présentation :**
- **Thymeleaf Templates** : Templates HTML avec Thymeleaf
- **Bootstrap CSS/JS** : Framework CSS pour le style
- **REST Client** : Application cliente (Angular/React/Vue.js) - optionnel

**Couche Contrôleurs :**
- **Web Controllers** : Controllers Thymeleaf (AdminController, EtudiantController, etc.)
- **REST Controllers** : Controllers API REST (EtudiantRestController, etc.)

**Couche Services :**
- **Business Services** : Services métier (InscriptionService, EmailService, etc.)
- **Security Service** : Service de sécurité (Spring Security)

**Couche Données :**
- **Repositories** : Spring Data JPA Repositories
- **Entities** : Entités JPA

**Couche Infrastructure :**
- **Database** : H2 (dev) / MySQL (prod)
- **Email Service** : JavaMailSender / MockMailService

#### Diagramme textuel (à créer avec un outil UML)

```
┌─────────────────────────────────────────────────────────┐
│              Couche Présentation                        │
│  ┌──────────────┐              ┌──────────────┐         │
│  │  Thymeleaf   │              │ REST Client  │         │
│  │  Templates   │              │  (SPA)       │         │
│  └──────┬───────┘              └──────┬───────┘         │
└─────────┼──────────────────────────────┼────────────────┘
          │                              │
┌─────────▼──────────────────────────────▼────────────────┐
│              Couche Contrôleurs                         │
│  ┌──────────────┐              ┌──────────────┐         │
│  │ Web Controllers│            │ REST Controllers│       │
│  └──────┬───────┘              └──────┬───────┘         │
└─────────┼──────────────────────────────┼────────────────┘
          │                              │
┌─────────▼──────────────────────────────▼────────────────┐
│              Couche Services                            │
│  ┌──────────────────────────────────────────────────┐   │
│  │  InscriptionService, EmailService, etc.          │   │
│  └──────────────────────────────────────────────────┘   │
└─────────────────────────┬───────────────────────────────┘
                          │
┌─────────────────────────▼───────────────────────────────┐
│              Couche Données                             │
│  ┌──────────────┐              ┌──────────────┐         │
│  │ Repositories │              │   Entities    │         │
│  └──────┬───────┘              └──────┬───────┘         │
└─────────┼──────────────────────────────┼────────────────┘
          │                              │
┌─────────▼──────────────────────────────▼────────────────┐
│              Infrastructure                              │
│  ┌──────────────┐              ┌──────────────┐         │
│  │  Database    │              │ Email Service│         │
│  └──────────────┘              └──────────────┘         │
└─────────────────────────────────────────────────────────┘
```

---

## Captures de Réalisation

### 6.1 Interfaces Utilisateur

#### 6.1.1 Page de Connexion
- **URL** : `/login`
- **Description** : Formulaire de connexion avec champs username et password
- **Design** : Interface moderne avec gradient, icônes Bootstrap

#### 6.1.2 Tableau de Bord Administrateur
- **URL** : `/admin/dashboard`
- **Description** : Vue d'ensemble avec statistiques (total étudiants, formateurs, cours, taux de réussite)
- **Design** : Cards avec gradients, icônes, graphiques

#### 6.1.3 Gestion des Étudiants
- **URL** : `/admin/etudiants`
- **Liste** : Tableau moderne avec actions (modifier, supprimer)
- **Formulaire** : Card avec header coloré, champs organisés en colonnes, icônes

#### 6.1.4 Interface Étudiant - Cours Disponibles
- **URL** : `/etudiant/cours/disponibles`
- **Description** : Liste des cours avec bouton "S'inscrire"
- **Fonctionnalité** : Inscription en un clic avec confirmation

#### 6.1.5 Interface Formateur - Notifications
- **URL** : `/formateur/notifications`
- **Description** : Liste des notifications avec badge de compteur
- **Fonctionnalité** : Marquer comme lu, marquer toutes comme lues

#### 6.1.6 Emails Simulés (Admin)
- **URL** : `/admin/emails`
- **Description** : Liste de tous les emails simulés avec détails
- **Fonctionnalité** : Visualisation du contenu des emails

### 6.2 Fonctionnalités Clés

#### 6.2.1 Inscription avec Email Automatique
- **Processus** : 
  1. Étudiant clique sur "S'inscrire"
  2. Inscription créée dans la base
  3. Email envoyé à l'étudiant (simulé ou réel)
  4. Notification créée pour le formateur
  5. Message de confirmation affiché

#### 6.2.2 Gestion des Notes
- **Processus** :
  1. Formateur accède à "Gérer les notes" d'un cours
  2. Liste des étudiants inscrits affichée
  3. Formulaire pour attribuer/modifier les notes
  4. Calcul automatique de la moyenne

#### 6.2.3 Emploi du Temps
- **Processus** :
  1. Étudiant consulte son emploi du temps
  2. Affichage des séances de ses cours inscrits
  3. Informations : date, heure, salle, type (CM/TD/TP)

### 6.3 Points Techniques Remarquables

- **Architecture hybride SSR/CSR** : Support des deux modes de rendu
- **Sécurité multi-rôles** : Gestion fine des autorisations
- **Emails automatiques** : Système flexible avec mode simulation
- **Notifications in-app** : Système de notifications pour les formateurs
- **Interface moderne** : Design cohérent avec Bootstrap 5 et icônes

---

## Technologies Utilisées

### 7.1 Backend
- **Spring Boot 3.2.0** : Framework principal
- **Spring Data JPA** : Accès aux données
- **Spring Security** : Authentification et autorisation
- **Hibernate** : ORM
- **Maven** : Gestion des dépendances

### 7.2 Frontend
- **Thymeleaf** : Templates serveur
- **Bootstrap 5** : Framework CSS
- **Bootstrap Icons** : Bibliothèque d'icônes
- **JavaScript** : Interactivité

### 7.3 Base de Données et Profils

L'application utilise les **profils Spring Boot** pour gérer différentes configurations :

**Profil Dev (`application-dev.properties`)**
- **H2 Database en mémoire** : `jdbc:h2:mem:gestion_formation`
- Données volatiles (perdues au redémarrage)
- H2 Console activée pour le développement
- Logging DEBUG
- Thymeleaf cache désactivé

**Profil Prod (`application-prod.properties`)**
- **MySQL** : Base de données relationnelle (par défaut)
- **PostgreSQL** : Alternative configurable
- Données persistantes
- H2 Console désactivée
- Logging INFO
- Thymeleaf cache activé

**Activation :**
```bash
# Développement
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Production
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

### 7.4 Outils
- **Docker** : Containerisation (optionnel)
- **Git** : Contrôle de version

---

## Conclusion

### 8.1 Objectifs Atteints

✅ **Gestion complète** : Tous les modules demandés sont implémentés
✅ **Architecture hybride** : Support SSR et CSR
✅ **Sécurité** : Authentification et autorisation par rôle
✅ **Interface moderne** : Design cohérent et responsive
✅ **Notifications** : Système d'emails et notifications in-app
✅ **Extensibilité** : API REST pour intégrations futures

### 8.2 Points Forts

- Architecture en couches claire et maintenable
- Code bien structuré et documenté
- Interface utilisateur moderne et intuitive
- Système de notifications complet
- Gestion d'erreurs robuste

### 8.3 Améliorations Possibles

- Implémentation de JWT pour l'API REST
- Ajout de tests unitaires et d'intégration
- Optimisation des performances (cache)
- Ajout de fonctionnalités de recherche avancée
- Export des données en différents formats

---

## Annexes

### A. Structure du Projet

```
gestion-formation/
├── src/
│   ├── main/
│   │   ├── java/com/iit/formation/
│   │   │   ├── entity/          # Entités JPA
│   │   │   ├── repository/      # Repositories
│   │   │   ├── service/         # Services métier
│   │   │   ├── controller/     # Controllers
│   │   │   │   ├── api/        # REST Controllers
│   │   │   │   └── ...         # Web Controllers
│   │   │   ├── dto/            # Data Transfer Objects
│   │   │   └── config/         # Configuration
│   │   └── resources/
│   │       ├── templates/       # Templates Thymeleaf
│   │       ├── static/css/      # Fichiers CSS
│   │       └── application.properties
│   └── test/
└── pom.xml
```

### B. Configuration des Profils Spring Boot

#### B.1 Profil Dev (Développement)

**Fichier** : `src/main/resources/application-dev.properties`

**Configuration :**
- Base de données : H2 en mémoire (`jdbc:h2:mem:gestion_formation`)
- Persistance : Données perdues au redémarrage
- H2 Console : Activée (`/h2-console`)
- Logging : Mode DEBUG
- Thymeleaf Cache : Désactivé

**Activation :**
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

#### B.2 Profil Prod (Production)

**Fichier** : `src/main/resources/application-prod.properties`

**Configuration MySQL (par défaut) :**
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/gestion_formation
spring.datasource.username=${DB_USERNAME:root}
spring.datasource.password=${DB_PASSWORD:root}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

**Configuration PostgreSQL (alternative) :**
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/gestion_formation
spring.datasource.username=${DB_USERNAME:postgres}
spring.datasource.password=${DB_PASSWORD:postgres}
spring.datasource.driver-class-name=org.postgresql.Driver
```

**Activation :**
```bash
# Avec variables d'environnement
export DB_USERNAME=myuser
export DB_PASSWORD=mypassword
java -jar app.jar --spring.profiles.active=prod
```

**Variables d'environnement :**
- `DB_USERNAME` : Nom d'utilisateur de la base de données
- `DB_PASSWORD` : Mot de passe de la base de données
- `MAIL_USERNAME` : Email pour l'envoi d'emails (optionnel)
- `MAIL_PASSWORD` : Mot de passe d'application email (optionnel)

### C. Endpoints API REST

- `GET /api/etudiants` : Liste des étudiants
- `POST /api/etudiants` : Créer un étudiant
- `GET /api/cours` : Liste des cours
- `POST /api/inscriptions/etudiant/{id}/cours/{id}` : Inscrire un étudiant
- `GET /api/notes/etudiant/{id}` : Notes d'un étudiant
- `GET /api/formateur/notifications` : Notifications d'un formateur

### D. Scripts PlantUML (pour générer les diagrammes)

Les diagrammes peuvent être générés avec PlantUML. Des fichiers `.puml` peuvent être créés pour chaque diagramme.

---

**Document préparé pour** : Rapport de projet - Système de Gestion de Formation
**Date** : 2024
**Version** : 1.0

