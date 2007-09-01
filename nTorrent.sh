#!/bin/bash
BASE_PATH="`dirname $0`"
cd $BASE_PATH
command java -jar nTorrent.jar -eq 127 || \
echo "You need java! get it at http://java.sun.com."
