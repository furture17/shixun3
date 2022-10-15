package sx5.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sx5.common.BaseResponse;
import sx5.common.ResultUtils;
import sx5.model.domain.Userdataset;
import sx5.service.FileService;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;

@RestController
@CrossOrigin
@RequestMapping("/file")
@Slf4j
public class FileController {

    @Resource
    private FileService fileService;
    /**
     * RequestParam的参数值为前端FormData对象的存储内容的key值
     * 多文件上传时，用MultipartFile[]数组进行接收，单文件也可以这样接收
     * @param files 文件数组
     * @param req Http请求
     * @return TODO 暂无
     */
    @PostMapping("/fileUp")
    public String fileUp(@RequestParam("file") MultipartFile[] files, @RequestParam("userAccount") String userAccount, HttpServletRequest req) {
        System.out.println("接收到的文件有"+files.length+"个");
        String name = "";
        log.info(userAccount);

        for(MultipartFile file:files){
            System.out.println("正在存储"+file.getOriginalFilename()+"文件");
            String savePath =  new ApplicationHome(this.getClass()).getDir().getParentFile()
                    .getParentFile().getAbsolutePath() + "\\src\\main\\resources\\data";;

            name=file.getOriginalFilename();
            File folder=new File(savePath);
            if(!folder.isDirectory())
                folder.mkdirs();
            try{
                assert name != null;
                file.transferTo(new File(folder,name));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        log.info("文件名为:" + name);
        //TODO 这里可以改成多个
        fileService.updateDsInfo(userAccount, name);

        return "上传成功";
    }

    @GetMapping("/getFiles")
    public BaseResponse<Userdataset> getFiles(@RequestParam("userAccount") String userAccount) {
        Userdataset userdataset = fileService.getFiles(userAccount);
        return ResultUtils.success(userdataset);
    }
}
