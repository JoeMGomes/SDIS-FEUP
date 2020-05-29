# SDIS T2 G22

### Compiling
<!-- Inside "/src" directory: -->
Run the following bash script on the root directory:

```cmd
bash compile.sh
```


### Running

#### Peer

Run the following bash script on the root directory:  

```cmd
bash peer.sh <peer_port> [<known_peer_IP_address> <known_peer_port>]
```
**Note:** The first Peer will not have the \<known_peer_IP_address\> and \<known_peer_port\> arguments

#### Client
Run the following bash scripts also on the root directory, according to the desired operation:

**Backup**

```cmd
bash client.sh <known_peer_IP_address> <known_peer_port> BACKUP <file_path> <replication_degree>
```

**Restore**

```cmd
bash client.sh <known_peer_IP_address> <known_peer_port> RESTORE <file_path> 
```

**Delete**

```cmd
bash client.sh <known_peer_IP_address> <known_peer_port> DELETE <file_path> 
```

**Reclaim**

```cmd
bash client.sh <known_peer_IP_address> <known_peer_port> RECLAIM <max_disk_space>
```

**State**

```cmd
bash client.sh <known_peer_IP_address> <known_peer_port> STATE
```
**Note:** The capitalziation on the Operation name is not necessary