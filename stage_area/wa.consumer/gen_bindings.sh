#!/usr/bin/env bash
set -e

cd $(dirname $0)

JIBX_LIB_PATH=$HOME/Downloads/jibx/lib

echo "JIBX library path is set to: $JIBX_LIB_PATH"

GEN=gen_src
rm -rf $GEN

java -version
java -cp "$JIBX_LIB_PATH/*" org.jibx.schema.codegen.CodeGen -p pt.sapo.wa.persister -t $GEN -w event.xsd

#rm -rf src/wstest/srv/actors
#cp -Rv $GEN/wstest/* ../src/wstest/
#mv -vf $GEN/binding.xml ./srv_binding.xml

#rm -rf $GEN
