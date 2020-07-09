#!/bin/bash

# Get version number
new_version=$(cat ./pom.xml | grep -o '<version>[0-9\.]*[A-Z\-]*</version>' | sed -e 's/<[\/]*version>//g' | sed ':a;N;$!ba;s/\n/ /g' | sed 's/[A-Z\-]*//g' | awk '{printf $1}')
echo "Sending release mail for version ${new_version}"

curl --request POST \
     --url https://api.sendgrid.com/v3/mail/send \
     --header "Authorization: Bearer ${0}" \
     --header 'Content-Type: application/json' \
     --data "{\"from\": {\"email\": \"${1}\", \"name\": \"AWE Team\"}, \"template_id\": \"${3}\", \"personalizations\": [{\"to\": [{\"email\": \"${2}\"}}], \"dynamic_template_data\": {\"version\": \"${new_version}\"}}]}"
