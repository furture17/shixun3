package sx5.service;

import sx5.model.domain.Model;

import java.util.List;

public interface ModelService {
    int saveModel(Model model);

    List<Model> getModels(String userAccount);
}
