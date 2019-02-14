
# Aviation Pre processing

Application will gather data regarding airplane current position.

Airplane live statistics computation for presentation.



Processing Data
Raw data have to be deduplicated - snapshot for same flight with same timestamp has to merger (only one should be left)
Data cleaning - apply simple rules, that remove unbelievable data. cleaning rules - TODO
For none existing snapshot (flightNumber,timestamp), inputation/projection has to be made - add information regarding position for flight.
Normalized According time - calculate position(speed, location, etc) of all snapshot normalized to same Time Windows(time 19:00, 19:05, etc)

Make several aggregation based on different condition. Some kind of real time statistics presented in Kibana.
1. Density of air traffic - how many airplanes is at particular position (rectangle geo position)
2. How many planes flow through some position (rectangle geo position) through some time
3. Plane, Airline statistic (how many flight, distance etc)






