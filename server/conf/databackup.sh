#!/bin/bash

################################################
#   Game server backup script
#   ver 1.0     2010.05.14
#   Created by Frank
################################################

bakdst=/home/game/resin/webapps/game_server
bakdir=/home/game/server_backup

oldfile=$bakdir"/gameserver-"$(/bin/date '+%Y_%m_%d' --date='30 days ago')".tgz"
newfile=$bakdir"/gameserver-"$(/bin/date '+%Y_%m_%d')".tgz"

#删除过期的文件
x=`rm -f $oldfile`

#备份今天的文件
x=`/bin/tar cvfz $newfile $bakdst`

#echo "/bin/tar cvfz $newfile $bakdst"
