# 📧 Où trouver l'email de confirmation d'inscription ?

## ⚠️ IMPORTANT : Mode Simulation (Par défaut)

**Par défaut, le système fonctionne en MODE SIMULATION**. Cela signifie que :
- ✅ L'email est **créé et formaté** correctement
- ✅ L'email est **affiché dans la console** de l'application
- ❌ L'email n'est **PAS envoyé** à votre boîte mail réelle

---

## 🔍 Où trouver l'email ?

### Situation 1 : Mode Simulation (Par défaut - Normal)

**Si vous n'avez PAS configuré `MAIL_USERNAME` et `MAIL_PASSWORD`**, le système fonctionne en **mode simulation**.

#### ✅ Où trouver l'email simulé :

**Dans la console de l'application Spring Boot** (où vous avez lancé `mvn spring-boot:run` ou l'application)

**📍 Comment trouver la console :**
1. Cherchez la fenêtre/terminal où vous avez démarré l'application
2. C'est généralement la fenêtre où vous voyez les logs Spring Boot
3. Faites défiler vers le haut pour voir les messages récents

**📋 Exemple de ce que vous devriez voir :**

```
═══════════════════════════════════════════════════════════
📧 EMAIL SIMULÉ - INSCRIPTION
═══════════════════════════════════════════════════════════
À: etudiant@example.com
Sujet: Inscription au cours: [Nom du cours]
Message: Vous avez été inscrit(e) au cours: [Nom] ([Code])

📧 NOTIFICATION FORMATEUR SIMULÉE
À: formateur@example.com
Sujet: Notification: Nouvel étudiant inscrit au cours [Nom]
Étudiant: [Prénom] [Nom]
═══════════════════════════════════════════════════════════
```

**💡 Astuce :** Après avoir cliqué sur "S'inscrire", regardez immédiatement la console. L'email apparaît juste après l'inscription.

**⚠️ Important :** L'email n'est **PAS réellement envoyé** en mode simulation. Il est seulement **affiché dans la console** pour le développement.

---

### Situation 2 : Mode Réel (Si configuré)

**Si vous avez configuré `MAIL_USERNAME` et `MAIL_PASSWORD`**, les emails sont **réellement envoyés**.

#### ✅ Où trouver l'email réel :

1. **Vérifiez la boîte mail de l'étudiant** (l'email utilisé lors de la création du compte)
2. **Vérifiez les spams/courriers indésirables** si l'email n'apparaît pas dans la boîte de réception
3. **Vérifiez les logs** pour confirmer l'envoi :
   ```
   INFO  EmailService - ✅ Email d'inscription envoyé avec succès à: etudiant@example.com
   ```

---

## 🔧 Comment savoir dans quel mode vous êtes ?

### Vérification rapide :

1. **Regardez la console de l'application** après une inscription
2. **Cherchez ces messages :**

   **Mode Simulation :**
   ```
   WARN  EmailService - JavaMailSender non configuré - Mode simulation activé
   ═══════════════════════════════════════════════════════════
   📧 EMAIL SIMULÉ - INSCRIPTION
   ```

   **Mode Réel :**
   ```
   INFO  EmailService - ✅ Email d'inscription envoyé avec succès à: ...
   ```

---

## 📋 Étapes détaillées pour trouver l'email

### Étape 1 : Localiser la console de l'application

**Où est la console ?**
- Si vous avez lancé l'application avec `mvn spring-boot:run` → C'est le terminal/CMD/PowerShell où vous avez tapé cette commande
- Si vous avez lancé depuis un IDE (IntelliJ, Eclipse, VS Code) → C'est l'onglet "Console" ou "Terminal" en bas de l'écran
- Si vous avez lancé depuis un fichier `.jar` → C'est la fenêtre où l'application s'exécute

**Comment la reconnaître ?**
- Vous voyez des logs Spring Boot qui commencent par :
  ```
  .   ____          _            __ _ _
   /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
  ( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
   \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
    '  |____| .__|_| |_|_| |_\__, | / / / /
   =========|_|==============|___/=/_/_/_/
  ```

### Étape 2 : Inscrire un étudiant et regarder la console

1. **Inscrivez un étudiant** à un cours depuis l'interface web
2. **Immédiatement après**, regardez la console
3. **Cherchez** les messages commençant par :
   - `📧 EMAIL SIMULÉ - INSCRIPTION` (mode simulation)
   - `✅ Email d'inscription envoyé avec succès` (mode réel)

### Étape 2 : Si en mode simulation

- ✅ **L'email est dans la console** - C'est normal !
- ❌ **L'email n'est PAS dans votre boîte mail** - C'est normal en mode simulation
- 📝 **Pour voir l'email**, regardez la console de l'application

### Étape 3 : Si en mode réel

- ✅ **L'email est dans la boîte mail de l'étudiant**
- 📧 **Vérifiez l'adresse email** utilisée lors de la création du compte étudiant
- 🔍 **Vérifiez les spams** si l'email n'apparaît pas

---

## 🚀 Activer l'envoi réel d'emails

Si vous voulez recevoir de **vrais emails** dans votre boîte mail :

### Configuration Gmail :

1. **Créer un mot de passe d'application Gmail** (voir GUIDE_DEPANNAGE_EMAIL.md)
2. **Configurer les variables d'environnement :**

   **Windows (CMD) :**
   ```cmd
   set MAIL_USERNAME=votre-email@gmail.com
   set MAIL_PASSWORD=votre-mot-de-passe-app
   ```

   **Windows (PowerShell) :**
   ```powershell
   $env:MAIL_USERNAME="votre-email@gmail.com"
   $env:MAIL_PASSWORD="votre-mot-de-passe-app"
   ```

3. **Redémarrer l'application**

4. **Tester** : Les emails seront maintenant envoyés à la vraie adresse email

---

## 📝 Résumé

| Mode | Où trouver l'email | Email réellement envoyé ? |
|------|---------------------|---------------------------|
| **Simulation** (par défaut) | Console de l'application | ❌ Non |
| **Réel** (si configuré) | Boîte mail de l'étudiant | ✅ Oui |

---

## 💡 Astuce

Pour voir facilement les emails simulés :
1. Gardez la console de l'application visible
2. Après chaque inscription, regardez la console
3. Les emails simulés sont affichés avec un formatage clair

---

## ❓ Questions fréquentes

**Q : Pourquoi je ne reçois pas l'email dans ma boîte mail ?**
R : Par défaut, le système fonctionne en mode simulation. Les emails sont affichés dans la console, pas envoyés réellement.

**Q : Comment activer l'envoi réel ?**
R : Configurez `MAIL_USERNAME` et `MAIL_PASSWORD` puis redémarrez l'application.

**Q : Où est la console de l'application ?**
R : 
- **Terminal/CMD** : Si vous avez lancé avec `mvn spring-boot:run`, c'est cette fenêtre
- **IDE (IntelliJ/Eclipse)** : Onglet "Console" ou "Terminal" en bas de l'écran
- **VS Code** : Onglet "Terminal" intégré
- **Fichier .jar** : La fenêtre où l'application s'exécute

**Q : Je ne vois rien dans la console, que faire ?**
R : 
1. Vérifiez que vous regardez la bonne fenêtre (celle où l'application tourne)
2. Faites défiler vers le haut dans la console
3. Vérifiez que l'inscription s'est bien passée (l'étudiant apparaît dans "Mes Cours")
4. Cherchez les messages avec `📧` ou `EMAIL` dans la console

**Q : L'email simulé contient-il toutes les informations ?**
R : Oui, l'email simulé contient exactement les mêmes informations que l'email réel.

