package model;

import transforms.Col;
import transforms.Cubic;
import transforms.Point3D;

import java.util.List;

public class Wave {

    public Wave(List<Part> parts, List<Integer> ib, List<Vertex> vb, boolean skeleton) {
        Col col1 = new Col(17, 235, 139);
        Col col2 = new Col(0, 137, 214);
        Col col3 = new Col(188, 42, 69);
        Col col4 = new Col(140, 145, 4);
        Col col5 = new Col(56, 23, 184);

        int startIb = ib.size();
        int startVb = vb.size();

        vb.add(new Vertex(new Point3D(2,2,-2), col2));
        vb.add(new Vertex(new Point3D(2,2,0), col2));
        vb.add(new Vertex(new Point3D(2,4,0), col2));
        vb.add(new Vertex(new Point3D(2,4,-2), col2));

        vb.add(new Vertex(new Point3D(2,2,-2), col3));
        vb.add(new Vertex(new Point3D(4,2,-2), col3));
        vb.add(new Vertex(new Point3D(4,4,-2), col3));
        vb.add(new Vertex(new Point3D(2,4,-2), col3));

        if (skeleton) {
            ib.add(startVb);
            ib.add(startVb + 1);
            ib.add(startVb + 1);
            ib.add(startVb + 2);
            ib.add(startVb + 2);
            ib.add(startVb + 3);
            ib.add(startVb + 3);
            ib.add(startVb);

            ib.add(startVb + 4);
            ib.add(startVb + 5);
            ib.add(startVb + 5);
            ib.add(startVb + 6);
            ib.add(startVb + 6);
            ib.add(startVb + 7);
            ib.add(startVb + 7);
            ib.add(startVb + 4);
        } else {
            ib.add(startVb);
            ib.add(startVb + 1);
            ib.add(startVb + 2);
            ib.add(startVb);
            ib.add(startVb + 2);
            ib.add(startVb + 3);


            ib.add(startVb + 4);
            ib.add(startVb + 5);
            ib.add(startVb + 6);
            ib.add(startVb + 4);
            ib.add(startVb + 6);
            ib.add(startVb + 7);
        }

        int orig1 = vb.size();
        vb.add(new Vertex(new Point3D(2,2,-2), col4));

        int orig2 = vb.size();
        vb.add(new Vertex(new Point3D(2,4,-2), col5));

        Cubic curve1 = new Cubic(Cubic.BEZIER, new Point3D(2,2,0), new Point3D(4,3,1), new Point3D(5,1, -1), new Point3D(4,2,-2));
        Cubic curve2 = new Cubic(Cubic.BEZIER, new Point3D(2,4,0), new Point3D(4,5,1), new Point3D(5,3, -1), new Point3D(4,4,-2));

        for (int i = 0; i <= 10; i++) {
            startVb = vb.size();
            float temp = (float)i/10;
            vb.add(new Vertex(curve1.compute(temp), col1));
            vb.add(new Vertex(curve2.compute(temp), col1));

            vb.add(new Vertex(curve1.compute(temp), col4));
            vb.add(new Vertex(curve2.compute(temp), col5));

            if (skeleton) {
                if (i == 0) {
                    ib.add(startVb);
                    ib.add(startVb + 1);

                    ib.add(orig1);
                    ib.add(startVb + 2);
                    ib.add(orig2);
                    ib.add(startVb + 3);
                } else {
                    ib.add(startVb - 4);
                    ib.add(startVb + 1);

                    ib.add(startVb - 4);
                    ib.add(startVb);

                    ib.add(startVb - 3);
                    ib.add(startVb + 1);

                    ib.add(startVb);
                    ib.add(startVb + 1);


                    ib.add(startVb - 2);
                    ib.add(startVb + 2);

                    ib.add(orig1);
                    ib.add(startVb + 2);

                    ib.add(startVb - 1);
                    ib.add(startVb + 3);

                    ib.add(orig2);
                    ib.add(startVb + 3);
                }
            } else {
                if (i != 0) {
                    ib.add(startVb - 4);
                    ib.add(startVb - 3);
                    ib.add(startVb + 1);

                    ib.add(startVb - 4);
                    ib.add(startVb);
                    ib.add(startVb + 1);


                    ib.add(orig1);
                    ib.add(startVb - 2);
                    ib.add(startVb + 2);

                    ib.add(orig2);
                    ib.add(startVb - 1);
                    ib.add(startVb + 3);
                }
            }
        }
        if (skeleton) {
            parts.add(new Part(TopologyType.LINES, ModelType.WAVE, startIb, ib.size() - startIb));
        } else {
            parts.add(new Part(TopologyType.TRIANGLES, ModelType.WAVE, startIb, ib.size() - startIb));
        }
    }
}
