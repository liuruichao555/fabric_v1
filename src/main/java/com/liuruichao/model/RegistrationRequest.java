package com.liuruichao.model;

import lombok.Data;

import javax.json.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

/**
 * RegistrationRequest
 *
 * @author liuruichao
 * Created on 2017/3/22 18:13
 */
@Data
public class RegistrationRequest {

    // The enrollment ID of the user
    private String enrollmentID;
    // Type of identity
    private String type;
    // Optional secret
    private String secret;
    // Maximum number of enrollments with the secret
    private int maxEnrollments;
    // Affiliation for a user
    private String affiliation;
    // Array of attribute names and values
    private ArrayList<Attribute> attrs = new ArrayList<Attribute>();

    // Constructor
    public RegistrationRequest(String id, String affiliation) throws Exception {
        if (id == null) {
            throw new Exception("id may not be null");
        }
        if (affiliation == null) {
            throw new Exception("affiliation may not be null");
        }
        this.enrollmentID = id;
        this.affiliation = affiliation;
        this.type = "user";
    }

    // Convert the registration request to a JSON string
    public String toJson() {
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = Json.createWriter(new PrintWriter(stringWriter));
        jsonWriter.writeObject(this.toJsonObject());
        jsonWriter.close();
        return stringWriter.toString();
    }

    // Convert the registration request to a JSON object
    public JsonObject toJsonObject() {
        JsonObjectBuilder ob = Json.createObjectBuilder();
        ob.add("id", this.enrollmentID);
        ob.add("type",  this.type);
        if (this.secret != null) {
            ob.add("secret",  this.secret);
        }
        ob.add("max_enrollments",  this.maxEnrollments);
        ob.add("affiliation",  this.affiliation);
        ob.add("group",  this.affiliation);  // TODO: REMOVE THIS WHEN API IS CHANGED  (See https://jira.hyperledger.org/browse/FAB-2534)
        JsonArrayBuilder ab = Json.createArrayBuilder();
        for (Attribute attr: this.attrs) {
            ab.add(attr.toJsonObject());
        }
        return ob.build();
    }


}
