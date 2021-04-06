#!/bin/bash
/bin/cat $1 | /bin/grep "一口价买" | /bin/cut -d " " -f 6,8,14 | /bin/sed -e 's/[]\[\-]/ /g' | /bin/cut -d " " -f 2,5,9