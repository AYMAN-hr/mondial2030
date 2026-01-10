# COMPTE RENDU DE PROJET

## Mondial 2030 - Plateforme de Gestion des Tickets

---

## 1. Informations Générales

| Élément | Description |
|---------|-------------|
| **Titre du Projet** | Mondial 2030 - Plateforme Intelligente de Gestion des Tickets |
| **Date** | Janvier 2026 |
| **Type** | Application Desktop JavaFX |

---

## 2. Descriptif du Projet

### 2.1 Objectif
Développer une plateforme complète de gestion de billetterie pour la Coupe du Monde 2030 qui sera co-organisée par le Maroc, l'Espagne et le Portugal.

### 2.2 Fonctionnalités Principales

#### Pour les Utilisateurs (USER)
- **Inscription et Connexion** : Création de compte avec stockage en base de données
- **Consultation des matchs** : Visualisation de tous les matchs disponibles
- **Achat de tickets** : Sélection et achat de billets par match et catégorie
- **Historique des achats** : Consultation des tickets achetés dans "Mes Tickets"
- **Tirage au sort** : Participation à la loterie pour gagner des tickets gratuits
- **Codes QR** : Génération automatique de codes QR pour chaque ticket

#### Pour les Administrateurs (ADMIN)
- **Gestion des tickets** : Ajout, modification et suppression de tickets
- **Gestion des matchs** : Création et gestion des matchs du tournoi
- **Gestion des utilisateurs** : Administration des comptes utilisateurs
- **Statistiques** : Visualisation des statistiques de vente
- **Export** : Export CSV de tous les tickets
- **Réinitialisation** : Création de données de test

---

## 3. Technologies et Environnement

### 3.1 Stack Technique

| Technologie | Version | Utilisation |
|-------------|---------|-------------|
| **Java** | 17+ | Langage principal |
| **JavaFX** | 17+ | Interface graphique |
| **Maven** | 3.8.5 | Gestion des dépendances |
| **Hibernate** | 6.2.7 | ORM (Object-Relational Mapping) |
| **MySQL** | 8.0 | Base de données |
| **Docker** | - | Conteneurisation MySQL |

### 3.2 Environnement de Développement

| Outil | Description |
|-------|-------------|
| **IDE** | IntelliJ IDEA 2025.2.6 |
| **OS** | Windows |
| **JDK** | OpenJDK 17+ |

### 3.3 Architecture

```
┌─────────────────────────────────────────────────────────┐
│                    PRESENTATION                          │
│    (JavaFX Views - FXML + Controllers)                  │
├─────────────────────────────────────────────────────────┤
│                      SERVICE                             │
│  (AuthService, TicketService, MatchService, etc.)       │
├─────────────────────────────────────────────────────────┤
│                        DAO                               │
│  (TicketDaoHibernate, MatchDaoHibernate, UserDao)       │
├─────────────────────────────────────────────────────────┤
│                      MODEL                               │
│        (Ticket, Match, User + Hibernate Mappings)       │
├─────────────────────────────────────────────────────────┤
│                     DATABASE                             │
│                  (MySQL / Docker)                        │
└─────────────────────────────────────────────────────────┘
```

---

## 4. Structure du Projet

```
mondial2030/
├── src/main/java/com/mondial/ticket/
│   ├── model/
│   │   ├── Ticket.java          # Modèle ticket
│   │   ├── Match.java           # Modèle match
│   │   └── User.java            # Modèle utilisateur
│   ├── dao/
│   │   ├── IDao.java            # Interface DAO générique
│   │   ├── TicketDaoHibernate.java
│   │   ├── MatchDaoHibernate.java
│   │   └── UserDao.java
│   ├── service/
│   │   ├── AuthService.java     # Authentification
│   │   ├── TicketService.java   # Gestion tickets
│   │   ├── MatchService.java    # Gestion matchs
│   │   └── UserTicketService.java # Historique achats
│   ├── view/
│   │   ├── LoginController.java
│   │   ├── MainController.java
│   │   ├── TicketController.java
│   │   └── MatchController.java
│   └── util/
│       ├── HibernateUtil.java
│       └── DatabaseConnection.java
├── src/main/resources/
│   ├── hibernate.cfg.xml
│   └── com/mondial/ticket/
│       ├── model/
│       │   ├── Ticket.hbm.xml
│       │   ├── Match.hbm.xml
│       │   └── User.hbm.xml
│       └── view/
│           ├── LoginView.fxml
│           ├── MainView.fxml
│           ├── TicketView.fxml
│           └── MatchView.fxml
├── docker-compose.yml
├── Dockerfile
└── pom.xml
```

---

## 5. Captures d'Écran des Interfaces

### 5.1 Écran de Connexion
```
┌────────────────────────────────────────┐
│          MONDIAL 2030                  │
│   Plateforme de Gestion des Tickets    │
│      Maroc - Espagne - Portugal        │
├────────────────────────────────────────┤
│           Connexion                    │
│                                        │
│  Nom d'utilisateur: [____________]     │
│  Mot de passe:      [____________]     │
│                                        │
│        [   Se connecter   ]            │
│  ─────────────────────────────────     │
│     Pas encore de compte?              │
│        [  Créer un compte ]            │
│  ─────────────────────────────────     │
│  Demo: admin/admin123, user/user123    │
└────────────────────────────────────────┘
```

### 5.2 Écran d'Inscription
```
┌────────────────────────────────────────┐
│     Inscription - Mondial 2030         │
├────────────────────────────────────────┤
│  Nom d'utilisateur: [____________]     │
│  Mot de passe:      [____________]     │
│  Confirmer:         [____________]     │
│  Nom complet:       [____________]     │
│  Email:             [____________]     │
│                                        │
│  [  Annuler  ]    [  S'inscrire  ]     │
└────────────────────────────────────────┘
```

### 5.3 Interface Principale (Tickets)
```
┌─────────────────────────────────────────────────────────────┐
│  Billetterie du Mondial 2030                                │
├─────────────────────────────────────────────────────────────┤
│  [Admin] Match: [____] Categorie: [▼] Prix: [__] [Ajouter]  │
├─────────────────────────────────────────────────────────────┤
│  Choisir un match: [Maroc vs Espagne ▼] Cat: [Tous ▼]       │
│                                              [Voir Tous]    │
├─────────────────────────────────────────────────────────────┤
│  ┌─────────────┬──────────┬────────┬──────────┬──────────┐  │
│  │ Match       │ Categorie│ Prix   │ Statut   │ Acheteur │  │
│  ├─────────────┼──────────┼────────┼──────────┼──────────┤  │
│  │ Maroc vs ES │ VIP      │ 500.0  │DISPONIBLE│          │  │
│  │ Maroc vs ES │ Standard │ 200.0  │DISPONIBLE│          │  │
│  │ Maroc vs ES │ Tribune  │ 80.0   │ VENDU    │ Ahmed    │  │
│  └─────────────┴──────────┴────────┴──────────┴──────────┘  │
├─────────────────────────────────────────────────────────────┤
│  Acheter: [Acheter] (Connecté en tant que: Utilisateur)     │
├─────────────────────────────────────────────────────────────┤
│  Recherche: [________] [Rechercher] [Suppr] [Stats] [Export]│
└─────────────────────────────────────────────────────────────┘
```

### 5.4 Gestion des Matchs
```
┌─────────────────────────────────────────────────────────────┐
│  Matchs du Mondial 2030                                     │
├─────────────────────────────────────────────────────────────┤
│  [Admin] Eq1:[___] VS Eq2:[___] Stade:[___] Ville:[___]     │
│          Pays:[▼] Phase:[▼] [Ajouter Match]                 │
├─────────────────────────────────────────────────────────────┤
│  ┌────┬──────────┬──────────┬────────┬───────┬─────┬──────┐ │
│  │ ID │ Equipe 1 │ Equipe 2 │ Stade  │ Ville │Pays │Phase │ │
│  ├────┼──────────┼──────────┼────────┼───────┼─────┼──────┤ │
│  │ 1  │ Maroc    │ Espagne  │ Casa   │ Casa  │ MA  │Quart │ │
│  │ 2  │ France   │ Allemagne│ Madrid │ Madrid│ ES  │Demi  │ │
│  └────┴──────────┴──────────┴────────┴───────┴─────┴──────┘ │
├─────────────────────────────────────────────────────────────┤
│  [Rafraichir] [Details] [Stats] [Supprimer]                 │
└─────────────────────────────────────────────────────────────┘
```

### 5.5 Mes Tickets (Historique Achats)
```
┌────────────────────────────────────────────────────────────┐
│  Mes Tickets                                               │
├────────────────────────────────────────────────────────────┤
│  ✅ Maroc vs Espagne [VIP] - 500€ | QR: QR-ABC123          │
│     10/01/2026 15:30                                       │
│  🎉 France vs Allemagne [Standard] - 200€ (GAGNÉ TIRAGE)   │
│     QR: QR-XYZ789 | 10/01/2026 16:00                       │
├────────────────────────────────────────────────────────────┤
│  🎫 Nombre de tickets: 2                                   │
│  💰 Total dépensé: 500€                                    │
└────────────────────────────────────────────────────────────┘
```

---

## 6. Base de Données

### 6.1 Tables

#### Table `users`
| Colonne | Type | Description |
|---------|------|-------------|
| id | INT (PK) | Identifiant unique |
| username | VARCHAR(50) | Nom d'utilisateur (unique) |
| password | VARCHAR(100) | Mot de passe |
| role | VARCHAR(20) | ADMIN ou USER |
| nom | VARCHAR(100) | Nom complet |
| email | VARCHAR(100) | Adresse email |

#### Table `tickets`
| Colonne | Type | Description |
|---------|------|-------------|
| id | INT (PK) | Identifiant unique |
| nom_match | VARCHAR(200) | Nom du match |
| categorie | VARCHAR(50) | VIP, Standard, Tribune |
| prix | DOUBLE | Prix du ticket |
| status | VARCHAR(20) | DISPONIBLE, VENDU |
| acheteur | VARCHAR(100) | Nom de l'acheteur |

#### Table `matchs`
| Colonne | Type | Description |
|---------|------|-------------|
| id | INT (PK) | Identifiant unique |
| equipe1 | VARCHAR(100) | Première équipe |
| equipe2 | VARCHAR(100) | Deuxième équipe |
| stade | VARCHAR(200) | Nom du stade |
| ville | VARCHAR(100) | Ville |
| pays | VARCHAR(50) | Pays hôte |
| phase | VARCHAR(50) | Phase du tournoi |

---

## 7. Fonctionnalités Détaillées

### 7.1 Système d'Authentification
- Inscription avec validation (username unique, email valide, mot de passe confirmé)
- Connexion avec vérification en base de données
- Gestion des sessions utilisateur
- Deux rôles : ADMIN et USER

### 7.2 Gestion des Tickets
- Création de tickets par l'admin
- Filtrage par match et catégorie
- Achat automatique avec l'utilisateur connecté
- Génération de code QR unique
- Historique des achats par utilisateur

### 7.3 Tirage au Sort
- Participation avec nom et email
- 30% de chance de gagner
- Ticket gagné automatiquement enregistré
- Affiché avec icône 🎉 dans "Mes Tickets"

### 7.4 Statistiques
- Nombre total de tickets
- Tickets vendus vs disponibles
- Revenus générés
- Répartition par catégorie

---

## 8. Instructions d'Installation

### 8.1 Prérequis
- Java JDK 17+
- Maven 3.8+
- Docker (pour MySQL)

### 8.2 Lancement de la Base de Données
```bash
docker-compose up -d
```

### 8.3 Compilation et Exécution
```bash
mvn clean compile exec:java
```

### 8.4 Comptes par Défaut
| Rôle | Username | Password |
|------|----------|----------|
| Admin | admin | admin123 |
| User | user | user123 |

---

## 9. Observations et Améliorations Futures

### 9.1 Points Forts
- Architecture MVC bien structurée
- Séparation claire des responsabilités (DAO, Service, Controller)
- Interface utilisateur intuitive
- Gestion des rôles (Admin/User)
- Persistance des données avec Hibernate/MySQL

### 9.2 Améliorations Possibles
- Ajout de paiement réel (Stripe, PayPal)
- Envoi d'emails de confirmation
- Génération de vrais codes QR (image)
- Application mobile complémentaire
- Système de notifications en temps réel
- Multi-langue (FR, EN, AR)

---

## 10. Conclusion

Ce projet démontre la mise en œuvre d'une application JavaFX complète avec :
- Architecture MVC
- Persistance avec Hibernate/MySQL
- Authentification et gestion des rôles
- Interface graphique moderne
- Fonctionnalités métier réalistes

L'application est fonctionnelle et prête pour une démonstration, avec possibilité d'extension pour des fonctionnalités supplémentaires.

---

*Document généré le 10 Janvier 2026*

