package sx5.model.domain.request;

import lombok.Data;

@Data
public class AlgRequest {

    /**
     * 数据库主键ID
     */
    private Long id;

    /**
     * 训练集路径
     */
    private String trainDataSetPath;

    /**
     * 测试集路径
     */
    private String testDataSetPath;

    /**
     * 学习率
     */
    private Double lr;

    /**
     * 正则化系数
     */
    private Double lambda;

    /**
     * 训练轮数
     */
    private Integer epochNum;

    /**
     * elect_num
     */
    private Integer electNum;

    /**
     * 邻居个数
     */
    private Integer neighbor;
}
