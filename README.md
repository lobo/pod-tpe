# ITBA - Distributed Objects Programming course Project: Grupo 3

## Getting started
These instructions will install the development environment into your local machine

### Prerequisites

1. Install maven

	#### MacOS
	```
	$ brew install maven
	```
	
	#### Ubuntu
	```
	$ sudo apt-get install maven
	```
	
	#### Other OSes
	Check [https://maven.apache.org/install.html](https://maven.apache.org/install.html.).

2. Clone the repository, or download source code
	
	```
	$ git clone https://github.com/lobo/pod-tpe
	```
	or
	
	```
	$ wget https://github.com/lobo/pod-tpe/archive/master.zip
	```

## Running Instructions For Eclipse

* Clone the repo
* Run:

```bash
$ mvn clean install eclipse:eclipse
```

### Usage of files

There are two files (CSV format) to use in this project. Both should be located in the following path:
```
client/src/main/resources/files/filename.csv
```

The files with the data are listed below (in a Google Drive public folder):

1. [aeropuertos.csv](https://drive.google.com/file/d/1nEzF5higXIDDnJzWVuZP5TxVFUj4SBBh/view?usp=sharing)
2. [movimientos.csv](https://drive.google.com/file/d/1sGCqq8JjQ3SJ7V0_WXR_jqZHtfs0n62y/view?usp=sharing)

### Run From terminal

First, run `./make.sh` to generate scripts

#### Node

* cd to `server/target/tpe-server-1.0-SNAPSHOT/`
* run `./run-node`

**NOTE**: make sure to activate interfaces (false->true) and specify network in hazelcast.xml to activate multiple nodes in different computers. All computers need to have same hazelcast.xml


#### Client

* cd to `client/target/tpe-client-1.0-SNAPSHOT/`

##### For Query 1:
* run `./run-client -Daddresses=xx.xx.xx.xx;yy.yy.yy.yy -Dquery=1 -DmovementsInPath=movimientos.csv -DairportsInPath=aeropuertos.csv -DoutPath=output.csv`

##### For Query 2:
* run `./run-client -Daddresses=xx.xx.xx.xx;yy.yy.yy.yy -Dquery=2 -DmovementsInPath=movimientos.csv -DairportsInPath=aeropuertos.csv -DoutPath=output.csv`

##### For Query 3:
* run `./run-client -Daddresses=xx.xx.xx.xx;yy.yy.yy.yy -Dquery=3 -DmovementsInPath=movimientos.csv -DairportsInPath=aeropuertos.csv -DoutPath=output.csv`

##### For Query 4: 
* run `./run-client -Daddresses=xx.xx.xx.xx;yy.yy.yy.yy -Dquery=2 -DmovementsInPath=movimientos.csv -DairportsInPath=aeropuertos.csv -DoutPath=output.csv -Doaci=SAEZ -Dn=5`
* Notice here the query parameters are: `oaci=SAEZ` and `n=5`.

##### For Query 5: 
* run `./run-client -Daddresses=xx.xx.xx.xx;yy.yy.yy.yy -Dquery=2 -DmovementsInPath=movimientos.csv -DairportsInPath=aeropuertos.csv -DoutPath=output.csv -Dn=5`
* Notice here the query parameter is: `n=5`.

##### For Query 6: 
* run `./run-client -Daddresses=xx.xx.xx.xx;yy.yy.yy.yy -Dquery=2 -DmovementsInPath=movimientos.csv -DairportsInPath=aeropuertos.csv -DoutPath=output.csv -Dmin=3`
* Notice here the query parameter is: `min=3`.

### Run From Eclipse

* Open Eclipse
  * Import as existing project

* Open run-configuration for client and set VM arguments

* Move hazelcast from `server/target/tpe-server-1.0-SNAPSHOT/` to `server/`

* Create run-configuration named `node` with main class: `com.hazelcast.console.ConsoleApp`

* Run node main

* Run client

**NOTE**: if after running `mvn clean install` or `./make.sh`, if want to work again from Eclipse, go to `Project->Clean`.

## Authors

* [Giuliano Scaglioni](https://github.com/giulianos)
* [Matias Ota](https://github.com/m074)
* [Daniel Lobo](https://github.com/lobo)
