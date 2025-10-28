module.exports = function (grunt) {
	const sass = require('dart-sass');
	const os = require('os');
	const path = require('path');


	grunt.log.writeln('============================================================');
	grunt.log.writeln('============= CONFIG MANAGEMENT PORTAL BUILD ===============');
	grunt.log.writeln('============================================================');


	grunt.initConfig({
		system: grunt.file.readJSON('grunt_config.json'),

		clean: {
			generated: {
				options: {
					force: true
				},
				src: ['../../resources/_generated']
			}
		},

		watch: {
			watchAssetsFolder: {
				files: ['../static/**/*', '../../resources/templates/**'],
				tasks: ['build_watch'],
				options: {
					interrupt: true
				}
			}
		},

		sass: {
			options: {
				implementation: sass,
				sourceMap: true,
				outputStyle: 'compressed'
			},
			combined: {
				src: '../static/scss/combined.scss',
				dest: '../../resources/_generated/css/combined.css'
			}
		},

		jshint: {
			allNewFiles: {
				options: {
					jshintrc: '.jshintrc'
				},
				src: [
					'../static/js/*.js',
					'../static/js/config/*.js',
					'!../static/js/3rd_party/*.js'
				]
			}
		},

		concat: {
			options: {
				banner: 'Generated: /*! <%= grunt.template.today("yyyy-mm-dd HH:mm:ss") %>*/\n'
			},

			combined: {
				nonull: true,
				dest: '../../resources/_generated/js/combined.js', //ggf .es6.js
				src: [
					'node_modules/jquery/dist/jquery.js',
					'node_modules/datatables.net/js/jquery.dataTables.js',
					'../static/js/*.js',
					'../static/js/config/*.js',
				]
			}
		},

		copy: {
			img2generated: {
				files: [
					{
						cwd: '../static/img/',
						src: '**/*',
						dest: '../../resources/_generated/img/',
						expand: true,
						filter: 'isFile'
					}
				]
			},

			webfonts2generated: {
				files: [
					{
						cwd: '../static/webfonts/',
						src: '**/*',
						dest: '../../resources/_generated/webfonts/',
						expand: true,
						filter: 'isFile'
					}
				]
			},

			htmlWatcher2generated: {
				files: [
					{
						cwd: '../../resources/templates/',
						src: '**/*',
						dest: '../../../../out/production/resources/templates/',
						expand: true,
						filter: 'isFile'
					}
				]
			},

			staticWatcher2generated: {
				files: [
					{
						cwd: '../../resources/_generated/',
						src: '**/*',
						dest: '../../../../out/production/resources/_generated/',
						expand: true,
						filter: 'isFile'
					}
				]
			},

			htmlWatcher2Gradle: {
				files: [
					{
						cwd: '../../resources/templates/',
						src: '**/*',
						dest: '../../../../build/resources/main/templates/',
						expand: true,
						filter: 'isFile'
					}
				]
			},

			staticWatcher2Gradle: {
				files: [
					{
						cwd: '../../resources/_generated/',
						src: '**/*',
						dest: '../../../../build/resources/main/_generated/',
						expand: true,
						filter: 'isFile'
					}
				]
			},
		},
	});


	grunt.loadNpmTasks('grunt-contrib-clean');
	grunt.loadNpmTasks('grunt-sass');
	grunt.loadNpmTasks('grunt-contrib-jshint');
	grunt.loadNpmTasks('grunt-contrib-concat');
	grunt.loadNpmTasks('grunt-contrib-copy');
	grunt.loadNpmTasks('grunt-contrib-watch');

	grunt.registerTask('build_full', ['clean', 'sass', 'jshint', 'concat', 'copy:img2generated', 'copy:webfonts2generated']);
	grunt.registerTask('build_watch', ['sass', 'jshint', 'concat', 'copy:img2generated', 'copy:webfonts2generated', 'copy:htmlWatcher2generated', 'copy:staticWatcher2generated', 'copy:htmlWatcher2Gradle', 'copy:staticWatcher2Gradle']);
	grunt.registerTask('build', ['build_full']);
	grunt.registerTask('dev', ['clean', 'build_watch', 'watch']);
	grunt.registerTask('default', ['build']);
};
