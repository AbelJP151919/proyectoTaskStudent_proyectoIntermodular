create database TaskStudent;
use TaskStudent;

CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255),
    email VARCHAR(255),
    puesto VARCHAR(255),
    curso VARCHAR(255),
    conn VARCHAR(255),
    conn2 VARCHAR(255)
);


select * from usuarios;

delete from usuarios where id="9";