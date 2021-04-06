#!/bin/sh
echo "第1个参数" $1 "第二个参数" $2
#. /etc/profile
#/bin/cat $1 | /bin/grep $2 
/bin/cat $1|/bin/grep  $2
# ls -hlt