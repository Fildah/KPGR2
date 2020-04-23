package renderer;

import model.ModelType;
import model.Part;
import model.TopologyType;
import model.Vertex;
import transforms.*;
import view.Raster;

import java.util.List;
import java.util.Optional;

public class Renderer3D implements GPURenderer {

    private final Raster raster;
    private Mat4 model, view, projection, moveModel;
    private final DepthBuffer<Double> zb;

    public Renderer3D(Raster raster) {
        this.raster = raster;
        model = new Mat4Identity();
        moveModel = new Mat4Identity();
        view = new Mat4Identity();
        projection = new Mat4Identity();
        zb = new DepthBuffer<>(new Double[raster.getScreenSize().width][raster.getScreenSize().height]);
    }

    @Override
    public void clear() {
        raster.clear();
        zb.clear(1.0);
    }

    @Override
    public void draw(List<Part> parts, List<Vertex> vb, List<Integer> ib) {
        for (Part part : parts) {
            if (part.getType() == TopologyType.LINES) {
                int start = part.getIndex();
                int end = start + part.getCount();
                for (int index = start; index < end; index += 2) {
                    Integer i1 = ib.get(index);
                    Integer i2 = ib.get(index + 1);
                    Vertex v1 = vb.get(i1);
                    Vertex v2 = vb.get(i2);
                    prepareLine(v1, v2, part.getModelType() == ModelType.AXIS, part.getModelType() == ModelType.CUBE || part.getModelType() == ModelType.PYRAMID);
                }
            }
            if (part.getType() == TopologyType.TRIANGLES) {
                int start = part.getIndex();
                int end = start + part.getCount();
                for (int index = start; index < end; index += 3) {
                    Integer i1 = ib.get(index);
                    Integer i2 = ib.get(index + 1);
                    Integer i3 = ib.get(index + 2);
                    Vertex v1 = vb.get(i1);
                    Vertex v2 = vb.get(i2);
                    Vertex v3 = vb.get(i3);
                    prepareTriangle(v1, v2, v3, part.getModelType() == ModelType.CUBE || part.getModelType() == ModelType.PYRAMID);
                }
            }

        }
    }

    private void prepareLine(Vertex v1, Vertex v2, boolean axis, boolean moveable) {
        Vertex a;
        Vertex b;
        if (axis) {
            a = new Vertex(v1.getPoint().mul(view).mul(projection), v1.getColor());
            b = new Vertex(v2.getPoint().mul(view).mul(projection), v2.getColor());
        } else {
            if (moveable) {
                a = new Vertex(v1.getPoint().mul(model).mul(moveModel).mul(view).mul(projection), v1.getColor());
                b = new Vertex(v2.getPoint().mul(model).mul(moveModel).mul(view).mul(projection), v2.getColor());
            } else {
                a = new Vertex(v1.getPoint().mul(model).mul(view).mul(projection), v1.getColor());
                b = new Vertex(v2.getPoint().mul(model).mul(view).mul(projection), v2.getColor());
            }
        }
        if (-a.getW() > a.getX() && -b.getW() > b.getX()) return;
        if (a.getX() > a.getW() && b.getX() > b.getW()) return;
        if (-a.getW() > a.getY() && -b.getW() > b.getY()) return;
        if (a.getY() > a.getW() && b.getY() > b.getW()) return;
        if (0 > a.getZ() && 0 > b.getZ()) return;
        if (a.getZ() > a.getW() && b.getZ() > b.getW()) return;

        if (a.getZ() < b.getZ()) {
            Vertex temp = a;
            a = b;
            b = temp;
        }

        if (a.getZ() < 0) {
            return;
        } else if (b.getZ() < 0) {
            double t = (0 - a.getZ()) / (b.getZ() - a.getZ());
            Vertex ab = a.mul(1 - t).add(b.mul(t));

            drawLine(a, ab);
        } else {
            drawLine(a, b);
        }
    }

    private void prepareTriangle(Vertex v1, Vertex v2, Vertex v3, boolean moveable) {
        // 1. transformace vrcholu
        Vertex a;
        Vertex b;
        Vertex c;
        if (moveable) {
            a = new Vertex(v1.getPoint().mul(model).mul(moveModel).mul(view).mul(projection), v1.getColor());
            b = new Vertex(v2.getPoint().mul(model).mul(moveModel).mul(view).mul(projection), v2.getColor());
            c = new Vertex(v3.getPoint().mul(model).mul(moveModel).mul(view).mul(projection), v3.getColor());

        } else {
            a = new Vertex(v1.getPoint().mul(model).mul(view).mul(projection), v1.getColor());
            b = new Vertex(v2.getPoint().mul(model).mul(view).mul(projection), v2.getColor());
            c = new Vertex(v3.getPoint().mul(model).mul(view).mul(projection), v3.getColor());

        }

        // 2. orezani
        // orez trojuhelniku ktery je cely mimo
        if (-a.getW() > a.getX() && -b.getW() > b.getX() && -c.getW() > c.getX()) return;
        if (a.getX() > a.getW() && b.getX() > b.getW() && c.getX() > c.getW()) return;
        if (-a.getW() > a.getY() && -b.getW() > b.getY() && -c.getW() > c.getY()) return;
        if (a.getY() > a.getW() && b.getY() > b.getW() && c.getY() > c.getW()) return;
        if (0 > a.getZ() && 0 > b.getZ() && 0 > c.getZ()) return;
        if (a.getZ() > a.getW() && b.getZ() > b.getW() && c.getZ() > c.getW()) return;

        // 3. seradit podle Z
        if (a.getZ() < b.getZ()) {
            Vertex temp = a;
            a = b;
            b = temp;
        }
        if (b.getZ() < c.getZ()) {
            Vertex temp = b;
            b = c;
            c = temp;
        }
        if (a.getZ() < b.getZ()) {
            Vertex temp = a;
            a = b;
            b = temp;
        }

        // 4. orezani podle hrany Z
        if (a.getZ() < 0) {
            return;
        } else if (b.getZ() < 0) {
            double t = (0 - a.getZ()) / (b.getZ() - a.getZ());
            Vertex ab = a.mul(1 - t).add(b.mul(t));

            double t2 = (0 - a.getZ()) / (c.getZ() - a.getZ());
            Vertex ac = a.mul(1 - t2).add(b.mul(t2));

            drawTriangle(a, ab, ac);
        } else if (c.getZ() < 0) {
            double t = (0 - b.getZ()) / (c.getZ() - b.getZ());
            Vertex bc = b.mul(1 - t).add(c.mul(t));

            double t2 = (0 - a.getZ()) / (c.getZ() - a.getZ());
            Vertex ac = a.mul(1 - t2).add(b.mul(t2));

            drawTriangle(a, b, bc);
            drawTriangle(a, bc, ac);
        } else {
            drawTriangle(a, b, c);
        }
        
    }

    private void drawLine(Vertex a, Vertex b) {
        Optional<Vertex> dA = a.dehomog();
        Optional<Vertex> dB = b.dehomog();

        if (!dA.isPresent() || !dB.isPresent()) return;

        Vertex v1 = dA.get();
        Vertex v2 = dB.get();

        Vec3D vec3D1 = transformToWindow(v1.getPoint());
        Vertex aa = new Vertex(new Point3D(vec3D1), v1.getColor());
        Vec3D vec3D2 = transformToWindow(v2.getPoint());
        Vertex bb = new Vertex(new Point3D(vec3D2), v2.getColor());


        if (aa.getY() > bb.getY()) {
            Vertex temp = aa;
            aa = bb;
            bb = temp;
        }

        if (Math.abs(aa.getX() - bb.getX()) > Math.abs(aa.getY() - bb.getY())) {
            if (aa.getX() > bb.getX()) {
                Vertex temp = aa;
                aa = bb;
                bb = temp;
            }
            for (int x = Math.max((int) aa.getX() + 1, 0); x < Math.min(bb.getX(), raster.getScreenSize().width - 1); x++) {
                double tz = (x - aa.getX()) / (bb.getX() - aa.getX());
                double z = aa.getZ() * (1.0 - tz) + bb.getZ() * tz;
                int y = (int) ((1.0 - tz) * aa.getY() + tz * bb.getY());
                if (y < 0 || y >= raster.getScreenSize().height) {
                    continue;
                }
                Vertex ab = a.mul(1 - tz).add(b.mul(tz));
                drawPixel(x, y, z, ab.getColor());
            }
        } else {
            if (aa.getY() > bb.getY()) {
                Vertex temp = aa;
                aa = bb;
                bb = temp;
            }
            for (int y = Math.max((int) aa.getY() + 1, 0); y < Math.min(bb.getY(), raster.getScreenSize().height - 1); y++) {
                double tz = (y - aa.getY()) / (bb.getY() - aa.getY());
                double z = aa.getZ() * (1.0 - tz) + bb.getZ() * tz;
                int x = (int) ((1.0 - tz) * aa.getX() + tz * bb.getX());
                if (x < 0 || x >= raster.getScreenSize().width) {
                    continue;
                }
                Vertex ab = a.mul(1 - tz).add(b.mul(tz));
                drawPixel(x, y, z, ab.getColor());
            }
        }
    }

    private void drawTriangle(Vertex a, Vertex b, Vertex c) {
        Optional<Vertex> dA = a.dehomog();
        Optional<Vertex> dB = b.dehomog();
        Optional<Vertex> dC = c.dehomog();

        if (!dA.isPresent() || !dB.isPresent() || !dC.isPresent()) return;

        Vertex v1 = dA.get();
        Vertex v2 = dB.get();
        Vertex v3 = dC.get();

        Vec3D vec3D1 = transformToWindow(v1.getPoint());
        Vertex aa = new Vertex(new Point3D(vec3D1), v1.getColor());
        Vec3D vec3D2 = transformToWindow(v2.getPoint());
        Vertex bb = new Vertex(new Point3D(vec3D2), v2.getColor());
        Vec3D vec3D3 = transformToWindow(v3.getPoint());
        Vertex cc = new Vertex(new Point3D(vec3D3), v3.getColor());

        // 3. serazeni podle Y
        if (aa.getY() > bb.getY()) {
            Vertex temp = aa;
            aa = bb;
            bb = temp;
        }
        if (bb.getY() > cc.getY()) {
            Vertex temp = bb;
            bb = cc;
            cc = temp;
        }
        if (aa.getY() > bb.getY()) {
            Vertex temp = aa;
            aa = bb;
            bb = temp;
        }

        for (int y = Math.max((int) aa.getY() + 1, 0); y <= Math.min(bb.getY(), raster.getScreenSize().height - 1); y++) {
            double t = (y - aa.getY()) / (bb.getY() - aa.getY());
            Vertex d = aa.mul(1 - t).add(bb.mul(t));

            double t2 = (y - aa.getY()) / (cc.getY() - aa.getY());
            Vertex e = aa.mul(1 - t2).add(cc.mul(t2));

            fillLine(d, e);
        }

        for (int y = Math.max((int) bb.getY() + 1, 0); y <= Math.min(cc.getY(), raster.getScreenSize().width - 1); y++) {
            double t = (y - aa.getY()) / (cc.getY() - aa.getY());
            Vertex f = aa.mul(1 - t).add(cc.mul(t));

            double t2 = (y - bb.getY()) / (cc.getY() - bb.getY());
            Vertex g = bb.mul(1 - t2).add(cc.mul(t2));

            fillLine(f, g);
        }
    }

    private void fillLine(Vertex a, Vertex b) {
        if (a.getX() > b.getX()) {
            Vertex temp = a;
            a = b;
            b = temp;
        }

        for (int x = (int) Math.max(a.getX() + 1, 0); x <= Math.min(b.getX(), raster.getScreenSize().width - 1); x++) {
            double tz = (x - a.getX()) / (b.getX() - a.getX());
            double z = a.getZ() * (1 - tz) + b.getZ() * tz;
            Vertex ab = a.mul(1 - tz).add(b.mul(tz));
            if ((int) Math.round(a.getY()) < raster.getScreenSize().height) {
                drawPixel(x, (int) Math.round(a.getY()), z, ab.getColor());
            }

        }
    }

    private void drawPixel(int x, int y, double z, Col color) {
        // 1 > 0.5
        if (zb.get(x, y) > z) {
            zb.set(x, y, z);
            raster.drawPixel(x, y, color.getRGB());
        }
    }

    private Vec3D transformToWindow(Point3D point3D) {
        return new Vec3D(point3D)
                .mul(new Vec3D(1, -1, 1)) // Y jde nahoru a my ho chceme dolů
                .add(new Vec3D(1, 1, 0)) // (0,0) je uprostřed, chceme v rohu
                // máme <0;2> -> vynásobíme polovinou velikosti okna
                .mul(new Vec3D((raster.getScreenSize().width - 1) / 2f, (raster.getScreenSize().height - 1) / 2f, 1));
    }

    public void setMoveModel(Mat4 moveModel) {
        this.moveModel = moveModel;
    }

    @Override
    public void setModel(Mat4 model) {
        this.model = model;
    }

    @Override
    public void setView(Mat4 view) {
        this.view = view;
    }

    @Override
    public void setProjection(Mat4 projection) {
        this.projection = projection;
    }

}
