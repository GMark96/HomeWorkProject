/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import org.xbill.DNS.*;
/**
 *
 * @author Mark
 */


/* DNS records and methods to store details and query */
public class DNS {
    private SOARecord       soa;
    private List<NSRecord>  ns;
    private List<MXRecord>  mx;
    private SimpleResolver  resolver;
    private ARecord         a;
    private ARecord         wa; //www.FQDN 
    private AAAARecord       a_4;
    private AAAARecord      wa_4; //www.FQDN
    private List<PTRRecord> ptr;
        
    public void setA(ARecord value)
    {
        a=value;
    }
    public void setA_4(AAAARecord value)
    {
        a_4=value;
    }
    public void setWA(ARecord value)
    {
        wa=value;
    }
    public void setWA_4(AAAARecord value)
    {
        wa_4=value;
    }
    
    public DNS(){
        ns = new ArrayList<NSRecord>();
        mx = new ArrayList<MXRecord>();
        ptr = new ArrayList<PTRRecord>();
    }
    public void addNS(NSRecord value){
        ns.add(value);
    }
    public void addMX(MXRecord value){
        mx.add(value);
    }
    public void setSOA(SOARecord value){
        soa=value;
    }
    public boolean CheckSOA(){
        for (int i=0; i<ns.size(); i++){
            if (ns.get(i).getTarget().equals(soa.getHost())){
                return true;
            }
        }
        return false;
    }
    public void CheckTCPandUDP() throws UnknownHostException, TextParseException{
        for (int i = 0;i<ns.size(); i++){
            Lookup TCPtest = new Lookup(ns.get(i).getTarget().toString());
           
            SimpleResolver resolver = 
                    new SimpleResolver(ns.get(i).getTarget().toString());
            
            resolver.setPort(53);
            resolver.setTCP(true);  //TCP
            TCPtest.setResolver(resolver);
            TCPtest.run();
            
            Lookup UDPtest = new Lookup(ns.get(i).getTarget().toString());
            resolver.setTCP(false); //UDP
            UDPtest.setResolver(resolver);
            UDPtest.run();
            
            if (TCPtest.getResult() == Lookup.SUCCESSFUL){
                System.out.print(ns.get(i).getTarget() +
                        " available from Port nr. 53 (TCP");
                if (UDPtest.getResult() == Lookup.SUCCESSFUL){
                    System.out.print(" and UDP");
                }
                System.out.println(")");
            }else{  
                if (UDPtest.getResult() == Lookup.SUCCESSFUL){
                    System.out.println(ns.get(i).getTarget() +
                            " available from Port nr. 53 (UDP)");
                }else{
                    System.out.println(ns.get(i).getTarget() +
                            " not available from Port nr. 53");
                }
            }
        }   
    }
    
    public void addPTR() throws TextParseException{
        /*A PTR*/
        Name name = ReverseMap.fromAddress(a.getAddress());
        Record [] records = new Lookup(name, Type.PTR).run();
        for (int i = 0; i < records.length; i++) {
            PTRRecord tmp = (PTRRecord) records[i];
            ptr.add(tmp);
            System.out.print(a.getAddress() + ": ");
            System.out.println(tmp.getTarget());
        }
        
        /*www A PTR*/
        name = ReverseMap.fromAddress(wa.getAddress());
        records = new Lookup(name, Type.PTR).run();
        for (int i = 0; i < records.length; i++) {
            PTRRecord tmp = (PTRRecord) records[i];
            System.out.print(wa.getAddress() + ": ");
            ptr.add(tmp);
            System.out.println(tmp.getTarget());
        }
        
        /*AAAA PTR*/
        name = ReverseMap.fromAddress(a_4.getAddress());
        records = new Lookup(name, Type.PTR).run();
        for (int i = 0; i < records.length; i++) {
            PTRRecord tmp = (PTRRecord) records[i];
            System.out.print(a_4.getAddress() + ": ");
            ptr.add(tmp);
            System.out.println(tmp.getTarget());
        }
        /*www AAAA PTR*/
        name = ReverseMap.fromAddress(wa_4.getAddress());
        records = new Lookup(name, Type.PTR).run();
        for (int i = 0; i < records.length; i++) {
            PTRRecord tmp = (PTRRecord) records[i];
            System.out.print(wa_4.getAddress() + ": ");
            ptr.add(tmp);
            System.out.println(tmp.getTarget());
        }
    }
    
        
    public void writeFile(String name) 
            throws UnsupportedEncodingException, FileNotFoundException, IOException{
        String[] tmp = name.split("\\.");
        Writer writer = new BufferedWriter(new OutputStreamWriter(      
              new FileOutputStream(tmp[0]+".txt"), "utf-8")); 
        writer.write("\nSOA: " + "\n mail: " + soa.getAdmin() +
                    "\n Serial: Nr.:" + soa.getSerial() +
                    "\n Refresh: " + soa.getRefresh() +
                    "\n Retry: " + soa.getRetry() +
                    "\n Expire: " + soa.getExpire() +
                    "\n TTL:" + soa.getTTL());
        writer.write("\nNS:");
        for (int i=0;i<ns.size();i++){
            writer.write("\n" + ns.get(i).getTarget().toString());
        }
        writer.write("\nMX:");
        for (int i=0;i<mx.size();i++){
            writer.write("\n" + mx.get(i).getPriority() + "\t" + mx.get(i).getTarget());
        }
        writer.write("\nA:\n" + a.getAddress());
        writer.write("\nA(www):\n" + wa.getAddress());
        writer.write("\nAAAA:\n" + a_4.getAddress());
        writer.write("\nAAAA(www):\n" + wa_4.getAddress());
        writer.write("\nPTR:");
        for (int i=0;i<ptr.size();i++){
            writer.write("\n" + ptr.get(i).getTarget());
        }
        writer.close();
    }
}
    
    