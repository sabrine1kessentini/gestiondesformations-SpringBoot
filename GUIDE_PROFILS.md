# Guide - Configuration des Profils Spring Boot

## Vue d'ensemble

L'application utilise les **profils Spring Boot** pour séparer les configurations de développement et de production. Cela permet d'avoir des configurations optimisées pour chaque environnement.

---

## Profils Disponibles

### 1. Profil `dev` (Développement)

**Fichier** : `src/main/resources/application-dev.properties`

**Caractéristiques :**
- ✅ **H2 en mémoire** : Base de données volatile (données perdues au redémarrage)
- ✅ **H2 Console** : Interface web pour consulter la base (`/h2-console`)
- ✅ **Logging DEBUG** : Logs détaillés pour le débogage
- ✅ **Thymeleaf Cache désactivé** : Modifications visibles immédiatement
- ✅ **SQL visible** : Requêtes SQL affichées dans les logs

**Avantages :**
- Démarrage rapide
- Pas besoin de configurer une base de données externe
- Parfait pour les tests et le développement

**Inconvénients :**
- Données perdues à chaque redémarrage
- Ne convient pas pour la production

---

### 2. Profil `prod` (Production)

**Fichier** : `src/main/resources/application-prod.properties`

**Caractéristiques :**
- ✅ **MySQL ou PostgreSQL** : Base de données persistante
- ✅ **H2 Console désactivée** : Sécurité en production
- ✅ **Logging INFO** : Logs moins verbeux
- ✅ **Thymeleaf Cache activé** : Meilleures performances
- ✅ **SQL masqué** : Requêtes SQL non affichées

**Avantages :**
- Données persistantes
- Performances optimisées
- Sécurité renforcée
- Prêt pour la production

**Inconvénients :**
- Nécessite une base de données externe configurée
- Configuration plus complexe

---

## Activation des Profils

### En Développement (Maven)

```bash
# Activer le profil dev
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### En Production (JAR)

```bash
# Activer le profil prod
java -jar gestion-formation.jar --spring.profiles.active=prod
```

### Avec Variables d'Environnement

```bash
# Windows (CMD)
set SPRING_PROFILES_ACTIVE=prod
java -jar gestion-formation.jar

# Windows (PowerShell)
$env:SPRING_PROFILES_ACTIVE="prod"
java -jar gestion-formation.jar

# Linux/Mac
export SPRING_PROFILES_ACTIVE=prod
java -jar gestion-formation.jar
```

---

## Configuration MySQL (Production)

### 1. Créer la Base de Données

```sql
CREATE DATABASE gestion_formation CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 2. Configurer les Variables d'Environnement

```bash
export DB_USERNAME=root
export DB_PASSWORD=votre_mot_de_passe
```

### 3. Lancer l'Application

```bash
java -jar gestion-formation.jar --spring.profiles.active=prod
```

---

## Configuration PostgreSQL (Production)

### 1. Créer la Base de Données

```sql
CREATE DATABASE gestion_formation;
```

### 2. Modifier `application-prod.properties`

Décommentez les lignes PostgreSQL et commentez les lignes MySQL :

```properties
# Configuration PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/gestion_formation
spring.datasource.username=${DB_USERNAME:postgres}
spring.datasource.password=${DB_PASSWORD:postgres}
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

### 3. Ajouter la Dépendance PostgreSQL dans `pom.xml`

```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>
```

### 4. Configurer et Lancer

```bash
export DB_USERNAME=postgres
export DB_PASSWORD=votre_mot_de_passe
java -jar gestion-formation.jar --spring.profiles.active=prod
```

---

## Comparaison des Profils

| Caractéristique | Profil Dev | Profil Prod |
|----------------|------------|-------------|
| **Base de données** | H2 en mémoire | MySQL/PostgreSQL |
| **Persistance** | ❌ Non (perdue au redémarrage) | ✅ Oui |
| **H2 Console** | ✅ Activée | ❌ Désactivée |
| **Logging** | DEBUG (détaillé) | INFO (moins verbeux) |
| **Thymeleaf Cache** | ❌ Désactivé | ✅ Activé |
| **SQL visible** | ✅ Oui | ❌ Non |
| **Performance** | ⚡ Rapide (mémoire) | 🚀 Optimisée |
| **Sécurité** | ⚠️ Basique | 🔒 Renforcée |

---

## Recommandations

### Pour le Développement
- ✅ Utilisez le profil `dev`
- ✅ Profitez de H2 Console pour inspecter les données
- ✅ Utilisez les logs DEBUG pour déboguer

### Pour la Production
- ✅ Utilisez le profil `prod`
- ✅ Configurez MySQL ou PostgreSQL
- ✅ Utilisez des variables d'environnement pour les secrets
- ✅ Activez le cache Thymeleaf pour les performances
- ✅ Désactivez H2 Console pour la sécurité

---

## Dépannage

### L'application ne démarre pas avec le profil prod

**Vérifiez :**
1. La base de données est créée
2. Les identifiants sont corrects
3. Le serveur de base de données est démarré
4. Les variables d'environnement sont définies

### Les données sont perdues en dev

**C'est normal !** Le profil dev utilise H2 en mémoire. Les données sont perdues à chaque redémarrage. C'est le comportement attendu pour le développement.

### Erreur de connexion à la base de données

**Solutions :**
1. Vérifiez que MySQL/PostgreSQL est démarré
2. Vérifiez les identifiants dans les variables d'environnement
3. Vérifiez que la base de données existe
4. Vérifiez les permissions de l'utilisateur

---

## Exemple Complet

### Développement

```bash
# Terminal 1 : Lancer l'application en dev
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Terminal 2 : Accéder à H2 Console
# Ouvrir http://localhost:8080/h2-console
# JDBC URL: jdbc:h2:mem:gestion_formation
# Username: sa
# Password: (vide)
```

### Production

```bash
# 1. Créer la base de données MySQL
mysql -u root -p
CREATE DATABASE gestion_formation;

# 2. Configurer les variables d'environnement
export DB_USERNAME=root
export DB_PASSWORD=mon_mot_de_passe
export MAIL_USERNAME=mon_email@gmail.com
export MAIL_PASSWORD=mon_mot_de_passe_app

# 3. Lancer l'application
java -jar gestion-formation.jar --spring.profiles.active=prod
```

---

**Note** : Le profil par défaut (sans spécification) utilise la configuration dans `application.properties`. Pour un meilleur contrôle, utilisez toujours explicitement un profil.

