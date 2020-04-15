# Addressbook (Spring Framework Application Example)

#### Back-end:
1. Spring Boot: 
    -   Maven dependency management
    -   Single executable Jar assembly (with React static)
    -   Setting http-session duration
    -   Static resource handler
2.  Spring Security:
    -   Custom AuthenticationProvider
    -   Custom UrlAuthenticationSuccessHandler for REST API
    -   Custom role model
    -   External user management
3.  Spring Cloud Netflix:
    -   Service registration with Spring Eureka
    -   Service location using Spring Feign and Ribbon
4. Spring Core:
    -   Exception handler for all back-end exceptions
    -   Custom 404 page & 401 handlers with session listener 
    (using ErrorController, ExceptionHandler and HttpSessionListener)
    -   Automatic Jackson marshalling
4.  Spring Flux for reactive updates on front-end
5.  Apache Ignite client configuration, start/stop at Spring context start/closing, SQL queries, transactions, 
monitoring, binary marshalling

#### Front-end:
1.  Complex user interface with multiple dynamically added tables/tabs/custom forms, user-input validation and more
2.  React, redux, react-router, Component lifecycle, integration with Spring Security for REST-API
3.  React-bootstrap-table & React-bootstrap components
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
Build Spring Boot with Apache Ignite Server:
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
