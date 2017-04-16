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
    private DNS dns;
    /**
     * @param args the command line arguments
     */
    private HomeWorkProject(){
        fqdn="";
        dns = new DNS();
    }
    private void start() throws IOException{
        
        System.out.println("Enter the FQDN: ");
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        fqdn = in.readLine();
        
        //Print details and save it
        QueryDetails();
        
        //checking SOA and NS
        System.out.println("3. task: ");
        if (dns.CheckSOA()){
            System.out.println("SOA consistent with NS");
        }else{
            System.out.println("SOA NOT consistent with NS");
        }
        dns.CheckTCPandUDP();
    }
    public static void main(String[] args) throws IOException {
        new HomeWorkProject().start();
    }
    
    public void QueryDetails() throws TextParseException{
                
        Record [] records = new Lookup(fqdn, Type.MX).run();
        for (int i = 0; i < records.length; i++) {
            MXRecord mx = (MXRecord) records[i];
            dns.addMX(mx);
            System.out.println("MX: " + mx.getTarget() + " has preference " + mx.getPriority());
        }
        records = new Lookup(fqdn,Type.NS).run();
        for (int i = 0; i < records.length; i++) {
            NSRecord ns = (NSRecord) records[i];
            dns.addNS(ns);
            System.out.println("NS: " + ns.getTarget());
        }
        records = new Lookup(fqdn,Type.SOA).run();
        for (int i = 0; i < records.length; i++) {
            SOARecord soa = (SOARecord) records[i];
            dns.setSOA(soa);
            System.out.println("SOA: " + soa.getHost() +
                    "\n mail: " + soa.getAdmin() +
                    "\n Serial: Nr.:" + soa.getSerial() +
                    "\n Refresh: " + soa.getRefresh() +
                    "\n Retry: " + soa.getRetry() +
                    "\n Expire: " + soa.getExpire() +
                    "\n TTL:" + soa.getTTL());
        }
        
        System.out.println("5. task");
        records = new Lookup(fqdn,Type.A).run();
        for (int i = 0; i < records.length; i++) {
            ARecord a = (ARecord) records[i];
            dns.setA(a);
            System.out.println("A: " + a.getAddress());
        }
        records = new Lookup(fqdn,Type.AAAA).run();
        for (int i = 0; i < records.length; i++) {
            AAAARecord aaaa = (AAAARecord) records[i];
            dns.setA_4(aaaa);
            System.out.println("AAAA: " + aaaa.getAddress());
        }
        records = new Lookup("www."+fqdn,Type.A).run();
        for (int i = 0; i < records.length; i++) {
            ARecord a = (ARecord) records[i];
            dns.setWA(a);
            System.out.println("(www) A: " + a.getAddress());
        }
        records = new Lookup("www."+fqdn,Type.AAAA).run();
        for (int i = 0; i < records.length; i++) {
            AAAARecord aaaa = (AAAARecord) records[i];
            dns.setWA_4(aaaa);
            System.out.println("(www) AAAA: " + aaaa.getAddress());
        }
        
    }
   
}
