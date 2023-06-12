# Airborne Pollutant Pathfinder (APP)

This backend is responsible for providing the API for the Airborne Pollutant Pathfinder (APP) project. It uses GraphQL,
Spring Boot, Google Directions API, and GraphHopper. The front-end can be found [here](https://github.com/Airborne-Pollutant-Pathfinder/APP-interface).

The project aims to create an Android application called Airborne Pollutant Pathfinder (APP), which will enable users to
navigate from point A to point B while considering their allergies and pollen count in different locations. The backend
supports computing the fastest and safest routes and finding relevant sensors for a particular geolocation.

## Video

Click below to see our video for the app.

[![Watch the video](https://i.imgur.com/OBm9FRB.png)](https://youtu.be/z_J9tR2n-vY)

## Setup

1. Add the `GOOGLE_API_KEY` to your environment variables. IntelliJ has native support for this if 
   you edit the configuration settings. More detailed information on how to add environment
   variables to your IntelliJ run configurations can be found 
   [here](https://www.jetbrains.com/help/objc/add-environment-variables-and-program-arguments.html#add-environment-variables).
2. If using the `DallasOSMFileProvider` (this is the default behaviour), download data extracted from BBBike (as of 
   April 10, 2023) for Dallas 
   [here](https://cometmail-my.sharepoint.com/:u:/g/personal/jjp160630_utdallas_edu/EdrAdf1TrIdFumvKV8GIZ6cBgPfMQwgkTuHwvgzG4dns4A?e=6nTln6). Place the
   downloaded file in the `infrastructure/src/main/resources/maps` directory, naming it simply `dallas` (no file extension).
   - Note that if the link above does not work, you will have to extract a new OSM PBF file. You can go to 
     BBBike's website to do that 
     [here](https://extract.bbbike.org/?sw_lng=-97.395&sw_lat=32.635&ne_lng=-96.024&ne_lat=33.309&format=osm.pbf&city=Dallas&lang=en),
     with the coordinates already defined. All you will have to do is provide an email for it to email you a link to
     download the OSM PBF file.
3. The database must first be loaded before running the backend application. More instructions can be found 
   [here](https://github.com/Airborne-Pollutant-Pathfinder/APP-database).
4. As we are using Spring Boot, you will need Java 17 or above.
   You will need to ensure the Project Settings SDK is directed to your Java 17 folder. You should also check for your Gradle JVM,
   located at Settings -> Build, Execution, Deployment -> Build Tools -> Gradle, to be directed at the same folder.

![](https://i.imgur.com/g3dg3Ls.png)

**Note:** `AZURE_MAPS_CLIENT_ID` and `AZURE_MAPS_SHARED_KEY_ID` are now deprecated and no 
longer needed.


### Useful IntelliJ Plugins

- [Lombok](https://plugins.jetbrains.com/plugin/6317-lombok) - You will also need to enable annotation processing in 
  File | Settings | Build, Execution, Deployment | Compiler | Annotation Processors.
- [Spring](https://plugins.jetbrains.com/plugin/20221-spring)
- [GraphQL](https://plugins.jetbrains.com/plugin/8097-graphql)

## Running

The main method is located in `web/src/main/java/edu/utdallas/cs/app/presentation/Main.java`. You can run the code from 
there.

This project uses GraphQL. The GraphQL schema is located in `web/src/main/resources/schema.graphqls`. You can use
GraphQL playground to test the API. The playground is located at [http://localhost:8080/playground](http://localhost:8080/playground).

### Queries

#### route

Retrieve both the safest and fastest route between two geographic locations. The safest route is returned first, then
the fastest route.

Input parameters:

- originLatitude (`Float!`): The latitude of the origin location.
- originLongitude (`Float!`): The longitude of the origin location.
- destinationLatitude (`Float!`): The latitude of the destination location.
- destinationLongitude (`Float!`): The longitude of the destination location.
- preferences (`Preferences!`): The user's preferences for the route.
- pedestrian (`Boolean!`): Whether the user is a pedestrian. If true, the route will be optimized for pedestrians.
  If false, the route will be optimized for vehicles.

Output type: `[Route]!` - **note** that the route is nullable. If the safest route is outside the scope of the currently
loaded OSM file, the safest route will be null.

Example query:

```graphql
query {
    route(
        originLatitude: 33.057207,
        originLongitude: -96.750027,
        destinationLatitude: 32.9856974,
        destinationLongitude: -96.75024739999999,
        preferences: {
            avoidHighways: false,
            avoidTolls: true,
            coThreshold: 9999.0,
            no2Threshold: 9999.0,
            o3Threshold: 9999.0,
            pm2_5Threshold: 9999.0,
            pm10Threshold: 9999.0,
            so2Threshold: 9999.0
        },
        pedestrian: false
    ) {
        lengthInMeters
        travelTimeInSeconds
        waypoints {
            latitude
            longitude
        }
    }
}
```

#### sensorsWithData

Retrieve a list of all sensors, including the data for the latest captured pollutant data for each supported pollutant
type.

Input parameters: N/A

Output type: `[SensorData!]!`

Example query:

```graphql
query {
    allSensorsWithData {
        sensor {
            id
            location {
                latitude
                longitude
            }
            radiusInMeters
        }
        data {
            pollutantId
            value
        }
    }
}
```

#### userNearHazardousArea

Returns if the user is near (500 meters) a hazardous area. A hazardous area is defined as an area where the pollutant 
concentration is above the user's threshold for that pollutant.

Input parameters:

- latitude (`Float!`): The latitude of the user's location.
- longitude (`Float!`): The longitude of the user's location.
- preferences (`Preferences!`): The user's preferences for the route.

Output type: `Boolean!`

Example query:

```graphql
query {
    userNearHazardousArea(
        latitude: 33.1375,
        longitude: -96.7679,
        preferences: {
            coThreshold: 0.0,
            no2Threshold: 0.0,
            o3Threshold: 0.0,
            pm2_5Threshold: 0.0,
            pm10Threshold: 0.0,
            so2Threshold: 0.0,
        }
    )
}
```

### Inputs

#### Preferences

Represents the user's preferences for routing.

Fields:

- avoidHighways (`Boolean`): Whether to avoid highways. - **note** that this is optional.
- avoidTolls (`Boolean`): Whether to avoid tolls. - **note** that this is optional.
- coThreshold (`Float!`): The user's threshold for carbon monoxide in parts per million.
- no2Threshold (`Float!`): The user's threshold for nitrogen dioxide in parts per billion.
- o3Threshold (`Float!`): The user's threshold for ozone in parts per billion.
- pm2_5Threshold (`Float!`): The user's threshold for PM2.5 in micrograms per cubic meter.
- pm10Threshold (`Float!`): The user's threshold for PM10 in micrograms per cubic meter.
- so2Threshold (`Float!`): The user's threshold for sulfur dioxide in parts per billion.

### Types

#### Route

Represents a route between two geographic locations.

Fields:

- lengthInMeters (`Float!`): The total length of the route in meters.
- travelTimeInSeconds (`Duration!`): The total travel time for the route in seconds. The format returned is `PT_M_S`, with
  `_M` being the number of minutes and `_S` being the number of seconds. For example: `PT20M42S` is 20 minutes and 42 seconds.
- waypoints (`[GeoLocation!]!`): A list of waypoints along the route.

#### GeoLocation

Represents a geographic location.

Fields:

- latitude (`Float!`): The latitude of the location.
- longitude (`Float!`): The longitude of the location.

#### SensorData

Represents a sensor and the latest captured pollutant data by the sensor for each supported pollutant.

Fields:

- sensor (`Sensor!`): The sensor being represented.
- data (`[CapturedPollutant!]`): The latest captured pollutant data by the sensor for each supported pollutant.

#### Sensor

Represents a sensor.

Fields:

- id (`Int!`): The ID of the sensor according to the database. - **note** this ID does not correspond to the ID of the
  sensor from its source. That is found in the `sourceId` column in the database.
- location (`GeoLocation!`): The location of the sensor.
- radiusInMeters (`Float!`): The radius the sensor supports in meters.

#### CapturedPollutant

Represents the latest captured pollutant data for a pollutant.

Fields:

- pollutantId (`Int!`): The ID of the pollutant according to the database. - **note** this is the pollutant ID and not
  the captured pollutant ID. The `pollutant` refers to the pollutant type, like CO or NO2.
- value (`Float!`): The value of the pollutant in the respective unit of measurement defined by the pollutant type.

## Problem Troubleshooting

**Q: I changed the PBF OSM file, but the GraphHopper instance is still using my old data. I noticed this from the
bounding box being the same as before.**

A: You need to delete the `graph_folder` directory. This is a cache of the last loaded PBF OSM file.

**Q: I'm getting errors with compiling due to annotations. It says I am missing getters, setters, among other things.**

A: Make sure you enable annotation processing in File | Settings | Build, Execution, Deployment | Compiler | Annotation Processors
