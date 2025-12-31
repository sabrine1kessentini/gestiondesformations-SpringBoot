# 📧 Configuration pour l'envoi réel d'emails

## 🎯 Objectif

Configurer le système pour envoyer de **vrais emails** aux étudiants lors de leur inscription à un cours. L'email sera envoyé à l'adresse email stockée dans les **informations personnelles** de l'étudiant.

---

## 📋 Prérequis

1. **Un compte Gmail** (ou autre service email SMTP)
2. **Validation en 2 étapes activée** sur votre compte Gmail
3. **Un mot de passe d'application Gmail** (voir étape 1)

---

## 🔧 Étapes de configuration

### Étape 1 : Créer un mot de passe d'application Gmail

1. Allez sur [Google Account](https://myaccount.google.com/)
2. Cliquez sur **Sécurité** (Security) dans le menu de gauche
3. Activez la **Validation en 2 étapes** si ce n'est pas déjà fait
4. Faites défiler jusqu'à **Mots de passe des applications** (App passwords)
5. Cliquez sur **Sélectionner une application** → Choisissez **Autre (nom personnalisé)**
6. Entrez un nom (ex: "Gestion Formation")
7. Cliquez sur **Générer**
8. **Copiez le mot de passe** (16 caractères, ex: `abcd efgh ijkl mnop`)
   - ⚠️ **Important** : Vous ne pourrez plus voir ce mot de passe après !

---

### Étape 2 : Configurer les variables d'environnement

#### Option A : Script automatique (Recommandé)

1. Ouvrez le fichier `configurer-email.bat` (à créer)
2. Modifiez les valeurs :
   ```cmd
   set MAIL_USERNAME=votre-email@gmail.com
   set MAIL_PASSWORD=votre-mot-de-passe-app
   ```
3. Double-cliquez sur `configurer-email.bat`
4. L'application se lancera automatiquement avec la configuration

#### Option B : Configuration manuelle

**Windows (CMD) :**
```cmd
set MAIL_USERNAME=votre-email@gmail.com
set MAIL_PASSWORD=votre-mot-de-passe-app
mvn spring-boot:run
```

**Windows (PowerShell) :**
```powershell
$env:MAIL_USERNAME="votre-email@gmail.com"
$env:MAIL_PASSWORD="votre-mot-de-passe-app"
mvn spring-boot:run
```

**Linux/Mac :**
```bash
export MAIL_USERNAME=votre-email@gmail.com
export MAIL_PASSWORD=votre-mot-de-passe-app
mvn spring-boot:run
```

---

### Étape 3 : Vérifier la configuration

Après avoir démarré l'application, regardez les logs. Vous devriez voir :

**✅ Configuration réussie :**
```
INFO  EmailService - Tentative d'envoi d'email d'inscription
INFO  EmailService - ✅ Email d'inscription envoyé avec succès à: etudiant@example.com
```

**❌ Configuration échouée (mode simulation) :**
```
WARN  EmailService - JavaMailSender non configuré - Mode simulation activé
```

---

## 🧪 Test de l'envoi d'email

1. **Connectez-vous** en tant qu'étudiant
2. Allez sur **"Cours Disponibles"**
3. **Inscrivez-vous** à un cours
4. **Vérifiez la boîte mail** de l'étudiant (l'email dans ses informations personnelles)
5. **Vérifiez les spams** si l'email n'apparaît pas

---

## 📝 Important

- **L'email est envoyé à l'adresse stockée dans les informations personnelles** de l'étudiant
- Pour vérifier/modifier l'email d'un étudiant : Allez dans **"Mon Profil"** → **"Informations personnelles"**
- Les emails peuvent prendre quelques secondes à arriver
- Vérifiez les **spams/courriers indésirables** si l'email n'apparaît pas

---

## 🔍 Dépannage

### Problème : "Authentication failed"

**Solution :**
- Vérifiez que vous utilisez un **mot de passe d'application** (pas votre mot de passe Gmail normal)
- Vérifiez que la validation en 2 étapes est activée
- Régénérez un nouveau mot de passe d'application

### Problème : "Connection timeout"

**Solution :**
- Vérifiez votre connexion Internet
- Vérifiez que le port 587 n'est pas bloqué par votre firewall

### Problème : L'email n'arrive pas

**Solution :**
- Vérifiez que l'email de l'étudiant est correct dans ses informations personnelles
- Vérifiez les spams
- Regardez les logs pour voir si l'email a été envoyé avec succès
- Attendez quelques minutes (les emails peuvent être retardés)

---

## 💡 Astuce

Pour éviter de retaper les variables à chaque démarrage, créez un fichier `configurer-email.bat` avec :

```cmd
@echo off
set MAIL_USERNAME=votre-email@gmail.com
set MAIL_PASSWORD=votre-mot-de-passe-app
echo Configuration email chargee !
echo Demarrage de l'application...
mvn spring-boot:run
```

Puis lancez simplement ce fichier au lieu de `mvn spring-boot:run`.

