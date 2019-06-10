package com.redhat.freelancer.project.api;

import com.redhat.freelancer.project.model.Project;
import com.redhat.freelancer.project.verticle.service.ProjectService;
import io.vertx.core.*;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(VertxUnitRunner.class)
public class ApiVerticleTest {
    private Vertx vertx;
    private Integer port;
    private ProjectService projectService;

    @Before
    public void setUp(TestContext context) throws IOException {
        vertx = Vertx.vertx();

        // Register the context exception handler
        vertx.exceptionHandler(context.exceptionHandler());

        // Let's configure the verticle to listen on the 'test' port (randomly picked).
        // We create deployment options and set the _configuration_ json object:
        ServerSocket socket = new ServerSocket(0);
        port = socket.getLocalPort();
        socket.close();

        DeploymentOptions options = new DeploymentOptions().setConfig(new JsonObject().put("project.http.port", port));

        //Mock the Product Service
        projectService = mock(ProjectService.class);

        // We pass the options as the second parameter of the deployVerticle method.
        vertx.deployVerticle(new ApiVerticle(projectService), options, context.asyncAssertSuccess());
    }

    @Test
    public void testGetProjects(TestContext context) throws Exception {
        String projectId1 = "111111";
        JsonObject json1 = new JsonObject()
                .put("projectId", projectId1)
                .put("firstName", "firstName1")
                .put("lastName",  "lastName1")
                .put("emailAdress",  "emailAdress1")
                .put("title",  "title1")
                .put("desc", "description1")
                .put("status", "open");
        String projectId2 = "222222";
        JsonObject json2 = new JsonObject()
                .put("projectId", projectId2)
                .put("firstName", "firstName2")
                .put("lastName",  "lastName2")
                .put("emailAdress",  "emailAdress2")
                .put("title",  "title2")
                .put("desc", "description2")
                .put("status", "in_progress");

        List<Project> projects = new ArrayList<>();
        projects.add(new Project(json1));
        projects.add(new Project(json2));
        doAnswer(new Answer<Void>() {
            public Void answer(InvocationOnMock invocation){
                Handler<AsyncResult<List<Project>>> handler = invocation.getArgument(0);
                handler.handle(Future.succeededFuture(projects));
                return null;
            }
        }).when(projectService).getProjects(any());

        Async async = context.async();
        vertx.createHttpClient().get(port, "localhost", "/projects", response -> {
            assertThat(response.statusCode(), equalTo(200));
            assertThat(response.headers().get("Content-type"), equalTo("application/json"));
            response.bodyHandler(body -> {
                JsonArray json = body.toJsonArray();
                Set<String> itemIds =  json.stream()
                        .map(j -> new Project((JsonObject)j))
                        .map(p -> p.getProjectId())
                        .collect(Collectors.toSet());
                assertThat(itemIds.size(), equalTo(2));
                assertThat(itemIds, allOf(hasItem(projectId1),hasItem(projectId2)));
                verify(projectService).getProjects(any());
                async.complete();
            })
                    .exceptionHandler(context.exceptionHandler());
        })
                .exceptionHandler(context.exceptionHandler())
                .end();
    }
}
