#!/bin/bash

rm -r bin
mkdir -p bin

echo "Compiling..."
javac $(find src | grep .java) -Xlint:unchecked -d bin

if [ $? -eq 0 ]; then
    echo "Compiled successfully!"
fi