# compile
mvn clean install

# extract server files
tar -xf server/target/tpe-server-1.0-SNAPSHOT-bin.tar.gz -C server/target
chmod +x server/target/tpe-server-1.0-SNAPSHOT/run-node.sh

# extract client files
tar -xf client/target/tpe-client-1.0-SNAPSHOT-bin.tar.gz -C client/target
chmod +x client/target/tpe-client-1.0-SNAPSHOT/run-client.sh
