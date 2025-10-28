package by.pano.contracttests.client;

import java.util.List;


public interface ConfigManagementClient {

	ConfigDTO getConfigById(String id);

	List<ConfigDTO> getConfigs();

	void delete(String id);

	ConfigDTO create(ConfigDTO config);

	ConfigDTO update(ConfigDTO config);
}
