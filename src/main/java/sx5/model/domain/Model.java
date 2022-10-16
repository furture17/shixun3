package sx5.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

@Data
public class Model implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 算法名称
     */
    private String alg;

    /**
     * 训练集
     */
    private String trainDataset;

    /**
     * 测试集
     */
    private String testDataset;

    /**
     * 模型准确率
     */
    private Double accuracy;
}
