#!/bin/bash

# Get version number
export NEW_VERSION=`cat ./pom.xml | grep -o '<version>[0-9\.]*[A-Z\-]*</version>' | sed -e 's/<[\/]*version>//g' | sed ':a;N;$!ba;s/\n/ /g' | sed 's/[A-Z\-]*//g' | awk '{printf $1}'`
echo "Generating changelog for version $NEW_VERSION"

# Get merge requests and transforms into changelog
currentDate=`date +%d/%m/%Y`
changelog=`curl --header "Authorization: Bearer ${2}" -s "${1}/merge_requests?milestone=$NEW_VERSION&state=merged&per_page=100" | jq --raw-output 'map("- " + (if (.labels | map(select(. == "has impacts")) | length == 1) then "**[HAS IMPACTS]** " else "" end) + (.title | gsub("Resolve ";"") | gsub("\"";"")) + ". [MR #" + (.iid | tostring) + "](https://gitlab.com/aweframework/awe/merge_requests/" + (.iid | tostring) + ") (" + .merged_by.name + ")") | join("\n")'`
changelogText=`printf "\n# Changelog for ${3} $NEW_VERSION\n*$currentDate*\n\n$changelog\n\n"`
echo "$changelogText"

# Generate changelog
echo "$changelogText" > changelogUpdate && cat changelogUpdate ./CHANGELOG.md > allChangelog && rm changelogUpdate && mv allChangelog ./CHANGELOG.md && echo "Generated changelog file at ./CHANGELOG.md"