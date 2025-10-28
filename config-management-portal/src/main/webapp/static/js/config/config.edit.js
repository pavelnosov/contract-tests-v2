configModule.config = configModule.config || {};
$.extend(true, configModule.config, {
	edit: {

		init: function () {
			const _this = this;

			document.getElementById('goBackButton').addEventListener('click', function () {
				location.href = this.dataset.href;
			});

			document.getElementById('saveButton').addEventListener('click', function () {
				// if (_this.validateConfig()) {
					_this.saveConfig(this);
				// }
			});

		},

		validateConfig: () => {
			configModule.config.edit.resetErrors();
			let isValid = true;
			isValid &= configModule.config.edit.isNotEmpty('configId', 'Id should not be empty');
			isValid &= configModule.config.edit.isNotEmpty('configName', 'Name should not be empty');
			isValid &= configModule.config.edit.isNotEmpty('configValue', 'Value should not be empty');

			configModule.config.edit.scrollToFirstInvalid();

			return isValid;
		},

		isNotEmpty: (id, errorMessage) => {
			const field = configModule.config.edit.findField(id);
			if (!field.value || !field.value.trim()) {
				const errorParagraph = configModule.config.edit.findErrorParagraph(id);
				configModule.config.edit.showError(field, errorParagraph, errorMessage);
				return false;
			}

			return true;
		},

		resetErrors: function () {
			document.querySelectorAll('p.help.is-danger').forEach(element => {
				element.classList.add('is-hidden');
			});
			document.querySelectorAll('input.is-danger, textarea.is-danger').forEach(element => {
				element.classList.remove('is-danger');
			});
		},

		findField: function(id) {
			return document.getElementById(id);
		},

		findErrorParagraph: function(id) {
			return document.getElementById(`${id}Error`);
		},

		showError: (field, errorParagraph, content) => {
			let textContent;
			if (content.constructor === Array) {
				textContent = content.length === 1 ? content[0] : content.map((message) => '• ' + message).join('\n');
			} else {
				textContent = content;
			}

			errorParagraph.classList.remove('is-hidden');
			errorParagraph.textContent = textContent;
			field.classList.add('is-danger');
		},

		scrollToFirstInvalid: function() {
			const firstErroParagraphOnThePage = document.querySelector('p.is-danger:not(.is-hidden)');

			if (firstErroParagraphOnThePage && typeof firstErroParagraphOnThePage.scrollIntoView === 'function') {
				firstErroParagraphOnThePage.scrollIntoView({behavior: 'smooth', block: 'start', inline: 'nearest'});
			}
		},

		saveConfig: (button) => {
			button.classList.add('is-loading');
			let postData = configModule.config.edit.createPostData();
			$.ajax({
				type: 'POST',
				url: '/config/save',
				data: JSON.stringify(postData),
				dataType: 'json',
				contentType: 'application/json',
			}).done(function (data) {
				button.classList.remove('is-loading');
				if (data.success) {
					location.href = data.redirectUrl;
				} else {
					console.log(data);
					configModule.config.edit.showSaveErrors(data);
				}
			}).fail(function () {
				button.classList.remove('is-loading');
				alert('Error, please try again later');
			});
		},

		createPostData: function () {
			return {
				id: configModule.config.edit.findField('configId').value,
				name: configModule.config.edit.findField('configName').value.trim(),
				value: configModule.config.edit.findField('configValue').value.trim(),
			};
		},

		showSaveErrors: function (data) {
			configModule.config.edit.resetErrors();

			const validatorErrors = configModule.config.edit.normalizeErrors(data.validatorError.errors);
			validatorErrors.forEach((error) => {
				const errorParagraph = configModule.config.edit.findErrorParagraph(error.field);
				if(errorParagraph) {
					const field = configModule.config.edit.findField(error.field);
					configModule.config.edit.showError(field, errorParagraph, error.messages);
				} else {
					alert('Error:\n' + error.message);
				}
			});

			configModule.config.edit.scrollToFirstInvalid();
		},

		normalizeErrors: function(errors) {
			return errors.reduce((acc, nextError) => {
				const error = acc.find((item) => item.field === nextError.field);
				if (!error) {
					nextError.messages = [nextError.message];
					acc.push(nextError);
				} else {
					error.messages.push(nextError.message);
				}
				return acc;
			}, []);
		},

	}
});
