package com.redhat.freelancer.project.model;

import java.io.Serializable;
import java.util.List;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;

@DataObject
public class Project implements Serializable  {

    private String projectId;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String title;
    private String desc;
    private String status;

    public String getProjectId() {
        return projectId;
    }

    public Project(JsonObject json) {
        this.projectId   = json.getString("projectId");
        this.firstName   = json.getString("firstName");
        this.lastName    = json.getString("lastName");
        this.emailAddress = json.getString("emailAddress");
        this.title       = json.getString("title");
        this.desc        = json.getString("desc");
        this.status      = json.getString("status");
    }

    public JsonObject toJson() {

        final JsonObject json = new JsonObject();
        json.put("projectId",   this.projectId  );
        json.put("firstName",   this.firstName  );
        json.put("lastName",    this.lastName   );
        json.put("emailAddress", this.emailAddress);
        json.put("title",       this.title      );
        json.put("desc",        this.desc       );
        json.put("status",      this.status     );

        return json;
    }

}
