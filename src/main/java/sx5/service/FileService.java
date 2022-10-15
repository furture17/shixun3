package sx5.service;

import sx5.model.domain.Userdataset;


public interface FileService {
    Userdataset getFiles(String userAccount);

    void updateDsInfo(String userAccount, String fileName);
}
