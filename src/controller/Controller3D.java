package controller;

import model.*;
import renderer.GPURenderer;
import renderer.Renderer3D;
import transforms.*;
import view.Raster;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Controller3D {

    private final GPURenderer renderer;
    private final Mat4 model;
    private Mat4 view;
    private Mat4 projection;
    private Mat4 moveModel;
    private final Mat4 projectionPersp;
    private final Mat4 projectionOrtho;
    private Camera camera;
    private int startX = -3000;
    private int startY = -3000;
    private int endX, endY, diferenceX, diferenceY;

    private List<Part> parts;
    private List<Integer> ib;
    private List<Vertex> vb;

    public Controller3D(Raster raster) {
        parts = new ArrayList<>();
        ib = new ArrayList<>();
        vb = new ArrayList<>();

        this.renderer = new Renderer3D(raster);

        model = new Mat4Identity(); // jednotková matice -> nic se s tělesem nestane
        moveModel = new Mat4Identity();

        camera = new Camera()
                .withPosition(new Vec3D(3, -4, 5))
                .withAzimuth(Math.toRadians(90))
                .withZenith(Math.toRadians(-40));

        projectionPersp = new Mat4PerspRH(Math.PI / 3, (float) raster.getScreenSize().height/raster.getScreenSize().width, 0.1, 20);
        projectionOrtho = new Mat4OrthoRH(raster.getScreenSize().width/60.0, raster.getScreenSize().height/60.0, 0.1, 200);
        projection = projectionPersp;

        new Cube(parts, ib, vb, false);
        new Wave(parts, ib, vb, false);
        new Pyramid(parts, ib, vb, false);
        new Axis(parts, ib, vb);

        initListeners(raster);

        display();
    }

    private void display() {
        renderer.clear();

        renderer.setView(camera.getViewMatrix());
        renderer.setProjection(projection);

        // vykreslení těles
        renderer.setModel(model);
        renderer.setMoveModel(moveModel);
        renderer.draw(parts, vb, ib);
    }

    private void initListeners(Raster raster) {

        raster.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                endX = e.getX();
                endY = e.getY();
                diferenceX = startX - endX;
                diferenceY = startY - endY;
                if (startX != -3000 && startY != -3000 && diferenceX < 10 && diferenceX > -10 && diferenceY < 10 && diferenceY > -10) {
                    if (diferenceY > 0) {
                        Camera camera2 = camera.addZenith(Math.toRadians((float) diferenceY/10));
                        if (Math.toDegrees(camera2.getZenith()) < 90 && Math.toDegrees(camera2.getZenith()) > -90){
                            camera = camera2;
                        } else {
                            camera = camera.withZenith(Math.toRadians(90));
                        }
                        display();
                    } else if (diferenceY < 0) {
                        Camera camera2 = camera.addZenith(Math.toRadians((float) diferenceY/10));
                        if (Math.toDegrees(camera2.getZenith()) < 90 && Math.toDegrees(camera2.getZenith()) > -90){
                            camera = camera2;
                        } else {
                            camera = camera.withZenith(Math.toRadians(-90));
                        }
                        display();
                    }
                    if (diferenceX > 0) {
                        camera = camera.addAzimuth(Math.toRadians((float) diferenceX/10));
                        display();
                    } else if (diferenceX < 0) {
                        camera = camera.addAzimuth(Math.toRadians((float) diferenceX/10));
                        display();
                    }
                }
                startX = endX;
                startY = endY;
            }
        });

        raster.addKeyListener(new KeyListener() {
            private final Set<Character> pressed = new HashSet<>();

            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == 'o') {
                    projection = projectionOrtho;
                }
                if (e.getKeyChar() == 'p') {
                    projection = projectionPersp;
                }
                display();
            }

            @Override
            public void keyPressed(KeyEvent e) {
                pressed.add(e.getKeyChar());
                if (pressed.size() > 0) {
                    for (char charE: pressed) {
                        if (charE == 'w') {
                            camera = camera.forward(0.1);
                        }
                        if (charE == 's') {
                            camera = camera.backward(0.1);
                        }
                        if (charE == 'a') {
                            camera = camera.left(0.1);
                        }
                        if (charE == 'd') {
                            camera = camera.right(0.1);
                        }
                        if (charE == 't') {
                            moveModel = moveModel.mul(new Mat4RotX(0.1));
                        }
                        if (charE == 'g') {
                            moveModel = moveModel.mul(new Mat4RotX(-0.1));
                        }
                        if (charE == 'f') {
                            moveModel = moveModel.mul(new Mat4RotY(0.1));
                        }
                        if (charE == 'h') {
                            moveModel = moveModel.mul(new Mat4RotY(-0.1));
                        }
                        if (charE == 'k') {
                            moveModel = moveModel.mul(new Mat4RotZ(0.1));
                        }
                        if (charE == 'l') {
                            moveModel = moveModel.mul(new Mat4RotZ(-0.1));
                        }
                        if (charE == 'u') {
                            parts = new ArrayList<>();
                            ib = new ArrayList<>();
                            vb = new ArrayList<>();

                            new Cube(parts, ib, vb, false);
                            new Wave(parts, ib, vb, false);
                            new Pyramid(parts, ib, vb, false);
                            new Axis(parts, ib, vb);
                        }
                        if (charE == 'i') {
                            parts = new ArrayList<>();
                            ib = new ArrayList<>();
                            vb = new ArrayList<>();

                            new Cube(parts, ib, vb, true);
                            new Wave(parts, ib, vb, true);
                            new Pyramid(parts, ib, vb, true);
                            new Axis(parts, ib, vb);
                        }

                    }
                }
                display();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                pressed.remove(e.getKeyChar());
            }
        });
    }


}
