package ar.edu.itba.pod.hz.client;

import ar.edu.itba.pod.hz.client.reader.AirportsReader;
import ar.edu.itba.pod.hz.client.reader.MovementsReader;
import ar.edu.itba.pod.hz.model.AirportData;
import ar.edu.itba.pod.hz.model.AirportTuple;
import ar.edu.itba.pod.hz.model.BiIntegerTuple;
import ar.edu.itba.pod.hz.model.MovementData;
import ar.edu.itba.pod.hz.mr.query1.MovementCounterMapper;
import ar.edu.itba.pod.hz.mr.query1.MovementCounterReducerFactory;
//import ar.edu.itba.pod.hz.mr.query2.MovementCounterDividerReducerFactory;
import ar.edu.itba.pod.hz.mr.query2.*;
import ar.edu.itba.pod.hz.mr.query3.AirportTupleIntegerTupleReducerFactory;
import ar.edu.itba.pod.hz.mr.query3.OriginDestinationMapper;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ICompletableFuture;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobCompletableFuture;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class Client {


    class Frmt extends Formatter {
        private final DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss:SSSS");

        public String format(LogRecord record) {
            StringBuilder builder = new StringBuilder(1000);
            builder.append(df.format(new Date(record.getMillis()))).append(" - ");
            builder.append("[").append(record.getSourceClassName()).append(".");
            builder.append(record.getSourceMethodName()).append("] - ");
            builder.append("[").append(record.getLevel()).append("] - ");
            builder.append(formatMessage(record));
            builder.append("\n");
            return builder.toString();
        }

        public String getHead(Handler h) {
            return super.getHead(h);
        }

        public String getTail(Handler h) {
            return super.getTail(h);
        }
    }

    private final String JOB_TRACKER = "default";

    private HazelcastInstance client;

    private String airportsInPath;
    private String movementsInPath;

    private PrintWriter outPath;
    private FileHandler FhtimeOutPath;

    private  static Logger logger =  Logger.getLogger("Client");;

    public Client(ClientConfig ccfg, String airportsInPath, String movementsInPath, String outPath, String timeOutPath)
            throws IOException {
        this.client = HazelcastClient.newHazelcastClient(ccfg);
        this.airportsInPath = airportsInPath;
        this.movementsInPath = movementsInPath;
        if(outPath.equals(""))
            this.outPath = new PrintWriter(System.out);
        else
            this.outPath = new PrintWriter(outPath, "UTF-8");
        if(!timeOutPath.equals("")){ // logging file
            this.FhtimeOutPath = new FileHandler(timeOutPath);
            this.FhtimeOutPath.setFormatter(new Frmt());
            this.logger.addHandler(this.FhtimeOutPath);
//        }else{ //TODO FIX FORMAT
//            Handler h = new ConsoleHandler();
//            h.setFormatter(new Frmt());
//            this.logger.addHandler(h);
        }




    }

    private static final String AIRPORT_MAP_NAME = "G3-2018-AIR";
    private static final String MOVEMENT_MAP_NAME = "G3-2018-MOV";



    public static void main(String[] args) {
        try{
            Parameter p = Parameter.loadParameters();
            ClientConfig ccfg = new ClientConfig();
            ccfg.getGroupConfig().setName(p.getName()).setPassword(p.getPassword());

            ClientNetworkConfig net = new ClientNetworkConfig();
            net.addAddress(p.getAddresses());
            ccfg.setNetworkConfig(net);

            Client queryClient = new Client(ccfg, p.getAirportsInPath(),p.getMovementsInPath(),p.getOutPath(),p.getTimeOutPath());

            IMap<String, AirportData> airportsMap = queryClient.client.getMap(AIRPORT_MAP_NAME);
            IMap<Integer, MovementData> movementMap = queryClient.client.getMap(MOVEMENT_MAP_NAME);
            movementMap.clear();

            logger.info("Cargando los aeropuertos");
            try {
                AirportsReader.partialReadWithCsvBeanReader(airportsMap, queryClient.airportsInPath);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            logger.info("Cargando los movimientos");
            try {
                MovementsReader.partialReadWithCsvBeanReader(movementMap, queryClient.movementsInPath);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            logger.info("Procesando la query");
            switch (p.getQuery_number()) {
                case 1:
                    queryClient.query1(movementMap, airportsMap);
                    break;
                case 2:
                    queryClient.query2(movementMap);
                    break;
                case 3:
                    queryClient.query3(movementMap);
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 6:
                    break;
            }
            logger.info("Terminado de procesar la query");
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

        System.out.println("OACI;Denominación;Movimientos");
        for(Map.Entry<String, Integer> airport : result.entrySet()) {
            AirportData airportData = airportsMap.get(airport.getKey());
            if(airportData != null) {
                System.out.println(airport.getKey() + ";" + airportData.getDenomination() +  ";" + airport.getValue());
            }
        }
    }



    public void query2(IMap<Integer, MovementData> movementsMap)
            throws InterruptedException, ExecutionException, FileNotFoundException, UnsupportedEncodingException {

        final String INTERMEDIATE_MAP_NAME = "LEGAJOS-INTERMEDIATE";

        // Find job tracker
        JobTracker tracker = client.getJobTracker(JOB_TRACKER);

        // Create source for first map-reduce job
        KeyValueSource<Integer, MovementData> source = KeyValueSource.fromMap(movementsMap);

        // Create job
        Job<Integer, MovementData> job = tracker.newJob(source);

        // Submit first map-reduce job
        ICompletableFuture<Map<String, Integer>> intermediateFutureResult = job.mapper(new MovementCounterMapper())
                .reducer(new MovementCounterDividerReducerFactory()).submit();

        // Create local intermediate map
        Map<String, Integer> intermediateResult = intermediateFutureResult.get();

        // Create intermediate map for second map-reduce job input
        IMap<String, Integer> intermediateMap = this.client.getMap(INTERMEDIATE_MAP_NAME);

        // Clear it in case it already exists
        intermediateMap.clear();

        // Fill with entries from first map-reduce job out
        for(Map.Entry<String, Integer> entry : intermediateResult.entrySet()) {
            intermediateMap.set(entry.getKey(), entry.getValue());
        }

        // Create source for second map-reduce job
        KeyValueSource<String, Integer> source2 = KeyValueSource.fromMap(intermediateMap);

        // Create second job
        Job<String, Integer> job2 = tracker.newJob(source2);

        // Submit second map-reduce job
        ICompletableFuture<Map<Integer, List<AirportTuple>>> futureResult = job2.mapper(new SwapKeyValueMapper())
                .reducer(new SameKeyGrouperReducerFactory()).submit();

        // Get map from result
        Map<Integer, List<AirportTuple>> result = futureResult.get();

        // Iterate through entries to print them
        System.out.println("Grupo;Aeropuerto A;Aeropuerto B");
        for(Map.Entry<Integer, List<AirportTuple>> entry : result.entrySet()) {
            Integer millennium = entry.getKey();
            for(AirportTuple tuple : entry.getValue()) {
                System.out.println((millennium*1000) + ";" + tuple.getAirport1() + ";" + tuple.getAirport2());
            }
        }
    }

    public void query3(IMap<Integer, MovementData> movementsMap) throws ExecutionException, InterruptedException {


        JobTracker tracker = client.getJobTracker(JOB_TRACKER);

        KeyValueSource<Integer, MovementData> source = KeyValueSource.fromMap(movementsMap);

        Job<Integer, MovementData> job = tracker.newJob(source);

        // Submit map-reduce job
        JobCompletableFuture<Map<AirportTuple, BiIntegerTuple>> futureResult = job.mapper(new OriginDestinationMapper())
                .reducer(new AirportTupleIntegerTupleReducerFactory()).submit();

        // Get map from result
        Map<AirportTuple, BiIntegerTuple> result = futureResult.get();

        System.out.println("Origen;Destino;Origen->Destino;Destino->Origen");
        for(Map.Entry<AirportTuple, BiIntegerTuple> entry : result.entrySet()){
            AirportTuple atuple=entry.getKey();
            BiIntegerTuple ituple=entry.getValue();
            System.out.println(atuple.getAirport1()+";"+atuple.getAirport2()+";"+ituple.getNumber1()+";"+ituple.getNumber2());
        }
    }




}
