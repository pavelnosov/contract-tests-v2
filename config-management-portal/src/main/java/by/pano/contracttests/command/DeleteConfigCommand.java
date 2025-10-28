package by.pano.contracttests.command;

import by.pano.contracttests.client.ConfigManagementClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class DeleteConfigCommand {

	private final ConfigManagementClient configManagementClient;

	public void execute(String id) {
		configManagementClient.delete(id);
	}
}
