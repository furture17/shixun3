package sx5.service;

import sx5.model.domain.Model;

import java.util.List;

public interface ModelService {
    void saveModel();

    List<Model> getModels(String userAccount);
}
