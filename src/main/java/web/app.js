import FormController from "./utils/form-controller.js";

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
  // Emulate HTMX here, since HTMX did copy directive usage from AngularJS
  .directive("ngPost", () => ({
      link(_scope, elm) {
        
        /**
         * @type {HTMLFormElement} - The HTML form element being controlled.
         */
        const form = elm[0];
        const controller = new FormController(form);
        elm.on("$destroy", () => {
          controller.destroy();
        })
      }
  }));
