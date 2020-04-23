package model;

import transforms.Col;
import transforms.Point3D;

import java.util.List;

public class Cube {

    public Cube(List<Part> parts, List<Integer> ib, List<Vertex> vb, boolean skeleton) {
        Col col1 = new Col(162, 223, 84);
        Col col2 = new Col(16, 120, 124);
        Col col3 = new Col(222, 250, 126);
        Col col4 = new Col(233, 102, 215);
        Col col5 = new Col(97, 38, 204);
        Col col6 = new Col(144, 102, 52);

        int startIb = ib.size();
        int startVb = vb.size();

        vb.add(new Vertex(new Point3D(3, 3, -1), col1));
        vb.add(new Vertex(new Point3D(5, 3, -1), col1));
        vb.add(new Vertex(new Point3D(5, 3, 1), col1));
        vb.add(new Vertex(new Point3D(3, 3, 1), col1));

        vb.add(new Vertex(new Point3D(3, 3, -1), col2));
        vb.add(new Vertex(new Point3D(5, 3, -1), col2));
        vb.add(new Vertex(new Point3D(5, 5, -1), col2));
        vb.add(new Vertex(new Point3D(3, 5, -1), col2));

        vb.add(new Vertex(new Point3D(5, 3, -1), col3));
        vb.add(new Vertex(new Point3D(5, 3, 1), col3));
        vb.add(new Vertex(new Point3D(5, 5, 1), col3));
        vb.add(new Vertex(new Point3D(5, 5, -1), col3));

        vb.add(new Vertex(new Point3D(5, 3, 1), col4));
        vb.add(new Vertex(new Point3D(3, 3, 1), col4));
        vb.add(new Vertex(new Point3D(3, 5, 1), col4));
        vb.add(new Vertex(new Point3D(5, 5, 1), col4));

        vb.add(new Vertex(new Point3D(3, 3, -1), col5));
        vb.add(new Vertex(new Point3D(3, 3, 1), col5));
        vb.add(new Vertex(new Point3D(3, 5, 1), col5));
        vb.add(new Vertex(new Point3D(3, 5, -1), col5));

        vb.add(new Vertex(new Point3D(3, 5, -1), col6));
        vb.add(new Vertex(new Point3D(5, 5, -1), col6));
        vb.add(new Vertex(new Point3D(5, 5, 1), col6));
        vb.add(new Vertex(new Point3D(3, 5, 1), col6));

        if (skeleton) {
            for (int i = 0; i < (vb.size() - startVb); i += 4) {
                ib.add(startVb + i);
                ib.add(startVb + i + 1);

                ib.add(startVb + i + 1);
                ib.add(startVb + i + 2);

                ib.add(startVb + i + 2);
                ib.add(startVb + i + 3);

                ib.add(startVb + i + 3);
                ib.add(startVb + i);
            }

            parts.add(new Part(TopologyType.LINES, ModelType.CUBE, startIb, ib.size() - startIb));

        } else {
            for (int i = 0; i < (vb.size() - startVb); i += 4) {
                ib.add(startVb + i);
                ib.add(startVb + i + 1);
                ib.add(startVb + i + 2);

                ib.add(startVb + i);
                ib.add(startVb + i + 3);
                ib.add(startVb + i + 2);
            }

            parts.add(new Part(TopologyType.TRIANGLES, ModelType.CUBE, startIb, ib.size() - startIb));
        }
    }
}
