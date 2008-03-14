#!/bin/bash
BASE_PATH="`dirname $0`"
NTORRENT_CONFIG="$HOME/.ntorrent/ntorrent.properties"

cd $BASE_PATH

if [ -f NTORRENT_CONFIG ]
then
  java -Djpf.boot.config=$NTORRENT_CONFIG -jar lib/jpf-boot.jar "$@"
else
 java -jar lib/jpf-boot.jar "$@"
fi
