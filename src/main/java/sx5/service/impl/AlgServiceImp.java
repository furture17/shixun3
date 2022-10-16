package sx5.service.impl;

import org.springframework.stereotype.Service;
import sx5.alg.LogisticRegression;
import sx5.alg.SVM;
import sx5.alg.SVM2;
import sx5.service.AlgService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AlgServiceImp implements AlgService {

    @Override
    public ArrayList<ArrayList<Double>> logPredict(String trainPath, String testPath) {
        try {
            return LogisticRegression.makePredict(trainPath, testPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public Map<String, ArrayList<ArrayList<Double>>> svmPredict(String trainPath, String testPath) {
        return SVM2.makePredict(trainPath, testPath);
    }

//    @Override
//    public List<Map<String, Double>> svmPredict(String trainPath, String testPath) {
//            return SVM.makePredict(trainPath, testPath);
//    }
}
