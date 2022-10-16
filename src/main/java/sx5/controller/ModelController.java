package sx5.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sx5.common.BaseResponse;
import sx5.common.ResultUtils;
import sx5.model.domain.Model;
import sx5.service.ModelService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/model")
@Slf4j
public class ModelController {

    @Resource
    private ModelService modelService;

    @GetMapping("/getModels")
    public BaseResponse<List<Model>> getModels(@RequestParam("userAccount") String userAccount) {
        List<Model> models = modelService.getModels(userAccount);
        return ResultUtils.success(models);
    }

    @PostMapping("/saveModel")
    public String saveModel() {
        modelService.saveModel();
        return "aa";
    }

}
