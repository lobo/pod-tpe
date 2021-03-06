package ar.edu.itba.pod.hz.client;

import ar.edu.itba.pod.hz.client.reader.AirportsReader;
import ar.edu.itba.pod.hz.client.reader.MovementsReader;
import ar.edu.itba.pod.hz.model.*;
import ar.edu.itba.pod.hz.mr.query1.MovementCounterMapper;
import ar.edu.itba.pod.hz.mr.query1.MovementCounterReducerFactory;
import ar.edu.itba.pod.hz.mr.query2.*;
import ar.edu.itba.pod.hz.mr.query3.AirportTupleIntegerTupleReducerFactory;
import ar.edu.itba.pod.hz.mr.query3.OriginDestinationMapper;
import ar.edu.itba.pod.hz.mr.query4.OrderByCollator;
import ar.edu.itba.pod.hz.mr.query4.OrderByKeyAndValueNCollator;
import ar.edu.itba.pod.hz.mr.query4.OrderKeyAndValueCollator;
import ar.edu.itba.pod.hz.mr.query5.MovementCounter2Reducer;
import ar.edu.itba.pod.hz.mr.query5.MovementInternationalMapper;
import ar.edu.itba.pod.hz.mr.query6.*;
import ar.edu.itba.pod.hz.mr.query4.AirportLandingFromOaciMapper;
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

import static java.lang.Math.floor;

public class Client {


    class Frmt extends Formatter {
        private final DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss:SSSS");

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
        this.logger.setUseParentHandlers(false);
        if(timeOutPath.equals("")){ // console logger
            Handler h = new ConsoleHandler();
            h.setFormatter(new Frmt());
            this.logger.addHandler(h);
        }else{ //TODO FIX FORMAT

            this.FhtimeOutPath = new FileHandler(timeOutPath);
            this.FhtimeOutPath.setFormatter(new Frmt());
            this.logger.addHandler(this.FhtimeOutPath);
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
            if(p.getReload()){
//                movementMap.clear();
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
            }else{
                logger.info("Omitiendo la carga de los CSV");
            }

            logger.info("Procesando la query");
            switch (p.getQuery_number()) {
                case 1:
                    queryClient.query1(movementMap, airportsMap);
                    break;
                case 2: //TODO corregir
                    queryClient.query2(movementMap);
                    break;
                case 3:
                    queryClient.query3(movementMap);
                    break;
                case 4:
                    queryClient.query4(movementMap,p.getOaci(),p.getN());
                    break;
                case 5:
                    queryClient.query5(movementMap,airportsMap,p.getN());
                    break;
                case 6: //TODO ordernar y comparable
                    queryClient.query6(movementMap, airportsMap, p.getMin());
                    break;
            }
            logger.info("Terminado de procesar la query");
            queryClient.outPath.flush();
            System.exit(0);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void query1(IMap<Integer, MovementData> movementsMap, IMap<String, AirportData> airportsMap)
            throws InterruptedException, ExecutionException, FileNotFoundException, UnsupportedEncodingException {

        JobTracker tracker = client.getJobTracker(JOB_TRACKER);

        KeyValueSource<Integer, MovementData> source = KeyValueSource.fromMap(movementsMap);
        Job<Integer, MovementData> job = tracker.newJob(source);

        JobCompletableFuture<List<Map.Entry<String, Integer>>> futureResult = job.mapper(new MovementCounterMapper())
                .reducer(new MovementCounterReducerFactory()).submit(new OrderKeyAndValueCollator<>(false,true,false));

        List<Map.Entry<String, Integer>> result = futureResult.get();

        this.outPath.println("OACI;Denominación;Movimientos");
        for(Map.Entry<String, Integer> airport : result) {
            AirportData airportData = airportsMap.get(airport.getKey());
            if(airportData != null) {
                this.outPath.println(airport.getKey() + ";" + airportData.getDenomination() +  ";" + airport.getValue());
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
                .reducer(new MovementCounterReducerFactory()).submit();

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
        JobCompletableFuture<List<Map.Entry<Integer, List<AirportTuple>>>> futureResult = job2.mapper(new MovementCounterOverThousandsMapper())
                .reducer(new SameKeyGrouperReducerFactory()).submit(new OrderByKeyCollator<>(false));

        // Get map from result
        List<Map.Entry<Integer, List<AirportTuple>>> result = futureResult.get();

        // Iterate through entries to print them
        this.outPath.println("Grupo;Aeropuerto A;Aeropuerto B");
        for(Map.Entry<Integer, List<AirportTuple>> entry : result) {
            Integer millennium = entry.getKey();
            for(AirportTuple tuple : entry.getValue()) {
                this.outPath.println((millennium) + ";" + tuple.getAirport1() + ";" + tuple.getAirport2());
            }
        }
    }

    public void query3(IMap<Integer, MovementData> movementsMap) throws ExecutionException, InterruptedException {


        JobTracker tracker = client.getJobTracker(JOB_TRACKER);

        KeyValueSource<Integer, MovementData> source = KeyValueSource.fromMap(movementsMap);

        Job<Integer, MovementData> job = tracker.newJob(source);


        // Submit map-reduce job
        JobCompletableFuture<List<Map.Entry<AirportTuple, BiIntegerTuple>>> futureResult = job.mapper(new OriginDestinationMapper())
                .reducer(new AirportTupleIntegerTupleReducerFactory()).submit(new OrderByCollator<AirportTuple, BiIntegerTuple>(true,true));

        // Get map from result
        List<Map.Entry<AirportTuple, BiIntegerTuple>> result = futureResult.get();

        this.outPath.println("Origen;Destino;Origen->Destino;Destino->Origen");
        for(Map.Entry<AirportTuple, BiIntegerTuple> entry : result){
            AirportTuple atuple=entry.getKey();
            BiIntegerTuple ituple=entry.getValue();
            this.outPath.println(atuple.getAirport1()+";"+atuple.getAirport2()+";"+ituple.getNumber1()+";"+ituple.getNumber2());
        }
    }

    public void query4(IMap<Integer, MovementData> movementsMap,String oaci,Integer n) throws ExecutionException, InterruptedException {
        int q=0;

        JobTracker tracker = client.getJobTracker(JOB_TRACKER);

        KeyValueSource<Integer, MovementData> source = KeyValueSource.fromMap(movementsMap);

        Job<Integer, MovementData> job = tracker.newJob(source);

        // Submit map-reduce job
        JobCompletableFuture<List<Map.Entry<String, Integer>>> futureResult = job.mapper(new AirportLandingFromOaciMapper(oaci))
                .reducer(new MovementCounterReducerFactory()).submit(new OrderByKeyAndValueNCollator<>(n,false,true,false));

        // Get map from result
        this.outPath.println("OACI;Aterrizajes");

        List<Map.Entry<String, Integer>> result = futureResult.get();
        for(Map.Entry<String, Integer> entry : result) {
            this.outPath.println(entry.getKey()+";"+entry.getValue());

        }
    }

    public void query5(IMap<Integer, MovementData> movementsMap, IMap<String, AirportData> airportsMap,Integer n) throws ExecutionException, InterruptedException {
        int q=0;

        // Find job tracker
        JobTracker tracker = client.getJobTracker(JOB_TRACKER);

        // Create source for first map-reduce job
        KeyValueSource<Integer, MovementData> movements = KeyValueSource.fromMap(movementsMap);


        // Create job
        Job<Integer, MovementData> job = tracker.newJob(movements);

        // Submit map-reduce job
        JobCompletableFuture<List<Map.Entry<String, Integer>>> futureResult = job.mapper(new MovementInternationalMapper())
                .reducer(new MovementCounter2Reducer()).submit(new OrderByKeyAndValueNCollator<>(n,false,true,false));

        // Get map from result
        List<Map.Entry<String, Integer>> result = futureResult.get();

        // Iterate through entries to print them
        this.outPath.println("IATA;Porcentaje");

        double total=1.0;
        for(Map.Entry<String,Integer> entry : result) {
            AirportData ad=airportsMap.get(entry.getKey());
            if(ad!=null && !entry.getKey().equals("")) {
                this.outPath.println(ad.getIata() + ';' + entry.getValue() + '%');
            }

        }
    }


    public void query6(IMap<Integer, MovementData> movementsMap, IMap<String, AirportData> airportsMap, Integer min) throws ExecutionException, InterruptedException {
        JobTracker tracker = client.getJobTracker(JOB_TRACKER);

        final String INTERMEDIATE_MAP_NAME = "LEGAJOS-INTERMEDIATE";

        // Use created map as source for job
        KeyValueSource<Integer, MovementData> source = KeyValueSource.fromMap(movementsMap);

        // Create job
        Job<Integer, MovementData> job = tracker.newJob(source);

        // Submit map-reduce job
        JobCompletableFuture<List<Map.Entry<ProvinceTuple, Integer>>> futureResult = job.mapper(new MovementToProvinceTupleMapper(AIRPORT_MAP_NAME))
//                .combiner(new ProvinceTupleCombiner())
                .reducer(new ProvToProvMoveCounterReducerFactory())
                .submit(new OrderAndLimitCollator(min,false,true,false));

        // Get result
        List<Map.Entry<ProvinceTuple, Integer>> result = futureResult.get();

        this.outPath.println("Provincia A;Provincia B;Movimientos");
        for(Map.Entry<ProvinceTuple, Integer> entry : result) {
            ProvinceTuple tuple = entry.getKey();
            this.outPath.println(tuple.getProvince1() + ";" + tuple.getProvince2() + ";" + entry.getValue());
        }
    }




}
