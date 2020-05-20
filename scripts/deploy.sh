#!/bin/bash

RG=`jq -r ".az_resource_group" .config.json`
NAME=`jq -r ".az_acr_repo" .config.json`
IMAGE=`jq -r ".docker_name_tag" .config.json`

echo "Logging in..."
az acr login --resource-group $RG --name $NAME
echo "Deploying..."
docker image push $NAME.azurecr.io/$IMAGE
echo "Done"
