import FormController from "./utils/form-controller.js";
import getProductRoutes from "./product/product.js";

window.angular
  .module("app", ["ng.router"])
  .config([
    "$stateProvider",
    "$locationProvider",
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
          templateUrl: route.serverPath,
        });
      });
      getProductRoutes().forEach((route) => {
        $stateProvider.state({
          name: route.name,
          url: route.url,
          templateUrl: route.serverPath,
        });
      });
    },
  ])
  .run([
    "$state",
    "$templateCache",
    "$transitions",
    (stateService, $templateCache, $transitions) => {
      $transitions.onSuccess({}, () => {
        // This clears the cache on all transitions because we are using templateUrl for views
        $templateCache.removeAll();
      });
      window.stateService = stateService;
    },
  ])
  // Emulate HTMX here, since HTMX did copy directive usage from AngularJS and there is nothing that HTMX CAN do that AngularJS can't
  .directive("ngGet", () => ({
    link(_scope, elm) {
      /**
       * @type {HTMLFormElement} - The HTML form element being controlled.
       */
      const form = elm[0];
      const controller = new FormController(form, "get");
      elm.on("$destroy", () => {
        controller.destroy();
      });
    },
  }))
  .directive("ngPost", () => ({
    link(_scope, elm) {
      /**
       * @type {HTMLFormElement} - The HTML form element being controlled.
       */
      const form = elm[0];
      const controller = new FormController(form, "post");
      elm.on("$destroy", () => {
        controller.destroy();
      });
    },
  }))
  .directive("ngPut", () => ({
    link(_scope, elm) {
      /**
       * @type {HTMLFormElement} - The HTML form element being controlled.
       */
      const form = elm[0];
      const controller = new FormController(form, "put");
      elm.on("$destroy", () => {
        controller.destroy();
      });
    },
  }))
  .directive("ngDelete", () => ({
    link(_scope, elm) {
      /**
       * @type {HTMLFormElement} - The HTML form element being controlled.
       */
      const form = elm[0];
      const controller = new FormController(form, "delete");
      elm.on("$destroy", () => {
        controller.destroy();
      });
    },
  }));
