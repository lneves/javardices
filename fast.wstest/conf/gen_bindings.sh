#!/usr/bin/env bash
set -e

cd $(dirname $0)

JIBX_LIB_PATH=$HOME/Dowloads/jibx/lib

echo "JIBX library path is set to: $JIBX_LIB_PATH"

GEN=gen_src
rm -rf $GEN

java -cp "$JIBX_LIB_PATH/*" org.jibx.schema.codegen.CodeGen -p wstest.srv.actors -t $GEN -w wstest.xsd

rm -rf src/wstest/srv/actors
cp -Rv $GEN/wstest/* ../src/wstest/
mv -vf $GEN/binding.xml ./srv_binding.xml

rm -rf $GEN
