package sx5.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

@Data
public class Userdataset implements Serializable {

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
     * 是否上传过PDE
     */
    private Integer PDE;


    /**
     * 是否上传过JDT
     */
    private Integer JDT;


    /**
     * 是否上传过Lucene
     */
    private Integer Lucene;
}
