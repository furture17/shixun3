package sx5.controller;

import org.springframework.boot.system.ApplicationHome;
import org.springframework.web.bind.annotation.*;
import sx5.model.domain.request.AlgRequest;
import sx5.service.AlgService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/alg")
public class AlgController {

    @Resource
    private AlgService algService;

    @PostMapping("/logPredict")
//    public List<Map<String, Double>> logPredict(@RequestBody AlgRequest algRequest) {
    public ArrayList<ArrayList<Double>> logPredict(@RequestBody AlgRequest algRequest) {
        String Path =  new ApplicationHome(this.getClass()).getDir().getParentFile()
                .getParentFile().getAbsolutePath() + "\\src\\main\\resources\\data\\";

        String trainDataSetPath = Path.concat(algRequest.getTrainDataSetPath()).concat(".csv");
        String testDataSetPath = Path.concat(algRequest.getTestDataSetPath()).concat(".csv");

        return algService.logPredict(trainDataSetPath, testDataSetPath);
    }

//    @PostMapping("/svmPredict")
//    public List<Map<String, Double>> svmPredict(@RequestBody AlgRequest algRequest) {
//        String Path =  new ApplicationHome(this.getClass()).getDir().getParentFile()
//                .getParentFile().getAbsolutePath() + "\\src\\main\\resources\\data\\";
//
//        String trainDataSetPath = Path.concat(algRequest.getTrainDataSetPath());
//        String testDataSetPath = Path.concat(algRequest.getTestDataSetPath());
//
//        return algService.svmPredict(trainDataSetPath, testDataSetPath);
//    }

    @PostMapping("/svmPredict")
    public Map<String, ArrayList<ArrayList<Double>>> svmPredict(@RequestBody AlgRequest algRequest) {
        String Path =  new ApplicationHome(this.getClass()).getDir().getParentFile()
                .getParentFile().getAbsolutePath() + "\\src\\main\\resources\\data\\";

//        String trainDataSetPath = Path.concat(algRequest.getTrainDataSetPath());
//        String testDataSetPath = Path.concat(algRequest.getTestDataSetPath());


        String trainDataSetPath = Path.concat(algRequest.getTrainDataSetPath()).concat(".csv");
        String testDataSetPath = Path.concat(algRequest.getTestDataSetPath()).concat(".csv");

//
//        String trainDataSetPath = Path.concat(algRequest.getTrainDataSetPath());
//        String testDataSetPath = Path.concat(algRequest.getTestDataSetPath());

        return algService.svmPredict(trainDataSetPath, testDataSetPath);
    }

//    @PostMapping("/predict")
//    public Map<String, ArrayList<ArrayList<Double>>> svmPredict(@RequestBody AlgRequest algRequest) {
//        String Path =  new ApplicationHome(this.getClass()).getDir().getParentFile()
//                .getParentFile().getAbsolutePath() + "\\src\\main\\resources\\data\\";
//
//        String trainPath = algRequest.getTrainDataSetPath();
//        String testPath = algRequest.getTestDataSetPath();
//        String[] trainDataSetPath = trainPath.split(trainPath);
//        String[] testDataSetPath= trainPath.split(testPath);
//
//        System.out.println(trainDataSetPath);
//        System.out.println(testDataSetPath);
//
//        for (String s : trainDataSetPath) {
//            s = Path.concat(s);
//        }
//        for (String s : testDataSetPath) {
//            s = Path.concat(s);
//        }
//
////
////        String trainDataSetPath = Path.concat(algRequest.getTrainDataSetPath());
////        String testDataSetPath = Path.concat(algRequest.getTestDataSetPath());
//
//        return algService.svmPredict(trainDataSetPath, testDataSetPath);
//    }

}
