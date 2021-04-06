#!/bin/bash

resin_home="/home/game/resin"

if [[ $1 = "" ]]
then
        echo "Usage: ./configure --resin_home=..."
        exit
fi

if [[ $1 = --resin_home* ]]
then
        resin_home=`echo $1 | awk -F = '{print $2}'`
fi

log4jFile="web/WEB-INF/spring_config/log4j.properties"
serverPropsFile="web/WEB-INF/spring_config/server.properties"
gamesFile="web/WEB-INF/game_init_config/games.xml"

resin_home=${resin_home//\//\\/}

x=`sed -i "s/@resin_home/$resin_home/g" $log4jFile`
x=`sed -i "s/@resin_home/$resin_home/g" $serverPropsFile`
x=`sed -i "s/\/home\/game\/resin-3.0.19/$resin_home/g" $gamesFile`