#!/bin/bash

# Get version number
version=`cat ./pom.xml | grep -o '<version>[0-9\.]*[A-Z\-]*</version>' | sed -e 's/<[\/]*version>//g' | sed ':a;N;$!ba;s/\n/ /g' | sed 's/[A-Z\-]*//g' | awk '{printf $2}'`
echo "Generating changelog for version $version"

# Get merge requests and transforms into changelog
currentDate=`date +%d/%m/%Y`
changelog=`curl -s "https://gitlab.com/api/v4/projects/9781451/merge_requests" | jq --raw-output 'map(select(.milestone.title == "4.0.5" and .state == "merged")) | map("- " + (.title | gsub("Resolve ";"") | gsub("\"";"")) + ". !" + (.iid | tostring) + " (" + .merged_by.name + ")") | join("\n")'`
changelogText=`echo "# Changelog for AWE $version ($currentDate)\n\n$changelog\n\n"`
echo "$changelogText"

# Generate changelog
cat <(echo "$changelogText") ./CHANGELOG.md > temp && mv temp ./CHANGELOG.md && echo "Generated changelog file at ./CHANGELOG.md"