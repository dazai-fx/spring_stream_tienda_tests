/* 1. Lista los nombres y los precios de todos los productos de la tabla producto */

use tienda;

SELECT p.nombre, ROUND(p.precio, 2) AS precio FROM producto p;

/* 2. Devuelve una lista de Producto completa con el precio de euros convertido a dólares . */

SELECT p.nombre, ROUND(p.precio*1.08, 2) AS 'precio en dolares' FROM producto p;

# * 3. Lista los nombres y los precios de todos los productos, convirtiendo los nombres a mayúscula.

SELECT UPPER(p.nombre) AS nombre, ROUND(p.precio, 2) AS precio FROM producto p;

# 4. Lista el nombre de todos los fabricantes y a continuación en mayúsculas los dos primeros caracteres del nombre del fabricante.

SELECT CONCAT(UPPER(SUBSTRING(nombre, 1, 2)), SUBSTRING(nombre, 3)) AS nombre FROM fabricante;

# 5. Lista el código de los fabricantes que tienen productos.

SELECT DISTINCT f.codigo FROM fabricante f INNER JOIN producto p ON f.codigo = p.codigo_fabricante;