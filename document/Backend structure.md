# Backend structure

Packages:

* controller: make HTTP requests to BigQuery using service's functions
* model: classes to define entities
* exception: for handling exception while processing
* service: include functions for querying to get data from BigQuery and parse it to response

Detail:

* model: contains 2 classes Trip and Station as main entities of the app
* As we have mentioned entities, the service and controller should have 2 classes to handle for each entity too
* exception: handle exception while app can't connect to BQ, or there are problems with data
