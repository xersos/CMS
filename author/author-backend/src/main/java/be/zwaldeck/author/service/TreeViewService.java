package be.zwaldeck.author.service;

import be.zwaldeck.author.json.response.TreeItemResponse;
import be.zwaldeck.zcms.repository.api.exception.BrakingRepositoryException;
import be.zwaldeck.zcms.repository.api.model.Site;

public interface TreeViewService {
    TreeItemResponse getTreeViewBySite(Site site) throws BrakingRepositoryException;
}
