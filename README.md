# Airborne Pollutant Pathfinder (APP)

An Android application that enables users to navigate from point A to point B while considering
their allergies and pollen count in different locations.

## Setup

1. Add the `GOOGLE_API_KEY` to your environment variables. IntelliJ has native support for this if 
   you edit the configuration settings. More detailed information on how to add environment
   variables to your IntelliJ run configurations can be found 
   [here](https://www.jetbrains.com/help/objc/add-environment-variables-and-program-arguments.html#add-environment-variables).
2. If using the `DallasOSMFileProvider` (this is the default behaviour), download data extracted from BBBike (as of 
   April 10, 2023) for Dallas 
   [here](https://cometmail-my.sharepoint.com/:u:/g/personal/jjp160630_utdallas_edu/EdrAdf1TrIdFumvKV8GIZ6cBgPfMQwgkTuHwvgzG4dns4A?e=6nTln6). Place the
   downloaded file in the `src/main/resources/maps` directory, naming it simply `dallas` (no file extension).
   - Note that if the link above does not work, you will have to extract a new OSM PBF file. You can go to 
     BBBike's website to do that 
     [here](https://extract.bbbike.org/?sw_lng=-97.395&sw_lat=32.635&ne_lng=-96.024&ne_lat=33.309&format=osm.pbf&city=Dallas&lang=en),
     with the coordinates already defined. All you will have to do is provide an email for it to email you a link to
     download the OSM PBF file.

![](https://i.imgur.com/g3dg3Ls.png)

**Note:** `AZURE_MAPS_CLIENT_ID` and `AZURE_MAPS_SHARED_KEY_ID` are now deprecated and no 
longer needed.

## Problem Troubleshooting

**Q: I changed the PBF OSM file, but the GraphHopper instance is still using my old data. I noticed this from the
bounding box being the same as before.**

A: You need to delete the `graph_folder` directory. This is a cache of the last loaded PBF OSM file.  