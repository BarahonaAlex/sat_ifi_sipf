Prerrequisitos:
	-	Maven 3.3.9
	-	Java 1.8

Instrucciones:
1.	Este proyecto debe contener la funcionalidad de reglas y validaciones de negocio asi como el mapeo de las
	tablas cuando aplique y definicion de los endpoints de los servicios.
2.	Se debe mantener el estandar para el nombrado de paquetes y clases.

	gt.gob.sat.sat_ifi_sipf				->	clases que permiten la ejecucipn del proyecto, no debe modificarlas (Application.java).
	gt.gob.sat.sat_ifi_sipf.controllers	->	clases que definen los endpoints de los servicios REST (NombreMicroservicioController.java).
	gt.gob.sat.sat_ifi_sipf.models		->	clases que contienen el mapeo de tablas (NombreEntidad.java).
	gt.gob.sat.sat_ifi_sipf.repositories	->	interfaces que permiten realizar las operaciones de persistencia o consultas a base de datos (NombreRepositorioRepository.java).
	gt.gob.sat.sat_ifi_sipf.services		->	clases con la implementacion de la funcionalidad o logica de negocio (NombreServicioService.java).
	gt.gob.sat.sat_ifi_sipf.projections	->	interfaces con la estructura de los atributos que se filtran en consultas (NombreInterfazProjection.java).
	gt.gob.sat.sat_ifi_sipf.dtos      	->	clases con atributos (NombreDTODto.java).
	
3.	Desde la version 2.5 de spring se permite utilizar anotaciones para la declaracion de los beans en lugar de	usar el
	applicationContext.xml, para ello se debe agregar en NombreServicioService.java la anotacion @Service y para hacer referencia a
	ese servicio con la anotacion @Autowire. Por defecto tienen @Scope("singleton") por lo que no es necesario agregarlo a menos
	que se necesite especificarlo.
	
	Ejemplo:
	
	Clase1Service.java
		
		@Service
		public class Clase1Service {
		...
		
	Clase2Service.java
		
		@Service
		public class Clase2Service{
			
			@Autowired
			Clase1Service clase1Service;
			...
		
4. Las clases que se generaron en este proyecto deben borrarse, se incluyen como ejemplo.
5. Todos los servicios deben tener pruebas unitarias y tener validaciones (assert) de manera que al ejecutar la clase con
   junit se puedan realizar pruebas de regresion comprobando su funcionamiento. 
6. Para construir el proyecto (JAR) ejecuten maven: mvn -e clean eclipse:eclipse -DskipTests -Dmaven.javadoc.skip=true package
7. Para ejecutar el proyecto de forma local ejecute la clase Application.java como Java Application e ingrese a 
   http://localhost:8080/
   Tambien puede ejecutarse el JAR generado por medio de la linea de comandos con la siguiente instruccion:
   java -jar -DurlServerConfig=http://10.10.10.10:8888 JAR_FILE 
   [Mas informacion: https://gi.sat.gob.gt/wiki/bin/view/Arquitectura/Microservicios/Ejecuci%C3%B3n/]
8. Para hacer pruebas del microservicio utilice el API de Swagger que se genera con este proyecto.
9. Estas instrucciones deben borrarse de este archivo y dejar la siguiente documentacion:

/*****************************************************************************************************************************/
/*****************************************************************************************************************************/
Prerrequisito:
	-	Maven 3.3.9
	-	Java 1.8

Url SVN trunk:
Descripcion del proyecto:
Esquemas de base de datos:
Url documentacion wiki: https://gi.sat.gob.gt/wiki/...