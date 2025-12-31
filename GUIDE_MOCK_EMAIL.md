# 📧 Guide - MockMailService (Emails Simulés)

## 🎯 Objectif

Le **MockMailService** permet de simuler l'envoi d'emails lors du développement, sans avoir besoin de configurer un serveur SMTP. Les emails sont stockés dans la base de données et peuvent être visualisés via une interface web.

---

## ✅ Fonctionnalités

### Ce qui est simulé :

1. **Email d'inscription à l'étudiant** : Lorsqu'un étudiant s'inscrit à un cours
2. **Notification au formateur** : Lorsqu'un étudiant s'inscrit à son cours
3. **Email de désinscription à l'étudiant** : Lorsqu'un étudiant se désinscrit d'un cours
4. **Notification au formateur** : Lorsqu'un étudiant se désinscrit de son cours

### Avantages :

- ✅ **Pas de configuration SMTP nécessaire** pour le développement
- ✅ **Visualisation des emails** via une interface web
- ✅ **Stockage dans la base de données** pour consultation ultérieure
- ✅ **Affichage dans la console** pour debugging
- ✅ **Même format que les vrais emails** pour tester le contenu

---

## 🚀 Utilisation

### Mode automatique

Le MockMailService est **automatiquement activé** lorsque :
- `MAIL_USERNAME` n'est pas configuré, OU
- `MAIL_PASSWORD` n'est pas configuré, OU
- `JavaMailSender` n'est pas disponible

**Aucune configuration supplémentaire n'est nécessaire !**

### Comment ça fonctionne

1. **Un étudiant s'inscrit à un cours**
2. **Le système détecte** que le mode simulation est activé
3. **MockMailService** est appelé automatiquement
4. **L'email est sauvegardé** dans la table `mock_emails`
5. **L'email est affiché** dans la console
6. **L'email peut être consulté** via l'interface web

---

## 📊 Visualisation des Emails

### Via l'interface web (Recommandé)

1. **Connectez-vous en tant qu'administrateur**
2. **Allez dans** "Emails Simulés" dans le menu
3. **Consultez la liste** de tous les emails simulés
4. **Cliquez sur "Voir"** pour voir le détail d'un email

**URL :** `http://localhost:8080/admin/emails`

### Via la console

Les emails sont également affichés dans la console avec un formatage clair :

```
═══════════════════════════════════════════════════════════
📧 EMAIL SIMULÉ (MOCK) - INSCRIPTION
═══════════════════════════════════════════════════════════
De: noreply@gestion-formation.com
À: etudiant@example.com
Sujet: Inscription au cours: Java Avancé
Message:
Bonjour Jean Dupont,

Vous avez été inscrit(e) au cours: Java Avancé (JAVA-101).

Formateur: Marie Martin

Nous vous souhaitons une excellente formation !

Cordialement,
L'équipe de gestion de formation
═══════════════════════════════════════════════════════════
💡 Consultez http://localhost:8080/admin/emails pour voir tous les emails simulés
═══════════════════════════════════════════════════════════
```

---

## 🔍 Structure des Données

### Table `mock_emails`

Les emails simulés sont stockés avec les informations suivantes :

- **id** : Identifiant unique
- **from_email** : Expéditeur
- **to_email** : Destinataire
- **subject** : Sujet de l'email
- **body** : Contenu de l'email
- **date_envoi** : Date et heure de simulation
- **est_lu** : Indicateur de lecture
- **type_email** : Type (INSCRIPTION_ETUDIANT, INSCRIPTION_FORMATEUR, etc.)
- **etudiant_nom** : Nom de l'étudiant (si applicable)
- **cours_titre** : Titre du cours (si applicable)
- **cours_code** : Code du cours (si applicable)

---

## 🧪 Test

### Test d'inscription

1. **Connectez-vous en tant qu'étudiant**
2. **Allez sur** "Cours Disponibles"
3. **Inscrivez-vous à un cours**
4. **Vérifiez la console** : L'email simulé devrait apparaître
5. **Connectez-vous en tant qu'admin**
6. **Allez sur** "Emails Simulés"
7. **Vérifiez** que l'email apparaît dans la liste

### Test de désinscription

1. **Connectez-vous en tant qu'étudiant**
2. **Allez sur** "Mes Cours"
3. **Désinscrivez-vous d'un cours**
4. **Vérifiez** que l'email de désinscription apparaît

---

## 🔄 Passage en mode réel

Pour passer du mode simulation au mode réel :

1. **Configurez les variables d'environnement** :
   ```cmd
   set MAIL_USERNAME=votre-email@gmail.com
   set MAIL_PASSWORD=votre-mot-de-passe-app
   ```

2. **Redémarrez l'application**

3. **Le système utilisera JavaMailSender** au lieu de MockMailService

**Note :** Les emails simulés déjà stockés restent dans la base de données et peuvent toujours être consultés.

---

## 📝 Notes importantes

1. **Les emails simulés ne sont PAS envoyés** - Ils sont seulement stockés et affichés
2. **Le format est identique** aux vrais emails pour faciliter les tests
3. **Les emails sont persistés** dans la base de données H2
4. **L'interface web** permet de consulter tous les emails simulés
5. **Le mode simulation est activé par défaut** si aucune configuration SMTP n'est fournie

---

## 🐛 Dépannage

### Les emails n'apparaissent pas dans la liste

- Vérifiez que l'inscription s'est bien passée
- Vérifiez les logs pour voir si MockMailService a été appelé
- Vérifiez que vous êtes connecté en tant qu'administrateur

### L'interface web ne s'affiche pas

- Vérifiez que vous êtes connecté en tant qu'administrateur
- Vérifiez l'URL : `http://localhost:8080/admin/emails`
- Vérifiez les logs pour les erreurs

---

## 💡 Astuce

Pour tester rapidement le système :

1. Créez un compte étudiant de test
2. Inscrivez-le à plusieurs cours
3. Consultez la liste des emails simulés
4. Vérifiez que tous les emails sont bien formatés

---

## 🔗 Fichiers concernés

- `src/main/java/com/iit/formation/service/MockMailService.java` - Service principal
- `src/main/java/com/iit/formation/entity/MockEmail.java` - Entité JPA
- `src/main/java/com/iit/formation/repository/MockEmailRepository.java` - Repository
- `src/main/java/com/iit/formation/service/EmailService.java` - Service qui utilise MockMailService
- `src/main/java/com/iit/formation/controller/AdminController.java` - Contrôleur pour l'interface web
- `src/main/resources/templates/admin/emails/list.html` - Liste des emails
- `src/main/resources/templates/admin/emails/view.html` - Détail d'un email

