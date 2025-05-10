package ch.zhaw.fakereader;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class InspectEncoding {

    public static void main(String[] args) throws IOException {
        Path paramsFile = Paths.get("models", "plant-detector", "plant-detector-0025.params");
        try (DataInputStream is = new DataInputStream(Files.newInputStream(paramsFile))) {
            // 1) Magic-Header (ASCII "DJL\0") überspringen
            is.readInt();
            // 2) tatsächliche Encoding-Version lesen
            int encodingVersion = is.readInt();
            System.out.println("DJL Encoding Version: " + encodingVersion);
        }
    }
}
