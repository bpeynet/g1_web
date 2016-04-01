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

CREATE TABLE Taches (
    idTache integer NOT NULL PRIMARY KEY,
    titreTache varchar(100),
    idCommanditaire varchar(100) NOT NULL references Utilisateurs(email) ON DELETE CASCADE
);

INSERT INTO Taches VALUES (1, 'Essai','james.bond@mi6.gov.co.uk');

CREATE TABLE TachesAtom (
    idTacheAtom integer NOT NULL PRIMARY KEY,
    titreTacheAtom varchar(100) NOT NULL,
    descriptionTache varchar(300) NOT NULL,
    prixTache decimal(9,2) NOT NULL,
    lattitude decimal(9,6) not null,
    longitude decimal(9,6) not null,
    datePlusTot date NOT NULL,
    datePlusTard date NOT NULL,
    idTacheMere integer NOT NULL references Taches(idTache) ON DELETE CASCADE,
    idExecutant varchar(100) references Utilisateurs(email) ON DELETE SET NULL,
    CHECK (prixTache >= 0)
);

INSERT INTO TachesAtom VALUES (1, 'Escorte d''Elizabeth', 'Attention! La mission contient un saut d''hélicoptère', 100.15, 43, 45, TO_date('04/05/2016','dd/mm/yyyy'), TO_date('04/05/2017','dd/mm/yyyy'), 1, null);
INSERT INTO TachesAtom VALUES (2, 'Construire une application Web de crowsourcing', 'En équipe de 5', 0, 50, 62, TO_date('04/05/2016','dd/mm/yyyy'), TO_date('04/05/2017','dd/mm/yyyy'), 1, null);

CREATE TABLE Competences (
    nomComp varchar(100) NOT NULL PRIMARY KEY
);

CREATE TABLE CompetencesUtilisateurs (
    idCU integer NOT NULL PRIMARY KEY,
    idUtilisateur varchar(100) NOT NULL references Utilisateurs(email) ON DELETE CASCADE,
    competence varchar(100) NOT NULL references Competences(nomComp) ON DELETE CASCADE
);

CREATE TABLE CompetencesTaches (
    idCT integer NOT NULL PRIMARY KEY,
    idTacheAtom integer NOT NULL references TachesAtom(idTacheAtom) ON DELETE CASCADE,
    competence varchar(100) NOT NULL references Competences(nomComp) ON DELETE CASCADE
);

CREATE TABLE Evaluations (
    idEvaluation integer NOT NULL PRIMARY KEY,
    evaluation integer NOT NULL,
    dateEval date NOT NULL,
    idEvalue varchar(100) NOT NULL references Utilisateurs(email) ON DELETE SET NULL,
    idEvaluateur varchar(100) NOT NULL references Utilisateurs(email) ON DELETE SET NULL,
    idTache integer NOT NULL references TachesAtom(idTacheAtom) ON DELETE SET NULL,
    commentaire varchar(300),
    CHECK (evaluation >= 0 AND evaluation <= 5),
    CHECK (idEvalue != idEvaluateur)/*,
    CHECK () que la tache a bien été proposé par idEvaluateur et faite par idEvalue*/
);

CREATE TABLE Candidatures (
    idCandidature integer NOT NULL PRIMARY KEY,
    idTacheAtom integer NOT NULL references TachesAtom(idTacheAtom),
    idCandidat varchar(100) NOT NULL references Utilisateurs(email)
);