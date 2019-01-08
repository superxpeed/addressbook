# Address Book (Skeleton Spring MVC application)

#### Contains following useful snippets:
####Back-end:
1. Spring Boot: 
    -   Java Configuration
    -   Maven dependencies
    -   Fat-jar assembly (with React static)
    -   Setting http-session duration
    -   Static resource handler
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
5.  Apache Ignite client configuration, start/stop at Spring context start/closing, SQL queries, transactions, 
monitoring, binary marshalling

####Front-end:
1.  complicated user interface with multiple dynamically added tables/tabs/custom forms, user-input validation and more
2.  react, redux, react-router, component lifecycle, fetch, custom headers, credentials (JSESSIONID) for REST-API
3.  react-bootstrap-table & react-bootstrap components
4.  webpack-dev-server & npm configuration & assembly (including maven)
5.  working with http statuses & js exceptions
6.  user notifications about all back-end and front-end exceptions
7.  hierarchical menu with breadcrumbs and role-dependent availability
8.  locking table records for user editing with notifications
9.  working with refs of dynamically added/removed children components