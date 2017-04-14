/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.xbill.DNS.*;


/**
 *
 * @author Mark
 */
public class HomeWorkProject {
    private String fqdn;
    /**
     * @param args the command line arguments
     */
    private HomeWorkProject(){
        fqdn="";
    }
    private void start() throws IOException{
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        fqdn = in.readLine();
        QueryDetails();
    }
    public static void main(String[] args) throws IOException {
        new HomeWorkProject().start();
        System.out.println("Hello DNSJAVA");
    }
    public void QueryDetails() throws TextParseException{
                
        Record [] records = new Lookup(fqdn, Type.MX).run();
        for (int i = 0; i < records.length; i++) {
            MXRecord mx = (MXRecord) records[i];
            System.out.println("MX: " + mx.getTarget() + " has preference " + mx.getPriority());
        }
        records = new Lookup(fqdn,Type.NS).run();
        for (int i = 0; i < records.length; i++) {
            NSRecord ns = (NSRecord) records[i];
            System.out.println("NS: " + ns.getTarget());
        }
        records = new Lookup(fqdn,Type.SOA).run();
        for (int i = 0; i < records.length; i++) {
            SOARecord soa = (SOARecord) records[i];
            System.out.println("SOA: " + soa.getHost() +
                    "\n mail: " + soa.getAdmin() +
                    "\n Serial: Nr.:" + soa.getSerial() +
                    "\n Refresh: " + soa.getRefresh() +
                    "\n Retry: " + soa.getRetry() +
                    "\n Expire: " + soa.getExpire() +
                    "\n TTL:" + soa.getTTL());
        }
    }
}
