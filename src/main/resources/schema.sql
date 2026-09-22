CREATE TABLE IF NOT EXISTS producto(
    idProducto INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(255),
    codigo VARCHAR(255) NOT NULL,
    imagen TEXT,
    precio DOUBLE(10,2),
    PRIMARY KEY (idProducto)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS shopping_cart(
    idShoppingCart INT NOT NULL AUTO_INCREMENT,
    idProducto INT,
    codUsuario VARCHAR(128),
    cantidad INT,
    PRIMARY KEY (idShoppingCart),
    KEY cc_profk_1 (idProducto),
    CONSTRAINT cc_profk_1 FOREIGN KEY (idProducto) REFERENCES producto (idProducto)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

