package jaxrs.examples.link.clusterservice;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientFactory;
import javax.ws.rs.core.Response;

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
        if (rc.hasLink("onliner")) {
            client.invocation(rc.getLink("onliner")).invoke();
        }
        
        // Start all machines in cluster
        Cluster c = rc.getEntity(Cluster.class);
        for (Machine m : c.getMachines()) {
            // Machine name is need for URI template in link
            Response rm = client.invocation(rc.getLink("item"), m.getName()).invoke();
            
            // Start machine if not started already
            if (rm.hasLink("starter")) {
                client.invocation(rm.getLink("starter")).invoke();
            }
        }
    }
}
