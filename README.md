# Airborne Pollutant Pathfinder (APP)

An Android application that enables users to navigate from point A to point B while considering
their allergies and pollen count in different locations.

## Setup

1. Add the `GOOGLE_API_KEY` to your environment variables. IntelliJ has native support for this if 
   you edit the configuration settings. More detailed information on how to add environment
   variables to your IntelliJ run configurations can be found 
   [here](https://www.jetbrains.com/help/objc/add-environment-variables-and-program-arguments.html#add-environment-variables).
2. If using the `DallasOSMFileProvider` (this is the default behaviour), download the latest data from BBBike
   for Dallas 
   [here](https://download.bbbike.org/osm/extract/planet_-97.563,32.578_-96.192,33.252.osm.pbf). Place the
   downloaded file in the `src/main/resources/maps` directory, naming it simply `dallas` (no file extension).

![](https://i.imgur.com/g3dg3Ls.png)

**Note:** `AZURE_MAPS_CLIENT_ID` and `AZURE_MAPS_SHARED_KEY_ID` are now deprecated and no 
longer needed.

## Problem Troubleshooting

**Q: I changed the PBF OSM file, but the GraphHopper instance is still using my old data. I noticed this from the
bounding box being the same as before.**

A: You need to delete the `graph_folder` directory. This is a cache of the last loaded PBF OSM file.  