# Spring Ecosystem Single Page Application Example
### Spring Boot/Cloud/Eureka/Netflix/MVC using React/Redux for UI and MongoDB/PostgreSQL/Apache Ignite for data persistence

#### Back-end:
1. [**Spring Boot**](https://spring.io/projects/spring-boot): 
    -   [Maven dependency management for Spring & Kotlin & Java & NPM](https://github.com/dredwardhyde/addressbook/blob/master/addressbook-main/pom.xml)
    -   [Single executable Jar assembly (with React static)](https://github.com/dredwardhyde/addressbook/blob/master/addressbook-main/pom.xml)
    -   [Setting http-session duration](https://github.com/dredwardhyde/addressbook/blob/master/addressbook-main/src/main/kotlin/com/addressbook/security/RequestAwareAuthenticationSuccessHandler.kt)
    -   [Static resource handler](https://github.com/dredwardhyde/addressbook/blob/master/addressbook-main/src/main/kotlin/com/addressbook/configuration/WebConfiguration.kt)
2.  [**Spring Security**](https://spring.io/projects/spring-security):
    -   Custom [AuthenticationProvider](https://github.com/dredwardhyde/addressbook/blob/master/addressbook-main/src/main/kotlin/com/addressbook/security/IgniteAuthenticationProvider.kt)
    -   Custom [UrlAuthenticationSuccessHandler](https://github.com/dredwardhyde/addressbook/blob/master/addressbook-main/src/main/kotlin/com/addressbook/security/RequestAwareAuthenticationSuccessHandler.kt) for REST API
    -   Custom [role model](https://github.com/dredwardhyde/addressbook/blob/master/addressbook-main/src/main/kotlin/com/addressbook/ignite/UserCreator.kt) for [feature availabilty](https://github.com/dredwardhyde/addressbook/blob/master/addressbook-main/src/main/kotlin/com/addressbook/ignite/MenuCreator.kt)
    -   External user management
3.  [**Spring Cloud Netflix**](https://spring.io/projects/spring-cloud-netflix):
    -   [Service registration](https://github.com/dredwardhyde/addressbook/blob/master/addressbook-common/src/main/kotlin/com/addressbook/AddressBookDAO.kt) with **Spring Eureka**
    -   [Service location](https://github.com/dredwardhyde/addressbook/blob/master/addressbook-main/src/main/kotlin/com/addressbook/ignite/IgniteClient.kt) using **Spring Feign** and **Netflix Ribbon**
4. [**Spring Web**](https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html):
    -   [Exception handler for all back-end exceptions](https://github.com/dredwardhyde/addressbook/blob/master/addressbook-main/src/main/kotlin/com/addressbook/rest/MainController.kt#L121)
    -   [Custom 404 page & 401 handlers](https://github.com/dredwardhyde/addressbook/blob/master/addressbook-main/src/main/kotlin/com/addressbook/rest/ErrorWebController.kt) with [session listener](https://github.com/dredwardhyde/addressbook/blob/master/addressbook-main/src/main/kotlin/com/addressbook/configuration/SessionListener.kt)  
    -   [Automatic Jackson marshalling](https://github.com/dredwardhyde/addressbook/blob/master/addressbook-main/src/main/kotlin/com/addressbook/configuration/RootConfiguration.kt)
5.  [**Spring Flux**](https://docs.spring.io/spring/docs/current/spring-framework-reference/web-reactive.html) for [reactive updates on front-end](https://github.com/dredwardhyde/addressbook/blob/master/addressbook-main/src/main/kotlin/com/addressbook/services/JVMStateService.kt)
6.  [**Spring Fox**](https://springfox.github.io/springfox/docs/current/) [configuration](https://github.com/dredwardhyde/addressbook/blob/master/addressbook-main/src/main/kotlin/com/addressbook/configuration/SpringFoxConfiguration.kt) with Swagger2
7.  [**Apache Ignite**](https://apacheignite.readme.io/docs) [server configuration](https://github.com/dredwardhyde/addressbook/blob/master/ignite-server/src/main/kotlin/com/addressbook/server/dao/DAO.kt#L37), [SQL queries](https://github.com/dredwardhyde/addressbook/blob/master/ignite-server/src/main/kotlin/com/addressbook/server/dao/DAO.kt#L278), [transactions](https://github.com/dredwardhyde/addressbook/blob/master/ignite-server/src/main/kotlin/com/addressbook/server/dao/DAO.kt#L122), [binary marshalling](https://github.com/dredwardhyde/addressbook/blob/master/addressbook-common/src/main/kotlin/com/addressbook/model/Organization.kt)
8.  [**MongoDB with Morphia ORM**](https://github.com/dredwardhyde/addressbook/blob/master/mongo-server/src/main/kotlin/com/addressbook/server/dao/DAO.kt)  
9.  [**PostgreSQL with Spring JPA**](https://github.com/dredwardhyde/addressbook/blob/master/postgre-server/src/main/kotlin/com/addressbook/server/dao/DAO.kt)  

#### Front-end:
1.  Complex user interface with multiple dynamically added tables/tabs/custom forms, user-input validation and more
2.  [**react v15.3.1**](https://reactjs.org/blog/2016/04/07/react-v15.html), [**redux v3.6.0**](https://react-redux.js.org/), [**react-router v4.0.0**](https://reacttraining.com/react-router/web/guides/quick-start), component lifecycle, integration with Spring Security for REST-API
3.  [**react-bootstrap-table**](http://allenfang.github.io/react-bootstrap-table/) & [**react-bootstrap**](https://react-bootstrap.github.io/components/table/) components
4.  Webpack-dev-server & npm configuration & assembly (+ multiplatform maven configs)
5.  Heavy usage of HTTP Statuses & handling JS exceptions
6.  User notifications about all back-end and front-end events (+ exception handling) using react-bs-notifier
7.  Hierarchical menu with breadcrumbs and role-dependent feature availability
8.  Dynamically added/removed components using Maps

#### Tested by JUnit & Selenium (Chrome)

#### Deployment:
Build the following Docker images (Intellij Idea profiles available):

Build Spring Boot Web Application:
```sh
cd addressbook-main
docker build -t addressbook_web .
```
Build Spring Boot DPL Application with Apache Ignite Server:
```sh
cd ignite-server
docker build -t addressbook_ignite .
```
Build Spring Boot DPL Application with PostgreSQL:
```sh
cd postgre-server
docker build -t addressbook_postgre .
```
Build Spring Boot DPL Application with MongoDB:
```sh
cd mongo-server
docker build -t addressbook_mongo .
```
Build Spring Boot with Eureka Server:
```sh
cd eureka-server
docker build -t addressbook_eureka .
```
Start all:
```sh
cd ..
docker-compose -f docker-compose.yml up -d
```

#### PostgreSQL preparation:
```sql
CREATE SCHEMA IF NOT EXISTS test;
CREATE USER test WITH PASSWORD 'test';
GRANT ALL ON SCHEMA test TO test;
```

#### WebApp host: http://localhost:10000/  (user/userPass, admin/adminPass)
#### Eureka host: http://localhost:7777/  

### Docker Apps:
<img src="https://raw.githubusercontent.com/dredwardhyde/addressbook/master/deployment.png" width="900"/>  
  
### Eureka Services:
<img src="https://raw.githubusercontent.com/dredwardhyde/addressbook/master/eureka.png" width="900"/>  

### Swagger2 WebApp: http://localhost:10000/swagger-ui.html
<img src="https://raw.githubusercontent.com/dredwardhyde/addressbook/master/swagger-web.png" width="700"/>  

### Swagger2 DPL: http://localhost:11000/swagger-ui.html
<img src="https://raw.githubusercontent.com/dredwardhyde/addressbook/master/swagger-dao.png" width="700"/>  


### How to use CDS:

```sh
"C:\Java\BellSoft\LibericaJDK-14\bin\java.exe" -Xlog:cds=debug -Xshare:off -XX:DumpLoadedClassList=C:\Users\edwardhyde\IdeaProjects\addressbook\eureka.lst -jar C:\Users\edwardhyde\IdeaProjects\addressbook\eureka-server\target\eureka-server.jar com.addressbook.eureka.EurekaMain
```
```sh
"C:\Java\BellSoft\LibericaJDK-14\bin\java.exe" -Xlog:cds=debug -Xshare:dump -XX:SharedClassListFile=C:\Users\edwardhyde\IdeaProjects\addressbook\eureka.lst -XX:SharedArchiveFile=C:\Users\edwardhyde\IdeaProjects\addressbook\eureka.jsa -jar C:\Users\edwardhyde\IdeaProjects\addressbook\eureka-server\target\eureka-server.jar com.addressbook.eureka.EurekaMain
```
```sh
"C:\Java\BellSoft\LibericaJDK-14\bin\java.exe" -Xlog:cds=debug -Xshare:on -XX:SharedArchiveFile=C:\Users\edwardhyde\IdeaProjects\addressbook\eureka.jsa -XX:ArchiveClassesAtExit=C:\Users\edwardhyde\IdeaProjects\addressbook\eureka-ext.jsa -jar C:\Users\edwardhyde\IdeaProjects\addressbook\eureka-server\target\eureka-server.jar com.addressbook.eureka.EurekaMain 
```
```sh
"C:\Java\BellSoft\LibericaJDK-14\bin\java.exe" -Xlog:cds=debug -Xshare:on -XX:SharedArchiveFile=C:\Users\edwardhyde\IdeaProjects\addressbook\eureka-ext.jsa -jar C:\Users\edwardhyde\IdeaProjects\addressbook\eureka-server\target\eureka-server.jar com.addressbook.eureka.EurekaMain 
```

### How CDS affects statup time:  
#### Before
<img src="https://raw.githubusercontent.com/dredwardhyde/addressbook/master/cds-without.PNG" width="900"/>  

#### After
<img src="https://raw.githubusercontent.com/dredwardhyde/addressbook/master/cds-with.PNG" width="900"/>  
