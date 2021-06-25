package app.digitrecognition;

public class Parameters {
    static public int maxLayers = 20;
    static public int nLayers, batchSize = 100, nEpochs = 1;
    static public String[] layerType = new String[maxLayers];
    static public String[] layerProp = new String[maxLayers];
    static public String lossType;
    static public int[] layerHStride = new int[maxLayers];
    static public int[] layerVStride = new int[maxLayers];
    static public int[] layerFilterSize = new int[maxLayers];
    static public int[] nFilters = new int[maxLayers];
}
