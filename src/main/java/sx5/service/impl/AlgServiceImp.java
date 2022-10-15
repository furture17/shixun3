package sx5.service.impl;

import org.springframework.stereotype.Service;
import sx5.alg.LogisticRegression;
import sx5.service.AlgService;

import java.io.IOException;

@Service
public class AlgServiceImp implements AlgService {

    @Override
    public void makePredict(String trainPath, String testPath) {
        try {
            LogisticRegression.makePredict(trainPath, testPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
