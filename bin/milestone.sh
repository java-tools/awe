#!/bin/bash

# Get version number
new_version=$(cat ./pom.xml | grep -o '<version>[0-9\.]*[A-Z\-]*</version>' | sed -e 's/<[\/]*version>//g' | sed ':a;N;$!ba;s/\n/ /g' | sed 's/[A-Z\-]*//g' | awk '{printf $1}')
echo "Generating new milestone for version ${new_version}"

# Generate milestone
status=$(curl -X POST -H "Content-Type: application/json" --header "Authorization: Bearer ${2}" -s "${1}/milestones" -d "{\"id\": \"${1}\",\"title\": \"${new_version}\",\"description\": \"Bug fixes and minor improvements\"}" | jq --raw-output '.state')
if [[ "${status}" == "null" ]]
then
  echo "Milestone ${new_version} already exists."
else
  echo "Milestone ${new_version} has been generated."
  echo "Current status is ${status}"
fi
echo ""