window.angular.module("app", ["ng.router"])
  .config([
    "$stateProvider", "$locationProvider",
    ($stateProvider, $locationProvider) => {
      // Enable html5mode
      $locationProvider.hashPrefix("").html5Mode({
        enabled: true,
        requireBase: false,
        rewriteLinks: false,
      });
      window.routes.forEach((route) => {
        $stateProvider.state({
          name: route.name,
          url: route.url,
          templateUrl: route.serverPath
        })
      })
      
    }
  ])
