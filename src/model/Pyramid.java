package model;

import transforms.Col;
import transforms.Point3D;

import java.util.List;

public class Pyramid {

    public Pyramid (List<Part> parts, List<Integer> ib, List<Vertex> vb, boolean skeleton) {
        Col col1 = new Col(217, 92, 41);
        Col col2 = new Col(57, 4, 95);
        Col col3 = new Col(180, 72, 108);
        Col col4 = new Col(82, 219, 225);
        Col col5 = new Col(12, 75, 110);

        int startIb = ib.size();
        int startVb = vb.size();

        vb.add(new Vertex(new Point3D(1, 1, -1), col1));
        vb.add(new Vertex(new Point3D(3, 1, -1), col1));
        vb.add(new Vertex(new Point3D(3, 3, -1), col1));
        vb.add(new Vertex(new Point3D(1, 3, -1), col1));

        vb.add(new Vertex(new Point3D(1, 1, -1), col2));
        vb.add(new Vertex(new Point3D(3, 1, -1), col2));
        vb.add(new Vertex(new Point3D(2, 2, 3), col2));

        vb.add(new Vertex(new Point3D(3, 1, -1), col3));
        vb.add(new Vertex(new Point3D(3, 3, -1), col3));
        vb.add(new Vertex(new Point3D(2, 2, 3), col3));

        vb.add(new Vertex(new Point3D(3, 3, -1), col4));
        vb.add(new Vertex(new Point3D(1, 3, -1), col4));
        vb.add(new Vertex(new Point3D(2, 2, 3), col4));

        vb.add(new Vertex(new Point3D(1, 3, -1), col5));
        vb.add(new Vertex(new Point3D(1, 1, -1), col5));
        vb.add(new Vertex(new Point3D(2, 2, 3), col2));

        if (skeleton) {
            ib.add(startVb);
            ib.add(startVb + 1);

            ib.add(startVb + 1);
            ib.add(startVb + 2);

            ib.add(startVb + 2);
            ib.add(startVb + 3);

            ib.add(startVb + 3);
            ib.add(startVb);

            startVb += 4;

            for (int i = 0; i < (vb.size() - startVb); i += 3) {
                ib.add(startVb + i);
                ib.add(startVb + i + 1);

                ib.add(startVb + i);
                ib.add(startVb + i + 2);

                ib.add(startVb + i + 1);
                ib.add(startVb + i + 2);
            }

            parts.add(new Part(TopologyType.LINES, ModelType.PYRAMID, startIb, ib.size() - startIb));

        } else {
            ib.add(startVb);
            ib.add(startVb + 1);
            ib.add(startVb + 2);

            ib.add(startVb);
            ib.add(startVb + 2);
            ib.add(startVb + 3);

            startVb += 4;

            for (int i = 0; i < (vb.size() - startVb); i += 3) {
                ib.add(startVb + i);
                ib.add(startVb + i + 1);
                ib.add(startVb + i + 2);
            }

            parts.add(new Part(TopologyType.TRIANGLES, ModelType.PYRAMID, startIb, ib.size() - startIb));
        }
    }
}
