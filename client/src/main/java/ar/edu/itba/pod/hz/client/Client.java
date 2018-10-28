package ar.edu.itba.pod.hz.client;

import ar.edu.itba.pod.hz.client.reader.AirportsReader;
import ar.edu.itba.pod.hz.client.reader.MovementsReader;
import ar.edu.itba.pod.hz.model.AirportData;
import ar.edu.itba.pod.hz.model.MovementData;
import ar.edu.itba.pod.hz.mr.query1.MovementCounterMapper;
import ar.edu.itba.pod.hz.mr.query1.MovementCounterReducerFactory;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ICompletableFuture;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class Client {

    private final String JOB_TRACKER = "default";

    private HazelcastInstance client;

    private String airportsInPath;
    private String movementsInPath;

    private PrintWriter outPath;
    private PrintWriter timeOutPath;


    public Client(ClientConfig ccfg, String airportsInPath, String movementsInPath, String outPath, String timeOutPath)
            throws FileNotFoundException, UnsupportedEncodingException {
        super();
        this.client = HazelcastClient.newHazelcastClient(ccfg);
        this.airportsInPath = airportsInPath;
        this.movementsInPath = movementsInPath;
        //this.outPath = new PrintWriter(outPath, "UTF-8");
        //this.timeOutPath = new PrintWriter(timeOutPath,"UTF-8");

    }

    private static final String AIRPORT_MAP_NAME = "G3-2018-AIR";
    private static final String MOVEMENT_MAP_NAME = "G3-2018-MOV";

    private static Logger logger = LoggerFactory.getLogger(Client.class); // TODO CHECK

    public static void main(String[] args) {
        try{
            OtaParameter p = OtaParameter.loadParameters();
            ClientConfig ccfg = new ClientConfig();
            ccfg.getGroupConfig().setName(p.getName()).setPassword(p.getPassword());

            ClientNetworkConfig net = new ClientNetworkConfig();
            net.addAddress(p.getAddresses());
            ccfg.setNetworkConfig(net);

            Client queryClient = new Client(ccfg, p.getAirportsInPath(),p.getMovementsInPath(),p.getOutPath(),p.getTimeOutPath());

            IMap<String, AirportData> airportsMap = queryClient.client.getMap(AIRPORT_MAP_NAME);
            IMap<Integer, MovementData> movementMap = queryClient.client.getMap(MOVEMENT_MAP_NAME);
            movementMap.clear();

            logger.info("Inicio del aeropuerto");
            try {
                AirportsReader.partialReadWithCsvBeanReader(airportsMap, queryClient.airportsInPath);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            try {
                MovementsReader.partialReadWithCsvBeanReader(movementMap, queryClient.movementsInPath);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            logger.info("Inicio de movimientos");
            switch (p.getQuery_number()) {
                case 1:
                    queryClient.query1(movementMap, airportsMap);
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

    public void query1(IMap<Integer, MovementData> movementsMap, IMap<String, AirportData> airportsMap)
            throws InterruptedException, ExecutionException, FileNotFoundException, UnsupportedEncodingException {

        JobTracker tracker = client.getJobTracker(JOB_TRACKER);

        KeyValueSource<Integer, MovementData> source = KeyValueSource.fromMap(movementsMap);
        Job<Integer, MovementData> job = tracker.newJob(source);

        ICompletableFuture<Map<String, Integer>> futureResult = job.mapper(new MovementCounterMapper())
                .reducer(new MovementCounterReducerFactory()).submit();

        Map<String, Integer> result = futureResult.get();

        for(Map.Entry<String, Integer> airport : result.entrySet()) {
            AirportData airportData = airportsMap.get(airport.getKey());
            if(airportData != null) {
                System.out.println(airportData.getDenomination() +  ": " + airport.getValue());
            }
        }
    }

}
