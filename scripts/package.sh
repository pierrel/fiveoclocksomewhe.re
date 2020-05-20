#!/bin/bash
# TODO: convert to clojure file (https://github.com/tonsky/uberdeps#programmatic-ap)

NAME=`jq -r ".az_acr_repo" .config.json`
IMAGE=`jq -r ".docker_name_tag" .config.json`


# clean it up
echo "cleaning"
rm -rf dev_resources
rm -rf classes
rm -rf target/*.jar
rm -rf out # javascript

# compile cljs
echo "compiling cljs"
clj -A:dev -m compile-cljs

# create the jar
echo "compiling clj"
mkdir classes
clj -A:dev -e "(compile 'server)"
clj -A:uberjar --main-class server

echo "building docker image"
docker build -t $NAME.azurecr.io/$IMAGE ./


