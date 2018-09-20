<pre>
                                                                                                                                                               
                                                                                                                                                               
                                                                                                                                                               
                                                                                                                                                               
                                                                                                                                                               
                                                                                                                                                               
                                                                                                          ..............                                       
                                                                                                     .......................                                   
                                                                                                  .............................                                
                                                                                                .................................                              
                                                                                               ....................................                            
                                                                                             .......................................                           
                                                                                            .........................................                          
                          ..                                                               ....................***,...................                         
                      #@@@@@@@@@,             @@@@@             @@@@@@@@@@@&        .@@@@@@@@@@@@/.........*@@@@@@@@@&.................                        
                    /@@@@@*,(@@@,            @@@@@@@            @@@@(,,*&@@@@        ****@@@@(///*........@@@@@@@@@@@@@(...............                        
                   .@@@@                    @@@@ @@@@           @@@@*    @@@@            @@@@,...........&@@@@@@@@@@@@@@,..............                        
                   ,@@@@                   @@@@, ,@@@%          @@@@@@@@@@@@*            @@@@,...........@@@@@@@@@@@@@@@*..............                        
                    @@@@(     #           %@@@@@@@@@@@/         @@@@@@@@@@,              @@@@,...........(@@@@@@@@@@@@@@...............                        
                     @@@@@@@@@@@@,       (@@@@&&&&&@@@@.        @@@@*  @@@@%             @@@@,............(@@@@@@@@@@@@,...............                        
                       #@@@@@@@,        ,@@@&       @@@@        @@@@*   /@@@@            @@@@,............../@@@@@@@&,................                         
                                                                                            ..........................................                         
                                                                                             ........................................                          
                                                                                              ......................................                           
                                                                                               ...................................                             
                                                                                                 ................................                              
                                                                                                   ...........................                                 
                                                                                                       ....................                                    
                                                                                                             ........                                          
                                                                                                                                                               
                                                                                                                                                               
                                                                                                                                                               
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   
</pre>

GeoJSON Activities Server
=========================

**The document [Function Description](./Functional_Description.md) contains all my thoughts**, issues and functional description of the application. Take into account that my main objective here has been to develop a strong base where future use case can be developed fast but with quality. 

Requirements for run:
- Docker-compose (v1.22+)
- Docker (v18.06.1+)
- JDK 1.8
- Developed in Windows 7 with Docker-Machine

For compilation I 

How to compile using Maven Docker image:
1a. Linux 
docker run -it --rm --name activities -v "$(pwd)":/usr/src/mymaven -v maven-repo-carto:/root/.m2 -v /var/run/docker.sock:/var/run/docker.sock -w /usr/src/mymaven maven:3.5.4-jdk-8 mvn clean package -DskipTests dockerfile:build

1b. Windows 7 (from Docker Quickstart Terminal)
docker run -it --rm --name activities -v "%cd%":/usr/src/mymaven -v maven-repo-carto:/root/.m2 -v /var/run/docker.sock:/var/run/docker.sock -w /usr/src/mymaven maven:3.5.4-jdk-8 mvn clean package -DskipTests dockerfile:build


How to compile using Maven wrapper and JDK 1.8:
2. Execute command ./mvnw clean package -DskipTests dockerfile:build 



RUN PROJECT
-----------
0. Start docker / docker-machine
1. Compile using maven wrapper  -> docker image should be generated
2. Execute docker-compose up postgres -d 
3a. (while issue with postgis images is not fixed) execute ./mvnw spring-boot:run 
3b. (when issue with postgis image is fixed) Access the URL at your docker daemon (192.168.99.100 for windows)
	http://192.168.99.100:8080

STOP PROJECT
1. Excute docker-compose rm
	
AVAILABLE ENDPOINTS
-------------------
(GET) http://192.168.99.100:8080/madrid

(GET) http://192.168.99.100:8080/madrid/category/<name>

(GET) http://192.168.99.100:8080/madrid/district/<name>

(GET) http://192.168.99.100:8080/madrid/location/<name>

(GET) http://192.168.99.100:8080/madrid/recomendation/ (NOT 100% IMPLEMENTED, REST LAYER IS NOT IMPLEMENTED YET, DATABASE QUERY IMPLEMENTED 

NOTE 1: dockerized application and debug application uses different spring-boot profiles for configuration
NOTE 2: not all the endpoints are case insensitive, but it is easy adding a new IgnoreCase repository method 

DEBUG OR DEVELOP PROJECT
-----------------------
How to develop using Eclipse Photon:
- Install Eclipse Photon
- Install STS plugin (Spring IDE)
- Import the existing maven project or materialize a maven project directly from github
- Execute commands: 
1. docker-compose up postgres
2. from eclipse in project-> right click->debug->as spring-boot:run

CONFIGURATION
--------------
Spring Boot properties explained
TODO


Summary of steps for implementation
---------------------------------
1. from https://start.spring.io/ select spring-boot version 2.0.4 with dependencies "JPA" "Rest Respositories" "PostgreSQL"
2. Download and add the files to my local git repository
3. Create the code layers Entities, Repositories, Services
4. Create the data loader
5. (Unit testing should be created during development, I disabled for compilation due to the need of having postgres running ...)