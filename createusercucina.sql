-- run this as root

drop user if exists 'cucina'@'localhost';
drop database if exists cucina;

create database cucina;

CREATE USER 'cucina'@'localhost' IDENTIFIED BY 'scuola';
GRANT ALL PRIVILEGES ON cucina.* TO 'cucina'@'localhost';
