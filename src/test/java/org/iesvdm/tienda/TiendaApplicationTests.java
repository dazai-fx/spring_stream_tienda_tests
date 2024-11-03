package org.iesvdm.tienda;

import org.iesvdm.tienda.modelo.Fabricante;
import org.iesvdm.tienda.modelo.Producto;
import org.iesvdm.tienda.repository.FabricanteRepository;
import org.iesvdm.tienda.repository.ProductoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;


@SpringBootTest
class TiendaApplicationTests {

	@Autowired
	FabricanteRepository fabRepo;
	
	@Autowired
	ProductoRepository prodRepo;

	@Test
	void testAllFabricante() {
		var listFabs = fabRepo.findAll();
		
		listFabs.forEach(f -> {
			System.out.println(">>"+f+ ":");
			f.getProductos().forEach(System.out::println);
		});
	}
	
	@Test
	void testAllProducto() {
		var listProds = prodRepo.findAll();

		listProds.forEach( p -> {
			System.out.println(">>"+p+":"+"\nProductos mismo fabricante "+ p.getFabricante());
			p.getFabricante().getProductos().forEach(pF -> System.out.println(">>>>"+pF));
		});
				
	}

	
	/**
	 * 1. Lista los nombres y los precios de todos los productos de la tabla producto
	 */
	@Test
	void test1() {
		var listProds = prodRepo.findAll();

		record Tupla (String nombre, double precio){}
		var listaNombresPrecios = listProds
				.stream()
				.map((p -> new Tupla(p.getNombre(), p.getPrecio())))
				.toList();

//		System.out.println("Nombre producto\t\t\tPrecio");
//		listaNombresPrecios.forEach(x -> System.out.println(x));

		// Encabezado de la tabla
		System.out.printf("%-35s %-10s%n", "Nombre producto", "Precio");
		System.out.println("-----------------------------------------------");

		// Imprimir cada producto en formato de tabla
		listaNombresPrecios.forEach(tupla ->
				System.out.printf("%-35s %-10.2f%n", tupla.nombre(), tupla.precio())
		);

	}
	
	
	/**
	 * 2. Devuelve una lista de Producto completa con el precio de euros convertido a dólares .
	 */
	@Test
	void test2() {

		var listProds = prodRepo.findAll();

		record Tupla (String nombre, double precio){}
		var listaNombresPrecios = listProds
				.stream()
				.map((p -> new Tupla(p.getNombre(), convertirEurosDolares(p.getPrecio()))))
				.toList();

		System.out.printf("%-35s | %-20s%n", "Nombre del Producto", "Precio en dolares");
		System.out.println("-------------------------------------------------------");
		listaNombresPrecios.forEach(tupla ->
				System.out.printf("%-38s %-20.2f%n", tupla.nombre(), tupla.precio())
		);

	}

	public static double convertirEurosDolares(double euros){
		final Double TASA_DE_CAMBIO=1.08;
		return euros*TASA_DE_CAMBIO;
	}
	
	/**
	 * 3. Lista los nombres y los precios de todos los productos, convirtiendo los nombres a mayúscula.
	 */
	@Test
	void test3() {
		var listProds = prodRepo.findAll();

		record Tupla (String nombre, double precio){}
		var listaNombresPrecios = listProds
				.stream()
				.map((p -> new Tupla(p.getNombre().toUpperCase(), p.getPrecio())))
				.toList();

		// Encabezado de la tabla
		System.out.printf("%-35s %-10s%n", "Nombre producto", "Precio");
		System.out.println("-----------------------------------------------");

		// Imprimir cada producto en formato de tabla
		listaNombresPrecios.forEach(tupla ->
				System.out.printf("%-35s %-10.2f%n", tupla.nombre(), tupla.precio())
		);

	}
	
	/**
	 * 4. Lista el nombre de todos los fabricantes y a continuación en mayúsculas los dos primeros caracteres del nombre del fabricante.
	 */
	@Test
	void test4() {
		var listFabs = fabRepo.findAll();


		record Tupla (String nombre){}
		var listaFabricantes = listFabs.stream()
				.map(p -> new Tupla(capitalizeFirstTwoLetters(p.getNombre())))
				.toList();
		// Encabezado de la tabla
		System.out.printf("%-35s%n", "Nombre fabricante");
		System.out.println("------------------------");

		// Imprimir cada producto en formato de tabla
		listaFabricantes.forEach(tupla ->
				System.out.printf("%-35s%n", tupla.nombre())
		);

	}

	public static String capitalizeFirstTwoLetters(String input){

		if(input.length()==2) {
			input = input.toUpperCase();
		}else {
			input=input.substring(0, 2).toUpperCase() + input.substring(2);
		}

		return input;

	}
	
	/**
	 * 5. Lista el código de los fabricantes que tienen productos.
	 */
	@Test
	void test5() {
		var listFabs = fabRepo.findAll();

		record Tupla(Integer codigoFabricante){}

		var fabricantesConProductos = listFabs.stream()
				.filter(f -> !f.getProductos().isEmpty())
				.map(f -> new Tupla(f.getCodigo()))
				.toList();

		System.out.printf("%-35s%n", "Codigo");
		System.out.println("------------------------");

		fabricantesConProductos.forEach( tupla ->
				System.out.printf("%-35d%n", tupla.codigoFabricante())
		);

	}
	
	/**
	 * 6. Lista los nombres de los fabricantes ordenados de forma descendente.
	 */
	@Test
	void test6() {
		var listFabs = fabRepo.findAll();

		record Tupla(String nombre){}

		var listaFabricantes = listFabs
				.stream()
				.sorted((f1, f2) -> f2.getNombre().compareTo(f1.getNombre()))
				.map(f -> new Tupla(f.getNombre()))
				.toList();

		System.out.printf("%n%-35s%n", "Nombre");
		System.out.println("----------------------");
		listaFabricantes.forEach(tupla -> {
			System.out.printf("%-35s%n", tupla.nombre());
		});

		System.out.println();

	}
	
	/**
	 * 7. Lista los nombres de los productos ordenados en primer lugar por el nombre de forma ascendente y en segundo lugar por el precio de forma descendente.
	 */
	@Test
	void test7() {
		var listProds = prodRepo.findAll();

//		Revisar en clase

		record Tupla (String nombre, double precio){}
		// es necesario usar comparator si quiero usar thenComparingDouble y solo se hace una llamada a sorted porque si intento hacer 2 se sobreescribe.
		var listaProductos = listProds
				.stream()
				.sorted(Comparator.comparing(Producto::getNombre)
						.thenComparing(Comparator.comparingDouble(Producto::getPrecio).reversed()))
				.map(p -> new Tupla (p.getNombre(), p.getPrecio()))
				.toList();


		System.out.printf("%n%-35s%-15s%n", "| Nombre", "| Precio |");
		System.out.println("---------------------------------------------------");

		listaProductos.forEach(tupla -> {
			System.out.printf("| %-33s| %-13.2f|%n", tupla.nombre(), tupla.precio());
		});

		System.out.println("---------------------------------------------------");

		System.out.println();

	}
	
	/**
	 * 8. Devuelve una lista con los 5 primeros fabricantes.
	 */
	@Test
	void test8() {
		var listFabs = fabRepo.findAll();

		record Tupla (String nombre){}

		var primeros5Fabricantes = listFabs.stream()
				.sorted(Comparator.comparing(Fabricante::getCodigo))
				.limit(5)
				.map(f -> new Tupla(f.getNombre()))
				.toList();

		System.out.println("----------------------------");
		System.out.printf("| %-25s | %n", "Nombre");
		System.out.println("----------------------------");
		primeros5Fabricantes.forEach( Tupla ->
				System.out.printf("| %-25s | %n", Tupla.nombre())
		);
		System.out.println("----------------------------");

	}
	
	/**
	 * 9.Devuelve una lista con 2 fabricantes a partir del cuarto fabricante. El cuarto fabricante también se debe incluir en la respuesta.
	 */
	@Test
	void test9() {
		var listFabs = fabRepo.findAll();

		record Tupla (int codigo, String nombre) {}

		var lista2Fabricantes = listFabs.stream()
				.skip(3)
				.limit(2)
				.map(f -> new Tupla(f.getCodigo(), f.getNombre()))
				.toList();

		System.out.println("--------------------------------------------------------");
		System.out.printf("| %-25s || %-25s | %n", "Codigo", "Nombre" );
		System.out.println("--------------------------------------------------------");
		lista2Fabricantes.forEach( tupla ->
				System.out.printf("| %-25d || %-25s | %n", tupla.codigo(), tupla.nombre() )
		);
		System.out.println("--------------------------------------------------------");


	}
	
	/**
	 * 10. Lista el nombre y el precio del producto más barato
	 */
	@Test
	void test10() {
		var listProds = prodRepo.findAll();

		record Tupla (String nombre, double precio) {}

		var productoMasBarato = listProds.stream()
				.min(Comparator.comparing(Producto::getPrecio))
				.map(p -> new Tupla(p.getNombre(), p.getPrecio()));

		System.out.println("--------------------------------------------------------");
		System.out.printf("| %-25s || %-25s | %n", "nombre", "precio" );
		System.out.println("--------------------------------------------------------");

		if (productoMasBarato.isPresent()) {
			Tupla tupla = productoMasBarato.get();
			System.out.printf("| %-25s || %-25.2f | %n", tupla.nombre(), tupla.precio());
		} else {
			System.out.printf("| %-25s || %-25s | %n", "No se encontró", "producto");
		}

		System.out.println("--------------------------------------------------------");

	}
	
	/**
	 * 11. Lista el nombre y el precio del producto más caro
	 */
	@Test
	void test11() {
		var listProds = prodRepo.findAll();

		record Tupla (String nombre, double precio) {}

		var productoMasBarato = listProds.stream()
				.max(Comparator.comparing(Producto::getPrecio))
				.map(p -> new Tupla(p.getNombre(), p.getPrecio()));

		System.out.println("--------------------------------------------------------");
		System.out.printf("| %-25s || %-25s | %n", "nombre", "precio" );
		System.out.println("--------------------------------------------------------");

		if (productoMasBarato.isPresent()) {
			Tupla tupla = productoMasBarato.get();
			System.out.printf("| %-25s || %-25.2f | %n", tupla.nombre(), tupla.precio());
		} else {
			System.out.printf("| %-25s || %-25s | %n", "No se encontró", "producto");
		}

		System.out.println("--------------------------------------------------------");

	}
	
	/**
	 * 12. Lista el nombre de todos los productos del fabricante cuyo código de fabricante es igual a 2.
	 * 
	 */
	@Test
	void test12() {
		var listProds = prodRepo.findAll();

		record Tupla (String nombre){}

		var productosFabricante2 = listProds
				.stream()
				.filter(p -> p.getFabricante().getCodigo()==2)
				.map(p -> new Tupla(p.getNombre()))
				.toList();

		System.out.println("-----------------------------");
		System.out.printf("| %-25s | %n", "nombre" );
		System.out.println("-----------------------------");
		productosFabricante2.forEach(tupla -> {
			System.out.printf("| %-25s | %n", tupla.nombre());
		});
		System.out.println("-----------------------------");

	}
	
	/**
	 * 13. Lista el nombre de los productos que tienen un precio menor o igual a 120€.
	 */
	@Test
	void test13() {
		var listProds = prodRepo.findAll();

		record Tupla (String nombre) {}

		var listaProductos = listProds
				.stream()
				.filter(p -> p.getPrecio() >= 120)
				.map(p -> new Tupla(p.getNombre()))
				.toList();

		System.out.println("-----------------------------------");
		System.out.printf("| %-32s | %n", "nombre" );
		System.out.println("-----------------------------------");
		listaProductos.forEach(tupla -> {
			System.out.printf("| %-32s | %n", tupla.nombre());
		});
		System.out.println("-----------------------------------");


	}
	
	/**
	 * 14. Lista los productos que tienen un precio mayor o igual a 400€.
	 */
	@Test
	void test14() {
		var listProds = prodRepo.findAll();

		record Tupla (int codigo, String nombre, double precio, int codigo_fabricante) {}

		var listaProductos = listProds
				.stream()
				.filter(p -> p.getPrecio() >= 400)
				.map(p -> new Tupla(p.getCodigo(), p.getNombre(), p.getPrecio(), p.getFabricante().getCodigo() ))
				.toList();


		System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------");
		System.out.printf("| %-32s | | %-32s | | %-32s | | %-32s | %n", "codigo", "nombre", "precio", "codigo_fabricante" );
		System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------");
		listaProductos.forEach(tupla -> {
			System.out.printf("| %-32d | | %-32s | | %-32.2f | | %-32d |  %n", tupla.codigo(), tupla.nombre(), tupla.precio(), tupla.codigo_fabricante()  );
		});
		System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------");

	}
	
	/**
	 * 15. Lista todos los productos que tengan un precio entre 80€ y 300€. 
	 */
	@Test
	void test15() {
		var listProds = prodRepo.findAll();

		record Tupla (int codigo, String nombre, double precio, int codigo_fabricante) {}

		var listaProductos = listProds
				.stream()
				.filter(p -> p.getPrecio() >= 80 && p.getPrecio() <= 300)
				.map(p -> new Tupla(p.getCodigo(), p.getNombre(), p.getPrecio(), p.getFabricante().getCodigo() ))
				.toList();


		System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------");
		System.out.printf("| %-32s | | %-32s | | %-32s | | %-32s | %n", "codigo", "nombre", "precio", "codigo_fabricante" );
		System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------");
		listaProductos.forEach(tupla -> {
			System.out.printf("| %-32d | | %-32s | | %-32.2f | | %-32d |  %n", tupla.codigo(), tupla.nombre(), tupla.precio(), tupla.codigo_fabricante()  );
		});
		System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------");

	}
	
	/**
	 * 16. Lista todos los productos que tengan un precio mayor que 200€ y que el código de fabricante sea igual a 6.
	 */
	@Test
	void test16() {
		var listProds = prodRepo.findAll();

		record Tupla (int codigo, String nombre, double precio, int codigo_fabricante) {}

		var listaProductos = listProds
				.stream()
				.filter(p -> p.getPrecio() > 200 && p.getFabricante().getCodigo() == 6)
				.map(p -> new Tupla(p.getCodigo(), p.getNombre(), p.getPrecio(), p.getFabricante().getCodigo() ))
				.toList();


		System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------");
		System.out.printf("| %-32s | | %-32s | | %-32s | | %-32s | %n", "codigo", "nombre", "precio", "codigo_fabricante" );
		System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------");
		listaProductos.forEach(tupla -> {
			System.out.printf("| %-32d | | %-32s | | %-32.2f | | %-32d |  %n", tupla.codigo(), tupla.nombre(), tupla.precio(), tupla.codigo_fabricante()  );
		});
		System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------");

	}
	
	/**
	 * 17. Lista todos los productos donde el código de fabricante sea 1, 3 o 5 utilizando un Set de codigos de fabricantes para filtrar.
	 */
	@Test
	void test17() {
		var listProds = prodRepo.findAll();

		record Tupla (int codigo, String nombre, double precio, int codigo_fabricante) {}

		Set<Integer> codigosFabricantes = Set.of(1, 3, 5);

		var listaProductos = listProds
				.stream()
				.filter(p -> codigosFabricantes.contains(p.getFabricante().getCodigo()))
				.map(p -> new Tupla(p.getCodigo(), p.getNombre(), p.getPrecio(), p.getFabricante().getCodigo()))
				.toList();

		System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------");
		System.out.printf("| %-32s | | %-32s | | %-32s | | %-32s | %n", "codigo", "nombre", "precio", "codigo_fabricante" );
		System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------");
		listaProductos.forEach(tupla -> {
			System.out.printf("| %-32d | | %-32s | | %-32.2f | | %-32d |  %n", tupla.codigo(), tupla.nombre(), tupla.precio(), tupla.codigo_fabricante()  );
		});
		System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------");

	}
	
	/**
	 * 18. Lista el nombre y el precio de los productos en céntimos.
	 */
	@Test
	void test18() {
		var listProds = prodRepo.findAll();
		//TODO
	}
	
	
	/**
	 * 19. Lista los nombres de los fabricantes cuyo nombre empiece por la letra S
	 */
	@Test
	void test19() {
		var listFabs = fabRepo.findAll();
		//TODOS
	}
	
	/**
	 * 20. Devuelve una lista con los productos que contienen la cadena Portátil en el nombre.
	 */
	@Test
	void test20() {
		var listProds = prodRepo.findAll();
		//TODO
	}
	
	/**
	 * 21. Devuelve una lista con el nombre de todos los productos que contienen la cadena Monitor en el nombre y tienen un precio inferior a 215 €.
	 */
	@Test
	void test21() {
		var listProds = prodRepo.findAll();
		//TODO
	}
	
	/**
	 * 22. Lista el nombre y el precio de todos los productos que tengan un precio mayor o igual a 180€. 
	 * Ordene el resultado en primer lugar por el precio (en orden descendente) y en segundo lugar por el nombre (en orden ascendente).
	 */
	void test22() {
		var listProds = prodRepo.findAll();
		//TODO
	}
	
	/**
	 * 23. Devuelve una lista con el nombre del producto, precio y nombre de fabricante de todos los productos de la base de datos. 
	 * Ordene el resultado por el nombre del fabricante, por orden alfabético.
	 */
	@Test
	void test23() {
		var listProds = prodRepo.findAll();
		//TODO
	}
	
	/**
	 * 24. Devuelve el nombre del producto, su precio y el nombre de su fabricante, del producto más caro.
	 */
	@Test
	void test24() {
		var listProds = prodRepo.findAll();
		//TODO
	}
	
	/**
	 * 25. Devuelve una lista de todos los productos del fabricante Crucial que tengan un precio mayor que 200€.
	 */
	@Test
	void test25() {
		var listProds = prodRepo.findAll();
		//TODO	
	}
	
	/**
	 * 26. Devuelve un listado con todos los productos de los fabricantes Asus, Hewlett-Packard y Seagate
	 */
	@Test
	void test26() {
		var listProds = prodRepo.findAll();

		Set <String> setFabricantes = new HashSet<>();

		setFabricantes.add("Asus");
		setFabricantes.add("Hewlett-Packard");
		setFabricantes.add("Seagate");

		record Tupla (int codigo, String nombreProducto, double precio, int codigo_fabricante, String nombreFabriante) {}

		var listaProductos = listProds
				.stream()
				.filter(p -> setFabricantes.contains(p.getFabricante().getNombre()))
				.map(p -> new Tupla(p.getCodigo(), p.getNombre(), p.getPrecio(), p.getFabricante().getCodigo(), p.getFabricante().getNombre()))
				.toList();

		System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
		System.out.printf("| %-32s | | %-32s | | %-32s | | %-32s | | %-32s | %n", "codigo", "nombre producto", "precio", "codigo_fabricante", "nombre fabricante" );
		System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
		listaProductos.forEach(tupla -> {
			System.out.printf("| %-32d | | %-32s | | %-32.2f | | %-32d | | %-32s |  %n", tupla.codigo(), tupla.nombreProducto(), tupla.precio(), tupla.codigo_fabricante(), tupla.nombreFabriante()  );
		});
		System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");



	}
	
	/**
	 * 27. Devuelve un listado con el nombre de producto, precio y nombre de fabricante, de todos los productos que tengan un precio mayor o igual a 180€. 
	 * Ordene el resultado en primer lugar por el precio (en orden descendente) y en segundo lugar por el nombre.
	 * El listado debe mostrarse en formato tabla. Para ello, procesa las longitudes máximas de los diferentes campos a presentar y compensa mediante la inclusión de espacios en blanco.
	 * La salida debe quedar tabulada como sigue:

Producto                Precio             Fabricante
-----------------------------------------------------
GeForce GTX 1080 Xtreme|611.5500000000001 |Crucial
Portátil Yoga 520      |452.79            |Lenovo
Portátil Ideapd 320    |359.64000000000004|Lenovo
Monitor 27 LED Full HD |199.25190000000003|Asus

	 */		
	@Test
	void test27() {
		var listProds = prodRepo.findAll();
		//TODO
	}
	
	/**
	 * 28. Devuelve un listado de los nombres fabricantes que existen en la base de datos, junto con los nombres productos que tiene cada uno de ellos. 
	 * El listado deberá mostrar también aquellos fabricantes que no tienen productos asociados. 
	 * SÓLO SE PUEDEN UTILIZAR STREAM, NO PUEDE HABER BUCLES
	 * La salida debe queda como sigue:
Fabricante: Asus

            	Productos:
            	Monitor 27 LED Full HD
            	Monitor 24 LED Full HD

Fabricante: Lenovo

            	Productos:
            	Portátil Ideapd 320
            	Portátil Yoga 520

Fabricante: Hewlett-Packard

            	Productos:
            	Impresora HP Deskjet 3720
            	Impresora HP Laserjet Pro M26nw

Fabricante: Samsung

            	Productos:
            	Disco SSD 1 TB

Fabricante: Seagate

            	Productos:
            	Disco duro SATA3 1TB

Fabricante: Crucial

            	Productos:
            	GeForce GTX 1080 Xtreme
            	Memoria RAM DDR4 8GB

Fabricante: Gigabyte

            	Productos:
            	GeForce GTX 1050Ti

Fabricante: Huawei

            	Productos:


Fabricante: Xiaomi

            	Productos:

	 */
	@Test
	void test28() {
		var listFabs = fabRepo.findAll();

		/*var listaFabricantesYProductos = listFabs
				.stream()
				.map(
						f -> "Fabricante: "+ f.getNombre()+"\n\n"+
								"Productos: "
							+f.getProductos().stream()
									.map(p -> p.getNombre()+"\n")
				)
				.collect(Collectors.joining()))
				.toList();*/



	}
	
	/**
	 * 29. Devuelve un listado donde sólo aparezcan aquellos fabricantes que no tienen ningún producto asociado.
	 */
	@Test
	void test29() {
		var listFabs = fabRepo.findAll();
		//TODO
	}
	
	/**
	 * 30. Calcula el número total de productos que hay en la tabla productos. Utiliza la api de stream.
	 */
	@Test
	void test30() {
		var listProds = prodRepo.findAll();
		//TODO
	}

	
	/**
	 * 31. Calcula el número de fabricantes con productos, utilizando un stream de Productos.
	 */
	@Test
	void test31() {
		var listProds = prodRepo.findAll();
		//TODO
	}
	
	/**
	 * 32. Calcula la media del precio de todos los productos
	 */
	@Test
	void test32() {
		var listProds = prodRepo.findAll();
		//TODO
	}
	
	/**
	 * 33. Calcula el precio más barato de todos los productos. No se puede utilizar ordenación de stream.
	 */
	@Test
	void test33() {
		var listProds = prodRepo.findAll();
		//TODO
	}
	
	/**
	 * 34. Calcula la suma de los precios de todos los productos.
	 */
	@Test
	void test34() {
		var listProds = prodRepo.findAll();
		//TODO	
	}
	
	/**
	 * 35. Calcula el número de productos que tiene el fabricante Asus.
	 */
	@Test
	void test35() {
		var listProds = prodRepo.findAll();
		//TODO		
	}
	
	/**
	 * 36. Calcula la media del precio de todos los productos del fabricante Asus.
	 */
	@Test
	void test36() {
		var listProds = prodRepo.findAll();
		//TODO
	}
	
	
	/**
	 * 37. Muestra el precio máximo, precio mínimo, precio medio y el número total de productos que tiene el fabricante Crucial. 
	 *  Realízalo en 1 solo stream principal. Utiliza reduce con Double[] como "acumulador".
	 */
	@Test
	void test37() {
		var listProds = prodRepo.findAll();
		//TODO
	}
	
	/**
	 * 38. Muestra el número total de productos que tiene cada uno de los fabricantes. 
	 * El listado también debe incluir los fabricantes que no tienen ningún producto. 
	 * El resultado mostrará dos columnas, una con el nombre del fabricante y otra con el número de productos que tiene. 
	 * Ordene el resultado descendentemente por el número de productos. Utiliza String.format para la alineación de los nombres y las cantidades.
	 * La salida debe queda como sigue:
	 
     Fabricante     #Productos
-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
           Asus              2
         Lenovo              2
Hewlett-Packard              2
        Samsung              1
        Seagate              1
        Crucial              2
       Gigabyte              1
         Huawei              0
         Xiaomi              0

	 */
	@Test
	void test38() {
		var listFabs = fabRepo.findAll();
		//TODO
	}
	
	/**
	 * 39. Muestra el precio máximo, precio mínimo y precio medio de los productos de cada uno de los fabricantes. 
	 * El resultado mostrará el nombre del fabricante junto con los datos que se solicitan. Realízalo en 1 solo stream principal. Utiliza reduce con Double[] como "acumulador".
	 * Deben aparecer los fabricantes que no tienen productos.
	 */
	@Test
	void test39() {
		var listFabs = fabRepo.findAll();
		//TODO
	}
	
	/**
	 * 40. Muestra el precio máximo, precio mínimo, precio medio y el número total de productos de los fabricantes que tienen un precio medio superior a 200€. 
	 * No es necesario mostrar el nombre del fabricante, con el código del fabricante es suficiente.
	 */
	@Test
	void test40() {
		var listFabs = fabRepo.findAll();
		//TODO
	}
	
	/**
	 * 41. Devuelve un listado con los nombres de los fabricantes que tienen 2 o más productos.
	 */
	@Test
	void test41() {
		var listFabs = fabRepo.findAll();
		//TODO
	}
	
	/**
	 * 42. Devuelve un listado con los nombres de los fabricantes y el número de productos que tiene cada uno con un precio superior o igual a 220 €. 
	 * Ordenado de mayor a menor número de productos.
	 */
	@Test
	void test42() {
		var listFabs = fabRepo.findAll();
		//TODO
	}
	
	/**
	 * 43.Devuelve un listado con los nombres de los fabricantes donde la suma del precio de todos sus productos es superior a 1000 €
	 */
	@Test
	void test43() {
		var listFabs = fabRepo.findAll();
		//TODO
	}
	
	/**
	 * 44. Devuelve un listado con los nombres de los fabricantes donde la suma del precio de todos sus productos es superior a 1000 €
	 * Ordenado de menor a mayor por cuantía de precio de los productos.
	 */
	@Test
	void test44() {
		var listFabs = fabRepo.findAll();
		//TODO	
	}
	
	/**
	 * 45. Devuelve un listado con el nombre del producto más caro que tiene cada fabricante. 
	 * El resultado debe tener tres columnas: nombre del producto, precio y nombre del fabricante. 
	 * El resultado tiene que estar ordenado alfabéticamente de menor a mayor por el nombre del fabricante.
	 */
	@Test
	void test45() {
		var listFabs = fabRepo.findAll();
		//TODO
	}
	
	/**
	 * 46. Devuelve un listado de todos los productos que tienen un precio mayor o igual a la media de todos los productos de su mismo fabricante.
	 * Se ordenará por fabricante en orden alfabético ascendente y los productos de cada fabricante tendrán que estar ordenados por precio descendente.
	 */
	@Test
	void test46() {
		var listFabs = fabRepo.findAll();
		//TODO
	}

}
