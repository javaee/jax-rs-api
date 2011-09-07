package jaxrs.examples.link;

import java.net.URI;
import java.net.URISyntaxException;
import javax.ws.rs.Path;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.LinkBuilder;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

/**
 * LinkExamples class.
 *
 * @author Santiago.Pericas-Geertsen@oracle.com
 */
public class LinkExamples {
    
    @Path("employees")
    static class EmployeeResource {  }
    
    /**
     * 3-step process: Build URI, build Link and build Response.
     */
    public Response example1() {
        URI uri = UriBuilder.fromUri("http://foo.bar/employee/john").build();
        Link link = LinkBuilder.fromUri(uri).rel("emp").title("employee").build();
        return Response.ok().linkHeader(link).build();
    }
    
    /**
     * 2-step process: Build Link from String and build Response.
     */
    public Response example2() {
        Link link = LinkBuilder.fromUri("http://foo.bar/employee/john")
                .rel("manager").title("employee").build();
        return Response.ok().linkHeader(link).build();
    }
    
    /**
     * 1-step process: Build Response and add a link directly to it
     * using either a String or a URI.
     * @return 
     */
    public Response example3() throws URISyntaxException {
        Response r;
        r = Response.ok().linkHeader("http://foo.bar/employee/john", "manager").build();
        r = Response.ok().linkHeader(new URI("http://foo.bar/employee/john"), 
                                     "manager").build();
        return r;
    }
    
    /**
     * 3-step process: Build URI, build Link and build Response.
     */
    public Response example11() {
        URI uri = UriBuilder.fromUri("http://foo.bar/employee/john").build();
        Link link = Link.fromUri(uri).rel("emp").title("employee").build();
        return Response.ok().linkHeader(link).build();
    }
    
    /**
     * 2-step process: Build Link from String and build Response.
     */
    public Response example21() {
        Link link = Link.fromUri("http://foo.bar/employee/john")
                .rel("manager").rel("friend").title("employee").type("application/xml").build();
        System.out.println("Link = " + link);
        return Response.ok().build(); // .linkHeader(link).build();
    }
    
    /**
     * 1-step process: Build Response and add a link directly to it
     * using either a String or a URI.
     * @return 
     */
    public Response example31() throws URISyntaxException {
        Response r;
        r = Response.ok().linkHeader("http://foo.bar/employee/john", "manager").build();
        r = Response.ok().linkHeader(new URI("http://foo.bar/employee/john"), 
                                     "manager").build();
        return r;
    }
    
    public static void main(String... args) {
        new LinkExamples().example21();
    }
}
