# 🏆 Mondial 2030 - Plateforme de Gestion des Tickets

Application de gestion de billetterie pour la Coupe du Monde 2030 (Maroc - Espagne - Portugal).

![Java](https://img.shields.io/badge/Java-17+-orange)
![JavaFX](https://img.shields.io/badge/JavaFX-17+-blue)
![MariaDB](https://img.shields.io/badge/MariaDB-10.11-blue)
![Hibernate](https://img.shields.io/badge/Hibernate-6.2.7-green)
![Maven](https://img.shields.io/badge/Maven-3.8+-red)
![Docker](https://img.shields.io/badge/Docker-Compose-blue)

## 📋 Description

Cette application permet de gérer la billetterie pour les matchs de la Coupe du Monde 2030. Elle offre des fonctionnalités complètes pour les utilisateurs et les administrateurs avec une persistance complète en base de données.

### Fonctionnalités Utilisateur
- ✅ Inscription et connexion avec persistance en BD
- ✅ Consultation des matchs disponibles (dropdown dynamique)
- ✅ Achat de tickets par match et catégorie (VIP, Standard, Tribune)
- ✅ Recherche dynamique en temps réel
- ✅ Historique des achats persisté ("Mes Tickets")
- ✅ Tirage au sort pour gagner des tickets (stockés en BD)
- ✅ Génération de codes QR uniques

### Fonctionnalités Administrateur
- ✅ Gestion des matchs (CRUD complet)
- ✅ Gestion des tickets (uniquement pour matchs existants)
- ✅ Dropdown dynamique des matchs depuis la base de données
- ✅ Cascade delete : suppression d'un match → suppression de ses tickets
- ✅ Gestion des utilisateurs
- ✅ Statistiques de vente en temps réel
- ✅ Export CSV
- ✅ Réinitialisation des données de test

## 🛠️ Technologies

| Technologie | Version | Utilisation |
|-------------|---------|-------------|
| Java | 17+ | Langage principal |
| JavaFX | 17+ | Interface graphique |
| Maven | 3.8.5 | Gestion des dépendances |
| Hibernate | 6.2.7 | ORM (mapping objet-relationnel) |
| MariaDB | 10.11 | Base de données |
| Docker | Compose | Conteneurisation MariaDB |
| Git/GitHub | - | Versioning |

## 📁 Structure du Projet

```
mondial2030/
├── src/main/java/com/mondial/ticket/
│   ├── model/          # Entités (Ticket, Match, User, PurchasedTicket)
│   ├── dao/            # Data Access Objects (Hibernate)
│   ├── service/        # Logique métier
│   ├── view/           # Contrôleurs JavaFX
│   ├── util/           # Utilitaires (HibernateUtil, DatabaseConnection)
│   └── exception/      # Exceptions personnalisées
├── src/main/resources/
│   ├── hibernate.cfg.xml
│   └── com/mondial/ticket/
│       ├── model/      # Mappings Hibernate (*.hbm.xml)
│       └── view/       # Fichiers FXML (interfaces)
├── docker-compose.yml  # Configuration Docker MariaDB
├── schema.sql          # Script SQL de création des tables
├── pom.xml             # Configuration Maven
└── README.md
```

## 🚀 Installation

### Prérequis
- Java JDK 17+
- Maven 3.8+
- Docker (pour MariaDB)

### 1. Cloner le repository
```bash
git clone https://github.com/AYMAN-hr/mondial2030.git
cd mondial2030
```

### 2. Lancer la base de données MariaDB
```bash
docker-compose up -d db
```

### 3. Compiler et exécuter
```bash
./mvnw clean compile exec:java
```

Ou sur Windows :
```cmd
mvnw.cmd clean compile exec:java
```

## 👤 Comptes par Défaut

| Rôle | Username | Password |
|------|----------|----------|
| Admin | admin | admin123 |
| User | user | user123 |

## 🗄️ Base de Données

### Architecture des Tables

```
┌────────────────────┐                    ┌────────────────────┐
│       USERS        │                    │       MATCHS       │
├────────────────────┤                    ├────────────────────┤
│ id (PK)            │                    │ id (PK)            │
│ username (UNIQUE)  │◄───────┐           │ equipe1            │
│ password           │        │           │ equipe2            │
│ role               │        │           │ stade              │
│ nom                │        │           │ ville              │
│ email              │        │           │ pays               │
└────────────────────┘        │           │ phase              │
                              │           └─────────┬──────────┘
                              │                     │ 1:N (CASCADE)
┌────────────────────────┐    │           ┌────────────────────┐
│   PURCHASED_TICKETS    │    │           │      TICKETS       │
├────────────────────────┤    │           ├────────────────────┤
│ id (PK)                │    │           │ id (PK)            │
│ ticket_id              │    │           │ nom_match          │
│ username (FK) ─────────┼────┘           │ categorie          │
│ match_name             │                │ prix               │
│ category               │                │ status             │
│ price                  │    ┌───────────│ acheteur           │
│ purchase_date          │    │           │ match_id (FK)      │
│ qr_code                │    │           └────────────────────┘
│ status                 │    │
│ original_ticket_id (FK)┼────┘
└────────────────────────┘
```

### Tables

| Table | Description |
|-------|-------------|
| `users` | Utilisateurs (admin/user) avec authentification |
| `matchs` | Matchs du Mondial 2030 |
| `tickets` | Billets disponibles par match et catégorie |
| `purchased_tickets` | Historique des achats par utilisateur |

## 🔧 Configuration

### hibernate.cfg.xml
```xml
<property name="connection.driver_class">org.mariadb.jdbc.Driver</property>
<property name="connection.url">jdbc:mariadb://localhost:3306/mondial2030</property>
<property name="connection.username">root</property>
<property name="connection.password">mondial2030</property>
<property name="dialect">org.hibernate.dialect.MariaDBDialect</property>
```

### docker-compose.yml
```yaml
services:
  db:
    image: mariadb:10.11
    container_name: mariadb_mondial2030
    environment:
      MARIADB_ROOT_PASSWORD: mondial2030
      MARIADB_DATABASE: mondial2030
    ports:
      - "3306:3306"
    volumes:
      - mariadb_data:/var/lib/mysql
      - ./schema.sql:/docker-entrypoint-initdb.d/schema.sql
```

## 📸 Fonctionnalités Principales

### 🔐 Authentification
- Connexion sécurisée avec rôles (Admin/User)
- Inscription de nouveaux utilisateurs
- Données persistées en base de données

### 🎫 Gestion des Tickets
- Dropdown dynamique des matchs (depuis la table `matchs`)
- Recherche en temps réel (filtre pendant la saisie)
- Filtrage par catégorie (VIP, Standard, Tribune)
- Achat avec nom d'utilisateur automatique

### ⚽ Gestion des Matchs (Admin)
- CRUD complet
- Cascade delete : suppression d'un match → suppression de ses tickets
- Validation : tickets uniquement pour matchs existants

### 📊 Statistiques
- Dashboard temps réel
- Tickets vendus/disponibles
- Revenus générés

## 📝 Documentation

- [RAPPORT_PROJET_MONDIAL2030.md](RAPPORT_PROJET_MONDIAL2030.md) - Rapport complet du projet
- [COMPTE_RENDU_PROJET.md](COMPTE_RENDU_PROJET.md) - Compte rendu structuré

## 📄 Licence

Ce projet est développé dans un cadre éducatif - EMSI 4IIR.

## 👥 Auteur

**AYMAN-hr** - Projet Java Avancé - Année 2025-2026

---

*Coupe du Monde 2030 - Maroc 🇲🇦 | Espagne 🇪🇸 | Portugal 🇵🇹*
