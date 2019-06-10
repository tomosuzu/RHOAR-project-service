package com.redhat.freelancer.project.verticle.service;

import com.redhat.freelancer.project.model.Project;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

import java.util.List;
import java.util.stream.Collectors;

public class ProjectServiceImpl implements ProjectService {
    private MongoClient client;

    public ProjectServiceImpl(Vertx vertx, JsonObject config, MongoClient client) {
        this.client = client;
    }

    @Override
    public void getProjects(Handler<AsyncResult<List<Project>>> resulthandler) {
        JsonObject query = new JsonObject();
        client.find("Projects", query, ar -> {
            if (ar.succeeded()) {
                List<Project> Projects = ar.result().stream()
                        .map(json -> new Project(json))
                        .collect(Collectors.toList());
                resulthandler.handle(Future.succeededFuture(Projects));
            } else {
                resulthandler.handle(Future.failedFuture(ar.cause()));
            }
        });
    }
}
