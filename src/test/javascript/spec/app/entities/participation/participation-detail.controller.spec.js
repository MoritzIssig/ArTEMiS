'use strict';

describe('Controller Tests', function() {

    describe('Participation Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockParticipation, MockUser, MockResult, MockExercise;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockParticipation = jasmine.createSpy('MockParticipation');
            MockUser = jasmine.createSpy('MockUser');
            MockResult = jasmine.createSpy('MockResult');
            MockExercise = jasmine.createSpy('MockExercise');


            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Participation': MockParticipation,
                'User': MockUser,
                'Result': MockResult,
                'Exercise': MockExercise
            };
            createController = function() {
                $injector.get('$controller')("ParticipationDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'artemisApp:participationUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
