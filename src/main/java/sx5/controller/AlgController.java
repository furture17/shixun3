package sx5.controller;

import org.springframework.web.bind.annotation.*;
import sx5.model.domain.request.AlgRequest;
import sx5.service.AlgService;

import javax.annotation.Resource;

@RestController
@CrossOrigin
@RequestMapping("/alg")
public class AlgController {

    @Resource
    private AlgService algService;

    @PostMapping("/predict")
    public void predict(@RequestBody AlgRequest algRequest) {
        String trainDataSetPath = algRequest.getTrainDataSetPath();
        String testDataSetPath = algRequest.getTestDataSetPath();

        algService.makePredict(trainDataSetPath, testDataSetPath);
        //TODO return ???
    }

}
