# Requirement summary for team Report project

## General Information

Name: Web system for managing and analysing data of Bike stations and trips

Technology to be used:

* Backend: Spring Boot
* Frontend: VueJS
* Other:
  * Google Cloud Platform: BigQuery, Google Maps API
  * Apache POI
  * Chart.js/D3.js

## Function

* Get BigQuery data by calling APIs from Spring Boot
* Show data on table on UI with VueJS
* Search function:
  * Search for records by using 1 field
  * Search for records by using multiple fields
* Export query result to excel file
* Display chart showing fixed query result
* Show bike station location on Google Maps

## Work schedule

Note: Details from this list are subject to change

* Week 4 (21/10 - 25/10): Design system and set up environment

  * Summarize requirement
  * Design frontend screens: Data table, query form, route to charts and maps (if needed)
  * Design backend APIs: search APIs (can use @RequestParam to use for both 1 field and multiple fields search)

    Note: Consider using pagination for query (need to research more)

  * Create repository on GitHub
  * Create backend project with Spring Initializr
  * Create frontend project with Vuetify (using Vite)
  * Connect BigQuery service
  * Verify project structure with mentor

* Week 5 (28/10 - 1/11): Show data and implement search with 1 field

  * Implement API for {/query1field}
  * Integrate with frontend to show the table
  * Implement API for search data with 1 field {/query}
  
* Week 6 (4/11 - 8/11): Update API for searching with multiple fields

  * Update API for {/querymanyfields}
  * Update frontend for choosing multiple fields
    Note: Do we need to update frontend for showing result screen?

* Week 7 (11/11 - 15/11): Export to Excel

  * Implement Apache POI to export excel file

* Week 8 (18/11 - 22/11): Charts

  * Implement chart.js/d3.js for top 10, top 5,... fixed query

* Week 9 (25/11 - 29/11): Google Maps API

  * Use Maps API to show location of the station on the map
  * Allow user to click on name of station to show location

* Week 10 (2/12 - 6/12): Test and fix bug

  * Create and execute test (need to research more)
  * Fix bug if there is any

* Week 11 (9/12 - 13/12): Deploy and review

  * Deploy
  * Final review with mentor and manager
  
* Week 12 (16/12 - 20/12): Buffer time
  
* Week 13 (23/12 - 24/12): End Internship program

## Questions

* Week 8: Do we need to edit chart with query result, like top 10 of ... from query result?
* Search UI: For fields like start_station_name,... use input field or a dropdown of choices?
* Test: Use JUnit to test in Spring Boot or just use Postman?

## Note

@review mentor to request review
