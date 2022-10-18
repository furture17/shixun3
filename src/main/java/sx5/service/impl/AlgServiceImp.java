package sx5.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import sx5.alg.BYS;
import sx5.alg.DT;
import sx5.alg.LogisticRegression;
import sx5.alg.KNN;
import sx5.mapper.ModelMapper;
import sx5.model.domain.Model;
import sx5.service.AlgService;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

@Service
public class AlgServiceImp implements AlgService {

    @Resource
    private ModelMapper modelMapper;

    @Override
    public ArrayList<ArrayList<Double>> logPredict(Long id, String trainPath, String testPath, Double lr, Double lambda, int epochNum) {
        try {
            LogisticRegression logisticRegression = new LogisticRegression();
            ArrayList<ArrayList<Double>> arrayList = logisticRegression.makePredict(id, trainPath, testPath, lr, lambda, epochNum);

            ArrayList<Double> arrayList1 = arrayList.get(arrayList.size() - 1);
            double acc = arrayList1.get(0);

            //存储准确率
            QueryWrapper<Model> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", id);
            Model model = modelMapper.selectOne(queryWrapper);
            model.setAccuracy(acc);
            modelMapper.updateById(model);


            arrayList.remove(arrayList.size() - 1);//TODO:验证最后一个是否还有
            System.out.println(arrayList);

            return arrayList;
//            return logisticRegression.makePredict(id, trainPath, testPath, lr, lambda, epochNum);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public Map<String, ArrayList<ArrayList<Double>>> KNNPredict(Long id, String trainPath, String testPath, int elect_num, int neighbor) {
        Map<String, ArrayList<ArrayList<Double>>> map = KNN.makePredict(trainPath, testPath, elect_num, neighbor);
        ArrayList<ArrayList<Double>> arrayList = map.get("bxy");
        ArrayList<Double> arrayList1 = arrayList.get(0);
        Double acc = arrayList1.get(0);

        QueryWrapper<Model> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        Model model = modelMapper.selectOne(queryWrapper);
        model.setAccuracy(acc);
        modelMapper.updateById(model);
        map.remove("bxy");

        return map;
        //        return SVM2.makePredict(trainPath, testPath, elect_num, neighbor);
    }

    @Override
    public Map<String, ArrayList<ArrayList<Double>>> DTPredict(Long id, String trainDataSetPath, String testDataSetPath) {
        Map<String, ArrayList<ArrayList<Double>>> map = DT.makePredict(trainDataSetPath, testDataSetPath);
        ArrayList<ArrayList<Double>> arrayList = map.get("bxy");
        ArrayList<Double> arrayList1 = arrayList.get(0);
        Double acc = arrayList1.get(0);

        QueryWrapper<Model> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        Model model = modelMapper.selectOne(queryWrapper);
        model.setAccuracy(acc);
        modelMapper.updateById(model);
        map.remove("bxy");

        return map;
    }

    @Override
    public Map<String, ArrayList<ArrayList<Double>>> BYSPredict(Long id, String trainDataSetPath, String testDataSetPath) {
        Map<String, ArrayList<ArrayList<Double>>> map = BYS.makePredict(trainDataSetPath, testDataSetPath);
        ArrayList<ArrayList<Double>> arrayList = map.get("bxy");
        ArrayList<Double> arrayList1 = arrayList.get(0);
        Double acc = arrayList1.get(0);

        QueryWrapper<Model> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        Model model = modelMapper.selectOne(queryWrapper);
        model.setAccuracy(acc);
        modelMapper.updateById(model);
        map.remove("bxy");

        return map;
    }
}
