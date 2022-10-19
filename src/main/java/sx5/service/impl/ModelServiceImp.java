package sx5.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import sx5.mapper.ModelMapper;
import sx5.mapper.UserMapper;
import sx5.model.domain.Model;
import sx5.model.domain.User;
import sx5.model.domain.Userdataset;
import sx5.service.ModelService;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class ModelServiceImp implements ModelService {

    @Resource
    private ModelMapper modelMapper;

    @Resource
    private UserMapper userMapper;


    @Override
    public int saveModel(Model model) {
        int result = modelMapper.insert(model);
        return result;
    }

    @Override
    public List<Model> getModels(String userAccount) {
        QueryWrapper<Model> queryWrapper = new QueryWrapper<>();
        QueryWrapper<User> queryWrapper1 = new QueryWrapper<>();

        queryWrapper.eq("userAccount", userAccount);
        queryWrapper1.eq("userAccount", userAccount);

        User user = userMapper.selectOne(queryWrapper1);
        if (user.getUserRole() == 1) {
            return modelMapper.selectList(null);
        }
        return modelMapper.selectList(queryWrapper);
    }
}
