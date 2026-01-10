-- Base de données pour la Plateforme de Gestion des Tickets du Mondial 2030
CREATE DATABASE IF NOT EXISTS mondial2030;
USE mondial2030;

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
    FOREIGN KEY (match_id) REFERENCES matchs(id) ON DELETE SET NULL
);

-- Données d'exemple pour les matchs
INSERT INTO matchs (equipe1, equipe2, stade, ville, pays, phase) VALUES
('Maroc', 'Espagne', 'Grand Stade de Casablanca', 'Casablanca', 'Maroc', 'Groupe'),
('Portugal', 'France', 'Estádio da Luz', 'Lisbonne', 'Portugal', 'Groupe'),
('Brésil', 'Argentine', 'Santiago Bernabéu', 'Madrid', 'Espagne', 'Demi-finale'),
('Allemagne', 'Italie', 'Camp Nou', 'Barcelone', 'Espagne', 'Quart'),
('Maroc', 'Portugal', 'Stade Mohammed V', 'Rabat', 'Maroc', 'Finale');

-- Données d'exemple pour les tickets
INSERT INTO tickets (nom_match, categorie, prix) VALUES
('Maroc vs Espagne', 'VIP', 500.00),
('Maroc vs Espagne', 'Standard', 150.00),
('Portugal vs France', 'VIP', 450.00),
('Brésil vs Argentine', 'Tribune', 75.00),
('Finale Mondial 2030', 'VIP', 1500.00);

