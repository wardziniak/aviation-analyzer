
# Aviation Data Analyzer

This project is a part of dissertation for Big Data postgraduaties studies in Warsaw University of Technology.
Aim of the project is to write to a tool for importing and analyzing Aviation Data, that are available in https://aviation-edge.com. 

### Aviation-Edge Data Source

Aviation-Edge lets users to access to global Database and API regarding Aviation. 

There is lot of different data, some of them can be accessed in almost real time, others can be downloaded as databases.
Description of available data sets in the Aviation-Edge.
* Rest Api
  * Flight tracker
  * Airport timetable
  * Airline routes 
  * Airports
  * Airlines
  * Aircrafts
  * Cities
  * Countries
* Databases to download
  * Airports
  * Airlines
  * Aircrafts
  * Cities
  * Countries

To play with sample data. You have to only register at https://aviation-edge.com.
To access to rest API you can use any rest Client or go to https://aviation-edge.com/get.php, where you can check data through web page.




Parts:

Kafka Streams is used for preprocess data for spark. 
At spark model will

To start application:
1. Setup environment with script: ./environment_setup.sh // Note, that kafka-connect plugins have to placed in proper directory
2. Load Kafka Connect Sink configuration to copy data to mongo 
3. Import Airport information
4. Start loading real data regarding Flights
5. Start preprocessing application
6. Run Train model app
7. Run predication landing information app 





