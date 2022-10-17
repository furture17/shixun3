package sx5.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface AlgService {
//     List<Map<String, Double>> logPredict(String trainDataSetPath, String testDataSetPath);
     ArrayList<ArrayList<Double>> logPredict(Long id, String trainDataSetPath, String testDataSetPath, Double lr, Double lambda, int epochNum);
     Map<String, ArrayList<ArrayList<Double>>> KNNPredict(Long id, String trainDataSetPath, String testDataSetPath, int elect_num, int neighbor);
//     List<Map<String, Double>> svmPredict(String trainDataSetPath, String testDataSetPath);
}
