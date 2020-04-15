# Addressbook (Spring Framework Application Example)

#### Back-end:
1. [**Spring Boot**](https://spring.io/projects/spring-boot): 
    -   Maven dependency management
    -   Single executable Jar assembly (with React static)
    -   Setting http-session duration
    -   Static resource handler
2.  [**Spring Security**](https://spring.io/projects/spring-security):
    -   Custom AuthenticationProvider
    -   Custom UrlAuthenticationSuccessHandler for REST API
    -   Custom role model
    -   External user management
3.  [**Spring Cloud Netflix**](https://spring.io/projects/spring-cloud-netflix):
    -   Service registration with **Spring Eureka**
    -   Service location using **Spring Feign** and **Netflix Ribbon**
4. [**Spring Core**](https://docs.spring.io/spring-framework/docs/current/spring-framework-reference/core.html):
    -   Exception handler for all back-end exceptions
    -   Custom 404 page & 401 handlers with session listener 
    (using ErrorController, ExceptionHandler and HttpSessionListener)
    -   Automatic Jackson marshalling
5.  [**Spring Flux**](https://docs.spring.io/spring/docs/current/spring-framework-reference/web-reactive.html) for reactive updates on front-end
6.  [**Spring Fox**](https://springfox.github.io/springfox/docs/current/) with Swagger2
7.  [**Apache Ignite**](https://apacheignite.readme.io/docs) client configuration, start/stop at Spring context start/closing, SQL queries, transactions, 
monitoring, binary marshalling

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
docker build -t addressbook_db .
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

#### WebApp host: http://localhost:10000/  
#### Eureka host: http://localhost:7777/  

### Docker Apps:
<img src="https://raw.githubusercontent.com/dredwardhyde/addressbook/master/deployment.png" width="900"/>  
  
### Eureka Services:
<img src="https://raw.githubusercontent.com/dredwardhyde/addressbook/master/eureka.png" width="900"/>  

### Swagger2 WebApp: http://localhost:10000/swagger-ui.html
<img src="https://raw.githubusercontent.com/dredwardhyde/addressbook/master/swagger-web.png" width="700"/>  

### Swagger2 DPL: http://localhost:11000/swagger-ui.html
<img src="https://raw.githubusercontent.com/dredwardhyde/addressbook/master/swagger-dao.png" width="700"/>  
