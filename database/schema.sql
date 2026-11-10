-- Ejecutar completo en phpMyAdmin (pestaña SQL) o cliente MySQL.
-- Requiere MySQL 8 / MariaDB de XAMPP.
CREATE DATABASE IF NOT EXISTS ockly_db
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE ockly_db;

CREATE TABLE IF NOT EXISTS categorias (
  id BIGINT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(100) NOT NULL,
  descripcion VARCHAR(500),
  PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS productos (
  id BIGINT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(120) NOT NULL,
  descripcion VARCHAR(500),
  precio DECIMAL(12,2) NOT NULL,
  stock INT NOT NULL,
  categoria_id BIGINT NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_productos_categorias FOREIGN KEY (categoria_id)
    REFERENCES categorias(id) ON UPDATE CASCADE ON DELETE RESTRICT,
  CONSTRAINT ck_productos_precio CHECK (precio > 0),
  CONSTRAINT ck_productos_stock CHECK (stock >= 0)
) ENGINE=InnoDB;
