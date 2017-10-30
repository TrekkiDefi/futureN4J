
package com.github.ittalks.commons.example.ws.jax.demo3.server;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.github.ittalks.commons.example.ws.jax.demo3.server package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GetRoleByUserResponse_QNAME = new QName("http://client.demo3.jax.ws.example.commons.ittalks.github.com/", "getRoleByUserResponse");
    private final static QName _GetRoleByUser_QNAME = new QName("http://client.demo3.jax.ws.example.commons.ittalks.github.com/", "getRoleByUser");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.github.ittalks.commons.example.ws.jax.demo3.server
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetRoleByUserResponse }
     * 
     */
    public GetRoleByUserResponse createGetRoleByUserResponse() {
        return new GetRoleByUserResponse();
    }

    /**
     * Create an instance of {@link GetRoleByUser }
     * 
     */
    public GetRoleByUser createGetRoleByUser() {
        return new GetRoleByUser();
    }

    /**
     * Create an instance of {@link Role }
     * 
     */
    public Role createRole() {
        return new Role();
    }

    /**
     * Create an instance of {@link User }
     * 
     */
    public User createUser() {
        return new User();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetRoleByUserResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://client.demo3.jax.ws.example.commons.ittalks.github.com/", name = "getRoleByUserResponse")
    public JAXBElement<GetRoleByUserResponse> createGetRoleByUserResponse(GetRoleByUserResponse value) {
        return new JAXBElement<GetRoleByUserResponse>(_GetRoleByUserResponse_QNAME, GetRoleByUserResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetRoleByUser }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://client.demo3.jax.ws.example.commons.ittalks.github.com/", name = "getRoleByUser")
    public JAXBElement<GetRoleByUser> createGetRoleByUser(GetRoleByUser value) {
        return new JAXBElement<GetRoleByUser>(_GetRoleByUser_QNAME, GetRoleByUser.class, null, value);
    }

}
