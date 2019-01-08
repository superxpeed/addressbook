# Address Book (Skeleton Spring MVC application)

#### Contains following useful snippets:
####Back-end:
1. Spring Boot: 
    -   Java Configuration
    -   Maven dependencies
    -   Fat-jar assembly (with React static)
    -   Setting http-session duration
2.  Spring Security:
    -   Custom AuthenticationProvider
    -   Custom UrlAuthenticationSuccessHandler for REST API
    -   Retrieving current user with roles
3. Spring Core:
    -   Exception handler for all back-end exceptions
    -   Custom 404 page & 401 handlers with session listener 
    (using ErrorController, ExceptionHandler and HttpSessionListener)
    -   Automatic Jackson marshalling
4.  Spring Flux for reactive updates on front-end
5.  Ignite client configuration, start/stop at Spring context start/closing, SQL queries, transactions, 
monitoring.

####Front-end:
1.  React, Redux, React-router, component lifecycle, fetch, custom headers, credentials (JSESSIONID) for REST-API,
2.  React-bootstrap-table & React-bootstrap components
3.  webpack-dev-server & npm configuration & assembly (including maven)
4.  Working with http statuses & js exceptions
5.  User notifications about all back-end and front-end exceptions
6.  Hierarchical menu with breadcrumbs and role-dependent availability
7.  Locking model with user notifications
8.  Working with refs of dynamically added/removed children components