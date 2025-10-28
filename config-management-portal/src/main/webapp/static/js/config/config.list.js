configModule.config = configModule.config || {};
$.extend(true, configModule.config, {
	list: {

		init: function () {
			const _this = this;

			(document.querySelectorAll('.modal-background, .modal-close, .modal-card-head .delete, .modal-card-foot .button') || [])
				.forEach(($close) => {
					const $target = $close.closest('.modal');
					$close.addEventListener('click', () => {
						$target.classList.remove('is-active');
					});
				});


			document.querySelectorAll('#configTable tr .deleteConfigButton')
				.forEach(function (button) {
					button.addEventListener('click', function (event) {
						event.preventDefault();
						_this.openDeleteConfigModal(button);
					});
				});

			document.querySelector('#deleteModal .confirmDelete').addEventListener('click', (event) => {
				event.preventDefault();
				let confirmDeleteButton = event.currentTarget;
				confirmDeleteButton.classList.add('is-loading');
				_this.deleteConfig(confirmDeleteButton.getAttribute('href'));
			});

		},

		deleteConfig: function (href) {
			fetch(href, {
				method: 'DELETE',
				headers: {
					'Content-Type': 'application/json',
				},
			})
				.then(response => response.json())
				.then(data => {
					document.querySelector('#deleteModal .confirmDelete').classList.remove('is-loading');
					if (data.success) {
						let configId = document.querySelector('#deleteModal .configId').textContent;
						document.querySelector(`tr#config_${configId}`).remove();
					} else {
						alert('Can not delete config');
					}
				})
				.catch((error) => {
					console.error('Error:', error);
					document.querySelector('#deleteModal .confirmDelete').classList.remove('is-loading');
					alert('Can not delete config');
				});
		},

		openDeleteConfigModal: function (button) {
			const configRow = button.closest('tr');
			const deleteModal = document.getElementById('deleteModal');
			const configId = configRow.querySelector('.configId').textContent;
			const configName = configRow.querySelector('.configName').textContent;
			deleteModal.querySelector('.configId').textContent = configId;
			deleteModal.querySelector('.configName').textContent = configName;
			deleteModal.querySelector('.confirmDelete').href = `/config/delete/${configId}`;
			deleteModal.classList.add('is-active');
		},
	}
});
