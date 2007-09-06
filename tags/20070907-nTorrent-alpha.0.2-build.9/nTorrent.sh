#!/bin/bash
BASE_PATH="`dirname $0`"

i=""
while (($#)); do
	i="$i$1 "
shift
done

cd $BASE_PATH
command java -jar nTorrent.jar $i || \
echo "You need java! get it at http://java.sun.com."
