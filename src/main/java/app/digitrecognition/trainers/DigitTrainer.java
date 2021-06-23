package app.digitrecognition.trainers;
import org.datavec.api.io.labels.ParentPathLabelGenerator;
import org.datavec.api.split.FileSplit;
import org.datavec.image.loader.NativeImageLoader;
import org.datavec.image.recordreader.ImageRecordReader;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.inputs.InputType;
import org.deeplearning4j.nn.conf.layers.*;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;
import org.nd4j.linalg.learning.config.Nesterovs;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.nd4j.linalg.schedule.MapSchedule;
import org.nd4j.linalg.schedule.ScheduleType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static app.digitrecognition.Parameters.*;

public class DigitTrainer {
    private static final Logger LOGGER = LoggerFactory.getLogger(DigitTrainer.class);
    private static final String BASE_PATH = "/home/roger";
    private static final int height = 28, width = 28, channels = 1;

    public static MultiLayerNetwork buildModel() {
        int seed = 1234;
        Map<Integer, Double> learningRateSchedule = new HashMap<>();
        learningRateSchedule.put(0, 0.06);
        learningRateSchedule.put(200, 0.05);
        learningRateSchedule.put(600, 0.028);
        learningRateSchedule.put(800, 0.0060);
        learningRateSchedule.put(1000, 0.001);
        NeuralNetConfiguration.ListBuilder b = new NeuralNetConfiguration.Builder()
                .seed(seed)
                .l2(0.0005) // ridge regression value
                .updater(new Nesterovs(new MapSchedule(ScheduleType.ITERATION, learningRateSchedule)))
                .weightInit(WeightInit.XAVIER)
                .list();
        for (int i = 0; i < nLayers; i++) {
            switch (layerType[i]) {
                case "Input" -> {
                    Activation a;
                    switch(layerProp[i]) {
                        case "RELU" -> a = Activation.RELU;
                        case "IDENTITY" -> a = Activation.IDENTITY;
                        case "SIGMOID" -> a = Activation.SIGMOID;
                        case "TANH" -> a=Activation.TANH;
                        default -> throw new IllegalStateException("Unexpected value: " + layerProp[i]);
                    }
                    b.layer(i, new ConvolutionLayer.Builder(layerFilterSize[i], layerFilterSize[i])
                            .nIn(1)
                            .stride(layerHStride[i], layerVStride[i])
                            .activation(a)
                            .nOut(nFilters[i]).build());
                }
                case "Convolution" -> {
                    Activation a;
                    switch(layerProp[i]) {
                        case "RELU" -> a = Activation.RELU;
                        case "IDENTITY" -> a = Activation.IDENTITY;
                        case "SIGMOID" -> a = Activation.SIGMOID;
                        case "TANH" -> a=Activation.TANH;
                        default -> throw new IllegalStateException("Unexpected value: " + layerProp[i]);
                    }
                    b.layer(i, new ConvolutionLayer.Builder(layerFilterSize[i], layerFilterSize[i])
                            .stride(layerHStride[i], layerVStride[i])
                            .activation(a)
                            .nOut(nFilters[i]).build());
                }
                case "Pooling" -> {
                    SubsamplingLayer.PoolingType p;
                    switch (layerProp[i]) {
                        case "AVERAGE" -> p = SubsamplingLayer.PoolingType.AVG;
                        case "MAX" -> p = SubsamplingLayer.PoolingType.MAX;
                        default -> throw new IllegalStateException("Unexpected value: " + layerProp[i]);
                    }
                    b.layer(i, new SubsamplingLayer.Builder(p)
                            .kernelSize(layerFilterSize[i], layerFilterSize[i])
                            .stride(layerHStride[i], layerVStride[i])
                            .build());
                }
                case "Dense" -> {
                    Activation a;
                    switch(layerProp[i]) {
                        case "RELU" -> a = Activation.RELU;
                        case "IDENTITY" -> a = Activation.IDENTITY;
                        case "SIGMOID" -> a = Activation.SIGMOID;
                        case "TANH" -> a=Activation.TANH;
                        default -> throw new IllegalStateException("Unexpected value: " + layerProp[i]);
                    }
                    b.layer(i, new DenseLayer.Builder()
                            .activation(a)
                            .nOut(nFilters[i]).build());
                }
                case "Output" -> {
                    Activation a;
                    LossFunctions.LossFunction l;
                    switch(layerProp[i]) {
                        case "RELU" -> a = Activation.RELU;
                        case "IDENTITY" -> a = Activation.IDENTITY;
                        case "SIGMOID" -> a = Activation.SIGMOID;
                        case "TANH" -> a=Activation.TANH;
                        case "SOFTMAX" -> a=Activation.SOFTMAX;
                        default -> throw new IllegalStateException("Unexpected value: " + layerProp[i]);
                    }
                    switch(lossType) {
                        case "MSE" -> l = LossFunctions.LossFunction.MSE;
                        case "MAE" -> l = LossFunctions.LossFunction.MEAN_ABSOLUTE_ERROR;
                        case "NEGATIVELOGLIKELIHOOD" -> l = LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD;
                        default -> throw new IllegalStateException("Unexpected value: " + lossType);
                    }
                    b.layer(i, new OutputLayer.Builder(l)
                            .nOut(10)
                            .activation(a)
                            .build());
                }
            }
        }
        b.setInputType(InputType.convolutionalFlat(height, width, channels)); // InputType.convolutional for normal image
        MultiLayerConfiguration conf = b.build();
        return new MultiLayerNetwork(conf);
    }

    public static MultiLayerNetwork buildModel1() {
        int seed = 1234;
        Map<Integer, Double> learningRateSchedule = new HashMap<>();
        learningRateSchedule.put(0, 0.06);
        learningRateSchedule.put(200, 0.05);
        learningRateSchedule.put(600, 0.028);
        learningRateSchedule.put(800, 0.0060);
        learningRateSchedule.put(1000, 0.001);

        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                .seed(seed)
                .l2(0.0005) // ridge regression value
                .updater(new Nesterovs(new MapSchedule(ScheduleType.ITERATION, learningRateSchedule)))
                .weightInit(WeightInit.XAVIER)
                .list()
                .layer(new ConvolutionLayer.Builder(5, 5)
                        .nIn(channels)
                        .stride(1, 1)
                        .nOut(20)
                        .activation(Activation.IDENTITY)
                        .build())
                .layer(new SubsamplingLayer.Builder(SubsamplingLayer.PoolingType.MAX)
                        .kernelSize(2, 2)
                        .stride(2, 2)
                        .build())
                .layer(new ConvolutionLayer.Builder(5, 5)
                        .stride(1, 1) // nIn need not specified in later layers
                        .nOut(50)
                        .activation(Activation.IDENTITY)
                        .build())
                .layer(new SubsamplingLayer.Builder(SubsamplingLayer.PoolingType.MAX)
                        .kernelSize(2, 2)
                        .stride(2, 2)
                        .build())
                .layer(new DenseLayer.Builder().activation(Activation.RELU)
                        .nOut(500)
                        .build())
                .layer(new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                        .nOut(10)
                        .activation(Activation.SOFTMAX)
                        .build())
                .setInputType(InputType.convolutionalFlat(height, width, channels)) // InputType.convolutional for normal image
                .build();
        return new MultiLayerNetwork(conf);
    }

    public static void train(MultiLayerNetwork net) throws IOException {
        int seed = 1234;
        Random randNumGen = new Random(seed);
        LOGGER.info("Data vectorization...");

        // vectorization of train data
        File trainData = new File(BASE_PATH + "/mnist_png/training");
        FileSplit trainSplit = new FileSplit(trainData, NativeImageLoader.ALLOWED_FORMATS, randNumGen);
        ParentPathLabelGenerator labelMaker = new ParentPathLabelGenerator(); // use parent directory name as the image label
        ImageRecordReader trainRR = new ImageRecordReader(height, width, channels, labelMaker);
        trainRR.initialize(trainSplit);
        DataSetIterator trainIter = new RecordReaderDataSetIterator(trainRR, batchSize, 1, 10);

        // pixel values from 0-255 to 0-1 (min-max scaling)
        DataNormalization imageScaler = new ImagePreProcessingScaler();
        imageScaler.fit(trainIter);
        trainIter.setPreProcessor(imageScaler);

        // vectorization of test data
        File testData = new File(BASE_PATH + "/mnist_png/testing");
        FileSplit testSplit = new FileSplit(testData, NativeImageLoader.ALLOWED_FORMATS, randNumGen);
        ImageRecordReader testRR = new ImageRecordReader(height, width, channels, labelMaker);
        testRR.initialize(testSplit);
        DataSetIterator testIter = new RecordReaderDataSetIterator(testRR, batchSize, 1, 10);
        testIter.setPreProcessor(imageScaler); // same normalization for better results

        LOGGER.info("Network configuration and training...");
        // reduce the learning rate as the number of training epochs increases
        // iteration #, learning rate

        net.init();
        net.setListeners(new ScoreIterationListener(10));
        LOGGER.info("Total num of params: {}", net.numParams());

        // evaluation while training (the score should go down)
        for (int i = 0; i < nEpochs; i++) {
            net.fit(trainIter);
            LOGGER.info("Completed epoch {}", i);
            Evaluation eval = net.evaluate(testIter);
            LOGGER.info(eval.stats());

            trainIter.reset();
            testIter.reset();
        }

        File ministModelPath = new File("/home/roger/check.zip");
        ModelSerializer.writeModel(net, ministModelPath, true);
        LOGGER.info("The MINIST model has been saved in {}", ministModelPath.getPath());
    }
}