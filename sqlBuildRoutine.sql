CREATE TABLE Utilisateurs (
    email varchar(100) NOT NULL PRIMARY KEY,
    nom varchar(100) NOT NULL,
    prenom varchar(100) NOT NULL,
    hash_de_motdepasse varchar(100) NOT NULL,
    genre integer not null,
    dateDeNaissance date not null,
    lattitude decimal(9,6) not null,
    longitude decimal(9,6) not null,
    evaluation integer,
    CHECK (evaluation >= 0 AND evaluation <= 5)
);

INSERT INTO Utilisateurs VALUES ('james.bond@mi6.gov.co.uk', 'Bond', 'James', 'rrr', 1, TO_date('04/05/1985','dd/mm/yyyy'), 45.879865, 42.365165,null);
INSERT INTO Utilisateurs VALUES ('m@mi6.gov.co.uk', '*', 'M', 'bbb', '2', TO_date('11/12/1948','dd/mm/yyyy'), 45.879865, 42.365165, 5);

CREATE TABLE Evaluations (
    idEvaluation integer NOT NULL PRIMARY KEY,
    evaluation integer NOT NULL,
    dateEval date NOT NULL,
    commentaire varchar(300),
    CHECK (evaluation >= 0 AND evaluation <= 5)
);


CREATE TABLE Taches (
    idTache integer NOT NULL PRIMARY KEY,
    titreTache varchar(100)
);

INSERT INTO Taches VALUES (1, 'Essai');

CREATE TABLE TachesAtom (
    idTacheAtom integer NOT NULL PRIMARY KEY,
    titreTacheAtom varchar(100) NOT NULL,
    descriptionTache varchar(300) NOT NULL,
    prixTache decimal(9,2) NOT NULL,
    lattitude decimal(9,6) not null,
    longitude decimal(9,6) not null,
    datePlusTot date NOT NULL,
    datePlusTard date NOT NULL,
    idTacheMere integer foreign key references Taches(idTache),/*a revoir erreur/*
    CHECK (prixTache >= 0)
/*check dates plus tard que la date d'aujourd'hui*/
);

INSERT INTO TachesAtom VALUES (1, 'Escorte d''Elizabeth', 'Attention! La mission contient un saut d''hélicoptère', 100.15, 43, 45, TO_date('04/05/2016','dd/mm/yyyy'), TO_date('04/05/2017','dd/mm/yyyy'), 1);
INSERT INTO TachesAtom VALUES (2, 'Construire une application Web de crowsourcing', 'En équipe de 5', 0, 50, 62, TO_date('04/05/2016','dd/mm/yyyy'), TO_date('04/05/2017','dd/mm/yyyy'), 1);

CREATE TABLE Competences (
    nomComp varchar(100) NOT NULL PRIMARY KEY
);