#!/bin/bash

bash peer.sh 3000 &
sleep 3
bash peer.sh 4000 127.0.0.1 3000 &
sleep 3
bash peer.sh 5000 127.0.0.1 3000 &
sleep 3
bash peer.sh 6000 127.0.0.1 4000 &
sleep 3
bash peer.sh 7001 127.0.0.1 5000 &
sleep 3
bash peer.sh 8000 127.0.0.1 5000 &
sleep 3
bash peer.sh 9000 127.0.0.1 3000 &