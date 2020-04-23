package renderer;

import model.Part;
import model.Vertex;
import transforms.Mat4;

import java.util.List;

public interface GPURenderer {

    void clear();

    void draw(List<Part> parts, List<Vertex> vb, List<Integer> ib);

    void setModel(Mat4 model);

    void setMoveModel(Mat4 moveModel);

    void setView(Mat4 view);

    void setProjection(Mat4 projection);

}
