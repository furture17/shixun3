package sx5.model.domain.request;

import lombok.Data;

@Data
public class AlgRequest {

    /**
     * 训练集路径
     */
    private String trainDataSetPath;

    /**
     * 测试集路径
     */
    private String testDataSetPath;
}
