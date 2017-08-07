(function() {
    'use strict';

    angular
        .module('artemisApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('result', {
            parent: 'entity',
            url: '/result',
            contentContainerClass: 'container-fluid',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'artemisApp.result.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/result/results.html',
                    controller: 'ResultController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('result');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('result-detail', {
            parent: 'entity',
            url: '/result/{id}',
            data: {
                authorities: ['ROLE_ADMIN', 'ROLE_TA'],
                pageTitle: 'artemisApp.result.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/result/result-detail.html',
                    controller: 'ResultDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('result');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Result', function($stateParams, Result) {
                    return Result.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('result.new', {
            parent: 'result',
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN', 'ROLE_TA']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/result/result-dialog.html',
                    controller: 'ResultDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                resultString: null,
                                buildCompletionDate: null,
                                buildSuccessful: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('result', null, { reload: true });
                }, function() {
                    $state.go('result');
                });
            }]
        })
        .state('result.edit', {
            parent: 'result',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_ADMIN', 'ROLE_TA']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/result/result-dialog.html',
                    controller: 'ResultDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Result', function(Result) {
                            return Result.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('result', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('result.delete', {
            parent: 'result',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_ADMIN', 'ROLE_TA']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/result/result-delete-dialog.html',
                    controller: 'ResultDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Result', function(Result) {
                            return Result.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('result', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
