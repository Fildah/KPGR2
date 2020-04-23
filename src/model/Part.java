package model;

public class Part {
    private TopologyType type;
    private ModelType modelType;
    private int index;
    private int count;

    public Part(TopologyType type, ModelType modelType, int index, int count) {
        this.type = type;
        this.modelType = modelType;
        this.index = index;
        this.count = count;
    }

    public TopologyType getType() {
        return type;
    }

    public void setType(TopologyType type) {
        this.type = type;
    }

    public ModelType getModelType() {
        return modelType;
    }

    public void setModelType(ModelType modelType) {
        this.modelType = modelType;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
