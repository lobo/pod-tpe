package ar.edu.itba.pod.hz.client;

public class OtaParameter{

    private String name;
    private String password;
    private String[] addresses;

    private int query_number;

    private String airportsInPath;
    private String movementsInPath;
    private String outPath;
    private String timeOutPath;

    private String oaci;
    private int n;
    private int min;

    public OtaParameter(){ //default config
        this.name="user"; //TODO FIX BY ENUNCIADO CHE
        this.password="password";
    }

    public static OtaParameter loadParameters() {
        try {
            OtaParameter ret = new OtaParameter();

            ret.name = System.getProperty("name", "user");
            ret.password = System.getProperty("password", "password");
            String addrs = System.getProperty("addresses", "127.0.0.1");
            ret.addresses = addrs.split("[,;]");

            String queryNumber = System.getProperty("query");
            ret.query_number = Integer.valueOf(queryNumber);

            ret.airportsInPath = System.getProperty("airportsInPath");
            ret.movementsInPath = System.getProperty("movementsInPath");
            ret.outPath = System.getProperty("movementsInPath","");
            ret.timeOutPath = System.getProperty("movementsInPath","");
            if(ret.query_number == 1){
                //ninguno
            }else if(ret.query_number == 2){
                //ninguno
            }else if(ret.query_number == 3){
                //ninguno
            }else if(ret.query_number == 4){
                //oaci,n
                ret.oaci = System.getProperty("oaci");

                String ene = System.getProperty("n");
                ret.n = Integer.valueOf(ene);

            }else if(ret.query_number == 5){
                //n
                String ene = System.getProperty("n");
                ret.n = Integer.valueOf(ene);

            }else if(ret.query_number == 6) {
                //min
                String mins = System.getProperty("min");
                ret.min = Integer.valueOf(mins);

            }else{
                throw new Exception("Invalid query");
            }
            return ret;

        } catch (Exception e) {
            System.out.println("Error in parameters.");
            System.exit(1);
        }
        return null;

    }

    public String getPassword() {
        return password;
    }

    public String[] getAddresses() {
        return addresses;
    }

    public int getQuery_number() {
        return query_number;
    }

    public String getAirportsInPath() {
        return airportsInPath;
    }

    public String getMovementsInPath() {
        return movementsInPath;
    }

    public String getOutPath() {
        return outPath;
    }

    public String getTimeOutPath() {
        return timeOutPath;
    }

    public String getOaci() {
        return oaci;
    }

    public int getN() {
        return n;
    }

    public int getMin() {
        return min;
    }
}