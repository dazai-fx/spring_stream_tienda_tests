/* 1. Lista los nombres y los precios de todos los productos de la tabla producto */

use tienda;

SELECT p.nombre, ROUND(p.precio, 2) AS precio
FROM producto p;

/* 2. Devuelve una lista de Producto completa con el precio de euros convertido a dólares . */

SELECT p.nombre, ROUND(p.precio*1.08, 2) AS 'precio en dolares'
FROM producto p;

# * 3. Lista los nombres y los precios de todos los productos, convirtiendo los nombres a mayúscula.

SELECT UPPER(p.nombre) AS nombre, ROUND(p.precio, 2) AS precio
FROM producto p;

# 4. Lista el nombre de todos los fabricantes y a continuación en mayúsculas los dos primeros caracteres del nombre del fabricante.

SELECT CONCAT(UPPER(SUBSTRING(nombre, 1, 2)), SUBSTRING(nombre, 3)) AS nombre
FROM fabricante;

# 5. Lista el código de los fabricantes que tienen productos.

SELECT DISTINCT f.codigo
FROM fabricante f
INNER JOIN producto p
ON f.codigo = p.codigo_fabricante;

# 6. Lista los nombres de los fabricantes ordenados de forma descendente.

SELECT nombre
FROM fabricante
ORDER BY nombre DESC;

# 7 Lista los nombres de los productos ordenados en primer lugar por el nombre de forma ascendente y en segundo lugar por el precio de forma descendente.

SELECT nombre, precio
FROM producto
ORDER BY nombre ASC, precio DESC;

# 8. Devuelve una lista con los 5 primeros fabricantes.

SELECT nombre
FROM fabricante
ORDER BY codigo LIMIT 5;

# 9.Devuelve una lista con 2 fabricantes a partir del cuarto fabricante. El cuarto fabricante también se debe incluir en la respuesta.

SELECT codigo, nombre
FROM fabricante
LIMIT 2 OFFSET 3;

# 10. Lista el nombre y el precio del producto más barato

SELECT nombre, precio
FROM producto
ORDER BY precio ASC
LIMIT 1;

# 11. Lista el nombre y el precio del producto más caro

SELECT nombre, precio
FROM producto
ORDER BY precio DESC
LIMIT 1;

# 12. Lista el nombre de todos los productos del fabricante cuyo código de fabricante es igual a 2.

SELECT nombre
FROM producto
WHERE codigo_fabricante=2;

# 13. Lista el nombre de los productos que tienen un precio menor o igual a 120€.

SELECT nombre
FROM producto
WHERE precio >= 120;

# 14. Lista los productos que tienen un precio mayor o igual a 400€.

SELECT *
FROM producto
WHERE precio >= 400;

# 15. Lista todos los productos que tengan un precio entre 80€ y 300€.

SELECT *
FROM producto
WHERE precio >= 80 AND precio <= 300;

# 17. Lista todos los productos donde el código de fabricante sea 1, 3 o 5 utilizando un Set de codigos de fabricantes para filtrar.

SELECT *
FROM producto
WHERE codigo_fabricante IN (1, 3, 5);