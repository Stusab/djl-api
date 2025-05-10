// src/main/java/ch/zhaw/fakereader/Models.java
package ch.zhaw.fakereader;

import ai.djl.Model;
import ai.djl.nn.Block;
import ai.djl.basicmodelzoo.cv.classification.ResNetV1;
import ai.djl.ndarray.types.Shape;
import ai.djl.basicdataset.cv.classification.ImageFolder;
import ai.djl.training.dataset.RandomAccessDataset;
import ai.djl.translate.Pipeline;
import ai.djl.modality.cv.transform.Resize;
import ai.djl.modality.cv.transform.ToTensor;
import ai.djl.translate.TranslateException;

import java.io.IOException;
import java.nio.file.Paths;

public class Models {

    public static final int IMAGE_HEIGHT = 224;
    public static final int IMAGE_WIDTH  = 224;
    public static final int CHANNELS     = 3;
    public static final int NUM_CLASSES  = 5;
    public static final int BATCH_SIZE   = 32;

    /**
     * Lädt Bilder aus 'dir', resized sie auf 224×224 und wandelt
     * sie in Tensoren um, damit alle Batches dieselbe Form haben.
     */
    public static RandomAccessDataset getDataset(String dir)
            throws IOException, TranslateException {
        Pipeline pipeline = new Pipeline()
                .add(new Resize(IMAGE_WIDTH, IMAGE_HEIGHT))
                .add(new ToTensor());

        return ImageFolder.builder()
                .setRepositoryPath(Paths.get(dir))
                .optPipeline(pipeline)
                .setSampling(BATCH_SIZE, true)
                .build();
    }

    /**
     * Baut ein ResNet-18 Modell mit fester Input-Grösse 3×224×224.
     */
    public static Model buildModel() {
        Model model = Model.newInstance("plant-detector");
        Block block = ResNetV1.builder()
                .setImageShape(new Shape(CHANNELS, IMAGE_HEIGHT, IMAGE_WIDTH))
                .setNumLayers(18)
                .setOutSize(NUM_CLASSES)
                .build();
        model.setBlock(block);
        return model;
    }
}
