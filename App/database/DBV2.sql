CREATE TABLE Utilisateur (
    id              INT PRIMARY KEY AUTO_INCREMENT,
    nom             VARCHAR(100)        NOT NULL,
    email           VARCHAR(150)        NOT NULL UNIQUE,
    mot_de_passe    VARCHAR(255)        NOT NULL,
    telephone       VARCHAR(20),
    role            ENUM('client', 'pharmacie') NOT NULL,
    date_inscription DATE               DEFAULT (CURRENT_DATE)
);
 
CREATE TABLE Client (
    id          INT PRIMARY KEY,
    adresse     VARCHAR(255),
    latitude    DOUBLE,
    longitude   DOUBLE,
    FOREIGN KEY (id) REFERENCES Utilisateur(id) ON DELETE CASCADE
);
 
CREATE TABLE Pharmacie (
    id                  INT PRIMARY KEY,
    nom_pharmacie       VARCHAR(150)    NOT NULL,
    adresse             VARCHAR(255)    NOT NULL,
    latitude            DOUBLE          NOT NULL,
    longitude           DOUBLE          NOT NULL,
    numero_pharmacie    VARCHAR(50)     UNIQUE,
    horaires            VARCHAR(255),
    FOREIGN KEY (id) REFERENCES Utilisateur(id) ON DELETE CASCADE
);
 
CREATE TABLE Medicament (
    id          INT PRIMARY KEY AUTO_INCREMENT,
    nom         VARCHAR(150)    NOT NULL,
    code_barre  VARCHAR(50)     UNIQUE,
    description TEXT,
    categorie   VARCHAR(100),
    fabricant   VARCHAR(100)
);
 
CREATE TABLE Catalogue (
    id          INT PRIMARY KEY AUTO_INCREMENT,
    version     VARCHAR(20),
    date_maj    DATE
);
 
CREATE TABLE Catalogue_Medicament (
    catalogue_id    INT,
    medicament_id   INT,
    PRIMARY KEY (catalogue_id, medicament_id),
    FOREIGN KEY (catalogue_id)  REFERENCES Catalogue(id)   ON DELETE CASCADE,
    FOREIGN KEY (medicament_id) REFERENCES Medicament(id)  ON DELETE CASCADE
);
 
CREATE TABLE Stock (
    id                  INT PRIMARY KEY AUTO_INCREMENT,
    pharmacie_id        INT             NOT NULL,
    medicament_id       INT             NOT NULL,
    quantite            INT             NOT NULL DEFAULT 0,
    prix                DECIMAL(10,3)   NOT NULL,
    delai_reservation   INT             DEFAULT 24,
    date_maj            DATETIME        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE (pharmacie_id, medicament_id),
    FOREIGN KEY (pharmacie_id)  REFERENCES Pharmacie(id)   ON DELETE CASCADE,
    FOREIGN KEY (medicament_id) REFERENCES Medicament(id)  ON DELETE CASCADE
);
 
CREATE TABLE Reservation (
    id                  INT PRIMARY KEY AUTO_INCREMENT,
    client_id           INT             NOT NULL,
    stock_id            INT             NOT NULL,
    quantite            INT             NOT NULL DEFAULT 1,
    date_reservation    DATETIME        DEFAULT CURRENT_TIMESTAMP,
    date_expiration     DATETIME,
    statut              ENUM('EN_ATTENTE', 'CONFIRMEE', 'ANNULEE', 'COMPLETEE', 'EXPIREE')
                        NOT NULL DEFAULT 'EN_ATTENTE',
    FOREIGN KEY (client_id) REFERENCES Client(id)   ON DELETE CASCADE,
    FOREIGN KEY (stock_id)  REFERENCES Stock(id)    ON DELETE CASCADE
);
 
CREATE TABLE Notification (
    id          INT PRIMARY KEY AUTO_INCREMENT,
    reservation_id  INT             NOT NULL,
    message     TEXT                NOT NULL,
    type        ENUM('CONFIRMATION', 'ANNULATION', 'RAPPEL', 'COMPLETION') NOT NULL,
    date_envoi  DATETIME            DEFAULT CURRENT_TIMESTAMP,
    lu          BOOLEAN             DEFAULT FALSE,
    FOREIGN KEY (reservation_id) REFERENCES Reservation(id) ON DELETE CASCADE
);