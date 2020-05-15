#!/bin/bash

bash peer.sh 3000 &
bash peer.sh 4000 127.0.0.1 3000 &
sleep 5
bash peer.sh 5000 127.0.0.1 3000 &