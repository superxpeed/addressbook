# Spring Ecosystem Backbone SPA With k8s/Istio Support

## Client applications

- ### [Electron Desktop Application For macOS & Windows](https://github.com/dredwardhyde/addressbook-desktop-app)
<img src="https://raw.githubusercontent.com/dredwardhyde/addressbook-desktop-app/master/readme/macos_installed_app_light.png" width="400"/>  <img src="https://raw.githubusercontent.com/dredwardhyde/addressbook-desktop-app/master/readme/macos_installed_app_dark.png" width="400"/>  
- ### [Android Client Application](https://github.com/dredwardhyde/addressbook-android-app)

  #### Light Theme
  <img src="https://raw.githubusercontent.com/dredwardhyde/addressbook-android-app/master/screenshots/all_panels_light.png" width="1000"/>  

  #### Dark Theme
  <img src="https://raw.githubusercontent.com/dredwardhyde/addressbook-android-app/master/screenshots/all_panels_dark.png" width="1000"/>  

- ### Web Client Application

  https://user-images.githubusercontent.com/8986329/216774063-d909e32a-14d9-4639-bdcc-7f61118c9483.mp4

## Architecture

#### Back-end
1. [**Spring Boot**](https://spring.io/projects/spring-boot) 
    -   [Maven dependency management for Spring & Kotlin & npm](https://github.com/dredwardhyde/addressbook/blob/master/addressbook-main/pom.xml)
    -   [Single layered executable **.jar** assembly (with static resources)](https://github.com/dredwardhyde/addressbook/blob/master/addressbook-main/pom.xml)
    -   [Resource compression](https://github.com/dredwardhyde/addressbook/blob/master/addressbook-main/src/main/resources/application.yml#L5)  
    -   [Static resource handler](https://github.com/dredwardhyde/addressbook/blob/master/addressbook-main/src/main/kotlin/com/addressbook/configurations/WebConfiguration.kt)
2. [**Spring Security with JWT**](https://spring.io/projects/spring-security)
    -   [Security configuration](https://github.com/dredwardhyde/addressbook/blob/master/addressbook-main/src/main/kotlin/com/addressbook/security/SecurityConfiguration.kt)  
    -   [X.509 Authentication](https://github.com/dredwardhyde/addressbook/blob/master/addressbook-main/src/main/resources/application.yml#L10) with JWT [integration](https://github.com/dredwardhyde/addressbook/blob/master/addressbook-main/src/main/kotlin/com/addressbook/index/IndexWebController.kt#L37) using [cookies](https://github.com/dredwardhyde/addressbook/blob/master/addressbook-main/src/main/react/src/Common/Utils.js#L81)
    -   [JWT provider](https://github.com/dredwardhyde/addressbook/blob/master/addressbook-main/src/main/kotlin/com/addressbook/security/JwtProvider.kt)
    -   [JWT request filter](https://github.com/dredwardhyde/addressbook/blob/master/addressbook-main/src/main/kotlin/com/addressbook/security/JwtFilter.kt) for REST API
    -   [Role model](https://github.com/dredwardhyde/addressbook/blob/master/addressbook-main/src/main/kotlin/com/addressbook/dao/UserCreator.kt) for [feature availabilty](https://github.com/dredwardhyde/addressbook/blob/master/addressbook-main/src/main/kotlin/com/addressbook/dao/MenuCreator.kt)
    -   External user management
3. [**Spring Cloud Netflix**](https://spring.io/projects/spring-cloud-netflix)
    -   [Service registration](https://github.com/dredwardhyde/addressbook/blob/master/addressbook-common/src/main/kotlin/com/addressbook/AddressBookDAO.kt) with **Spring Eureka**
    -   [Service location](https://github.com/dredwardhyde/addressbook/blob/master/addressbook-main/src/main/kotlin/com/addressbook/dao/DaoClient.kt) using **Spring Feign** and **Netflix Ribbon**
4. [**Spring Web**](https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html)
    -   [Exception handler for all back-end exceptions](https://github.com/dredwardhyde/addressbook/blob/master/addressbook-main/src/main/kotlin/com/addressbook/rest/MainController.kt#L132)
    -   [404 page & 401 handlers](https://github.com/dredwardhyde/addressbook/blob/master/addressbook-main/src/main/kotlin/com/addressbook/rest/ErrorWebController.kt)  
    -   [Automatic JSON marshalling](https://github.com/dredwardhyde/addressbook/blob/master/addressbook-main/src/main/kotlin/com/addressbook/configurations/RootConfiguration.kt)
5. [**Spring Flux**](https://docs.spring.io/spring/docs/current/spring-framework-reference/web-reactive.html) for [reactive updates](https://github.com/dredwardhyde/addressbook/blob/master/addressbook-main/src/main/kotlin/com/addressbook/services/JVMStateService.kt) on [front-end](https://github.com/dredwardhyde/addressbook/blob/master/addressbook-main/src/main/react/src/Pages/AdminPageForm.js#L60#)
6. [**Spring Doc**](https://springdoc.org) [configuration](https://github.com/dredwardhyde/addressbook/blob/master/addressbook-main/src/main/kotlin/com/addressbook/configurations/RootConfiguration.kt#L20) 
7. [**Apache Ignite**](https://apacheignite.readme.io/docs) [server configuration](https://github.com/dredwardhyde/addressbook/blob/master/ignite-server/src/main/kotlin/com/addressbook/server/dao/DAO.kt#L37), [generic SQL queries](https://github.com/dredwardhyde/addressbook/blob/master/ignite-server/src/main/kotlin/com/addressbook/server/dao/DAO.kt#L262), [transactions](https://github.com/dredwardhyde/addressbook/blob/master/ignite-server/src/main/kotlin/com/addressbook/server/dao/DAO.kt#L109), [binary marshalling](https://github.com/dredwardhyde/addressbook/blob/master/addressbook-common/src/main/kotlin/com/addressbook/model/AddressBookEntities.kt#L17)
8. [**MongoDB with Morphia ORM**](https://github.com/dredwardhyde/addressbook/blob/master/mongo-server/src/main/kotlin/com/addressbook/server/dao/DAO.kt) and [**TLS 1.2**](https://github.com/dredwardhyde/addressbook/blob/master/devops/instructions.md#mongodb-with-tls-12)
9. [**PostgreSQL with Spring JPA**](https://github.com/dredwardhyde/addressbook/blob/master/postgre-server/src/main/kotlin/com/addressbook/server/dao/DAO.kt) and [**TLS 1.3**](https://github.com/dredwardhyde/addressbook/blob/master/devops/instructions.md#postgresql-15-with-tls-13)
10. [**UI tests using Selenium**](https://github.com/dredwardhyde/addressbook/tree/master/addressbook-main/src/test/kotlin/com/addressbook/test)  

#### Front-end
1.  User interface with multiple dynamically added tables/tabs/custom forms, input validation and more
2.  [**react v18.2.0**](https://reactjs.org/blog/2022/03/29/react-v18.html), [**react-redux v8.0.5**](https://react-redux.js.org), [**react-router v5.2.0**](https://v5.reactrouter.com), component lifecycle, integration with Spring Security for REST API
3.  [**Material React Table**](https://www.material-react-table.com) & [**Material UI v5**](https://mui.com/core) components
4.  [**webpack-dev-server**](https://github.com/dredwardhyde/addressbook/blob/master/addressbook-main/src/main/react/webpack.config.js#L56) & **npm** configuration & assembly with [**compression**](https://github.com/dredwardhyde/addressbook/blob/master/addressbook-main/src/main/react/webpack.prod.config.js#L17) (+ multiplatform **Maven** configuration)
5.  Dark & Light Themes with automatic switching
6.  Handling HTTP Statuses & JS exceptions
7.  User notifications of all back-end and front-end events (+ exception handling)  
8.  Hierarchical menu with breadcrumbs and role-dependent feature availability
9.  Dynamic collections of child components

#### [DevOps](https://github.com/dredwardhyde/addressbook/blob/master/devops/instructions.md)
1.  [**Ansible** playbook](https://github.com/dredwardhyde/addressbook/tree/master/devops/ansible)  
2.  [**Kubernetes** templates:](https://github.com/dredwardhyde/addressbook/tree/master/devops/templates)
    - [External **logback** configuration](https://github.com/dredwardhyde/addressbook/blob/master/devops/templates/web/cm-web-logback.yaml)
    - **Secret** as [**Volume**](https://github.com/dredwardhyde/addressbook/blob/master/devops/templates/web/dp-web.yaml#L89)
    - **Secret** as [**ConfigMap**](https://github.com/dredwardhyde/addressbook/blob/master/devops/templates/web/dp-web.yaml#L81)
    - **[Secrets](https://github.com/dredwardhyde/addressbook/tree/master/devops/secrets) encryption  using ansible-vault** 
    - [**ConfigMap**](https://github.com/dredwardhyde/addressbook/blob/master/devops/templates/web/cm-web-app-settings.yaml)
    - [**Service**](https://github.com/dredwardhyde/addressbook/blob/master/devops/templates/web/svc-web-api.yaml)
    - [**Ingress**](https://github.com/dredwardhyde/addressbook/blob/master/devops/templates/istio_x509/web-ingress.yaml)
    - [**ServiceEntry**](https://github.com/dredwardhyde/addressbook/blob/master/devops/templates/postgre/se-postgre-database.yaml)
    - [**Gateway**](https://github.com/dredwardhyde/addressbook/blob/master/devops/templates/postgre/gw-postgre-database.yaml)
    - [**VirtualService**](https://github.com/dredwardhyde/addressbook/blob/master/devops/templates/postgre/vs-postgre-database.yaml)
    - [**Egress Gateway**](https://github.com/dredwardhyde/addressbook/blob/master/devops/templates/egress/dp-addressbook-egress.yaml)
    - [**Ingress Gateway**](https://github.com/dredwardhyde/addressbook/blob/master/devops/templates/ingress/dp-addressbook-ingress.yaml)
    - [**mTLS inside namespace**](https://github.com/dredwardhyde/addressbook/blob/master/devops/templates/istio_https/pa-mtls.yaml)  
    - [**Fluent Bit sidecar**](https://github.com/dredwardhyde/addressbook/blob/master/devops/templates/web/dp-web.yaml#L90)  
    - [**Fluent Bit configuration for Elastic**](https://github.com/dredwardhyde/addressbook/blob/master/devops/templates/fluent_bit_web/cm-web-fluentbit.yaml)  
3.  [**docker-compose-ignite**](https://github.com/dredwardhyde/addressbook/blob/master/devops/docker-compose-ignite.yml)
4.  [**docker-compose**](https://github.com/dredwardhyde/addressbook/blob/master/devops/docker-compose.yml)
5.  [**.zip**](https://github.com/dredwardhyde/addressbook/blob/master/build-distrib/distr-zip.xml) artifact [**assembly**](https://github.com/dredwardhyde/addressbook/blob/master/build-distrib/pom.xml#L23)
6.  [**Dockerfile** for layered Spring Boot .jar](https://github.com/dredwardhyde/addressbook/blob/master/addressbook-main/Dockerfile)
7.  [**Building Docker images with Maven**](https://github.com/dredwardhyde/addressbook/blob/master/addressbook-main/pom.xml#L430)  

### [Project deployment](https://github.com/dredwardhyde/addressbook/blob/master/devops/instructions.md#deploy-project)
```shell
cd $PROJECT_ROOT/devops/ansible
# Ingress, Egress, Eureka, Ignite, WebApp, Fluent Bit 
ansible-playbook deploy.yaml --tags "ingress, egress, eureka, ignite, web, istio_https, fluent_bit_web"
```
<img src="https://raw.githubusercontent.com/dredwardhyde/addressbook/master/devops/readme/ignite_kiali.png" width="900"/>  

```shell
# Ingress, Egress, Eureka, Mongo, WebApp, Fluent Bit 
# Requires enabled TLS on MongoDB server
ansible-playbook deploy.yaml --tags "ingress, egress, eureka, mongo, web, istio_https, fluent_bit_web"
```
<img src="https://raw.githubusercontent.com/dredwardhyde/addressbook/master/devops/readme/mongo_kiali.png" width="900"/>  

```shell
# Ingress, Egress, Eureka, PostgreSQL, WebApp, Fluent Bit 
# Requires enabled TLS on PostgreSQL server
ansible-playbook deploy.yaml --tags "ingress, egress, eureka, postgre, web, istio_https, fluent_bit_web"
```
<img src="https://raw.githubusercontent.com/dredwardhyde/addressbook/master/devops/readme/postgre_kiali.png" width="900"/>  


#### PostgreSQL setup
[**How to enable TLS 1.3**](https://github.com/dredwardhyde/addressbook/blob/master/devops/instructions.md#postgresql-15-with-tls-13)  
```sql
CREATE SCHEMA IF NOT EXISTS test;
CREATE USER test WITH PASSWORD 'test';
GRANT ALL ON SCHEMA test TO test;
```
#### MongoDB setup
[**How to enable TLS 1.2**](https://github.com/dredwardhyde/addressbook/blob/master/devops/instructions.md#mongodb-with-tls-12)  
```mongodb-json-query
use addressbook
db.createUser({
    user: "user", pwd: "q1w2e3r4", roles: [{role: "readWrite", db: "addressbook"}]
})
```

