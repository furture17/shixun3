package sx5.alg;

import com.csvreader.CsvReader;
import lombok.val;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class LogisticRegression {

    //270行数据会造成y特别大，从而e之后是无穷
    //解决方案：标准化

    //优化，加正则项

    public static Map<ArrayList<Double>, Integer> mapBeforeProcess = new LinkedHashMap<>();
    public static Map<ArrayList<Double>, Integer> map = new LinkedHashMap<>();

//    public static ArrayList<Double> featureList = new ArrayList<>();
    public static ArrayList<Double> params = new ArrayList<>();
    public static ArrayList<Double> predis = new ArrayList<>();
//    public static double lr = 0.0001;//0.7939086294416243
    public static double lr = 0.001;//
    public static int epochNum = 10000;
    public static Double costPre = Double.MAX_VALUE;


    public static void main(String[] args) throws IOException {
        //TODO：62可以不写死
        for (int i = 0; i < 62; i++) {//第一个即为b
            params.add(0.0);
        }

        //1. 读文件，特征存储到ArrayList中，类别存储为0/1,整体为Map，最后只有985条数据
        readFile();
        dataProcess();

        for(int i = 0; i < map.size(); i++) {
            predis.add(0.0);
        }

//        System.out.println(map.size());

        for (int epoch = 0; epoch < epochNum; epoch++) {
            //2. 构建模型函数，得到预测值
            Iterator<ArrayList<Double>> iterator = map.keySet().iterator();
            for (int i = 0; i < map.size(); i++) {
                double pred = model(iterator.next());
//                System.out.println("第" + i + "行的预测为:" + pred);
                predis.set(i, pred);
            }
//            System.out.println("===============");
//            System.out.println(predis);

//            System.out.println("aksdjfas");
//
//            if (epoch == 1) {
//                val iterator1 = map.keySet().iterator();
//                for (int i = 0; i < 270; i++) {
//                    iterator1.next();
//                }
//                model(iterator1.next());
//                System.out.println(predis.get(270));
//            }

            //3. 代价函数
            double cost = costFunction();
            System.out.println("==============");
            System.out.println("本轮损失为：");
            System.out.println(cost);
            if (cost > costPre) {
                predict();
                return;
            }
            costPre = cost;

            //4. 梯度下降
            gradDesc();

//            //查看当前参数与损失
//            System.out.println("w为：");
//            System.out.println(params);
        }

//        predict();
    }

    private static void readFile() throws IOException {
        CsvReader csvReader = new CsvReader("D:\\VI\\University\\大三\\大三上\\实训\\软件缺陷数据集及相关说明材料\\AEEEM\\csv\\PDE.csv", ',', StandardCharsets.UTF_8);
        csvReader.readHeaders();    //过滤表头

        while (csvReader.readRecord()) {
            ArrayList<Double> featureList = new ArrayList<>();
            int type = 0;
            for (int i = 0; i < 61; i ++) {
                featureList.add(Double.parseDouble(csvReader.get(i)));
//                System.out.print(csvReader.get(i) + " ");
            }

            if (csvReader.get(61).equals("clean")) {
//                System.out.println(csvReader.get(61));
                type = 1;
            }
            mapBeforeProcess.put(featureList,type);
        }
        csvReader.close();
    }

    private static void dataProcess() {
        int featureAmount = params.size() - 1; //不包含b/w0/x0
        ArrayList<Double> feaAvg = new ArrayList<>(featureAmount); // 平均值
        ArrayList<Double> varList = new ArrayList<>(featureAmount);

        Iterator<Integer> labelsIt = mapBeforeProcess.values().iterator();
        Iterator<ArrayList<Double>> keyIt = mapBeforeProcess.keySet().iterator();

        //初始化平均值
        for (int i = 0; i < featureAmount; i++) {
            feaAvg.add(0.0);
        }

        //计算平均值
        while (keyIt.hasNext()) {
            ArrayList<Double> list = keyIt.next();
            for (int i = 0; i < featureAmount; i++) {
                feaAvg.set(i, feaAvg.get(i) + list.get(i));
            }
        }
        for (int i = 0; i < featureAmount; i++) {
            feaAvg.set(i, feaAvg.get(i) / mapBeforeProcess.size());
        }

        //初始化标准差
        for (int i = 0; i < featureAmount; i++) {
            varList.add(0.0);
        }

        //计算标准差
        keyIt = mapBeforeProcess.keySet().iterator();
        while (keyIt.hasNext()) {
            ArrayList<Double> list = keyIt.next();
            for (int i = 0; i < featureAmount; i++) {
                varList.set(i, varList.get(i) + Math.pow(list.get(i) - feaAvg.get(i), 2));
            }
        }
        for (int i = 0; i < featureAmount; i++) {
            varList.set(i, Math.sqrt(varList.get(i) / mapBeforeProcess.size()));
        }

        keyIt = mapBeforeProcess.keySet().iterator();
        while (keyIt.hasNext()) {
            ArrayList<Double> list = keyIt.next();
            for (int i = 0; i < featureAmount; i++) {
                list.set(i, (list.get(i) - feaAvg.get(i)) / varList.get(i));
            }
            map.put(list, labelsIt.next());
        }
    }

    private static double model(ArrayList<Double> featureList) {
        double y = 0.0;
        y += params.get(0);
        for (int i = 1; i <= featureList.size(); i++) {
            y += params.get(i) * featureList.get(i - 1);
        }
        y = Math.exp(-y);
        if (y > Double.MAX_VALUE) y = Integer.MAX_VALUE;

        y = 1 / (1 + y);
        return y;
    }

    private static double costFunction() {
         double cost = 0.0;
        int i = 0;
        for (Integer label : map.values()) {
//            if ((predis.get(i) == 1 && label == 0) || (predis.get(i) == 0&& label == 1)) {
//                cost += Integer.MAX_VALUE;
//            } else {
//                cost += -1 * label * Math.log(predis.get(i)) - (1 - label) * Math.log(1 - predis.get(i));
//            }
            cost += -1 * label * Math.log(predis.get(i)) - (1 - label) * Math.log(1 - predis.get(i));
            i++;
        }
        cost /= predis.size();
        return cost;
    }

    private static void gradDesc() {
        int amount = predis.size();
        int varAmount = params.size();

        //计算b
        double var = 0.0;
        Iterator<Integer> iterator = map.values().iterator();
        for (Double predi : predis) {
            var += predi - iterator.next();
        }
        var /= amount;

        //更新b
        params.set(0, params.get(0) -  lr * var);

        //计算w
        ArrayList<Double> med = new ArrayList<>(varAmount - 1);
        iterator = map.values().iterator();
        Iterator<ArrayList<Double>> keyIterator = map.keySet().iterator();

        for (int i = 0; i < amount; i++) {
            double label = iterator.next();
            ArrayList<Double> features = keyIterator.next();
            for (int j = 0; j < varAmount - 1; j++) {
                if (i == 0)
                    med.add((predis.get(j) - label) * features.get(j));
                else
                    med.set(j, med.get(j) + (predis.get(j) - label) * features.get(j));
            }
        }

        //更新w
        for (int i = 1; i < varAmount; i++) {
            params.set(i, params.get(i) - lr * (med.get(i - 1)) / amount);
        }
    }

    private static void predict() {
        int amount = map.size();
        Iterator<ArrayList<Double>> iterator = map.keySet().iterator();
        Iterator<Integer> labelsIt = map.values().iterator();
        int accurateNum = 0;

        while (iterator.hasNext()) {
            ArrayList<Double> vars = iterator.next();
            double labelPre = 0.0;

            labelPre += params.get(0);
            for (int i = 1; i <= vars.size(); i++) {
                labelPre += params.get(i) * vars.get(i - 1);
                labelPre= Math.exp(-labelPre);
                labelPre = 1 / (1 + labelPre);
            }
            int trueLabel = labelsIt.next();
            if (Math.abs(trueLabel - labelPre) < 0.5) accurateNum++;
            System.out.println("labelPre: " + labelPre);
        }
        System.out.println("accuracy:" + (accurateNum + 0.0) / amount);

        System.out.println("------------");
        System.out.println(params);
    }
}
