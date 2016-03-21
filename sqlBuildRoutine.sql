CREATE TABLE Utilisateurs (
    email varchar(100) NOT NULL,
    hash_de_motdepasse varchar(100) NOT NULL,
    prenom varchar(100) NOT NULL,
    nom varchar(100) NOT NULL,
    genre varchar(6) not null,
    dateDeNaissance date not null,
    longitude decimal(9,6) not null,
    lattitude decimal(9,6) not null
);
INSERT INTO Utilisateurs VALUES ('james.bond@mi6.gov.co.uk','rrr','James','Bond','homme',TO_date('04/05/1985','dd/mm/yyyy'),45.879865, 42.365165);
INSERT INTO Utilisateurs VALUES ('m@mi6.gov.co.uk','bbb','M','*','femme',TO_date('11/12/1948','dd/mm/yyyy'),45.879865, 42.365165);

CREATE TABLE Taches (
    titre varchar(100) NOT NULL,
    description varchar(100) NOT NULL,
    remuneration decimal(6,2) NOT NULL
);
INSERT INTO Taches VALUES ('Escorte d''Elizabeth','Attention! La mission contient un saut d''hélicoptère',100.15);
INSERT INTO Taches VALUES ('Construire une application Web de crowsourcing','En équipe de 5',0);