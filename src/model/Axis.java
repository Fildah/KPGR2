package model;

import transforms.Col;
import transforms.Point3D;

import java.awt.*;
import java.util.List;

public class Axis {

    public Axis(java.util.List<Part> parts, java.util.List<Integer> ib, List<Vertex> vb) {
        Col red = new Col(255, 0, 0);
        Col green = new Col(0, 255, 0);
        Col blue = new Col(0, 0, 255);

        int startIb = ib.size();
        int startVb = vb.size();

        vb.add(new Vertex(new Point3D(0, 0, 0), red));
        vb.add(new Vertex(new Point3D(1, 0, 0), red));
        vb.add(new Vertex(new Point3D(0.75, 0.10, 0), red));
        vb.add(new Vertex(new Point3D(0.75, -0.10, 0), red));

        //line
        ib.add(startVb + 0);
        ib.add(startVb + 1);
        //pointers
        ib.add(startVb + 1);
        ib.add(startVb + 2);
        ib.add(startVb + 1);
        ib.add(startVb + 3);

        vb.add(new Vertex(new Point3D(0, 0, 0), green));
        vb.add(new Vertex(new Point3D(0, 1, 0), green));
        vb.add(new Vertex(new Point3D(0.10, 0.75, 0), green));
        vb.add(new Vertex(new Point3D(-0.10, 0.75, 0), green));

        //line
        ib.add(startVb + 4);
        ib.add(startVb + 5);
        //pointers
        ib.add(startVb + 5);
        ib.add(startVb + 6);
        ib.add(startVb + 5);
        ib.add(startVb + 7);

        vb.add(new Vertex(new Point3D(0, 0, 0), blue));
        vb.add(new Vertex(new Point3D(0, 0, 1), blue));
        vb.add(new Vertex(new Point3D(0.10, 0, 0.75), blue));
        vb.add(new Vertex(new Point3D(-0.10, 0, 0.75), blue));

        //line
        ib.add(startVb + 8);
        ib.add(startVb + 9);
        //pointers
        ib.add(startVb + 9);
        ib.add(startVb + 10);
        ib.add(startVb + 9);
        ib.add(startVb + 11);

        parts.add(new Part(TopologyType.LINES, ModelType.AXIS, startIb, 18));
    }
}
