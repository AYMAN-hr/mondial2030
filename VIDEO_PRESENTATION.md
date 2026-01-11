# 🎬 Guide de Présentation Vidéo - Mondial 2030

## Script de Présentation et Étapes de Démonstration

---

## 📋 Plan de la Vidéo (Durée estimée: 8-10 minutes)

| Étape | Section | Durée |
|-------|---------|-------|
| 1 | Introduction | 1 min |
| 2 | Architecture & Technologies | 1 min |
| 3 | Base de données | 1 min |
| 4 | Authentification | 1 min |
| 5 | Fonctionnalités Utilisateur | 2 min |
| 6 | Fonctionnalités Admin | 2 min |
| 7 | Conclusion | 30 sec |

---

## 🎤 SCRIPT DE PRÉSENTATION

### ÉTAPE 1: Introduction (1 minute)

**[Afficher: Écran d'accueil de l'application]**

> "Bonjour, je suis [Votre Nom], étudiant en 4ème année Ingénierie Informatique à l'EMSI.
>
> Aujourd'hui, je vais vous présenter mon projet Java Avancé : **La Plateforme Intelligente de Gestion des Tickets du Mondial 2030**.
>
> Comme vous le savez, le Maroc, l'Espagne et le Portugal accueilleront ensemble la Coupe du Monde 2030. Cette application permet de gérer la billetterie pour cet événement majeur.
>
> L'application offre deux types d'accès :
> - **Utilisateur** : pour acheter des tickets
> - **Administrateur** : pour gérer les matchs et les tickets"

---
### ÉTAPE 4: Authentification (1 minute)

**[Afficher: Page de Login]**

> "L'application commence par une page de connexion sécurisée.
>
> Nous avons deux comptes par défaut :
> - **admin / admin123** pour l'administrateur
> - **user / user123** pour un utilisateur standard"

**[Action: Montrer l'inscription]**

> "Les nouveaux utilisateurs peuvent s'inscrire. Les données sont persistées en base de données."

**[Action: Se connecter en tant qu'utilisateur]**

> "Connectons-nous d'abord en tant qu'utilisateur pour voir les fonctionnalités disponibles."

---

### ÉTAPE 5: Fonctionnalités Utilisateur (2 minutes)

#### 5.1 Dashboard Principal

**[Afficher: Interface principale]**

> "Voici le tableau de bord principal. On peut voir :
> - Les statistiques en temps réel
> - Le nombre de tickets disponibles
> - L'accès aux différentes sections"

#### 5.2 Consultation des Matchs

**[Action: Naviguer vers les matchs]**

> "L'utilisateur peut consulter tous les matchs du Mondial 2030 avec les informations : équipes, stade, ville, pays et phase du tournoi."

#### 5.3 Achat de Tickets

**[Action: Aller dans la section Tickets]**

> "Pour acheter un ticket, l'utilisateur :
>
> 1. **Sélectionne un match** dans le dropdown - qui est alimenté dynamiquement depuis la base de données
> 2. **Filtre par catégorie** : VIP, Standard ou Tribune
> 3. **Utilise la recherche dynamique** - le filtrage se fait en temps réel pendant la saisie"

**[Action: Démontrer la recherche dynamique]**

> "Regardez, quand je tape 'Maroc', les résultats se filtrent instantanément."

**[Action: Acheter un ticket]**

> "Je sélectionne ce ticket et je clique sur Acheter. Le nom de l'utilisateur connecté est automatiquement utilisé."

#### 5.4 Mes Tickets

**[Action: Aller dans "Mes Tickets"]**

> "L'historique des achats est persisté en base de données. Chaque ticket a un code QR unique."

#### 5.5 Tirage au Sort

**[Action: Montrer le tirage au sort]**

> "L'utilisateur peut aussi participer à un tirage au sort pour gagner des tickets gratuits. Les tickets gagnés sont également stockés dans son historique."

---

### ÉTAPE 6: Fonctionnalités Administrateur (2 minutes)

**[Action: Se déconnecter et se reconnecter en admin]**

> "Maintenant, connectons-nous en tant qu'administrateur avec admin/admin123."

#### 6.1 Interface Admin

**[Afficher: Dashboard admin]**

> "L'administrateur a accès à des fonctionnalités supplémentaires visibles dans le menu."

#### 6.2 Gestion des Matchs

**[Action: Aller dans Gestion des Matchs]**

> "L'admin peut créer, modifier et supprimer des matchs.
>
> Je vais créer un nouveau match : France vs Allemagne au Stade de France."

**[Action: Créer le match]**

> "Le match apparaît immédiatement dans la liste."

#### 6.3 Gestion des Tickets

**[Action: Aller dans Gestion des Tickets]**

> "Pour créer un ticket, l'admin doit :
>
> 1. **Sélectionner un match existant** dans le dropdown - on ne peut PAS taper un nom de match manuellement
> 2. Choisir la catégorie
> 3. Entrer le prix
>
> Cette validation garantit que les tickets ne peuvent être créés que pour des matchs existants."

**[Action: Créer un ticket pour le nouveau match]**

> "Je crée un ticket VIP à 500€ pour France vs Allemagne."

#### 6.4 Cascade Delete

**[Action: Retourner aux matchs et supprimer le match créé]**

> "Si je supprime ce match... Voyez, tous ses tickets associés sont automatiquement supprimés grâce au CASCADE DELETE."

#### 6.5 Statistiques & Export

**[Action: Montrer les statistiques]**

> "L'admin peut consulter les statistiques de vente et exporter les données en CSV."

---

### ÉTAPE 7: Conclusion (30 secondes)

**[Afficher: README ou page d'accueil]**

> "En résumé, cette application offre :
>
> ✅ Une authentification sécurisée avec rôles
> ✅ Une gestion complète des matchs et tickets
> ✅ Des fonctionnalités dynamiques : dropdown, recherche en temps réel
> ✅ Une persistance complète en base de données avec Hibernate
> ✅ Une architecture propre en couches MVC
>
> Merci pour votre attention. Le code source est disponible sur GitHub."

---

## 📝 CHECKLIST AVANT ENREGISTREMENT

### Préparation Technique
- [ ] Docker Desktop lancé
- [ ] MariaDB container en cours (`docker-compose up -d db`)
- [ ] Application compilée (`mvnw clean compile`)
- [ ] Données de test présentes dans la BD

### Éléments à Montrer
- [ ] Page de login
- [ ] Inscription d'un nouvel utilisateur
- [ ] Connexion utilisateur + admin
- [ ] Dropdown dynamique des matchs
- [ ] Recherche dynamique (taper et voir le filtre)
- [ ] Achat d'un ticket
- [ ] Mes Tickets (historique)
- [ ] Création d'un match (admin)
- [ ] Création d'un ticket pour ce match
- [ ] Suppression du match → cascade delete des tickets
- [ ] Statistiques

### Points Clés à Mentionner
- [ ] Technologies : Java 17, JavaFX, Hibernate, MariaDB, Docker
- [ ] Architecture MVC en couches
- [ ] 4 tables : users, matchs, tickets, purchased_tickets
- [ ] Relation 1:N avec CASCADE DELETE
- [ ] Validation : tickets uniquement pour matchs existants
- [ ] Recherche dynamique en temps réel
- [ ] Persistance complète en BD

---

## 🎯 CONSEILS POUR LA VIDÉO

1. **Parlez lentement et clairement**
2. **Montrez votre écran pendant les actions**
3. **Faites des pauses entre les sections**
4. **Préparez les données à l'avance** (matchs, tickets)
5. **Testez le scénario complet avant l'enregistrement**
6. **Gardez l'IDE ouvert pour montrer le code si nécessaire**

---

## 📱 RÉSUMÉ EN 1 MINUTE (Si demandé)

> "Mon projet est une plateforme de gestion de billetterie pour le Mondial 2030.
>
> Développée en Java 17 avec JavaFX et Hibernate, elle utilise MariaDB via Docker.
>
> Les utilisateurs peuvent consulter les matchs, acheter des tickets avec recherche dynamique, et voir leur historique d'achats.
>
> Les administrateurs gèrent les matchs et tickets, avec une validation qui empêche la création de tickets pour des matchs inexistants.
>
> La suppression d'un match entraîne automatiquement la suppression de tous ses tickets grâce au cascade delete.
>
> L'architecture suit le pattern MVC avec une séparation claire en couches : Model, DAO, Service et View."

---

*Bonne présentation ! 🎬*

