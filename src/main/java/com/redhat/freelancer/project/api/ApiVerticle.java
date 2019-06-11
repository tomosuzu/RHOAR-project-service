package com.redhat.freelancer.project.api;

import com.redhat.freelancer.project.model.Project;
import com.redhat.freelancer.project.verticle.service.ProjectService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

import java.util.List;

public class ApiVerticle extends AbstractVerticle {
    private ProjectService projectService;

    public ApiVerticle(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Override
    public void start(Future<Void> startFuture) throws Exception {

        Router router = Router.router(vertx);
        router.get("/projects").handler(this::getProjects);
        router.get("/projects/:projectId").handler(this::getProject);
        router.get("/projects/status/:status").handler(this::getStatus);
        router.route("/project").handler(BodyHandler.create());
        router.post("/project").handler(this::addProject);

        vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(config().getInteger("project.http.port", 8080), result -> {
                    if (result.succeeded()) {
                        startFuture.complete();
                    } else {
                        startFuture.fail(result.cause());
                    }
                });
    }

    private void getProjects(RoutingContext rc) {
        projectService.getProjects(ar -> {
            if (ar.succeeded()) {
                List<Project> projects = ar.result();
                JsonArray json = new JsonArray();
                projects.stream()
                        .map(p -> p.toJson())
                        .forEach(p -> json.add(p));
                rc.response()
                        .putHeader("Content-type", "application/json")
                        .end(json.encodePrettily());
            } else {
                rc.fail(ar.cause());
            }
        });
    }

    private void getProject(RoutingContext rc) {
        String projectId = rc.request().getParam("projectId");
        projectService.getProject(projectId, ar -> {
            if (ar.succeeded()) {
                List<Project> projects = ar.result();
                JsonArray json = new JsonArray();
                projects.stream()
                        .map(p -> p.toJson())
                        .forEach(p -> json.add(p));
                rc.response()
                        .putHeader("Content-type", "application/json")
                        .end(json.encodePrettily());
            } else {
                rc.fail(ar.cause());
            }
        });
    }

    private void getStatus(RoutingContext rc) {
        String status = rc.request().getParam("status");
        projectService.getStatus(status, ar -> {
            if (ar.succeeded()) {
                List<Project> projects = ar.result();
                JsonArray json = new JsonArray();
                projects.stream()
                        .map(p -> p.toJson())
                        .forEach(p -> json.add(p));
                rc.response()
                        .putHeader("Content-type", "application/json")
                        .end(json.encodePrettily());
            } else {
                rc.fail(ar.cause());
            }
        });
    }

    private void addProject(RoutingContext rc) {
        JsonObject json = rc.getBodyAsJson();
        projectService.addProject(new Project(json), ar -> {
            if (ar.succeeded()) {
                rc.response().setStatusCode(201).end();
            } else {
                rc.fail(ar.cause());
            }
        });
    }
}
