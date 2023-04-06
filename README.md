# Airborne Pollutant Pathfinder (APP)

An Android application that enables users to navigate from point A to point B while considering
their allergies and pollen count in different locations.

## Setup

1. Add the `GOOGLE_API_KEY` to your environment variables. IntelliJ has native support for this if 
   you edit the configuration settings. More detailed information on how to add environment
   variables to your IntelliJ run configurations can be found 
   [here](https://www.jetbrains.com/help/objc/add-environment-variables-and-program-arguments.html#add-environment-variables).
2. If using the `DallasOSMFileProvider` (this is the default behaviour), download the latest data from OpenStreetMap's 
   Overpass API for the test bounding box 
   [here](https://download.bbbike.org/osm/bbbike/Dallas/Dallas.osm.pbf). Place the
   downloaded file in the `src/main/resources` directory, naming it simply `dallas` (no file extension).

![](https://i.imgur.com/g3dg3Ls.png)

**Note:** `AZURE_MAPS_CLIENT_ID` and `AZURE_MAPS_SHARED_KEY_ID` are now deprecated and no 
longer needed.