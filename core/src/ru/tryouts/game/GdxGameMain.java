package ru.tryouts.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class GdxGameMain extends ApplicationAdapter {

    Camera camera;
    private ScreenViewport viewport;
    Mesh mesh;
    ShaderProgram shader;

    long start = TimeUtils.millis();
    private FirstPersonCameraController controller;

    @Override
    public void create() {
        MeshBuilder meshBuilder = new MeshBuilder();
        VertexAttributes attributes = new VertexAttributes(new VertexAttribute(Usage.Position, 3, "a_position"));
        meshBuilder.begin(attributes, GL20.GL_TRIANGLES);
//        meshBuilder.capsule(2f, 4f, 10);
        meshBuilder.box(2f, 4f, 2f);
        mesh = meshBuilder.end();
        shader = new ShaderProgram(Gdx.files.internal("shader.vertex"), Gdx.files.internal("shader.fragment"));

        camera = new PerspectiveCamera();
        viewport = new ScreenViewport(camera);

        camera.position.set(5f, 5f, 5f);
        camera.lookAt(0, 0, 0);
        camera.near = 1f;
        camera.far = 30f;
        camera.update();

        controller = new FirstPersonCameraController(camera);
        Gdx.input.setInputProcessor(controller);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void render() {
        Gdx.gl20.glViewport(0, 0, Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight());
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        controller.update();

        shader.bind();
        shader.setUniformMatrix("u_mvpMatrix", camera.combined);
        shader.setUniformf("u_time", TimeUtils.timeSinceMillis(start));
        shader.setUniformf("u_resolution", new Vector2(camera.viewportWidth, camera.viewportHeight));
        mesh.render(shader, GL20.GL_TRIANGLES);
    }

    @Override
    public void dispose() {
        shader.dispose();
        mesh.dispose();
    }
}
