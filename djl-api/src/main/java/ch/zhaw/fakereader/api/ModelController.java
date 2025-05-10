package ch.zhaw.fakereader.api;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import ai.djl.modality.Classifications;
import ai.djl.translate.TranslateException;
import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ModelController {

    private final ModelService modelService;

    public ModelController(ModelService modelService) {
        this.modelService = modelService;
    }

    @PostMapping(path = "/analyze", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, 
    produces = MediaType.APPLICATION_JSON_VALUE)
    public String analyze(@RequestParam("image") MultipartFile file)
    throws IOException, TranslateException {
    Classifications result = modelService.classify(file.getBytes());
    return result.toJson();  // gibt bereits einen JSON-String zurück
    }


}
