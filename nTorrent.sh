#!/bin/bash
BASE_PATH="`dirname $0`"
cd $BASE_PATH
java -Djpf.boot.config=$HOME/.ntorrent/ntorrent.properties -jar nTorrent.jar "$@"
