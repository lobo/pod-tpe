package ar.edu.itba.pod.hz.client;

import ar.edu.itba.pod.hz.client.reader.AirportsDataReader;
import ar.edu.itba.pod.hz.client.reader.DataSetReader;
import ar.edu.itba.pod.hz.client.reader.MovementsDataReader;
import ar.edu.itba.pod.hz.model.AirportData;
import ar.edu.itba.pod.hz.model.Data;
import ar.edu.itba.pod.hz.model.MovementData;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class Client2 {

    private HazelcastInstance client;

    private String airportsInPath;
    private String movementsInPath;

    private PrintWriter outPath;
    private PrintWriter timeOutPath;


    public Client2(ClientConfig ccfg, String airportsInPath,String movementsInPath,String outPath,String timeOutPath)
            throws FileNotFoundException, UnsupportedEncodingException {
        super();
        this.client = HazelcastClient.newHazelcastClient(ccfg);
        this.airportsInPath = airportsInPath;
        this.movementsInPath = movementsInPath;
        this.outPath = new PrintWriter(outPath, "UTF-8");
        this.timeOutPath = new PrintWriter(timeOutPath,"UTF-8");

    }

    private static final String AIRPORT_MAP_NAME = "G3-2018-AIR";
    private static final String MOVEMENT_MAP_NAME = "G3-2018-MOV";

    private static Logger logger = LoggerFactory.getLogger(Client2.class); // TODO CHECK

    public static void main(String[] args) {
        try{
            OtaParameter p = OtaParameter.loadParameters();
            ClientConfig ccfg = new ClientConfig();
            ccfg.getGroupConfig().setName(p.getName()).setPassword(p.getPassword());

            ClientNetworkConfig net = new ClientNetworkConfig();
            net.addAddress(p.getAddresses());
            ccfg.setNetworkConfig(net);

            Client2 queryClient = new Client2(ccfg, p.getAirportsInPath(),p.getMovementsInPath(),p.getOutPath(),p.getTimeOutPath());

            IMap<Integer, AirportData> airportMap = queryClient.client.getMap(AIRPORT_MAP_NAME);
            IMap<Integer, MovementData> movementMap = queryClient.client.getMap(MOVEMENT_MAP_NAME);

            logger.info("Inicio del aeropuerto");
            try {
                AirportsDataReader.readDataSet(airportMap, queryClient.airportsInPath);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            logger.info("Inicio de movimientos");
            try {
                MovementsDataReader.readDataSet(movementMap, queryClient.movementsInPath);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            switch (p.getQuery_number()) {
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 6:
                    break;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    }
