# Airborne Pollutant Pathfinder (APP)

This backend is responsible for providing the API for the Airborne Pollutant Pathfinder (APP) project. It uses GraphQL,
Spring Boot, Google Directions API, and GraphHopper. The frontend can be found [here](https://github.com/Airborne-Pollutant-Pathfinder/APP-interface).

The project aims to create an Android application called Airborne Pollutant Pathfinder (APP), which will enable users to
navigate from point A to point B while considering their allergies and pollen count in different locations. The backend
supports computing the fastest and safest routes and finding relevant sensors for a particular geolocation.

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

![](https://i.imgur.com/g3dg3Ls.png)

**Note:** `AZURE_MAPS_CLIENT_ID` and `AZURE_MAPS_SHARED_KEY_ID` are now deprecated and no 
longer needed.

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

Output type: `[Route]` - **note** that the route is nullable. If the safest route is outside the scope of the currently
loaded OSM file, the safest route will be null.

Example query:

```graphql
query {
  route(originLatitude: 33.133092, originLongitude: -96.772497, destinationLatitude: 32.985661, destinationLongitude: -96.750462) {
    lengthInMeters
    travelTimeInSeconds
    waypoints {
      latitude
      longitude
    }
  }
}
```

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

## Problem Troubleshooting

**Q: I changed the PBF OSM file, but the GraphHopper instance is still using my old data. I noticed this from the
bounding box being the same as before.**

A: You need to delete the `graph_folder` directory. This is a cache of the last loaded PBF OSM file.  