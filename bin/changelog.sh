#!/bin/bash

# Get version number
export NEW_VERSION=`cat ./pom.xml | grep -o '<version>[0-9\.]*[A-Z\-]*</version>' | sed -e 's/<[\/]*version>//g' | sed ':a;N;$!ba;s/\n/ /g' | sed 's/[A-Z\-]*//g' | awk '{printf $1}'`
echo "Generating changelog for version $NEW_VERSION"

# Get merge requests and transforms into changelog
currentDate=`date +%d/%m/%Y`
changelog=`curl -s "https://gitlab.com/api/v4/projects/9781451/merge_requests" | jq --raw-output 'map(select(.milestone.title == "'$NEW_VERSION'" and .state == "merged")) | map("- " + (if (.labels | map(select(. == "has impacts")) | length == 1) then "**[HAS IMPACTS]** " else "" end) + (.title | gsub("Resolve ";"") | gsub("\"";"")) + ". [MR #" + (.iid | tostring) + "](https://gitlab.com/aweframework/awe/merge_requests/" + (.iid | tostring) + ") (" + .merged_by.name + ")") | join("\n")'`
changelogText=`printf "\n# Changelog for AWE $NEW_VERSION\n*$currentDate*\n\n$changelog\n\n"`
echo "$changelogText"

# Generate changelog
echo "$changelogText" > changelogUpdate && cat changelogUpdate ./CHANGELOG.md > allChangelog && rm changelogUpdate && mv allChangelog ./CHANGELOG.md && echo "Generated changelog file at ./CHANGELOG.md"