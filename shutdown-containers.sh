#! /usr/bin/bash

docker stop $(docker ps -a -q)
docker rm $(docker ps -a -q)
docker image rm docker-dev-environment_game:latest 
docker image rm docker-dev-environment_confsvr:latest 
docker image rm docker-dev-environment_rfid-processors:latest
docker image rm docker-dev-environment_mobi:latest
docker image rm docker-development-environment_game
docker image rm docker-development-environment_confsvr
docker image rm docker-development-environment_rfid-processors
docker image rm docker-development-environment_mobi


docker image rm docker-development-environment_game:latest 
docker image rm docker-development-environment_confsvr:latest 
