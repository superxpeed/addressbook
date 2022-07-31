# Spring Ecosystem Backbone Single Page Application
### Spring Boot/Cloud/Eureka/Netflix/MVC using React/Redux for UI and MongoDB/PostgreSQL/Apache Ignite for data persistence

#### Back-end:
1. [**Spring Boot**](https://spring.io/projects/spring-boot): 
    -   [Maven dependency management for Spring & Kotlin & NPM](https://github.com/dredwardhyde/addressbook/blob/master/addressbook-main/pom.xml)
    -   [Single layered executable **.jar** assembly (with static resources)](https://github.com/dredwardhyde/addressbook/blob/master/addressbook-main/pom.xml)
    -   [Resource compression](https://github.com/dredwardhyde/addressbook/blob/master/addressbook-main/src/main/resources/application.yml#L5)  
    -   [Static resource handler](https://github.com/dredwardhyde/addressbook/blob/master/addressbook-main/src/main/kotlin/com/addressbook/configurations/WebConfiguration.kt)
2.  [**Spring Security with JWT**](https://spring.io/projects/spring-security):
    -   [Security configuration](https://github.com/dredwardhyde/addressbook/blob/master/addressbook-main/src/main/kotlin/com/addressbook/security/SecurityConfiguration.kt)  
    -   [X.509 Authentication](https://github.com/dredwardhyde/addressbook/blob/master/addressbook-main/src/main/resources/application.yml#L10) with JWT [integration](https://github.com/dredwardhyde/addressbook/blob/master/addressbook-main/src/main/kotlin/com/addressbook/index/IndexWebController.kt#L37) using [cookies](https://github.com/dredwardhyde/addressbook/blob/master/addressbook-main/src/main/react/src/Common/Utils.js#L50)
    -   [JWT provider](https://github.com/dredwardhyde/addressbook/blob/master/addressbook-main/src/main/kotlin/com/addressbook/security/JwtProvider.kt)
    -   [JWT request filter](https://github.com/dredwardhyde/addressbook/blob/master/addressbook-main/src/main/kotlin/com/addressbook/security/JwtFilter.kt) for REST API
    -   [Role model](https://github.com/dredwardhyde/addressbook/blob/master/addressbook-main/src/main/kotlin/com/addressbook/dao/UserCreator.kt) for [feature availabilty](https://github.com/dredwardhyde/addressbook/blob/master/addressbook-main/src/main/kotlin/com/addressbook/dao/MenuCreator.kt)
    -   External user management
3.  [**Spring Cloud Netflix**](https://spring.io/projects/spring-cloud-netflix):
    -   [Service registration](https://github.com/dredwardhyde/addressbook/blob/master/addressbook-common/src/main/kotlin/com/addressbook/AddressBookDAO.kt) with **Spring Eureka**
    -   [Service location](https://github.com/dredwardhyde/addressbook/blob/master/addressbook-main/src/main/kotlin/com/addressbook/dao/DaoClient.kt) using **Spring Feign** and **Netflix Ribbon**
4. [**Spring Web**](https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html):
    -   [Exception handler for all back-end exceptions](https://github.com/dredwardhyde/addressbook/blob/master/addressbook-main/src/main/kotlin/com/addressbook/rest/MainController.kt#L132)
    -   [404 page & 401 handlers](https://github.com/dredwardhyde/addressbook/blob/master/addressbook-main/src/main/kotlin/com/addressbook/rest/ErrorWebController.kt)  
    -   [Automatic JSON marshalling](https://github.com/dredwardhyde/addressbook/blob/master/addressbook-main/src/main/kotlin/com/addressbook/configurations/RootConfiguration.kt)
5.  [**Spring Flux**](https://docs.spring.io/spring/docs/current/spring-framework-reference/web-reactive.html) for [reactive updates on front-end](https://github.com/dredwardhyde/addressbook/blob/master/addressbook-main/src/main/kotlin/com/addressbook/services/JVMStateService.kt)
6.  [**Spring Doc**](https://springdoc.org) [configuration](https://github.com/dredwardhyde/addressbook/blob/master/addressbook-main/src/main/kotlin/com/addressbook/configurations/RootConfiguration.kt#L20) 
7.  [**Apache Ignite**](https://apacheignite.readme.io/docs) [server configuration](https://github.com/dredwardhyde/addressbook/blob/master/ignite-server/src/main/kotlin/com/addressbook/server/dao/DAO.kt#L37), [generic SQL queries](https://github.com/dredwardhyde/addressbook/blob/master/ignite-server/src/main/kotlin/com/addressbook/server/dao/DAO.kt#L262), [transactions](https://github.com/dredwardhyde/addressbook/blob/master/ignite-server/src/main/kotlin/com/addressbook/server/dao/DAO.kt#L109), [binary marshalling](https://github.com/dredwardhyde/addressbook/blob/master/addressbook-common/src/main/kotlin/com/addressbook/model/AddressBookEntities.kt#L17)
8.  [**MongoDB with Morphia ORM**](https://github.com/dredwardhyde/addressbook/blob/master/mongo-server/src/main/kotlin/com/addressbook/server/dao/DAO.kt)  
9.  [**PostgreSQL with Spring JPA**](https://github.com/dredwardhyde/addressbook/blob/master/postgre-server/src/main/kotlin/com/addressbook/server/dao/DAO.kt)  
10. [**UI tests using Selenium**](https://github.com/dredwardhyde/addressbook/tree/master/addressbook-main/src/test/kotlin/com/addressbook/test)
#### Front-end:
1.  User interface with multiple dynamically added tables/tabs/custom forms, input validation and more
2.  [**react v15.3.1**](https://reactjs.org/blog/2016/04/07/react-v15.html), [**redux v3.6.0**](https://react-redux.js.org/), [**react-router v4.0.0**](https://reacttraining.com/react-router/web/guides/quick-start), component lifecycle, integration with Spring Security for REST API
3.  [**react-bootstrap-table**](http://allenfang.github.io/react-bootstrap-table/) & [**react-bootstrap**](https://react-bootstrap.github.io/components/table/) components
4.  **webpack-dev-server** & **npm** configuration & assembly with compression (+ multiplatform **Maven** configuration)
5.  Handling HTTP Statuses & JS exceptions
6.  User notifications of all back-end and front-end events (+ exception handling) using **react-bs-notifier**
7.  Hierarchical menu with breadcrumbs and role-dependent feature availability
8.  Dynamic collections of child components

#### [DevOps](https://github.com/dredwardhyde/addressbook/blob/master/devops/instructions.md):
1.  [**Ansible** playbook](https://github.com/dredwardhyde/addressbook/tree/master/devops/ansible)  
2.  [**Kubernetes** templates:](https://github.com/dredwardhyde/addressbook/tree/master/devops/templates)
    - [External **logback** configuration](https://github.com/dredwardhyde/addressbook/blob/master/devops/templates/web/cm-web-logback.yaml)
    - [**Secret** as **Volume**](https://github.com/dredwardhyde/addressbook/blob/master/devops/templates/web/cm-web-logback.yaml)
    - [**ConfigMap**](https://github.com/dredwardhyde/addressbook/blob/master/devops/templates/web/cm-web-app-settings.yaml)
    - [**Service**](https://github.com/dredwardhyde/addressbook/blob/master/devops/templates/web/svc-web-api.yaml)
    - [**Ingress**](https://github.com/dredwardhyde/addressbook/blob/master/devops/templates/web/web-ingress.yaml)
3.  [**docker-compose**](https://github.com/dredwardhyde/addressbook/blob/master/devops/docker-compose-ignite.yml)
4.  [**.zip** artifact assembly](https://github.com/dredwardhyde/addressbook/blob/master/build-distrib/pom.xml#L23)
5.  [**Dockerfile** for layered Spring Boot .jar](https://github.com/dredwardhyde/addressbook/blob/master/addressbook-main/Dockerfile)
#### PostgreSQL setup:
```sql
CREATE SCHEMA IF NOT EXISTS test;
CREATE USER test WITH PASSWORD 'test';
GRANT ALL ON SCHEMA test TO test;
```

