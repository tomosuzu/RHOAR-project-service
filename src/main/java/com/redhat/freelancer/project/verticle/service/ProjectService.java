package com.redhat.freelancer.project.verticle.service;

import com.redhat.freelancer.project.model.Project;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

import java.util.List;

@ProxyGen
public interface ProjectService {

    final static String ADDRESS = "Project-service";

    static ProjectService create(Vertx vertx, JsonObject config, MongoClient client) {
        return new ProjectServiceImpl(vertx, config, client);
    }

    // todo something
    static ProjectService createProxy(Vertx vertx) {
        return new ProjectServiceVertxEBProxy(vertx, ADDRESS);
    }

    void getProjects(Handler<AsyncResult<List<Project>>> resulthandler);

//    void getProject(String itemId, Handler<AsyncResult<Project>> resulthandler);
//
//    void addProject(Project project, Handler<AsyncResult<String>> resulthandler);

}
