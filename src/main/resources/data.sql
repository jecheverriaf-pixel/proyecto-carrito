INSERT INTO producto (idProducto, nombre, codigo, imagen, precio)
SELECT * FROM (SELECT 1, 'FinePix Pro2 3D Camera', '3DcAM01', 'images/product/camera.jpg', 671.21) AS tmp
WHERE NOT EXISTS (SELECT idProducto FROM producto WHERE idProducto = 1) LIMIT 1;

INSERT INTO producto (idProducto, nombre, codigo, imagen, precio)
SELECT * FROM (SELECT 2, 'EXP Portable Hard Drive', 'USB02', 'images/product/external-hard-drive.jpg', 215.10) AS tmp
WHERE NOT EXISTS (SELECT idProducto FROM producto WHERE idProducto = 2) LIMIT 1;

INSERT INTO producto (idProducto, nombre, codigo, imagen, precio)
SELECT * FROM (SELECT 3, 'Luxury Ultra thin Wrist Watch', 'wristWear03', 'images/product/watch.jpg', 2121.86) AS tmp
WHERE NOT EXISTS (SELECT idProducto FROM producto WHERE idProducto = 3) LIMIT 1;

INSERT INTO producto (idProducto, nombre, codigo, imagen, precio)
SELECT * FROM (SELECT 4, 'XP 1155 Intel Core Laptop', 'LPN45', 'images/product/laptop.jpg', 10232.35) AS tmp
WHERE NOT EXISTS (SELECT idProducto FROM producto WHERE idProducto = 4) LIMIT 1;
