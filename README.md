# What this APP does?
- User can search nearby restuarants, charging stations, parking spots by provinding a Place/Location input. For Eg. Starbucks Berlin
- The app uses HERE MAPS API to fetch 3 closest POI's for each type and displays them in the response.
- 
# Key Features of the APP
- It uses Spring Reactive JAVA (Mono, FLux) to call services in asynchronously.
- All 3 services are called in parallel with results from HERE MAPS API cached.
- Caching results avoid multiple calls to HERE MAPS API 

#### Input
- Place/Location Name: 'Starbucks Berlin'

#### Output
```
	{
	    "restaurants": [
	        {
	            "distance": 1,
	            "title": "Dummy Restaurant1"
	        },
	        {
	            "distance": 2,
	            "title": "Dummy Restaurant2"
	        },
	        {
	            "distance": 3,
	            "title": "Dummy Restaurant3"
	        }
	    ],
	    "charging-stations": [
	        {
	            "distance": 1,
	            "title": "Dummy Charging Station 1"
	        },
	        {
	            "distance": 2,
	            "title": "Dummy Charging Station 2"
	        },
	        {
	            "distance": 3,
	            "title": "Dummy Charging Station 3"
	        }
	    ],
	    "parking-spots": [
	        {
	            "distance": 1,
	            "title": "Dummy Parking Spot 1"
	        },
	        {
	            "distance": 2,
	            "title": "Dummy Parking Spot 2"
	        },
	        {
	            "distance": 3,
	            "title": "Dummy Parking Spot 3"
	        }
	    ]
	} 
```

###### 
The app has been dockerized. Docker images for individual microservices can be found at https://hub.docker.com/repositories/murtuza5689


## How to Run App on LOCAL using DOKCER


```
1) git clone https://github.com/murtuza5689/mercedes-api
2) cd mercedes-api
3) Go to indivudual folders (i.e. /charging-station-service, /parking-spot-service, /euraka-naming-server, /search-api, /restaurant-service) and RUN './gradlew clean build' to build *.jar' file
3) Return back to main folder (/mercedes-api)
4) Run 'docker-compose up' cmd. This will build docker images in a correct order from the docker-compose.yml file present at root.
```

## Ports

|     Application       |     Port          |
| ------------- | ------------- |
| Parking Lot Service | 8803 |
| Charging Station Service | 8802, ...  |
| Restaurant Service | 8801, ... |
| Netflix Eureka Naming Server | 8761 |
| Search API | 8081 |

## URLs

|     Application       |     URL          |
| ------------- | ------------- |
| Parking Lot Service - Indirect call from Search API| http://localhost:8803/api/v1/parking/52.5159,13.3777 |
| Charging Station Service - Indirect call from Search API| http://localhost:8802/api/v1/station/52.5159,13.3777|
| Restaurant Service - Indirect call from Search API| http://localhost:8801/api/v1/restaurant/52.5159,13.3777|
| Search API Server - Direct call | http://localhost:8081/v1/search?q=Starbucks Berlin|
| Eureka | http://localhost:8761/|
## VM Argument

-Djasypt.encryptor.password=<PWD_SENT_ON_EMAIL>

## More additions that could be done to the app
- Implementing ZUUL api server and tracing request using Cloud Sleuth and Zipkin.
- Spring Cloud config server can be implemented to store properties of all microservices w.r.t to different profiles like test/dev/prod
- Parking Lot Service has not been implemented. (Could not find API in HERE MAPS)
