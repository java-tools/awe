#!/bin/bash

# Get version number
export NEW_VERSION=`cat ./pom.xml | grep -o '<version>[0-9\.]*[A-Z\-]*</version>' | sed -e 's/<[\/]*version>//g' | sed ':a;N;$!ba;s/\n/ /g' | sed 's/[A-Z\-]*//g' | awk '{printf $1}'`
echo "Generating new milestone for version $NEW_VERSION"

# CURL
curl -X POST -H "Content-Type: application/json" --header "Authorization: Bearer ${2}" -s "${1}/milestones" -d "{\"id\": \"${1}\",\"title\": \"${NEW_VERSION}\",\"description\": \"Bug fixes and minor improvements\"}"