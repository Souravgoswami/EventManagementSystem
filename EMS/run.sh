#!/bin/sh
cd `dirname $0`/src && /bin/java -cp commons-codec-1.13.jar:sqlite-jdbc-3.27.2.1.jar:. Login >/dev/null 2>&1 &
