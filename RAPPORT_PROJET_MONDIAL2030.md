# Rapport de Projet Java Avancé
# Plateforme Intelligente de Gestion des Tickets du Mondial 2030

---

## 1. Page de Garde

<div align="center">

![EMSI Logo](https://www.emsi.ma/wp-content/uploads/2020/07/logo.png)

### **ÉCOLE MAROCAINE DES SCIENCES DE L'INGÉNIEUR**

---

# **Rapport de Projet Java Avancé**

## **Plateforme Intelligente de Gestion des Tickets**
## **Coupe du Monde 2030**
### Maroc - Espagne - Portugal 🇲🇦 🇪🇸 🇵🇹

---

**Intitulé du module :** Java Avancé / Programmation Orientée Objet

**Réalisé par :** AYMAN-hr

**Encadré par :** [Nom du Professeur]

**Année Universitaire :** 2025-2026

**Filière :** 4IIR - Ingénierie Informatique et Réseaux

</div>

---

## 2. Remerciements

Nous tenons à exprimer notre profonde gratitude envers :

- **Notre encadrant** pour son accompagnement, ses conseils précieux et sa disponibilité tout au long de la réalisation de ce projet.

- **L'administration de l'EMSI** pour les ressources pédagogiques et l'environnement de travail mis à notre disposition.

- **La communauté open-source** pour les outils et frameworks qui ont rendu ce projet possible (Hibernate, JavaFX, Maven).

Ce projet a été une occasion unique d'appliquer les concepts théoriques du Java avancé dans un contexte pratique et stimulant : la gestion de billetterie pour un événement mondial.

---

## 3. Table des Matières

1. [Page de Garde](#1-page-de-garde)
2. [Remerciements](#2-remerciements)
3. [Table des Matières](#3-table-des-matières)
4. [Introduction Générale](#4-introduction-générale)
5. [Partie I : Analyse et Conception](#5-partie-i--analyse-et-conception)
   - 5.1 Spécification des besoins
   - 5.2 Conception UML
   - 5.3 Conception de la Base de Données
6. [Partie II : Environnement Technique](#6-partie-ii--environnement-technique)
7. [Partie III : Architecture et Implémentation](#7-partie-iii--architecture-et-implémentation)
   - 7.1 Architecture logicielle
   - 7.2 Design Patterns
   - 7.3 Extraits de code clés
8. [Partie IV : Interface Utilisateur et Tests](#8-partie-iv--interface-utilisateur-et-tests)
   - 8.1 Présentation des interfaces
   - 8.2 Scénarios de Test
9. [Conclusion et Perspectives](#9-conclusion-et-perspectives)
10. [Webographie / Bibliographie](#10-webographie--bibliographie)

---

## 4. Introduction Générale

### 4.1 Contexte du Projet

La **Coupe du Monde FIFA 2030** sera co-organisée par le **Maroc**, l'**Espagne** et le **Portugal**, marquant un événement historique pour le football mondial. Avec des millions de spectateurs attendus, la gestion de la billetterie représente un défi majeur nécessitant une solution informatique robuste et performante.

Ce projet s'inscrit dans le cadre du module **Java Avancé** de la 4ème année Ingénierie Informatique et Réseaux (4IIR) à l'EMSI. Il vise à développer une **plateforme complète de gestion de billetterie** utilisant les technologies Java modernes.

### 4.2 Problématique

La gestion traditionnelle des billets pour des événements de grande envergure présente plusieurs défis :

- **Risques de fraude** : Duplication et revente illégale de billets
- **Difficultés de traçabilité** : Suivi des ventes en temps réel
- **Expérience utilisateur** : Files d'attente et processus d'achat complexe
- **Gestion des stocks** : Contrôle des places disponibles par catégorie
- **Administration centralisée** : Besoin d'un dashboard pour les gestionnaires

### 4.3 Objectifs du Projet

| Objectif | Description |
|----------|-------------|
| **Authentification** | Système de connexion sécurisé avec rôles (Admin/User) |
| **Gestion des Matchs** | CRUD complet pour les matchs du tournoi |
| **Gestion des Tickets** | Création, vente, suivi des billets par catégorie |
| **Achat en ligne** | Interface intuitive pour les utilisateurs |
| **Historique** | Suivi des achats par utilisateur |
| **Statistiques** | Dashboard avec indicateurs de performance |
| **Notifications** | Système d'alertes en temps réel |
| **Export** | Génération de rapports CSV |

---

## 5. Partie I : Analyse et Conception

### 5.1 Spécification des Besoins

#### 5.1.1 Besoins Fonctionnels

**Pour l'Utilisateur (User) :**
- S'inscrire et se connecter au système
- Consulter la liste des matchs disponibles
- Visualiser les tickets disponibles par match
- Acheter des tickets (sélection catégorie : VIP, Standard, Tribune)
- Consulter l'historique de ses achats ("Mes Tickets")
- Participer au tirage au sort pour gagner des tickets
- Appliquer des codes promotionnels

**Pour l'Administrateur (Admin) :**
- Toutes les fonctionnalités utilisateur
- Créer, modifier, supprimer des matchs
- Gérer les tickets (CRUD complet)
- Gérer les utilisateurs
- Consulter les statistiques de vente
- Exporter les données en CSV
- Gérer les codes promotionnels
- Réinitialiser les données de test

#### 5.1.2 Besoins Non-Fonctionnels

| Critère | Exigence |
|---------|----------|
| **Performance** | Temps de réponse < 2 secondes |
| **Sécurité** | Mots de passe hashés, contrôle d'accès par rôle |
| **Ergonomie** | Interface intuitive JavaFX |
| **Portabilité** | Compatible Windows/Linux/Mac via Java |
| **Maintenabilité** | Architecture en couches, code documenté |
| **Persistance** | Base de données MySQL avec Hibernate ORM |

---

### 5.2 Conception UML

#### 5.2.1 Diagramme de Cas d'Utilisation (Use Case)

```
                    ┌─────────────────────────────────────────────────────────┐
                    │           SYSTÈME MONDIAL 2030                          │
                    │                                                         │
    ┌───────┐       │  ┌──────────────────┐    ┌──────────────────┐          │
    │       │       │  │  Se connecter    │    │  S'inscrire      │          │
    │ USER  │───────┼─▶│                  │    │                  │          │
    │       │       │  └──────────────────┘    └──────────────────┘          │
    └───────┘       │                                                         │
        │           │  ┌──────────────────┐    ┌──────────────────┐          │
        │           │  │ Voir les matchs  │    │ Acheter ticket   │          │
        └───────────┼─▶│                  │───▶│                  │          │
                    │  └──────────────────┘    └──────────────────┘          │
                    │                                                         │
                    │  ┌──────────────────┐    ┌──────────────────┐          │
                    │  │ Mes tickets      │    │ Tirage au sort   │          │
                    │  │                  │    │                  │          │
                    │  └──────────────────┘    └──────────────────┘          │
                    │                                                         │
    ┌───────┐       │  ┌──────────────────┐    ┌──────────────────┐          │
    │       │       │  │ Gérer matchs     │    │ Gérer tickets    │          │
    │ ADMIN │───────┼─▶│ (CRUD)           │    │ (CRUD)           │          │
    │       │       │  └──────────────────┘    └──────────────────┘          │
    └───────┘       │                                                         │
        │           │  ┌──────────────────┐    ┌──────────────────┐          │
        │           │  │ Gérer users      │    │ Voir stats       │          │
        └───────────┼─▶│                  │    │                  │          │
                    │  └──────────────────┘    └──────────────────┘          │
                    │                                                         │
                    │  ┌──────────────────┐                                   │
                    │  │ Export CSV       │                                   │
                    │  │                  │                                   │
                    │  └──────────────────┘                                   │
                    └─────────────────────────────────────────────────────────┘
```

#### 5.2.2 Diagramme de Classes

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                              MODÈLE DE CLASSES                                   │
└─────────────────────────────────────────────────────────────────────────────────┘

┌──────────────────────────┐       ┌──────────────────────────┐
│         <<Entity>>       │       │      <<Entity>>          │
│           User           │       │         Match            │
├──────────────────────────┤       ├──────────────────────────┤
│ - id: int                │       │ - id: int                │
│ - username: String       │       │ - equipe1: String        │
│ - password: String       │       │ - equipe2: String        │
│ - role: String           │       │ - stade: String          │
│ - nom: String            │       │ - ville: String          │
│ - email: String          │       │ - pays: String           │
├──────────────────────────┤       │ - phase: String          │
│ + isAdmin(): boolean     │       │ - dateHeure: LocalDateTime│
│ + getters/setters        │       │ - tickets: Set<Ticket>   │
└────────────┬─────────────┘       ├──────────────────────────┤
             │                     │ + getNomComplet(): String│
             │ 1:N                 │ + getters/setters        │
             ▼                     └────────────┬─────────────┘
┌──────────────────────────┐                    │
│      <<Entity>>          │                    │ 1:N (CASCADE)
│    PurchasedTicket       │                    ▼
├──────────────────────────┤       ┌──────────────────────────┐
│ - id: int                │       │      <<Entity>>          │
│ - ticketId: String       │       │        Ticket            │
│ - username: String       │       ├──────────────────────────┤
│ - matchName: String      │       │ - id: int                │
│ - category: String       │       │ - nomMatch: String       │
│ - price: double          │       │ - categorie: String      │
│ - purchaseDate: DateTime │       │ - prix: double           │
│ - qrCode: String         │◆──────│ - status: String         │
│ - status: String         │       │ - acheteur: String       │
│ - originalTicketId: int  │       │ - match: Match           │
├──────────────────────────┤       ├──────────────────────────┤
│ + generateQRCode(): String│      │ + toString(): String     │
│ + getters/setters        │       │ + getters/setters        │
└──────────────────────────┘       └──────────────────────────┘


┌──────────────────────────┐       ┌──────────────────────────┐
│     <<Interface>>        │       │      <<DAO>>             │
│         IDao<T>          │       │  TicketDaoHibernate      │
├──────────────────────────┤       ├──────────────────────────┤
│ + create(T): void        │◁──────│ + create(Ticket): void   │
│ + readAll(): List<T>     │       │ + readAll(): List<Ticket>│
│ + readByName(String): T  │       │ + readById(int): Ticket  │
│ + update(T): void        │       │ + deleteByMatch(): void  │
│ + delete(String): void   │       │ + deleteAll(): void      │
└──────────────────────────┘       └──────────────────────────┘

┌──────────────────────────┐       ┌──────────────────────────┐
│      <<DAO>>             │       │      <<DAO>>             │
│  MatchDaoHibernate       │       │  PurchasedTicketDao      │
├──────────────────────────┤       ├──────────────────────────┤
│ + create(Match): void    │       │ + create(): boolean      │
│ + readAll(): List<Match> │       │ + findByUsername(): List │
│ + delete(String): void   │       │ + findByTicketId()       │
│ (cascade delete tickets) │       │ + update(): boolean      │
└──────────────────────────┘       └──────────────────────────┘

┌──────────────────────────┐       ┌──────────────────────────┐
│      <<Service>>         │       │      <<Service>>         │
│     TicketService        │       │      AuthService         │
├──────────────────────────┤       ├──────────────────────────┤
│ - dao: IDao<Ticket>      │       │ - instance: AuthService  │
├──────────────────────────┤       │ - currentUser: User      │
│ + enregistrerTicket()    │       │ - userDao: UserDao       │
│ + recupererTout()        │       ├──────────────────────────┤
│ + modifierPrix()         │       │ + getInstance(): AuthService │
│ + retirerTicket()        │       │ + login(): boolean       │
│ + acheterTicket()        │       │ + logout(): void         │
│ + recupererDisponibles() │       │ + register(): boolean    │
│ + afficherStatistiques() │       │ + isAdmin(): boolean     │
└──────────────────────────┘       └──────────────────────────┘

┌──────────────────────────┐       ┌──────────────────────────┐
│      <<Service>>         │       │       <<Utility>>        │
│   UserTicketService      │       │      HibernateUtil       │
├──────────────────────────┤       ├──────────────────────────┤
│ - instance: UserTicket   │       │ - sessionFactory         │
│ - purchasedTicketDao     │       ├──────────────────────────┤
├──────────────────────────┤       │ + getSessionFactory()    │
│ + recordPurchase()       │       │ + shutdown(): void       │
│ + getUserTickets()       │       └──────────────────────────┘
│ + getValidTickets()      │
│ + requestRefund()        │
│ + getTotalSpent()        │
└──────────────────────────┘
└──────────────────────────┘
```

#### 5.2.3 Diagramme de Séquence - Processus d'Achat de Ticket

```
┌──────┐          ┌────────────┐       ┌──────────────┐      ┌──────────────┐      ┌────────┐
│ User │          │ Controller │       │TicketService │      │ TicketDao    │      │   BD   │
└──┬───┘          └─────┬──────┘       └──────┬───────┘      └──────┬───────┘      └───┬────┘
   │                    │                     │                     │                  │
   │ 1. Sélectionner    │                     │                     │                  │
   │    match           │                     │                     │                  │
   │───────────────────▶│                     │                     │                  │
   │                    │                     │                     │                  │
   │                    │ 2. getTicketsByMatch│                     │                  │
   │                    │────────────────────▶│                     │                  │
   │                    │                     │ 3. readByMatchId()  │                  │
   │                    │                     │────────────────────▶│                  │
   │                    │                     │                     │ 4. SELECT        │
   │                    │                     │                     │─────────────────▶│
   │                    │                     │                     │                  │
   │                    │                     │                     │◀─────────────────│
   │                    │                     │◀────────────────────│ 5. tickets[]    │
   │                    │◀────────────────────│                     │                  │
   │◀───────────────────│ 6. Afficher tickets │                     │                  │
   │                    │                     │                     │                  │
   │ 7. Acheter ticket  │                     │                     │                  │
   │───────────────────▶│                     │                     │                  │
   │                    │ 8. acheterTicket()  │                     │                  │
   │                    │────────────────────▶│                     │                  │
   │                    │                     │ 9. update(ticket)   │                  │
   │                    │                     │────────────────────▶│                  │
   │                    │                     │                     │ 10. UPDATE       │
   │                    │                     │                     │─────────────────▶│
   │                    │                     │                     │                  │
   │                    │                     │                     │◀─────────────────│
   │                    │                     │◀────────────────────│ 11. success     │
   │                    │◀────────────────────│ 12. confirmation   │                  │
   │◀───────────────────│ 13. Notification    │                     │                  │
   │                    │     "Achat réussi!" │                     │                  │
```

---

### 5.3 Conception de la Base de Données

#### 5.3.1 Modèle Logique de Données (MLD)

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                                  SCHÉMA BDD                                      │
└─────────────────────────────────────────────────────────────────────────────────┘

┌────────────────────┐                              ┌────────────────────┐
│       USERS        │                              │       MATCHS       │
├────────────────────┤                              ├────────────────────┤
│ id (PK)            │                              │ id (PK)            │
│ username (UNIQUE)  │◄─────────────────┐           │ equipe1            │
│ password           │                  │           │ equipe2            │
│ role               │                  │           │ stade              │
│ nom                │                  │           │ ville              │
│ email              │                  │           │ pays               │
└────────────────────┘                  │           │ phase              │
                                        │           │ date_heure         │
                                        │           └─────────┬──────────┘
                                        │                     │
                                        │                     │ 1:N (CASCADE DELETE)
                                        │                     ▼
┌────────────────────────────┐          │           ┌────────────────────┐
│    PURCHASED_TICKETS       │          │           │      TICKETS       │
├────────────────────────────┤          │           ├────────────────────┤
│ id (PK)                    │          │           │ id (PK)            │
│ ticket_id                  │          │           │ nom_match          │
│ username (FK) ─────────────┼──────────┘           │ categorie          │
│ match_name                 │                      │ prix               │
│ category                   │                      │ status             │
│ price                      │                      │ acheteur           │
│ purchase_date              │          ┌───────────│ match_id (FK)      │
│ qr_code                    │          │           └────────────────────┘
│ status                     │          │
│ original_ticket_id (FK) ───┼──────────┘
└────────────────────────────┘

Relations:
• Match → Tickets : One-to-Many (1:N) avec CASCADE DELETE
• User → PurchasedTickets : One-to-Many (1:N) via username
• Ticket → PurchasedTicket : One-to-Many (1:N) via original_ticket_id
```

#### 5.3.2 Dictionnaire de Données

**Table USERS :**

| Champ | Type | Taille | Contrainte | Description |
|-------|------|--------|------------|-------------|
| id | INT | - | PK, AUTO_INCREMENT | Identifiant unique |
| username | VARCHAR | 50 | UNIQUE, NOT NULL | Nom d'utilisateur |
| password | VARCHAR | 100 | NOT NULL | Mot de passe |
| role | VARCHAR | 20 | NOT NULL, DEFAULT 'USER' | Rôle (ADMIN/USER) |
| nom | VARCHAR | 100 | - | Nom complet |
| email | VARCHAR | 100 | - | Email |

**Table MATCHS :**

| Champ | Type | Taille | Contrainte | Description |
|-------|------|--------|------------|-------------|
| id | INT | - | PK, AUTO_INCREMENT | Identifiant unique |
| equipe1 | VARCHAR | 100 | NOT NULL | Première équipe |
| equipe2 | VARCHAR | 100 | NOT NULL | Deuxième équipe |
| stade | VARCHAR | 200 | NOT NULL | Nom du stade |
| ville | VARCHAR | 100 | NOT NULL | Ville |
| pays | VARCHAR | 50 | NOT NULL | Pays hôte (Maroc/Espagne/Portugal) |
| phase | VARCHAR | 50 | NOT NULL | Phase du tournoi |
| date_heure | DATETIME | - | - | Date et heure du match |

**Table TICKETS :**

| Champ | Type | Taille | Contrainte | Description |
|-------|------|--------|------------|-------------|
| id | INT | - | PK, AUTO_INCREMENT | Identifiant unique |
| nom_match | VARCHAR | 200 | NOT NULL | Nom du match (équipe1 vs équipe2) |
| categorie | VARCHAR | 50 | NOT NULL | Catégorie (VIP/Standard/Tribune) |
| prix | DOUBLE | - | NOT NULL | Prix en euros |
| status | VARCHAR | 20 | DEFAULT 'DISPONIBLE' | Statut (DISPONIBLE/VENDU/RESERVE) |
| acheteur | VARCHAR | 100 | - | Nom de l'acheteur |
| match_id | INT | - | FK → matchs(id) CASCADE | Référence au match |

**Table PURCHASED_TICKETS :**

| Champ | Type | Taille | Contrainte | Description |
|-------|------|--------|------------|-------------|
| id | INT | - | PK, AUTO_INCREMENT | Identifiant unique |
| ticket_id | VARCHAR | 50 | NOT NULL | Identifiant du ticket acheté |
| username | VARCHAR | 50 | FK → users(username) | Acheteur |
| match_name | VARCHAR | 200 | NOT NULL | Nom du match |
| category | VARCHAR | 50 | NOT NULL | Catégorie du ticket |
| price | DOUBLE | - | NOT NULL | Prix payé |
| purchase_date | DATETIME | - | NOT NULL | Date d'achat |
| qr_code | VARCHAR | 50 | - | Code QR unique |
| status | VARCHAR | 20 | DEFAULT 'VALID' | Statut (VALID/USED/REFUNDED/WON) |
| original_ticket_id | INT | - | FK → tickets(id) | Référence au ticket original |

---

## 6. Partie II : Environnement Technique

### 6.1 Outils et Technologies Utilisés

| Catégorie | Outil/Technologie | Version | Justification |
|-----------|-------------------|---------|---------------|
| **Langage** | Java | JDK 17+ | Langage orienté objet robuste, multiplateforme |
| **IDE** | IntelliJ IDEA | 2024.x | IDE professionnel avec excellent support Java |
| **Build Tool** | Apache Maven | 3.8.5+ | Gestion automatisée des dépendances et du build |
| **Interface** | JavaFX | 17.0.8 | Framework moderne pour les IHM Java |
| **ORM** | Hibernate | 6.2.7 | Mapping objet-relationnel, productivité accrue |
| **SGBD** | MariaDB | 10.11 | Base de données relationnelle performante (compatible MySQL) |
| **SGBD (dev)** | H2 Database | 2.2.224 | Base embarquée pour développement rapide |
| **Conteneurisation** | Docker | - | Isolation et portabilité de la base de données |
| **Versioning** | Git/GitHub | - | Gestion de versions et collaboration |

### 6.2 Extrait du fichier pom.xml

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.mondial.ticket</groupId>
    <artifactId>gestion-tickets-mondial2030</artifactId>
    <version>1.0-SNAPSHOT</version>
    <name>Plateforme Intelligente de Gestion des Tickets du Mondial 2030</name>

    <properties>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
    </properties>

    <dependencies>
        <!-- MariaDB Connector pour la connexion à la base de données -->
        <dependency>
            <groupId>org.mariadb.jdbc</groupId>
            <artifactId>mariadb-java-client</artifactId>
            <version>3.3.2</version>
        </dependency>

        <!-- Hibernate ORM pour le mapping objet-relationnel -->
        <dependency>
            <groupId>org.hibernate.orm</groupId>
            <artifactId>hibernate-core</artifactId>
            <version>6.2.7.Final</version>
        </dependency>

        <!-- JavaFX pour l'interface graphique moderne -->
        <dependency>
            <groupId>org.openjfx</groupId>
            <artifactId>javafx-controls</artifactId>
            <version>17.0.8</version>
        </dependency>
        <dependency>
            <groupId>org.openjfx</groupId>
            <artifactId>javafx-fxml</artifactId>
            <version>17.0.8</version>
        </dependency>

        <!-- H2 Database pour le développement local -->
        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <version>2.2.224</version>
        </dependency>
    </dependencies>
</project>
```

### 6.3 Configuration Hibernate (hibernate.cfg.xml)

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE hibernate-configuration PUBLIC
    "-//Hibernate/Hibernate Configuration DTD 3.0//EN"
    "http://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd">
<hibernate-configuration>
    <session-factory>
        <!-- Connexion MariaDB -->
        <property name="connection.driver_class">org.mariadb.jdbc.Driver</property>
        <property name="connection.url">jdbc:mariadb://localhost:3306/mondial2030</property>
        <property name="connection.username">root</property>
        <property name="connection.password">mondial2030</property>

        <!-- Dialecte SQL -->
        <property name="dialect">org.hibernate.dialect.MariaDBDialect</property>

        <!-- Affichage SQL pour le débogage -->
        <property name="show_sql">true</property>
        <property name="format_sql">true</property>

        <!-- Mise à jour automatique du schéma -->
        <property name="hbm2ddl.auto">update</property>

        <!-- Fichiers de mapping -->
        <mapping resource="com/mondial/ticket/model/Ticket.hbm.xml"/>
        <mapping resource="com/mondial/ticket/model/Match.hbm.xml"/>
        <mapping resource="com/mondial/ticket/model/User.hbm.xml"/>
        <mapping resource="com/mondial/ticket/model/PurchasedTicket.hbm.xml"/>
    </session-factory>
</hibernate-configuration>
```

### 6.4 Configuration Docker (docker-compose.yml)

```yaml
version: '3.8'

services:
  db:
    image: mariadb:10.11
    container_name: mariadb_mondial2030
    environment:
      MARIADB_ROOT_PASSWORD: mondial2030
      MARIADB_DATABASE: mondial2030
    volumes:
      - mariadb_data:/var/lib/mysql
      - ./schema.sql:/docker-entrypoint-initdb.d/schema.sql
    ports:
      - "3306:3306"
    healthcheck:
      test: ["CMD", "healthcheck.sh", "--connect", "--innodb_initialized"]
      interval: 10s
      timeout: 20s
      retries: 10

volumes:
  mariadb_data:
```

---

## 7. Partie III : Architecture et Implémentation

### 7.1 Architecture Logicielle (Architecture en Couches)

L'application suit une **architecture en couches** (Layered Architecture) qui sépare les responsabilités :

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                              ARCHITECTURE EN COUCHES                             │
└─────────────────────────────────────────────────────────────────────────────────┘

    ┌───────────────────────────────────────────────────────────────────────┐
    │                     COUCHE PRÉSENTATION (VIEW)                         │
    │  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐ ┌─────────────┐      │
    │  │LoginController│ │MainController│ │MatchController│ │TicketController│  │
    │  └─────────────┘ └─────────────┘ └─────────────┘ └─────────────┘      │
    │  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐ ┌─────────────┐      │
    │  │LoginView.fxml│ │MainView.fxml│ │MatchView.fxml│ │TicketView.fxml│   │
    │  └─────────────┘ └─────────────┘ └─────────────┘ └─────────────┘      │
    └───────────────────────────────────────────────────────────────────────┘
                                       │
                                       ▼
    ┌───────────────────────────────────────────────────────────────────────┐
    │                     COUCHE SERVICE (MÉTIER)                            │
    │  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐ ┌─────────────┐      │
    │  │ AuthService │ │TicketService│ │MatchService │ │UserTicketService│  │
    │  └─────────────┘ └─────────────┘ └─────────────┘ └─────────────┘      │
    │  ┌─────────────────────┐ ┌─────────────────────┐                       │
    │  │ NotificationService │ │  LanguageService    │                       │
    │  └─────────────────────┘ └─────────────────────┘                       │
    └───────────────────────────────────────────────────────────────────────┘
                                       │
                                       ▼
    ┌───────────────────────────────────────────────────────────────────────┐
    │                  COUCHE ACCÈS AUX DONNÉES (DAO)                        │
    │  ┌─────────────┐ ┌─────────────────┐ ┌─────────────┐                  │
    │  │  IDao<T>    │ │TicketDaoHibernate│ │MatchDaoHibernate│             │
    │  │ (Interface) │ └─────────────────┘ └─────────────┘                  │
    │  └─────────────┘ ┌─────────────┐                                       │
    │                  │   UserDao   │                                       │
    │                  └─────────────┘                                       │
    └───────────────────────────────────────────────────────────────────────┘
                                       │
                                       ▼
    ┌───────────────────────────────────────────────────────────────────────┐
    │                      COUCHE MODÈLE (ENTITÉS)                           │
    │  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐                      │
    │  │    User     │ │   Match     │ │   Ticket    │                      │
    │  └─────────────┘ └─────────────┘ └─────────────┘                      │
    └───────────────────────────────────────────────────────────────────────┘
                                       │
                                       ▼
    ┌───────────────────────────────────────────────────────────────────────┐
    │                     COUCHE UTILITAIRES                                 │
    │  ┌─────────────────┐ ┌─────────────────────┐                          │
    │  │  HibernateUtil  │ │ DatabaseConnection  │                          │
    │  │   (Singleton)   │ └─────────────────────┘                          │
    │  └─────────────────┘                                                   │
    └───────────────────────────────────────────────────────────────────────┘
                                       │
                                       ▼
    ┌───────────────────────────────────────────────────────────────────────┐
    │                    BASE DE DONNÉES (MySQL/H2)                          │
    └───────────────────────────────────────────────────────────────────────┘
```

#### Structure des Packages

```
com.mondial.ticket/
├── model/          # Entités JPA/Hibernate (User, Match, Ticket)
├── dao/            # Data Access Objects - Accès base de données
├── service/        # Logique métier et règles de gestion
├── view/           # Contrôleurs JavaFX (MVC)
├── util/           # Classes utilitaires (HibernateUtil)
├── exception/      # Exceptions personnalisées
└── repository/     # Repository pattern (alternative DAO)
```

---

### 7.2 Design Patterns Utilisés

#### 7.2.1 Pattern Singleton - HibernateUtil

**Objectif :** Garantir une instance unique de la SessionFactory pour toute l'application.

**Justification :** La création d'une SessionFactory est coûteuse en ressources. Le Singleton permet de réutiliser la même instance.

```java
/**
 * Utilitaire Hibernate pour la gestion de la SessionFactory.
 * Pattern Singleton : Une seule instance partagée dans toute l'application.
 */
public class HibernateUtil {
    // Instance unique (eager initialization)
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            Configuration configuration = new Configuration().configure();
            
            // Support Docker : override URL si variable d'environnement définie
            String dbHost = System.getenv("DB_HOST");
            if (dbHost != null) {
                String url = "jdbc:mysql://" + dbHost + ":3306/mondial2030";
                configuration.setProperty("hibernate.connection.url", url);
            }
            
            return configuration.buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    // Point d'accès global
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        getSessionFactory().close();
    }
}
```

#### 7.2.2 Pattern DAO (Data Access Object)

**Objectif :** Isoler le code d'accès aux données du reste de l'application.

**Justification :** Permet de changer de technologie de persistance (JDBC → Hibernate) sans impacter les services.

```java
/**
 * Interface DAO générique pour les opérations CRUD.
 * @param <T> Le type d'entité
 */
public interface IDao<T> {
    void create(T element);
    List<T> readAll();
    T readByName(String nom) throws TicketException;
    void update(T element) throws TicketException;
    void delete(String nom) throws TicketException;
}

/**
 * Implémentation Hibernate du DAO pour les tickets.
 */
public class TicketDaoHibernate implements IDao<Ticket> {

    @Override
    public void create(Ticket ticket) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(ticket);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.err.println("Erreur Hibernate : " + e.getMessage());
        }
    }

    @Override
    public List<Ticket> readAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Ticket", Ticket.class).list();
        }
    }
    
    // ... autres méthodes CRUD
}
```

#### 7.2.3 Pattern Service Layer

**Objectif :** Encapsuler la logique métier dans des services dédiés.

**Justification :** Séparation des préoccupations entre présentation et métier.

```java
/**
 * Service d'authentification - Gestion des utilisateurs et sessions.
 * Combine Singleton + Service Layer.
 */
public class AuthService {
    private static AuthService instance;
    private UserDao userDao = new UserDao();
    private User currentUser = null;

    private AuthService() {
        userDao.initializeDefaultUsers();
    }

    public static AuthService getInstance() {
        if (instance == null) {
            instance = new AuthService();
        }
        return instance;
    }

    public boolean login(String username, String password) {
        User user = userDao.authenticate(username, password);
        if (user != null) {
            currentUser = user;
            return true;
        }
        return false;
    }

    public boolean isAdmin() {
        return currentUser != null && currentUser.isAdmin();
    }
    
    public boolean register(String username, String password, String nom, String email) {
        if (userDao.usernameExists(username)) {
            return false;
        }
        User newUser = new User(username, password, "USER", nom, email);
        return userDao.create(newUser);
    }
}
```

#### 7.2.4 Récapitulatif des Patterns

| Pattern | Classe | Avantage |
|---------|--------|----------|
| **Singleton** | HibernateUtil, AuthService | Économie de ressources, point d'accès unique |
| **DAO** | IDao, TicketDaoHibernate, UserDao | Abstraction de la persistance |
| **Service Layer** | TicketService, AuthService | Séparation logique métier |
| **MVC** | Controllers + FXML | Séparation présentation/logique |

---

### 7.3 Extraits de Code Clés

#### 7.3.1 Entité Ticket avec Mapping Hibernate

```java
/**
 * Modèle représentant un ticket pour un match du Mondial 2030.
 */
public class Ticket {
    private int id;
    private String nomMatch;
    private String categorie; // VIP, Standard, Tribune
    private double prix;
    private Match match;
    private String status; // DISPONIBLE, VENDU, RESERVE
    private String acheteur;

    public Ticket() {
        this.status = "DISPONIBLE";
    }

    public Ticket(String nomMatch, String categorie, double prix) {
        this.nomMatch = nomMatch;
        this.categorie = categorie;
        this.prix = prix;
        this.status = "DISPONIBLE";
    }

    @Override
    public String toString() {
        return nomMatch + " [" + categorie + "] - " + prix + "€ (" + status + ")";
    }
    
    // Getters et Setters...
}
```

#### 7.3.2 Service d'Achat de Ticket avec Notification

```java
/**
 * Acheter un ticket - marque le ticket comme vendu et envoie une notification.
 */
public void acheterTicket(String nomMatch, String acheteur) throws TicketException {
    Ticket t = dao.readByName(nomMatch);
    
    if (t == null) {
        throw new TicketException("Ticket non trouvé: " + nomMatch);
    }
    if ("VENDU".equals(t.getStatus())) {
        throw new TicketException("Ce ticket est déjà vendu!");
    }
    
    // Mise à jour du statut
    t.setStatus("VENDU");
    t.setAcheteur(acheteur);
    dao.update(t);

    // Simulation d'envoi d'email de confirmation
    System.out.println("📧 EMAIL DE CONFIRMATION ENVOYÉ");
    System.out.println("Destinataire: " + acheteur);
    System.out.println("Match: " + t.getNomMatch());
    System.out.println("Catégorie: " + t.getCategorie());
    System.out.println("Prix: " + t.getPrix() + "€");
}
```

#### 7.3.3 Gestion des Transactions Hibernate (Try-with-resources)

```java
@Override
public void update(Ticket ticket) throws TicketException {
    Transaction transaction = null;
    // Try-with-resources : fermeture automatique de la session
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
        transaction = session.beginTransaction();
        session.merge(ticket);
        transaction.commit();
    } catch (Exception e) {
        // Rollback en cas d'erreur
        if (transaction != null) {
            transaction.rollback();
        }
        throw new TicketException("Erreur lors de la mise à jour.");
    }
}
```

#### 7.3.4 Contrôle d'Accès Basé sur les Rôles

```java
/**
 * Configure l'accès selon le rôle de l'utilisateur connecté.
 */
private void setupUserAccess() {
    User user = authService.getCurrentUser();
    boolean isAdmin = authService.isAdmin();

    // Afficher le nom avec l'icône appropriée
    if (userLabel != null && user != null) {
        String roleIcon = user.isAdmin() ? "👑" : "👤";
        userLabel.setText(roleIcon + " " + user.getNom());
    }

    // Masquer le menu admin pour les utilisateurs normaux
    if (adminMenu != null) {
        adminMenu.setVisible(isAdmin);
    }

    // Masquer les fonctionnalités admin
    if (exportDataMenuItem != null) {
        exportDataMenuItem.setVisible(isAdmin);
    }
}
```

---

## 8. Partie IV : Interface Utilisateur et Tests

### 8.1 Présentation des Interfaces

#### 8.1.1 Page de Connexion (Login)

**Description :** Interface d'authentification permettant aux utilisateurs de se connecter ou de s'inscrire.

**Fonctionnalités :**
- Champs username et mot de passe
- Bouton de connexion avec validation
- Lien vers l'inscription pour les nouveaux utilisateurs
- Messages d'erreur en cas d'échec

```
┌─────────────────────────────────────────────────────────┐
│                  🏆 MONDIAL 2030                        │
│              Plateforme de Billetterie                  │
├─────────────────────────────────────────────────────────┤
│                                                         │
│    ┌─────────────────────────────────────────────┐      │
│    │ 👤 Nom d'utilisateur                        │      │
│    │ ┌─────────────────────────────────────────┐ │      │
│    │ │ admin                                   │ │      │
│    │ └─────────────────────────────────────────┘ │      │
│    └─────────────────────────────────────────────┘      │
│                                                         │
│    ┌─────────────────────────────────────────────┐      │
│    │ 🔒 Mot de passe                             │      │
│    │ ┌─────────────────────────────────────────┐ │      │
│    │ │ ********                                │ │      │
│    │ └─────────────────────────────────────────┘ │      │
│    └─────────────────────────────────────────────┘      │
│                                                         │
│         ┌──────────────────────────────┐                │
│         │       🔓 SE CONNECTER        │                │
│         └──────────────────────────────┘                │
│                                                         │
│         Pas encore inscrit ? S'inscrire                 │
│                                                         │
└─────────────────────────────────────────────────────────┘
```

#### 8.1.2 Dashboard Principal

**Description :** Interface principale affichant les statistiques et les actions disponibles.

**Fonctionnalités :**
- Statistiques en temps réel (tickets vendus, disponibles, revenus)
- Horloge en temps réel
- Menu de navigation
- Zone de notifications
- Accès aux différentes sections

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│  🏆 Mondial 2030   │ Matchs │ Tickets │ 👑 Admin │ Aide │    👑 Administrateur  │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                                 │
│  ┌─────────────────────────────────────────────────────────────────────────┐   │
│  │                        📊 TABLEAU DE BORD                               │   │
│  │  ┌───────────────┐ ┌───────────────┐ ┌───────────────┐ ┌───────────────┐│   │
│  │  │ 🎫 Total      │ │ ✅ Vendus     │ │ 📦 Disponibles│ │ 💰 Revenus   ││   │
│  │  │     150       │ │      45       │ │     105       │ │  15,750€     ││   │
│  │  └───────────────┘ └───────────────┘ └───────────────┘ └───────────────┘│   │
│  └─────────────────────────────────────────────────────────────────────────┘   │
│                                                                                 │
│  ┌─────────────────────────────────────────────────────────────────────────┐   │
│  │ 🔔 Notification: ✨ Bienvenue Administrateur!                           │   │
│  └─────────────────────────────────────────────────────────────────────────┘   │
│                                                                                 │
│  ┌────────────────────────────────────────────┐                                 │
│  │        ACTIONS RAPIDES                      │                                │
│  │  ┌──────────────┐  ┌──────────────┐        │                                │
│  │  │ 🎫 Acheter   │  │ 📋 Mes       │        │                                │
│  │  │    Ticket    │  │   Tickets    │        │                                │
│  │  └──────────────┘  └──────────────┘        │                                │
│  │  ┌──────────────┐  ┌──────────────┐        │                                │
│  │  │ 🎲 Tirage    │  │ 📊 Stats     │        │                                │
│  │  │   au Sort    │  │              │        │                                │
│  │  └──────────────┘  └──────────────┘        │                                │
│  └────────────────────────────────────────────┘                                 │
│                                                                     🕐 14:35:22│
└─────────────────────────────────────────────────────────────────────────────────┘
```

#### 8.1.3 Gestion des Matchs (Admin)

**Description :** Interface CRUD pour la gestion des matchs.

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                           ⚽ GESTION DES MATCHS                                  │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                                 │
│  ┌────────────────────────────────────────────────────────────────────────────┐│
│  │ ID │ Équipe 1  │ Équipe 2  │ Stade              │ Ville      │ Phase      ││
│  ├────┼───────────┼───────────┼────────────────────┼────────────┼────────────┤│
│  │ 1  │ Maroc     │ Espagne   │ Grand Stade        │ Casablanca │ Groupe     ││
│  │ 2  │ Portugal  │ France    │ Estádio da Luz     │ Lisbonne   │ Groupe     ││
│  │ 3  │ Brésil    │ Argentine │ Santiago Bernabéu  │ Madrid     │ Demi-finale││
│  │ 4  │ Allemagne │ Italie    │ Camp Nou           │ Barcelone  │ Quart      ││
│  │ 5  │ Maroc     │ Portugal  │ Stade Mohammed V   │ Rabat      │ Finale     ││
│  └────────────────────────────────────────────────────────────────────────────┘│
│                                                                                 │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐        │
│  │ ➕ Ajouter   │  │ ✏️ Modifier  │  │ ❌ Supprimer │  │ 🔄 Actualiser│        │
│  └──────────────┘  └──────────────┘  └──────────────┘  └──────────────┘        │
│                                                                                 │
└─────────────────────────────────────────────────────────────────────────────────┘
```

#### 8.1.4 Gestion des Tickets (Admin et User)

**Description :** Interface de gestion des tickets avec fonctionnalités différenciées selon le rôle.

**Fonctionnalités Admin :**
- Ajouter des tickets pour des matchs existants (dropdown dynamique)
- Supprimer des tickets
- Voir les statistiques
- Exporter en CSV

**Fonctionnalités User :**
- Filtrer par match (dropdown dynamique depuis la base de données)
- Filtrer par catégorie (VIP, Standard, Tribune)
- Recherche dynamique en temps réel
- Acheter un ticket disponible
- Voir "Mes Tickets" (historique d'achat)

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                           🎫 BILLETTERIE DU MONDIAL 2030                         │
├─────────────────────────────────────────────────────────────────────────────────┤
│ Admin: [Choisir un match ▼] [Standard ▼] [Prix] [Ajouter Ticket]               │
├─────────────────────────────────────────────────────────────────────────────────┤
│ Choisir un match: [Maroc vs Espagne ▼] Catégorie: [Tous ▼] [Voir Tous]         │
├─────────────────────────────────────────────────────────────────────────────────┤
│ Sélectionnez un ticket et cliquez sur Acheter (ou double-cliquez)              │
├────────────────────────────────────────────────────────────────────────────────┤
│ │ Match            │ Catégorie │ Prix    │ Statut      │ Acheteur            │ │
│ ├──────────────────┼───────────┼─────────┼─────────────┼─────────────────────┤ │
│ │ Maroc vs Espagne │ VIP       │ 500.00€ │ DISPONIBLE  │                     │ │
│ │ Maroc vs Espagne │ Standard  │ 150.00€ │ VENDU       │ Ahmed               │ │
│ │ Maroc vs Espagne │ Tribune   │ 75.00€  │ DISPONIBLE  │                     │ │
│ │ Portugal vs France│ VIP      │ 450.00€ │ DISPONIBLE  │                     │ │
├─────────────────────────────────────────────────────────────────────────────────┤
│ Acheter le ticket sélectionné: [Acheter] (Connecté en tant que: Ahmed)         │
├─────────────────────────────────────────────────────────────────────────────────┤
│ Recherche: [________________] [Rechercher]  [Supprimer] [Stats] [Export]       │
├─────────────────────────────────────────────────────────────────────────────────┤
│ 🎫 Maroc vs Espagne: 2 ticket(s) disponible(s) sur 3                           │
└─────────────────────────────────────────────────────────────────────────────────┘
```

**Caractéristiques techniques :**
- **Recherche dynamique** : Filtre en temps réel pendant la saisie
- **Dropdown de matchs** : Alimenté depuis la table `matchs` (pas les tickets)
- **Validation admin** : Un ticket ne peut être créé que pour un match existant
- **Cascade delete** : La suppression d'un match supprime tous ses tickets

---

### 8.2 Scénarios de Test

#### 8.2.1 Tests Nominaux (Cas Passants)

| # | Scénario | Données d'entrée | Résultat attendu | Statut |
|---|----------|------------------|------------------|--------|
| 1 | Connexion Admin | username: admin, password: admin123 | Accès dashboard avec menu admin visible | ✅ Passé |
| 2 | Connexion User | username: user, password: user123 | Accès dashboard sans menu admin | ✅ Passé |
| 3 | Inscription | Nouveau user avec données valides | Compte créé, redirection vers login | ✅ Passé |
| 4 | Consultation matchs | Aucune | Liste des matchs affichée | ✅ Passé |
| 5 | Achat ticket | Sélection match + catégorie VIP | Ticket acheté, ajouté à "Mes Tickets" | ✅ Passé |
| 6 | Code promo | Code: MONDIAL2030 | -20% sur le prix du ticket | ✅ Passé |
| 7 | Création match (Admin) | Maroc vs USA, Stade Rabat | Match ajouté à la liste | ✅ Passé |
| 8 | Export CSV (Admin) | Clic sur Export | Fichier CSV généré | ✅ Passé |
| 9 | Recherche dynamique | Taper "Maroc" | Filtrage instantané | ✅ Passé |
| 10 | Dropdown matchs dynamique | Créer nouveau match | Match apparaît dans dropdown | ✅ Passé |
| 11 | Cascade delete | Supprimer match | Tous les tickets du match supprimés | ✅ Passé |

#### 8.2.2 Tests d'Erreurs (Cas Limites)

| # | Scénario | Données d'entrée | Résultat attendu | Statut |
|---|----------|------------------|------------------|--------|
| 1 | Login invalide | Mauvais mot de passe | Alert "Identifiants incorrects" | ✅ Passé |
| 2 | Username existant | Inscription avec username existant | Alert "Nom d'utilisateur déjà pris" | ✅ Passé |
| 3 | Ticket déjà vendu | Achat d'un ticket VENDU | Alert "Ce ticket est déjà vendu!" | ✅ Passé |
| 4 | Code promo invalide | Code: FAUX123 | Alert "Code promo invalide" | ✅ Passé |
| 5 | Champs vides | Login avec champs vides | Alert "Veuillez remplir tous les champs" | ✅ Passé |
| 6 | Suppression match (User) | Tentative suppression | Menu non visible (contrôle d'accès) | ✅ Passé |
| 7 | Ticket sans match | Admin: aucun match sélectionné | Alert "Veuillez sélectionner un match" | ✅ Passé |
| 8 | Ticket match inexistant | Match supprimé entre-temps | Alert "Le match n'existe pas" | ✅ Passé |

---

## 9. Conclusion et Perspectives

### 9.1 Bilan Technique

Le projet **Mondial 2030 - Plateforme de Gestion des Tickets** répond intégralement au cahier des charges initial :

✅ **Authentification sécurisée** avec gestion des rôles (Admin/User)  
✅ **Inscription des utilisateurs** avec persistance en base de données  
✅ **Gestion complète des matchs** (CRUD avec cascade delete des tickets)  
✅ **Système de billetterie** avec catégories (VIP, Standard, Tribune)  
✅ **Dropdown dynamique** des matchs depuis la base de données  
✅ **Recherche en temps réel** (filtrage dynamique pendant la saisie)  
✅ **Validation admin** : tickets uniquement pour matchs existants  
✅ **Achat de tickets** avec nom d'utilisateur automatique  
✅ **Historique des achats** persisté en base ("Mes Tickets")  
✅ **Tickets gagnés** au tirage au sort stockés en base  
✅ **Dashboard statistiques** en temps réel  
✅ **Interface JavaFX** moderne et ergonomique  
✅ **Persistance MariaDB** via Hibernate ORM  
✅ **Docker Compose** pour l'environnement de base de données  
✅ **Architecture en couches** maintenable  

### 9.2 Compétences Acquises

| Domaine | Compétences |
|---------|-------------|
| **Java Avancé** | ORM Hibernate, API Streams, Lambdas, Generics |
| **Design Patterns** | Singleton, DAO, Service Layer, MVC |
| **IHM** | JavaFX, FXML, CSS, Controllers, Listeners dynamiques |
| **Base de données** | MariaDB, H2, SQL, Transactions, Foreign Keys, Cascade |
| **Outils** | Maven, Git, GitHub, Docker, IntelliJ IDEA |
| **Architecture** | Conception en couches, modularité, relations entités |

### 9.3 Difficultés Rencontrées et Solutions

| Difficulté | Solution Apportée |
|------------|-------------------|
| Configuration Hibernate 6.x avec Java 17 | Migration vers les nouvelles API Jakarta |
| Gestion des sessions Hibernate | Utilisation de try-with-resources |
| Contrôle d'accès par rôle | Implémentation d'un AuthService centralisé |
| Persistance des tickets achetés | Service UserTicketService avec PurchasedTicketDao |
| Cascade delete Match → Tickets | Configuration Hibernate cascade="all-delete-orphan" |
| Dropdown dynamique des matchs | Chargement depuis MatchService au lieu des tickets |
| Recherche en temps réel | Listener sur textProperty du TextField |
| Conteneurisation MariaDB | Docker Compose avec healthcheck |

### 9.4 Perspectives et Améliorations Futures

1. **Version Web (Spring Boot)** : Transformation en application web accessible depuis n'importe quel navigateur

2. **Application Mobile** : Développement d'une app Android/iOS pour la réservation

3. **Paiement en ligne** : Intégration de passerelles de paiement (Stripe, PayPal)

4. **QR Code** : Génération de QR codes sur les billets pour validation à l'entrée

5. **Notifications Push** : Alertes en temps réel pour les nouveaux matchs

6. **Analytics avancées** : Tableaux de bord avec graphiques (JFreeChart)

7. **Multi-langue** : Support complet FR/EN/AR/ES

8. **Tests unitaires** : Couverture JUnit/Mockito pour les services

---

## 10. Webographie / Bibliographie

### Documentation Officielle
- [Oracle Java Documentation](https://docs.oracle.com/en/java/) - Documentation officielle Java 17
- [Hibernate ORM Documentation](https://hibernate.org/orm/documentation/6.2/) - Guide Hibernate 6.2
- [JavaFX Documentation](https://openjfx.io/openjfx-docs/) - Documentation OpenJFX
- [Maven Documentation](https://maven.apache.org/guides/) - Guide Apache Maven

### Tutoriels et Cours
- [Baeldung - Hibernate Tutorial](https://www.baeldung.com/hibernate-5-spring) - Tutoriels Hibernate
- [JetBrains Academy](https://www.jetbrains.com/academy/) - Cours Java avancé
- [YouTube - Derek Banas](https://www.youtube.com/user/deaboranas) - Tutoriels Design Patterns

### Ressources Techniques
- [Stack Overflow](https://stackoverflow.com/) - Résolution de problèmes techniques
- [GitHub](https://github.com/) - Hébergement du code source
- [Docker Hub](https://hub.docker.com/) - Images Docker MariaDB

---

<div align="center">

## 📋 Informations du Projet

| Élément | Valeur |
|---------|--------|
| **Titre du Projet** | Plateforme Intelligente de Gestion des Tickets du Mondial 2030 |
| **Technologies** | Java 17, JavaFX, Hibernate 6.2, MariaDB 10.11, Maven, Docker |
| **Lien GitHub** | [https://github.com/AYMAN-hr/mondial2030](https://github.com/AYMAN-hr/mondial2030) |
| **Base de données** | MariaDB 10.11 (Docker) |
| **Année** | 2025-2026 |

---

**© 2025-2026 - EMSI - Projet Java Avancé - 4IIR**

</div>

