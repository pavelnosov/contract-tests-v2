configModule = configModule || {};
$.extend(true, configModule, {
	config: {
		init: function () {

			if ($('#configEditPage').length > 0) {
				this.edit.init();
			}
			if ($('#configListPage').length > 0) {
				this.list.init();
			}
		},
	}
});
