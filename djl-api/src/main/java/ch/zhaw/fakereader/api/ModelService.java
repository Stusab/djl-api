package ch.zhaw.fakereader.api;

import ch.zhaw.fakereader.Models;               
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

import ai.djl.MalformedModelException;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.modality.Classifications;
import ai.djl.modality.cv.Image;
import ai.djl.inference.Predictor;
import ai.djl.translate.TranslateException;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ZooModel;

import ai.djl.modality.cv.translator.ImageClassificationTranslator;
import ai.djl.modality.cv.transform.Resize;
import ai.djl.modality.cv.transform.ToTensor;
import ai.djl.translate.Batchifier;

import ai.djl.modality.cv.ImageFactory;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class ModelService {

    private Predictor<Image, Classifications> predictor;

    @PostConstruct
    public void init()
        throws IOException,
               TranslateException,
               ModelNotFoundException,
               MalformedModelException {
        // Nur noch "models/plantdetector" relativ zum Arbeitsverzeichnis /app
        Path modelDir = Paths.get("models", "plantdetector");

        // Debug-Ausgabe
        System.out.println("Lade Modell aus: " + modelDir.toAbsolutePath());
        Files.list(modelDir).forEach(p -> System.out.println("  → " + p.getFileName()));

        final int IMAGE_WIDTH  = Models.IMAGE_WIDTH;
        final int IMAGE_HEIGHT = Models.IMAGE_HEIGHT;

        var translator = ImageClassificationTranslator.builder()
            .addTransform(new Resize(IMAGE_WIDTH, IMAGE_HEIGHT))
            .addTransform(new ToTensor())
            .optSynset(List.of("toxic", "nontoxic"))
            .optBatchifier(Batchifier.STACK)
            .build();

        Criteria<Image, Classifications> criteria = Criteria.builder()
            .setTypes(Image.class, Classifications.class)
            .optEngine("MXNet")
            .optModelPath(modelDir)
            .optBlock(Models.buildModel().getBlock())
            .optTranslator(translator)
            .build();

        ZooModel<Image, Classifications> model = criteria.loadModel();
        predictor = model.newPredictor();
    }

    public Classifications classify(byte[] imageBytes) throws IOException, TranslateException {
        var img = ImageFactory.getInstance()
            .fromImage(ImageIO.read(new ByteArrayInputStream(imageBytes)));
        return predictor.predict(img);
    }
}
