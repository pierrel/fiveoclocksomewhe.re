# Template for websites

With a focus on clojure, clojurescript, and azure

## Prerequisites

You will need
* [clojure 1.10+](https://clojure.org)
* [jq](https://stedolan.github.io/jq/)
* [azure cli](https://docs.microsoft.com/en-us/cli/azure/) 
* [docker](https://www.docker.com) 

### Config

Before your can run the package and deploy commands you'll need to create a file called `.config.json` in the root of this project. It should look like this:
```
{
    "docker_name_tag": "thing:v1",
    "az_acr_repo": "repo",
    "az_resource_group": "rg-name"
}
```

## Running
You can run the app using the server script
`$ clj -A:dev -m server`

## Dev
It's recommended to use cider. There're already some defaults provided to start the server with dev. From cider just do `(in-ns 'dev)` and evaluate the /scripts/dev.clj file. You'll have a `server` variable which you can use like above.

### Javascript
Once the server has been started like above you can connect to the browser by starting the `cider-jack-in-cljs`.

## Production

Compile the docker image using `$ ./scripts/package.sh`. It can be run locally with `$ ./scripts/docker-server.sh` and deployed to the remote registery with `$ ./scripts/deploy.sh`.

## License

Copyright © 2020 FIXME
