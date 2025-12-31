# Guide de Test des Fonctionnalités d'Email

Ce guide explique comment tester les fonctionnalités d'envoi d'emails automatiques lors des inscriptions et désinscriptions.

## 📋 Fonctionnalités à Tester

1. **Email automatique à l'étudiant lors d'une inscription**
2. **Notification du formateur lors d'une inscription**
3. **Email à l'étudiant lors d'une désinscription**
4. **Notification du formateur lors d'une désinscription**

---

## 🧪 Méthode 1 : Tests Automatisés (Recommandé)

### Exécuter les tests unitaires

```bash
# Exécuter tous les tests
mvn test

# Exécuter uniquement les tests d'email
mvn test -Dtest=EmailServiceTest
mvn test -Dtest=InscriptionServiceTest

# Exécuter avec affichage détaillé
mvn test -Dtest=EmailServiceTest -X
```

### Tests disponibles

- **EmailServiceTest** : Tests unitaires du service d'email
  - Vérifie l'envoi d'email à l'étudiant lors de l'inscription
  - Vérifie l'envoi d'email au formateur lors de l'inscription
  - Vérifie l'envoi d'email lors de la désinscription
  - Vérifie le comportement sans serveur mail (mode simulation)

- **InscriptionServiceTest** : Tests d'intégration du service d'inscription
  - Vérifie que les emails sont envoyés lors des inscriptions
  - Vérifie que les emails sont envoyés lors des désinscriptions
  - Vérifie la gestion des erreurs d'envoi d'email

---

## 🖥️ Méthode 2 : Tests Manuels via l'Interface Web

### Prérequis

1. Démarrer l'application :
```bash
mvn spring-boot:run
```

2. Accéder à l'application : http://localhost:8080

### Test 1 : Inscription d'un étudiant (Email automatique)

1. **Se connecter en tant qu'étudiant** (ou créer un compte étudiant)
2. **Aller dans "Mes Cours" → "Cours Disponibles"**
3. **Sélectionner un cours et cliquer sur "S'inscrire"**
4. **Vérifier les logs de la console** :
   - Si `JavaMailSender` n'est pas configuré, vous verrez :
     ```
     Email simulé - Inscription: [email] inscrit au cours [titre]
     Notification formateur: [email] - Nouvel étudiant inscrit: [nom]
     ```
   - Si `JavaMailSender` est configuré, l'email sera réellement envoyé

### Test 2 : Désinscription d'un étudiant

1. **Se connecter en tant qu'étudiant**
2. **Aller dans "Mes Cours"**
3. **Sélectionner un cours et cliquer sur "Se désinscrire"**
4. **Vérifier les logs de la console** :
   ```
   Email simulé - Désinscription: [email] désinscrit du cours [titre]
   Notification formateur: [email] - Étudiant désinscrit: [nom]
   ```

### Test 3 : Via l'API REST

#### Inscription via API

```bash
# Inscrire un étudiant à un cours
curl -X POST http://localhost:8080/api/inscriptions/etudiant/1/cours/1

# Vérifier les logs de la console pour les emails simulés
```

#### Désinscription via API

```bash
# Annuler une inscription
curl -X DELETE http://localhost:8080/api/inscriptions/1

# Ou avec l'ID de l'étudiant et du cours
curl -X DELETE http://localhost:8080/api/inscriptions/etudiant/1/cours/1
```

---

## 📧 Méthode 3 : Tests avec un Serveur Mail Réel

### Configuration Gmail (Exemple)

1. **Modifier `application.properties`** :
```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=votre-email@gmail.com
spring.mail.password=votre-mot-de-passe-app
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

2. **Créer un mot de passe d'application Gmail** :
   - Aller dans votre compte Google → Sécurité
   - Activer la validation en 2 étapes
   - Créer un "Mot de passe d'application"
   - Utiliser ce mot de passe dans la configuration

3. **Ou utiliser des variables d'environnement** :
```bash
# Windows
set MAIL_USERNAME=votre-email@gmail.com
set MAIL_PASSWORD=votre-mot-de-passe-app

# Linux/Mac
export MAIL_USERNAME=votre-email@gmail.com
export MAIL_PASSWORD=votre-mot-de-passe-app
```

### Configuration avec Mailtrap (Recommandé pour les tests)

1. **Créer un compte gratuit sur [Mailtrap.io](https://mailtrap.io/)**
2. **Récupérer les identifiants SMTP**
3. **Modifier `application.properties`** :
```properties
spring.mail.host=smtp.mailtrap.io
spring.mail.port=2525
spring.mail.username=votre-username-mailtrap
spring.mail.password=votre-password-mailtrap
spring.mail.properties.mail.smtp.auth=true
```

### Test avec un serveur mail réel

1. **Configurer les identifiants mail** (voir ci-dessus)
2. **Démarrer l'application**
3. **Effectuer une inscription/désinscription**
4. **Vérifier votre boîte mail** (ou Mailtrap) pour recevoir les emails

---

## 🔍 Vérification des Logs

### Mode Simulation (sans serveur mail)

Lorsque `JavaMailSender` n'est pas configuré, les emails sont simulés. Vous verrez dans les logs :

```
Email simulé - Inscription: sophie.martin@etudiant.com inscrit au cours Programmation Java
Notification formateur: jean.dupont@formation.com - Nouvel étudiant inscrit: Sophie Martin
```

### Mode Réel (avec serveur mail)

Si un serveur mail est configuré, vous verrez :
- Les emails envoyés dans votre boîte mail
- Pas de messages de simulation dans les logs
- Les erreurs éventuelles dans les logs si l'envoi échoue

---

## ✅ Checklist de Test

### Tests Automatisés
- [ ] Exécuter `EmailServiceTest` - Tous les tests passent
- [ ] Exécuter `InscriptionServiceTest` - Tous les tests passent
- [ ] Vérifier la couverture de code (optionnel)

### Tests Manuels
- [ ] Inscrire un étudiant via l'interface web → Vérifier les logs
- [ ] Désinscrire un étudiant via l'interface web → Vérifier les logs
- [ ] Inscrire via l'API REST → Vérifier les logs
- [ ] Désinscrire via l'API REST → Vérifier les logs

### Tests avec Serveur Mail Réel
- [ ] Configurer un serveur mail (Gmail ou Mailtrap)
- [ ] Inscrire un étudiant → Vérifier la réception de l'email
- [ ] Vérifier que le formateur reçoit la notification
- [ ] Désinscrire un étudiant → Vérifier la réception de l'email
- [ ] Vérifier que le formateur reçoit la notification de désinscription

---

## 🐛 Dépannage

### Les emails ne sont pas envoyés

1. **Vérifier la configuration** dans `application.properties`
2. **Vérifier les logs** pour les erreurs
3. **Vérifier les variables d'environnement** si utilisées
4. **Tester avec Mailtrap** (plus simple pour les tests)

### Erreur "Authentication failed"

- Vérifier les identifiants
- Pour Gmail, utiliser un mot de passe d'application
- Vérifier que la validation en 2 étapes est activée (Gmail)

### Les tests échouent

- Vérifier que toutes les dépendances sont installées : `mvn clean install`
- Vérifier que Java 17+ est utilisé
- Vérifier les logs d'erreur détaillés

---

## 📝 Notes Importantes

1. **Mode Simulation** : Par défaut, si `JavaMailSender` n'est pas configuré, les emails sont simulés via `System.out.println()`. C'est utile pour le développement sans serveur mail.

2. **Gestion des Erreurs** : Les erreurs d'envoi d'email ne font pas échouer l'inscription/désinscription. Elles sont loggées mais n'interrompent pas le processus.

3. **Tests en Production** : Pour tester en production, utilisez un service comme Mailtrap ou configurez un serveur SMTP de test.

---

## 🔗 Ressources

- [Spring Boot Mail Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/io.html#io.email)
- [Mailtrap - Service de test d'email](https://mailtrap.io/)
- [Gmail - Créer un mot de passe d'application](https://support.google.com/accounts/answer/185833)

