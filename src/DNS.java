/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import org.xbill.DNS.*;
/**
 *
 * @author Mark
 */
public class DNS {
    private SOARecord soa;
    private List<NSRecord> ns;
    private List<MXRecord> mx;
    private SimpleResolver resolver;
    
    public DNS(){
        ns = new ArrayList<NSRecord>();
        mx = new ArrayList<MXRecord>();
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
    public void CheckTCPandUDP() throws UnknownHostException{
        for (int i = 0;i<ns.size(); i++){
           SimpleResolver tmp;
            tmp = new SimpleResolver(ns.get(i).getTarget().toString());
            System.out.println(tmp.DEFAULT_PORT);
        }
                
    }
      
}
    
