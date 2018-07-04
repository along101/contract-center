#!/bin/sh

APP_NAME=contractcenter.jar

nohup  java -Xmx2048m -jar $APP_NAME >logs/start.log 2>logs/startError.log &

sleep 15

echo "pwd: 当前文件夹名字:"
pwd

echo "1"
ls  ../


echo "1"
ls -ltrh ../20180629113552/logs

tail -n 500 ../20180629113552/logs/start.log
tail -n 500 ../20180629113552/logs/startError.log

if test $(pgrep -f $APP_NAME|wc -l) -eq 0
then
   echo "start failed"
   cat logs/start.log
   cat logs/startError.log

else
   echo "start successed"
fi