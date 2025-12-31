# 🚀 Guide Rapide - Activer l'envoi réel d'emails

## ⚡ Configuration en 3 étapes

### 1️⃣ Créer un mot de passe d'application Gmail

1. Allez sur : https://myaccount.google.com/apppasswords
2. Si demandé, activez la **validation en 2 étapes**
3. Créez un mot de passe d'application pour "Gestion Formation"
4. **Copiez le mot de passe** (16 caractères)

---

### 2️⃣ Modifier le fichier `configurer-email.bat`

Ouvrez `configurer-email.bat` et modifiez ces lignes :

```cmd
set MAIL_USERNAME=votre-email@gmail.com
set MAIL_PASSWORD=votre-mot-de-passe-app-copie
```

**Remplacez :**
- `votre-email@gmail.com` → Votre vraie adresse Gmail
- `votre-mot-de-passe-app-copie` → Le mot de passe d'application que vous venez de copier

---

### 3️⃣ Lancer l'application

**Double-cliquez sur `configurer-email.bat`** au lieu de lancer `mvn spring-boot:run` normalement.

---

## ✅ Vérification

Après avoir lancé l'application, testez :

1. Connectez-vous en tant qu'étudiant
2. Inscrivez-vous à un cours
3. Vérifiez la boîte mail de l'étudiant (l'email dans ses informations personnelles)

**L'email devrait arriver dans quelques secondes !**

---

## 📧 Où est l'email de l'étudiant ?

L'email est stocké dans les **informations personnelles** de l'étudiant :
- Connectez-vous en tant qu'étudiant
- Allez dans **"Mon Profil"**
- L'email est affiché dans la section **"Informations personnelles"**

---

## ❓ Problèmes courants

**L'email n'arrive pas ?**
- Vérifiez que l'email de l'étudiant est correct dans "Mon Profil"
- Vérifiez les spams
- Regardez les logs de l'application pour voir les erreurs

**Erreur "Authentication failed" ?**
- Utilisez un **mot de passe d'application**, pas votre mot de passe Gmail normal
- Vérifiez que la validation en 2 étapes est activée

---

## 💡 Astuce

Gardez le fichier `configurer-email.bat` à portée de main. Vous devrez le modifier une seule fois, puis vous pourrez le réutiliser à chaque démarrage de l'application.

