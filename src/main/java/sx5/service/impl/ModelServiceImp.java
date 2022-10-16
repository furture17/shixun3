package sx5.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import sx5.mapper.ModelMapper;
import sx5.model.domain.Model;
import sx5.model.domain.Userdataset;
import sx5.service.ModelService;
import javax.annotation.Resource;
import java.util.List;

@Service
public class ModelServiceImp implements ModelService {

    @Resource
    private ModelMapper modelMapper;


    @Override
    public void saveModel() {
        Model model = new Model();
        modelMapper.insert(model);
    }

    @Override
    public List<Model> getModels(String userAccount) {
        QueryWrapper<Model> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);

        List<Model> models = modelMapper.selectList(queryWrapper);
        return models;
    }
}
