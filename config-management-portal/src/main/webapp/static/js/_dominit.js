var configModule = configModule || {};

$.extend(true, configModule, {
	init: function () {
		this.config.init();
	}
});

$(document).ready(function () {
	configModule.init();
});
