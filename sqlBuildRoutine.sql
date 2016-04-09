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
    CHECK (email like '%@%.%'),
    CHECK (genre = 1 OR genre = 2),
    CHECK (evaluation >= -1 AND evaluation <= 5),
    CHECK (latitude >= 0 AND latitude <= 90),
    CHECK (longitude >= 0 AND longitude <= 360)
);

INSERT INTO Utilisateurs VALUES ('james.bond@mi6.gov.co.uk', 'Bond', 'James', 'rrr', 1, TO_date('04/05/1985','dd/mm/yyyy'), 45.879865, 42.365165, null, -1);
INSERT INTO Utilisateurs VALUES ('m@mi6.gov.co.uk', '*', 'M', 'bbb', 2, TO_date('11/12/1948','dd/mm/yyyy'), 45.879865, 42.365165, null, -1);
INSERT INTO Utilisateurs VALUES ('bpeynet@free.fr', 'PEYNET', 'Benoît', 'aaa', 1, TO_date('11/06/1994','dd/mm/yyyy'), 45.879865, 42.365165, null, -1);
INSERT INTO Utilisateurs VALUES ('picsou@disney.com', 'Picsou', 'Archibald', 'ddd', 1, TO_date('11/06/1994','dd/mm/yyyy'), 45.879865, 42.365165, null, -1);

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

INSERT INTO Taches VALUES (taches_sequence.nextval, 'Première tâche','m@mi6.gov.co.uk');
INSERT INTO Taches VALUES (taches_sequence.nextval, 'Deuxième tâche','picsou@disney.com');
INSERT INTO Taches VALUES (taches_sequence.nextval, 'Troisième tâche','m@mi6.gov.co.uk');

CREATE TABLE TachesAtom (
    idTacheAtom integer NOT NULL,
    titreTacheAtom varchar(100) NOT NULL,
    descriptionTache varchar(300) NOT NULL,
    prixTache decimal(9,2) NOT NULL,
    latitude decimal(9,6) NOT NULL,
    longitude decimal(9,6) NOT NULL,
    datePlusTot date NOT NULL,
    datePlusTard date NOT NULL,
    idTacheMere integer NOT NULL,
    idCommanditaire varchar(100) NOT NULL,
    constraint FK_TAtom_T foreign key (idTacheMere, idCommanditaire)
        references Taches(idTache, idCommanditaire) ON DELETE CASCADE,
    idExecutant varchar(100) references Utilisateurs(email) ON DELETE SET NULL,
    PRIMARY KEY (idTacheAtom,idCommanditaire),
    CHECK (prixTache >= 0)
);

CREATE SEQUENCE TachesAtom_Sequence
    INCREMENT BY 1
    START WITH 1
    NOMAXVALUE
    NOCYCLE;

INSERT INTO TachesAtom VALUES (tachesAtom_sequence.NEXTVAL, 'Escorte d''Elizabeth',
    'Attention! La mission contient un saut d''hélicoptère', 100.15, 43, 45, TO_date('04/05/2016','dd/mm/yyyy'),
    TO_date('04/05/2017','dd/mm/yyyy'), 1, 'm@mi6.gov.co.uk', null);
INSERT INTO TachesAtom VALUES (tachesAtom_sequence.NEXTVAL, 'Construire une application Web de crowsourcing',
    'En équipe de 5', 0, 50, 62, TO_date('04/05/2016','dd/mm/yyyy'),
    TO_date('04/05/2017','dd/mm/yyyy'), 1, 'm@mi6.gov.co.uk', 'james.bond@mi6.gov.co.uk');
INSERT INTO TachesAtom VALUES (tachesAtom_sequence.NEXTVAL, 'Battre le Chiffre au poker',
    '6 millions de livres appartenant au gouvernement seront mis à disposition. A rendre.',
    5005,55.4, 1.6, TO_date('18/09/1995','dd/mm/yyyy'), TO_date('19/09/1995','dd/mm/yyyy'),
    3,'m@mi6.gov.co.uk', null);
INSERT INTO TachesAtom VALUES (tachesAtom_sequence.NEXTVAL, 'Construire un niche pour Pluto',
    'En bois', 1, 55.4, 1.6, TO_date('18/09/1995','dd/mm/yyyy'), TO_date('19/09/1995','dd/mm/yyyy'),
    2,'picsou@disney.com', null);

CREATE TABLE Competences (
    competence varchar(100) NOT NULL PRIMARY KEY
);

INSERT INTO Competences VALUES ('bricolage');
INSERT INTO Competences VALUES ('couture');
INSERT INTO Competences VALUES ('défense');
INSERT INTO Competences VALUES ('programmation');
INSERT INTO Competences VALUES ('plomberie');
INSERT INTO Competences VALUES ('cuisine');

CREATE TABLE CompetencesUtilisateurs (
    idUtilisateur varchar(100) NOT NULL references Utilisateurs(email) ON DELETE CASCADE,
    competence varchar(100) NOT NULL references Competences(competence) ON DELETE CASCADE,
    PRIMARY KEY (idUtilisateur, competence)
);

INSERT INTO CompetencesUtilisateurs VALUES ('james.bond@mi6.gov.co.uk','défense');

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

CREATE SEQUENCE CompetencesTaches_Sequence
    INCREMENT BY 1
    START WITH 1
    NOMAXVALUE
    NOCYCLE;

CREATE TABLE Evaluations (
    idEvaluation integer NOT NULL PRIMARY KEY,
    evaluation integer NOT NULL,
    dateEval date NOT NULL,
    idTache integer NOT NULL,
    idEvaluateur varchar(100) NOT NULL,
    idEvalue varchar(100) NOT NULL,
    constraint FK_Ev_TAtom foreign key (idTache,idEvaluateur)
        references TachesAtom(idTacheAtom,idCommanditaire) ON DELETE SET NULL,
    --Cette contrainte d'intégrité est vitale afin de vérifier que l'évaluation
    --est donnée pour une tâche vraiment proposée par le bon commanditaire associé
    --au bon exécutant.
    commentaire varchar(300),
    CHECK (evaluation >= 0 AND evaluation <= 5),
    CHECK (idEvalue != idEvaluateur)
);

CREATE SEQUENCE Evaluations_Sequence
    INCREMENT BY 1
    START WITH 1
    NOMAXVALUE
    NOCYCLE;

CREATE TABLE Candidatures (
    idCandidature integer NOT NULL PRIMARY KEY,
    idTacheAtom integer NOT NULL,
    idCommanditaire varchar(100) NOT NULL,
    --On est obligé d'ajouter la colonne idCommanditaire
    idExecutant varchar(100) NOT NULL,
    --et la colonne idExecutant car pour associer
    --une compétence à une tâche, on a besoin d'au moins idTacheAtom
    --mais on ne peut pas séparer idTacheAtom des deux autres propriétés.
    constraint FK_Cand_TAtom foreign key (idTacheAtom,idCommanditaire)
        references TachesAtom(idTacheAtom,idCommanditaire) ON DELETE CASCADE,
    idCandidat varchar(100) NOT NULL references Utilisateurs(email)
);

CREATE SEQUENCE Candidatures_Sequence
    INCREMENT BY 1
    START WITH 1
    NOMAXVALUE
    NOCYCLE;