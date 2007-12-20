#!/bin/bash
BASE_PATH="`dirname $0`"
cd $BASE_PATH
java -jar nTorrent.jar "$@"
