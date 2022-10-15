package sx5.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.stereotype.Service;
import sx5.mapper.UserDatasetMapper;
import sx5.model.domain.Userdataset;
import sx5.service.FileService;
import javax.annotation.Resource;

@Service
public class FileServiceImp implements FileService {

    @Resource
    private UserDatasetMapper userDatasetMapper;

    @Override
    public Userdataset getFiles(String userAccount) {
        QueryWrapper<Userdataset> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);

        Userdataset userdataset = userDatasetMapper.selectOne(queryWrapper);
        return userdataset;
    }

    @Override
    public void updateDsInfo(String userAccount, String fileName) {
        QueryWrapper<Userdataset> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);

        Userdataset userdataset = userDatasetMapper.selectOne(queryWrapper);

        //若用户第一次上传数据集
        if (userdataset == null) {
            Userdataset newUserdataset = new Userdataset();
            newUserdataset.setUserAccount(userAccount);
            userdataset = newUserdataset;
            userDatasetMapper.insert(newUserdataset);
        }

        UpdateWrapper<Userdataset> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("userAccount", userAccount);
        switch (fileName) {
            case "PDE.csv":
                userdataset.setPDE(1);
                break;
            case "JDT.csv":
                userdataset.setJDT(1);
                break;
            case "Lucene.csv":
                userdataset.setLucene(1);
                break;
        }
        userDatasetMapper.update(userdataset, updateWrapper);
    }
}
