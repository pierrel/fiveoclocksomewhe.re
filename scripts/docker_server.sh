#!/bin/bash

NAME=`jq -r ".az_acr_repo" .config.json`
IMAGE=`jq -r ".docker_name_tag" .config.json`

docker run -p 3000:3000 $NAME.azurecr.io/$IMAGE
