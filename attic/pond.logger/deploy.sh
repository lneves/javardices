#!/bin/bash

set -e
cd $(dirname $0)

SERVERS="pond-upload02.pond.bk.sapo.pt"
TEMP_DIR="/tmp/pond-logger"
FINAL_DIR="/servers/pond-logger"


rm -rfv dist
mkdir dist
ant clean
ant package

cp -Rv lib dist/
cp -Rv conf dist/
mv -v pond-logger.jar dist/lib/
cp -v run dist/

find dist -name ".svn" | xargs rm -rfv



for srv in $SERVERS
do
echo "==== $srv ===="
ssh -o StrictHostKeyChecking=no -o ConnectTimeout=6 -i /home/lneves/conf/SAPO_SSH_Keys/sapobofh/id_dsa sapobofh@$srv "sudo rm -rfv $TEMP_DIR && mkdir -p $TEMP_DIR"
scp -o StrictHostKeyChecking=no -o ConnectTimeout=6 -i /home/lneves/conf/SAPO_SSH_Keys/sapobofh/id_dsa -r dist/* sapobofh@$srv:$TEMP_DIR/
ssh -o StrictHostKeyChecking=no -o ConnectTimeout=6 -i /home/lneves/conf/SAPO_SSH_Keys/sapobofh/id_dsa sapobofh@$srv "sudo svc -kd /service/pond-logger && sudo chown -R nobody.nogroup $TEMP_DIR/* && sudo rm -rfv $FINAL_DIR/bin && sudo rm -rfv $FINAL_DIR/lib && sudo rm -rfv $FINAL_DIR/conf && sudo rm -rfv $FINAL_DIR/conf && sudo mv -v $TEMP_DIR/* $FINAL_DIR  && sudo svc -u /service/pond-logger"
echo "=============="
echo
done

rm -rf dist

date

exit 0
