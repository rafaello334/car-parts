# car-parts
Rest API which returns information about available car parts

Swagger url:
http://localhost:8080/swagger-ui.html#/

Actuator:
http://localhost:8080/actuator

Deployment:
Application is deployed to Heroku:
https://car-parts-interview.herokuapp.com/swagger-ui.html


#Project content:
Used technologies:
    java 11
    spring-boot
    spring-mvc
    spring-data-jpa
    spring-fox (swagger)
    spring-actuator (all endpoints enabled)
    postgres database
    
Project structure:

    java:
        aspects
            LoggerAspect.java - Aspect performing application logs for each method invocation in service package with all method params and returned value
        configuration
            AspectConfig.java - Aspect config class enabling EnableAspectJAutoProxy
            PersistenceJPAConfig.java - spring data configuration with repository classes scan
            SwaggerConfig.java - Swagger configuration
        controller
            CarPartsController.java - I've decided to put all endpoints in one controller class because there are connected with one main table (part) so it was not worth to split that
        dao
            model
                entities
                    Part.java - entity class for Part table
                    SalesArgument.java - entity class for SalesArgument table
                    ServiceAction.java - entity class for ServiceAction table
                PartAvailability.java - model used to return json value for partAvailability endpoint
            repositories
                PartRepository.java - database access class to Part table
                SalesArgumentRepository.java - database access class to SalesArgument table
                ServiceActionRepository.java - database access class to ServiceAtion table
        service
            CarPartsService - service class for CarPartsController with main logic to return proper data for specific endpoints using database repositories.
        CarPartsApplication - Spring boot main class which starts application
          
    resources: 
        application.properties - common configuration for spring data, swagger and actuator
        data.sql - initial insert scripts to prepare data on postgres database on application startup
        
    test
        CarPartsControllerTest - class which test controller endpoints using MockMVC
        CarPartsServiceTest - class testing service layer 
    resources:
        service-action.json - json request for addServiceAction endpoint