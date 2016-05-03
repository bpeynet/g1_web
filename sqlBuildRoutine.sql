CREATE TABLE Utilisateurs (
    email varchar(100) NOT NULL PRIMARY KEY,
    nom varchar(100) NOT NULL,
    prenom varchar(100) NOT NULL,
    hash_de_motdepasse varchar(100) NOT NULL,
    genre integer not null,
    dateDeNaissance date not null,
    latitude double precision not null,
    longitude double precision not null,
    adresse varchar(100),
    evaluation float,
    rayon integer,
    CHECK (email like '%@%.%'),
    CHECK (genre = 1 OR genre = 2),
    CHECK (evaluation >= -1 AND evaluation <= 10),/*,
    CHECK (latitude >= 0 AND latitude <= 90),
    CHECK (longitude >= 0 AND longitude <= 360)*/
    CHECK (rayon > 0 OR rayon = -1) /*dans le cas ou la distance est "n'importe"*/
);

INSERT INTO Utilisateurs VALUES ('james.bond@mi6.gov.co.uk', 'Bond', 'James', '44f437ced647ec3f40fa0841041871cd', 1, TO_date('04/05/1985','dd/mm/yyyy'), 45.879865, 42.365165, null, -1, -1);
INSERT INTO Utilisateurs VALUES ('m@mi6.gov.co.uk', '*', 'M', '08f8e0260c64418510cefb2b06eee5cd', 2, TO_date('11/12/1948','dd/mm/yyyy'), 45.879865, 42.365165, 'Londres', -1, -1);
INSERT INTO Utilisateurs VALUES ('bpeynet@free.fr', 'PEYNET', 'Benoît', '47bce5c74f589f4867dbd57e9ca9f808', 1, TO_date('11/06/1994','dd/mm/yyyy'), 45.879865, 42.365165, 'Paris', -1, -1);
INSERT INTO Utilisateurs VALUES ('picsou@disney.com', 'Picsou', 'Balthazar', '77963b7a931377ad4ab5ad6a9cd718aa', 1, TO_date('11/06/1994','dd/mm/yyyy'), 45.879865, 42.365165, 'Donaldville', -1, -1);


CREATE TABLE Taches (
    idTache integer NOT NULL,
    titreTache varchar(100),
    idCommanditaire varchar(100) NOT NULL references Utilisateurs(email) ON DELETE CASCADE,
    PRIMARY KEY(idTache, idCommanditaire)
);

CREATE SEQUENCE Taches_Sequence
    INCREMENT BY 1
    START WITH 1
    NOMAXVALUE
    NOCYCLE;

INSERT INTO Taches VALUES (taches_sequence.nextval, 'Missions 00*','m@mi6.gov.co.uk');
INSERT INTO Taches VALUES (taches_sequence.nextval, 'Mon argent','picsou@disney.com');
INSERT INTO Taches VALUES (taches_sequence.nextval, 'Casino Royale','m@mi6.gov.co.uk');

CREATE TABLE TachesAtom (
    idTacheAtom integer NOT NULL,
    titreTacheAtom varchar(100) NOT NULL,
    descriptionTache varchar(300) NOT NULL,
    prixTache decimal(9,2) NOT NULL,
    latitude double precision NOT NULL,
    longitude double precision NOT NULL,
    datePlusTot date NOT NULL,
    datePlusTard date NOT NULL,
    idTacheMere integer NOT NULL,
    idCommanditaire varchar(100) NOT NULL,
    constraint FK_TAtom_T foreign key (idTacheMere, idCommanditaire)
        references Taches(idTache, idCommanditaire) ON DELETE CASCADE,
    idExecutant varchar(100) references Utilisateurs(email) ON DELETE SET NULL,
    indicateurFin integer,    
    PRIMARY KEY (idTacheAtom,idCommanditaire),
    CHECK (prixTache >= 0)
);

CREATE SEQUENCE TachesAtom_Sequence
    INCREMENT BY 1
    START WITH 1
    NOMAXVALUE
    NOCYCLE;

INSERT INTO TachesAtom VALUES (tachesAtom_sequence.NEXTVAL, 'Escorte d''Elizabeth',
    'Attention! La mission contient un saut d''hélicoptère', 100.15, 51.487270, -0.124559, TO_date('04/05/2016','dd/mm/yyyy'),
    TO_date('04/05/2017','dd/mm/yyyy'), 1, 'm@mi6.gov.co.uk', null,0);
INSERT INTO TachesAtom VALUES (tachesAtom_sequence.NEXTVAL, 'Eliminer SPECTRE',
    'Des rencontres perturbantes sont à prévoir.', 100.15, 51.487270, -0.124559, TO_date('04/05/2016','dd/mm/yyyy'),
    TO_date('04/05/2017','dd/mm/yyyy'), 1, 'm@mi6.gov.co.uk', null,0);
INSERT INTO TachesAtom VALUES (tachesAtom_sequence.NEXTVAL, 'Construire une application Web de crowdsourcing',
    'En équipe de 5', 0, 45.200666, 5.725531, TO_date('04/05/2016','dd/mm/yyyy'),
    TO_date('04/05/2017','dd/mm/yyyy'), 2, 'picsou@disney.com', null,0);
INSERT INTO TachesAtom VALUES (tachesAtom_sequence.NEXTVAL, 'Battre le Chiffre au poker',
    '6 millions de livres appartenant au gouvernement seront mis à disposition. A rendre.',
    5005,51.487270, -0.124559, TO_date('18/09/1995','dd/mm/yyyy'), TO_date('19/09/1995','dd/mm/yyyy'),
    3,'m@mi6.gov.co.uk', null,0);
INSERT INTO TachesAtom VALUES (tachesAtom_sequence.NEXTVAL, 'Rattrapper l''africain',
    'Savoir monter et sauter sur une grue',
    5005,51.487270, -0.124559, TO_date('18/09/1995','dd/mm/yyyy'), TO_date('19/09/1995','dd/mm/yyyy'),
    3,'m@mi6.gov.co.uk', null,0);
INSERT INTO TachesAtom VALUES (tachesAtom_sequence.NEXTVAL, 'Construire une niche pour Pluto',
    'En bois', 1, 45.200666, 5.725531, TO_date('18/09/1995','dd/mm/yyyy'), TO_date('19/09/1995','dd/mm/yyyy'),
    2,'picsou@disney.com', null,0);

CREATE TABLE Competences (
    competence varchar(100) NOT NULL PRIMARY KEY
);

INSERT INTO Competences VALUES ('bricolage');
INSERT INTO Competences VALUES ('couture');
INSERT INTO Competences VALUES ('arts martiaux');
INSERT INTO Competences VALUES ('informatique');
INSERT INTO Competences VALUES ('plomberie');
INSERT INTO Competences VALUES ('cuisine');
INSERT INTO Competences VALUES ('chant');
INSERT INTO Competences VALUES ('jardinage');

CREATE TABLE CompetencesUtilisateurs (
    idUtilisateur varchar(100) NOT NULL references Utilisateurs(email) ON DELETE CASCADE,
    competence varchar(100) NOT NULL references Competences(competence) ON DELETE CASCADE,
    PRIMARY KEY (idUtilisateur, competence)
);

INSERT INTO CompetencesUtilisateurs VALUES ('james.bond@mi6.gov.co.uk','arts martiaux');
INSERT INTO CompetencesUtilisateurs VALUES ('picsou@disney.com','cuisine');
INSERT INTO CompetencesUtilisateurs VALUES ('bpeynet@free.fr','cuisine');
INSERT INTO CompetencesUtilisateurs VALUES ('bpeynet@free.fr','chant');
INSERT INTO CompetencesUtilisateurs VALUES ('bpeynet@free.fr','arts martiaux');

CREATE SEQUENCE CompUtilisateurs_Sequence
    INCREMENT BY 1
    START WITH 1
    NOMAXVALUE
    NOCYCLE;

CREATE TABLE CompetencesTaches (
    idTacheAtom integer NOT NULL,
    idCommanditaire varchar(100) NOT NULL,
    --On est obligé d'ajouter la colonne idCommanditaire
    constraint FK_CompT_TAtom foreign key (idTacheAtom,idCommanditaire)
        references TachesAtom(idTacheAtom,idCommanditaire) ON DELETE CASCADE,
    competence varchar(100) NOT NULL references Competences(competence) ON DELETE CASCADE
);

INSERT INTO CompetencesTaches VALUES (1, 'm@mi6.gov.co.uk','arts martiaux');
INSERT INTO CompetencesTaches VALUES (3, 'picsou@disney.com','arts martiaux');

CREATE TABLE Candidatures (
    idTacheAtom integer NOT NULL,
    idCommanditaire varchar(100) NOT NULL,
    --On est obligé d'ajouter la colonne idCommanditaire
    --car pour associer
    --un candidat à une tâche, on a besoin d'au moins idTacheAtom
    --mais on ne peut pas séparer idTacheAtom des deux autres propriétés.
    constraint FK_Cand_TAtom foreign key (idTacheAtom,idCommanditaire)
        references TachesAtom(idTacheAtom,idCommanditaire) ON DELETE CASCADE,
    idCandidat varchar(100) NOT NULL references Utilisateurs(email) ON DELETE CASCADE,
    PRIMARY KEY (idTacheAtom,idCommanditaire,idCandidat)
);

CREATE TABLE Evaluations (
    idTache integer NOT NULL PRIMARY KEY,
    evaluation integer NOT NULL,
    dateEval date NOT NULL,
    idEvaluateur varchar(100) NOT NULL,
    idEvalue varchar(100) NOT NULL,
    --constraint FK_Ev foreign key (idTache,idEvaluateur,idEvalue)
        --references Candidatures(idTacheAtom,idCommanditaire,idCandidat) ON DELETE SET NULL,
    --Cette contrainte d'intégrité est vitale afin de vérifier que l'évaluation
    --est donnée pour une tâche vraiment proposée par le bon commanditaire associé
    --au bon exécutant.
    constraint FK_Ev_Taches foreign key (idTache, idEvaluateur)
        references Taches(idTache, idCommanditaire) ON DELETE CASCADE,
    commentaire varchar(300),
    CHECK (evaluation >= 0 AND evaluation <= 10),
    CHECK (idEvalue != idEvaluateur)
);