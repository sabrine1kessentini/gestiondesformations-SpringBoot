# Application de Gestion d'un Centre de Formation

Application web Spring Boot pour la gestion d'un centre de formation avec support SSR (Thymeleaf) et CSR (API REST).

## Fonctionnalités

### Modules principaux
- **Gestion des étudiants** : CRUD complet, consultation des notes et cours
- **Gestion des formateurs** : CRUD, gestion des cours et notes
- **Gestion des cours** : Création, planification, association aux groupes
- **Inscriptions** : Inscription en ligne, annulation
- **Notes** : Attribution et consultation des notes
- **Planning** : Gestion des séances, emploi du temps, détection de conflits
- **Reporting** : Statistiques, taux de réussite, cours les plus suivis
- **Notifications** : Emails automatiques lors des inscriptions/désinscriptions

### Sécurité
- Authentification via Spring Security
- Rôles : ADMIN, FORMATEUR, ETUDIANT
- Autorisations par rôle

### Architecture
- **SSR** : Interface d'administration en Thymeleaf + Bootstrap
- **CSR** : API REST pour client SPA (Angular/React)
- **Base de données** : MySQL (production) / H2 (développement)

## Prérequis

- Java 17+
- Maven 3.6+
- MySQL 8.0+ (pour la production)
- Docker et Docker Compose (optionnel)

## Installation

### Développement local

1. Cloner le projet
2. Configurer la base de données dans `application.properties`
3. Lancer l'application :
```bash
mvn spring-boot:run
```

### Avec Docker Compose

```bash
docker-compose up -d
```

L'application sera accessible sur http://localhost:8080

## Configuration

### Profils Spring

- **dev** : Utilise H2 en mémoire (pour le développement)
- **prod** : Utilise MySQL (pour la production)

Activer un profil :
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Base de données

Pour MySQL, créer la base de données :
```sql
CREATE DATABASE gestion_formation;
```

## API REST

Les endpoints REST sont disponibles sous `/api/*` :

- `/api/etudiants` - Gestion des étudiants
- `/api/cours` - Gestion des cours
- `/api/inscriptions` - Gestion des inscriptions
- `/api/notes` - Gestion des notes
- `/api/seances` - Gestion des séances
- `/api/reporting` - Statistiques et rapports

## Interface Web

- `/login` - Page de connexion
- `/admin/*` - Interface d'administration (ADMIN)
- `/formateur/*` - Interface formateur
- `/etudiant/*` - Interface étudiant

## Structure du projet

```
src/
├── main/
│   ├── java/com/iit/formation/
│   │   ├── entity/          # Entités JPA
│   │   ├── repository/      # Repositories Spring Data
│   │   ├── service/         # Services métier
│   │   ├── controller/      # Controllers (REST + Thymeleaf)
│   │   └── config/          # Configuration (Security, etc.)
│   └── resources/
│       ├── templates/       # Templates Thymeleaf
│       └── application.properties
└── test/
```

## Technologies utilisées

- Spring Boot 3.2.0
- Spring Data JPA
- Spring Security
- Thymeleaf
- Bootstrap 5
- MySQL / H2
- Maven
- Docker

## Auteur

Projet réalisé dans le cadre du cours d'Architectures Logicielles Évoluées - Framework Spring




