/**
 * Created by muenchdo on 11/06/16.
 */
(function () {
    'use strict';

    angular
        .module('exerciseApplicationApp')
        .component('exerciseList', {
            bindings: {
                course: '<'
            },
            templateUrl: 'app/courses/exercises/exercise-list.html',
            controller: ExerciseListController
        });

    ExerciseListController.$inject = ['$sce', 'AlertService', 'CourseExercises', 'ExerciseParticipation'];

    function ExerciseListController($sce, AlertService, CourseExercises, ExerciseParticipation) {
        var vm = this;

        vm.clonePopover = {
            placement: 'left'
        };
        vm.loading = {};

        vm.$onInit = init;
        vm.getClonePopoverTemplate = getClonePopoverTemplate;
        vm.hasParticipation = hasParticipation;
        vm.start = start;

        function init() {
            CourseExercises.query({courseId: vm.course.id}).$promise.then(function (exercises) {
                angular.forEach(exercises, function (exercise) {
                    exercise['participation'] = ExerciseParticipation.get({
                        courseId: exercise.course.id,
                        exerciseId: exercise.id
                    });
                });
                vm.exercises = exercises;
            });
        }

        var trusted = {};

        function getClonePopoverTemplate(exercise) {
            var html = [
                '<div>',
                '<p>Clone your personal repository for this exercise:</p>',
                '<pre>', exercise.participation.cloneUrl, '</pre>',
                '</div>'
            ].join('');
            return trusted[html] || (trusted[html] = $sce.trustAsHtml(html));
        }

        function hasParticipation(exercise) {
            return !angular.equals({}, exercise.participation.toJSON());
        }

        function start(exercise) {
            vm.loading[exercise.id.toString()] = true;
            exercise.$start({
                courseId: exercise.course.id,
                exerciseId: exercise.id
            }).then(function (returnedExercise) {
                // console.log(participation);
                // TODO: maybe exercise.get()?
                exercise['participation'] = returnedExercise.participation;
                exercise['participation'].toJSON = exercise.toJSON;
            }).catch(function () {
                AlertService.error('The server might be encountering heavy loads. Please try again in a few seconds.');
            }).finally(function () {
                vm.loading[exercise.id.toString()] = false;
            });
        }
    }
})();