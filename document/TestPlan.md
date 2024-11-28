# Test Plan

## Issues found / Enhancement

|No |Issues                                                                                    |Status       |
|---|------------------------------------------------------------------------------------------|-------------|
|1  |`property_type` value ‘Any’ should be ‘’ when selecting then de-selecting                 |Fixed        |
|2  |Exported file should include query detail                                                 |To do        |
|3  |Reset button to delete all current params                                                 |Fixed        |
|4  |Modify timestamp format                                                                   |Fixed        |
|5  |Cut the excel sheet after each 1000000 rows                                               |In progress  |

## Stage 1: Stations table

|No |Input parameters                                                                          |(Expected) result|Status|Export Status|Note|
|---|------------------------------------------------------------------------------------------|-----------------|:----:|:-----------:|----|
|1  |name = ‘th’                                                                               |56 records       |Tested|   Tested    |    |
|2  |name = ‘th’, status = ‘active’                                                            |42 records       |Tested|   Tested    |    |
|3  |name = ‘th’, status = ‘active’, address = ‘17’                                            |2 records        |Tested|   Tested    |Found bug when select then de-select property_type parameter on UI (Issue #1)    |
|4  |status = ‘active’, property_type =  ‘parkland’                                            |6 records        |Tested|   Tested    |    |
|5  |status = ‘active’, power_type = ‘solar’, footprint_width = [5.5; 12]                      |0 records        |Tested|   Tested    |    |
|6  |number_of_docks = [10; 22], footprint_length = [19; 44], footprint_width = [3,5; 14,5]    |56 records       |Tested|   Tested    |    |
|7  |address = ‘Red River Street’                                                              |1 record         |Tested|   Tested    |    |
|8  |power_type = ‘non-meter’                                                                  |3 records        |Tested|   Tested    |    |
|9  |status = ‘active’, footprint_length = [25; 50], footprint_width = [10; 15]                |1 record         |Tested|   Tested    |    |
|10 |name = ‘square’, number_of_docks = [10; 30]                                               |2 records        |Tested|   Tested    |    |
|11 |                                                                                          |101 records      |Tested|   Tested    |    |
|12 |property_type =  ‘undetermined_parking’, number_of_docks <= 15, footprint_length <= 40    |16 records       |Tested|   Tested    |    |
|13 |number_of_docks <= 15, footprint_length = [25; 40]                                        |53 records       |Tested|   Tested    |    |
|14 |name = ‘th’, status = ‘active’, address = ‘t’, property_type=’sidewalk’, number_of_docks = [5; 26], footprint_length = [11; 60], footprint_width = [2,5; 17]|12 records      |Tested|   Tested    |    |
|15 |status = ‘active’, number_of_docks >= 10, foot_print_length >= 30, footprint_width >= 7.5 |4 records        |Tested|   Tested    |    |

## Stage 2: Trips table

|No |Input parameters                                                                          |(Expected) result                     |Status|Export Status|Charts Status|Note|
|---|------------------------------------------------------------------------------------------|--------------------------------------|:----:|:-----------:|:-----------:|----|
|1  |trip_id = ‘1’                                                                             |1551200 records, export in 4 min      |Tested|   Tested    | Tested      |Found bug when exporting large file that exceeds max number of rows of a sheet (#5)|
|2  |                                                                                          |2271152 records, crash in about 5 min |Tested|   Failed    |             |Returned error “This archive contains unclosed entries” – to be fixed|
|3  |trip_id = '123', duration = [7, 10]                                                       |2835 records                          |Tested|Tested       |Tested       ||
|4  |bike_type = 'electric', start_station_name = 'henderson'                                  |6288 records                          |Tested|Tested       |Tested       ||
|5  |start_time = ['01-01-2022 00:00', '02-01-2022 00:00']                                     |14513 records                         |Tested|Tested       |Tested       ||
|6  |trip_id = '76', subscriber_type = '365', bike_type = 'electric', duration = [10, 20]      |2313 records                          |Tested|Tested       |Tested       ||
|7  |trip_id = '56', bike_type = 'electric', end_station = '3rd', start_time = ['01-01-2022 00:00', '12-31-2022 23:59'], duration <= 20|1110 records|Tested|Tested|Tested||
|8  |start_station_name = '8th/Congress', end_station_name = '8th/Congress'                    |1162 records                          |Tested|Tested       |Tested       ||
|9  |start_station_name = '6th/West', duration >= 15, start_time < '06-28-2022 12:00'          |4487 records                          |Tested|Tested       |Tested       ||
|10 |bike_id = '19274'                                                                         |2755 records                          |Tested|Tested       |Tested       ||
