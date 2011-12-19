package jaxrs.examples.link.clusterservice;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientFactory;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.ResponseHeaders;

/**
 * ClientTest class.
 *
 * @author Santiago.Pericas-Geertsen@oracle.com
 */
public class ClientTest {
    
    public void test() {
        Client client = ClientFactory.newClient();
        
        // Get cluster representation -- entry point
        Response rc = client.target("/cluster").request("application/json").get();
        
        // Ensure cluster is online
        ResponseHeaders rh = rc.getHeaders();
        if (rh.hasLink("onliner")) {
            client.invocation(rh.getLink("onliner")).invoke();
        }
        
        // Start all machines in cluster
        Cluster c = rc.getEntity(Cluster.class);
        for (Machine m : c.getMachines()) {
            // Machine name is need for URI template in link
            Response rm = client.invocation(rh.getLink("item"), m.getName()).invoke();
            
            // Start machine if not started already
            if (rm.getHeaders().hasLink("starter")) {
                client.invocation(rm.getHeaders().getLink("starter")).invoke();
            }
        }
    }
}
