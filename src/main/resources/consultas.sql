/* 1. Lista los nombres y los precios de todos los productos de la tabla producto */

use tienda;

SELECT p.nombre, ROUND(p.precio, 2) AS precio FROM producto p;

/* 2. Devuelve una lista de Producto completa con el precio de euros convertido a dólares . */

SELECT p.nombre, ROUND(p.precio*1.08, 2) AS 'precio en dolares' FROM producto p;

# * 3. Lista los nombres y los precios de todos los productos, convirtiendo los nombres a mayúscula.

SELECT UPPER(p.nombre) AS nombre, ROUND(p.precio, 2) AS precio FROM producto p;

