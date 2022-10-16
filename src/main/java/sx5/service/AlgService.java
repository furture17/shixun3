package sx5.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface AlgService {
//     List<Map<String, Double>> logPredict(String trainDataSetPath, String testDataSetPath);
     ArrayList<ArrayList<Double>> logPredict(String trainDataSetPath, String testDataSetPath);
     Map<String, ArrayList<ArrayList<Double>>> svmPredict(String trainDataSetPath, String testDataSetPath);
//     List<Map<String, Double>> svmPredict(String trainDataSetPath, String testDataSetPath);
}
