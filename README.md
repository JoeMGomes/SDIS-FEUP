# SDIS T2 G22

### Compiling
Run the following bash script on the root directory:

```cmd
bash scripts/compile.sh
```


### Running

#### Peer

Run the following bash script on the root directory:  

```cmd
bash scripts/peer.sh <peer_port> [<known_peer_IP_address> <known_peer_port>]
```
**Note:** The first Peer will not have the \<known_peer_IP_address\> and \<known_peer_port\> arguments

#### Client
Run the following bash scripts also on the root directory, according to the desired operation:

**Backup**

```cmd
bash scripts/client.sh <known_peer_IP_address> <known_peer_port> BACKUP <file_path> <replication_degree>
```

**Restore**

```cmd
bash scripts/client.sh <known_peer_IP_address> <known_peer_port> RESTORE <file_path> 
```

**Delete**

```cmd
bash scripts/client.sh <known_peer_IP_address> <known_peer_port> DELETE <file_path> 
```

**Reclaim**

```cmd
bash scripts/client.sh <known_peer_IP_address> <known_peer_port> RECLAIM <max_disk_space>
```

**State**

```cmd
bash scripts/client.sh <known_peer_IP_address> <known_peer_port> STATE
```
**Note:** The capitalziation on the Operation name is not necessary

## Test Scripts
In order to test the program easier the following scripts are provided

**runMany.sh**

Runs 6 instances oh the peer.sh
```cmd
bash scripts/runMany.sh
```    

**shutdown.sh**

Graciously shuts down ALL the running peers and deletes their stored files 

**Warning:** this script deletes all the running Java processes in the machine
```cmd
bash scripts/runMany.sh
```