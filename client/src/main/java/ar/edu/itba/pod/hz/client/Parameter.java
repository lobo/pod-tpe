package ar.edu.itba.pod.hz.client;

import com.sun.org.apache.xpath.internal.operations.Bool;

public class Parameter {

    private String name;
    private String password;
    private String[] addresses;

    private int query_number;

    private String airportsInPath;
    private String movementsInPath;
    private String outPath;
    private String timeOutPath;

    private Boolean reload;

    private String oaci;
    private int n;
    private int min;

    public Parameter(){ //default config
        this.name="user"; //TODO FIX BY ENUNCIADO CHE
        this.password="password";
    }

    public static Parameter loadParameters() {
        try {
            Parameter ret = new Parameter();

            ret.name = System.getProperty("name", "52539-53891");
            ret.password = System.getProperty("password", "pass");
            String addrs = System.getProperty("addresses", "127.0.0.1");
            ret.addresses = addrs.split("[,;]");

            String queryNumber = System.getProperty("query","-1");
            ret.query_number = Integer.valueOf(queryNumber);
            if(ret.query_number==-1){
                throw new Exception("El numero de query es obligatorio");
            }

            String sReload = System.getProperty("reload","true");
            ret.reload = Boolean.valueOf(sReload);

            ret.airportsInPath = System.getProperty("airportsInPath","aeropuertos.csv");
            ret.movementsInPath = System.getProperty("movementsInPath","movimientos_short.csv");
            ret.outPath = System.getProperty("movementsInPath","");
            ret.timeOutPath = System.getProperty("timeOutPath","");
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

            }else {
                throw new Exception("Invalid query");
            }
            return ret;

        } catch (Exception e) {
            System.out.println("Error en los parametros.");
            System.exit(1);
        }
        return null;

    }

    public String getName() {
        return name;
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

    public boolean getReload(){return reload;}
}