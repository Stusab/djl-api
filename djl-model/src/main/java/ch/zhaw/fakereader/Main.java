// src/main/java/ch/zhhaw/fakereader/Main.java
package ch.zhaw.fakereader;

import ai.djl.Model;
import ai.djl.nn.Parameter;
import ai.djl.ndarray.types.Shape;
import ai.djl.training.Trainer;
import ai.djl.training.dataset.RandomAccessDataset;
import ai.djl.training.DefaultTrainingConfig;
import ai.djl.training.EasyTrain;
import ai.djl.training.loss.Loss;
import ai.djl.training.optimizer.Optimizer;
import ai.djl.training.tracker.Tracker;
import ai.djl.training.initializer.XavierInitializer;
import ai.djl.training.evaluator.Accuracy;
import ai.djl.training.listener.TrainingListener;
import ai.djl.training.listener.SaveModelTrainingListener;
import ai.djl.metric.Metrics;
import ai.djl.translate.TranslateException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {

     public static void main(String[] args) throws IOException, TranslateException {
        if (args.length < 3) {
            System.err.println("Usage: java -jar djl-model-1.0-shaded.jar <trainDir> <validDir> <epochs>");
            System.exit(1);
        }
        String trainDir = args[0];
        String validDir = args[1];
        int epochs     = Integer.parseInt(args[2]);

        // Check Verzeichnisse
        for (String d : new String[]{trainDir, validDir}) {
            if (!Files.isDirectory(Path.of(d))) {
                System.err.printf("Directory not found: %s%n", d);
                System.exit(2);
            }
        }
        // models/ anlegen
        Path mp = Path.of("models");
        if (!Files.exists(mp)) {
            Files.createDirectory(mp);
        }

        RandomAccessDataset trainDataset = Models.getDataset(trainDir);
        RandomAccessDataset validDataset = Models.getDataset(validDir);

        Model model = Models.buildModel();

        SaveModelTrainingListener saver =
                new SaveModelTrainingListener("models", "plantdetector");

        DefaultTrainingConfig config = new DefaultTrainingConfig(Loss.softmaxCrossEntropyLoss())
            .optOptimizer(Optimizer.adam().optLearningRateTracker(Tracker.fixed(0.001f)).build())
            .optInitializer(new XavierInitializer(), Parameter.Type.WEIGHT)
            .addEvaluator(new Accuracy())
            .addTrainingListeners(TrainingListener.Defaults.logging());
        config.addTrainingListeners(saver);

        try (Trainer trainer = model.newTrainer(config)) {
            trainer.setMetrics(new Metrics());
            trainer.initialize(
                new Shape(Models.BATCH_SIZE, Models.CHANNELS, Models.IMAGE_HEIGHT, Models.IMAGE_WIDTH)
            );
            System.out.printf("Starte Training für %d Epochen…%n", epochs);
            EasyTrain.fit(trainer, epochs, trainDataset, validDataset);
            System.out.println("Training beendet. Beste Checkpoints in ./models");
        }
    }
}
