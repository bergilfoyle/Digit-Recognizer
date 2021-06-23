package app.digitrecognition;

public class Parameters {
    public int maxLayers, nLayers, batchSize, nEpochs;
    public String[] layerType;
    public int[] layerHStride, layerVStride, layerFilterSize, nFilters;
    Parameters() {
        this.maxLayers = 20;
        this.layerType = new String[maxLayers];
        this.layerHStride = new int[maxLayers];
        this.layerVStride = new int[maxLayers];
        this.layerFilterSize = new int[maxLayers];
        this.nFilters = new int[maxLayers];
    }
}
