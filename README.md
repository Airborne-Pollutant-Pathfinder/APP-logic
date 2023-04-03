# Airborne Pollutant Pathfinder (APP)

An Android application that enables users to navigate from point A to point B while considering
their allergies and pollen count in different locations.

## Setup

1. Add the `GOOGLE_API_KEY` to your environment variables. IntelliJ has native support for this if 
   you edit the configuration settings. More detailed information on how to add environment
   variables to your IntelliJ run configurations can be found 
   [here](https://www.jetbrains.com/help/objc/add-environment-variables-and-program-arguments.html#add-environment-variables).
2. If using the `StaticOSMFileProvider` (this is the default behaviour), download the latest data from OpenStreetMap's 
   Overpass API for the test bounding box 
   [here](http://overpass-api.de/api/map?bbox=-96.84933500000002,32.948792499999996,-96.730685,33.1699375). Place the
   downloaded file in the `src/main/resources` directory, naming it simply `map` (no file extension).

![](https://i.imgur.com/g3dg3Ls.png)

**Note:** `AZURE_MAPS_CLIENT_ID` and `AZURE_MAPS_SHARED_KEY_ID` are now deprecated and no 
longer needed.