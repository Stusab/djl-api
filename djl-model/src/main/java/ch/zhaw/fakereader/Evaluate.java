package ch.zhaw.fakereader;

import ai.djl.MalformedModelException;                            
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.modality.Classifications;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.transform.Resize;
import ai.djl.modality.cv.transform.ToTensor;
import ai.djl.translate.TranslateException;
import ai.djl.translate.Batchifier;
import ai.djl.modality.cv.translator.ImageClassificationTranslator;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.inference.Predictor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Evaluate {

     public static void main(String[] args) 
     throws IOException, 
            TranslateException,
            ai.djl.repository.zoo.ModelNotFoundException,  
            MalformedModelException  {
        // 1) Pfade prüfen
        Path modelDir = Path.of("models", "plantdetector");
        Path validDir = Path.of("dataset", "valid");
        if (!Files.isDirectory(modelDir) || !Files.isDirectory(validDir)) {
            System.err.println("models/plantdetector oder dataset/valid nicht gefunden.");
            System.exit(1);
        }

        // 2) Synset-Liste
        List<String> synset = List.of("toxic", "nontoxic");

        // 3) Translator mit neuem Builder
        var translator = ImageClassificationTranslator.builder()
            .addTransform(new Resize(Models.IMAGE_WIDTH, Models.IMAGE_HEIGHT))
            .addTransform(new ToTensor())
            .optSynset(synset)
            .optBatchifier(Batchifier.STACK)
            .build();

        // 4) Modell laden
        Criteria<Image, Classifications> criteria = Criteria.builder()
            .setTypes(Image.class, Classifications.class)
            .optModelPath(modelDir)
            .optTranslator(translator)
            .build();

        try (
            ZooModel<Image, Classifications> zooModel = criteria.loadModel();
            Predictor<Image, Classifications> predictor = zooModel.newPredictor()
        ) {
            // 5) Confusion-Matrix initialisieren
            Map<String, Map<String, Integer>> confusion = new TreeMap<>();
            for (String actual : synset) {
                confusion.put(actual, new TreeMap<>());
                for (String pred : synset) {
                    confusion.get(actual).put(pred, 0);
                }
            }

            int correct = 0, total = 0;

            // 6) Über alle Validierungsbilder iterieren
            for (Path classDir : Files.list(validDir).filter(Files::isDirectory).toList()) {
                String actual = classDir.getFileName().toString();
                for (Path imgPath : Files.list(classDir).filter(Files::isRegularFile).toList()) {
                    Image img = ImageFactory.getInstance().fromFile(imgPath);
                    Classifications result = predictor.predict(img);
                    String predicted = result.best().getClassName();
                    confusion.get(actual).compute(predicted, (k, v) -> v + 1);
                    if (predicted.equals(actual)) {
                        correct++;
                    }
                    total++;
                }
            }

            // 7) Ergebnisse ausgeben
            double accuracy = 100.0 * correct / total;
            System.out.printf("Validation Accuracy: %.2f%%%n", accuracy);
            System.out.println("\nConfusion Matrix:");
            System.out.print("            ");
            synset.forEach(lbl -> System.out.printf("%12s", lbl));
            System.out.println();
            for (String actual : synset) {
                System.out.printf("%12s", actual);
                for (String pred : synset) {
                    System.out.printf("%12d", confusion.get(actual).get(pred));
                }
                System.out.println();
            }
        }
    }
}
