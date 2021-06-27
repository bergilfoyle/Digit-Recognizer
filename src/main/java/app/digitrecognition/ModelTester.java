package app.digitrecognition;

import org.datavec.api.io.labels.ParentPathLabelGenerator;
import org.datavec.api.split.FileSplit;
import org.datavec.image.loader.NativeImageLoader;
import org.datavec.image.recordreader.ImageRecordReader;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import static app.digitrecognition.Parameters.batchSize;

public class ModelTester {
    private static final String BASE_PATH = "/home/roger";
    private static final int height = 28, width = 28, channels = 1;

    private static Evaluation eval;
    private static MultiLayerNetwork model;
    private static DataNormalization imageScaler;

    public static void setScaler(DataNormalization imageScaler) { ModelTester.imageScaler = imageScaler; }

    public static void setModel(MultiLayerNetwork model) { ModelTester.model = model; }

    public static Evaluation getEvalualtion() {
        return eval;
    }

    public static void testModel() throws IOException {
        int seed = 1234;
        Random randNumGen = new Random(seed);
        ParentPathLabelGenerator labelMaker = new ParentPathLabelGenerator();

        File testData = new File(BASE_PATH + "/mnist_png/testing");
        FileSplit testSplit = new FileSplit(testData, NativeImageLoader.ALLOWED_FORMATS, randNumGen);
        ImageRecordReader testRR = new ImageRecordReader(height, width, channels, labelMaker);
        testRR.initialize(testSplit);
        DataSetIterator testIter = new RecordReaderDataSetIterator(testRR, batchSize, 1, 10);
        ModelTrainer.setScaler();
        setScaler(ModelTrainer.getScaler());
        testIter.setPreProcessor(imageScaler);
        eval = model.evaluate(testIter);
    }
}
