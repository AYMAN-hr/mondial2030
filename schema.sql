-- Base de données pour la Plateforme de Gestion des Tickets du Mondial 2030
CREATE DATABASE IF NOT EXISTS mondial2030;
USE mondial2030;

-- Table des utilisateurs
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'USER',
    nom VARCHAR(100),
    email VARCHAR(100)
);

-- Table des matchs
CREATE TABLE IF NOT EXISTS matchs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    equipe1 VARCHAR(100) NOT NULL,
    equipe2 VARCHAR(100) NOT NULL,
    stade VARCHAR(200) NOT NULL,
    ville VARCHAR(100) NOT NULL,
    pays VARCHAR(50) NOT NULL,
    phase VARCHAR(50) NOT NULL,
    date_heure DATETIME
);

-- Table des tickets
CREATE TABLE IF NOT EXISTS tickets (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom_match VARCHAR(200) NOT NULL,
    categorie VARCHAR(50) NOT NULL,
    prix DOUBLE NOT NULL,
    status VARCHAR(20) DEFAULT 'DISPONIBLE',
    acheteur VARCHAR(100),
    match_id INT,
    FOREIGN KEY (match_id) REFERENCES matchs(id) ON DELETE CASCADE
);

-- Table des tickets achetés (historique des achats)
CREATE TABLE IF NOT EXISTS purchased_tickets (
    id INT AUTO_INCREMENT PRIMARY KEY,
    ticket_id VARCHAR(50) NOT NULL,
    username VARCHAR(50) NOT NULL,
    match_name VARCHAR(200) NOT NULL,
    category VARCHAR(50) NOT NULL,
    price DOUBLE NOT NULL,
    purchase_date DATETIME NOT NULL,
    qr_code VARCHAR(50),
    status VARCHAR(20) DEFAULT 'VALID',
    original_ticket_id INT,
    FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE,
    FOREIGN KEY (original_ticket_id) REFERENCES tickets(id) ON DELETE SET NULL
);

-- Données par défaut pour les utilisateurs
INSERT INTO users (username, password, role, nom, email) VALUES
('admin', 'admin123', 'ADMIN', 'Administrateur', 'admin@mondial2030.com'),
('user', 'user123', 'USER', 'Utilisateur', 'user@mondial2030.com')
ON DUPLICATE KEY UPDATE username=username;

-- Données d'exemple pour les matchs
INSERT INTO matchs (equipe1, equipe2, stade, ville, pays, phase) VALUES
('Maroc', 'Espagne', 'Grand Stade de Casablanca', 'Casablanca', 'Maroc', 'Groupe'),
('Portugal', 'France', 'Estádio da Luz', 'Lisbonne', 'Portugal', 'Groupe'),
('Brésil', 'Argentine', 'Santiago Bernabéu', 'Madrid', 'Espagne', 'Demi-finale'),
('Allemagne', 'Italie', 'Camp Nou', 'Barcelone', 'Espagne', 'Quart'),
('Maroc', 'Portugal', 'Stade Mohammed V', 'Rabat', 'Maroc', 'Finale');

-- Données d'exemple pour les tickets
INSERT INTO tickets (nom_match, categorie, prix, status) VALUES
('Maroc vs Espagne', 'VIP', 500.00, 'DISPONIBLE'),
('Maroc vs Espagne', 'Standard', 150.00, 'DISPONIBLE'),
('Maroc vs Espagne', 'Tribune', 75.00, 'DISPONIBLE'),
('Portugal vs France', 'VIP', 450.00, 'DISPONIBLE'),
('Portugal vs France', 'Standard', 120.00, 'DISPONIBLE'),
('Brésil vs Argentine', 'VIP', 600.00, 'DISPONIBLE'),
('Brésil vs Argentine', 'Tribune', 75.00, 'DISPONIBLE'),
('Maroc vs Portugal', 'VIP', 1500.00, 'DISPONIBLE'),
('Maroc vs Portugal', 'Standard', 400.00, 'DISPONIBLE'),
('Maroc vs Portugal', 'Tribune', 150.00, 'DISPONIBLE');

